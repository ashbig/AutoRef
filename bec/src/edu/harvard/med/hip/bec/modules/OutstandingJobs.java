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
import  edu.harvard.med.hip.bec.database.*;
import java.io.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import sun.jdbc.rowset.*;
/**
 *
 * @author  htaycher
 */
public class OutstandingJobs
{
     //store input & output blast files
    private static final String INPUT = "/tmp/";
  //  private static final String OUTPUT = "/blastoutput/";
    private static final String OUTPUT = "/output/blastoutput/";
    private int          m_species = RefSequence.SPECIES_NOT_SET;
    private ArrayList       m_plateids= null;
    private String      m_blastable_dbname = null;
    private int         m_blast_minimum_stretch = 100;
    private double      m_blast_identity = 95.0;
    
    
    public OutstandingJobs(){}
    public void setSpecies(int s){m_species = s;}
    public void setPlateIds(ArrayList ar){ m_plateids = ar;}
    public void setBlastablDB(String s){ m_blastable_dbname = s;}
    public void setPassParamBlastIdentity(double i){m_blast_identity = i;}
    public void setPassParamBlastMinimumStretch(int i){m_blast_minimum_stretch = i;}
    
    
    public  void findWriteORF(String reportfilename)throws Exception
    {
        //take all isolates with no match status
     
        findWriteORF( reportfilename,null);
    }
    public  void findWriteORF(String reportfilename,int[] subm_status)throws Exception
    {
        //take all isolates with no match status
        int [] status =  {IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH};
          
          if ( subm_status!=null)
          {
              status = subm_status;
          }
        String processed_isolatetr_ids = "";
        FileWriter in = null; int count_isolates = 0;
        while (true)
        {
            if (count_isolates == 0)
            {
                in = new FileWriter(reportfilename);
                in.write("Construct Id\t Bec RefSequence Id\tFlex Refsequence Id\t Isolate Id\t Clone ID\tRead Id\tRead seq id\t hit id \t identity\t stretch");
            }
            else
            {
                                in = new FileWriter(reportfilename, true);
            }
  // by species ArrayList isolate_engines = IsolateTrackingEngine.getIsolateTrackingEnginesByStatusandSpecies(status, m_species,processed_isolatetr_ids);
// plates
            ArrayList isolate_engines = IsolateTrackingEngine.getIsolateTrackingEnginesByStatusAndPlates(status, m_plateids,processed_isolatetr_ids);
           if ( isolate_engines == null || isolate_engines.size() == 0)break;
            IsolateTrackingEngine cur_engine = null;
            String sequence = null;
            ConstructInfo cur_construct = null;
            ArrayList messages = new ArrayList();
            //set blaster
            BlastParserNew parser = null;
            BlastWrapper blaster = new BlastWrapper();
            //set matrix to small sequences if not specified by user
            blaster.setProgramName("blastn");

            blaster.setFormat(8);blaster.setGI("T");blaster.setHitNumber(10);blaster.setFilter("F");

            for (int isolate_count = 0; isolate_count < isolate_engines.size(); isolate_count++)
            {
                cur_engine = (IsolateTrackingEngine)isolate_engines.get(isolate_count);
                cur_construct = new ConstructInfo(cur_engine.getId());
                processed_isolatetr_ids += cur_engine.getId()+",";
                int cloneId = getCloneId(cur_engine.getId());
                in.write("\n\n"+cur_construct.getConstructId() +"\t"+ cur_construct.getRefSeqId()+"\t"+cur_construct.getFlexRefSeqId() +"\t"+ cur_engine.getId() + "\t"+ cloneId + "\t");
                for (int read_count = 0; read_count < cur_engine.getEndReads().size(); read_count++)
                {
                    count_isolates++;
                    Read read = (Read)cur_engine.getEndReads().get(read_count);
                 //   if ( read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH ||
                   //     read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH )
                   // {
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
                        String match_info = confirmORFMatch(hits, cur_construct.getFlexRefSeqId());
                        if (match_info != null && match_info.length() != 0)
                            in.write( "\n\t"+read.getId() + "\t"+ read.getSequence().getId()+ "\n"+ match_info );
                      
                 //  }
                }
            }
            
             in.flush();
            in.close();
    }
    }
    
    
    
    
    
    
    
    
    
    
    //_________________________________________________________________________
    private static void setDBforBlaster(int species,BlastWrapper blaster)
    {
         // set species db
            switch (species)
            {
                case  RefSequence.SPECIES_HUMAN :
                {
                    blaster.setDB( BlastWrapper.HUMANDB);
                    break;
                }
                case  RefSequence.SPECIES_YEAST :
                {
                    blaster.setDB( BlastWrapper.YEASTDB);
                    break;
                }
                case RefSequence.SPECIES_MOUSE :
                {
                    blaster.setDB("");
                    break;
                }
                case RefSequence.SPECIES_PSEUDOMONAS :
                {
                    blaster.setDB(BlastWrapper.PSEUDOMONASDB);
                    break;
                }
                
            }
    }
    
