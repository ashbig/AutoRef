/*
 * EnterResultController.java
 *
 * Created on December 7, 2007, 11:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import core.Fileresult;
import dao.ContainerDAO;
import dao.DaoException;
import dao.FilereferenceDAO;
import io.FileRepository;
import io.ResultFileParser;
import io.StaticResultFileParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import process.ResultManager;
import transfer.ContainerheaderTO;
import transfer.FilereferenceTO;
import transfer.ProcessexecutionTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.ResultTO;
import transfer.SampleTO;
import transfer.SamplepropertyTO;
import util.Constants;

/**
 *
 * @author dzuo
 */
public class EnterResultController extends ProcessController implements Serializable {
    private static final String SEPARATOR = "_";
    
    private List<String> labels;
    private List<String> foundLabels;
    private List<String> nofoundLabels;
    private List<String> filenames;
    private List<String> foundFilenames;
    
    private Collection<ContainerheaderTO> containers;
    private List<FilereferenceTO> filerefs;
    private String resulttype;
    private ProcessexecutionTO processexecution;
    
    /** Creates a new instance of EnterResultController */
    public EnterResultController() {
    }
    
    public void readFiles() throws ControllerException {
        setFilenames(new ArrayList<String>());
        try {
            File dir = new File(Constants.DIR_RESULT_FILE);
            File[] files = dir.listFiles();
            for(int i=0; i<files.length; i++) {
                File f = files[i];
                getFilenames().add(f.getName());
            }
        } catch (Exception ex) {
            throw new ControllerException("Error reading files."+ex.getMessage());
        }
    }
    
    @Override
    public void doProcess() throws ControllerException {
        super.doProcess();
        setProcessexecution(getPe());
    }
    
    @Override
    public void persistProcess() throws ControllerException {
        setPe(getProcessexecution());
        super.persistProcess();
    }
    
    public String findFilename(String label) {
        for(String filename:filenames) {
            int index = filename.indexOf(getSEPARATOR());
            String labelInFile = filename;
            if(index > 0)
                labelInFile = filename.substring(0, index);
            if(labelInFile.equals(label))
                return filename;
        }
        return null;
    }
    
    public void processFilenames() {
        String found = null;
        setFoundLabels(new ArrayList<String>());
        setFoundFilenames(new ArrayList<String>());
        setNofoundLabels(new ArrayList<String>());
        
        for(String label:labels) {
            if((found = findFilename(label)) != null) {
                getFoundLabels().add(label);
                getFoundFilenames().add(found);
            } else {
                getNofoundLabels().add(label);
            }
        }
    }
    
    public void retrieveContainers() throws ControllerException {
        try {
            setContainers(ContainerDAO.checkContainers(getFoundLabels(), ContainerheaderTO.getSTATUS_GOOD(), false, true, false));
            
            for(String filename:foundFilenames) {
                String labelInFile = getLabelInFilename(filename);
                ContainerheaderTO container = findContainer(filename);
                if(container == null) {
                    getFoundLabels().remove(labelInFile);
                    getFoundFilenames().remove(filename);
                    getNofoundLabels().add(labelInFile);
                }
            }
        } catch (DaoException ex) {
            throw new ControllerException(ex.getMessage());
        }
    }
    
    public String getLabelInFilename(String filename) {
        String labelInFile = filename;
        int index = filename.indexOf(getSEPARATOR());
        if(index > 0) {
            labelInFile = filename.substring(0, index);
        }
        return labelInFile;
    }
    
    public ContainerheaderTO findContainer(String filename) {
        String labelInFile = getLabelInFilename(filename);
        for(ContainerheaderTO container:getContainers()) {
            if(labelInFile.equals(container.getBarcode()))
                return container;
        }
        return null;
    }
    
    public void setProcessprotocol(String resulttype) {
        if(ResultTO.TYPE_CULTURE.equals(resulttype)) {
            getPe().setProtocol(new ProcessprotocolTO(ProcessprotocolTO.CULTURE_RESULT, null, null));
        }
        
        if(ResultTO.TYPE_DNA.equals(resulttype)) {
            getPe().setProtocol(new ProcessprotocolTO(ProcessprotocolTO.DNA_RESULT, null, null));
        }
    }
    
