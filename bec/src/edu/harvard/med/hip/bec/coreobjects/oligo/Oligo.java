/*
 * Oligo.java
 *
 * Created on September 30, 2002, 4:43 PM
 */

package edu.harvard.med.hip.bec.coreobjects.oligo;


import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import java.util.*;


/**
 * This class represents an oligo object.
 * $Id: Oligo.java,v 1.1 2003-06-03 18:57:01 Elena Exp $
 * @File:	Oligo.java

 */
public class Oligo
{
    
    // insert SYSTEM as user
    public static final int TYPE_COMMON = 1;
    public static final int TYPE_GENESEPECIFIC_CALCULATED = 2;
    public static final int TYPE_GENESEPECIFIC_MANUALADDED = 3;
    public static final int TYPE_UNIVERSAL = 0;
    public static final int TYPE_VECTORSPECIFIC = 2;
    public static final int TYPE_NOTKNOWN = -1;
    
    //for inner primers
    public static final int ORIENTATION_FORWARD = 1;
    public static final int ORIENTATION_REVERSE = -1;
    //for end reads primers
    public static final int ORIENTATION_SENSE = 1;
    public static final int ORIENTATION_ANTISENSE = -1;
    
    public static final int ORIENTATION_NOTKNOWN = 0;
    
    
    //for end reads primers
    public static final int POSITION_FORWARD = 5;
    public static final int POSITION_REVERSE = 3;
    
    private int             m_id = -1;
    private int             m_submitterid = -1;//for manually added genespecific oligo
    private String          m_sequence = null;
    private double          m_Tm = -1;
    private int             m_type = TYPE_NOTKNOWN;//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    private int             m_gc_content = -1;
    private int m_position = -1;// for full sequencing, start of the prime regarding sequence start
    private String          m_oligo_name = null;
    private int             m_orientation = ORIENTATION_NOTKNOWN;    
    
    private int m_calculationid = -1;    
    
    
    /**
     * Constructor. Return an Oligo object.
     *
     * @param type An oligo type (five, three, threeopen).
     * @param sequence The oligo sequence text.
     * @param Tm  A double type.
     * @return An Oligo object.
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
                int orientation   , int calculationid  ) throws BecDatabaseException
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

       
    }
   
       
    //////////////////////////////getters & setters ////////////////////////////
    
    
   
    public double       getTm()    {        return m_Tm;    }
    public int          getOligoLength()    {        return m_sequence.length();    }
    public int          getId()    {        return m_id;    }
    public int          getSubmitterId()    {        return m_submitterid;    }
    public int          getType(){ return m_type ;}//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    public int          getGCContent() { return m_gc_content ;}
    public int          getStart() { return m_position;}// for full sequencing, start of the prime regarding sequence start
    public String       getName() { return m_oligo_name ;}
    public String       getSequence() { return m_sequence ;}
    public int          getOrientation() { return m_orientation;}
    public int          getCalculationid(){ return m_calculationid;}
     
    public void         setTm(double s )    {         m_Tm= s ;    }
    public void         setId(int s )    {         m_id= s ;    }
    public void         setType(int s ){  m_type = s ;}//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
    public void         setGCContent(int s ) {  m_gc_content = s ;}
    public void         setStart(int s ) {  m_position= s ;}// for full sequencing, start of the prime regarding sequence start
    public void         setName(String s ) {  m_oligo_name = s ;}
    public void         setSequence(String s){ m_sequence = s;}
    public void         setOrientation(int v) {  m_orientation = v;}
     public void        setSubmitterId(int v)    {         m_submitterid = v;    }
     public void        setOligoCalculationId(int v)    {         m_calculationid = v;    }
      /**
     * insert oligos into Oligo table.
     */
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        Statement stmt = null;
        if (m_sequence==null || m_sequence.length()==0) return;
        String sql = null;
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
        try
        {
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
        "Gccont: " + m_gc_content + "\tStart: "+ m_position +"\tName: " + m_oligo_name;
    }
    
    
    public static ArrayList getOligosByCalculationId(int oligocalcid)throws BecDatabaseException
    {
        ArrayList res = new ArrayList();
        String sql = "select  oligoid,sequence,  tm,  submissiontype, " +
            "position, orientation, name,oligocalculationid,submitterid "+
            " from geneoligo where oligocalculationid = "+oligocalcid;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                Oligo ol = new  Oligo();
                ol.setId(rs.getInt("oligoid") );
                ol.setSubmitterId(rs.getInt("submitterid") );//for manually added genespecific oligo
                ol.setSequence(rs.getString("SEQUENCE"));
                ol.setTm(rs.getDouble("TM") );
                ol.setStart( rs.getInt("position")  );//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                ol.setName(   rs.getString("name") );
                ol.setOrientation(rs.getInt("orientation"));
                ol.setOligoCalculationId(oligocalcid);
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
            
            System.out.print("Oligo ID: "+o.getId()+ "\n");
            System.out.println("Oligo Sequence: "+o.getSequence());
            System.out.println("Oligo TM: "+ o.getTm());
            o.insert(c);
            c.commit();
            Oligo ol = new Oligo();//o.getId());
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
