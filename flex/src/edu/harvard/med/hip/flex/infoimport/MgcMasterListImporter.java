/*
 * MgcMasterListImporter.java
 *
 * This class imports the Mgc master list into the FLEXGene database.
 * The file contains the following information per mgc clone:
 *	-	mgc_id
 *	-	image_id
 *	-	vector
 *	-	marker
 *	-	plate_id
 *	-	row_id
 *	-	column_id
 *
 * Created on May 6, 2002, htaycher
 * for each plate: create new container;
 * import mgc clone data into DB
 */

package edu.harvard.med.hip.flex.infoimport;


import java.io.*;
import java.util.*;
import java.sql.*;
import sun.jdbc.rowset.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;

public class MgcMasterListImporter
{
    
    
    private static final String DILIM = "!\t";
    private static final String STATUS = FlexSequence.NEW;
    
    
    private int m_successCount = 0;
    private int m_failCount = 0;
    private int m_totalCount = 0;
    
    /** Creates a new instance of MgcMasterListImporter */
    public MgcMasterListImporter()
    {
        
    }
    
    
    /**
     *Read mgc master list and collect mgc clone, mgc container, mgc clone seq info
     *upload this information to the database
     *main function for the class
     *@param master list file name
     */
    public boolean importMgcCloneInfoIntoDB(InputStream input, String fileName)
    {
        Hashtable sequenceCol = new Hashtable();
        ArrayList containerCol = new ArrayList();
        Hashtable existingSeqCol = new Hashtable();
        if (! readCloneInfo(  input,  fileName, containerCol) ) return false;
        if (! checkForMgcIDMatches(existingSeqCol) ) return false;
        if (! readSeqences(containerCol, sequenceCol, existingSeqCol) ) return false;
        if (! uploadToDatabase(containerCol, sequenceCol) ) return false;
        return true;
        
    }
    /**
     * Read the mgc master list  information from the input stream.
     * The input is in the following format:
     *IMAGE_cloneID:MGC_ID:source_collection:source_plate:source_row:source_column
     *      0      :   1  :      2          :     3      :     4    :   5
     *libr_id:	species:vector:	rearray_collection:rearray_plate:rearray_row:rearray_column
     *    6   :    7   :   8  :       9           :     10      :     11    :    12
     *
     * @param input The InputStream object containing the mgc clone information.
     * @param cloneCol - hashtable (key mgcid) of all mgc clones read from the file
     * @ param containerCol - collection of mgcContainer objects that hold these mgc clones.
     * @return true if successful; false otherwise.
     */
    private boolean readCloneInfo(InputStream input, String fileName,  ArrayList containerCol)
    {
        int prev_mgc_containers = 0;//how many mgccontainers exist in DB
        
        String line = null;
        String last_container = "";
        String current_container = "";
        MgcContainer cont = null;
        int i = 0;
        int current_container_number = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        try
        {
            prev_mgc_containers = FlexIDGenerator.getCount("mgccontainer");
        }
        catch(Exception e)
        {return false;}
        
        try
        {
            line = in.readLine();
            while((line = in.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String [] info = new String[13];
                i = 0;
                
                m_totalCount++;
                try
                {
                    while(st.hasMoreTokens())
                    {
                        info[i] = st.nextToken();
                        i++;
                    }
                }
                
                catch(Exception e)
                {m_failCount++; continue;}
                
                //look only for human
                if ( !info[7].equals("human") ) continue;
                
                //if container the same - create new MgcClone
                //************************** change
                current_container = info[9] + info[10];
                if ( !last_container.equals(current_container))
                {
                    
                    cont = new MgcContainer( -1,fileName, new Location(Location.FREEZER) ,
                    current_container,
                    MgcContainer.getLabel(prev_mgc_containers +  current_container_number++));
                    containerCol.add(cont);
                    last_container = current_container;
                    
                }
                
                
                MgcSample clone = new MgcSample( -1,  -1,
                Integer.parseInt(info [1]), Integer.parseInt(info[0]),
                info[8], info[11], Integer.parseInt(info[12]),
                MgcSample.STATUS_AVAILABLE);
                
                cont.addSample(clone);
                m_successCount++;
            }
            input.close();
            return true;
        }catch (Exception ex)
        {    try
             {input.close(); }catch(Exception e)
             {
                 System.out.println(e.getMessage() );
                 return false;}
             return false;        }
    }
    
    /**
     *Function checks for duplication of MgcID to prevent inserting duplicated sequences into DB
     */
    
    private boolean checkForMgcIDMatches(Hashtable existingSeqCol)
    {
        String sql = "select mgcid, sequenceid from mgcclone";
        int mgc_id = 0;
        int seq_id = 0;
        CachedRowSet crs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
                existingSeqCol.put(new Integer(crs.getInt("mgcid")), new Integer(crs.getInt("sequenceid")));
            }
            return true;
        }
        catch (Exception e)
        {return false;}
        
    }
    
    /**
     * Function loops through all  mgc clones,
     * checks if mgc_id already exists in db if not quering ncbi for their sequences.
     *@param cloneCol - hashtable of all clones from master list
     *@param sequenceCol - hashtable of sequences that will be filled by method
     *@param existingSeqCol - hashtable of mgc_id (and sequence _id ) from database
     *
     *
     *@return true - no exception was raised, false otherwise
     *
     */
    private boolean readSeqences(ArrayList containerCol, Hashtable sequenceCol, Hashtable existingSeqCol)
    {
        GenbankGeneFinder gb = new GenbankGeneFinder();
        Vector genBankSeq = new Vector();
        Hashtable seqData = new Hashtable();
        int current_key ;
        String current_gi = null;
        int duplicate_id = 0;
        
        for (int container_count = 0; container_count < containerCol.size(); container_count++)
        {
            Vector sampl = ((MgcContainer)containerCol.get(container_count)).getSamples();
            for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)
            {
                current_key = ((MgcSample)sampl.get(sample_count)).getMgcId();
                try
                {
                    if ( existingSeqCol.isEmpty()  || ! existingSeqCol.contains(new Integer(current_key)) )
                    {
                        
                        genBankSeq = gb.search("\"MGC:" + current_key +"\"");
                        if (genBankSeq.isEmpty() ) continue;
                        current_gi = ((GenbankSequence)genBankSeq.get(0)).getGi();
                        if (current_gi == null || current_gi.equals("") ) continue;
                        seqData = gb.searchDetail(current_gi);
                        FlexSequence fs = createFlexSequence( seqData, genBankSeq);
                        sequenceCol.put( Integer.toString(current_key), fs );
                        current_gi = null;
                    }
                    
                    else
                    {
                        duplicate_id = ((Integer)existingSeqCol.get( new Integer(current_key))).intValue();
                        ((MgcSample)sampl.get(sample_count)).setSequenceId( duplicate_id );
                    }
                }catch(Exception e)
                {return false;  }
                
            }
        }//end loop container_count
        return true;
        
    }
    
    
    /** Creat flexsequence object from data collected from ncbi
     * @param seqData - hashtable of  "species" => organism;
     * "start" => start codon
     * "stop" => stop codon
     * "sequencetext" => sequencetex
     * "locus_link", =>locus_link_id
     * "gene_name" =>  gene_symbol
     *@param        genBankData vector of GenebankSeq objects (gi, gb_accession, description
     *returns flexseq object
     */
    public FlexSequence createFlexSequence(Hashtable seqData, Vector genBankData)
    {
        //can get empty seqData filled by default values
        String seqText = (String)seqData.get("sequencetext");
        if (seqText == null || seqText.equals("")) return null;
        
        int start = ((Integer)seqData.get("start")).intValue();
        int stop = ((Integer)seqData.get("stop")).intValue();
        String seqQuality;
        int cdsLength; int gccont = 0;
        Vector publicInfo = new Vector();
        
        if(start==-1 || stop == -1)
        {
            cdsLength = 0;
            seqQuality = FlexSequence.QUESTIONABLE;
        }
        else
        {
            seqQuality = FlexSequence.GOOD;
            cdsLength = stop -start +1;
        }
        
        
        Hashtable pubinfo_entry_gi = new Hashtable();
        pubinfo_entry_gi.put(FlexSequence.NAMETYPE,FlexSequence.GI);
        pubinfo_entry_gi.put(FlexSequence.NAMEVALUE,((GenbankSequence)genBankData.get(0)).getGi() );
        pubinfo_entry_gi.put(FlexSequence.DESCRIPTION,((GenbankSequence)genBankData.get(0)).getDescription() );
        publicInfo.add(pubinfo_entry_gi);
        
       
        Hashtable pubinfo_entry_gb = new Hashtable();
        pubinfo_entry_gb.put(FlexSequence.NAMETYPE,FlexSequence.GENBANK_ACCESSION);
        pubinfo_entry_gb.put(FlexSequence.NAMEVALUE,((GenbankSequence)genBankData.get(0)).getAccession() );
        publicInfo.add(pubinfo_entry_gb);
       
        if (seqData.containsKey("gene_name") )
        {
            Hashtable pubinfo_entry_gene_name = new Hashtable();
            pubinfo_entry_gene_name.put(FlexSequence.NAMETYPE,"GENE_SYMBOL");
            pubinfo_entry_gene_name.put(FlexSequence.NAMEVALUE,seqData.get("gene_name" ) );
            publicInfo.add(pubinfo_entry_gene_name);
        }
        
        if (seqData.containsKey("locus_link") )
        {
            Hashtable pubinfo_entry_locus_link = new Hashtable();
            pubinfo_entry_locus_link.put(FlexSequence.NAMETYPE,"LOCUS_ID");
            pubinfo_entry_locus_link.put(FlexSequence.NAMEVALUE,seqData.get("locus_link" ) );
            pubinfo_entry_locus_link.put(FlexSequence.NAMEURL,"http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=" + seqData.get("locus_link" ) );
            publicInfo.add(pubinfo_entry_locus_link);
        }
        
        gccont =  (int) gc_content(seqText);
        FlexSequence seq = new FlexSequence(-1, FlexSequence.NEW,
        "Homo sapiens", null, seqText, start, stop,
        cdsLength, gccont, publicInfo);
        seq.setQuality(seqQuality);
        return seq;
    }
    
    //calculates gc_content for the sequence shold be some other place
    private double gc_content(String seq)
    {
        int i=0  ;
        seq = seq.toLowerCase();
        char seq_char[] = seq.toCharArray();
        for (int j=0;j<seq_char.length;j++)
        {
            if (seq_char[j]=='g' || seq_char[j]=='c')
            {
                i++;
            }
        }
        return (i*100/seq_char.length);
    }
    
    
    
    /**Load sequence, mgc container and mgc sample information into database
     */
    private boolean uploadToDatabase( ArrayList containerCol, Hashtable sequenceCol)
    {
        String current_key ;
        int fs_key;
        MgcSample current_clone = null;
        DatabaseTransaction t = null;
        Connection conn = null;
        int commit_count = 0;
        
        try
        {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            for (int container_count = 0; container_count < containerCol.size(); container_count++)
            {
                
                MgcContainer cont = (MgcContainer)containerCol.get(container_count);
                Vector sampl = cont.getSamples();
                
                for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)
                {
                    current_clone = (MgcSample)sampl.get(sample_count);
                    current_key = Integer.toString(current_clone.getMgcId());
                    FlexSequence fs = (FlexSequence)sequenceCol.get( current_key);
                    if (fs != null && !fs.getQuality().equals(FlexSequence.QUESTIONABLE)  )
                    {
                        
                        fs.insert(conn);
                        fs_key = fs.getId();
                        current_clone.setSequenceId(fs_key);
                    }
                    if (fs == null) current_clone.setStatus(MgcSample.STATUS_NO_SEQUENCE);
                    if (fs.getQuality().equals(FlexSequence.QUESTIONABLE ) ) current_clone.setStatus(MgcSample.STATUS_BAD_SEQUENCE);
                    
                }//end loop clone
                cont.insert(conn);
                commit_count++;
                //if ( (commit_count % 10) == 0 ) DatabaseTransaction.commit(conn);
               DatabaseTransaction.commit(conn);
            }//end loop container_count
        } catch (FlexDatabaseException ex1)
        {   DatabaseTransaction.rollback(conn);  }
        catch (Exception ex)
        {  DatabaseTransaction.rollback(conn);   }
        //DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
        return true;
    }
    
    public int getFailedCount()
    { return m_failCount ;}
    public int getTotalCount()
    { return m_totalCount; }
    public int getSuccessCount()
    { return m_successCount ;}
    
    
    //****************************Testing*******************************
    
    public static void main(String args[])
    {
        String file = "c:\\mgc_plate_info.txt";
        InputStream input;
        
        try
        {
            input = new FileInputStream(file);
            
        } catch (FileNotFoundException ex)
        {
            System.out.println(ex);
            return;
        }
        
        MgcMasterListImporter importer = new MgcMasterListImporter();
        importer.importMgcCloneInfoIntoDB(input, file) ;
        System.exit(0);
    }
    
    
}
