/*
 * OligoPlater.java
 * @date    06052001
 * @author  Wendy Mar
 *
 * This class first sort a list of OligoPattern objects according to
 * the cds length of the sequence which the oligos are derived from.
 * Then, the oligo samples are arranged in a saw-tooth pattern on the
 * oligo plates. Three files for oligo ordering are generated.
 *
 * modified 062801 wmar:    added method to output threee oligo order files
 *
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
import java.sql.*;
import java.io.*;


public class OligoPlater {
    // private final String plateOutFileName1 =
    // FileRepository.REPOSITORY_PATH+FileRepository.OLIGO_ORDER_PATH+uniqueFileName;
    private static final String filePath = "H:/Dev/OligoOrder/";
    private static final int positiveControlPosition = 1;
    private static final int negativeControlPosition = 96;
    private static final String PositiveControlSampleType = "CONTROL_POSITIVE";
    private static final String NegativeControlSampleType = "CONTROL_NEGATIVE";
    
    private LinkedList oligoPatternList;
    private LinkedList constructList;
    private Plateset plateset;
    private Connection conn;
    private Container container_5p;
    private Container container_3s;
    private Container container_3op;
    
    private String plateOutFileName1 = null;
    private String plateOutFileName2 = null;
    private String plateOutFileName3 = null;
    private FileWriter plateWriter_5p = null;
    private FileWriter plateWriter_3s = null;
    private FileWriter plateWriter_3op = null;
    
    /** Creates new OligoPlater */
    public OligoPlater(LinkedList oligoPatternList, LinkedList constructList, Connection c)
    throws FlexDatabaseException, IOException {
        this.oligoPatternList = oligoPatternList;
        this.constructList = constructList;
        this.conn = c;
        
    }
    
    /**
     * This class generates three oligo plates, sort the samples on the plate.
     * The containerheader, sample and containercell tables are updated
     */
    public void createOligoPlates() throws FlexDatabaseException, IOException {
        
        generateOligoPlate();
        generateControlSamples();
        generateOligoOrder();
        container_5p.insert(conn);
        container_3s.insert(conn);
        container_3op.insert(conn);
        
        plateset.insert(conn);
        
        //update the platesetid column in containerheader table
        int platesetId = plateset.getId();
        container_5p.updatePlatesetId(platesetId,conn);
        container_3s.updatePlatesetId(platesetId,conn);
        container_3op.updatePlatesetId(platesetId,conn);
        
    }
    
    
    /**
     * This method creates three oligo order file objects
     * and store them in a list
     */
    public LinkedList generateOligoOrderFiles(){
        LinkedList fileList = new LinkedList();
        File file_5p = new File(plateOutFileName1);
        File file_3s = new File(plateOutFileName2);
        File file_3op = new File(plateOutFileName3);
        fileList.add(file_5p);
        fileList.add(file_3s);
        fileList.add(file_3op);
        
        return fileList;
    }
    
    /**
     * Precondition: all three of the oligo plate headers are created
     * and inserted. all of the oligos are calculated and inserted.
     * First, the oligos are sorted by cds-length and arranged by
     * saw-tooth pattern based on cds-length.
     * Second, the oligo sample objecsts are generated and the sample
     * records are added to the container.
     *
     */
    protected void generateOligoOrder() throws FlexDatabaseException, IOException{
        String geneId = null;
        String oligo5p = null;
        String oligo3s = null;
        String oligo3op = null;
        
        int containerId_5p = container_5p.getId();
        int containerId_3s = container_3s.getId();
        int containerId_3op = container_3op.getId();
        
        int cdsLength = 0;
        boolean done = false;
        int geneNum = 0;
        
        GeneComparator comparator = new GeneComparator();
        
        // Sort oligos in ascending order of CDS length.
        OligoPattern[] geneArray = new OligoPattern[0];
        geneArray = (OligoPattern[]) oligoPatternList.toArray(geneArray);
        oligoPatternList = null;
        System.out.println("Sorting.");
        Arrays.sort(geneArray,comparator);
        
        // Map genes to plates and wells, with 94 genes per plate.
        // The arrangement of oligos are based  on the CDS length.
        int firstGeneIndexOfPlate = 0;
        int lastGeneIndexOfPlate = 0;
        int currentGeneIndex = -1;
        int well = 0;
        int numGenes = geneArray.length;
        boolean fullPlate = true;
        done = false;
        boolean wellsDone = false;
        int relativeLowerIndex = 0;
        int relativeUpperIndex = 0;
        
        OligoPattern currentGene = null;
        Sample oligoSample_5p = null;
        Sample oligoSample_3s = null;
        Sample oligoSample_3op = null;
        
        //prepare files for oligo order
        plateOutFileName1 = filePath + container_5p.getLabel();
        plateOutFileName2 = filePath + container_3s.getLabel();
        plateOutFileName3 = filePath + container_3op.getLabel();
        
        plateWriter_5p = new FileWriter(plateOutFileName1);
        plateWriter_3s = new FileWriter(plateOutFileName2);
        plateWriter_3op = new FileWriter(plateOutFileName3);
        
        // Loop over plates.
        while (!done) {
            
            // array index starts at 0
            firstGeneIndexOfPlate = currentGeneIndex+1;
            lastGeneIndexOfPlate = firstGeneIndexOfPlate+93;
            
            // check whether there are enough oligos to fill a 96 well plate
            if (lastGeneIndexOfPlate >= numGenes) {
                fullPlate = false; //not a fully filled oligo plate
                done = true;
                // Calculate last gene index of plate.
                lastGeneIndexOfPlate = numGenes-1;
                if (firstGeneIndexOfPlate >= numGenes) {
                    continue;
                }
                relativeLowerIndex = -1;
                relativeUpperIndex = ((lastGeneIndexOfPlate-firstGeneIndexOfPlate)/2);
            }
            else {
                fullPlate = true;  // a fully filled oligo plate
                relativeLowerIndex = -1;
                relativeUpperIndex = 46;
            } //outter if
            
            // Loop over wells.
            well = 1;
            wellsDone = false;
            while (!wellsDone) {
                ++well; //oligo sample starts at well 2
                ++currentGeneIndex;  //starts at 0
                ++relativeLowerIndex;
                ++relativeUpperIndex;
                
                if ((firstGeneIndexOfPlate + relativeUpperIndex) > lastGeneIndexOfPlate) {
                    wellsDone = true;
                    continue;
                }
                
                System.out.println("gene index: "+currentGeneIndex);
                // Output lower gene.
                currentGene = geneArray[currentGeneIndex];
                
                // generate three oligo sample objects and insert into sample table
                // also a record for each oligo sample is inserted into containercell table
                oligoSample_5p = generateOligoSample(currentGene,"5p", containerId_5p, well);
                System.out.println("PlateID: "+containerId_5p +"; " + "5p lower oligo well: "+ well + "; "
                + "sampleID: "+ oligoSample_5p.getId() +"; "+ "oligoID: "+oligoSample_5p.getOligoid()+"; " + currentGene.getCDSLength());
                container_5p.addSample(oligoSample_5p);
                
                plateWriter_5p.write(container_5p.getLabel()+"\t");
                plateWriter_5p.write(currentGene.getOligoId_5p()+"\t");
                plateWriter_5p.write(currentGene.getOligoseq_5p()+"\t");
                plateWriter_5p.write(oligoSample_5p.getPosition()+"\n");
                
                oligoSample_3s = generateOligoSample(currentGene,"3s", containerId_3s, well);
                System.out.println("PlateID: "+containerId_3s+"; " + "3s lower oligo well: "+ well + "; "
                + "sampleID: "+ oligoSample_3s.getId() +"; " + currentGene.getCDSLength());
                container_3s.addSample(oligoSample_3s);
                
                plateWriter_3s.write(container_3s.getLabel()+"\t");
                plateWriter_3s.write(currentGene.getOligoId_3s()+"\t");
                plateWriter_3s.write(currentGene.getOligoseq_3s()+"\t");
                plateWriter_3s.write(oligoSample_3s.getPosition()+"\n");
                
                oligoSample_3op = generateOligoSample(currentGene,"3op", containerId_3op, well);
                System.out.println("PlateID: "+containerId_3op+"; " + "3op lower oligo well: "+ well + "; "
                + "sampleID: "+ oligoSample_3op.getId() +"; " + currentGene.getCDSLength());
                container_3op.addSample(oligoSample_3op);
                
                plateWriter_3op.write(container_3op.getLabel()+"\t");
                plateWriter_3op.write(currentGene.getOligoId_3op()+"\t");
                plateWriter_3op.write(currentGene.getOligoseq_3op()+"\t");
                plateWriter_3op.write(oligoSample_3op.getPosition()+"\n");
                
                ++well;
                // check whether plate is full
                if (relativeUpperIndex > lastGeneIndexOfPlate-firstGeneIndexOfPlate) {
                    wellsDone = true;
                    continue;
                }
                
                // Output upper gene.
                currentGene = geneArray[firstGeneIndexOfPlate + relativeUpperIndex];
                
                // generate three oligo sample objects and insert them into the sample table
                oligoSample_5p = generateOligoSample(currentGene,"5p", containerId_5p, well);
                System.out.println("PlateID: "+containerId_5p +"; " + "5p upper oligo well: "+ well + "; "
                + "sampleID: "+ oligoSample_5p.getId() + "; "+"oligoID: "+oligoSample_5p.getOligoid()+"; " + currentGene.getCDSLength());
                container_5p.addSample(oligoSample_5p);
                
                plateWriter_5p.write(container_5p.getLabel()+"\t");
                plateWriter_5p.write(currentGene.getOligoId_5p()+"\t");
                plateWriter_5p.write(currentGene.getOligoseq_5p()+"\t");
                System.out.println("5p oligo seq is: "+ currentGene.getOligoseq_5p());
                plateWriter_5p.write(oligoSample_5p.getPosition()+"\n");
                
                oligoSample_3s = generateOligoSample(currentGene,"3s", containerId_3s, well);
                System.out.println("PlateID: "+containerId_3s+"; " + "3s upper oligo well: "+ well + "; "
                + "sampleID: "+ oligoSample_3s.getId() +"; " + currentGene.getCDSLength());
                container_3s.addSample(oligoSample_3s);
                
                plateWriter_3s.write(container_3s.getLabel()+"\t");
                plateWriter_3s.write(currentGene.getOligoId_3s()+"\t");
                plateWriter_3s.write(currentGene.getOligoseq_3s()+"\t");
                plateWriter_3s.write(oligoSample_3s.getPosition()+"\n");
                
                oligoSample_3op = generateOligoSample(currentGene,"3op", containerId_3op, well);
                System.out.println("PlateID: "+containerId_3op+"; " + "3op upper oligo well: "+ well + "; "
                + "sampleID: "+ oligoSample_3op.getId() +"; " + currentGene.getCDSLength());
                container_3op.addSample(oligoSample_3op);
                
                plateWriter_3op.write(container_3op.getLabel()+"\t");
                plateWriter_3op.write(currentGene.getOligoId_3op()+"\t");
                plateWriter_3op.write(currentGene.getOligoseq_3op()+"\t");
                plateWriter_3op.write(oligoSample_3op.getPosition()+"\n");
                
            } //inner while to fill oligo plate wells
            
            currentGeneIndex += 47;
            System.out.println("next plate gene index: "+currentGeneIndex);
        } //outter while to fill oligo plates
        
        //add controls to oligo plates
        
        plateWriter_5p.flush();
        plateWriter_5p.close();
        plateWriter_3s.flush();
        plateWriter_3s.close();
        plateWriter_3op.flush();
        plateWriter_3op.close();
    } // oligoSorter
    
    /**
     * This class insert three oligo plates (containerheader table)
     * and one plateset record (plateset table)
     */
    private void generateOligoPlate() throws FlexDatabaseException {
        Location location = null;
        String plateType = "96 WELL OLIGO PLATE";
        String locationType = "UNAVAILABLE";
        String label_5p = "default";
        String label_3s = "default";
        String label_3op = "default";
        
        // get the oligo container location object
        location = new Location();
        int locationId = location.getId(locationType);
        location = new Location(locationId,locationType,"");
        
        // generate new container header object
        container_5p = new Container(plateType, location,label_5p);
        System.out.println("Created the 5p oligo plate: "+ container_5p.getId());
        
        container_3s = new Container(plateType, location,label_3s);
        System.out.println("Created the 3s oligo plate: "+ container_3s.getId());
        
        container_3op = new Container(plateType, location,label_3op);
        System.out.println("Created the 3op oligo plate: "+ container_3op.getId());
        
        plateset = new Plateset(container_5p.getId(), container_3op.getId(),container_3s.getId());
        int platesetId = plateset.getId();
        label_5p = Container.getLabel("Ou",platesetId,null); //upstream
        label_3s = Container.getLabel("Oc",platesetId,null); //closed
        label_3op = Container.getLabel("Of",platesetId,null); //fusion
        
        container_5p.setLabel(label_5p);
        container_3s.setLabel(label_3s);
        container_3op.setLabel(label_3op);
        System.out.println("5p oligo plate label: "+ label_5p);
        System.out.println("3s oligo plate label: "+ label_3s);
        System.out.println("3op oligo plate label: "+ label_3op);
        
    }
    
    private Sample generateOligoSample(OligoPattern currentOligo, String oligoType, int plateId, int wellId)
    throws FlexDatabaseException {
        int oligoId;
        String sampleType = null;
        String status = "G";
        Sample sample = null;
        
        if (oligoType.equals("5p")) {
            oligoId = currentOligo.getOligoId_5p();
            sampleType = "OLIGO_5P";
        }
        else if (oligoType.equals("3s")) {
            oligoId = currentOligo.getOligoId_3s();
            sampleType = "OLIGO_3C";
        }
        else {
            oligoId = currentOligo.getOligoId_3op();
            sampleType = "OLIGO_3F";
        }
        
        sample = new Sample(sampleType, wellId, plateId, oligoId, status);
        
        return sample;
    } //generateOligoSample
    
    /**
     * This method adds one positive and one negative control to each of
     * the three oligo plates
     */
    private void generateControlSamples()throws FlexDatabaseException {
        Sample control_positive = null;
        Sample control_negative = null;
        //add positive control samples
        control_positive = new Sample(PositiveControlSampleType,positiveControlPosition,container_5p.getId());
        container_5p.addSample(control_positive);
        control_positive = new Sample(PositiveControlSampleType,positiveControlPosition,container_3s.getId());
        container_3s.addSample(control_positive);
        control_positive = new Sample(PositiveControlSampleType,positiveControlPosition,container_3op.getId());
        container_3op.addSample(control_positive);
        
        //add negative samples
        control_negative = new Sample(NegativeControlSampleType,negativeControlPosition,container_5p.getId());
        container_5p.addSample(control_negative);
        control_negative = new Sample(NegativeControlSampleType,negativeControlPosition,container_3s.getId());
        container_3s.addSample(control_negative);
        control_negative = new Sample(NegativeControlSampleType,negativeControlPosition,container_3op.getId());
        container_3op.addSample(control_negative);
        
    }
    
    /**
     * insert process execution io for "generate oligo order" protocol
     */
    protected void insertProcessInputOutput() throws FlexDatabaseException {
        Process process = null;
        Protocol protocol = new Protocol("generate oligo orders");
        Researcher r = new Researcher();
        String status = "S"; //SUCCESS
        String extraInfo = "";
        
        int userId = r.getId("SYSTEM");
        r = new Researcher(userId);
        
        // insert process execution record into process execution table
        process = new Process(protocol,status,r);
        
        System.out.println("Insert process execution for generate oligo orders...");
        System.out.println("Generate oligo orders Execution ID: "+ process.getExecutionid());
        
        //Insert one process input records for each output construct design
        //(two per sequence)
        ListIterator iter = constructList.listIterator();
        String ioType = "I";
        Construct construct = null;
        ConstructProcessObject cpo = null;
        int constructId = -1;
        System.out.println("Inserting construct process output object...");
        while (iter.hasNext()){
            construct = (Construct)iter.next();
            constructId = construct.getId();
            cpo = new ConstructProcessObject(constructId,process.getExecutionid(),ioType);
            process.addProcessObject(cpo);
            
        } //while
        process.insert(conn);
        
        //insert the container output object
        ioType = "O";
        ContainerProcessObject platepo_5p =
        new ContainerProcessObject(container_5p.getId(),process.getExecutionid(),ioType);
        platepo_5p.insert(conn);
        ContainerProcessObject platepo_3s =
        new ContainerProcessObject(container_3s.getId(),process.getExecutionid(),ioType);
        platepo_3s.insert(conn);
        ContainerProcessObject platepo_3op =
        new ContainerProcessObject(container_3op.getId(),process.getExecutionid(),ioType);
        platepo_3op.insert(conn);
    } //inputProcessInputOutput
    
    /**
     * insert "receive oligo plates" queue record for each plate created
     */
    protected void insertReceiveOligoQueue() throws FlexDatabaseException {
        Protocol protocol = new Protocol("receive oligo plates");
        
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        QueueItem queueItem = null;
        LinkedList containerQueueItemList = new LinkedList();
        queueItem = new QueueItem(container_5p, protocol);
        containerQueueItemList.add(queueItem);
        queueItem = new QueueItem(container_3s, protocol);
        containerQueueItemList.add(queueItem);
        queueItem = new QueueItem(container_3op, protocol);
        containerQueueItemList.add(queueItem);
        
        System.out.println("Adding receive oligo plates to queue...");
        containerQueue.addQueueItems(containerQueueItemList, conn);
    } //insertReceiveOligoQueue
    
    
    /** Note: this comparator imposes orderings that are
     * inconsistent with equals. */
    class GeneComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            int result = 0;
            OligoPattern lhs = (OligoPattern) o1;
            OligoPattern rhs = (OligoPattern) o2;
            int lhsLength = lhs.getCDSLength();
            int rhsLength = rhs.getCDSLength();
            result = lhsLength-rhsLength;
            return result;
        }
        
        public boolean equals(java.lang.Object obj) {
            return false;
        }
        // compare
        
    } //GeneComparator
    
    
    
}
