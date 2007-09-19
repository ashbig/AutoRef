/*
 * ImportFleaxSequence.java
 *
 * Created on June 22, 2007, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.coreobjectsforimport;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
/**
 *
 * @author htaycher
 */
public class ImportFlexSequence 
{
   
    public static final String   PROPERTY_NAME_SEARCHID = "SEARCHID";
     public static final String   PROPERTY_NAME_SEQUENCETEXT = "TEXT";
     public static final String   PROPERTY_NAME_CDS_START = "CDS_START";
     public static final String   PROPERTY_NAME_CDS_STOP = "CDS_STOP";
     public static final String   PROPERTY_NAME_SPECIES = "SPECIES";
      public static final String   PROPERTY_NAME_CDSNA_SOURCE = "CDSNASOURCE";
       public static final String   PROPERTY_NAME_CHROMOSOME = "CHROMOSOME";
    
     public static final String   PROPERTY_NAME_IS_CHECK_CDS = "IS_CHECK_CDS";   
     public static final String   PROPERTY_VALUE_NOTCHECK_CDS = "N";   
       
    private ArrayList         m_constructs = null;
   
     
    private ArrayList         m_additional_info = null;
    private int               m_id = -1;
    private String            m_flexstatus ="INPROCESS";
    private String            m_species = null;
    private String            m_cdnasource = null;
    private String            m_chromosome = null;
    private int                 m_cds_start = -1;
    private int                 m_cds_stop =-1;
    private int                 m_gc_content =-1;
    private String              m_sequencetext = null;
    /** Creates a new instance of ImportFleaxSequence */
    public ImportFlexSequence()
    {
        m_additional_info = new ArrayList();
     }
    
    public String toString()
    {
        StringBuffer seq = new StringBuffer();
        seq.append("ID: "+m_id +"\n");
        seq.append("Status: "+m_flexstatus+"\n");
        seq.append("Species: "+m_species+"\n");
        seq.append("CDNA sorse: "+m_cdnasource+"\n");
        seq.append("Chromosome"+m_chromosome+"\n");
        seq.append("cda start: "+m_cds_start+"\n");
        seq.append("cda stop: "+m_cds_stop+"\n");
        seq.append("sequence: "+m_sequencetext+"\n");
        
        for (int count =0; count < m_additional_info.size(); count++)
        {
            seq.append( (PublicInfoItem) m_additional_info.get(count)+"\n");
        }
        return seq.toString();
     
    }
    
