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
import plasmid.util.CloneGrowthComparator;
import plasmid.util.SftpHandler;
import com.jscape.inet.sftp.*;

/**
 *
 * @author  DZuo
 */
public class ContainerProcessManager {
    public static final String DELIM = ",";
    public static final String NOTUBE = TubeMap.NOTUBE;
    public static final String TUBEMAPFILEPATH = Constants.TUBEMAP_FILE_PATH;
    public static final int ROW = 8;
    public static final int COLUMN = 12;
    
    /** Creates a new instance of ContainerProcessManager */
    public ContainerProcessManager() {
    }
    
    public List getContainersWithCloneInfo(List labels) {
        if(labels == null)
            return null;
        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            PlateManager manager = new PlateManager(conn);
            List containers = manager.queryContainersWithCloneInfo(labels);
            
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
    
    public List getGrowthConditions(List containers) {
        List growths = new ArrayList();
        
        if(containers == null)
            return growths;
        
        List samples = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            Container container = (Container)containers.get(i);
            List s = container.getSamples();
            if(s != null) {
                samples.addAll(s);
            }
        }
        
        Set clones = new TreeSet(new CloneGrowthComparator());
        for(int i=0; i<samples.size(); i++) {
            Sample sample = (Sample)samples.get(i);
            Clone c = sample.getClone();
            clones.add(c);
        }
        
        Iterator iter = clones.iterator();
        while(iter.hasNext()) {
            Clone c = (Clone)iter.next();
            GrowthCondition g = c.getRecommendedGrowthCondition();
            growths.add(g);
        }
        
        return growths;
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
    
    /**
     * Check a given list of Container objects to see if there is any non-empty containers.
     *
     * @param containers A list of Container objects.
     * @return A list of non-empty Container objects.
     */
    public List checkEmptyContainers(List containers, String status) {
        List l = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            if(c.getStatus().equals(status)) {
                l.add(c.getLabel());
            }
        }
        
        return l;
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
    
    public Map readTubeMappingFile(String filename, Sftp ftp) {
        BufferedReader in = null;
        Map mapping = new HashMap();
        try {
            //in = new BufferedReader(new FileReader(filename));
            in = new BufferedReader(new InputStreamReader(ftp.getInputStream(filename, 0)));
            String line = in.readLine();
            int i=0;
            int j=1;
            while((line = in.readLine()) != null) {
                String s = line.trim();
                if(s.length()<1)
                    continue;
                
                String label = null;
                if(s.indexOf(NOTUBE)<0) {
                    label = s.substring(0, s.length()-1);
                }
                
                mapping.put((new Integer(i*ROW+j)).toString(), label);
                i++;
                if(i == COLUMN) {
                    i=0;
                    j++;
                }
            }
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
        
        return mapping;
    }
    
    public boolean setContaineridAndLabels(List containers, String suffix) {
        if(containers == null || containers.size() == 0)
            return true;
        
        if(setContainerids(containers)) {
            for(int i=0; i<containers.size(); i++) {
                Container c = (Container)containers.get(i);
                int id = DefTableManager.getNextid("containerlabel");
                if(id < 0) {
                    if(Constants.DEBUG) {
                        System.out.println("Cannot get containerid from database.");
                    }
                    return false;
                }
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
                    Sample s = (Sample)samples.get(k);
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
    
    public List setSampleToidsForLineages(List samples, List lineages) {
        List l = new ArrayList();
        for(int i=0; i<samples.size(); i++) {
            Sample s = (Sample)samples.get(i);
            String label = s.getContainerlabel();
            int position = s.getPosition();
            SampleLineage sl = findSample(lineages, label, position);
            if(s == null) {
                if(Constants.DEBUG) {
                    System.out.println("Cannot find samplelineage with label/position: "+label+"/"+position);
                }
                return null;
            }
            sl.getSampleTo().setSampleid(s.getSampleid());
            l.add(sl);
        }
   
        return l;
    }
    
    public SampleLineage findSample(List lineages, String label, int position) {
        for(int i=0; i<lineages.size(); i++) {
            SampleLineage sl = (SampleLineage)lineages.get(i);
            Sample to = sl.getSampleTo();
            String l = to.getContainerlabel();
            int pos = to.getPosition();
            if(l.equals(label) && pos == position)
                return sl;
        }
        return null;
    }
    
    public boolean persistData(List newContainers, List updateContainers, ProcessExecution process, WorklistInfo info) {
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
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
        
        PlateManager manager = new PlateManager(conn);
        if(!manager.insertPlate(newContainers)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot insert containers.");
                System.out.println(manager.getErrorMessage());
            }
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
        if(!manager.updatePlates(updateContainers, Container.FILLED, Container.WORKBENCH)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot update containers.");
                System.out.println(manager.getErrorMessage());
            }
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
        
        for(int i=0; i<updateContainers.size(); i++) {
            Container c = (Container)updateContainers.get(i);
        }
        List samples = new ArrayList();
        List containers = new ArrayList();
        if(newContainers != null)
            containers.addAll(newContainers);
        if(updateContainers != null)
            containers.addAll(updateContainers);
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            List ss = c.getSamples();
            samples.addAll(ss);
        }
        if(!manager.insertSample(samples)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot insert samples.");
                System.out.println(manager.getErrorMessage());
            }
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
        
        ProcessManager m = new ProcessManager(conn);
        if(process != null) {
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
        
        if(info != null) {
            if(!m.updateWorklistInfo(info, WorklistInfo.COMMIT)) {
                if(Constants.DEBUG) {
                    System.out.println("Cannot update WORKLISTINFO with worklistid: "+info.getWorklistid());
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
    
    public boolean persistWorklistInfo(WorklistInfo info) {
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
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
        
        ProcessManager manager = new ProcessManager(conn);
        if(manager.insertWorklistInfo(info)) {
            DatabaseTransaction.commit(conn);
            DatabaseTransaction.closeConnection(conn);
            return true;
        } else {
            if(Constants.DEBUG) {
                System.out.println("Cannot insert data into WORKLISTINFO.");
                System.out.println(manager.getErrorMessage());
            }
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
    }
    
    public void writeContainers(List containers, PrintWriter out) {
        out.println("Container\tWell\tType\tClone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tMutation\tDiscrepancy\tInsert Format\tVector\tSelection Markers\tGrowth Condition");
        
        for(int i=0; i<containers.size(); i++) {
            Container container = (Container)containers.get(i);
            List samples = container.getSamples();
            for(int j=0; j<samples.size(); j++) {
                Sample s = (Sample)samples.get(j);
                Clone c = s.getClone();
                
                out.print(container.getLabel()+"\t"+s.getWell()+"\t"+s.getType());
                
                if(Sample.EMPTY.equals(s.getType())) {
                    out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t");
                } else {
                    out.print("\t"+c.getCloneid()+"\t"+c.getType());
                    
                    if(Clone.NOINSERT.equals(c.getType())) {
                        out.print("\t\t\t\t\t\t\t\t");
                    } else {
                        List inserts = c.getInserts();
                        for(int n=0; n<inserts.size(); n++) {
                            DnaInsert insert = (DnaInsert)inserts.get(n);
                            out.print("\t"+insert.getGeneid()+"\t"+insert.getName()+"\t"+insert.getDescription()+"\t"+insert.getTargetgenbank()+"\t"+insert.getTargetseqid()+"\t"+insert.getHasmutation()+"\t"+insert.getHasdiscrepancy()+"\t"+insert.getFormat());
                        }
                    }
                    
                    out.println("\t"+c.getVectorname()+"\t"+c.getSelectionString()+"\t"+c.getRecommendedGrowthCondition().getName());
                }
            }
        }
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
                
                MappingCalculator calculator = StaticMappingCalculatorFactory.generateMappingCalculator(StaticMappingCalculatorFactory.DIRECT_MAPPING, containers, newLabels, Sample.WORKING_GLYCEROL);
                if(!calculator.isMappingValid()) {
                    System.out.println("Incompatible container types.");
                    System.exit(0);
                }
                
                Sftp ftp = SftpHandler.getSftpConnection();
                List worklist = calculator.calculateMapping();
                WorklistGenerator generator = new WorklistGenerator(worklist);
                generator.printFullWorklist(filepath+"full_worklist.txt", ftp);
                generator.printWorklist(filepath+"worklist.txt", ftp);
                generator.readWorklist(filepath+"full_worklist.txt", ftp);
                
                ContainerMapper mapper = new ContainerMapper(generator.getWorklist());
                List destContainers = mapper.mapContainer();
                List lineages = mapper.getWorklist();
                List samples = new ArrayList();
                List tubes = new ArrayList();
                for(int i=0; i<destContainers.size(); i++) {
                    Container c = (Container)destContainers.get(i);
                    Map m = manager.readTubeMappingFile(TUBEMAPFILEPATH+c.getLabel(), ftp);
                    
                    List l = mapper.convertToTubes(c, m, true);
                    for(int n=0; n<l.size(); n++) {
                        Container c1 = (Container)l.get(n);
                        tubes.add(c1);
                        samples.addAll(c1.getSamples());
                    }
                }
                SftpHandler.disconnectSftp(ftp);
                manager.setContainerids(tubes);
                manager.setSampleids(samples);
                
                printContainer(destContainers);
                printContainer(tubes);
                printLineages(lineages);
                
                ProcessExecution execution = new ProcessExecution(0, ProcessExecution.COMPLETE, null, "test", "Tecan", "Test Protocol");
                execution.setLineages(lineages);
                execution.setInputObjects(containers);
                execution.setOutputObjects(tubes);
                
                if(!manager.persistData(tubes,null,execution,null)) {
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