    private String confirmORFMatch(ArrayList blast_output, int flexseqid)
    {
        String res = "";
         if (blast_output.size() < 1) return null;
        //take best hit
        BlastResult blresult = null;
        
        BlastAligment blalm = null;
        for (int count = 0 ; count <  blast_output.size(); count++)
        {
            blresult=(BlastResult)blast_output.get(count);
            blalm =(BlastAligment) blresult.getAligments().get(0);
            //if discrepancy matched 100% by identity on the whole length - confirm it
            boolean isConfirm = blalm.getIdentity() >= m_blast_identity
                && ( blalm.getQStop()-blalm.getQStart() + 1)>= m_blast_minimum_stretch;
            if ( isConfirm && count == 0)
            {
                res+= "\t\tbest hit \t";
                
               res+=blresult.getAcesession()+ "\t" +  blalm.getSequenceId()+ "\t"+ blalm.getIdentity() +"\t"+ ( blalm.getQStop()-blalm.getQStart());
            }
            if ( isConfirm && count > 0 && blresult.getAcesession().equalsIgnoreCase(""+ flexseqid))
            {
                res+= "\t\tref sequence hit \t"+blresult.getAcesession()+"\t" ;
                res +=  blalm.getSequenceId()+ "\t"+ blalm.getIdentity() +"\t"+ ( blalm.getQStop()-blalm.getQStart());
            }
            if ( flexseqid == blalm.getSequenceId()) break;
            
        }
        return res;     
    }
    
    public int getCloneId(int isolateid)throws BecDatabaseException
        {
            
            String sql = "select flexcloneid from flexinfo where isolatetrackingid  ="+isolateid    ;
            RowSet crs = null;
        
            try
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                crs = t.executeQuery(sql);

                if(crs.next())
                {
                   return crs.getInt("flexcloneid");
                  
                }
                return -1;
            } 
            catch (Exception e)
            {
                throw new BecDatabaseException("Error occured while extracting sequenceids: "+sql);
            } 
            finally
            {
                DatabaseTransaction.closeResultSet(crs);
            }
        
        }
    class ConstructInfo
    {
        private int i_constructid = -1;
        private int i_refseqid = -1;
        private int i_flexrefseqid = -1;
        
        public ConstructInfo(){}
        public ConstructInfo(int isolateid)throws BecDatabaseException
        {
            
            String sql = "select refsequenceid, flexsequenceid , i.constructid as constructid from sequencingconstruct c, flexinfo f, isolatetracking i "
+" where i.isolatetrackingid  ="+isolateid+" and f.isolatetrackingid  =i.isolatetrackingid   and i.constructid=c.constructid      ";
            RowSet crs = null;
        
            try
            {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                crs = t.executeQuery(sql);

                if(crs.next())
                {
                    i_refseqid = crs.getInt("refsequenceid");
                   i_flexrefseqid = crs.getInt("flexsequenceid");
                   i_constructid = crs.getInt("constructid");
                }
            } 
            catch (Exception e)
            {
                throw new BecDatabaseException("Error occured while extracting sequenceids: "+sql);
            } 
            finally
            {
                DatabaseTransaction.closeResultSet(crs);
            }
        
        }
        
        public int getConstructId(){ return i_constructid  ;}
        public int getRefSeqId(){ return i_refseqid  ;}
        public int getFlexRefSeqId(){ return i_flexrefseqid  ;}
        
        public void setConstructId(int v){   i_constructid  = v;}
        public void setRefSeqId(int v){   i_refseqid  = v;}
        public void setFlexRefSeqId(int v){   i_flexrefseqid  = v;}
        
    }
        
    
    
    public static void main(String [] args)
    {
        try
        {
               OutstandingJobs ot =new OutstandingJobs();
               ArrayList ar = new ArrayList();
               
     

               ar.add("227");
       ar.add("229");
       ar.add("233");
       ar.add("234");
       ar.add("235");
      

               ot.setPlateIds(ar);
               String filename="c:/tmp/no_match_pseudomonas.txt";
                ot.setPassParamBlastIdentity( 95.0);
                ot.setPassParamBlastMinimumStretch(70);
                ot.setSpecies( RefSequence.SPECIES_PSEUDOMONAS);
                int[] status = {IsolateTrackingEngine.PROCESS_STATUS_ER_ASSEMBLY_FINISHED};
                ot.findWriteORF(filename,status);
        }
        catch(Exception e){System.out.print(e.getMessage());}
  }
}
