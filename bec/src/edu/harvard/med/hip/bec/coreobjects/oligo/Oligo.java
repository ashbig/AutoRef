/*
 * Oligo.java
 *
 * Created on September 30, 2002, 4:43 PM
 */

package edu.harvard.med.hip.bec.coreobjects.oligo;

import  edu.harvard.med.hip.bec.*;
import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.action_runners.*;
import java.sql.*;
import java.util.*;


/**
 * This class represents an oligo object.
 * $Id: Oligo.java,v 1.15 2005-03-15 20:20:22 Elena Exp $
 * @@File:	Oligo.java

 */
public class Oligo
{
     // status in pipline
    public static final int STATUS_DESIGNED = 1;
    public static final int STATUS_APPROVED = 2;
    public static final int STATUS_ORDERED = 3;
    public static final int STATUS_REJECTED = 4;
    public static final int STATUS_PUTONHOLD = 5;
    public static final int STATUS_ANY = -1;
    
    // insert SYSTEM as user
    public static final int TYPE_COMMON = 1;
    public static final int TYPE_UNIVERSAL = 0;
    public static final int TYPE_VECTORSPECIFIC = 2;
    
    public static final int TYPE_GENESPECIFIC_CALCULATED = 3;
    public static final int TYPE_GENESPECIFIC_MANUALADDED = 4;
    //public static final int TYPE_HOLDER = 5;
    //public static final int TYPE_HOLDER = 6;
    public static final int TYPE_NOTKNOWN = -1;

   
    //for end reads primers
    public static final int ORIENTATION_SENSE = 1;
    public static final int ORIENTATION_ANTISENSE = -1;

    public static final int ORIENTATION_NOTKNOWN = 0;

    public static final int POSITION_5PRIME = 5;
    public static final int POSITION_3PRIME = 3;


    //for end reads primers
    public static final int POSITION_FORWARD = 5;
    public static final int POSITION_REVERSE = 3;
    
   
    private int             m_id = -1;
    private int             m_submitterid = -1;//for manually added genespecific oligo
    private String          m_sequence = null;
    private double          m_Tm = -1;
    private int             m_type = TYPE_NOTKNOWN;//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    private int             m_status = -1;//designed, approved, ordered, rejected
    private int             m_gc_content = -1;
    private int m_position = -1;// for full sequencing, start of the prime regarding sequence start
    private String          m_oligo_name = null;
    private int             m_orientation = ORIENTATION_NOTKNOWN;
    
    
    //for not gene specific primers only
    private int             m_leader_length = -1;
    private String          m_leader_squence = null;

    private int m_calculationid = -1;


    /**
     * Constructor. Return an Oligo object.
     *
     * @@param type An oligo type (five, three, threeopen).
     * @@param sequence The oligo sequence text.
     * @@param Tm  A double type.
     * @@return An Oligo object.
     */

   public Oligo( ){}

    public Oligo( int id  ,
                int submitterid  ,//for manually added genespecific oligo
                String sequence  ,
                double Tm  ,
                int type  ,//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                int gc_content  ,
                int oligo_start  ,// for full sequencing, start of the prime regarding sequence start
                String oligo_name  ,
                int orientation   , int calculationid , int status ) throws BecDatabaseException
    {

       m_sequence =  Algorithms.cleanWhiteSpaces(sequence);
           if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_id = BecIDGenerator.getID("oligoid");
        else
            m_id = id;
        m_submitterid = submitterid;//for manually added genespecific oligo
        m_sequence = sequence;
        m_Tm = Tm;
        m_type = type;//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
        m_gc_content = gc_content;
        m_position = oligo_start;// for full sequencing, start of the prime regarding sequence start
        m_oligo_name = oligo_name;
        m_orientation = orientation;
        m_calculationid = calculationid;
        m_status = status;


    }


    //////////////////////////////getters & setters ////////////////////////////


    public String       geneSpecificOligotoString(){ return m_oligo_name +"\t"+m_position+"\t"+m_sequence+"\t"+m_Tm+"\t"+this.getTypeAsString()+"\t"+this.getOrientationAsString();}
    public double       getTm()    {        return m_Tm;    }
    public int          getOligoLength()    {        return m_sequence.length();    }
    public int          getId()    {        return m_id;    }
    public int          getSubmitterId()    {        return m_submitterid;    }
    public int          getType(){ return m_type ;}
    public int          getStatus() { return m_status;}
    public String       getStatusAsString()
    {
        switch(m_status)
        {
            case STATUS_DESIGNED : return "Designed";
            case STATUS_APPROVED : return "Approved";
            case STATUS_ORDERED : return "Ordered";
            case STATUS_REJECTED : return "Rejected";
            case STATUS_PUTONHOLD: return "Put on hold";
            default: return "Not known";
        }
    }
    
    
    
