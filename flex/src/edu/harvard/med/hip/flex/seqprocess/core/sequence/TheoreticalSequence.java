/*
 * TheoreticalSequence.java
 *
 * Created on September 24, 2002, 1:28 PM
 *analog of flexsequence
 */

package edu.harvard.med.hip.flex.seqprocess.core.sequence;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;
import javax.sql.*;
/**
 *
 * @author  htaycher
 */
public class TheoreticalSequence extends BaseSequence
{
    public static final int THEORETICAL_SEQUENCE = 3;
    
    private String m_species = null;
    private String m_dateadded= null;
    private String m_quality= null;
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
    public TheoreticalSequence(String t)throws FlexDatabaseException
    {
        super("",THEORETICAL_SEQUENCE);
       // throw new FlexDatabaseException("Constrator not allowed");
    }
    
    public TheoreticalSequence(int id) throws FlexDatabaseException
    {
        super( id);
        m_type = THEORETICAL_SEQUENCE;
        try
        {
            restore(id);
        } catch (FlexDatabaseException fde)
        {
            System.out.println(fde.getMessage());
        }
    }
    
    
    public void insert(Connection conn)throws FlexDatabaseException
    {
        throw new FlexDatabaseException("Insert not aloowed for theoretical sequence");
    }
    
  
    
