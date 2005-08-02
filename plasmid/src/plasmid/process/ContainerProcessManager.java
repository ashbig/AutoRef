/*
 * ContainerProcessManager.java
 *
 * Created on May 24, 2005, 3:15 PM
 */

package plasmid.process;

import java.util.*;
import java.sql.*;
import java.io.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.util.PlatePositionConvertor;

/**
 *
 * @author  DZuo
 */
public class ContainerProcessManager {
    public static final String DELIM = ",";
    public static final String NOTUBE = TubeMap.NOTUBE;
    public static final String FILEPATH = "G:\\plasmid\\test\\";
    
    /** Creates a new instance of ContainerProcessManager */
    public ContainerProcessManager() {
    }
    
    public List getContainers(List labels, boolean isSampleRestore) {
        if(labels == null)
            return null;
        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            PlateManager manager = new PlateManager(conn);
            List containers = manager.queryContainers(labels, isSampleRestore);
            
            return containers;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getNofoundContainers(List labels, List containers) {
        List notFound = new ArrayList();
        
        if(labels == null)
            return notFound;
        if(containers == null)
            return labels;
        
        for(int i=0; i<labels.size(); i++) {
            String label = (String)labels.get(i);
            if(!found(label, containers)) {
                notFound.add(label);
            }
        }
        return notFound;
    }
    
    public List getLabels(int n, String seqname) {
        List labels = new ArrayList();
        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return null;
        }
        
        DefTableManager manager = new DefTableManager();
        for(int i=0; i<n; i++) {
            int id = manager.getNextid(seqname, t);
            if(id == -1) {
                if(Constants.DEBUG) {
                    System.out.println("Error occured while querying database for id.");
                    return null;
                }
            }
            labels.add(new Integer(id));
            
        }
        return labels;
    }
    
    public boolean found(String label, List containers) {
        if(containers == null || label == null)
            return false;
        
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            if(label.equals(c.getLabel())) {
                return true;
            }
        }
        
