/*
 * ReportItem.java
 *
 * Created on September 28, 2004, 4:31 PM
 */

package edu.harvard.med.hip.bec.util_objects;
import java.sql.*;
import java.io.*;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.file.*;
/**
 *
 * @author  HTaycher
 */
public class ReportItem {
    
    private int             i_cloneid = -1; 
        private int             i_refseq_cds_length = -1;        
        private int             i_analisys_status = -1;
        private int             i_clone_sequenceid = 0;   
        private int             i_discr_id = -1;
        private int             i_discr_change_type = -1;   
        private int             i_discr_position = -1;
        private int             i_discr_quality = -1;     
        private int             i_lqr_id = -1;
        private int             i_lqr_cds_start = -1;
        private int             i_refseq_id = -1; 
        private int             i_lqr_cds_stop = -1; 
        private int             i_lqr_sequence_start = -1;
        private int             i_lqr_sequence_stop = -1; 
        private int             i_gap_id = -1; 
        private int             i_contigid = -1; 
        private int             i_gap_cds_start = -1;
        private int             i_gap_cds_stop = -1;
        
        public ReportItem(){}
        public void             setCloneId   (int v){    i_cloneid = v;   }   
        public void             setRefSequenceCdsLength   (int v){    i_refseq_cds_length = v;   }        
        public void             setAnalysisStatus   (int v){    i_analisys_status = v;   }
        public void             setCloneSequenceId   (int v){    i_clone_sequenceid = v;   }   
        public void             setDiscrId   (int v){    i_discr_id = v;   }
        public void             setDiscrChangeType   (int v){    i_discr_change_type = v;   }   
        public void             setDiscrPosition   (int v){    i_discr_position = v;   }
        public void             setDiscrQuality   (int v){    i_discr_quality = v;   }     
        public void             setLqrId   (int v){    i_lqr_id = v;   }
        public void             setLqrCdsStart   (int v){    i_lqr_cds_start = v;   }
        public void             setLqrRefsequenceId  (int v){    i_refseq_id = v;   } 
        public void             setLqrCdsStop   (int v){    i_lqr_cds_stop = v;   } 
        public void             setLqrSequenceStart   (int v){    i_lqr_sequence_start = v;   }
        public void             setLqrSequenceStop   (int v){    i_lqr_sequence_stop = v;   } 
        public void             setGapId   (int v){    i_gap_id = v;   } 
        public void             setContigId   (int v){    i_contigid = v;   } 
        public void             setGapCdsStart   (int v){    i_gap_cds_start = v;   }
        public void             setGapCdsStop   (int v){    i_gap_cds_stop = v;   }
      /* format 
     * cloneid   cds_length   Analyzed    AssembledSeqId    DiskrId     DiskrType     DiscrPos   
     *  DiskrQuality   LQR_ID   LQR_Cds_start   LQR_Cds_Stop    
     *LQR_Sequence_start    LQR_Sequence_Stop   GapId   ContigId    Gap/Contig Cds Range
     **/  
        public String toString()
        {
            StringBuffer res = new StringBuffer();
            res.append( i_cloneid  + "\t"+    i_refseq_cds_length     );    
            if ( i_analisys_status  != -1)
               res.append( BaseSequence.getSequenceAnalyzedStatusAsString(i_analisys_status)+"\t");
            else
                 res.append( format( i_analisys_status ) );
            res.append( i_clone_sequenceid   + "\t");
         
            if ( i_discr_id != -1 ) 
            {
                res.append(   i_discr_id + "\t" );
                res.append(  i_discr_position   + "\t");  
                res.append(   Mutation.getQualityAsString(i_discr_quality)  + "\t");
            }
            else
            {
                res.append(   "N/A\tN/A\tN/A\t");   
            }
             if ( i_lqr_id != -1)
             {
                res.append(     i_lqr_id   + "\t");
                res.append( i_lqr_cds_start   + "\t"+    i_lqr_cds_stop   + "\t");
                res.append( i_lqr_sequence_start   + "\t"+  i_lqr_sequence_stop   + "\t");
             }
             else
             {
                  res.append(   "N/A\tN/A\tN/A\tN/A\tN/A\t");
             }
            res.append( format(i_gap_id)   + "\t"+          format(i_contigid )  + "\t");
            res.append( format(i_gap_cds_start)   + "\t"+          format(i_gap_cds_stop )  + "\t");
            return res.toString();
        }
        
        private String          format(int data)
        {
            if (data == -1 ) return "N/A";
            return String.valueOf( data );
        }
}
