/*
 * RearrayManager.java
 *
 * This class is the control class that manages rearray. It communicates with other
 * rearray related classes to handle rearray. It is the major class that people use.
 *
 * Created on May 16, 2003, 10:08 AM
 */

package edu.harvard.med.hip.flex.process;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.utility.Logger;

/**
 *
 * @author  dzuo
 */
public class RearrayManager {
    public static final String REARRAYEDPLATETYPE = "96 WELL PLATE";
    public static final String DILIM = "\t";
    public static final String FILEPATH = FlexProperties.getInstance().getProperty("tmp");
    public static final String LEFTFILE = "left";
    public static final String DEFAULTLOGFILE = "rearray_log.txt";
    
    protected InputStream fileInput = null;
    protected ArrayList leftSamples = null;
    protected Hashtable allRearrayedSamples = null;
    protected ArrayList rearrayedSamples = null;
    protected ArrayList rearrayedOligo5pSamples = null;
    protected ArrayList rearrayedOligo3fSamples = null;
    protected ArrayList rearrayedOligo3cSamples = null;
    protected ArrayList rearrayedContainers = null;
    protected ArrayList rearrayedPlatesets = null;
    protected ArrayList files = null;
    
    protected boolean isDestPlateSet = false;
    protected boolean isClone = false;
    protected boolean isTemplate = false;
    protected boolean isArrangeByFormat = false;
    protected boolean isArrangeBySize = false;
    protected boolean isSmall = false;
    protected boolean isMedium = false;
    protected boolean isLarge = false;
    protected boolean isFullPlate = false;
    protected boolean isControl = false;
    protected boolean isOligo = false;
    protected boolean isNewOligo = false;
    protected boolean isFusion = false;
    protected boolean isClosed = false;
    protected boolean isOnQueue = false;
    protected boolean isOneFile = false;
    protected boolean isPlateAsLabel = true;
    protected boolean isWellAsNumber = true;
    protected boolean isDestWellAsNumber = true;
    protected boolean isStorage = false;
    protected boolean isSourceDup = false;
    
    protected int sortBy = GenericRearrayer.SORT_BY_NONE;
    protected int numOfWellsOnPlate = 96;
    protected int numOfControls = 0;
    protected int sizeLower = GenericRearrayer.SIZELOWER;
    protected int sizeHigher = GenericRearrayer.SIZEHIGHER;
    protected String plateType = REARRAYEDPLATETYPE;
    protected String location = Location.UNAVAILABLE;
    protected int projectid = -1;
    protected int workflowid = -1;
    protected String protocolName = null;
    protected String labelPrefix = null;
    protected String sampleType = Sample.ISOLATE;
    protected String researcherBarcode = null;
    
    protected String storageForm = null;
    protected String storageType = null;
    protected String destStorageForm = null;
    protected String destStorageType = null;
    protected String logFile = DEFAULTLOGFILE;
    
    protected Project project = null;
    protected Workflow workflow = null;
    protected Protocol protocol = null;
    protected ProjectWorkflow pw = null;
    protected Location l = null;
    protected Researcher researcher = null;
    
    public ArrayList getFiles() {return files;}
    public ArrayList getRearrayedContainers() {return rearrayedContainers;}
    
    public void setIsDestPlateSet(boolean b) {this.isDestPlateSet = b;}
    public void setIsClone(boolean b) {this.isClone = b;}
    public void setIsTemplate(boolean b) {this.isTemplate = b;}
    public void setIsArrangeByFormat(boolean b) {this.isArrangeByFormat = b;}
    public void setIsArrangeBySize(boolean b) {this.isArrangeBySize = b;}
    public void setIsSmall(boolean b) {this.isSmall = b;}
    public void setIsMedium(boolean b) {this.isMedium = b;}
    public void setIsLarge(boolean b) {this.isLarge = b;}
    public void setIsFullPlate(boolean b) {this.isFullPlate = b;}
    public void setIsControl(boolean b) {this.isControl = b;}
    public void setIsOligo(boolean b) {this.isOligo = b;}
    public void setIsNewOligo(boolean b) {this.isNewOligo = b;}
    public void setIsOnQueue(boolean b) {this.isOnQueue = b;}
    public void setIsOneFile(boolean b) {this.isOneFile = b;}
    public void setIsPlateAsLabel(boolean b) {this.isPlateAsLabel = b;}
    public void setIsWellAsNumber(boolean b) {this.isWellAsNumber = b;}
    public void setIsDestWellAsNumber(boolean b) {this.isDestWellAsNumber = b;}
    public void setIsFusion(boolean b) {this.isFusion = b;}
    public void setIsClosed(boolean b) {this.isClosed = b;}
    public void setIsStorage(boolean b) {this.isStorage = b;}
    