        return false;
    }
    
    public TubeMap readTubeMappingFile(String filename) {
        BufferedReader in = null;
        TubeMap m = null;
        try {
            in = new BufferedReader(new FileReader(filename));
            String barcode = in.readLine();
            if(barcode == null)
                throw new Exception("No barcode was specified.");
            
            String line = null;
            Map mapping = new HashMap();
            PlatePositionConvertor convertor = new PlatePositionConvertor();
            while((line = in.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, DELIM);
                
                String well = tokenizer.nextToken();
                int position = convertor.convertWellFromA8_12toInt(well);
                
                String label = tokenizer.nextToken();
                if(NOTUBE.equals(label))
                    label = null;
                
                mapping.put((new Integer(position)).toString(), label);
            }
            m = new TubeMap(barcode, mapping);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot read mapping file.");
                System.out.println(ex);
            }
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                if(Constants.DEBUG) {
                    System.out.println(ex);
                }
            }
        }
        
        return m;
    }
    
    public boolean setContaineridAndLabels(List containers, String suffix) {
        if(containers == null || containers.size() == 0)
            return true;
        
        if(setContainerids(containers)) {
            for(int i=0; i<containers.size(); i++) {
                Container c = (Container)containers.get(i);
                int id = c.getContainerid();
                java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
                fmt.setMaximumIntegerDigits(8);
                fmt.setMinimumIntegerDigits(8);
                fmt.setGroupingUsed(false);
                String label = fmt.format(id);
                if(suffix != null && suffix.trim().length()>0) {
                    label = label + "-" + suffix.trim();
                }
                c.setLabel(label);
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    public boolean setContainerids(List containers) {
        if(containers == null || containers.size() == 0)
            return true;
        
        DefTableManager m = new DefTableManager();
        int containerid = m.getMaxNumber("containerheader", "containerid");
        if(containerid < 0) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get containerid from database.");
                System.out.println(m.getErrorMessage());
            }
            return false;
        }
        
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            c.setContainerid(containerid);
            List samples = c.getSamples();
            if(samples != null) {
                for(int k=0; k<samples.size(); k++) {
                    Sample s = (Sample)samples.get(i);
                    s.setContainerid(containerid);
                }
            }
            containerid++;
        }
        
        return true;
    }
    
    public boolean setSampleids(List samples) {
        if(samples == null || samples.size() == 0)
            return true;
        
        DefTableManager m = new DefTableManager();
        int sampleid = m.getMaxNumber("sample", "sampleid");
        if(sampleid < 0) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get sampleid from database.");
                System.out.println(m.getErrorMessage());
            }
            return false;
        }
        
        for(int i=0; i<samples.size(); i++) {
            Sample c = (Sample)samples.get(i);
            c.setSampleid(sampleid);
            sampleid++;
        }
        
        return true;
    }
    
    public boolean persistData(List containers, ProcessExecution process) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return false;
        }
        
        PlateManager manager = new PlateManager(conn);
        if(!manager.insertPlateAndSample(containers)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot insert containers.");
                System.out.println(manager.getErrorMessage());
            }
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
        
        if(process != null) {
            ProcessManager m = new ProcessManager(conn);
            if(!m.insertProcess(process)) {
                if(Constants.DEBUG) {
                    System.out.println("Cannot insert process.");
                    System.out.println(m.getErrorMessage());
                }
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                return false;
            }
        }
        
        DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
        return true;
    }
    
    public static void main(String args[]) {
        String filepath="G:\\plasmid\\test\\";
        
        List labels = new ArrayList();
        labels.add("HMG000406");
        labels.add("HMG000407");
        labels.add("HMG000408");
        
        ContainerProcessManager manager = new ContainerProcessManager();
        try {
            List containers = manager.getContainers(labels, true);
            if(containers == null) {
                System.out.println("Cannot get containers");
                System.exit(0);
            } else {
                List notFound = manager.getNofoundContainers(labels, containers);
                if(notFound.size()>0) {
                    System.out.println("We didn't find container: "+notFound);
                    System.exit(0);
                }
                
                String destContainerType = Container.PLATE_96;
                List newLabels = new ArrayList();
                for(int i=0; i<containers.size(); i++) {
                    newLabels.add("plate"+i);
                }
                
                MappingCalculator calculator = StaticMappingCalculatorFactory.generateMappingCalculator(StaticMappingCalculatorFactory.DIRECT_MAPPING, containers, newLabels, destContainerType, Sample.WORKING_GLYCEROL);
                if(!calculator.isMappingValid()) {
                    System.out.println("Incompatible container types.");
                    System.exit(0);
                }
                List worklist = calculator.calculateMapping();
                WorklistGenerator generator = new WorklistGenerator(worklist);
                generator.printFullWorklist(filepath+"full_worklist.txt");
                generator.printWorklistForRobot(filepath+"worklist.txt");
                generator.readWorklist(filepath+"full_worklist.txt");
                
                ContainerMapper mapper = new ContainerMapper(generator.getWorklist());
                List destContainers = mapper.mapContainer(destContainerType);
                List lineages = mapper.getWorklist();
                List samples = new ArrayList();
                List tubes = new ArrayList();
                for(int i=0; i<destContainers.size(); i++) {
                    Container c = (Container)destContainers.get(i);
                    TubeMap tm = manager.readTubeMappingFile(FILEPATH+c.getLabel());
                    
                    List l = mapper.convertToTubes(c, tm.getMapping());
                    for(int n=0; n<l.size(); n++) {
                        Container c1 = (Container)l.get(n);
                        tubes.add(c1);
                        samples.addAll(c1.getSamples());
                    }
                }
                manager.setContainerids(tubes);
                manager.setSampleids(samples);
                
                printContainer(destContainers);
                printContainer(tubes);
                printLineages(lineages);
                
                ProcessExecution execution = new ProcessExecution(0, ProcessExecution.COMPLETE, null, "test", "Tecan");
                execution.setLineages(lineages);
                execution.setInputObjects(containers);
                execution.setOutputObjects(tubes);
                
                if(!manager.persistData(tubes,execution)) {
                    System.out.println("Error occured while inserting into database.");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.exit(0);
        }
    }
    
    public static void printContainer(List containers) {
        System.out.println("=========== Containers ============");
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            System.out.println("containerid: "+c.getContainerid());
            System.out.println("container label: "+c.getLabel());
            System.out.println("location: "+c.getLocation());
            System.out.println("oriid: "+c.getOricontainerid());
            System.out.println("type: "+c.getType());
            
            List samples = c.getSamples();
            for(int k=0; k<samples.size(); k++) {
                Sample s = (Sample)samples.get(k);
                System.out.println("\tsampleid: "+s.getSampleid());
                System.out.println("\tcloneid: "+s.getCloneid());
                System.out.println("\tcontainerid: "+s.getContainerid());
                System.out.println("\tlabel: "+s.getContainerlabel());
                System.out.println("\tposition: "+s.getPosition());
                System.out.println("\tx: "+s.getPositionx());
                System.out.println("\ty: "+s.getPositiony());
                System.out.println("\tstatus: "+s.getStatus());
                System.out.println("\ttype: "+s.getType());
            }
            System.out.println();
        }
        System.out.println();
    }
    
    public static void printLineages(List lineages) {
        System.out.println("============ Sample Lineages ================");
        for(int i=0; i<lineages.size(); i++) {
            SampleLineage sl = (SampleLineage)lineages.get(i);
            System.out.println("from: "+sl.getSampleFrom().getSampleid());
            System.out.println("to: "+sl.getSampleTo().getSampleid());
        }
    }
}
