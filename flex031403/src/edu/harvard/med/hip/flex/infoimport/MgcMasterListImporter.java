/*
 * MgcMasterListImporter.java
 *
 * This class imports the Mgc master list into the FLEXGene database.
 * The file contains the following information per mgc clone:
 *	-	mgc_id
 *	-	image_id
 *	-	vector
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
import edu.harvard.med.hip.flex.user.*;

public class MgcMasterListImporter
{
    
    
    private static final String DILIM = "!\t";
    private static final String STATUS = FlexSequence.NEW;
    
    
    private int m_successCount = 0;
    private int m_failCount = 0;
    private int m_totalCount = 0;
    private String m_username = null;
    
    private Vector errors_to_print = null;
    private Vector log = null;
    
    /** Creates a new instance of MgcMasterListImporter */
    public MgcMasterListImporter(String username)
    {
        m_username = username;
        errors_to_print = new Vector();
        log = new Vector();
    }
    /** Creates a new instance of MgcMasterListImporter */
    public MgcMasterListImporter()
    {
        errors_to_print = new Vector();
        log = new Vector();
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
        Hashtable existingClones = new Hashtable();
        System.out.println(System.currentTimeMillis());
        boolean res = true;
        res =  getExistingClonesFromDB(existingClones);
        if (res) res = readCloneInfo(  input,  fileName, containerCol, existingClones) ;
        if (res) res = readSeqences(containerCol, sequenceCol) ;
        if (res) res = uploadToDatabase(containerCol, sequenceCol) ;
        if (m_username != null)
        {
            try
            {
                Mailer.notifyUser(m_username, "report.txt", "Report for error on master mgc list upload","Report for error on master mgc list upload",  errors_to_print);
               Mailer.notifyUser(m_username, "report_log.txt", "Report on master mgc list upload","Report for mgc list upload",  log);
              
                  
            }catch(Exception e){
            System.out.println(e.getMessage());}
        }
        System.out.println(System.currentTimeMillis());
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
    private boolean readCloneInfo(InputStream input, String fileName,  
                                    ArrayList containerCol, Hashtable existingClones)
    {
        int prev_mgc_containers = 0;//how many mgccontainers exist in DB
        
        String line = null;
        String last_container = "";
        String current_container = "";
        MgcContainer cont = null;
        int seq_id = -1;
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
                
                //check if this mgc clone info exist in db for the same clone
                seq_id = -1;
                if ( existingClones.containsKey(info [1] + "|" +  current_container )    )
                {
                    MinClone mc = (MinClone)existingClones.get(info [1] + "|" +  current_container );
                    
                    if (   mc.isEqual(info[11], info[12])  )
                        continue;
                    else// case that clone came on different plate
                        seq_id = mc.getSequenceId();
                }
                
                if ( !last_container.equals(current_container))
                {
                    
                    cont = new MgcContainer( -1,fileName, new Location(Location.FREEZER) ,
                    current_container,
                    MgcContainer.getLabel(prev_mgc_containers + current_container_number ),
                    info[9]);
                    log.add("Get from import file mgc container |"+current_container);
                    containerCol.add(cont);
                    current_container_number++;
                    last_container = current_container;
                    
                }
                
                
                MgcSample clone = new MgcSample( -1,  -1,
                                        Integer.parseInt(info [1]), Integer.parseInt(info[0]),
                                        info[8], info[11], Integer.parseInt(info[12]),
                                        MgcSample.STATUS_AVAILABLE);
                log.add("Get from import file mgc clone |"+info [1]);
                if (seq_id != -1) clone.setSequenceId(seq_id);
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
    
    private boolean getExistingClonesFromDB(Hashtable existingClones)
    {
        String sql = "select mc.mgcid as id, mc.sequenceid as seq, mc.orgrow as arow, mc.orgcol as acol, "+
                      " mcont.oricontainer as cont from mgcclone mc, mgccontainer mcont, sample s, containerheader h where "+
                        "(s.containerid = h.CONTAINERID and mcont.mgccontainerid = h.containerid and "+
                        "mc.mgccloneid=s.sampleid )";
        
        CachedRowSet crs = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            while(crs.next())
            {
                MinClone mc = new MinClone(crs.getInt("id"), 
                                            crs.getString("cont"),
                                           crs.getString("aROW"),
                                           crs.getInt("aCOL"),
                                           crs.getInt("SEQ"));
                existingClones.put(mc.getKey(), mc);
            }
            return true;
        }
        catch (Exception e)
        {
            errors_to_print.add("Can not open database for extracting existing clones.\n" );
            return false;
        }
        
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
    private boolean readSeqences(ArrayList containerCol, Hashtable sequenceCol)
    {
        GenbankGeneFinder gb = new GenbankGeneFinder();
        Vector genBankSeq = new Vector();
        Hashtable seqData = new Hashtable();
        int current_key ;
        String current_gi = null;
        
        MgcSample sample = null;
        FlexSequence fs = null;
        
        for (int container_count = 0; container_count < containerCol.size(); container_count++)
        {
            Vector sampl = ((MgcContainer)containerCol.get(container_count)).getSamples();
            for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)//sample count
            {
                sample = (MgcSample)sampl.get(sample_count);
                if (sample.getSequenceId() == -1)
                {
                    current_key = ((MgcSample)sampl.get(sample_count)).getMgcId();
                    fs = null;
                    try
                    {
                        genBankSeq = gb.search("\"MGC:" + current_key +"\"");
                        if (genBankSeq.isEmpty() ) continue;
                        for (int countGS = 0; countGS < genBankSeq.size(); countGS++)
                        {// human and not human have different format, human can come not first
                            current_gi = ((GenbankSequence)genBankSeq.get(countGS)).getGi();
                            if (current_gi == null || current_gi.equals("") ) continue;
                            seqData = gb.searchDetail(current_gi);
                            if (((String)seqData.get("species")).indexOf("sapiens") != -1)
                            {
                                fs = createFlexSequence( seqData, genBankSeq);
                                continue;
                            }
                       }
                         
                        
                        if (fs == null)//can not create sequence
                        {
                            errors_to_print.add("Can not find sequence for MGC : " + current_key);
                            continue;
                        }
                        sequenceCol.put( Integer.toString(current_key), fs );
                        log.add("Get from ncbi sequence for mgc clone |"+current_key  );
                        current_gi = null;
                       

                    }catch(Exception e)
                { errors_to_print.add("Can not find sequence for MGC : " + current_key + "\n");  }
                
            }
            }//end sample count
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
        if (seqText == null || seqText.equals("") ) return null;
        
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
        String current_label = null;
        
        try
        {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex)
        {
            errors_to_print.add("Master List Import: Can not open connection to database.\n");  }
        
        for (int container_count = 0; container_count < containerCol.size(); container_count++)
        {
            try
            {
                MgcContainer cont = (MgcContainer)containerCol.get(container_count);
                Vector sampl = cont.getSamples();
                current_label = cont.getLabel();
                
                for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)
                {
                    current_clone = (MgcSample)sampl.get(sample_count);
                    if (current_clone.getSequenceId() == -1)
                    {
                        current_key = Integer.toString(current_clone.getMgcId());

                        FlexSequence fs = (FlexSequence)sequenceCol.get( current_key);
                        if (fs != null && !fs.getQuality().equals(FlexSequence.QUESTIONABLE)  )
                        {

                            fs.insert(conn);
                             
                            fs_key = fs.getId();
                            log.add("Insert into db sequence for mgc clone |"+fs_key  );
                            current_clone.setSequenceId(fs_key);
                        }
                        if (fs == null) current_clone.setStatus(MgcSample.STATUS_NO_SEQUENCE);
                        if (fs != null && fs.getQuality().equals(FlexSequence.QUESTIONABLE ) ) current_clone.setStatus(MgcSample.STATUS_BAD_SEQUENCE);
                    }
                    
                }//end loop clone
                cont.insert(conn);
                log.add("Insert into db mgc container |"+cont.getLabel()  );
                //commit_count++;
                //if ( (commit_count % 10) == 0 ) DatabaseTransaction.commit(conn);
                DatabaseTransaction.commit(conn);
                
            } catch (FlexDatabaseException ex1)
            {
                errors_to_print.add("Can not commit MGC container to database: " + current_label +"\n");
                DatabaseTransaction.rollback(conn);
            }
            catch (Exception ex)
            {
                errors_to_print.add("Can not commit MGC container to database: " + current_label + "\n");
                DatabaseTransaction.rollback(conn);
            }
        }//end loop container_count
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
    
   
    
    class MinClone
    {
        int m_mgcid = -1;
        String m_container_name = null;
        String m_row = null;
        int m_col = -1;
        int m_seq_id = -1;
        
        public MinClone(int id, String cont, String row, int col, int seq_id)
        {
            m_mgcid = id;
            m_container_name = cont;
            m_row = row;
            m_col = col;
            m_seq_id = seq_id;
        }
        
        public boolean isEqual(  String row, String col)
        {
            if (m_row.equalsIgnoreCase(row) &&  m_col == Integer.parseInt(col))
                return true;
            else
                return false;
        }
        public String           getKey(){ return m_mgcid+"|"+m_container_name; }
        public int getMgcId(){ return m_mgcid;}
        public String getMgcIdString(){ return Integer.toString(m_mgcid);}
       // public int getCol(){ return m_col;}
        //public String getRow(){ return m_row;}
        
        public int getSequenceId() { return m_seq_id;}
    }
    //****************************Testing*******************************
    
    public static void main(String args[])
    {
        
        String file = "c:\\mgc_plate_info1.txt";
        InputStream input;
        
        try
        {
            input = new FileInputStream(file);
            
        } catch (FileNotFoundException ex)
        {
            System.out.println(ex);
            return;
        }
        
        MgcMasterListImporter importer = new MgcMasterListImporter("dzuo");
        importer.importMgcCloneInfoIntoDB(input, file) ;
        System.exit(0);
    }
    
    
    
}
   
