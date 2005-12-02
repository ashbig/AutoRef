/*
 * PlateCondensationManager.java
 *
 * Created on November 23, 2005, 3:36 PM
 */

package edu.harvard.med.hip.flex.process;

import java.sql.*;
import java.util.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.VectorComparator;
import edu.harvard.med.hip.flex.util.FlexIDGenerator;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class PlateCondensationManager {
    public static final int MULTIPLICATION = 4;
    
    private boolean isFormatSeparate;
    private boolean isPartial;
    private Vector sampleLineages;
    private List containerMaps;
    
    /** Creates a new instance of PlateCondensationManager */
    public PlateCondensationManager() {
    }
    
    public List getContainerMaps() {return containerMaps;}
    
    public boolean checkMultiplication(List srcLabels, int n) {
        if(srcLabels == null)
            return false;
        
        int num = srcLabels.size()%n;
        if(num == 0)
            return true;
        else
            return false;
    }
    
    public List restoreSrcContainers(List srcLabels) throws Exception {
        if(srcLabels == null)
            return null;
        
        List destContainers = new ArrayList();
        for(int i=0; i<srcLabels.size(); i++) {
            String label = (String)srcLabels.get(i);
            List containers = CloneContainer.findContainers(label, false, true);
            if(containers == null) {
                throw new Exception("Cannot find container with label: "+label);
            }
            destContainers.add((CloneContainer)containers.get(0));
        }
        
        return destContainers;
    }
    
    public boolean checkVector(List samples) {
        Set vectors = new TreeSet(new VectorComparator());
        
        for(int i=0; i<samples.size(); i++) {
            CloneSample s = (CloneSample)samples.get(i);
            String vector = s.getVectorname();
            if(vector != null)
                vectors.add(vector);
        }
        
        if(vectors.size()>1)
            return false;
        
        return true;
    }
    
    public boolean checkCloneType(List samples) {
        Set types = new TreeSet();
        
        for(int i=0; i<samples.size(); i++) {
            CloneSample s = (CloneSample)samples.get(i);
            String type = s.getClonetype();
            if(type != null)
                types.add(type);
        }
        
        if(types.size()>1)
            return false;
        
        return true;
    }
    
    public List condensePlates(int projectid, List containers, String containerType, String sampleType, boolean isWorking) throws Exception {
        PlateCondensationMapper mapper = new PlateCondensationMapper();
        sampleLineages = new Vector();
        containerMaps = new ArrayList();
        List destContainers = new ArrayList();
        List src = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            CloneContainer c = (CloneContainer)containers.get(i);
            src.add(c);
            if((i+1)%MULTIPLICATION == 0 || i+1==containers.size()) {
                int threadid = FlexIDGenerator.getID("threadid");        
                java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
                fmt.setMaximumIntegerDigits(6);
                fmt.setMinimumIntegerDigits(6);
                fmt.setGroupingUsed(false);
                String barcode = null;
                if(Project.HUMAN == projectid)
                    barcode = "H";
                else if(Project.Bacillus_anthracis == projectid)
                    barcode = "Q";
                else if(Project.FT == projectid)
                    barcode = "F";
                else if(Project.PSEUDOMONAS == projectid)
                    barcode = "P";
                else if(Project.VC == projectid)
                    barcode = "V";
                else if(Project.YEAST == projectid)
                    barcode = "Y";
                else if(Project.YP == projectid)
                    barcode = "S";
                else if(Project.Yersinia_pseudotuberculosis == projectid)
                    barcode = "I";
                else
                    barcode = "Z";
                barcode = barcode+"3AR"+fmt.format(threadid);
                Container container = mapper.mapContainers(src, containerType, barcode, sampleType, isWorking);
                destContainers.add(container);
                sampleLineages.addAll(mapper.getSampleLineageSet());
                List l = new ArrayList();
                l.add(container);
                ContainerMap map = new ContainerMap(src, l);
                containerMaps.add(map);
                src = new ArrayList();
            }
        }
        return destContainers;
    }
    
    public void persistData(Project project, Workflow workflow, Protocol protocol,
    Researcher researcher, List srcContainers, List destContainers, String storageType, 
    String storageForm, boolean isWorking) throws Exception {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            for(int i=0; i<destContainers.size(); i++) {
                Container c = (Container)destContainers.get(i);
                c.insert(conn);
            }
            
            WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
            manager.createProcessRecord(Process.SUCCESS, protocol, researcher,null, srcContainers, destContainers,null, sampleLineages, conn);
            
            DatabaseTransaction.commit(conn);
            
            //add the new containers to storage table as working glycerol            
            ThreadedCloneStorageManager storageManager = new ThreadedCloneStorageManager(destContainers, storageType, storageForm);
            new java.lang.Thread(storageManager).start();
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            throw ex;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}