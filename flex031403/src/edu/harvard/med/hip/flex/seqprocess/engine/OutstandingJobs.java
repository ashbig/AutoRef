/*
 * OutstandingJobs.java
 *
 * Created on October 21, 2002, 5:40 PM
 */

package edu.harvard.med.hip.flex.seqprocess.engine;

import java.util.*;
import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.blast.*;
import edu.harvard.med.hip.flex.util.*;
import java.sql.*;
import java.io.*;

/**
 *
 * @author  htaycher
 *it will start overnight jobs
 *like:
 *1. clear up mutations
 *2. run blast on bad full sequences
 */
public class OutstandingJobs
{
    private static ArrayList m_bad_full_sequences = null;
    private static ArrayList m_full_sequences_for_mutation_polimorphysm = null;
    /** Creates a new instance of OutstandingJobs */
   
    
    public static void runBadSequences(String useremail) throws FlexDatabaseException
    {
       ArrayList sequences= FullSequence.getAllSequencesWithStatus(FullSequence.STATUS_NOMATCH);
       ArrayList files = new ArrayList();
       File blastfile = null;
       DatabaseTransaction t = DatabaseTransaction.getInstance();
       SequenceAnalyzer analyzer = null;
       try
       {
           
           Connection conn = t.requestConnection();
           for (int count = 0; count < sequences.size();count++)
           {
               FullSequence seq = (FullSequence) sequences.get(count);
               analyzer  = new SequenceAnalyzer(seq);
               int res =  analyzer.run_blastn(  FlexSeqAnalyzer.HUMANDB ,  5,  blastfile) ;
               if (res >=1) files.add(blastfile);

               res = analyzer.run_blastn(   FlexSeqAnalyzer.MGCDB ,  5,  blastfile) ;
               if (res >=1) files.add(blastfile);

               res =  analyzer.run_blastn(   FlexSeqAnalyzer.YEASTDB ,  5,  blastfile) ;
               if (res >=1) files.add(blastfile);

                String to = "etaycher@hms.harvard.edu";
                String from = "dzuo@hms.harvard.edu";
                String cc = "flexgene_manager1@hms.harvard.edu1";
                String subject = "Blast files ";
                String msgText = "The attached files are blast output for not matched sequences.";

               
                if (count % 5 == 0 || ( count == sequences.size()- 1) )
                    Mailer.sendMessage(useremail, from, cc, subject, msgText, files);
                 seq.setStatus(FullSequence.STATUS_FINAL);
                seq.updateStatus(conn);
           }
       }
       catch(Exception e)
       {
           System.out.println(e.getMessage());
           throw new FlexDatabaseException("ll");
       }
    }
    
    public static void runPolimorphism() throws FlexDatabaseException
    {
        ArrayList sequences= FullSequence.getAllSequencesWithStatus(FullSequence.STATUS_ANALIZED);
    }
    
    
   
}
