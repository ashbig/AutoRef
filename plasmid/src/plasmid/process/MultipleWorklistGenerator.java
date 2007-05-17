/*
 * MultipleWorklistGenerator.java
 *
 * Created on March 27, 2007, 2:04 PM
 */

package plasmid.process;

import java.io.*;
import java.util.*;
import plasmid.util.ContainerLabelComparator;
import plasmid.coreobject.*;
import com.jscape.inet.sftp.*;

/**
 *
 * @author  DZuo
 */
public class MultipleWorklistGenerator extends WorklistGenerator {
    private List volumes;
    private String glycerollabel;
    private String glyceroltype;
    private int glycerolvolume;
    
    /** Creates a new instance of MultipleWorklistGenerator */
    public MultipleWorklistGenerator() {
        super();
    }
    
    public MultipleWorklistGenerator(List worklist) {
        super(worklist);
    }
    
    public MultipleWorklistGenerator(List worklist, boolean b) {
        super(worklist, b);
    }
    
    public void setVolumes(List l) {this.volumes = l;}
    public void setGlycerollabel(String s) {this.glycerollabel = s;}
    public void setGlyceroltype(String s) {this.glyceroltype = s;}
    public void setGlycerolvolume(int i) {this.glycerolvolume = i;}
    public String getGlycerollabel() {return glycerollabel;}
    public String getGlyceroltype() {return glyceroltype;}
    public int getGlycerolvolume() {return glycerolvolume;}
    public int getTotalvolume() {
        int total = 0;
        for(int i=0; i<volumes.size(); i++) {
            int v = ((Integer)volumes.get(i)).intValue();
            total += v;
        }
        return total;
    }
    
    public void printWorklistForRobot(String filename, int avolumn, int dvolumn, boolean isWash, Sftp ftp) throws Exception {
        List newWorklist = convertWorklist();
        
        if(newWorklist == null || filename == null)
            return;
        
        //OutputStreamWriter f = new FileWriter(filename);
        OutputStreamWriter f = new OutputStreamWriter(ftp.getOutputStream(filename, 0, false));
        
        int i=0;
        int size = newWorklist.size()/volumes.size();
        while(i<size) {
            SampleLineage w = (SampleLineage)newWorklist.get(i);
            Sample from = w.getSampleFrom();
            
            if(!isPrintEmpty && Sample.EMPTY.equals(from.getType())) {
                i++;
                continue;
            }
            
            f.write("A;;"+getGlycerollabel()+";"+getGlyceroltype()+";"+from.getPosition()+";;"+getGlycerolvolume()+"\n");
            f.write("D;;"+from.getContainerlabel()+";"+from.getContainerType()+";"+from.getPosition()+";;"+getGlycerolvolume()+";Glyc-Disp&Mix\n");
            f.write("A;;"+from.getContainerlabel()+";"+from.getContainerType()+";"+from.getPosition()+";;"+getTotalvolume()+"\n");

            for(int n=0; n<volumes.size(); n++) {
                SampleLineage sl = (SampleLineage)newWorklist.get(n*size+i);
                Sample to = sl.getSampleTo();
                f.write("D;;"+to.getContainerlabel()+";"+to.getContainerType()+";"+to.getPosition()+";;"+volumes.get(n)+"\n");
            }
            
            if(isWash) {
                f.write("W;\n");
            } else {
                if(i > size-NUMOFTIPS) {
                    f.write("W;\n");
                }
            }
            i++;
        }
        
        f.flush();
        f.close();
    }
    
    protected List convertWorklist() {
        List l = new ArrayList();
        
        for(int i=0; i<worklist.size(); i++) {
            l.addAll((List)worklist.get(i));
        }
        
        return l;
    }
}
