/*
 * Utils.java
 *
 * Created on June 27, 2003, 11:04 AM
 */

package edu.harvard.med.hip.bec.ui_objects;

import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.utility.*;
//import edu.harvard.med.hip.bec.engine.*;
import edu.harvard.med.hip.bec.database.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.Constants;
import java.util.*;
import java.sql.Date;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

/**
 *
 * @author  htaycher
 */
public class UIUtils
{
    private   String m_needle_output_path = null;
    {
    if (ApplicationHostDeclaration.IS_BIGHEAD)
        m_needle_output_path = "d:\\tmp\\";
    else
        m_needle_output_path = "c:\\tmp\\";
    }
    private RefSequence m_refsequence = null;
    private int         m_refsequence_id = -1;
    public UIUtils(){}
    
    
    public RefSequence getRefSequence(){ return m_refsequence;}
    public void          setRefSequenceId(int i){m_refsequence_id = i;}
    
    public  String getHTMLtransformedNeedleAlignmentForTrimedRead(int[] scores, int sequence_id, int sequence_type) throws Exception
    {
       //try get scores if possible
         String scores_str = BaseSequence.getSequenceInfo(sequence_id, BaseSequence.SEQUENCE_INFO_SCORE);
         if (scores == null && scores_str != null)
         {
                ArrayList scores_parsed = Algorithms.splitString(scores_str," ");
                scores = new int[scores_parsed.size()];
                for (int count = 0; count < scores_parsed.size(); count++)
                {
                   scores[count] =  (Integer.parseInt( (String) scores_parsed.get(count)));
                }
                if ( sequence_type==BaseSequence.READ_SEQUENCE)
                {
                    Read read =  Read.getReadByReadSequenceId(sequence_id);
                    boolean isCompliment = false;
                    isCompliment = isCompliment( sequence_id, read);
                    //convert scores ifd compliment requered
                    if ( !isCompliment) 
                        scores =  convertScoresForComplement( scores, read.getTrimStart(), read.getTrimEnd() );
                    else
                    {
                        int[] scores_converted = new int[read.getTrimEnd() - read.getTrimStart() +1];
                        int j=0;
                        for (int count= read.getTrimStart() ; count < read.getTrimEnd() ; count++)
                        {
                            scores_converted[j++] = scores[count];
                        }
                        scores = scores_converted;
                    }
                        
                }
         }
       String needle_file_name = null;
        //if needle was already run retrive file
        if (m_refsequence_id != -1)
        {
            DiscrepancyFinder nv = new DiscrepancyFinder();
            needle_file_name = nv.getOutputDirectory()+"/needle"+sequence_id+"_"+m_refsequence_id+".out";
    //  System.out.print(needle_file_name);
        }
        else
        {
            return null;
        }
       
        // reparse needle output
        return NeedleParser.parsetoHTMLString(needle_file_name, scores,      0, 0  );
      
    }
    
    
    public  String getHTMLtransformedNeedleAlignment(int sequence_type, int sequence_id) throws Exception
    {
        //get exp sequnce from db
        BaseSequence expsequence = null;
       
        CloningStrategy clstr = null;
        BioLinker linker3 = null;
        BioLinker linker5 = null;
        
        int[] scores = null;
        String needle_file_name = null;
        Read read = null; 
        
        if ( sequence_type==BaseSequence.READ_SEQUENCE)
        {
            read =  Read.getReadByReadSequenceId(sequence_id);
            expsequence = read.getSequence();
            scores = ((ScoredSequence) expsequence).getScoresAsArray();
        }
        else
        {
            if (sequence_type == BaseSequence.ANALIZED_SCORED_SEQUENCE ||
                sequence_type == BaseSequence.SCORED_SEQUENCE || sequence_type==BaseSequence.READ_SEQUENCE)
            {

                expsequence = new ScoredSequence(sequence_id);
                scores = ((ScoredSequence) expsequence).getScoresAsArray();
            }
        }
        // get refsequnce 
         //get for read primer direction
        boolean isCompliment = false;
        if ( sequence_type==BaseSequence.READ_SEQUENCE)
            {
                CloneDescription clone_description = getRefsequenceAndCloningStrategy( sequence_id);
                clstr = CloningStrategy.getById(clone_description.getCloningStrategyId());
                m_refsequence =new RefSequence(clone_description.getBecRefSequenceId());
                BaseSequence seq = Construct.getRefSequenceForAnalysis(clstr.getStartCodon(),
                           clstr.getFusionStopCodon(),clstr.getClosedStopCodon(),
                            m_refsequence.getCodingSequence(), clone_description.getConstructFormat());
               
                if ( clstr != null)
                {
                      linker3 = BioLinker.getLinkerById( clstr.getLinker3Id() );
                      linker5 = BioLinker.getLinkerById( clstr.getLinker5Id() );
                      m_refsequence.setText( linker5.getSequence().toLowerCase()+seq.getText().toUpperCase() + linker3.getSequence().toLowerCase());
                }
                else
                {
                        //construct reference sequence
                        m_refsequence.setText( seq.getText());
                }
                
                /*
                int containerid = BaseSequence.getContainerId(sequence_id, BaseSequence.READ_SEQUENCE);

                Oligo[] oligos = Container.findEndReadsOligos(containerid);
    
                if (oligos[0] != null && (read.getType() ==  Read.TYPE_ENDREAD_FORWARD || 
                            read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL || 
                            read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH || 
                            read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT))
                {
                       isCompliment = (oligos[0].getOrientation() == Oligo.ORIENTATION_SENSE);
                }
                if (oligos[1] != null && (read.getType() ==  Read.TYPE_ENDREAD_REVERSE || 
                            read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL || 
                            read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH || 
                            read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT))
                {
                      isCompliment =( oligos[1].getOrientation() == Oligo.ORIENTATION_SENSE) ; 
                }
                */
                isCompliment = isCompliment( sequence_id, read);
                //convert scores ifd compliment requered
                if ( !isCompliment) 
                    scores =  convertScoresForComplement( scores,  0,  scores.length);
                
            }

           
            // run needle
            needle_file_name  = runNeedle(m_refsequence,expsequence, isCompliment);
        
        // reparse needle output
        String needle_html_output = NeedleParser.parsetoHTMLString(needle_file_name, scores,
                                            linker5.getSequence().length(), linker5.getSequence().length() + m_refsequence.getText().length()
                                           );
        return needle_html_output;
    }
    
