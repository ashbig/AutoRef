/*
 * Oligo.java
 *
 * Created on September 30, 2002, 4:43 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.oligo;


import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.workflow.*;

/**
 * This class represents an oligo object.
 * $Id: Oligo.java,v 1.1 2003-03-07 16:45:50 wendy Exp $
 * @File:	Oligo.java

 */
public class Oligo
{
    private static final String GATEWAYTAG_5p = "AAAGCAGGCTCCACC";
    private static final String GATEWAYTAG_3p_CLOSE = "ACAAGAAAGCTGGGTCCTA";
    private static final String GATEWAYTAG_3p_CLOSE_PA = "AGAAAGCTGGGTCCTA";
    private static final String GATEWAYTAG_3p_FUSION = "ACAAGAAAGCTGGGTCCAA";
    private static final String CLONETECH_5p = "GAAGTTATCAGTCGACACC";
    private static final String CLONETECH_3p_CLOSE = "ATGGTCTAGAAAGCTTCCCTA";
    private static final String CLONETECH_3p_FUSION = "ATGGTCTAGAAAGCTTCCCAA";
    public static final String PSEUDOMONAS_5p = "AAAGCAGGCTCCGAAGGAGATACC";
    public static final String PSEUDOMONAS_3p_FUSION = "AGAAAGCTGGGTCTCC";
    
    public static final String OT_PCR_5p = "PCR_5p";
    public static final String OT_PCR_3p_CLOSE = "PCR_3p_CLOSE";
    public static final String OT_PCR_3p_FUSION = "PCR_3p_FUSION";
    public static final String OT_UNIVERSAL_5p = "UNIVERSAL_5p";
    public static final String OT_UNIVERSAL_3p = "UNIVERSAL_3p";
    public static final String OT_SEQ_3p_CLOSE = "SEQ_3p_CLOSE";
    public static final String OT_SEQ_3p_OPEN = "SEQ_3p_FUSION";
    
    
    public static final String OT_SEQ_3p = "SEQ_3p";
    public static final String OT_SEQ_5p = "SEQ_5p";
  
    
    private int m_id = -1;

    private String m_sequence = null;
    private double m_Tm = -1;
    private String m_tagSequence = null;
    
    private String m_type = null;//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    private int    m_gc_content = -1;
    private int    m_oligo_start = -1;// for full sequencing, start of the prime regarding sequence start
    private String m_oligo_name = null;

    
    /**
     * Constructor. Return an Oligo object.
     *
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
     */
    //   public Oligo(String type, String sequence, double Tm) throws FlexDatabaseException {
    public Oligo( String sequence, double Tm)
    {
       
       m_sequence = Algorithms.cleanWhiteSpaces(sequence);
       m_Tm = Tm;
     //  m_id = FlexIDGenerator.getID("seqoligoid");
    }
    
     public Oligo( )  
    {
       
     
    }
    
    /**
     * Constructor. Return an Oligo object.
     *
     * @param id The oligoID
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
     */
    //  public Oligo(int id, String type, String sequence, double Tm) {
    public Oligo(int id,  String sequence, double Tm) 
    {
       
       m_id = id;
       m_sequence =  Algorithms.cleanWhiteSpaces(sequence);
       m_Tm = Tm;
    }
    
     public Oligo( String sequence, double Tm, String oligoname, String oligotype, int oligostart, int gccontent) 
    {
       
       m_sequence =  Algorithms.cleanWhiteSpaces(sequence);
       m_Tm = Tm;
      //m_id = FlexIDGenerator.getID("seqoligoid");
       m_type = oligotype;//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
        m_gc_content = gccontent;
        m_oligo_start = oligostart;// for full sequencing, start of the prime regarding sequence start
        m_oligo_name = oligoname;
    }
     
    public Oligo( String sequence, double Tm, String oligoname, String oligotype, int oligostart) 
    {
       
       m_sequence =  Algorithms.cleanWhiteSpaces(sequence);
       m_Tm = Tm;
       //m_id = FlexIDGenerator.getID("seqoligoid");
       m_type = oligotype;//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
       m_oligo_start = oligostart;// for full sequencing, start of the prime regarding sequence start
       m_oligo_name = oligoname;
    }
    /**
     * Add the 5p tag to 5p oligo corresponding to the project and workflow
     */
    public void setTagSequence_5p(Project project, Workflow workflow)
    {
       
        int workflowid = workflow.getId();
        
        if(workflowid == Workflow.CREATOR_WORKFLOW || workflowid == Workflow.MGC_CREATOR_WORKFLOW)
        {
            m_tagSequence = CLONETECH_5p + m_sequence;
        } else if(workflowid == Workflow.PSEUDOMONAS_WORKFLOW)
        {
            m_tagSequence = PSEUDOMONAS_5p + m_sequence;
        } else
        {
            m_tagSequence = GATEWAYTAG_5p + m_sequence;
        }
    }
    