    public void setSortBy(int i) {this.sortBy = i;}
    public void setNumOfWellsOnPlate(int i) {this.numOfWellsOnPlate = i;}
    public void setNumOfControls(int i) {this.numOfControls = i;}
    public void setSizeLower(int i) {this.sizeLower = i;}
    public void setSizeHigher(int i) {this.sizeHigher = i;}
    public void setPlateType(String plateType) {this.plateType = plateType;}
    public void setLocation(String location) {this.location = location;}
    public void setProjectid(int projectid) {this.projectid = projectid;}
    public void setWorkflowid(int workflowid) {this.workflowid = workflowid;}
    public void setProtocolName(String protocolName) {this.protocolName = protocolName;}
    public void setLabelPrefix(String labelPrefix) {this.labelPrefix = labelPrefix;}
    public void setSampleType(String sampleType) {this.sampleType = sampleType;}
    public void setResearcherBarcode(String researcher) {this.researcherBarcode = researcher;}
    public void setStorageForm(String s) {this.storageForm = s;}
    public void setStorageType(String s) {this.storageType = s;}
    public void setDestStorageForm(String s) {this.destStorageForm = s;}
    public void setDestStorageType(String s) {this.destStorageType = s;}
    public void setIsSourceDup(boolean b) {this.isSourceDup = b;}
    public void setLogFile(String s) {this.logFile = s;}
    
    public int getProjectid() {return projectid;}
    public int getWorkflowid() {return workflowid;}
    public String getStorageForm() {return storageForm;}
    public String getStorageType() {return storageType;}
    public String getDestStorageForm() {return destStorageForm;}
    public String getDestStorageType() {return destStorageType;}
    public String getSampleType() {return sampleType;}
    
    /** Creates a new instance of RearrayManager */
    public RearrayManager(InputStream fileInput) {
        this.fileInput = fileInput;
    }
    
