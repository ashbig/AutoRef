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
    
  
   /* 
    public static final int SPECIES_NOT_SET = 0;
    public static final int SPECIES_HUMAN = 1;
    public static final int SPECIES_YEAST = 2;
    public static final int SPECIES_MOUSE = 3;
    public static final int SPECIES_PSEUDOMONAS = 4;
    public static final int SPECIES_YP = 5;
     public static final int SPECIES_FT = 6;
     // public static final int SPECIES_HOLDER = 7;
     // public static final int SPECIES_HOLDER = 8;
     // public static final int SPECIES_HOLDER = 9;
     // public static final int SPECIES_HOLDER = 10;
     // public static final int SPECIES_HOLDER = 11;
    */
    private int         m_species_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String      i_species_name = null;
    private String      m_dateadded= null;
   
    private String      m_currentAccession = null;
    private String      m_currentGi = null;
    private String      m_currentLocusLink = null;
    private String      m_currentDescription = null;
    private String      m_cdnasource = null;
    private String      m_chromosome = null;
    private ArrayList m_publicInfo = new ArrayList();
    
    private int    m_stop =-1;
    private int     m_start = -1;
    private int     m_gc_content = -1;
    private ArrayList   m_fullsequences = null;
    
    /** Creates a new instance of TheoreticalSequence */
    public RefSequence( )
    {
        super( "",THEORETICAL_SEQUENCE);
       // throw new BecDatabaseException("Constrator not allowed");
    }
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
            restore(id, true);
        } catch (BecDatabaseException fde)
        {
            System.out.println(fde.getMessage());
            throw new BecDatabaseException(fde.getMessage());
        }
    }
    
     
    
    public RefSequence(int id, boolean isIncludePublicInfo) throws BecDatabaseException
    {
        m_text = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_TEXT);
        m_id = id;
        m_type = THEORETICAL_SEQUENCE;
        try
        {
            restore(id, isIncludePublicInfo);
        } catch (BecDatabaseException fde)
        {
            System.out.println(fde.getMessage());
            throw new BecDatabaseException(fde.getMessage());
        }
    }
    
    public RefSequence(int id, boolean isIncludePublicInfo, 
            boolean sequencetext, 
            boolean generalinfo) throws BecDatabaseException
    {
        
        if (  ! isIncludePublicInfo && !sequencetext && !generalinfo) return;
        if ( sequencetext )
            m_text = BaseSequence.getSequenceInfo(id, BaseSequence.SEQUENCE_INFO_TEXT);
        m_id = id;
        m_type = THEORETICAL_SEQUENCE;
        if ( isIncludePublicInfo ) m_publicInfo = new ArrayList();
        String sql = null; boolean first_round = true;
        
        if (isIncludePublicInfo && generalinfo)
        {
            sql = "select nametype, namevalue,nameurl,description ," 
            +" GENUSSPECIES  ,CDSSTART  ,CDSSTOP  ,GCCONTENT "
            +" ,CDNASOURCE  ,CHROMOSOME ,"+
            "to_char(dateadded, 'fmYYYY-MM-DD') as dateadded "+
            "from name n,refsequence r  where n.sequenceid = r.sequenceid and r.sequenceid="+id;

        }
        else if (isIncludePublicInfo && !generalinfo)
        {
           sql = "select nametype, namevalue,nameurl,description from name where sequenceid="+id;

        }
        else if(!isIncludePublicInfo && generalinfo)
        {
             sql = "select GENUSSPECIES  ,CDSSTART  ,CDSSTOP  ,GCCONTENT "
            +" ,CDNASOURCE  ,CHROMOSOME ,"+
            "to_char(dateadded, 'fmYYYY-MM-DD') as dateadded\n"+
            "from refsequence where sequenceid="+id;
        }
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        try
        {
            while(rs.next())
            {
                if ( generalinfo && first_round )
                {
                    first_round = false;
                    m_species_id = rs.getInt("GENUSSPECIES");
                    m_dateadded = rs.getString("DATEADDED");
                    m_start = rs.getInt("CDSSTART");
                    m_stop = rs.getInt("CDSSTOP");
                    m_gc_content = rs.getInt("GCCONTENT");
                    m_cdnasource = rs.getString("CDNASOURCE");
                    m_chromosome = rs.getString("CHROMOSOME");
                }            
                // public info stuff
                if ( isIncludePublicInfo)
                {
                   
                   m_publicInfo.add(new PublicInfoItem(rs.getString("nametype"),
                                                               rs.getString("namevalue"),
                                                               rs.getString("nameurl"),
                                                               rs.getString("description")));
                  
                }
            }

        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
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
            "values ("+m_id+ ","+m_species_id + ","  +m_start+","+m_stop+"," 
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
    public int getSpecies()    {    return m_species_id;}
    
    public String getDateadded()    {    return m_dateadded;}
    public String getFastaHeader()    {    return new String(">"+ super.getId());}
    public ArrayList getPublicInfo()    {    return m_publicInfo;}
    public int    getCdsStart(){ return m_start;}
    public int    getCdsStop(){ return m_stop;}
    public int    getGCcontent(){ return m_gc_content;}
    public String getCdnaSource()    {    return m_cdnasource;}
    public String getChromosome()    {    return m_chromosome ;}
    public String getCodingSequence()    {        return getText().substring(m_start-1,m_stop);    }
    public String getSpeciesName()      { return i_species_name;}
    
    public void     setSpecies(int v)    {     m_species_id = v;}
    public void     setSpeciesName(String v){ i_species_name = v;}
    public void     setDateadded(String v)    {     m_dateadded= v;}
  
    public void     setPublicInfo(ArrayList v)    {     m_publicInfo= v;}
    public void     setCdsStart(int v){  m_start = v;}
    public void     setCdsStop(int v){  m_stop= v;}
    public void     setGCcontent(int v){  m_gc_content= v;}
    public void     setCdnaSource(String s)    {     m_cdnasource = s;}
    public void     setChromosome(String s)    {     m_chromosome = s ;}
    
    
     public void     addPublicInfo(PublicInfoItem v)   
     { 
         if ( m_publicInfo == null)   m_publicInfo = new ArrayList();
         m_publicInfo.add( v);
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
    public String getPublicInfoParameter(String param_name)
    {
        String value = null;
        param_name = param_name.toUpperCase();
         for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            PublicInfoItem info_item = (PublicInfoItem)m_publicInfo.get(elm);
            if( info_item.getName().equals(param_name))
            {
                return info_item.getValue() ;
               
            }
        }
        return "";
    }
    
    public ArrayList getPublicInfoParametersNotIncludedInList(ArrayList param_names)
    {
        ArrayList names = new ArrayList();
        for (int elm = 0; elm < m_publicInfo.size(); elm++)
        {
            PublicInfoItem info_item = (PublicInfoItem)m_publicInfo.get(elm);
            if( !param_names.contains( info_item.getName() ))
            {
                names.add( info_item.getName() +": "+ info_item.getValue()) ;
            }
        }
        return names;
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
     * Restore the data from database by sequence id.
     *
     * @param id The sequence id.
     * @exception BecDatabaseException.
     */
    public void restore(int id, boolean isIncludePublicInfo) throws BecDatabaseException
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
           
                m_species_id = rs.getInt("GENUSSPECIES");
                m_dateadded = rs.getString("DATEADDED");
                m_start = rs.getInt("CDSSTART");
                m_stop = rs.getInt("CDSSTOP");
                m_gc_content = rs.getInt("GCCONTENT");
                m_cdnasource = rs.getString("CDNASOURCE");
                m_chromosome = rs.getString("CHROMOSOME");
            }
            
            // public info stuff
            if ( isIncludePublicInfo)
            {
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
            }

        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+id+"\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
  
    
    public static RefSequence findSequenceByTheoreticalInfo(String info_type, String info_value, boolean isIncludePublicInfo)
    {
        RefSequence seq = null;
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        
        try
        {
            t = DatabaseTransaction.getInstance();
            
            String sql = "select sequenceid from name where nametype='"+info_type+"' and namevalue='"+info_value+"'";
            crs = t.executeQuery(sql);
            
            if(crs.next())
            {
                int id = crs.getInt("SEQUENCEID");
                seq = new RefSequence(id, isIncludePublicInfo);
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
     public static int getCdsLength(int id)throws BecDatabaseException
     {
       String sql =" select CDSSTART  ,CDSSTOP from  REFSEQUENCE where sequenceid = " +id;
       DatabaseTransaction t = DatabaseTransaction.getInstance();
       ResultSet rs = null;int result = 0;
       try
        {
            rs = t.executeQuery(sql);
            if(rs.next())
            {
                result = rs.getInt("CDSSTOP") - rs.getInt("CDSSTART");
            }
            return result;
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while restoring sequence with id "+id+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
      
     }
     
    public static RefSequence getByCloneId(int cloneid, boolean isIncludePublicInfo)throws Exception
    {
        String sql="select  refsequenceid as sequenceid from sequencingconstruct where constructid in "
+" (select constructid from isolatetracking where isolatetrackingid in "
+" (select isolatetrackingid from flexinfo where flexcloneid = "+cloneid+"))";
        return getByRule(sql,isIncludePublicInfo);
    }
    
    
    public static int           getLengthById(int id)throws Exception
    {
        String sql = "select CDSSTART  ,CDSSTOP  from refsequence where sequenceid="+id;
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
       
        
        try
        {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next())
            {
                return  crs.getInt("CDSSTOP")-crs.getInt("CDSSTART");
            }
            else throw new Exception ("Cannot get Reference sequence length by id :"+id);
        } 
        catch (Exception e)
        {
            throw new Exception ("Cannot get Reference sequence length by id :"+id);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
  
    }
    //--------------------------------------
    
    private static RefSequence getByRule(String sql, boolean isIncludePublicInfo)throws Exception
    {
        RefSequence seq = null;
        DatabaseTransaction t = null;
        CachedRowSet crs = null;
        
        try
        {
            t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if(crs.next())
            {
                int id = crs.getInt("SEQUENCEID");
                seq = new RefSequence(id, isIncludePublicInfo);
            }
            return seq;
        } 
        catch (Exception e)
        {
            throw new Exception ("Cannot get Reference sequence by rule :"+sql);
        } 
        finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        
    }
    //__________________________________________________________________________
    
    public static void main(String [] args)
    {
        try
        {
            //DatabaseTransaction t = DatabaseTransaction.getInstance();
            RefSequence theoretical_sequence = RefSequence.findSequenceByTheoreticalInfo("SGD","YLR048W",false);
          //  int refseqid = theoretical_sequence.getId();
       
         //   String query="ATGGAGCTACGTGTGGGGAACAAGTACCGCCTGGGACGGAAGATCGGGAGCGGGTCCTTCGGAGATATCTACCTGGGTGCCAACATCGCCTCTGGTGAGGAAGTCGCCATCAAGCTGGAGTGTGTGAAGACAAAGCACCCCCAGCTGCACATCGAGAGCAAGTTCTACAAGATGATGCAGGGTGGCGTGGGGATCCCGTCCATCAAGTGGTGCGGAGCTGAGGGCGACTACAACGTGATGGTCATGGAGCTGCTGGGGCCTAGCCTCGAGGACCTGTTCAACTTCTGTTCCCGCAAATTCAGCCTCAAGACGGTGCTGCTCTTGGCCGACCAGATGATCAGCCGCATCGAGTATATCCACTCCAAGAACTTCATCCACCGGGACGTCAAGCCCGACAACTTCCTCATGGGGCTGGGGAAGAAGGGCAACCTGGTCTACATCATCGACTTCGGCCTGGCCAAGAAGTACCGGGACGCCCGCACCCACCAGCACATTCCCTACCGGGAAAACAAGAACCTGACCGGCACGGCCCGCTACGCTTCCATCAACACGCACCTGGGCATTGAGCAAAGCCGTCGAGATGACCTGGAGAGCCTGGGCTACGTGCTCATGTACTTCAACCTGGGCTCCCTGCCCTGGCAGGGGCTCAAAGCAGCCACCAAGCGCCAGAAGTATGAACGGATCAGCGAGAAGAAGATGTCAACGCCCATCGAGGTCCTCTGCAAAGGCTATCCCTCCGAATTCTCAACATACCTCAACTTCTGCCGCTCCCTGCGGTTTGACGACAAGCCCGACTACTCTTACCTACGTCAGCTCTTCCGCAACCTCTTCCACCGGCAGGGCTTCTCCTATGACTACGTCTTTGACTGGAACATGCTGAAATTCGGTGCAGCCCGGAATCCCGAGGATGTGGACCGGGAGCGGCGAGAACACGAACGCGAGGAGAGGATGGGGCAGCTACGGGGGTCCGCGACCCGAGCCCTGCCCCCTGGCCCACCCACGGGGGCCACTGCCAACCGGCTCCGCAGTGCCGCCGAGCCCGTGGCTTCCACGCCAGCCTCCCGCATCCAGCCGGCTGGCAATACTTCTCCCAGAGCGATCTCGCGGGTCGACCGGGAGAGGAAGGTGAGTATGAGGCTGCACAGGGGTGCGCCCGCCAACGTCTCCTCCTCAGACCTCACTGGGCGGCAAGAGGTCTCCCGGATCCCAGCCTCACAGACAAGTGTGCCATTTGACCATCTCGGGAAGTTGG";
            boolean isIncludePublicInfo = true;
            boolean sequencetext = true;
            boolean generalinfo = true;
            RefSequence fl8 =  new RefSequence(24);
            RefSequence fl =  new RefSequence(24 ,  true,   true, true);
            RefSequence fl1 =  new RefSequence(24 ,  true,   false, true);
            RefSequence fl2 =  new RefSequence(24 ,  true,   false, false);
            RefSequence fl3 =  new RefSequence(24 ,  false,   false, false);
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
