/*
 * RNAMutation.java
 *
 * Created on October 19, 2002, 6:02 AM
 */

package edu.harvard.med.hip.bec.coreobjects.feature;

import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.bioutil.*;
import java.sql.*;

/**
 *
 * @author  Administrator
 */
public class RNAMutation extends Mutation {
    
    public static final int RNA_STREAM_RANGE = 20;
    
 
    
       //for database update - field names
    public static final String FIELD_POLYMID = "polymid";
    public static final String FIELD_POLYMDATE = "polymdate";
    public static final String FIELD_POLYMFLAG = "polymflag";
    
    
    private String          m_upstream = null;
    private String          m_downstream = null;
    private String          m_codon_ori = null;
    private String          m_codon_mut = null;
    private int             m_codon_pos = -1;
    private int             m_polymorphismflag = -1;//polymorphism or not
    private String          m_polymid = null;
    private java.util.Date            m_polymdate = null;
    
    
    /** Creates a new instance of RNAMutation */
     public RNAMutation(){super(); m_type = RNA;}
     /*
    public RNAMutation(int num,int fl,int pos, int ln,String obj, String subj, int hit, 
                        int seq, int id,int type,
                        String up, String dn, String cori,String codm, int cpos, int hittype)
    {
       super(num,  pos,  ln, obj,  subj,  hit,  seq,  id, RNA, hittype);
        m_upstream = up;
        m_downstream = dn;
        m_codon_ori = cori;
        m_codon_mut = codm;
        m_codon_pos = cpos;
        m_polymorphismflag = fl;
        if (type ==-1) 
                m_mutation_type = setType();
        else 
             m_mutation_type = type;
        
    }
    */
      /** Creates a new instance of Mutation */
    public RNAMutation(int id) throws BecDatabaseException
    {
        m_id = id;
     
        String sql = "select  DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,POLYMFLAG ,POLYMID ,POLMDATE,CODONORI ,CODONMUT ,UPSTREAM ,DOWNSTREAM "
 +",CODONPOS ,CHANGETYPE ,DISCRNUMBER ,DISCRPOSITION  ,DISCRLENGTH  ,DISCQUALITY  from discrepancy where discrepancyid = "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                m_type = rs.getInt("TYPE");
                m_position = rs.getInt("POSITION");
                m_length = rs.getInt("LENGTH");
                m_change_ori = rs.getString("CHANGEORI");//primer type: 5p-pcr, 5p-universal, 5p-full_set_n �
                m_change_mut = rs.getString("CHANGEMUT");
                m_sequenceid = rs.getInt("SEQUENCEID");
                m_upstream = rs.getString("UPSTREAM");
                m_downstream = rs.getString("DOWNSTREAM");
                m_codon_ori = rs.getString("CODONORI");
                m_codon_mut = rs.getString("CODONMUT");
                m_codon_pos = rs.getInt("CODONPOS");
                m_change_type = rs.getInt("CHANGETYPE");
                m_number = rs.getInt("DISCRNUMBER");
                m_polymorphismflag = rs.getInt("POLYMFLAG");
                m_polymid = rs.getString("POLYMID");
                m_polymdate = rs.getDate("POLMDATE");
                m_quality = rs.getInt("DISCQUALITY");
                m_exp_position = rs.getInt("DISCRPOSITION");
            }
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage()+"\n"+sql);
            throw new BecDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
   
    
      public void insert(Connection conn) throws BecDatabaseException
    {
         if (m_id == -1) m_id = BecIDGenerator.getID("discrepancyid");
        if (m_change_type == -1) m_change_type = setType();
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO discrepancy  (DISCREPANCYID  ,POSITION ,LENGTH ,CHANGEORI ,CHANGEMUT "
 +",TYPE ,SEQUENCEID ,POLYMFLAG ,POLYMID ,POLMDATE,CODONORI ,CODONMUT ,UPSTREAM ,DOWNSTREAM "
 +",CODONPOS ,CHANGETYPE ,DISCRNUMBER ,DISCQUALITY ,DISCRPOSITION)   VALUES(?,?,?,?,?,?,?,?,?,sysdate, ?,?,?,?,?,?,?,?,?)" ;
    
        try
        {
           
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, m_id);
            pstmt.setInt(2, m_position);
            pstmt.setInt(3, m_length);
            if (m_change_ori.length() > 300) m_change_ori=m_change_ori.substring(0,299);
            pstmt.setString(4, m_change_ori);
            if (m_change_mut.length() > 300) m_change_mut=m_change_mut.substring(0,299);
            pstmt.setString(5, m_change_mut);
            pstmt.setInt(6, m_type);
            pstmt.setInt(7, m_sequenceid);
            pstmt.setInt(8, m_polymorphismflag);
            pstmt.setString(9, m_polymid );
            
             if (m_codon_ori.length() > 300) m_codon_ori=m_codon_ori.substring(0,299);
            pstmt.setString(10,m_codon_ori );
            if (m_codon_mut.length() > 300) m_codon_mut=m_codon_mut.substring(0,299);
            pstmt.setString(11,m_codon_mut );
            pstmt.setString(12, m_upstream );
            pstmt.setString(13,m_downstream );
            pstmt.setInt(14,m_codon_pos);
            pstmt.setInt(15,m_change_type);
            pstmt.setInt(16,m_number);
             pstmt.setInt(17,m_quality);
              pstmt.setInt(18,m_exp_position);
            DatabaseTransaction.executeUpdate(pstmt);
              
            
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(pstmt);
        }
    }
    
   
    public  String          getPolymorphismId(){ return m_polymid  ;}
    public  java.util.Date            getPolymorphismDate(){ return m_polymdate  ;}
    public String           getUpStream(){ return m_upstream;}
    public String           getDownStream(){ return m_downstream;}
    public String           getCodonOri(){ return m_codon_ori ;}
    public String           getCodonMut(){ return m_codon_mut ;}
    public int              getCodonPos(){ return m_codon_pos ;}
    public int              getPolymorphismFlag(){ return m_polymorphismflag;} 
    public int              getChangeType()
    { 
        if (m_change_type ==-1) m_change_type = setType();
        return m_change_type;
    }
     
    public String              getPolymorphismFlagAsString()
    { 
        switch (m_polymorphismflag)
        {
            case FLAG_POLYM_YES : return  "+";
            case FLAG_POLYM_NO : return  "-";
            case FLAG_POLYM_NOKNOWN : return  "?";
            default: return "?";
        }
    }  
    public void             setPolymFlag(int v){  m_polymorphismflag = v;}//polymorphism or not
    public void             setPolymId(String v){m_polymid =v;}
    public void             setPolymDate(java.util.Date d){m_polymdate = d;}
    public  void          setUpstream(String v){  m_upstream  = v ;}
    public  void          setDownStream(String v){  m_downstream  = v ;}
    public  void          setCodonOri(String v){  m_codon_ori  = v ;}
    public  void          setCodonMut(String v){  m_codon_mut  = v ;}
    public  void             setCodonPos(int v){  m_codon_pos  = v ;}

    
    public String toString()
    {
        return super.toString() +   " \t upsterm: "+m_upstream +
        " \t Downstream: " + m_downstream
        +" \t codon ori: " +m_codon_ori 
        + "\t codon mut: " +m_codon_mut 
        +" \t codon pos: "+m_codon_pos +"\n";
    }
    
    public String toHTMLString()
    {
        StringBuffer res = new StringBuffer();
        
        res.append("<table  border=0>");
        res.append( super.toHTMLString());   
       
        res.append( "<tr><td>Codon ori.</td><td>" +m_codon_ori + "</td></tr>"); 
        res.append( "<tr><td>Codon mut.</td><td>" +m_codon_mut + "</td></tr>"); 
        if ( m_codon_pos == 0)
            res.append( "<tr><td>Codon position</td><td>3</td></tr>");
        else
            res.append( "<tr><td>Codon position</td><td>"+m_codon_pos +"</td></tr>");
        res.append(  "</table>");
        return res.toString();
    }
    
    
     public void updatePolymorphhismData(Connection conn) throws BecDatabaseException
    {
        Statement stmt =null;
        try
        {
            String sql = "UPDATE discrepancy SET polymflag = "
            + m_polymorphismflag + ", polymdate =" + m_polymdate + " ,polymid = " 
            + m_polymid + " WHERE discrepancyid = "+ m_id;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException("Error occured while calling updateStatus method for mutation "+m_id+"\n"+sqlE);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
     
 
    //------------------
    private int  setType()
   {
         String o =m_change_ori;
        String m = m_change_mut;
        if (o.length() == 0 ) o = null;
            if (m.length() == 0) m = null;
        int type = Mutation.TYPE_NOT_DEFINE;
         String aa_ori = null;
         String aa_mut = null;
        
        //point mutation
     if (o!= null && m!=null && o.length()==1 &&m.length()==1 && m_codon_ori.indexOf("-") == -1 && m_codon_mut.indexOf("-") == -1  ) 
     { 
      //   System.out.print(SequenceManipulation.isStartCodon (m_codon_ori)+" "+SequenceManipulation.isStartCodon (m_codon_mut));
         //start codon
         if (m_codon_ori != m_codon_mut && SequenceManipulation.isStartCodon (m_codon_ori)
           && !SequenceManipulation.isStartCodon (m_codon_mut)
            && ( m_position > 0 && m_position < 4))
         {
             return Mutation.TYPE_RNA_NO_TRANSLATION;
         }
         //no stop codon
         else if( m_codon_ori != m_codon_mut && SequenceManipulation.isStopCodon(m_codon_ori)
            &&  !SequenceManipulation.isStopCodon(m_codon_mut))
         {
             return Mutation.TYPE_RNA_POST_ELONGATION;
             
         }
         //trancation in the middle
         else if( m_codon_ori != m_codon_mut && SequenceManipulation.isStopCodon(m_codon_mut)
            && !SequenceManipulation.isStopCodon(m_codon_ori))
         {
             return Mutation.TYPE_RNA_NONSENSE;
         }
         else 
         {
              aa_ori = SequenceManipulation.translateCodonToAminoAcid(m_codon_ori);
             aa_mut = SequenceManipulation.translateCodonToAminoAcid(m_codon_mut);
             if ( aa_mut.equals("") || aa_ori.equals("") )// notranslation available 
                 //not full codon  '  A'
             {
                 return Mutation.TYPE_RNA_SILENT;
             }
             else if(aa_ori.equals(aa_mut))
             {
                return Mutation.TYPE_RNA_SILENT;
             }
             else
             {
              
                    return Mutation.TYPE_RNA_MISSENSE;
             }
         }
    }  
    else 
    {
      
	int len = 0;
	if (o != null) len = o.length();
        if (m!= null)  len = len - m.length();
	
	if (len%3 == 0 ) 
        {
	    type = Mutation.TYPE_RNA_INFRAME;
	} 
        else 
        {
	    type = Mutation.TYPE_RNA_FRAMESHIFT;
	}
	if (m == null ) 
        {
	    if (type == Mutation.TYPE_RNA_INFRAME) 
                    return Mutation.TYPE_RNA_INFRAME_DELETION;
            else
                 return Mutation.TYPE_RNA_FRAMESHIFT_DELETION;
	}
        else if(o==null ) 
        {
            if (type == Mutation.TYPE_RNA_INFRAME) 
                    return Mutation.TYPE_RNA_INFRAME_INSERTION;
            else
                 return Mutation.TYPE_RNA_FRAMESHIFT_INSERTION;
	  
	}
	else
        {
	  //???????  type += Mutation.TYPE_RNA_COMPLEX;
	}	
	if (m_codon_ori != null && SequenceManipulation.isStopCodon(m_codon_ori) )
        {
             if (type == Mutation.TYPE_RNA_INFRAME) 
                   return Mutation.TYPE_RNA_INFRAME_STOP_CODON;
            else
                return Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON;
	   
	}
    }
//System.out.print(type);
    return type;
    
   }
}
