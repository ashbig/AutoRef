/*
 * RearrayContainerMapper.java
 *
 * This class creates rearryed plate from source plates.
 *
 * Created on May 19, 2003, 10:44 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.io.*;
import java.sql.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class RearrayContainerMapper {
    private List samples = null;
    private Vector sampleLineageSet = null;
    private boolean isControl = false;
    private TreeSet sourceContainers = null;
    
    /** Creates a new instance of RearrayContainerMapper
     * @param samples A list of samples for rearrayed container.
     * @param isControl boolean value indicating whether control samples are required on the rearrayed plate.
     */
    public RearrayContainerMapper(List samples, boolean isControl) {
        this.samples = samples;
        this.isControl = isControl;
    }
    
    /**
     * Create the rearrayed container from source containers.
     *
     * @param containerType The rearrayed container type.
     * @param label The rearrayed container label.
     * @param threadId The thread ID for the rearrayed container.
     * @param location The Location object for the rearrayed container.
     * @param sampletype The rearrayed sample type.
     * @return A rearrayed container object.
     * @exception FlexDatabaseException.
     */
    public Container mapContainer(String containertype, String label, int threadId, Location location, String sampletype)
    throws FlexDatabaseException {
        sampleLineageSet = new Vector();
        sourceContainers = new TreeSet(new ContainerComparator());
        
        Container container = new Container(containertype, location, label, threadId );
        int containerid = container.getId();
        
        int position = 1; //sample position indexed on 0
        
        if(isControl) {
            Sample controlPositive = new Sample(Sample.CONTROL_POSITIVE, position, containerid, -1, Sample.GOOD);
            container.addSample(controlPositive);
            position++;
        }
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            
            int sourcePlateId = sample.getSourcePlateid();
            sourceContainers.add(new Container(sourcePlateId, null, null, null));
            
            //Sample s = new Sample(sampletype, position, containerid, sample.getConstructid(),-1, Sample.GOOD);
            Sample s = new Sample(sample.getSampletype(), position, containerid, sample.getConstructid(),-1, Sample.GOOD);
            s.setCloneid(sample.getCloneid());
            container.addSample(s);
            sampleLineageSet.addElement(new SampleLineage(sample.getSampleid(), s.getId()));
            
            sample.setDestPlateLabel(label);
            sample.setDestWell(position);
            
            position++;
        }
        
        if(isControl) {
            Sample controlNegative = new Sample(Sample.CONTROL_NEGATIVE,position,containerid, -1, Sample.BAD);
            container.addSample(controlNegative);
        }
        
        return container;
    }
    
    /**
     * Create the rearrayed container object based on the predefined mapper.
     *
     * @param containertype The rearrayed container type.
     * @param label The rearrayed container label.
     * @param threadId The thread ID for the rearrayed container.
     * @param location The location for the rearrayed container.
     * @param sampletype The sample type on the rearrayed container.
     * @return The rearrayed container object.
     * @exception FlexDatabaseException.
     */
    public Container mapContainerWithDest(String containertype, String label, int threadId, Location location, String sampletype)
    throws FlexDatabaseException {
        sampleLineageSet = new Vector();
        sourceContainers = new TreeSet(new ContainerComparator());
        Container container = new Container(containertype, location, label, threadId );
        int containerid = container.getId();
        
        for(int i=0; i<samples.size(); i++) {
            RearrayPlateMap sample = (RearrayPlateMap)samples.get(i);
            sourceContainers.add(new Container(sample.getSourcePlateid(), null, null, null));
            //Sample s = new Sample(sampletype, sample.getDestWell(), containerid, sample.getConstructid(), -1, Sample.GOOD);
            Sample s = new Sample(sample.getSampletype(), sample.getDestWell(), containerid, sample.getConstructid(), -1, Sample.GOOD);
            s.setCloneid(sample.getCloneid());
            container.addSample(s);
            sampleLineageSet.addElement(new SampleLineage(sample.getSampleid(), s.getId()));
            
            sample.setDestPlateLabel(label);
        }
        
        return container;
    }
    
    public Vector getSampleLineageSet() {
        return sampleLineageSet;
    }
    
    public Set getSourceContainers() {
        return sourceContainers;
    }
    
    //---------------------------------------------------------------------------------//
    //                          Internal Classes
    //---------------------------------------------------------------------------------//
    
    //Compare container objects by container ID.
    public class ContainerComparator implements Comparator {
        public ContainerComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            int id1 = ((Container)p1).getId();
            int id2 = ((Container)p2).getId();
            
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
    
    public static void main(String args[]) {
        String file = "G:\\rearraytest1.txt";
        String file2 = "G:\\rearraytest3.txt";
        ArrayList samples = null;
        Container container = null;
        RearrayContainerMapper mapper = null;
        
        try {
/*            
            samples = readFile(file);
            printSamples(samples);
            
            mapper = new RearrayContainerMapper(samples, true);
            container = mapper.mapContainer("96 WELL PLATE", "ABC000123", -1, new Location("FREEZER"), "ISOLATE");
            printSamples(samples);
            printContainer(container);
*/            
            samples = readFile(file2);
            printSamples(samples);
            
            mapper = new RearrayContainerMapper(samples, true);
            container = mapper.mapContainerWithDest("96 WELL PLATE", "ABC000123", -1, new Location("FREEZER"), "ISOLATE");
            printSamples(samples);
            printContainer(container);  
            
            Vector lineageset = mapper.getSampleLineageSet();
            printLineageset(lineageset);
            
            Set sourceContainers = mapper.getSourceContainers();
            printSourceContainers(sourceContainers);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        //samples = readFile(file2);
        
        
        System.exit(0);
    }
    
    private static ArrayList readFile(String file)
    throws IOException, FlexDatabaseException, SQLException, RearrayException {
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
    
    private static void printContainer(Container container) {
        System.out.println("Container ID: "+container.getId());
        System.out.println("Container label: "+container.getLabel());
        System.out.println("Container type: "+container.getType());
        System.out.println("Thread ID: "+container.getThreadid());
        System.out.println("Container location: "+container.getLocation().getType());
        System.out.println();
        
        Vector samples = container.getSamples();
        for(int i=0; i<samples.size(); i++) {
            Sample s = (Sample)samples.get(i);
            System.out.println("\tSample ID: "+s.getId());
            System.out.println("\tSample type: "+s.getType());
            System.out.println("\tSample positiion: "+s.getPosition());
            System.out.println("\tConstruct ID: "+s.getConstructid());
            System.out.println("\tOligo ID: "+s.getOligoid());
            System.out.println("\tSample status: "+s.getStatus());
            System.out.println();
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
    
    private static void printLineageset(Vector lineages) {
        System.out.println("===================================================");
        for(int i=0; i<lineages.size(); i++) {
            SampleLineage l = (SampleLineage)lineages.get(i);
            System.out.println("\tFrom: "+l.getFrom());
            System.out.println("\tTo: "+l.getTo());
        }
    }
    
    private static void printSourceContainers(Set containers) {
        Iterator iter = containers.iterator();
        while(iter.hasNext()) {
            Integer c = (Integer)iter.next();
            System.out.println("\t\t"+c);
        }
    }
}