    /**
     * Creates the rearrayed plates along with oligo plates if needed.
     *
     * @param conn Database connection object.
     * @exception FlexDatabaseException
     * @exception FlexProcessException
     * @exception FlexCoreException
     * @exception FlexWorkflowException
     * @exception IOException
     * @exception Exception
     */
    public void createAllPlates(Connection conn)
    throws FlexDatabaseException, FlexProcessException, FlexCoreException,
    FlexWorkflowException,IOException, RearrayException, NumberFormatException, Exception {
        if(projectid != -1 && workflowid != -1) {
            project = new Project(projectid);
            workflow = new Workflow(workflowid);
            pw = new ProjectWorkflow(projectid, workflowid);
        }
        if(protocolName != null) {
            protocol = new Protocol(protocolName);
        }
        
        l = new Location(location);
        researcher = new Researcher(researcherBarcode);
        
        ArrayList inputSamples = readFile();
        ArrayList samples = convertSamples(inputSamples, conn);
        
        if(samples == null || samples.size() == 0) {
            throw new RearrayException("No samples found in database.");
        }
        //printSamples(samples);
        
        if(!isSourceDup) {
            List dups = checkSourceDup(samples);
            
            if(dups.size() > 0) {
                String message = "The following samples occured more than once in source plates:<br>";
                for(int i=0; i<dups.size(); i++) {
                    RearrayPlateMap m = (RearrayPlateMap)dups.get(i);
                    if(isClone) {
                        message += "\t"+m.getRearrayInputSample().getClone()+"<br>";
                    } else {
                        message += "\t"+m.getRearrayInputSample().getSourcePlate()+"\t"+m.getRearrayInputSample().getSourceWell()+"<br>";
                    }
                }
                throw new RearrayException(message);
            }
        }
        
        leftSamples = new ArrayList();
        allRearrayedSamples = new Hashtable();
        rearrayedContainers = new ArrayList();
        rearrayedPlatesets = new ArrayList();
        
        rearrayedSamples = new ArrayList();
        rearrayedOligo5pSamples = new ArrayList();
        rearrayedOligo3fSamples = new ArrayList();
        rearrayedOligo3cSamples = new ArrayList();
        
        GenericRearrayer rearrayer = new GenericRearrayer();
        ArrayList rearrayedSamples = new ArrayList();
        
        //If the destination plates and wells are provided, then should not do any rearrangement.
        if(isDestPlateSet) {
            rearrayedSamples = rearrayer.groupByDest(samples);
            for(int i=0; i<rearrayedSamples.size(); i++) {
                createPlates((ArrayList)rearrayedSamples.get(i), conn);
            }
            
            addToAllSamples();
            
            return;
        }
        
        if(isArrangeByFormat)
            rearrayedSamples = rearrayer.groupByFormat(samples);
        else
            rearrayedSamples.add(samples);
        
        for(int i=0; i<rearrayedSamples.size(); i++) {
            ArrayList currentSamples = (ArrayList)rearrayedSamples.get(i);
            
            if(isArrangeBySize) {
                ArrayList smallSamples = rearrayer.getSequencesBySize(currentSamples, 0, sizeLower);
                ArrayList medSamples = rearrayer.getSequencesBySize(currentSamples, sizeLower, sizeHigher);
                ArrayList largeSamples = rearrayer.getSequencesBySize(currentSamples, sizeHigher, -1);
                ArrayList allSamples = new ArrayList();
                
                if(isSmall) {
                    createPlates(smallSamples, conn);
                } else {
                    allSamples.addAll(smallSamples);
                }
                
                if(isMedium) {
                    createPlates(medSamples, conn);
                } else {
                    allSamples.addAll(medSamples);
                }
                
                if(isLarge) {
                    createPlates(largeSamples, conn);
                } else {
                    allSamples.addAll(largeSamples);
                }
                
                if(allSamples.size() != 0) {
                    createPlates(allSamples, conn);
                }
            } else {
                createPlates(currentSamples, conn);
            }
        }
        
        addToAllSamples();
        
        if(isOnQueue) {
            for(int i=0; i<rearrayedPlatesets.size(); i++) {
                Plateset ps = (Plateset)rearrayedPlatesets.get(i);
                putPlatesetOnQueue(ps, conn);
            }
        }
    }
    
    /**
     * Write the rearrayed files in the following format:
     * source plate, source well, destination plate, destination well.
     *
     * @exception IOException
     */
    public void writeOutputFiles() throws IOException {
        files = new ArrayList();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String sb = sdf.format(cal.getTime());
        
        if(isOneFile) {
            ArrayList currentSamples = null;
            
            Enumeration enum = allRearrayedSamples.keys();
            while(enum.hasMoreElements()) {
                String k = (String)enum.nextElement();
                ArrayList allSamples = (ArrayList)allRearrayedSamples.get(k);
                if(allSamples.size() != 0) {
                    currentSamples = getSamples(allSamples);
                    writeFile(k+"_"+sb, currentSamples);
                }
            }
        } else {
            Enumeration enum = allRearrayedSamples.keys();
            while(enum.hasMoreElements()) {
                String k = (String)enum.nextElement();
                ArrayList allSamples = (ArrayList)allRearrayedSamples.get(k);
                if(allSamples.size() != 0) {
                    for(int i=0; i<allSamples.size(); i++) {
                        ArrayList samples = (ArrayList)allSamples.get(i);
                        String label = ((RearrayPlateMap)samples.get(0)).getDestPlateLabel();
                        writeFile(label, samples);
                    }
                }
            }
        }
        
        if(leftSamples.size() != 0) {
            writeFile(LEFTFILE+"_"+sb, leftSamples);
        }
    }
    