    public ArrayList getPublicInfo(){ return m_additional_info ;}
    public void setPublicInfo(ArrayList v){ this.m_additional_info = v;}
  public          void        addPublicInfo(PublicInfoItem v){m_additional_info.add(v);}
  public          void        addPublicInfoItems(ArrayList v){m_additional_info.addAll(v);}
    
   
    public void               setId  (int v) { m_id = v;}
    public void            setFlexStatus  (String v) { m_flexstatus = v;}
    public void            setSpesies  (String v) { m_species = v;}
    public void            setCDNASource  (String v) { m_cdnasource = v;}
    public void            setChromosome  (String v) { m_chromosome = v;}
    public void                 setCDSStart  (int v) { m_cds_start = v;}
    public void                 setCDSStop  (int v) { m_cds_stop =v;}
    public  void                setSequenceText(String v){ m_sequencetext = v;}
    public void                 setGCContent(int v){ m_gc_content = v;}
    
    
    public int               getId  ( ) { return m_id  ;}
public String            getFlexStatus  ( ) { return m_flexstatus  ;}
public String            getSpesies  ( ) { return m_species  ;}
public String            getCDNASource  ( ) { return m_cdnasource  ;}
public String            getChromosome  ( ) { return m_chromosome  ;}
public int                 getCDSStart  ( ) { return m_cds_start  ;}
public int                 getCDSStop  ( ) { return m_cds_stop ;}
public  String                getSequenceText(){return  m_sequencetext ;}
public int                  getGCContent(){ return m_gc_content;}


public boolean              isVerified()
{
    if ( m_cds_start == -1 || m_cds_stop == -1 || m_sequencetext == null || m_sequencetext.length() == 0) return false;
    if ( m_cds_start >= m_sequencetext.length()) return false;
    if ( m_cds_stop > m_sequencetext.length())return false;
    if ( (m_cds_stop - m_cds_start + 1) % 3 != 0 ) return false;
    return true;
}
   

public String               getStopCodon()
{
    if ( m_cds_start == -1 || m_cds_stop == -1 || m_sequencetext == null || m_sequencetext.length() == 0) return null;
    return m_sequencetext.substring( m_cds_stop - 3, m_cds_stop);
    
}





public void insert(Connection conn, ArrayList errors) throws Exception
    {
        if(m_id == -1)m_id = FlexIDGenerator.getID("sequenceid");
            
             
            m_cdnasource = ( m_cdnasource == null) ? null : "'"+m_cdnasource+"'";
            m_chromosome = ( m_chromosome == null) ? null : "'"+m_chromosome+"'";
             
            //insert into flexsequence table.
            String sql_i  = 	"insert into flexsequence (sequenceid, flexstatus, genusspecies, dateadded,"+
            "cdsstart, cdsstop, cdslength, gccontent";
             if ( m_cdnasource != null) sql_i +=   ",cdnasource";
            if ( m_chromosome != null) sql_i += ",chromosome";
            sql_i += ")";
          
            String sql_values =  " values ("+m_id+", '"+m_flexstatus+"', '"+m_species+"',sysdate,"+
            m_cds_start+","+m_cds_stop+","+(m_cds_stop - m_cds_start + 1) +","+getGccontent(m_sequencetext, m_cds_start,m_cds_stop);
            if ( m_cdnasource != null) sql_values +=   ","+m_cdnasource;
            if ( m_chromosome != null) sql_values += ","+m_chromosome;
            sql_values += ")";
            
            DatabaseTransaction.executeUpdate(sql_i + sql_values ,conn);
            
            //insert into sequencetext table.
            if(m_sequencetext != null)
            {
                int i=0;
                while(m_sequencetext.length()-4000*i>4000) {
                    String text = m_sequencetext.substring(4000*(i), 4000*(i+1)).toUpperCase();
                    sql_i = "insert into sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
                    "values("+ m_id+","+(i+1)+",'"+text+"')";;
                    DatabaseTransaction.executeUpdate(sql_i,conn);
                    
                    i++;
                }
                String text = m_sequencetext.substring(4000*(i));
                sql_i = "insert into sequencetext(sequenceid, sequenceorder, sequencetext)\n"+
                "values("+ m_id+","+(i+1)+",'"+text+"')";
                DatabaseTransaction.executeUpdate(sql_i,conn);
            }
            
            PublicInfoItem.insertPublicInfo(  conn, "NAME", 
            m_additional_info, m_id, "SEQUENCEID",    true, errors) ;
       
          /*  if (m_constructs != null && m_constructs.size() > 0)
            {
                for (int count = 0; count < m_constructs.size(); count++)
                {
                    ((ImportConstruct)m_constructs.get(count)).insert(conn,projectid, workflowid);
                }
            }
           **/
        }
    

private int getGccontent(String sequencetext, int cds_start, int cds_stop)
{
    int result = 0;
       if(sequencetext==null || sequencetext.trim().equals("") || cds_start==-1 || cds_stop==-1)
            return 0;
        sequencetext = sequencetext.toUpperCase();
        sequencetext = sequencetext.substring(cds_start - 1, cds_stop);
        char[] arr = sequencetext.toCharArray();
        for(int count=0; count < arr.length; count++)
        {
            if( arr[count] == 'G' || arr[count] == 'C')
            {
                result++;
            }
        }
        return result;
    }


public static ImportFlexSequence             getById(int sequenceid) throws Exception
{
    ImportFlexSequence sequence = null;
  String sql = "select  flexstatus ,genusspecies  ,"+
        "  cdsstart,   cdsstop,  cdslength,   gccontent,"+
        "to_char(s.dateadded, 'fmYYYY-MM-DD') as dateadded "+
        "from flexsequence  where  sequenceid="+sequenceid;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs =  null;
        try 
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                sequence= new ImportFlexSequence();
                sequence.setFlexStatus( rs.getString("STATUS"));
                sequence.setSpesies( rs.getString("SPECIES"));
               // dateadded = rs.getString("DATEADDED");
                sequence.setCDSStart( rs.getInt("CDSSTART"));
                sequence.setCDSStop( rs.getInt("CDSSTOP"));
               sequence.setGCContent( rs.getInt("GCCONTENT"));
            }
            
            // public info stuff
            ArrayList seq_info = PublicInfoItem.getPublicInfo( "NAME", 
            sequenceid, "SEQUENCEID") ;
            
            
            String sequencetext = "";
            sql = "select * from sequencetext where sequenceid="+sequenceid+" order by sequenceorder";
            rs = t.executeQuery(sql);
            
            while(rs.next())
            {
                sequencetext += rs.getString("SEQUENCETEXT");
            }
            sequence.setSequenceText(sequencetext);
            return sequence;
            
        } catch ( Exception sqlE) 
        {
            throw new Exception("Error occured while restoring sequence with id "+sequenceid+"\n"+sqlE.getMessage()+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
}
