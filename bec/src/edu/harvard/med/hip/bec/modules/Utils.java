/*
 * Utils.java
 *
 * Created on June 27, 2003, 11:04 AM
 */

package edu.harvard.med.hip.bec.modules;


import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.programs.needle.*;
import edu.harvard.med.hip.bec.programs.blast.*;
import edu.harvard.med.hip.bec.export.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.engine.*;
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
public class Utils
{
    private RefSequence m_refsequence = null;
    public Utils(){}
    
    
    public RefSequence getRefSequence(){ return m_refsequence;}
    public  String getHTMLtransformedNeedleAlignment(int sequence_type, int sequence_id) throws Exception
    {
        //get exp sequnce from db
        BaseSequence expsequence = null;
       
        CloningStrategy clstr = null;
        BioLinker linker3 = null;
        BioLinker linker5 = null;
        
        int[] scores = null;
        if (sequence_type == BaseSequence.ANALIZED_SCORED_SEQUENCE ||
            sequence_type == BaseSequence.SCORED_SEQUENCE || sequence_type==BaseSequence.READ_SEQUENCE)
        {
            
            expsequence = new ScoredSequence(sequence_id);
            scores = ((ScoredSequence) expsequence).getScoresAsArray();
        }
        // get refsequnce 
        if ( sequence_type==BaseSequence.READ_SEQUENCE)
        {
            Object[] res = getRefsequenceAndCloningStrategy( sequence_id);
            m_refsequence =(RefSequence) res[0]; clstr= (CloningStrategy)res[1];
            if ( clstr != null)
            {
                  linker3 = BioLinker.getLinkerById( clstr.getLinker3Id() );
                  linker5 = BioLinker.getLinkerById( clstr.getLinker5Id() );
                  m_refsequence.setText( linker5.getSequence()+m_refsequence.getText() + linker3.getSequence());
            }
            else
            {
                    //construct reference sequence
                    m_refsequence.setText( m_refsequence.getText());
            }

        }
     
        
        //get for read primer direction
        boolean isCompliment = false;
        // run needle
        String needle_file_name  = runNeedle(m_refsequence,expsequence, isCompliment);
        // reparse needle output
        String needle_html_output = NeedleParser.parsetoHTMLString(needle_file_name, scores,
                                            linker5.getSequence().length(), linker5.getSequence().length() + m_refsequence.getText().length()
                                           );
        return needle_html_output;
    }
    
    
   
    
    //-------------------------------------
     //function runs needle and parse output
    private   String runNeedle(BaseSequence refsequence, BaseSequence expsequence, boolean isCompliment) throws BecUtilException
    {
        //run needle
        NeedleWrapper nw = new NeedleWrapper();
        nw.setQueryId(expsequence.getId());
        nw.setReferenceId(refsequence.getId());
        if ( !isCompliment )
            nw.setQuerySeq(expsequence.getText());
        else
            nw.setQuerySeq( SequenceManipulation.getCompliment(expsequence.getText()));
        
        nw.setRefSeq(refsequence.getText());
       // nw.setGapOpen(10);
      //  nw.setGapExtend(m_needle_gap_ext);
   
        nw.setOutputFileDir("/tmp/");
        
       return  nw.runNeedleNoParsing();
     
    }
   
    private  Object[] getRefsequenceAndCloningStrategy(int readsequenceid)throws BecDatabaseException
    {
        String sql = "select cloningstrategyid, refsequenceid from sequencingconstruct where constructid in (select constructid from isolatetracking where "
        +" sampleid in (select sampleid from result  where resultid in (select resultid from readinfo where readsequenceid = "+readsequenceid+")))";
           DatabaseTransaction t = DatabaseTransaction.getInstance();
           int refseq_id = -1;
           int clonstr_id = -1;
           RefSequence refsequence=null; CloningStrategy clstr = null;
           Object[] res = new Object[2];
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                refseq_id= rs.getInt("refsequenceid");
                refsequence=new RefSequence(refseq_id);
                clonstr_id=rs.getInt("cloningstrategyid");
                clstr = CloningStrategy.getById(clonstr_id);
                res[0]=refsequence;res[1]=clstr;
            }
            return res;
          
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
            
            Utils ut= new Utils();
            String g = ut.getHTMLtransformedNeedleAlignment(BaseSequence.READ_SEQUENCE,1623) ;
        System.out.print(ut.getRefSequence());
        }catch(Exception e){}
        System.exit(0);
     }
}
