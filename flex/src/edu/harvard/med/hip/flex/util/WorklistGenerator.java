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
    public static final int NUMOFTIPS = 8;
    
    /** Creates a new instance of WorklistGenerator */
    public WorklistGenerator() {
    }

    public static File createWorklist(List mappingList, String sourcePlateType, String destPlateType, int aspirateVolumn, int dispenseVolumn, String fileName, boolean isWash) throws IOException {
        File file = new File(fileName);
        FileWriter fr = new FileWriter(file);
        int i=0;
        int size = mappingList.size();
        while(i<size) {
            RearrayInputSample sample = (RearrayInputSample)mappingList.get(i);
            fr.write("A;;"+sample.getSourcePlate()+";"+sourcePlateType+";"+sample.getSourceWell()+";;"+aspirateVolumn+"\n");
            
            int numOfDispense = aspirateVolumn/dispenseVolumn;
            for(int n=0; n<numOfDispense; n++) {
                sample = (RearrayInputSample)mappingList.get(i);
                fr.write("D;;"+sample.getDestPlate()+";"+destPlateType+";"+sample.getDestWell()+";;"+dispenseVolumn+"\n");
                i++;
            }
            
            if(isWash) {
                fr.write("W;\n");
            } else {
                if(i > size-NUMOFTIPS) {
                    fr.write("W;\n");
                }
            }
        }
        fr.flush();
        fr.close();
        
        return file;
    }   
}
