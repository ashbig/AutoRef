/*
 * TheoreticalSequence.java
 *
 * Created on September 24, 2002, 1:28 PM
 *analog of flexsequence
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;
import javax.sql.*;
/**
 *
 * @author  htaycher
 */
public class RefSequence extends BaseSequence
{
    
  
    
    public static final int SPECIES_NOT_SET = 0;
    public static final int SPECIES_HUMAN = 1;
    public static final int SPECIES_YEAST = 2;
    public static final int SPECIES_MOUSE = 3;
    public static final int SPECIES_PSEUDOMONAS = 4;
    
    private int m_species = -1;
    private String m_dateadded= null;
   
    private String m_currentAccession = null;
    private String m_currentGi = null;
    private String m_currentLocusLink = null;
    private String m_currentDescription = null;
    private String m_cdnasource = null;
    private String m_chromosome = null;
    private ArrayList m_publicInfo = new ArrayList();
    
    private int    m_stop =-1;
    private int     m_start = -1;
    private int     m_gc_content = -1;
    private ArrayList   m_fullsequences = null;
    
    /** Creates a new instance of TheoreticalSequence */
    public RefSequence( String t)
    {
        super( t,THEORETICAL_SEQUENCE);
       // throw new BecDatabaseException("Constrator not allowed");
    }
    
