/*
 * ManyToManyContainerMapper.java
 *
 * Created on February 17, 2004, 3:47 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.io.*;

import edu.harvard.med.hip.flex.process.FlexProcessException;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.process.Result;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  DZuo
 */
public class ManyToManyContainerMapper extends OneToOneContainerMapper {
    public static final int [] EMPTY_WELL_LIST = {};
    public static final int TOTAL_WELLS_DEST = 96;
    public static final String FILEPATH = RearrayManager.FILEPATH;
    public static final String DILIM = RearrayManager.DILIM;
    public static final String REARRAY_OUTPUT = "rearray_output.txt";
    public static final String REARRAY_WORKLIST = "rearray_worklist.gwl";
    
    protected int [] emptyWellList;
    protected int totalWellsDest;
    protected int destWellIndex;
    protected List sourceContainerLabels;
    protected List destContainerLabels;
    
    protected List rearrayMappingList;
    
    public List getRearrayMappingList() {return rearrayMappingList;}
    public List getSourceContainerLabels() {return sourceContainerLabels;}
    public List getDestContainerLabels() {return destContainerLabels;}
    public int getNumOfSamples() {return rearrayMappingList.size();}
    
    /** Creates a new instance of ManyToManyMapper */
    public ManyToManyContainerMapper() {
        destWellIndex = 1;
        emptyWellList = EMPTY_WELL_LIST;
        totalWellsDest = TOTAL_WELLS_DEST;
    }
    
    /** Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @param project The project used for mapping.
     * @param workflow The workflow related to the mapping container.
     * @return The new containers.
     * @exception FlexDatabaseException.
     *
     */
    public Vector doMapping(Vector containers, Protocol protocol, Project project, Workflow workflow) throws FlexDatabaseException {
        String newContainerType = getContainerType(protocol.getProcessname());
        Vector newContainers = new Vector();
        sourceContainerLabels = new ArrayList();
        destContainerLabels = new ArrayList();
        
        String projectCode = getProjectCode(project, workflow);
        
        boolean isNewPlate = true;
        String newBarcode = null;
        Container newContainer = null;
        rearrayMappingList = new ArrayList();
        Enumeration enu = containers.elements();
        while (enu.hasMoreElements()) {
            Container container = (Container)enu.nextElement();
            sourceContainerLabels.add(container.getLabel());
            getSamples(container);
            Vector oldSamples = container.getSamples();
            for (int i=0; i<oldSamples.size(); i++) {
                if(isNewPlate) {
                    int threadid = FlexIDGenerator.getID("threadid");
                    newBarcode = Container.getLabel(projectCode, protocol.getProcesscode(), threadid, getSubThread(container));
                    newContainer = new Container(newContainerType, null, newBarcode, container.getThreadid());
                    isNewPlate = false;
                    destContainerLabels.add(newBarcode);
                }
                
                boolean isMapped = false;
                while(destWellIndex <= totalWellsDest) {
                    if(found(destWellIndex, emptyWellList)) {
                        destWellIndex++;
                    } else {
                        Sample s = (Sample)oldSamples.get(i);
                        String type = s.getType();
                        Sample newSample = new Sample(type, destWellIndex, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
                        newSample.setCloneid(s.getCloneid());
                        newContainer.addSample(newSample);
                        sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
                        if(!type.equals(Sample.EMPTY)) {
                            RearrayInputSample rs = new RearrayInputSample(container.getLabel(), (new Integer(s.getPosition())).toString(), newContainer.getLabel(), (new Integer(destWellIndex)).toString(), false);
                            rearrayMappingList.add(rs);
                        }
                        
                        destWellIndex++;
                        isMapped = true;
                        break;
                    }
                }
                
                if(destWellIndex > totalWellsDest) {
                    newContainers.addElement(newContainer);
                    isNewPlate = true;
                    destWellIndex = 1;
                    if(!isMapped) {
                        i--;
                    }
                }
            }
        }
        
        if(destWellIndex <= totalWellsDest) {
            newContainers.addElement(newContainer);
        }
        
        return newContainers;
    }
    
    protected boolean found(int destWellIndex, int [] emptyWellList) {
        for(int i=0; i<emptyWellList.length; i++) {
            if(destWellIndex == emptyWellList[i]) {
                return true;
            }
        }
        
        return false;
    }
    
    public File createRearrayFile() throws IOException {
        File file = new File(FILEPATH+REARRAY_OUTPUT);
        FileWriter fr = new FileWriter(file);
        
        fr.write("Source containers:\n");
        if(sourceContainerLabels != null) {
            for(int i=0; i<sourceContainerLabels.size(); i++) {
                String label = (String)sourceContainerLabels.get(i);
                fr.write("\t"+label+"\n");
            }
        }
        
        fr.write("\nDestination containers:\n");
        if(destContainerLabels != null) {
            for(int i=0; i<destContainerLabels.size(); i++) {
                String label = (String)destContainerLabels.get(i);
                fr.write("\t"+label+"\n");
            }
        }
        
        fr.write("\nTotal number of samples: "+getNumOfSamples()+"\n");
        
        fr.write("\nOriginal plate label"+DILIM+"Original plate well"+DILIM+"Destination plate label"+ DILIM+  "Destination plate well\n");
        for(int i=0; i<rearrayMappingList.size(); i++) {
            RearrayInputSample sample = (RearrayInputSample)rearrayMappingList.get(i);
            fr.write(sample.getSourcePlate()+DILIM+sample.getSourceWell()+DILIM+sample.getDestPlate()+DILIM+sample.getDestWell()+"\n");
        }
        fr.flush();
        fr.close();
        
        return file;
    }
    
    public File createWorklist(String sourcePlateType, String destPlateType, int volumn) throws IOException {
        File file = new File(FILEPATH+REARRAY_WORKLIST);
        FileWriter fr = new FileWriter(file);
        for(int i=0; i<rearrayMappingList.size(); i++) {
            RearrayInputSample sample = (RearrayInputSample)rearrayMappingList.get(i);
            fr.write("A;;"+sample.getSourcePlate()+";"+sourcePlateType+";"+sample.getSourceWell()+";;"+volumn+"\n");
            fr.write("D;;"+sample.getDestPlate()+";"+destPlateType+";"+sample.getDestWell()+";;"+volumn+"\n");
            fr.write("W;\n");
        }
        fr.flush();
        fr.close();
        
        return file;
    }
}