    public void doSpecificProcess() throws ControllerException {
        ResultFileParser parser = StaticResultFileParserFactory.getResultFileParser(getResulttype());
        if(parser == null)
            throw new ControllerException("Invalid result type");
        
        ResultManager manager = new ResultManager();
        filerefs = new ArrayList<FilereferenceTO>();
        try {
            for(String filename:foundFilenames) {
                ContainerheaderTO container = findContainer(filename);
                if(container == null)
                    throw new ControllerException("Cannot find container for file: "+filename);
                
                InputStream input = new FileInputStream(Constants.DIR_RESULT_FILE+filename);
                List<Fileresult> results = parser.parseFile(input);
                manager.populateResultsForContainer(container,results, getResulttype());
                
                FilereferenceTO fileref = new FilereferenceTO(filename, FilereferenceTO.PATH, FilereferenceTO.TYPE_RESULT);
                FileRepository.uploadFile(fileref, Constants.DIR_RESULT_FILE+filename);
                fileref.setObjecttype(ProcessobjectTO.getTYPE_FILEREFERENCE());
                fileref.setIoflag(ProcessobjectTO.getIO_INPUT());
                getPe().addProcessobject(fileref);
                filerefs.add(fileref);
            }
            
            List<ResultTO> results = new ArrayList<ResultTO>();
            for(ContainerheaderTO c:getContainers()) {
                Collection<SampleTO> samples = c.getSamples();
                for(SampleTO s:samples) {
                    Collection<ResultTO> rs = s.getResults();
                    results.addAll(rs);
                }
            }
            getPe().setResults(results);
            setProcessprotocol(getResulttype());
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }
    
    public void persistSpecificProcess(Connection conn) throws ControllerException {
        try {
            FilereferenceDAO dao = new FilereferenceDAO(conn);
            dao.addFilereferences(filerefs);
            
            List<SamplepropertyTO> properties = new ArrayList<SamplepropertyTO>();
            for(ContainerheaderTO c:getContainers()) {
                Collection<SampleTO> samples = c.getSamples();
                for(SampleTO s:samples) {
                    Collection<SamplepropertyTO> ps = s.getProperties();
                    for(SamplepropertyTO p:ps) {
                        if(p.isIsnew())
                            properties.add(p);
                    }
                }
            }
            ContainerDAO dao1 = new ContainerDAO(conn);
            dao1.addSampleproperties(properties);
        } catch (DaoException ex) {
            throw new ControllerException("DaoException: "+ex.getMessage());
        }
    }
    
    public static void main(String args[]) {
        ResearcherTO researcher = new ResearcherTO("dzuo", null, null, null, "dzuo");
        String resulttype = ResultTO.TYPE_CULTURE;
        List<String> labels = new ArrayList();
        labels.add("HsxXG002542");
        
        System.out.println("EnterResultController:");
        EnterResultController c = new EnterResultController();
        
        c.setWho(researcher);
        c.setResulttype(resulttype);
        c.setLabels(labels);
        
        try {
            c.readFiles();
            c.processFilenames();
            
            if(c.getNofoundLabels().size()>0) {
                System.out.println("We cannot find files for the following labels: "+c.getNofoundLabels());
            }
            
            c.retrieveContainers();
            
            System.out.println("EnterResultController:1");
            c.doProcess();
            System.out.println("EnterResultController:2");
            c.persistProcess();
            System.out.println("EnterResultController:4");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static String getSEPARATOR() {
        return SEPARATOR;
    }
    
    public List<String> getLabels() {
        return labels;
    }
    
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    
    public List<String> getFoundLabels() {
        return foundLabels;
    }
    
    public void setFoundLabels(List<String> foundLabels) {
        this.foundLabels = foundLabels;
    }
    
    public List<String> getFilenames() {
        return filenames;
    }
    
    public void setFilenames(List<String> filenames) {
        this.filenames = filenames;
    }
    
    public List<String> getFoundFilenames() {
        return foundFilenames;
    }
    
    public void setFoundFilenames(List<String> foundFilenames) {
        this.foundFilenames = foundFilenames;
    }
    
    public void setContainers(Collection<ContainerheaderTO> containers) {
        this.containers = containers;
    }
    
    public String getResulttype() {
        return resulttype;
    }
    
    public void setResulttype(String resulttype) {
        this.resulttype = resulttype;
    }
    
    public Collection<ContainerheaderTO> getContainers() {
        return containers;
    }
    
    public List<FilereferenceTO> getFilerefs() {
        return filerefs;
    }
    
    public void setFilerefs(List<FilereferenceTO> filerefs) {
        this.filerefs = filerefs;
    }
    
    public List<String> getNofoundLabels() {
        return nofoundLabels;
    }
    
    public void setNofoundLabels(List<String> nofoundLabels) {
        this.nofoundLabels = nofoundLabels;
    }

    public ProcessexecutionTO getProcessexecution() {
        return processexecution;
    }

    public void setProcessexecution(ProcessexecutionTO processexecution) {
        this.processexecution = processexecution;
    }
}
