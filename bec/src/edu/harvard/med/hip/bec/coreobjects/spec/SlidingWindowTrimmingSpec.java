/*
 * SlidingWindowTrimmingSpec.java
 *
 * Created on June 30, 2004, 3:17 PM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;

import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import java.util.*;
/**
 *
 * @author  HTaycher
 */
public class SlidingWindowTrimmingSpec extends Spec
{
        // trim type for scored sequence
    public static final int TRIM_TYPE_NONE = 0; //no
  //  public static final int TRIM_TYPE_PHRED = 1; //no
    public static final int TRIM_TYPE_MOVING_WINDOW = 2; //no
    public static final int TRIM_TYPE_PHRED_NODISC = 3; //no
    public static final int TRIM_TYPE_MOVING_WINDOW_NODISC = 4; //no
    
    
        //parameters names and default velues for sequence quality definition
     public static final int QUALITY_WINDOW_SIZE = 25; 
     public static final int QUALITY_NUMBER_LOW_QUALITY_BASES = 3; 
     public static final int QUALITY_NUMBER_LOW_QUALITY_BASES_CONQ = 2; 
     public static final int QUALITY_CUTT_OFF = 20; 
     public static final int AMBIQUATY_WINDOW_SIZE = 25; 
     public static final int AMBIQUATY_NUMBER_BASES = 3; 
     public static final int AMBIQUATY_NUMBER_LOW_QUALITY_BASES_CONQ = 2;
     public  static final int MIN_DISTANCE_BETWEEN_CONTIGS = 50;
     public static final int TRIM_TYPE = TRIM_TYPE_MOVING_WINDOW;
     public  static final int        MIN_CONTIG_LENGTH = 50;
    
     public static final String PNAME_Q_WINDOW_SIZE = "SW_Q_WINDOW_SIZE"; 
    public static final String PNAME_Q_NUMBER_LOW_QUALITY_BASES = "SW_Q_NUMBER_LOW_QUALITY_BASES"; 
    public static final String PNAME_Q_NUMBER_LOW_QUALITY_BASES_CONQ = "SW_Q_N_LOW_QUALITY_BASES_CONQ"; 
    public static final String PNAME_Q_CUTT_OFF = "SW_Q_CUTT_OFF"; 
    public static final String PNAME_A_WINDOW_SIZE = "SW_A_WINDOW_SIZE"; 
    public static final String PNAME_A_NUMBER_BASES = "SW_A_NUMBER_BASES"; 
    public static final String PNAME_A_NUMBER_LOW_QUALITY_BASES_CONQ = "SW_A_N_LOW_QUALITY_BASES_CONQ";
    public  static final String PNAME_MIN_DISTANCE_BETWEEN_CONTIGS = "SW_MIN_DISTANCE_BTW_CONTIGS";
    public static final String PNAME_TRIM_TYPE = "SW_TRIM_TYPE";
    public static final String PNAME_MIN_CONTIG_LENGTH = "SW_MIN_CONTIG_LENGTH";
   
   
    
    
    private int m_type = -1;
    private int m_quality_window_size = -1 ; 
    private int m_max_number_low_quality_bases_in_window   = -1 ; 
    private int m_max_number_conq_low_quality_bases_in_window   =-1 ; 
    private int m_quality_cutoff   = -1 ; 
    private int m_ambiquaty_window_size   = -1 ; 
    private int m_ambiquaty_low_quality_bases_in_window   = -1 ; 
    private int m_ambiquaty_conq_low_quality_bases_in_window   = -1 ; 
    private int m_min_distance_between_two_stretches_to_collapse = -1;
    private int m_min_stretch_length_to_collapse = -1;
    