    public RefSequence(int id) throws BecDatabaseException
    {
        m_text = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_TEXT);
        m_id = id;
        m_type = THEORETICAL_SEQUENCE;
        try
        {
            restore(id);
        } catch (BecDatabaseException fde)
        {
            System.out.println(fde.getMessage());
        }
    }
    
    
    public synchronized  void insert(Connection conn)throws BecDatabaseException
    {
       String sql =null;
       Statement stmt = null;
        try
        {
            if (m_id ==  BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    m_id = BecIDGenerator.getID("sequenceid");
                    
              sql = 	"insert into REFSEQUENCE "+
            " (SEQUENCEID ,GENUSSPECIES  ,CDSSTART  ,CDSSTOP  ,GCCONTENT  ,"+
            "CDNASOURCE  ,CHROMOSOME  ,DATEADDED )\n"+
            "values ("+m_id+ ","+m_species + ","  +m_start+","+m_stop+"," 
            +m_gc_content+",\'"+m_cdnasource+"\',\'"+m_chromosome+"',";
              if (m_dateadded == null)
                  sql += "sysdate)";
              else
                  sql += m_dateadded+")";
            DatabaseTransaction.executeUpdate(sql,conn);
        
        //insert into sequencetext table.
            BaseSequence.insertSequence( conn, this.getText(),m_id, BaseSequence.SEQUENCE_INFO_TEXT);
            //insert public info
            insertPublicInfo(conn);
           
        } catch (Exception sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
   private void   insertPublicInfo( Connection conn)throws BecDatabaseException
    {
        String sql;String values ;PublicInfoItem info_item;
        for (int i = 0; i <     m_publicInfo.size(); i++)
        {
            info_item = (PublicInfoItem) m_publicInfo.get(i);
            sql = "insert into name (sequenceid, nametype, namevalue";
             values = "values("+m_id+",'"+DatabaseTransaction.prepareString(info_item.getName())
             +"','"+DatabaseTransaction.prepareString( info_item.getValue())+"\'";

            if( info_item.getUrl()!= null)
            {
                sql = sql+",nameurl";
                values += ",\'"+DatabaseTransaction.prepareString( info_item.getUrl())+"\'";
            }

            if( info_item.getDescription()!= null)
            {
               sql = sql+",description";
                values += ",\'"+DatabaseTransaction.prepareString( info_item.getDescription())+"'";
            }

            sql = sql+")\n"+values+")";
            DatabaseTransaction.executeUpdate(sql,conn);
        }
        }
    
    /**
     * Get the species value.
     *
     * @return The species value.
     */
    public int getSpecies()    {    return m_species;}
    public String getSpeciesAsString()  
    {    
        switch( m_species)
        {
            case SPECIES_NOT_SET : return "Not known";
            case SPECIES_HUMAN : return "Homo sapiens";
            case SPECIES_YEAST : return "Saccharomyces cerevisiae";
            case SPECIES_MOUSE : return "Mus musculus";
            case SPECIES_PSEUDOMONAS : return "Pseudomonas aeruginosa";
            default:return "Not known";
        }
    }
    public String getDateadded()    {    return m_dateadded;}
    public String getFastaHeader()    {    return new String(">"+ super.getId());}
    public ArrayList getPublicInfo()    {    return m_publicInfo;}
    public int    getCdsStart(){ return m_start;}
    public int    getCdsStop(){ return m_stop;}
    public int    getGCcontent(){ return m_gc_content;}
    public String getCdnaSource()    {    return m_cdnasource;}
    public String getChromosome()    {    return m_chromosome ;}
    public String getCodingSequence()    {        return getText().substring(m_start-1,m_stop);
    }
    
    
    public void   setSpecies(int v)    {     m_species = v;}
    public void     setDateadded(String v)    {     m_dateadded= v;}
  
    public void     setPublicInfo(ArrayList v)    {     m_publicInfo= v;}
    public void    setCdsStart(int v){  m_start = v;}
    public void    setCdsStop(int v){  m_stop= v;}
    public void    setGCcontent(int v){  m_gc_content= v;}
    public void     setCdnaSource(String s)    {     m_cdnasource = s;}
    public void     setChromosome(String s)    {     m_chromosome = s ;}
    
    /*
    public ArrayList getFullSequences()throws BecDatabaseException
    {
        if ( m_fullsequences != null) return m_fullsequences;
        m_fullsequences = new ArrayList();
           
        String sql = "select sequenceid from fullsequence  where refsequenceid="+m_id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                int mid =rs.getInt("mutationid"); //obtained/analyzed/mutations cleared/final
                m_fullsequences.add(new FullSequence(mid));
            }
             return m_fullsequences;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    public static ArrayList getFullSequences(int id)throws BecDatabaseException
    {
       
        ArrayList fullsequences = new ArrayList();
           
        String sql = "select sequenceid, refsequenceid from fullsequence  where refsequenceid="+id+" order by sequenceid";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try
        {
            rs = t.executeQuery(sql);
            while(rs.next())
            {
                int wid =rs.getInt("refsequenceid");
                int mid =rs.getInt("sequenceid"); //obtained/analyzed/mutations cleared/final
                FullSequence fl = new FullSequence(mid);
              
                fullsequences.add(fl );
            }
             return fullsequences;
        } catch (Exception E)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+E+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    */
    /**
     * Return Genbank accession number.
     *
     * @return The genbank accesion number.
     */
    public String getAccession()
    {
        if(m_currentAccession != null)   return m_currentAccession;
        
        for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            PublicInfoItem info_item = (PublicInfoItem)m_publicInfo.get(elm);
            if( info_item.getName().equals(PublicInfoItem.GENBANK_ACCESSION))
            {
               m_currentAccession = info_item.getValue();
               break;
            }
        }
        return m_currentAccession;
    }
    
    
    /**
     * Return Genbank gi number.
     *
     * @return The genbank gi number.
     */
    public String getGi()
    {
        if(m_currentGi != null)    return m_currentGi;
   
         for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            PublicInfoItem info_item = (PublicInfoItem)m_publicInfo.get(elm);
            if( info_item.getName().equals(PublicInfoItem.GI))
            {
                m_currentGi = info_item.getValue() ;
                break;
            }
        }
         return m_currentGi;
        
    }
    /**
     * Return Genbank gi number.
     *
     * @return The genbank gi number.
     */
    public String getLocusLinkId()
    {
        if(m_currentLocusLink != null)    return m_currentLocusLink;
        for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            PublicInfoItem info_item = (PublicInfoItem)m_publicInfo.get(elm);
            if( info_item.getName().equals(PublicInfoItem.LOCUS_ID))
            {
                m_currentLocusLink = info_item.getValue() ;
               break;
            }
          }
         return m_currentLocusLink;
     
    }
 
    /**
     * Return all the gene symbols as a string.
     *
     * @return All the gene symbols as a string.
     */
    public String getGenesymbolString()
    {
        String namevalue = "";
          for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            PublicInfoItem info_item = (PublicInfoItem)m_publicInfo.get(elm);
            if( info_item.getName().equals(PublicInfoItem.GENE_SYMBOL))
            {
                namevalue += info_item.getValue()+ ",";
             }
       }
        return namevalue;
      
    }
    
    /**
     * Return the PA number for Pseudomonas gene.
     *
     * @return The PA number for Pseudomonas gene.
     */
    public String getPanumber()
    {
        String value="";
        for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            PublicInfoItem info_item = (PublicInfoItem)m_publicInfo.get(elm);
            if( info_item.getName().equals(PublicInfoItem.PANUMBER))
            {
                value= info_item.getValue() ;
            }
        }
        return value;
       
    }
    
    /**
     * Return sequence description.
     *
     * @return The sequence description.
     *
    public String getDescription()
    {
        if(m_currentDescription != null) return m_currentDescription;
        
        String description = null;
        for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            Hashtable h = (Hashtable)m_publicInfo.get(elm);
            
            if(((String)h.get(NAMETYPE)).equals(GENBANK_ACCESSION))
            {
                description = (String)h.get(DESCRIPTION);
                if (description != null)
                    return description;
            }
            
            if(((String)h.get(NAMETYPE)).equals(GI))
            {
                description = (String)h.get(DESCRIPTION);
                if (description != null)
                    return description;
            }
            
            if(((String)h.get(NAMETYPE)).equals(UNIGENE_SID))
            {
                description = (String)h.get(DESCRIPTION);
                if (description != null)
                    return description;
            }
        }
        return description;
    }
    
    */
    
    /**
     * Restore the data from database by sequence id.
     *
     * @param id The sequence id.
     * @exception BecDatabaseException.
     */
    public void restore(int id) throws BecDatabaseException
    {
        
        String sql = "select GENUSSPECIES  ,CDSSTART  ,CDSSTOP  ,GCCONTENT "
            +" ,CDNASOURCE  ,CHROMOSOME ,"+
        "to_char(dateadded, 'fmYYYY-MM-DD') as dateadded\n"+
        "from refsequence where sequenceid="+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        try
        {
            while(rs.next())
            {
           
                m_species = rs.getInt("GENUSSPECIES");
                m_dateadded = rs.getString("DATEADDED");
                m_start = rs.getInt("CDSSTART");
                m_stop = rs.getInt("CDSSTOP");
                m_gc_content = rs.getInt("GCCONTENT");
                m_cdnasource = rs.getString("CDNASOURCE");
                m_chromosome = rs.getString("CHROMOSOME");
            }
            
            // public info stuff
            sql = "select nametype, namevalue,nameurl,description from name where sequenceid="+id;
            
            rs = t.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            m_publicInfo = new ArrayList();
            
            while(rs.next())
            {
                m_publicInfo.add(new PublicInfoItem(rs.getString("nametype"),
                                                       rs.getString("namevalue"),
                                                       rs.getString("nameurl"),
                                                       rs.getString("description")));
                
            }
      
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
  
    
    public static RefSequence findSequenceByGi(int gi)
    {
        RefSequence seq = null;
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        
        try
        {
            t = DatabaseTransaction.getInstance();
            
            String sql = "select sequenceid, nametype, namevalue\n"+
            "from name where nametype='GI' and namevalue='"+gi+"'";
            crs = t.executeQuery(sql);
            
            if(crs.next())
            {
                int id = crs.getInt("SEQUENCEID");
                seq = new RefSequence(id);
            }
        } catch (SQLException sqlE)
        {
        } catch (BecDatabaseException e)
        {
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return seq;
    }
    
    
    public String getHTMLText()
    {
        //String res = "<font color=black>"+m_text.substring(0,m_start-1);
       // res +="</font><font color=red>"+m_text.substring(m_start-1,m_stop);
       // res +="</font><font color=black>"+m_text.substring(m_stop-1,m_text.length()-1)+"</font>";
        //convert to fasta format
        int seqIndex = 1;
        StringBuffer formatedHTMLSeq = new StringBuffer();
         for(int i = 0;i<m_text.length();i++)
        {
            
            if(seqIndex == m_start)
            {
                formatedHTMLSeq.append("<FONT COLOR=\"red\">");
            }
            if(seqIndex == m_stop+1)
            {
                formatedHTMLSeq.append("</FONT>");
            }
            
            if(seqIndex%60 == 0)
            {
                formatedHTMLSeq.append("\n");
            }
            
            formatedHTMLSeq.append(m_text.charAt(i));
            seqIndex++;
            
        }
        return formatedHTMLSeq.toString();
    }
    //__________________________________________________________________________
    
    public static void main(String [] args)
    {
        try
        {
            //DatabaseTransaction t = DatabaseTransaction.getInstance();
          //  RefSequence theoretical_sequence = RefSequence.findSequenceByGi(4503092);
          //  int refseqid = theoretical_sequence.getId();
       
         //   String query="ATGGAGCTACGTGTGGGGAACAAGTACCGCCTGGGACGGAAGATCGGGAGCGGGTCCTTCGGAGATATCTACCTGGGTGCCAACATCGCCTCTGGTGAGGAAGTCGCCATCAAGCTGGAGTGTGTGAAGACAAAGCACCCCCAGCTGCACATCGAGAGCAAGTTCTACAAGATGATGCAGGGTGGCGTGGGGATCCCGTCCATCAAGTGGTGCGGAGCTGAGGGCGACTACAACGTGATGGTCATGGAGCTGCTGGGGCCTAGCCTCGAGGACCTGTTCAACTTCTGTTCCCGCAAATTCAGCCTCAAGACGGTGCTGCTCTTGGCCGACCAGATGATCAGCCGCATCGAGTATATCCACTCCAAGAACTTCATCCACCGGGACGTCAAGCCCGACAACTTCCTCATGGGGCTGGGGAAGAAGGGCAACCTGGTCTACATCATCGACTTCGGCCTGGCCAAGAAGTACCGGGACGCCCGCACCCACCAGCACATTCCCTACCGGGAAAACAAGAACCTGACCGGCACGGCCCGCTACGCTTCCATCAACACGCACCTGGGCATTGAGCAAAGCCGTCGAGATGACCTGGAGAGCCTGGGCTACGTGCTCATGTACTTCAACCTGGGCTCCCTGCCCTGGCAGGGGCTCAAAGCAGCCACCAAGCGCCAGAAGTATGAACGGATCAGCGAGAAGAAGATGTCAACGCCCATCGAGGTCCTCTGCAAAGGCTATCCCTCCGAATTCTCAACATACCTCAACTTCTGCCGCTCCCTGCGGTTTGACGACAAGCCCGACTACTCTTACCTACGTCAGCTCTTCCGCAACCTCTTCCACCGGCAGGGCTTCTCCTATGACTACGTCTTTGACTGGAACATGCTGAAATTCGGTGCAGCCCGGAATCCCGAGGATGTGGACCGGGAGCGGCGAGAACACGAACGCGAGGAGAGGATGGGGCAGCTACGGGGGTCCGCGACCCGAGCCCTGCCCCCTGGCCCACCCACGGGGGCCACTGCCAACCGGCTCCGCAGTGCCGCCGAGCCCGTGGCTTCCACGCCAGCCTCCCGCATCCAGCCGGCTGGCAATACTTCTCCCAGAGCGATCTCGCGGGTCGACCGGGAGAGGAAGGTGAGTATGAGGCTGCACAGGGGTGCGCCCGCCAACGTCTCCTCCTCAGACCTCACTGGGCGGCAAGAGGTCTCCCGGATCCCAGCCTCACAGACAAGTGTGCCATTTGACCATCTCGGGAAGTTGG";
            RefSequence fl =  new RefSequence(348);
            System.out.println(fl.getHTMLText());
            System.out.println("O");
           // fl.insert(t.requestConnection());
            //t.requestConnection().commit();
          
            
          //  ArrayList seq = TheoreticalSequence.getFullSequences(refseqid);
          //  System.out.println(seq.size());
       //     f.setStatus(FullSequence.STATUS_FINAL);
          //  f.updateStatus(t.requestConnection());
           //  t.requestConnection().commit();
          //  f.setApprovedName("htaycher");
            //f.updateApproved(t.requestConnection());
          //  t.requestConnection().commit();
           // f.setQuality(FullSequence.QUALITY_RECOMENDED_STORAGE);
           // f.updateQuality(t.requestConnection());
            
           // t.requestConnection().commit();
             
        } catch (Exception e)
        {
            System.out.println(e);
        }
        System.exit(0);
    }
}
