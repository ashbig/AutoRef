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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;

public class MgcMasterListImporter {
    
   
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
    public boolean importMgcCloneInfoIntoDB(InputStream input, String fileName,  Connection conn)
    {
        Hashtable sequenceCol = new Hashtable();
        ArrayList containerCol = new ArrayList();
        if (! readCloneInfo(  input,  fileName, containerCol) ) return false;
        if (! readSeqences(containerCol, sequenceCol) ) return false;
        if (! uploadToDatabase(containerCol, sequenceCol, conn) ) return false;
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
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        try
        {
             prev_mgc_containers = FlexIDGenerator.getCount("mgccontainer");
        }
        catch(Exception e) {return false;}
        
        try {
            
            while((line = in.readLine()) != null) 
            {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String [] info = new String[12];                
                
                m_totalCount++;
                try 
                {
                    while(st.hasMoreTokens())
                    {
                        info[i] = st.nextToken();
                        i++;
                    }
                } 
                
                catch(Exception e){m_failCount++;}
                
                //look only for human
                if ( !info[7].equals("human") ) continue;
                
                //if container the same - create new MgcClone 
                //************************** change
                current_container = info[9] + info[10];
                if ( !last_container.equals(current_container))
                {
                    cont = new MgcContainer( -1,fileName, new Location(Location.FREEZER) ,
                                                         current_container, 
                                                         MgcContainer.getLabel(prev_mgc_containers +  m_successCount + 1));
                    containerCol.add(cont);
                    last_container = current_container;
                   
                }

                   
                MgcSample clone = new MgcSample( -1, "", -1,  
                                                Integer.parseInt(info [1]), Integer.parseInt(info[0]), 
                                                info[8], info[11], Integer.parseInt(info[12]) );
                
                cont.addSample(clone);
                m_successCount++;
            }
            
            return true;
        }catch (Exception ex) {            return false;        }
    }
    
    /**
     * Function loops through all  mgc clones quering ncbi for their sequences.
     *@param cloneCol - hashtable of all clones from master list 
     *@param sequenceCol - hashtable of sequences that will be filled by method
     *
     *@return true - no exception was raised, false otherwise
     
     */
    private boolean readSeqences(ArrayList containerCol, Hashtable sequenceCol)
    {
       GenbankGeneFinder gb = new GenbankGeneFinder();
       Vector genBankSeq = null;
       Hashtable seqData = null;
        int current_key ;
        String current_gi = null;
        
        for (int container_count = 0; container_count < containerCol.size(); container_count++)
        {
           Vector sampl = ((MgcContainer)containerCol.get(container_count)).getSamples();
           for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)
           {
             current_key = ((MgcSample)sampl.get(sample_count)).getMgcId();

             try
            {
                genBankSeq = gb.search("MGC:" + current_key);
                current_gi = ((GenbankSequence)genBankSeq.get(0)).getGi();
                seqData = gb.searchDetail(current_gi);
                FlexSequence fs = createFlexSequence( seqData, genBankSeq);
                sequenceCol.put( Integer.toString(current_key), fs );
             } 
             catch(Exception e) { return false; }
      
       }
        }//end loop container_count
       return true;
     
    }
        
    
    /** Creat flexsequence object from data collected from ncbi
     * @param seqData - hashtable of  "species" => organism;
                                       "start" => start codon 
                                       "stop" => stop codon
                                       "sequencetext" => sequencetex
                                        "locus_link", =>locus_link_id
                                        "gene_name" =>  gene_symbol
     *@param        genBankData vector of GenebankSeq objects (gi, gb_accession, description
     *returns flexseq object
     */
        private FlexSequence createFlexSequence(Hashtable seqData, Vector genBankData)
        {
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
    
              gccont =  (int) gc_content((String)seqData.get("sequencetext"));
              FlexSequence seq = new FlexSequence(-1, FlexSequence.NEW, 
                            "Homo sapiens", null, 
                            (String)seqData.get("sequencetext"), start, stop,
                             cdsLength, gccont, publicInfo);
              seq.setQuality( seqQuality);
              return seq;
       }
    
        //calculates gc_content for the sequence shold be some other place
    private double gc_content(String seq)
    {             
          int i=0  ;
          seq = seq.toLowerCase();
          char seq_char[] = seq.toCharArray();
          for (int j=0;j<seq_char.length;j++){          
            if (seq_char[j]=='g' || seq_char[j]=='c'){         
              i++;                                 
            }                                     
                  }                                       
          return (i*100/seq_char.length);                  
    }   
    
    
   
    /**Load sequence, mgc container and mgc sample information into database
     */
    private boolean uploadToDatabase( ArrayList containerCol, Hashtable sequenceCol, Connection conn)
    {
        String current_key ;
        int fs_key; 
        MgcSample current_clone = null;
        try{
            for (int container_count = 0; container_count < containerCol.size(); container_count++)
            {

               MgcContainer cont = (MgcContainer)containerCol.get(container_count);
               Vector sampl = cont.getSamples();
           
               for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)
               {
                 current_clone = (MgcSample)sampl.get(sample_count);
                 current_key = Integer.toString(current_clone.getMgcId());
                 FlexSequence fs = (FlexSequence)sequenceCol.get( current_key);
                 if (fs != null) 
                {
                    fs_key = FlexIDGenerator.getID("sequenceid");
                    current_clone.setSequenceId(fs_key);
                    fs.insert(conn);
                 }

               }//end loop clone 
               cont.insert(conn);
            }//end loop container_count
        }catch(Exception e){return false;}
       return true;
    }
    public int getFailedCount(){ return m_failCount ;}
    public int getTotalCount(){ return m_totalCount; }
    public int getSuccessCount(){ return m_successCount ;}
    
    
    //****************************Testing*******************************
    
    public static void main(String args[]) {
        String file = "C:\\mgc_plate_info.txt";
        InputStream input;
        
        try {
            input = new FileInputStream(file);
           
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return;
        }
        
     
        
        MgcMasterListImporter importer = new MgcMasterListImporter();
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            if(importer.importMgcCloneInfoIntoDB(input, file, conn)) {
                DatabaseTransaction.commit(conn);
               
            } else {
                DatabaseTransaction.rollback(conn);
                System.out.println("Import aborted.");
            }
        } catch (FlexDatabaseException ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
    
    
}