    /**
     * Send the rearrayed files to user by email.
     *
     * @param userEmail The email address to send to.
     * @exception Exception.
     */
    public void sendRobotFiles(String userEmail) throws Exception {
        String to = "dzuo@hms.harvard.edu";
        String from = "dzuo@hms.harvard.edu";
        String cc = userEmail;
        String subject = "Rearraed plates for the project - "+project.getName();
        String msgText = "The attached files are robot file(s) for rearrayed plates.\n";
        Mailer.sendMessage(to, from, cc, subject, msgText, files);
    }
    
    public void writeToLog(String filename) throws IOException {
        java.util.Date today = new java.util.Date();
        File file = new File(FILEPATH+filename);
        FileWriter fr = new FileWriter(file);
        
        fr.write("Begin: "+today+"\n\n");
        
        
        
        fr.write("Project:\t"+project.getName()+"\n");
        fr.write("Workflow:\t"+workflow.getName()+"\n");
        fr.write("\nRearray Options:");
        if(sortBy == GenericRearrayer.SORT_BY_NONE) {
            fr.write("Create destination plates according to:\t"+"Input file order\n");
        }
        if(sortBy == GenericRearrayer.SORT_BY_SAWTOOTH) {
            fr.write("Create destination plates according to:\t"+"Saw-Tooth pattern\n");
        }
        if(sortBy == GenericRearrayer.SORT_BY_SOURCE) {
            fr.write("Create destination plates according to:\t"+"Source plate with most samples first\n");
        }
        if(isArrangeBySize) {
            fr.write("The following sequences grouped separately:");
            if(isSmall) {
                fr.write("\tgroup 1\n");
            }
            if(isMedium) {
                fr.write("\tgroup 2\n");
            }
            if(isLarge) {
                fr.write("\tgroup 3\n");
            }
            fr.write("\tCDS length to separate group 1 and group 2:\t"+sizeLower+"\n");
            fr.write("\tCDS length to separate group 2 and group 3:\t"+sizeHigher+"\n");
        }
        if(isArrangeByFormat) {
            fr.write("Separate by construct type (FUSION, CLOSE, etc.):\tYes\n");
        } else {
            fr.write("Separate by construct type (FUSION, CLOSE, etc.):\tNo\n");
        }
        if(isControl) {
            fr.write("Leave empty wells for controls (positive control on first well, negative control on last well):\tYes\n");
        } else {
            
            fr.write("Leave empty wells for controls (positive control on first well, negative control on last well):\tNo\n");
        }
        if(isFullPlate) {
            fr.write("Rearray partial plates:\tNo\n");
        } else {
            fr.write("Rearray partial plates:\tYes\n");
        }
        if(isSourceDup) {
            fr.write("Allow duplicate samples in source plate:\tYes\n");
        } else {
            fr.write("Allow duplicate samples in source plate:\tNo\n");
        }
        if(isOligo) {
            if(isNewOligo) {
                fr.write("Generate rearrayed oligo plates from new oligos\n");
            } else {
                fr.write("Generate rearrayed oligo plates from existing oligo plates\n");
            }
            if(isFusion) {
                fr.write("\tOligo format:\tFusion\n");
            } else if (isClosed) {
                fr.write("\tOligo format:\tClosed\n");
            } else {
                fr.write("\tOligo format:\tBoth\n");
            }
        }
        
        today = new java.util.Date();
        fr.write("\n\nEnd: "+today);
        
        fr.flush();
        fr.close();
        
        files.add(file);
    }
    
    //---------------------------------------------------------------------------------//
    //                          Private Methods
    //---------------------------------------------------------------------------------//
    
