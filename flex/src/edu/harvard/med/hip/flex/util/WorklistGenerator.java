/*
 * WorklistGenerator.java
 *
 * Created on April 9, 2004, 9:49 AM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.process.RearrayInputSample;

/**
 *
 * @author  DZuo
 */
public class WorklistGenerator {
    
    /** Creates a new instance of WorklistGenerator */
    public WorklistGenerator() {
    }

    public static File createWorklist(List mappingList, String sourcePlateType, String destPlateType, int aspirateVolumn, int dispenseVolumn, String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fr = new FileWriter(file);
        int i=0;
        while(i<mappingList.size()) {
            RearrayInputSample sample = (RearrayInputSample)mappingList.get(i);
            fr.write("A;;"+sample.getSourcePlate()+";"+sourcePlateType+";"+sample.getSourceWell()+";;"+aspirateVolumn+"\n");
            
            int numOfDispense = aspirateVolumn/dispenseVolumn;
            for(int n=0; n<numOfDispense; n++) {
                sample = (RearrayInputSample)mappingList.get(i);
                fr.write("D;;"+sample.getDestPlate()+";"+destPlateType+";"+sample.getDestWell()+";;"+dispenseVolumn+"\n");
                i++;
            }
            fr.write("W;\n");
        }
        fr.flush();
        fr.close();
        
        return file;
    }  
    
    public static File createWorklistForReservior(List mappingList, String sourcePlateType, String destPlateType, int aspirateVolumn, int dispenseVolumn, String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fr = new FileWriter(file);
        int i=0;
        while(i<mappingList.size()) {
            RearrayInputSample sample = (RearrayInputSample)mappingList.get(i);
            fr.write("A;;"+sample.getSourcePlate()+";"+sourcePlateType+";"+sample.getSourceWell()+";;"+aspirateVolumn+"\n");
            
            int numOfDispense = aspirateVolumn/dispenseVolumn;
            for(int n=0; n<numOfDispense; n++) {
                sample = (RearrayInputSample)mappingList.get(i);
                fr.write("D;;"+sample.getDestPlate()+";"+destPlateType+";"+sample.getDestWell()+";;"+dispenseVolumn+"\n");
                i++;
            }
            fr.write("W;\n");
        }
        fr.flush();
        fr.close();
        
        return file;
    }  
}