    /**
     * Add the 3p stop tag to 3s oligo corresponding to the project and workflow
     * the stop codon is included into the tag sequence
     */
    public void setTagSequence_3p_Close(Project project, Workflow workflow)
    {
       
        /**
         * Changed the tag sequence to attached to workflow rather than project. - dzuo 7/10/02
         */
        int workflowid = workflow.getId();
        
        if(workflowid == Workflow.CREATOR_WORKFLOW || workflowid == Workflow.MGC_CREATOR_WORKFLOW)
        {
            m_tagSequence = CLONETECH_3p_CLOSE + m_sequence;
        } else if(workflowid == Workflow.PSEUDOMONAS_WORKFLOW)
        {
            m_tagSequence = GATEWAYTAG_3p_CLOSE_PA + m_sequence;
        } else
        {
            m_tagSequence = GATEWAYTAG_3p_CLOSE + m_sequence;
        }
    }
    
    /**
     * Add the 3p open tag to 3op oligo corresponding to the project and workflow
     *
     */
    public void setTagSequence_3p_Fusion(Project project, Workflow workflow)
    {
     
        
        /**
         * Changed the tag sequence to attached to workflow rather than project. - dzuo 7/10/02
         */
        int workflowid = workflow.getId();
        
        if(workflowid == Workflow.CREATOR_WORKFLOW || workflowid == Workflow.MGC_CREATOR_WORKFLOW)
        {
            m_tagSequence = CLONETECH_3p_FUSION + m_sequence;
        } else if(workflowid == Workflow.PSEUDOMONAS_WORKFLOW)
        {
            m_tagSequence = PSEUDOMONAS_3p_FUSION + m_sequence;
        } else
        {
            m_tagSequence = GATEWAYTAG_3p_FUSION + m_sequence;
        }
    }
 
    
    //////////////////////////////getters & setters ////////////////////////////
    
    
   
    public double getTm()    {        return m_Tm;    }
    public int getOligoLength()    {        return m_sequence.length();    }
    public int getId()    {        return m_id;    }
    public String getType(){ return m_type ;}//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    public int    getGCContent() { return m_gc_content ;}
    public int    getStart() { return m_oligo_start;}// for full sequencing, start of the prime regarding sequence start
    public String getName() { return m_oligo_name ;}
    public String getSequence() { return m_sequence ;}
    
    public void setTm(double s )    {         m_Tm= s ;    }
  
    public void setId(int s )    {         m_id= s ;    }
    public void setType(String s ){  m_type = s ;}//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    public void    setGCContent(int s ) {  m_gc_content = s ;}
    public void    setStart(int s ) {  m_oligo_start= s ;}// for full sequencing, start of the prime regarding sequence start
    public void setName(String s ) {  m_oligo_name = s ;}
    public void setSequence(String s){ m_sequence = s;}

    
      /**
     * insert oligos into Oligo table.
     */
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
        Statement stmt = null;
        if (m_sequence==null || m_sequence.length()==0) return;
        if (m_id == -1 ) m_id = FlexIDGenerator.getID("seqoligoid");
        String sql = "INSERT INTO seq_oligo (seqoligoid, oligosequence, tm,  oligotype, " +
        "gccontent, oligostart, oligoname) VALUES(" +m_id+ ",'" + m_sequence + "',"
        + m_Tm + ", '"+m_type+"',"+m_gc_content+"," + m_oligo_start+",'"+m_oligo_name+"')";
        
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception sqlE)
        {
            System.out.println(sqlE);
            System.out.println(sql);
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertOligo
    
   public Oligo(int id) throws FlexDatabaseException
    {
        m_id = id;
        
        String sql = "select  oligosequence, tm, gatewaysequence, oligotype, " +
        "gccontent, oligostart, oligoname from seq_oligo where seqoligoid = "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                m_sequence = rs.getString("OLIGOSEQUENCE");
                m_Tm = rs.getDouble("TM");
                m_tagSequence = rs.getString("gatewaysequence");
                m_type = rs.getString("oligotype");//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                m_gc_content = rs.getInt("gccontent");
                m_oligo_start = rs.getInt("oligostart");;// for full sequencing, start of the prime regarding sequence start
                m_oligo_name = rs.getString("oligoname");
            }
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    } //constructor
    
    public String toString()
    {
        return "Sequence : "+ m_sequence+"\tTm: " + m_Tm +"\tType: "+ m_type +
        "Gccont: " + m_gc_content + "\tStart: "+ m_oligo_start +"\tName: " + m_oligo_name;
    }
    //////////////////////////////end getters & setters ////////////////////////////
  
    public static void main(String [] args)
    {
        Connection c = null;
        int oligoid = 1;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            Oligo o = new Oligo("TCGCGTTAACGCTAGCATGGATCTC",-1,"ATTF",Oligo.OT_UNIVERSAL_5p,-1);
            
            System.out.print("Oligo ID: "+o.getId()+ "\n");
            System.out.println("Oligo Sequence: "+o.getSequence());
            System.out.println("Oligo TM: "+ o.getTm());
            o.insert(c);
            c.commit();
            Oligo ol = new Oligo(o.getId());
             System.out.print("Oligo ID: "+ol.getId()+ "\n");
            System.out.println("Oligo Sequence: "+ol.getSequence());
            System.out.println("Oligo TM: "+ ol.getTm());
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }finally
        {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}
