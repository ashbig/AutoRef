/*
 * RNAMutation.java
 *
 * Created on October 19, 2002, 6:02 AM
 */

package edu.harvard.med.hip.flex.seqprocess.core.feature;

import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.seqprocess.util.*;
import java.sql.*;
/**
 *
 * @author  Administrator
 */
public class RNAMutation extends Mutation {
    
    public static final int RNA_STREAM_RANGE = 40;
    
    private String          m_upstream = null;
    private String          m_downstream = null;
    private String          m_codon_ori = null;
    private String          m_codon_mut = null;
    private int             m_codon_pos = -1;
    
    /** Creates a new instance of RNAMutation */
    public RNAMutation(int num,int fl,int pos, int ln,String obj, String subj, int hit, 
                        int seq, int id,int type,
                        String up, String dn, String cori,String codm, int cpos, int hittype)
    {
       super(num, fl, pos,  ln, obj,  subj,  hit,  seq,  id, RNA, hittype);
        m_upstream = up;
        m_downstream = dn;
        m_codon_ori = cori;
        m_codon_mut = codm;
        m_codon_pos = cpos;
        if (type ==-1) 
                m_mutation_type = setType();
        else 
             m_mutation_type = type;
        
    }
    
      /** Creates a new instance of Mutation */
    public RNAMutation(int id) throws FlexDatabaseException
    {
        m_id = id;
        
        String sql = "select  position,length,subject,query,type, hitid,sequenceid, flag,upstream,downstream,"+
                    "codonori,codonmut,codonpos, mutationtype,mutationnumber from mutation where mutationid = "+id;
        
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                m_type = rs.getInt("type");
                m_position = rs.getInt("position");
                m_length = rs.getInt("length");
                m_subject_str = rs.getString("subject");//primer type: 5p-pcr, 5p-universal, 5p-full_set_n …
                m_query_str = rs.getString("query");
                m_flag = rs.getInt("flag");
                m_hitid = rs.getInt("hitid");
                m_sequenceid = rs.getInt("sequenceid");
                m_upstream = rs.getString("upstream");
                m_downstream = rs.getString("downstream");
                m_codon_ori = rs.getString("codonori");
                m_codon_mut = rs.getString("codonmut");
                m_codon_pos = rs.getInt("codonpos");
                m_mutation_type = rs.getInt("mutationtype");
                m_number = rs.getInt("mutationnumber");
            }
        } catch (Exception sqlE)
        {
            throw new FlexDatabaseException("Error occured while initializing Oligo with id: "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
   
    
     public void insert(Connection conn) throws FlexDatabaseException
    {
         if (m_id == -1) m_id = FlexIDGenerator.getID("mutationid");
       
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO mutation (mutationid, position, type, length, "+
        "subject,query,hitid,sequenceid, flag,upstream,downstream,"+
        "codonori,codonmut,codonpos, mutationtype,mutationnumber) VALUES(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?)" ;
    
        try
        {
           
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, m_id);
            pstmt.setInt(2, m_position);
            pstmt.setInt(3, m_type);
            pstmt.setInt(4, m_length);
            if (m_subject_str.length() > 300) m_subject_str=m_subject_str.substring(0,299);
            pstmt.setString(5, m_subject_str);
            if (m_query_str.length() > 300) m_query_str=m_query_str.substring(0,299);
            pstmt.setString(6, m_query_str);
            pstmt.setInt(7, m_hitid);
            pstmt.setInt(8, m_sequenceid);
            pstmt.setInt(9, m_flag);
            pstmt.setString(10, m_upstream );
            pstmt.setString(11,m_downstream );
            if (m_codon_ori.length() > 300) m_codon_ori=m_codon_ori.substring(0,299);
            pstmt.setString(12,m_codon_ori );
            if (m_codon_mut.length() > 300) m_codon_mut=m_codon_mut.substring(0,299);
            pstmt.setString(13,m_codon_mut );
            pstmt.setInt(14,m_codon_pos);
            pstmt.setInt(15,m_mutation_type);
            pstmt.setInt(16,m_number);
            DatabaseTransaction.executeUpdate(pstmt);
              
            
        } catch (Exception sqlE)
        {
            System.out.println(sqlE.getMessage());
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
          
            DatabaseTransaction.closeStatement(pstmt);
        }
    }
    
    public String           getUpStream(){ return m_upstream;}
    public String           getDownStream(){ return m_downstream;}
     public String          getCodonOri(){ return m_codon_ori ;}
    public String           getCodonMut(){ return m_codon_mut ;}
    public int              getCodonPos(){ return m_codon_pos ;}
    
    
    public String toString()
    {
        return super.toString() +   " \nupsterm: "+m_upstream +
        " \nDownstream: " + m_downstream
        +" \ncodon ori: " +m_codon_ori 
        + "\n codon mut: " +m_codon_mut 
        +" \ncodon pos: "+m_codon_pos +"\n";
    }
    
    public String toHTMLString()
    {
        String res = "";
        
        res ="<table  border=0>"+  super.toHTMLString() +   
        "<tr><td>Upstream: </td><td>"+m_upstream + "</td></tr>" + 
        "<tr><td>Downstream</td><td>"  + m_downstream + "</td></tr>" + 
        "<tr><td>Codon ori</td><td>" +m_codon_ori + "</td></tr>" + 
        "<tr><td>Codon mut</td><td>" +m_codon_mut + "</td></tr>" + 
        "<tr><td>Codon position</td><td>"+m_codon_pos +"</td></tr>"
        +"</table>";
        return res;
    }
    //------------------
    private int  setType()
   {
         String o =m_subject_str;
        String m = m_query_str;
        if (o.length() == 0 ) o = null;
            if (m.length() == 0) m = null;
        int type = Mutation.TYPE_NONE;
        
        //point mutation
     if (o!= null && m!=null && o.length()==1 &&m.length()==1 && m_codon_ori.indexOf("-") == -1 && m_codon_mut.indexOf("-") == -1  ) 
     { 
         String aa_ori = SequenceManipulation.translateCodonToAminoAcid(m_codon_ori);
         String aa_mut = SequenceManipulation.translateCodonToAminoAcid(m_codon_mut);
	if (aa_ori.equals(aa_mut))
            type = Mutation.TYPE_RNA_SILENT;
        else
            if ( edu.harvard.med.hip.flex.seqprocess.util.Constants.getScore(aa_ori.charAt(0),aa_mut.charAt(0)) > 0 )
                type = Mutation.TYPE_RNA_NONSENSE;
            else
                type = Mutation.TYPE_RNA_MISSENSE;
	
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
                    type = Mutation.TYPE_RNA_INFRAME_DELETION;
            else
                 type = Mutation.TYPE_RNA_FRAMESHIFT_DELETION;
	}
        else if(o==null ) 
        {
            if (type == Mutation.TYPE_RNA_INFRAME) 
                    type = Mutation.TYPE_RNA_INFRAME_INSERTION;
            else
                 type = Mutation.TYPE_RNA_FRAMESHIFT_INSERTION;
	  
	}
	else
        {
	  //???????  type += Mutation.TYPE_RNA_COMPLEX;
	}	
	if (m_codon_ori != null && SequenceManipulation.isStopCodon(m_codon_ori) )
        {
             if (type == Mutation.TYPE_RNA_INFRAME) 
                    type = Mutation.TYPE_RNA_INFRAME_STOP_CODON;
            else
                 type = Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON;
	   
	}
    }

    return type;
   }
}