    //-------------------------------------
    private boolean   isCompliment(int sequence_id, Read read)throws Exception
    {
        boolean isCompliment = false;
        int containerid = BaseSequence.getContainerId(sequence_id, BaseSequence.READ_SEQUENCE);
        
        Oligo[] oligos = Container.findEndReadsOligos(containerid);

        if (oligos[0] != null && (read.getType() ==  Read.TYPE_ENDREAD_FORWARD || 
                    read.getType() == Read.TYPE_ENDREAD_FORWARD_FAIL || 
                    read.getType() == Read.TYPE_ENDREAD_FORWARD_NO_MATCH || 
                    read.getType() == Read.TYPE_ENDREAD_FORWARD_SHORT))
        {
               isCompliment = (oligos[0].getOrientation() == Oligo.ORIENTATION_SENSE);
        }
        if (oligos[1] != null && (read.getType() ==  Read.TYPE_ENDREAD_REVERSE || 
                    read.getType() == Read.TYPE_ENDREAD_REVERSE_FAIL || 
                    read.getType() == Read.TYPE_ENDREAD_REVERSE_NO_MATCH || 
                    read.getType() == Read.TYPE_ENDREAD_REVERSE_SHORT))
        {
              isCompliment =( oligos[1].getOrientation() == Oligo.ORIENTATION_SENSE) ; 
        }
        return isCompliment;
    }
    
    private int[]      convertScoresForComplement(int[] scores, int start, int end)
    {
         int[] scores_converted = new int[end-start +1];
        int j=0;
        for (int count= end -1 ; count >= start; count--)
        {
            scores_converted[j++] = scores[count];
        }
        return scores_converted;
    }
    
    
     //function runs needle and parse output
    private   String runNeedle(BaseSequence refsequence, BaseSequence expsequence, boolean isCompliment) throws BecUtilException
    {
        //run needle
        NeedleWrapper nw = new NeedleWrapper();
        nw.setQueryId(expsequence.getId());
        nw.setReferenceId(refsequence.getId());
        if ( isCompliment )
            nw.setQuerySeq(expsequence.getText());
        else
            nw.setQuerySeq( SequenceManipulation.getCompliment(expsequence.getText()));
        
        nw.setRefSeq(refsequence.getText());
       // nw.setGapOpen(10);
      //  nw.setGapExtend(m_needle_gap_ext);
   
        nw.setOutputFileDir(m_needle_output_path);
        
       return  nw.runNeedleNoParsing();
     
    }
   
    private  CloneDescription getRefsequenceAndCloningStrategy(int readsequenceid)throws BecDatabaseException
    {
        String sql = "select constructid, format,cloningstrategyid, refsequenceid from sequencingconstruct where constructid in (select constructid from isolatetracking where "
        +" sampleid in (select sampleid from result  where resultid in (select resultid from readinfo where readsequenceid = "+readsequenceid+")))";
           DatabaseTransaction t = DatabaseTransaction.getInstance();
           int refseq_id = -1;
           int clonstr_id = -1;
           CloneDescription clone_description = null;
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                clone_description = new CloneDescription();
                clone_description.setBecRefSequenceId (rs.getInt("refsequenceid"));
              //   clone_description.setIsolateTrackingId ();
              
                 clone_description.setConstructId(rs.getInt("constructid"));
                 clone_description.setConstructFormat(rs.getInt("format"));
                 clone_description.setCloningStrategyId(rs.getInt("cloningstrategyid")); 
               
                
            }
            return clone_description;
          
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while : "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
     public static void main(String args[])
    {
        try
        {
            
            UIUtils ut= new UIUtils();
            ut.setRefSequenceId(14584);
            String g = ut.getHTMLtransformedNeedleAlignmentForTrimedRead(null,14800,0) ;
        System.out.print(g);
        }catch(Exception e){}
        System.exit(0);
     }
}