    /** Creates a new instance of SlidingWindowTrimmingSpec */
    public static SlidingWindowTrimmingSpec getDefaultSpec()
    {
        
        Hashtable params = new Hashtable();
        params.put("SW_Q_WINDOW_SIZE", String.valueOf(QUALITY_WINDOW_SIZE) ); 
        params.put("SW_Q_NUMBER_LOW_QUALITY_BASES", String.valueOf(QUALITY_NUMBER_LOW_QUALITY_BASES) );  
        params.put("SW_Q_N_LOW_QUALITY_BASES_CONQ", String.valueOf(QUALITY_NUMBER_LOW_QUALITY_BASES_CONQ) ); 
        params.put( "SW_Q_CUTT_OFF", String.valueOf(QUALITY_CUTT_OFF) ); 
        params.put( "SW_A_WINDOW_SIZE", String.valueOf(AMBIQUATY_WINDOW_SIZE) ); 
        params.put("SW_A_NUMBER_BASES", String.valueOf(AMBIQUATY_NUMBER_BASES) ); 
        params.put("SW_A_N_LOW_QUALITY_BASES_CONQ", String.valueOf(AMBIQUATY_NUMBER_LOW_QUALITY_BASES_CONQ) ); 
        params.put( "SW_MIN_DISTANCE_BTW_CONTIGS", String.valueOf(MIN_DISTANCE_BETWEEN_CONTIGS) ); 
        params.put( "SW_TRIM_TYPE", String.valueOf(TRIM_TYPE_MOVING_WINDOW_NODISC) ); 
        params.put( "SW_MIN_CONTIG_LENGTH", String.valueOf( MIN_CONTIG_LENGTH) ); 
        SlidingWindowTrimmingSpec spec = new SlidingWindowTrimmingSpec( params,  "NA", -1);
        return spec;
    }
    
    public SlidingWindowTrimmingSpec(Hashtable p, String na, int submitter_id,int id) 
    {
         super( p,  na, submitter_id,TRIM_SLIDING_WINDOW_SPEC_INT, id);
         cleanup_parameters();
    }
    
    public SlidingWindowTrimmingSpec(Hashtable p, String na, int submitter_id) 
    {
         super( p,  na, submitter_id,TRIM_SLIDING_WINDOW_SPEC_INT);
         cleanup_parameters();
    }
    public int getQWindowSize ()  throws Exception
    { 
        if ( m_quality_window_size == -1)
            m_quality_window_size = this.getParameterByNameInt("SW_Q_WINDOW_SIZE")   ;
        return m_quality_window_size;
    } 
    public int getQMaxNumberLowQualityBases ()throws Exception
    { 
        if ( m_max_number_low_quality_bases_in_window == -1)
            m_max_number_low_quality_bases_in_window = this.getParameterByNameInt("SW_Q_NUMBER_LOW_QUALITY_BASES")   ;
        return m_max_number_low_quality_bases_in_window;
    } 
    public int getQMaxNumberConsLowQualityBase ()throws Exception
    { 
        if ( m_max_number_conq_low_quality_bases_in_window == -1)
            m_max_number_conq_low_quality_bases_in_window = this.getParameterByNameInt("SW_Q_N_LOW_QUALITY_BASES_CONQ")   ;
        return m_max_number_conq_low_quality_bases_in_window;
    } 
    public int getMinContigLength ()throws Exception
    { 
        if ( m_min_stretch_length_to_collapse == -1)
            m_min_stretch_length_to_collapse = this.getParameterByNameInt("SW_MIN_CONTIG_LENGTH")   ;
        return m_min_stretch_length_to_collapse;
    } 
    
    public int getQCutOff ()throws Exception
    { 
        if ( m_quality_cutoff == -1)
            m_quality_cutoff = this.getParameterByNameInt("SW_Q_CUTT_OFF")   ;
        return m_quality_cutoff;
    }
    
    public int getAWindowSize ()throws Exception
    { 
        if ( m_ambiquaty_window_size == -1)
            m_ambiquaty_window_size = this.getParameterByNameInt("SW_A_WINDOW_SIZE")   ;
        return m_ambiquaty_window_size;
    } 
    
    public int getAMaxNumberLowQualityBases  ()throws Exception
    { 
        if ( m_ambiquaty_low_quality_bases_in_window == -1)
            m_ambiquaty_low_quality_bases_in_window = this.getParameterByNameInt("SW_A_NUMBER_BASES")   ;
        return m_ambiquaty_low_quality_bases_in_window;
    } 
    
    public int getAMaxNumberConsLowQualityBase ()throws Exception
    { 
        if ( m_ambiquaty_conq_low_quality_bases_in_window == -1)
            m_ambiquaty_conq_low_quality_bases_in_window = this.getParameterByNameInt("SW_A_N_LOW_QUALITY_BASES_CONQ")   ;
        return m_ambiquaty_conq_low_quality_bases_in_window;
    }
    
