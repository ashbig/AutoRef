/*
 * OligoPlater.java
 * @date    06052001
 * @author  Wendy Mar
 * @version
 *
 * This class first sort a list of OligoPattern objects according to
 * the cds length of the sequence which the oligos are derived from.
 * Then, the oligo samples are arranged in a saw-tooth pattern on the
 * oligo plates.
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
import java.sql.*;


public class OligoPlater {
    private LinkedList oligoPatternList;
    private int platesetId;
    private Connection conn;
    private int containerId_5p;
    private int containerId_3s;
    private int containerId_3op;
    
    
    /** Creates new OligoPlater */
    public OligoPlater(LinkedList oligoPatternList, int platesetId, Connection c)  {
        this.oligoPatternList = oligoPatternList;
        this.platesetId = platesetId;
        this.conn = c;
    }
    
    public void createOligoPlates () throws FlexDatabaseException {
        generateOligoPlate();
        oligoSampleSorter();    
    }
    
    /**
     * Precondition: all three of the oligo plate headers are created
     * and inserted. all of the oligos are calculated and inserted.
     * First, the oligos are sorted by cds-length and arranged by
     * saw-tooth pattern based on cds-length.
     * Second, the oligo sample objecsts are generated and the sample
     * records are inserted into the sample table.
     * Third, the containercell table is populated.
     *
     */
    public void oligoSampleSorter() throws FlexDatabaseException {
        String geneId = null;
        String oligo5p = null;
        String oligo3s = null;
        String oligo3op = null;
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
        int plateNum = 0;
        String plateId = null;
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
        
        // Loop over plates.
        while (!done) {
            ++plateNum;
            plateId = String.valueOf(plateNum);
            
            System.out.println("Starting plate: "+plateId);
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
                oligoSample_5p.insert(conn);
                insertContainercell(containerId_5p,well,oligoSample_5p.getId());
                
                oligoSample_3s = generateOligoSample(currentGene,"3s", containerId_3s, well);
                oligoSample_3s.insert(conn);
                insertContainercell(containerId_3s,well,oligoSample_3s.getId());
                
                oligoSample_3op = generateOligoSample(currentGene,"3op", containerId_3op, well);
                oligoSample_3op.insert(conn);
                insertContainercell(containerId_3op,well,oligoSample_3op.getId());
                
                System.out.println(plateId+"\t");
                System.out.println(String.valueOf(well));
                System.out.println("\n");
                
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
                oligoSample_5p.insert(conn);
                insertContainercell(containerId_5p,well,oligoSample_5p.getId());
                
                oligoSample_3s = generateOligoSample(currentGene,"3s", containerId_3s, well);
                oligoSample_3s.insert(conn);
                insertContainercell(containerId_3s,well,oligoSample_3s.getId());
                
                oligoSample_3op = generateOligoSample(currentGene,"3op", containerId_3op, well);
                oligoSample_3op.insert(conn);
                insertContainercell(containerId_3op,well,oligoSample_3op.getId());
                
                // _plateWriter.write(currentGene.getId()+"\t");
                // _plateWriter.write(currentGene.getLength()+"\t");
                System.out.println(plateId+"\t");
                System.out.println(String.valueOf(well));
                System.out.println("\n");
                // _plateWriter.flush();
                
            } //inner while to fill oligo plate wells
            
            currentGeneIndex += 47;
        } //outter while to fill oligo plates
        
    } // oligoSorter
    
    /**
     * set the oligo plateId
     *
     * @return The plateId
     */
    private int setPlateId() throws FlexDatabaseException {
        try{
            int   plateId = FlexIDGenerator.getID("containerid");
            return plateId;
        }catch(FlexDatabaseException sqlex){
            throw new FlexDatabaseException(sqlex);
        }
    }
    
    /**
     * This class insert three oligo plates (containerheader table) 
     * and one plateset record (plateset table)
     */
    private void generateOligoPlate() throws FlexDatabaseException {
        Location location = null;
        String plateType = "96 WELL OLIGO PLATE";
        String locationType = "UNAVAILABLE";
        String label = "";
        Container container_5p = null;
        Container container_3s = null;
        Container container_3op = null;
        Plateset plateset = null;
        
        // get the oligo container location object
        location = new Location();
        int locationId = location.getId(locationType);
        location = new Location(locationId,locationType,"");
        
        // generate container id for the group of three oligo plates
        containerId_5p = setPlateId();
        containerId_3s = setPlateId();
        containerId_3op = setPlateId();
                
        // generate new container header object and insert into ContainerHeader table
        container_5p = new Container(containerId_5p, plateType, location,label);
        container_5p.insert(conn);
        container_3s = new Container(containerId_3s, plateType, location,label);
        container_3s.insert(conn);
        container_3op = new Container(containerId_3op, plateType, location,label);
        container_3op.insert(conn);
        
        //insert one plateset record identifying the three new plates
        plateset = new Plateset(platesetId, containerId_5p, containerId_3op,containerId_3s);
        plateset.insertPlateset(conn);
            
    }
    
    private Sample generateOligoSample(OligoPattern currentOligo, String oligoType, int plateId, int wellId) throws FlexDatabaseException {
        int oligoId;
        String sampleType = "OLIGO";
        String status = "GOOD";
        Sample sample = null;
        
        if (oligoType.equals("5p")) {
            oligoId = currentOligo.getOligoId_5p();
        }
        else if (oligoType.equals("3s")) {
            oligoId = currentOligo.getOligoId_3s();
        }
        else {
            oligoId = currentOligo.getOligoId_3op();
        }
        
        sample = new Sample(sampleType, wellId, plateId, oligoId, status);
        
        return sample;     
    } //generateOligoSample
    
    /**
     * This method insert the oligo sample record into the containercell table
     * precondition: containerheader and sample table are populated
     */
    private void insertContainercell (int plateId, int well, int sampleId) throws FlexDatabaseException {
        String sql = "INSERT INTO containercell\n"+
        "(containerid, position, sampleid\n"+
        "VALUES ("+plateId + "," +well+ "," +sampleId +")";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
    }
    
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
