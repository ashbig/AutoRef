/*
 * SeqSampleInfoExporter.java
 *
 * Created on March 23, 2003, 9:19 PM
 */

package edu.harvard.med.hip.flex.export;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.query.*;

import java.util.*;
import java.io.*;
/**
 *
 * @author  hweng
 */
public class SeqSampleInfoExporter {
    
    public void doExport(TreeSet seqSampleInfo, String sample_type, PrintWriter out) {
        
        if(!sample_type.equalsIgnoreCase("init")) {
            
            out.print("Sequence ID\t");
            out.print("CDS\t");
            out.print("Sample ID\t");
            out.print("Construct Type\t");
            out.print("Container Label\t");
            out.print("Well\t");
            out.print("Experiment Result\t");
            out.println();
            
            Iterator it = seqSampleInfo.iterator();
            while(it.hasNext()){
                SequenceInfo seqinfo = (SequenceInfo)(it.next());
                out.print("" + seqinfo.getSequence().getSeqID() + "\t");
                out.print("" + seqinfo.getSequence().getCDS_len() + "\t");
                out.print("" + seqinfo.getSample().getId() + "\t");
                out.print("" + seqinfo.getConstruct().getType() + "\t");
                out.print("" + seqinfo.getSample().getLabel() + "\t");
                out.print("" + seqinfo.getSample().getPosition() + "\t");
                out.print("" + seqinfo.getSample().getResult() + "\t");
                out.println();
            }
        }
        else{
            
            out.print("Sequence ID\t");
            out.println();
            Iterator it = seqSampleInfo.iterator();
            while(it.hasNext()){
                SequenceInfo seqinfo = (SequenceInfo)(it.next());
                out.print("" + seqinfo.getSequence().getSeqID() + "\t");
                out.println();
            }
        }
        
        
    }
    
}