    public String       getTypeAsString()
    {  
        return getTypeAsString(m_type);
      
    }
    public static String       getTypeAsString(int type)
    {  
        switch(type)
        {
            case TYPE_COMMON: return "Common";
            case TYPE_UNIVERSAL : return "Universal";
            case TYPE_VECTORSPECIFIC : return "Vector specific";
            case TYPE_GENESPECIFIC_CALCULATED : return "Gene specific, calculated";
            case TYPE_GENESPECIFIC_MANUALADDED: return "Gene specific, added";
            default: return "Not known";
        }
    }
    
    public static String       getOrientationAsString(int ort)
    {  
        switch(ort)
        {
            case ORIENTATION_SENSE : return "sense";
            case ORIENTATION_ANTISENSE : return "antisense";
            default : return "";
        }
    }

    
    //primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    public int          getGCContent() { return m_gc_content ;}
    public int          getPosition() { return m_position;}// for full sequencing, start of the prime regarding sequence start
    public String       getName() { return m_oligo_name ;}
    public String       getSequence() { return m_sequence ;}
    public int          getOrientation() { return m_orientation;}
    public String       getOrientationAsString() 
    { 
        if ( m_orientation == Constants.ORIENTATION_FORWARD)
            return "Forward";
        else if ( m_orientation == Constants.ORIENTATION_REVERSE)
            return "Reverse";
        return "Not known";
    }
    public int          getCalculationid(){ return m_calculationid;}
    public int          getLeaderLength(){ return m_leader_length ;}
    public String       getLeaderSequence(){ return   m_leader_squence ;}


    public void         setTm(double s )    {         m_Tm= s ;    }
    public void         setId(int s )    {         m_id= s ;    }
    public void         setType(int s ){  m_type = s ;}//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    public void         setStatus(int s ){  m_status = s;}
    public void         setGCContent(int s ) {  m_gc_content = s ;}
    public void         setPosition(int s ) {  m_position= s ;}// for full sequencing, start of the prime regarding sequence start
    public void         setName(String s ) {  m_oligo_name = s ;}
    public void         setSequence(String s)
    {
        m_sequence = s;}
    public void         setOrientation(int v) {  m_orientation = v;}
     public void        setSubmitterId(int v)    {         m_submitterid = v;    }
     public void        setOligoCalculationId(int v)    {         m_calculationid = v;    }
     
      public void       setLeaderLength(int i){  m_leader_length =i;}
    public void       setLeaderSequence(String v){    m_leader_squence =v;} 
     
     
     /**
     * insert oligos into Oligo table.
     */

