/*
 * OutstandingJobs.java
 *
 * Created on May 22, 2003, 2:17 PM
 */

package edu.harvard.med.hip.bec.modules;

import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.bioutil.*;
import  edu.harvard.med.hip.bec.coreobjects.sequence.*;


import java.util.*;
/**
 *
 * @author  htaycher
 */
public class OutstandingJobs
{
     //store input & output blast files
    private static final String INPUT = "/tmp/";
    private static final String OUTPUT = "/blastoutput/";
    private int          m_species = RefSequence.SPECIES_NOT_SET;
    private String      m_blastable_dbname = null;
    private int         m_blast_minimum_stretch = 100;
    private double      m_blast_identity = 95.0;
    
    
    public OutstandingJobs(){}
    public void setSpecies(int s){m_species = s;}
    public void setBlastablDB(String s){ m_blastable_dbname = s;}
    public void setPassParamBlastIdentity(double i){m_blast_identity = i;}
    public void setPassParamBlastMinimumStretch(int i){m_blast_minimum_stretch = i;}
    
    public  void findWriteORF()throws Exception
    {
        //take all isolates with no match status
        int[] status = {IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH};
        ArrayList isolate_engines = IsolateTrackingEngine.getIsolateTrackingEnginesByStatusandSpecies(status, m_species);
      
        IsolateTrackingEngine cur_engine = null;String sequence = null;
        ArrayList messages = new ArrayList();
        //set blaster
        BlastParserNew parser = null;
        BlastWrapper blaster = new BlastWrapper();
        //set matrix to small sequences if not specified by user
        blaster.setProgramName("blastn");
        
        blaster.setFormat(8);blaster.setGI("T");blaster.setHitNumber(1);blaster.setFilter("F");
              
        for (int isolate_count = 0; isolate_count < isolate_engines.size(); isolate_count++)
        {
            cur_engine = (IsolateTrackingEngine)isolate_engines.get(isolate_count);
            for (int read_count = 0; read_count < cur_engine.getEndReads().size(); read_count++)
            {
                Read read = (Read)cur_engine.getEndReads().get(read_count);
                if ( read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH ||
                    read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH )
                {
                      // blast each against blastable database
                     //write query file
                    sequence = read.getTrimmedSequence();
                    String queryFile = SequenceManipulation.makeQueryFileInFASTAFormat(INPUT,sequence, "bn", ""+ read.getSequence().getId());
                    //set matrix
                    if (sequence.length() < 100) blaster.setMatrix( "PAM30");
                    else if (sequence.length()>100 && sequence.length()<150) blaster.setMatrix("PAM70");
                    else blaster.setMatrix("BLOSUM62");
                        
                    blaster.setInputFN(queryFile+".in");
                    blaster.setOutputFN(queryFile+".out");
                    if ( m_blastable_dbname == null)
                        setDBforBlaster( m_species, blaster);
                    else
                        blaster.setDB( m_blastable_dbname );
                    blaster.run();
        
                    ArrayList hits = parser.parse(queryFile+".out", 8);
                    String match_info = confirmORFMatch(hits);
                    if (match_info != null)
                        messages.add( cur_engine.getId() + " "+ read.getId() + " "+ read.getSequence().getId()+ " "+ match_info );
                }
            }
        }
        // output best match into file info
        for (int count = 0; count < messages.size(); count++)
        {
            System.out.println( (String) messages.get(count));
        }
    }
    
    private static void setDBforBlaster(int species,BlastWrapper blaster)
    {
         // set species db
            switch (species)
            {
                case  RefSequence.SPECIES_HUMAN :
                {
                    blaster.setDB( FastaFileGenerator.HUMANDB);
                    break;
                }
                case  RefSequence.SPECIES_YEAST :
                {
                    blaster.setDB( FastaFileGenerator.YEASTDB);
                    break;
                }
                case RefSequence.SPECIES_MOUSE :
                {
                    blaster.setDB("");
                    break;
                }
                case RefSequence.SPECIES_PSEUDOMONAS :
                {
                    blaster.setDB(FastaFileGenerator.PSEUDOMONASDB);
                    break;
                }
                
            }
    }
    
    private String confirmORFMatch(ArrayList blast_output)
    {
        String res = null;
         if (blast_output.size() < 1) return null;
        //take best hit
        BlastResult blresult = (BlastResult)blast_output.get(0);
        if (blresult.getAligments().size() < 1) return null;
        BlastAligment blalm = (BlastAligment) blresult.getAligments().get(0);
        //if discrepancy matched 100% by identity on the whole length - confirm it
        boolean isConfirm = blalm.getIdentity() >= m_blast_identity
            && ( blalm.getQStop()-blalm.getQStart() + 1)>= m_blast_minimum_stretch;
        if (isConfirm)
            res= "hit "+blresult.getAcesession() + " " + blalm.getSequenceId() + " "+ blalm.getIdentity() +" "+ blalm.getQStop() +" "+blalm.getQStart();
        return res;     
    }
    
    
    
    
    
    public static void main(String [] args)
    {
        try
        {
               OutstandingJobs ot =new OutstandingJobs();
                ot.setSpecies(RefSequence.SPECIES_YEAST);
               
                ot.setPassParamBlastIdentity( 95.0);
                ot.setPassParamBlastMinimumStretch(10);
                ot.findWriteORF();
        }
        catch(Exception e){}
  }
}