    public int getTrimmingType()throws Exception
    { 
        if ( m_type == -1)
            m_type = this.getParameterByNameInt("SW_TRIM_TYPE")   ;
        return m_type;
    } 
    
    public int getMinDistanceBetweenStretches()throws Exception
    { 
        if ( m_min_distance_between_two_stretches_to_collapse == -1)
            m_min_distance_between_two_stretches_to_collapse = this.getParameterByNameInt("SW_MIN_DISTANCE_BTW_CONTIGS")   ;
        return m_min_distance_between_two_stretches_to_collapse;
    } 
   
           
       
    public void setQWindowSize (int v)
    { 
        m_quality_window_size   = v;
        m_params.put("SW_Q_WINDOW_SIZE", String.valueOf(v) );
    } 
    public void setQMaxNumberLowQualityBases (int v)
    { 
        m_max_number_low_quality_bases_in_window     = v;
         m_params.put("SW_Q_NUMBER_LOW_QUALITY_BASES", String.valueOf(v) ); 
    } 
    public void setQMaxNumberConsLowQualityBase (int v)
    {  
        m_max_number_conq_low_quality_bases_in_window     = v;
        m_params.put("SW_Q_N_LOW_QUALITY_BASES_CONQ", String.valueOf(v) ); 
    } 
    
    public void setMinContigLength (int v)throws Exception
    { 
        m_min_stretch_length_to_collapse     = v;
        m_params.put("SW_MIN_CONTIG_LENGTH", String.valueOf(v) ); 
      
    } 
    
    public void setQCutOff (int v)
    { 
        m_quality_cutoff     = v;
         m_params.put( "SW_Q_CUTT_OFF", String.valueOf(v) ); 
    } 
    public void setAWindowSize (int v)
    {  
        m_ambiquaty_window_size     = v;
           m_params.put( "SW_A_WINDOW_SIZE", String.valueOf(v) ); 
    } 
    public void setAMaxNumberLowQualityBases  (int v)
    {  
        m_ambiquaty_low_quality_bases_in_window     = v;
         m_params.put("SW_A_NUMBER_BASES", String.valueOf(v) ); 
    } 
    public void setAMaxNumberConsLowQualityBase (int v)
    { 
        m_ambiquaty_conq_low_quality_bases_in_window     = v;
        m_params.put("SW_A_N_LOW_QUALITY_BASES_CONQ", String.valueOf(v) ); 
    } 
    public void setTrimmingType(int v)
    {
        m_type = v;
        m_params.put( "SW_TRIM_TYPE", String.valueOf(v) ); 
    }
    public void setMinDistanceBetweenStretches(int v)
    { 
        m_min_distance_between_two_stretches_to_collapse= v;
        m_params.put( "SW_MIN_DISTANCE_BTW_CONTIGS", String.valueOf(v) ); 
    }

   
    
  
    
 
    
     public static ArrayList getAllSpecs() throws BecDatabaseException
     {
         return getAllSpecsByType(TRIM_SLIDING_WINDOW_SPEC_INT, true);
     }
     
     public static ArrayList getAllSpecNames() throws BecDatabaseException
     {
         return getAllSpecsByType(TRIM_SLIDING_WINDOW_SPEC_INT, false);
     }
     public static ArrayList getAllSpecsBySubmitter(int submitter_id) throws BecDatabaseException
     {
         return getAllSpecsByTypeAndSubmitter(TRIM_SLIDING_WINDOW_SPEC_INT, submitter_id);
     }
     
     protected void cleanup_parameters()    
     {    
         try
         {
            cleanup_parameters("SW_");
         }
         catch(Exception e1)
         {
             System.out.println(e1.getMessage());
         }
     }
     
     protected boolean validateParameters()     {return true;     }
      
     
     public static void main(String [] args) 
      
      {
        
        try 
        {
          edu.harvard.med.hip.bec.user.User user = edu.harvard.med.hip.bec.user.AccessManager.getInstance().getUser("htaycher123","htaycher");
           ArrayList  specs = SlidingWindowTrimmingSpec.getAllSpecsBySubmitter( user.getId());
             specs = SlidingWindowTrimmingSpec.getAllSpecs();
        }
        catch(Exception e)
        {
        }
     }
}