    //Read the input file.
    protected ArrayList readFile() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(fileInput));
        String line = null;
        ArrayList samples = new ArrayList();
        
        while((line = in.readLine()) != null) {
            if(line.trim() == null || line.trim().equals(""))
                continue;
            
            StringTokenizer st = new StringTokenizer(line, DILIM);
            String info[] = new String[4];
            int i = 0;
            while(st.hasMoreTokens()) {
                info[i] = st.nextToken();
                i++;
            }
            
            RearrayInputSample s = new RearrayInputSample(info[0],info[1],info[2],info[3],isClone);
            samples.add(s);
        }
        in.close();
        return samples;
    }
    
    //Convert the input samples into RearrayPlateMap samples.
    protected ArrayList convertSamples(List samples, Connection conn)
    throws SQLException, NumberFormatException, RearrayException {
        RearrayPlateMapCreator creator = new RearrayPlateMapCreator(isPlateAsLabel,isWellAsNumber);
        creator.setIsDestWellAsNumber(isDestWellAsNumber);
        
        ArrayList newSamples = null;
        
        if(isTemplate) {
            newSamples = creator.createRearrayTemplateSamples(samples,  conn);
        } else if(isClone) {
            newSamples = creator.createRearrayClones(samples, storageType, storageForm, conn);
        } else {
            newSamples = creator.createRearraySamples(samples, conn);
        }
        
        return newSamples;
    }
    
    //Creates rearrayed plates for given samples. Insert records into database.
    protected void createPlates(List samples, Connection conn)
    throws FlexDatabaseException, FlexCoreException, FlexWorkflowException,
    RearrayException, IOException, Exception {
        GenericRearrayer rearrayer = new GenericRearrayer();
        
        if(sortBy == GenericRearrayer.SORT_BY_SOURCE && !isDestPlateSet) {
            samples = rearrayer.groupBySource(samples);
        }
        
        if(sortBy == GenericRearrayer.SORT_BY_SAWTOOTH && !isDestPlateSet) {
            samples = rearrayer.sortBySawTooth(samples);
        }
        
        ArrayList plateSamples = new ArrayList();
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap m = (RearrayPlateMap)samples.get(i);
            
            //check the dest plate well position for illegal value
            if(isDestPlateSet && (m.getDestWell()<(numOfControls/2+1) || m.getDestWell()>(numOfWellsOnPlate-numOfControls/2)))
                throw new RearrayException("Invalid destination well: "+m.getRearrayInputSample().getDestPlate()+": "+m.getRearrayInputSample().getDestWell());
            
            plateSamples.add(samples.get(i));
            
            //This is one plate
            if((i+1)%(numOfWellsOnPlate-numOfControls)==0 || (i==(samples.size()-1)&&!isFullPlate)) {
                int threadid = FlexIDGenerator.getID("threadid");
                String label = getLabel(pw, protocol, threadid);
             
                Container container = createPlate(plateSamples, plateType,sampleType,threadid,label,conn);
                //rearrayedContainers.add(container);
                
                if(isOligo) {
                    if(isNewOligo) {
                        createNewOligos(plateSamples, container, conn);
                    } else {
                        Plateset plateset = createRearrayedOligos(plateSamples, container, threadid, conn);
                        rearrayedPlatesets.add(plateset);
                    }
                }
                
                rearrayedSamples.add(plateSamples);
                plateSamples = new ArrayList();
            } else {
                if(i == (samples.size() -1) && isFullPlate) {
                    leftSamples.addAll(plateSamples);
                }
            }
        }
    }
    
    //Create one rearrayed plate for given samples. Insert records into database.
    protected Container createPlate(List samples, String newPlateType,String newSampleType, int threadid, String label, Connection conn)
    throws FlexDatabaseException, FlexWorkflowException, SQLException {
        RearrayContainerMapper mapper = new RearrayContainerMapper(samples, isControl);
        Container container = null;
        
        if(isDestPlateSet) {
            container = mapper.mapContainerWithDest(newPlateType, label, threadid, l, newSampleType);
        } else {
            container = mapper.mapContainer(newPlateType, label, threadid, l, newSampleType);
        }
        
        container.insert(conn);
        Vector sampleLineageSet = mapper.getSampleLineageSet();
        Set sourceContainers = mapper.getSourceContainers();
        createProcessRecord(sourceContainers, container, sampleLineageSet, conn);
        
        if(isStorage) {
            addToStorage(container, conn);
        }
        
        rearrayedContainers.add(container);
        
        return container;
    }
    
    //Construct the plate label.
    protected String getLabel(ProjectWorkflow pw, Protocol protocol, int threadid)
    throws FlexDatabaseException {
        String label = null;
        
        if(labelPrefix != null) {
            label = labelPrefix+"threadid"+".1";
        } else {
            String projectCode = pw.getCode();
            String processCode = protocol.getProcesscode();
            
            if(sampleType.equals(OligoPlater.OLIGO_5P)) {
                processCode = OligoPlater.oligoFivePrefix;
            }
            if(sampleType.equals(OligoPlater.OLIGO_3C)) {
                processCode = OligoPlater.oligoClosePrefix;
            } 
            if(sampleType.equals(OligoPlater.OLIGO_3F)) {
                processCode = OligoPlater.oligoFusionPrefix;
            }
            
            label = Container.getLabel(projectCode, processCode, threadid, null);
        }
        
        return label;
    }
    
    //Create the process execution records in the database.
    protected void createProcessRecord(Set sourceContainers, Container c, Vector sampleLineageSet, Connection conn)
    throws FlexDatabaseException, FlexWorkflowException {
        List newContainers = new ArrayList();
        newContainers.add(c);
        List oldContainers = new ArrayList();
        oldContainers.addAll(sourceContainers);
        String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;
        WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
        manager.createProcessRecord(executionStatus, protocol, researcher, null, oldContainers, newContainers, null, sampleLineageSet, conn);
    }
    
    //Call OligoPlateManager to design primers for given samples. Create new oligo plates.
    protected void createNewOligos(List samples, Container container, Connection conn)
    throws FlexDatabaseException, FlexCoreException, IOException, Exception {
        LinkedList sequences = new LinkedList();
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            Sequence s = new Sequence(sample.getSequenceid(), sample.getCdsstart(), sample.getCdsstop());
            sequences.add(s);
        }
        
        OligoPlateManager om = new OligoPlateManager(conn, project, workflow, numOfWellsOnPlate, isFullPlate, false, protocol);
        om.setIsSetOpenClose(true);
        om.setIsOpenOnly(isFusion);
        om.setIsCloseOnly(isClosed);
        om.setIsReorder(false);
        om.createOligoPlate(sequences, container.getId());
        om.sendOligoOrders();
        
        OligoPlater plater = om.getOligoPlater();
        Container c5p = plater.get5pContainer();
        Container c3f = plater.get3fContainer();
        Container c3c = plater.get3cContainer();
        
        if(c5p != null) rearrayedContainers.add(c5p);
        if(c3f != null) rearrayedContainers.add(c3f);
        if(c3c != null) rearrayedContainers.add(c3c);
    }
    
    //Create the rearrayed oligo plates from the source oligo plates for given samples.
    //Insert records into database.
    protected Plateset createRearrayedOligos(List samples, Container container, int threadid, Connection conn)
    throws FlexDatabaseException, FlexWorkflowException, SQLException, RearrayException {
        RearrayPlateMapCreator creator = new RearrayPlateMapCreator();
        creator.setReversedOligos(samples, conn);
        ArrayList samples5p = creator.createRearrayOligoSamples(samples, RearrayPlateMapCreator.OLIGO_5P, conn);
        String projectCode = pw.getCode();
        String label = Container.getLabel(projectCode, OligoPlater.oligoFivePrefix, threadid, null);
        Container container5p = createPlate(samples5p, OligoPlater.DEFAULTCONTAINERTYPE,OligoPlater.OLIGO_5P,threadid,label,conn);
        rearrayedOligo5pSamples.add(samples5p);
        
        Plateset plateset = new Plateset(container5p.getId(), -1, -1, container.getId());
        
        //fusion only or both
        if(!isClosed) {
            ArrayList samples3f = creator.createRearrayOligoSamples(samples, Construct.FUSION, conn);
            label = Container.getLabel(projectCode, OligoPlater.oligoFusionPrefix, threadid, null);
            Container container3f = createPlate(samples3f, OligoPlater.DEFAULTCONTAINERTYPE,OligoPlater.OLIGO_3F,threadid,label,conn);
            plateset.setContainerid3pFusion(container3f.getId());
            rearrayedOligo3fSamples.add(samples3f);
        }
        
        //closed only or both
        if(!isFusion) {
            ArrayList samples3c = creator.createRearrayOligoSamples(samples, Construct.CLOSED, conn);
            label = Container.getLabel(projectCode, OligoPlater.oligoClosePrefix, threadid, null);
            Container container3c = createPlate(samples3c, OligoPlater.DEFAULTCONTAINERTYPE,OligoPlater.OLIGO_3C,threadid,label,conn);
            plateset.setContainerid3pClosed(container3c.getId());
            rearrayedOligo3cSamples.add(samples3c);
        }
        
        plateset.insert(conn);
        return plateset;
    }
    
    //Get samples from a list of samples.
    protected ArrayList getSamples(List samples) {
        ArrayList newSamples = new ArrayList();
        for(int i=0; i<samples.size(); i++) {
            ArrayList currentSamples = (ArrayList)samples.get(i);
            newSamples.addAll(currentSamples);
        }
        
        return newSamples;
    }
    
    //Write the rearrayed file for a given samples.
    protected void writeFile(String filename, List samples) throws IOException {
        filename = filename+".txt";
        File file = new File(FILEPATH+filename);
        FileWriter fr = new FileWriter(file);
        fr.write("Original plate label"+DILIM+"Original plate well"+DILIM+"Destination plate label"+ DILIM+  "Destination plate well\n");
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            fr.write(sample.getSourcePlateLabel()+DILIM+sample.getSourceWell()+DILIM+sample.getDestPlateLabel()+DILIM+sample.getDestWell()+"\n");
        }
        fr.flush();
        fr.close();
        
        files.add(file);
    }
    
    //Put the plateset containing rearrayed plate and oligo plates on queue for next protocol.
    protected void putPlatesetOnQueue(Plateset ps, Connection conn) throws FlexDatabaseException {
        LinkedList queueItems = new LinkedList();
        PlatesetProcessQueue platesetQueue = new PlatesetProcessQueue();
        
        Vector protocols = workflow.getNextProtocol(protocol);
        
        //put on queue
        for (int i = 0; i < protocols.size(); i++) {
            Protocol pr = (Protocol) protocols.get(i);
            QueueItem queueItem = new QueueItem( ps, pr, project, workflow);
            queueItems.add(queueItem);
        }
        
        platesetQueue.addQueueItems(queueItems, conn);
    }
    
    //Add individual sample list to one list.
    protected void addToAllSamples() {
        allRearrayedSamples.put("rearray", rearrayedSamples);
        allRearrayedSamples.put("rearray_5p", rearrayedOligo5pSamples);
        allRearrayedSamples.put("rearray_3f", rearrayedOligo3fSamples);
        allRearrayedSamples.put("rearray_3c", rearrayedOligo3cSamples);
    }
    
    //add all the samples with cloneid != 0 in container to clonestorage table.
    protected void addToStorage(Container c, Connection conn) throws FlexDatabaseException, SQLException {
        Vector samples = c.getSamples();
        CloneStorageSet cs = new CloneStorageSet();
        
        for(int i=0; i<samples.size(); i++) {
            Sample s = (Sample)samples.get(i);
            if(s.getCloneid() > 0) {
                CloneStorage cloneStorage = new CloneStorage(s.getId(), destStorageType, destStorageForm, s.getCloneid());
                cloneStorage.setContainerid(c.getId());
                cloneStorage.setLabel(c.getLabel());
                cloneStorage.setPosition(s.getPosition());
                cs.add(cloneStorage);
            }
        }
        
        cs.insert(conn);
    }
    
    protected List checkSourceDup(List samples) {
        GenericRearrayer rearrayer = new GenericRearrayer();
        List sortedSamples = rearrayer.groupBySource(samples);
        RearrayPlateMap last = null;
        List duplicates = new ArrayList();
        
        for(int i=0; i<sortedSamples.size(); i++) {
            RearrayPlateMap current = (RearrayPlateMap)sortedSamples.get(i);
            
            if(last != null) {
                if(last.getSourcePlateid() == current.getSourcePlateid() &&
                last.getSourceWell() == current.getSourceWell()) {
                    duplicates.add(last);
                }
            }
            last = current;
        }
        
        return duplicates;
    }
    
    /********************************************************************************
     *                          Test
     ********************************************************************************/
    public static void main(String args[]) {
        //String file = "G:\\rearraytest_source.txt";
        //String file = "G:\\rearraytest_dest.txt";
        //String file = "G:\\rearraytest_dest_dup.txt";
        String file = "G:\\rearraytest_clone.txt";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            InputStream input = new FileInputStream(file);
            RearrayManager manager = new RearrayManager(input);
            manager.setIsClone(true);
            manager.setIsWellAsNumber(false);
            manager.setIsArrangeByFormat(true);
            //manager.setIsControl(true);
            //manager.setNumOfControls(2);
            //manager.setIsFullPlate(true);
            manager.setIsOneFile(false);
            //manager.setIsOligo(true);
            //manager.setIsFusion(true);
            //manager.setIsClosed(true);
            //manager.setIsNewOligo(true);
            //manager.setIsOnQueue(true);
            //manager.setSortBy(GenericRearrayer.SORT_BY_SAWTOOTH);
            //manager.setSortBy(GenericRearrayer.SORT_BY_SOURCE);
            //manager.setIsArrangeBySize(true);
            //manager.setSizeLower(600);
            //manager.setSizeHigher(800);
            //manager.setIsSmall(true);
            //manager.setIsMedium(true);
            //manager.setIsLarge(true);
            //manager.setIsDestPlateSet(true);
            
            manager.setResearcherBarcode("joy");
            manager.setProjectid(2);
            manager.setWorkflowid(21);
            manager.setProtocolName(Protocol.REARRAY_DIST_GLYCEROL);
            //manager.setIsStorage(true);
            manager.setStorageForm(StorageForm.GLYCEROL);
            manager.setStorageType(StorageType.WORKING);
            //manager.setDestStorageForm(StorageForm.GLYCEROL);
            //manager.setDestStorageType(StorageType.ARCHIVE);
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            manager.createAllPlates(conn);
            manager.writeOutputFiles();
            manager.sendRobotFiles("dzuo@hms.harvard.edu");
            DatabaseTransaction.commit(conn);
        } catch (Exception e) {
            DatabaseTransaction.rollback(conn);
            System.out.println(e.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
    
    private static void printSamples(List samples) {
        System.out.println("========================================================");
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            System.out.println("source plate ID:\t"+sample.getSourcePlateid());
            System.out.println("source Plate label:\t"+sample.getSourcePlateLabel());
            System.out.println("source well:\t"+sample.getSourceWell());
            System.out.println("source sample:\t"+sample.getSampleid());
            System.out.println("construct ID:\t"+sample.getConstructid());
            System.out.println("construct type:\t"+sample.getConstructtype());
            System.out.println("dest plate label:\t"+sample.getDestPlateLabel());
            System.out.println("dest well:\t"+sample.getDestWell());
            System.out.println("sequence ID:\t"+sample.getSequenceid());
            System.out.println("CDS start:\t"+sample.getCdsstart());
            System.out.println("CDS stop:\t"+sample.getCdsstop());
            System.out.println("CDS length:\t"+sample.getCdslength());
            System.out.println("oligo 5p:\t"+sample.getOligoid5p());
            System.out.println("oligo 3p:\t"+sample.getOligoid3p());
            System.out.println("oligo 3p reversed:\t"+sample.getOligoid3pReversed());
            System.out.println();
        }
    }
}