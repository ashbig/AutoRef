/*
 * GenericRearrayer.java
 *
 * This class implements all the business rules applied to rearray.
 *
 * Created on May 15, 2003, 1:14 PM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.io.*;
import java.sql.*;

import edu.harvard.med.hip.flex.util.Algorithms;
import edu.harvard.med.hip.flex.core.Construct;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class GenericRearrayer {
    public static final int SORT_BY_NONE = 1;
    public static final int SORT_BY_SOURCE = 2;
    public static final int SORT_BY_SAWTOOTH = 3;
    public static final String FUSION = Construct.FUSION;
    public static final String CLOSED = Construct.CLOSED;
    public static final int SIZELOWER = Construct.SIZELOWER;
    public static final int SIZEHIGHER = Construct.SIZEHIGHER;
    
    /** Creates a new instance of GenericRearrayer */
    public GenericRearrayer() {}
    
    /**
     * Sort the samples based on source plate. Then sort the list based on
     * the number of samples on each plate by descending order.
     * Also sort the samples on each plate based on well position.
     *
     * @param samples A list of samples.
     * @return A list of sorted samples.
     */
    public ArrayList groupBySource(List samples) {
        Hashtable containers = new Hashtable();
        ArrayList newSamples = new ArrayList();
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap s = (RearrayPlateMap)samples.get(i);
            String label = s.getSourcePlateLabel();
            
            if ( ! containers.keySet().contains(label) ) {
                ArrayList l = new ArrayList();
                l.add(s);
                containers.put(label, l);
            } else {
                ArrayList l = (ArrayList)containers.get(label);
                l.add(s);
            }
        }
        
        //sort by number of samples on the container in descending order
        LinkedList sortedSamples = new LinkedList();
        Enumeration enum = containers.keys();
        while(enum.hasMoreElements()) {
            String k = (String)enum.nextElement();
            ArrayList allSamples = (ArrayList)containers.get(k);
            Collections.sort(allSamples, new SourceWellComparator());
            
            boolean isAdded = false;
            for(int j=0; j<sortedSamples.size(); j++) {
                if(allSamples.size() >= ((ArrayList)sortedSamples.get(j)).size()) {
                    sortedSamples.add(j, allSamples);
                    isAdded = true;
                    break;
                }
            }
            
            if(!isAdded) {
                sortedSamples.addLast(allSamples);
            }
        }
        
        for(int i=0; i<sortedSamples.size(); i++) {
            ArrayList s = (ArrayList)sortedSamples.get(i);
            newSamples.addAll(s);
        }
        
        return newSamples;
    }
    
    /**
     * Group the samples by destination plate assuming user provided destination plate
     * numbers and well positions. Samples are also sorted by destination well positions.
     *
     * @param samples A list of samples.
     * @return A sorted list of samples.
     */
    public ArrayList groupByDest(List samples) throws Exception {
        Hashtable containers = new Hashtable();
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap s = (RearrayPlateMap)samples.get(i);
            String label = s.getDestPlateLabel();
            
            if ( ! containers.keySet().contains(label) ) {
                ArrayList l = new ArrayList();
                l.add(s);
                containers.put(label, l);
            } else {
                ArrayList l = (ArrayList)containers.get(label);
                l.add(s);
            }
        }
        
        ArrayList results = new ArrayList();
        Enumeration enum = containers.keys();
        while(enum.hasMoreElements()) {
            String k = (String)enum.nextElement();
            ArrayList s = (ArrayList)containers.get(k);
            Collections.sort(s, new DestWellComparator());
            
            //check for duplicate well positions
            String lastLabel = null;
            int lastPosition = -1;
            for(int i=0; i<s.size(); i++) {
                RearrayPlateMap rs = (RearrayPlateMap)s.get(i);
                String label = rs.getDestPlateLabel();
                int position = rs.getDestWell();
                
                if(lastLabel == null && lastPosition == -1) {
                    lastLabel = label;
                    lastPosition = position;
                } else {
                    if(lastLabel.equals(label) && lastPosition == position) {
                        throw new Exception("Duplicate destination wells: "+rs.getRearrayInputSample().getDestPlate()+": "+rs.getRearrayInputSample().getDestWell());
                    } else {
                        lastLabel = label;
                        lastPosition = position;
                    }
                }
            }
            
            results.add(s);
        }
        
        return results;
    }
    
    /**
     * Sort the samples by Saw-Tooth pattern based on CDS length.
     *
     * @param samples A list of samples.
     * @return A list of sorted samples.
     */
    public ArrayList sortBySawTooth(List samples) {
        Collections.sort(samples, new CDSComparator());
        ArrayList result = Algorithms.rearrangeInSawToothPattern(samples);
        return result;
    }
    
    /**
     * Sort the samples into different groups based on the construct format (fusion, close, etc).
     * Samples with illegal format with be grouped into one separate group.
     *
     * @param samples A list of samples.
     * @return A list of sorted samples.
     */
    public ArrayList groupByFormat(List samples){
        ArrayList fusionSamples = new ArrayList();
        ArrayList closedSamples = new ArrayList();
        ArrayList unknownSamples = new ArrayList();
        ArrayList results = new ArrayList();
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            if(FUSION.equals(sample.getConstructtype())) {
                fusionSamples.add(sample);
            } else if(CLOSED.equals(sample.getConstructtype())) {
                closedSamples.add(sample);
            } else {
                unknownSamples.add(sample);
            }
        }
        
        if(fusionSamples.size() != 0)
            results.add(fusionSamples);
        if(closedSamples.size() != 0)
            results.add(closedSamples);
        if(unknownSamples.size() != 0)
            results.add(unknownSamples);
        
        return results;
    }
    
    /**
     * Return the samples belong to the specified CDS size range.
     *
     * @param samples The list of samples.
     * @int lower The lower limit (samples >= lower will be returned).
     * @int upper The upper limit (samples < upper will be returned). -1 means no upper limit.
     * @return A list of samples within the specified CDS size range.
     */
    public ArrayList getSequencesBySize(List samples, int lower, int upper) {
        ArrayList results = new ArrayList();
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            int length = sample.getCdslength();
            
            if(length>=lower) {
                if(upper < 0) {
                    results.add(sample);
                } else if(length<upper) {
                    results.add(sample);
                }
            }
        }
        
        return results;
    }
    
    //---------------------------------------------------------------------------------//
    //                          Internal Classes
    //---------------------------------------------------------------------------------//
    
    public class SourceWellComparator implements Comparator {
        
        public SourceWellComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            int id1 = ((RearrayPlateMap)p1).getSourceWell();
            int id2 = ((RearrayPlateMap)p2).getSourceWell();
            
            if(id1 < id2)
                return -1;
            
            if(id1 == id2)
                return 0;
            
            if(id1 > id2)
                return 1;
            
            return -1;
        }
    }
    
    public class DestWellComparator implements Comparator {
        
        public DestWellComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            int id1 = ((RearrayPlateMap)p1).getDestWell();
            int id2 = ((RearrayPlateMap)p2).getDestWell();
            
            if(id1 < id2)
                return -1;
            
            if(id1 == id2)
                return 0;
            
            if(id1 > id2)
                return 1;
            
            return -1;
        }
    }
    
    public class CDSComparator implements Comparator {
        
        public CDSComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            int id1 = ((RearrayPlateMap)p1).getCdslength();
            int id2 = ((RearrayPlateMap)p2).getCdslength();
            
            if(id1 < id2)
                return -1;
            
            if(id1 == id2)
                return 0;
            
            if(id1 > id2)
                return 1;
            
            return -1;
        }
    }
    
    /********************************************************************************
     *                          Test
     ********************************************************************************/
    
    public static void main(String args[]) throws IOException, FlexDatabaseException, SQLException {
        String file = "G:\\rearraytest1.txt";
        String file2 = "G:\\rearraytest2.txt";
        String file3 = "G:\\rearraytest4.txt";
        ArrayList samples = null;
        ArrayList newsamples = null;
        
        GenericRearrayer rearrayer = new GenericRearrayer();
        /*
        samples = readFile(file);
         
        System.out.println("Samples before rearray");
        printSamples(samples);
         
        System.out.println("Group by source");
        newsamples = rearrayer.groupBySource(samples);
         
        for(int i=0; i<newsamples.size(); i++) {
            ArrayList currentSamples = (ArrayList)newsamples.get(i);
            printSamples(currentSamples);
        }
         
        System.out.println("Group by format");
        newsamples = rearrayer.groupByFormat(samples);
        for(int i=0; i<newsamples.size(); i++) {
            ArrayList currentSamples = (ArrayList)newsamples.get(i);
            printSamples(currentSamples);
        }
         
        System.out.println("Sort by saw-tooth");
        newsamples = rearrayer.sortBySawTooth(samples);
        printSamples(newsamples);
         
        System.out.println("Group by size");
        newsamples = rearrayer.getSequencesBySize(samples, 500, 1000);
        printSamples(newsamples);
         */
        
        //samples = readFile(file2);
        samples = readFile(file3);
        
        System.out.println("Samples before rearray");
        printSamples(samples);
        
        try {
            newsamples = rearrayer.groupByDest(samples);
            
            System.out.println("Group by destination plate");
            for(int i=0; i<newsamples.size(); i++) {
                ArrayList currentSamples = (ArrayList)newsamples.get(i);
                printSamples(currentSamples);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.exit(0);
    }
    
    private static ArrayList readFile(String file)
    throws IOException, FlexDatabaseException, SQLException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line = null;
        ArrayList inputSamples = new ArrayList();
        
        while((line = (in.readLine())) != null) {
            if(line.trim() == null || line.trim().equals(""))
                continue;
            
            StringTokenizer st = new StringTokenizer(line, "\t");
            String info[] = new String[4];
            int i = 0;
            while(st.hasMoreTokens()) {
                info[i] = st.nextToken();
                i++;
            }
            
            RearrayInputSample s = new RearrayInputSample(info[0],info[1],info[2],info[3],false);
            inputSamples.add(s);
        }
        in.close();
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        RearrayPlateMapCreator creator = new RearrayPlateMapCreator(true, false);
        ArrayList samples = creator.createRearraySamples(inputSamples, conn);
        
        return samples;
    }
    
    private static void printSamples(List samples) {
        System.out.println("========================================================");
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            System.out.println("source plate ID:\t"+sample.getSourcePlateid());
            System.out.println("source Plate label:\t"+sample.getSourcePlateLabel());
            System.out.println("source well:\t"+sample.getSourceWell());
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