    /**
     * Get the species value.
     *
     * @return The species value.
     */
    public String getSpecies()    {    return m_species;}
    public String getDateadded()    {    return m_dateadded;}
    public String getFastaHeader()    {    return new String(">"+ super.getId());}
    public ArrayList getPublicInfo()    {    return m_publicInfo;}
    public int    getCdsStart(){ return m_start;}
    public int    getCdsStop(){ return m_stop;}
    public int    getGCcontant(){ return m_gc_content;}
    public String getCodingSequence()    {        return getText().substring(m_start-1,m_stop);
    }
    
    
    
    
    public ArrayList getFullSequences()throws FlexDatabaseException
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
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
    public static ArrayList getFullSequences(int id)throws FlexDatabaseException
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
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+E+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
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
            Hashtable h = (Hashtable)m_publicInfo.get(elm);
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.GENBANK_ACCESSION))
            {
               m_currentAccession = (String)h.get(FlexSequence.NAMEVALUE);
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
            Hashtable h = (Hashtable)m_publicInfo.get(elm);
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.GI))
            {
                m_currentGi = (String)h.get(FlexSequence.NAMEVALUE);
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
            Hashtable h = (Hashtable)m_publicInfo.get(elm);
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.LOCUS_ID))
            {
                m_currentLocusLink = (String)h.get(FlexSequence.NAMEVALUE);
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
        String namevalue = null;
        
         for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            Hashtable h = (Hashtable)m_publicInfo.get(elm);
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.GENE_SYMBOL))
            {
                if(namevalue == null)
                {
                    namevalue = (String)h.get(FlexSequence.NAMEVALUE);
                } else
                {
                    namevalue = namevalue+", "+(String)h.get(FlexSequence.NAMEVALUE);
                }
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
        String namevalue=null;
        for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            Hashtable h = (Hashtable)m_publicInfo.get(elm);
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.PANUMBER))
            {
                namevalue = (String)h.get(FlexSequence.NAMEVALUE);
                break;
            }
        }
        return namevalue;
    }
    
    /**
     * Return sequence description.
     *
     * @return The sequence description.
     */
    public String getDescription()
    {
        if(m_currentDescription != null) return m_currentDescription;
        
        String description = null;
        for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            Hashtable h = (Hashtable)m_publicInfo.get(elm);
            
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.GENBANK_ACCESSION))
            {
                description = (String)h.get(FlexSequence.DESCRIPTION);
                if (description != null)
                    return description;
            }
            
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.GI))
            {
                description = (String)h.get(FlexSequence.DESCRIPTION);
                if (description != null)
                    return description;
            }
            
            if(((String)h.get(FlexSequence.NAMETYPE)).equals(FlexSequence.UNIGENE_SID))
            {
                description = (String)h.get(FlexSequence.DESCRIPTION);
                if (description != null)
                    return description;
            }
        }
        return description;
    }
    
    
    
    /**
     * Restore the data from database by sequence id.
     *
     * @param id The sequence id.
     * @exception FlexDatabaseException.
     */
    public void restore(int id) throws FlexDatabaseException
    {
        String sql = "select s.flexstatus as status,"+
        "s.genusspecies as species,"+
        "s.cdsstart as cdsstart,"+
        "s.cdsstop as cdsstop,"+
        "s.cdslength as cdslength,"+
        "s.gccontent as gccontent,"+
        "to_char(s.dateadded, 'fmYYYY-MM-DD') as dateadded\n"+
        "from flexsequence s\n"+
        "where s.sequenceid="+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        try
        {
            while(rs.next())
            {
           
                m_species = rs.getString("SPECIES");
                m_dateadded = rs.getString("DATEADDED");
                m_start = rs.getInt("CDSSTART");
                m_stop = rs.getInt("CDSSTOP");
                m_gc_content = rs.getInt("GCCONTENT");
            }
            
            // public info stuff
            sql = "select * from name where sequenceid="+id;
            
            rs = t.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            m_publicInfo = new ArrayList();
            while(rs.next())
            {
                Hashtable ht = new Hashtable(cols);
                
                for(int i = 1 ;i <= cols; i++)
                {
                    Object o = rs.getObject(i);
                    if(o != null)
                        ht.put(meta.getColumnLabel(i),o);
                }
                m_publicInfo.add(ht);
            }
      
        } catch (SQLException sqlE)
        {
            throw new FlexDatabaseException("Error occured while restoring sequence with id "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
  
    
    public static TheoreticalSequence findSequenceByGi(String gi)
    {
        TheoreticalSequence seq = null;
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
                seq = new TheoreticalSequence(id);
            }
        } catch (SQLException sqlE)
        {
        } catch (FlexDatabaseException e)
        {
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        return seq;
    }
    public static TheoreticalSequence findSequenceByGi(int gi)
    {
        TheoreticalSequence seq = null;
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
                seq = new TheoreticalSequence(id);
            }
        } catch (SQLException sqlE)
        {
        } catch (FlexDatabaseException e)
        {
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        return seq;
    }
    
    //__________________________________________________________________________
    
    public static void main(String [] args)
    {
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            TheoreticalSequence theoretical_sequence = TheoreticalSequence.findSequenceByGi(String.valueOf("4503092"));
            int refseqid = theoretical_sequence.getId();
       
            String query="ATGGAGCTACGTGTGGGGAACAAGTACCGCCTGGGACGGAAGATCGGGAGCGGGTCCTTCGGAGATATCTACCTGGGTGCCAACATCGCCTCTGGTGAGGAAGTCGCCATCAAGCTGGAGTGTGTGAAGACAAAGCACCCCCAGCTGCACATCGAGAGCAAGTTCTACAAGATGATGCAGGGTGGCGTGGGGATCCCGTCCATCAAGTGGTGCGGAGCTGAGGGCGACTACAACGTGATGGTCATGGAGCTGCTGGGGCCTAGCCTCGAGGACCTGTTCAACTTCTGTTCCCGCAAATTCAGCCTCAAGACGGTGCTGCTCTTGGCCGACCAGATGATCAGCCGCATCGAGTATATCCACTCCAAGAACTTCATCCACCGGGACGTCAAGCCCGACAACTTCCTCATGGGGCTGGGGAAGAAGGGCAACCTGGTCTACATCATCGACTTCGGCCTGGCCAAGAAGTACCGGGACGCCCGCACCCACCAGCACATTCCCTACCGGGAAAACAAGAACCTGACCGGCACGGCCCGCTACGCTTCCATCAACACGCACCTGGGCATTGAGCAAAGCCGTCGAGATGACCTGGAGAGCCTGGGCTACGTGCTCATGTACTTCAACCTGGGCTCCCTGCCCTGGCAGGGGCTCAAAGCAGCCACCAAGCGCCAGAAGTATGAACGGATCAGCGAGAAGAAGATGTCAACGCCCATCGAGGTCCTCTGCAAAGGCTATCCCTCCGAATTCTCAACATACCTCAACTTCTGCCGCTCCCTGCGGTTTGACGACAAGCCCGACTACTCTTACCTACGTCAGCTCTTCCGCAACCTCTTCCACCGGCAGGGCTTCTCCTATGACTACGTCTTTGACTGGAACATGCTGAAATTCGGTGCAGCCCGGAATCCCGAGGATGTGGACCGGGAGCGGCGAGAACACGAACGCGAGGAGAGGATGGGGCAGCTACGGGGGTCCGCGACCCGAGCCCTGCCCCCTGGCCCACCCACGGGGGCCACTGCCAACCGGCTCCGCAGTGCCGCCGAGCCCGTGGCTTCCACGCCAGCCTCCCGCATCCAGCCGGCTGGCAATACTTCTCCCAGAGCGATCTCGCGGGTCGACCGGGAGAGGAAGGTGAGTATGAGGCTGCACAGGGGTGCGCCCGCCAACGTCTCCTCCTCAGACCTCACTGGGCGGCAAGAGGTCTCCCGGATCCCAGCCTCACAGACAAGTGTGCCATTTGACCATCTCGGGAAGTTGG";
            TheoreticalSequence fl =  TheoreticalSequence.findSequenceByGi("123456789");
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
