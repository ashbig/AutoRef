/*
 * WorklistGenerator.java
 *
 * Created on May 24, 2005, 3:00 PM
 */

package plasmid.process;

import java.io.*;
import java.util.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class WorklistGenerator {
    public static int NUMOFTIPS = 8;
    public static final String DELIM = "\t";
    private List worklist;
    
    /** Creates a new instance of WorklistGenerator */
    public WorklistGenerator() {
    }
    
    public WorklistGenerator(List worklist) {
        this.worklist = worklist;
    }
    
    public List getWorklist() {return worklist;}
    public void setWorklist(List l) {this.worklist = l;}
    
    /**
     * public void generateWorklist(List containers, List labels) throws Exception {
     * if(containers == null || labels == null)
     * throw new Exception("Source container list or destination container label list is null.");
     *
     * if(containers.size() != labels.size())
     * throw new Exception("Source and destination have different sizes.");
     *
     * worklist = new ArrayList();
     * for(int i=0; i<containers.size(); i++) {
     * Container c = (Container)containers.get(i);
     * String label = (String)labels.get(i);
     *
     * for(int k=0; k<c.getSize(); k++) {
     * Sample s = c.getSample(k);
     * if(s != null && !Sample.EMPTY.equals(s.getType())) {
     * String position = (new Integer(s.getPosition())).toString();
     * Worklist w = new Worklist(c.getLabel(), position, label,position);
     * worklist.add(w);
     * }
     * }
     * }
     * }
     *
     * public OutputStreamWriter generateOutputFile(String sourceLabel, String destLabel, List l, String filename) {
     * if(l == null)
     * return null;
     *
     * OutputStreamWriter f = new FileWriter(filename);
     * for(int i=0; i<l.size(); i++) {
     * SampleLineage s = (SampleLineage)l.get(i);
     * f.write(sourceLabel+"\t"+s.getSampleFrom().getPosition()+"\t"+destLabel+"\t"+s.getSampleTo().getPosition()+"\n");
     * }
     * f.close();
     * return f;
     * }
     */
    
    public void printFullWorklist(String filename) throws Exception {
        if(worklist == null || filename == null)
            return;
        
        OutputStreamWriter f = new FileWriter(filename);
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            Sample from = w.getSampleFrom();
            Sample to = w.getSampleTo();
            
            f.write("\t"+from.getContainerlabel()+"\t"+from.getPosition()+"\t"+to.getContainerlabel()+"\t"+to.getPosition()+"\t"+to.getType()+"\t"+from.getSampleid()+"\t"+to.getCloneid()+"\t"+from.getContainerid()+"\t"+to.getContainerid()+"\n");
            
        }
        f.close();
    }
    
    public void printWorklist(String filename) throws Exception {
        if(worklist == null || filename == null)
            return;
        
        OutputStreamWriter f = new FileWriter(filename);
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            Sample from = w.getSampleFrom();
            Sample to = w.getSampleTo();
            
            if(!Sample.EMPTY.equals(to.getType()))
                f.write("\t"+from.getContainerlabel()+"\t"+from.getPosition()+"\t"+to.getContainerlabel()+"\t"+to.getPosition()+"\n");
            
        }
        f.close();
    }
    
    public void printWorklistForRobot(String filename, int avolumn, int dvolumn, boolean isWash) throws Exception {
        if(worklist == null || filename == null)
            return;
        
        OutputStreamWriter f = new FileWriter(filename);
        
        int i=0;
        int size = worklist.size();
        while(i<size) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            Sample from = w.getSampleFrom();
            f.write("A;;"+from.getContainerlabel()+";"+from.getContainerType()+";"+from.getPosition()+";;"+avolumn+"\n");
            
            int numOfDispense = avolumn/dvolumn;
            for(int n=0; n<numOfDispense; n++) {
                SampleLineage sl = (SampleLineage)worklist.get(i);
                Sample to = sl.getSampleTo();
                f.write("D;;"+to.getContainerlabel()+";"+to.getContainerType()+";"+to.getPosition()+";;"+dvolumn+"\n");
                i++;
            }
            
            if(isWash) {
                f.write("W;\n");
            } else {
                if(i > size-NUMOFTIPS) {
                    f.write("W;\n");
                }
            }
        }
        
        f.flush();
        f.close();
    }
    
    public void readWorklist(String filename) throws Exception {
        worklist = new ArrayList();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        while((line = in.readLine()) != null) {
            if(line.trim().length()>0) {
                StringTokenizer st = new StringTokenizer(line, DELIM);
                String sourcePlate = st.nextToken();
                String sourcePosition = st.nextToken();
                String destPlate = st.nextToken();
                String destPosition = st.nextToken();
                String destSampleType = null;
                if(st.hasMoreTokens()) {
                    destSampleType = st.nextToken();
                }
                String sampleid = null;
                if(st.hasMoreTokens()) {
                    sampleid = st.nextToken();
                }
                String cloneid = null;
                if(st.hasMoreTokens()) {
                    cloneid = st.nextToken();
                }
                String srcContainerid = null;
                if(st.hasMoreTokens()) {
                    srcContainerid = st.nextToken();
                }
                String destContainerid = null;
                if(st.hasMoreTokens()) {
                    destContainerid = st.nextToken();
                }
                
                Sample from = new Sample();
                from.setContainerlabel(sourcePlate);
                from.setPosition(Integer.parseInt(sourcePosition));
                if(sampleid != null)
                    from.setSampleid(Integer.parseInt(sampleid));
                if(srcContainerid != null)
                    from.setContainerid(Integer.parseInt(srcContainerid));
                
                Sample to = new Sample();
                to.setContainerlabel(destPlate);
                to.setPositions(Integer.parseInt(destPosition));
                to.setType(destSampleType);
                if(cloneid != null)
                    to.setCloneid(Integer.parseInt(cloneid));
                if(destContainerid != null)
                    to.setContainerid(Integer.parseInt(destContainerid));
                
                worklist.add(new SampleLineage(from, to));
            }
        }
    }
    
    public Set getSourceContainerLabels() {
        if(worklist == null)
            return null;
        
        Set labels = new HashSet();
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            labels.add(w.getSampleFrom().getContainerlabel());
        }
        
        return labels;
    }
    
    public Set getDestContainerLabels() {
        if(worklist == null)
            return null;
        
        Set labels = new HashSet();
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            labels.add(w.getSampleTo().getContainerlabel());
        }
        
        return labels;
    }
    
    public Set getSrcContainerids() {
        if(worklist == null)
            return null;
        
        Set ids = new HashSet();
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            ids.add(new Integer(w.getSampleFrom().getContainerid()));
        }
        
        return ids;
    }
    
    public Set getSrcContainers() {
        if(worklist == null)
            return null;
        
        Set containers = new HashSet();
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            Sample s = w.getSampleFrom();
            int id = s.getContainerid();
            String label = s.getContainerlabel();
            String type = s.getContainerType();
            Container c = new Container(id, type, label, null, null, -1, null);
            containers.add(c);
        }
        
        return containers;
    }
    
    public Set getDestContainers() {
        if(worklist == null)
            return null;
        
        Set containers = new HashSet();
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            Sample s = w.getSampleTo();
            int id = s.getContainerid();
            String label = s.getContainerlabel();
            String type = s.getContainerType();
            Container c = new Container(id, type, label, null, null, -1, null);
            containers.add(c);
        }
        
        return containers;
    }
    
    public Set getDestContainerids() {
        if(worklist == null)
            return null;
        
        Set ids = new HashSet();
        for(int i=0; i<worklist.size(); i++) {
            SampleLineage w = (SampleLineage)worklist.get(i);
            ids.add(new Integer(w.getSampleTo().getContainerid()));
        }
        
        return ids;
    }
}