    public void insert(Connection conn) throws BecDatabaseException
    {
        Statement stmt = null;
        if (m_sequence==null || m_sequence.length()==0) return;
        String sql = null;
         try
        {
            if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                     m_id = BecIDGenerator.getID("oligoid");
            if (m_type == TYPE_COMMON && m_type ==TYPE_UNIVERSAL)
            {

            }
            else if (m_type == TYPE_GENESPECIFIC_CALCULATED || m_type ==TYPE_GENESPECIFIC_MANUALADDED)
            {
                 sql = "INSERT INTO geneoligo (oligoid,sequence,  tm,  submissiontype, " +
                "position, orientation, name,oligocalculationid,submitterid, status) VALUES(" +m_id+ ",'" + m_sequence + "',"
                +m_Tm + ", " + m_type+"," + m_position +","+ m_orientation + ",'"+ m_oligo_name +"',"
                +m_calculationid+"," + m_submitterid+","+m_status +")";
            }
            else
                return;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception sqlE)
        {

            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    } //insertOligo

   public Oligo(int id, int type) throws BecDatabaseException
    {
        /*
        m_id = id;
        m_type = type;
        String sql =  null;
        if (m_type == TYPE_COMMON && m_type ==TYPE_UNIVERSAL)
        {

        }
        else if (m_type == TYPE_GENESEPECIFIC_CALCULATED && m_type ==TYPE_GENESEPECIFIC_MANUALADDED)
        {
             sql = "INSERT INTO geneoligo (oligoid,sequence,  tm,  submissiontype, " +
            "position, orientation, name,oligocalculationid,submitterid) VALUES(" +m_id+ ",'" + m_sequence + "',"
            +m_Tm + ", " + m_type+"," + m_position +","+ m_orientation + ",'"+ m_oligo_name +"',"
            +m_calculationid+"," + m_submitterid+"')";
        }
        else
            return;

        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);

            while(rs.next())
            {
                m_sequence = rs.getString("OLIGOSEQUENCE");
                m_Tm = rs.getDouble("TM");
                m_orientation = rs.getInt("orientation");
                m_position = rs.getInt("position");// for full sequencing, start of the prime regarding sequence start
                m_oligo_name = rs.getString("name");
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
         **/
    } //constructor

    public String toString()
    {
        return "Sequence : "+ m_sequence+"\tTm: " + m_Tm +"\tType: "+ m_type +
        "Gccont: " + m_gc_content + "\tStart: "+ m_position +"\tName: " + m_oligo_name+"\tstatus"+m_status;
    }


    public static ArrayList getByOligoCalculationId(int oligocalcid, int oligo_status)throws BecDatabaseException
    {
        String oligo_status_condition = "";
       if ( oligo_status != STATUS_ANY)
       {
           oligo_status_condition = " status = "+oligo_status +" and ";
       }
        String sql = "select  oligoid,sequence,  tm,  submissiontype, " +
            "position, status, orientation, name,oligocalculationid,submitterid "+
            " from geneoligo where "+oligo_status_condition+" oligocalculationid = "+oligocalcid +" order by position";
        return getOligoByRule(sql);
   }
    public static Oligo getByOligoId(int oligoid)throws BecDatabaseException
    {
      
        String sql = "select  oligoid,sequence,  tm,  submissiontype, " +
            "position, status, orientation, name,oligocalculationid,submitterid "+
            " from geneoligo where  oligoid = "+oligoid ;
        ArrayList oligos = getOligoByRule(sql);
        if ( oligos != null && oligos.size() > 0)
            return (Oligo)oligos.get(0);
        else 
            return null;
   }
    
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByRefSequenceId(int refsequenceid, int oligo_status)throws BecDatabaseException
   {
       String oligo_status_condition = "";
       if ( oligo_status != STATUS_ANY)
       {
           oligo_status_condition = " status = "+oligo_status +" and ";
       }
        String sql = "select  oligoid,sequence,  tm,  submissiontype, " +
            "position, status,orientation, name,oligocalculationid,submitterid "+
            " from geneoligo where "+oligo_status_condition+" oligocalculationid in (select oligocalculationid "+
            " from oligo_calculation where sequenceid = "+refsequenceid +") order by oligocalculationid,POSITION";
        return getOligoByRule(sql);
   }
   
   
    public static ArrayList getAllCommonPrimers() throws BecDatabaseException
   {
        String sql = "select  primerid as oligoid, name , sequence, tm, type as submissiontype from commonprimer order by name";
        return getOligoByRule(sql, true);
   }
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByCloneId(int cloneid, int oligo_status, int primers_selection_rule)throws BecDatabaseException
   {
       String oligo_status_condition = ""; String sql =  "";
       if ( oligo_status != STATUS_ANY)
       {
           oligo_status_condition = " status = "+oligo_status +" and ";
       }
       if ( primers_selection_rule == PrimerOrderRunner.OLIGO_SELECTION_FORMAT_REFSEQ_ONLY)
       {
            sql = "select  oligoid,sequence,  tm,  submissiontype, position, status,orientation, name,oligocalculationid,submitterid "
+" from geneoligo where "+oligo_status_condition+" oligocalculationid in (select oligocalculationid from oligo_calculation where stretchcollectionid is null and  sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+")))) order by oligocalculationid, POSITION";
    
       }
       else if ( primers_selection_rule == PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COLLECTION_REFSEQ)
       {
           sql = "select  oligoid,sequence,  tm,  submissiontype, position, status,orientation, name,oligocalculationid,submitterid "
+" from geneoligo where "+oligo_status_condition+" oligocalculationid in (select oligocalculationid from oligo_calculation where sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+")))) order by oligocalculationid, POSITION";
         
       }
       else if ( primers_selection_rule == PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COOLECTION_ONLY)
       {
            sql = "select  oligoid,sequence,  tm,  submissiontype, position, status,orientation, name,oligocalculationid,submitterid "
+" from geneoligo where "+oligo_status_condition+" oligocalculationid in (select oligocalculationid from oligo_calculation where stretchcollectionid in "
+" (select max(collectionid) from stretch_collection where isolatetrackingid = "
+" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+"))) order by oligocalculationid, POSITION";
        }
        return getOligoByRule(sql);
   }
    //can return sets calculated ander different configs for Primer3
   public static ArrayList getByCloneId(int cloneid, int oligo_status)throws BecDatabaseException
   {
       return getByCloneId( cloneid,  oligo_status, PrimerOrderRunner.OLIGO_SELECTION_FORMAT_STRETCH_COLLECTION_REFSEQ);
   }
  
   
   /*
      //can return sets calculated ander different configs for Primer3
   public static ArrayList getByCloneId(int cloneid, int[] oligo_status)throws BecDatabaseException
   {
       String oligo_status_condition = "";
       
       if ( oligo_status != null)
       {
           String oligo_status_str = Algorithms.convertArrayToString(oligo_status,",");
           
           oligo_status_condition = " status in ( "+oligo_status_str +") and ";
       }
        String sql = "select  oligoid,sequence,  tm,  submissiontype, position, status,orientation, name,oligocalculationid,submitterid "
+" from geneoligo where "+oligo_status_condition+" oligocalculationid in (select oligocalculationid from oligo_calculation where sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where isolatetrackingid ="
+" (select isolatetrackingid from flexinfo where flexcloneid="+cloneid+")))) order by oligocalculationid, POSITION";
        return getOligoByRule(sql);
   }
     //can return sets calculated ander different configs for Primer3
   public static ArrayList getByPlateLabel(String label)throws BecDatabaseException
   {
        String sql = "select  oligoid,sequence,  tm,  submissiontype, position, status,orientation, name,oligocalculationid,submitterid "
+" from geneoligo where oligocalculationid in (select oligocalculationid from oligo_calculation where sequenceid = "
+" (select refsequenceid from sequencingconstruct where constructid =  (select constructid from isolatetracking where sampleid ="
+" (select sampleid from sample  where containerid =(select containerid from containerheader where label ='"+label+"')))) order by oligocalculationid, POSITION";
        return getOligoByRule(sql);
   }
    */
   
  
   //-----------------------
    private static ArrayList getOligoByRule(String sql)throws BecDatabaseException
    {
        return getOligoByRule( sql, false);
    }
    private static ArrayList getOligoByRule(String sql, boolean mode_common_primer)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);

            while(rs.next())
            {
                Oligo ol = new  Oligo();
                ol.setId(rs.getInt("oligoid") );
                ol.setSequence(rs.getString("SEQUENCE"));
                ol.setTm(rs.getDouble("TM") );
               ol.setName(   rs.getString("name") );
               
                if ( !mode_common_primer ) 
                {
                    ol.setSubmitterId(rs.getInt("submitterid") );//for manually added genespecific oligo
                    ol.setOligoCalculationId(rs.getInt("oligocalculationid"));
                    ol.setStatus(rs.getInt("status"));
                ol.setPosition( rs.getInt("position")  );//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                 ol.setOrientation(rs.getInt("orientation"));
                
                }
                  ol.setType(rs.getInt("submissiontype"));
                 res.add(ol);
            }
            return res;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }


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
            Oligo o = new Oligo();//"TCGCGTTAACGCTAGCATGGATCTC",-1,"ATTF",Oligo.OT_UNIVERSAL_5p,-1);
ArrayList res = Oligo.getAllCommonPrimers();//(15001,-1);
            for (int i=0; i < res.size();i++)
            {
                o = (Oligo)res.get(i);
                System.out.println("Oligo ID: "+o.getId()+ "\n");
                System.out.println("Oligo Sequence: "+o.getSequence());
                System.out.println("Oligo TM: "+ o.getTm());
                System.out.println("Oligo Position: "+o.getPosition());
                System.out.println("Oligo type: "+o.getType());
                System.out.println("Oligo orientation: "+ o.getOrientation());
            }
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
