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
 *
 *
 *
 *CREATE SEQUENCE MGCCONTAINERLABEL
START with 197 INCREMENT by 1
 NOMAXVALUE
 NOMINVALUE
 NOCYCLE
 NOCACHE
 *
 *ALTER TABLE MGCCLONE 
 ADD (ORIENTATION INTEGER DEFAULT 0 )
 *
 *format descriptions
 *format_2
 *Accession(0)	CDS start(1)	CDS stop(2)	gi(3)	Gene	IMAGE	library ID	orientation	vector	Collection	Plate	Row	Column

 *format_0
 *
 *IMAGE_cloneID(0)	MGC_ID(1)	source_collection	source_plate	source_row	source_column	libr_id	species	vector	rearray_collection	rearray_plate	rearray_row	rearray_column

 *format_1
 *
 *IMAGE_ID(0)	COLLECTION_NAME(1)	PLATE(2)	ROW_POS(3)	COL_POS(4)	LIBR_NAME(5)	VECTOR_NAME(6)

 */

package edu.harvard.med.hip.flex.infoimport;


import java.io.*;
import java.util.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.workflow.*;

public class MgcMasterListImporter
{
    public static final int     FORMAT_0 = 0;
    public static final int     FORMAT_1 = 1;
     public static final int     FORMAT_2 = 2;
     
     
      public static final int     ORIENTATION_FORWARD = 1;
    public static final int     ORIENTATION_NOT_KNOWN = 0;
     public static final int     ORIENTATION_REVERSE = -1;
   
    
    private static final String DILIM = "!\t";
    private static final String STATUS = FlexSequence.NEW;
    //private static final String FILE_PATH = "/tmp/";
    public static final String FILE_PATH = FlexProperties.getInstance().getProperty("tmp");
    
    
    private String              m_username = null;
    private FileWriter          m_writer = null;
    
    private Vector errors_to_print = null;
    
    //change for your submission file
    private   static   final  int     SUBMISSION_FORMAT = FORMAT_2;
    private   static   final int      WELLS_ON_PLATE = 96;
    /** Creates a new instance of MgcMasterListImporter */
    public MgcMasterListImporter(String username)
    {
        m_username = username;
        errors_to_print = new Vector();
    }
    
    
    /** Creates a new instance of MgcMasterListImporter */
    public MgcMasterListImporter()
    {
        errors_to_print = new Vector();
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
        ArrayList mgc_containers = new ArrayList();
        MgcContainer container = null;
        //Hashtable existingClones = new Hashtable();
       // Hashtable existingCloneSettings = new Hashtable();
        System.out.println(System.currentTimeMillis());
        String report_file_name = FILE_PATH + "import_log"+ System.currentTimeMillis()+".txt";
        boolean res = true;
        writeToFile("Log file for MGC master list upload\n",  report_file_name);
      //  if (res) res =  getExistingClonesFromDB(existingClones,existingCloneSettings);
        System.out.println("mgc master list read");
        mgc_containers = readCloneInfo(  input,  fileName, report_file_name) ;
        //verify container duplicates, delete containers that already submitted 
    //    printContainers(mgc_containers);
        mgc_containers = verifyDuplicates(mgc_containers );
        if ( mgc_containers != null && mgc_containers.size() > 0)
        {
             //create connection
            DatabaseTransaction t = null;
            Connection conn = null;
            try
            {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
            } catch (Exception ex)
            {
                errors_to_print.add("Master List Import: Can not open connection to database.\n");
            }
      
            for (int count = 0 ; count < mgc_containers.size(); count++)
            {
                try
                {
                    container = (MgcContainer) mgc_containers.get(count);
                    container.setLabel( container.getLabel(FlexIDGenerator.getID("MGCCONTAINERLABEL")));
                    sequenceCol = new Hashtable();
                    updateSequenceIds(container);
                    System.out.println("mgc master list read finish");
          //       printContainers(mgc_containers);
                    readSeqences(container, sequenceCol, report_file_name) ;
                    System.out.println("mgc master list read sequences finished");
                    uploadToDatabase(conn, container, sequenceCol, report_file_name) ;
                }
                catch(Exception e)
                {
                    errors_to_print.add("Cannot submit container "+ container.getOriginalContainer());
                }
            }
            if (res) res = putOnQueue(conn, mgc_containers);
             DatabaseTransaction.closeConnection(conn);  
        }
        else
        {
            errors_to_print.add("No new plates will be submitted. COntainer duplicates.");
        }
       
        System.out.println("mgc master list upload to DB finished");
        if (m_username != null)
        {
            try
            {
                if ( errors_to_print.size() == 0)
                    errors_to_print.add( "Report: master mgc list upload.\n File Name "+fileName);
                else
                    errors_to_print.add( 1,"Report: master mgc list upload.\n File Name "+fileName);
                System.out.println("send message");
                Mailer.notifyUser(m_username, "report.txt", "Report: master mgc list upload","Report: master mgc list upload",  errors_to_print);
                // m_writer.flush(); m_writer.close();
                File m_log_file = new File(report_file_name);
                
                Mailer.sendMessageWithAttachedFile(m_username,  "Report on master mgc list upload","Report for mgc list upload",  m_log_file);
                m_log_file.delete();
                
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(System.currentTimeMillis());
        return true;
        
    }
    
    
    private void         printContainers(ArrayList mgc_containers)
    {
        MgcContainer container = null;MgcSample sample = null;
        for(int count = 0; count < mgc_containers.size(); count++)
        {
            container = (MgcContainer)mgc_containers.get(count);
            System.out.println(container.toString());
            
        }
    }
     
     
    /*
     *function verifyes that no container was submitted into db on previous rounds and assigned 
     *plate labels
     *? change when it will be over 1000 plates
     */
    
    private ArrayList verifyDuplicates(ArrayList containers)
    {
        ArrayList non_duplicate_containers = new ArrayList();
        MgcContainer container = null;ArrayList duplicate_descriptions = null;
        String  query_str="select oricontainer  from mgccontainer ";
        ArrayList container_labels = new ArrayList();
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(query_str);
                       
            while(crs.next()) 
            {
                container_labels.add( crs.getString("oricontainer"));
            }
        } catch (Exception sqlE) {
            errors_to_print.add("Error occured while extracting mgc container labels \nSQL: "+query_str);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        //verify containers
        for ( int count = 0; count < containers.size(); count++)
        {
            container = (MgcContainer)containers.get(count);
            if (!container_labels.contains(container.getOriginalContainer() ))
            {
                non_duplicate_containers.add(container);
                container_labels.add(container.getOriginalContainer());
            }
        }
        return non_duplicate_containers;
    }
     
    
    /* try to update sequenceid for the samples with mgcid/imageid that
     have been submitted already. return value (true) indicates that this container can have a duplicate
     */
    private void         updateSequenceIds(ArrayList containers) 
    {
        MgcContainer container = null;
        String query_str = null;
        for (int count = 0; count < containers.size(); count++)
        {
            container = (MgcContainer)containers.get(count);
            updateSequenceIds(container);
           
        }
        
    }
    
    private void         updateSequenceIds(MgcContainer container) 
    {
        String query_str = null;
       
        try
        {
            query_str = getQueryStringForSequenceIdUpdate(container);
            if (query_str == null) throw new FlexDatabaseException("Cannot build query string for sequenceid verification");
            Hashtable sequenceid_cloneid  = getSequenceIdRecords(query_str);
            updateSequenceIds(container,sequenceid_cloneid);
        }
        catch(Exception e)
        {
            errors_to_print.add(e.getMessage());
        }
       
    }
    
    private void     updateSequenceIds(MgcContainer container,Hashtable sequenceid_cloneid)
    {
        boolean result = true;
        MgcSample sample = null;
        int sequenceid = -1;
        Integer id = null;
        
        for (int count = 0; count < container.getSamples().size(); count++)
        {
            sample = (MgcSample) container.getSamples().get(count);
            switch(SUBMISSION_FORMAT)
            {
                //image id
                case FORMAT_0: 
                { 
                    id = (Integer)sequenceid_cloneid.get( new Integer(sample.getMgcId()));
                    break;
                }
                  //mgsid
                case FORMAT_1: case FORMAT_2:
                {
                    id = (Integer)sequenceid_cloneid.get( new Integer(sample.getImageId()));
                    break;
                }
               
            }
            sequenceid = id.intValue();
            if (sequenceid > 0 )
                sample.setSequenceId(sequenceid);
            
        }
      
    }
    private Hashtable    getSequenceIdRecords(String query_str)throws FlexDatabaseException
    {
        Hashtable htable = new Hashtable(WELLS_ON_PLATE);
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(query_str);
                       
            while(crs.next()) 
            {
                htable.put( new Integer(crs.getInt("ID")),new Integer(crs.getInt("sequenceid")));
            }
        } catch (Exception sqlE) {
            throw new FlexDatabaseException("Error occured while extracting mgcclone information \nSQL: "+query_str);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        return htable;
        
    }
     private String      getQueryStringForSequenceIdUpdate(MgcContainer container)
    {
        /*should take into account that 
         *one imageid can be sitting on different plates, but
         *all samples with one image/mgcid have the same sequenceid
         */
        String query_str  = null;
        String id_type = null;
        StringBuffer ids = new StringBuffer();
        MgcSample sample = null;
        for (int count = 0; count < container.getSamples().size(); count++)
        {
            sample = (MgcSample) container.getSamples().get(count);
            switch(SUBMISSION_FORMAT)
            {
                //image id
                case FORMAT_0: { ids.append( sample.getMgcId() ); break;}
                  //mgsid
                case FORMAT_1: case FORMAT_2: {ids.append( sample.getImageId() ) ;break;}
                default: return null;
            }
            if ( count != container.getSamples().size()-1) ids.append(",");
        }
        switch(SUBMISSION_FORMAT)
        {
            //image id
            case FORMAT_0: 
            {
                return "select   mgcid as id, sequenceid from mgcclone  where "
                + "mgcid in (" + ids +") order by mgcid";
            }
             //mgsid
            case FORMAT_1: case FORMAT_2:
            {
                return "select  imageid as id,  sequenceid from mgcclone  where "
                + " imageid in (" + ids +") order by imageid";
            }
        }
         return null;
    }
  
 
        
    /**
     *depricated
     *Read mgc master list and collect mgc clone, mgc container, mgc clone seq info
     *upload this information to the database
     *main function for the class
     *@param master list file name
     */
    /*
    public boolean importMgcCloneInfoIntoDB(InputStream input, String fileName)
    {
        Hashtable sequenceCol = new Hashtable();
        ArrayList containerCol = new ArrayList();
        Hashtable existingClones = new Hashtable();
        Hashtable existingCloneSettings = new Hashtable();
        System.out.println(System.currentTimeMillis());
        
        boolean res = true;
        writeToFile("Log file for MGC master list upload\n");
        if (res) res =  getExistingClonesFromDB(existingClones,existingCloneSettings);
        System.out.println("mgc master list read");
        if ( m_data_format == FORMAT_OLD)
        {
            if (res) res = readCloneInfo(  input,  fileName, containerCol, existingClones, existingCloneSettings) ;
        }
        else if ( m_data_format == FORMAT_NEW)
        {
             if (res) res = readCloneInfo_NF(  input,  fileName, containerCol, existingClones, existingCloneSettings) ;
        }
        System.out.println("mgc master list read finish");
        if (res) res = readSeqences(containerCol, sequenceCol) ;
        System.out.println("mgc master list read sequences finished");
    
            //create connection
        DatabaseTransaction t = null;
        Connection conn = null;
        try
        {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex)
        {
            errors_to_print.add("Master List Import: Can not open connection to database.\n");
        }
  
        
        if (res) res = uploadToDatabase(conn,containerCol, sequenceCol) ;
        if (res) res = putOnQueue(conn, containerCol);
        DatabaseTransaction.closeConnection(conn);
        System.out.println("mgc master list upload to DB finished");
        if (m_username != null)
        {
            try
            {
                if ( errors_to_print.size() == 0)
                    errors_to_print.add( "Report: master mgc list upload.\n File Name "+fileName);
                else
                    errors_to_print.add( 1,"Report: master mgc list upload.\n File Name "+fileName);
                System.out.println("send message");
                Mailer.notifyUser(m_username, "report.txt", "Report: master mgc list upload","Report: master mgc list upload",  errors_to_print);
                // m_writer.flush(); m_writer.close();
                File m_log_file = new File(FILE_PATH + "import_log.txt");
                
                Mailer.sendMessageWithAttachedFile(m_username,  "Report on master mgc list upload","Report for mgc list upload",  m_log_file);
                m_log_file.delete();
                
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(System.currentTimeMillis());
        return true;
        
    }
    
    */
    
    
    //*********************************************************************
    
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
    
    /*
    private boolean readCloneInfo(InputStream input, String fileName,
    ArrayList containerCol, Hashtable existingClones, Hashtable existingCloneSettings)
    {
        int prev_mgc_containers = 0;//how many mgccontainers exist in DB
        
        String line = null;
        String last_container = "";
        String current_container = "";
        MgcContainer cont = null;MgcSample clone = null;
        int seq_id = -1;
        int i = 0;
        int current_container_number = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        try
        {
            prev_mgc_containers = FlexIDGenerator.getID("mgccontainerid");
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
                
                try
                {
                    while(st.hasMoreTokens())
                    {
                        info[i] = st.nextToken();
                        i++;
                    }
                }
                
                catch(Exception e)
                {continue;}
                
                //look only for human
                if ( !info[7].equals("human") ) continue;
                
                //if container the same - create new MgcClone
                //************************** change
                current_container = info[9] + info[10];
                
                //check if this mgc clone info exist in db for the same container/position
                seq_id = -1;
                if ( existingClones.containsKey(info [1] ) )
                {
                     MinClone mc =null;
                    if (existingCloneSettings.containsKey(info [1]+"|"+current_container) )
                    {
                        mc = (MinClone)existingCloneSettings.get(info [1] +"|"+current_container );
                        if (   mc.isEqual(info[11], info[12])  )
                            continue;
                    }
                    else// case that clone came on different plate
                    {
                        mc = (MinClone)existingClones.get(info [1] );
                        seq_id = mc.getSequenceId();
                    }
                }
                
                if ( !last_container.equals(current_container))
                {
                    
                    cont = new MgcContainer( -1,fileName, new Location(Location.FREEZER) ,
                    current_container,
                    MgcContainer.getLabel(prev_mgc_containers + current_container_number ),
                    info[9]);
                    writeToFile("Get from import file mgc container\t"+current_container +"\n");
                    containerCol.add(cont);
                    current_container_number++;
                    last_container = current_container;
                    
                }
                
               
                clone = new MgcSample( -1,  -1,
                        Integer.parseInt(info [1]), Integer.parseInt(info[0]),
                        info[8], info[11], Integer.parseInt(info[12]),
                        MgcSample.STATUS_AVAILABLE);
                
                writeToFile("Get from import file mgc clone\t "+info [1] + "\n");
                if (seq_id != -1) clone.setSequenceId(seq_id);
                //clone.setSequenceId(seq_id);
                cont.addSample(clone);
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
    */
    
    /**
     *depricated
     * Read the mgc master list  information from the input stream.
     * The input is in the following format:
     *IMAGE_cloneID:MGC_ID:source_collection:source_plate:source_row:source_column
     *      0      :   -(1  :      -(2          :     -(3      :     -(4    :   -(5
     *libr_id:	species:vector:	rearray_collection:rearray_plate:rearray_row:rearray_column
     *    6   :    -(7   :   6(8)  :       (1)9           :     2(10)      :     3(11)    :    4(12)
     *    
     *
     IMAGE_cloneID	MGC_ID	source_collection	source_plate	source_row	source_column	libr_id	species	vector	rearray_collection	rearray_plate	rearray_row	rearray_column
2899944                 5307            IRAK                 3              a	1	1422	human	pCMV-SPORT6	IRAT	1	a	4
         
    CLONE_ID	COLLECTION_NAME	PLATE	ROW_POS	COL_POS	LIBR_NAME	VECTOR_NAME
   2899899	IRAT	          1	  a	  1	NIH_MGC_10	pCMV-SPORT6
               
     * @param input The InputStream object containing the mgc clone information.
     * @param cloneCol - hashtable (key mgcid) of all mgc clones read from the file
     * @ param containerCol - collection of mgcContainer objects that hold these mgc clones.
     * @return true if successful; false otherwise.
     */
    
    /*
     private boolean readCloneInfo_NF(InputStream input, String fileName,
                ArrayList containerCol, Hashtable existingClones, 
                Hashtable existingCloneSettings)
    {
        int prev_mgc_containers = 0;//how many mgccontainers exist in DB
        
        String line = null;
        String last_container = "";
        String current_container = "";
        MgcContainer cont = null;MgcSample clone = null;MinClone mc =null;
        int seq_id = -1;
        int i = 0;
        int current_container_number = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        try
        {
            prev_mgc_containers = FlexIDGenerator.getID("mgccontainerid");
        }
        catch(Exception e)
        {return false;}
        
        try
        {
            line = in.readLine();
            while((line = in.readLine()) != null)
            {
                //CLONE_ID	COLLECTION_NAME	PLATE	ROW_POS	COL_POS	LIBR_NAME	VECTOR_NAME

                StringTokenizer st = new StringTokenizer(line, DILIM);
                String [] info = new String[7];
                i = 0;
                
                try
                {
                    while(st.hasMoreTokens())
                    {
                        info[i] = st.nextToken();
                        i++;
                    }
                }  catch(Exception e)                {continue;}
                
                //if container the same - create new MgcClone
                //************************** change
                current_container = info[1] + info[2];
                
                //check if this mgc clone info exist in db for the same container/position
                seq_id = -1;
                if ( existingClones.containsKey(info [0] ) )
                {
                    if (existingCloneSettings.containsKey(info [0]+"|"+current_container) )
                    {
                        mc = (MinClone)existingCloneSettings.get(info [0] +"|"+current_container );
                        if (   mc.isEqual(info[3], info[4])  )
                            continue;
                    }
                    else// case that clone came on different plate
                    {
                        mc = (MinClone)existingClones.get(info [0] );
                        seq_id = mc.getSequenceId();
                    }
                }
                
                if ( !last_container.equals(current_container))
                {
                    
                    cont = new MgcContainer( -1,fileName, new Location(Location.FREEZER) ,
                    current_container,
                    MgcContainer.getLabel(prev_mgc_containers + current_container_number ),
                    info[1]);
                    writeToFile("Get from import file mgc container\t"+current_container +"\n");
                    containerCol.add(cont);
                    current_container_number++;
                    last_container = current_container;
                    
                }
                
               
                clone = new MgcSample( -1,  -1,
                        Integer.parseInt(info[0]),
                        info[6], info[3], Integer.parseInt(info[4]),
                        MgcSample.STATUS_AVAILABLE);
                
                
                writeToFile("Get from import file mgc clone\t "+info [0] + "\n");
                if (seq_id != -1) clone.setSequenceId(seq_id);
                //clone.setSequenceId(seq_id);
                cont.addSample(clone);
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
     */
     
     /**
     * Read the mgc master list  information from the input stream.
     * The input is in the following format:
      *
      *format_0
      * *IMAGE_cloneID:MGC_ID:source_collection:source_plate:source_row:source_column
     *      0      :   1  :      2          :     3      :     4    :   5
     *libr_id:	species:vector:	rearray_collection:rearray_plate:rearray_row:rearray_column
     *    6   :    7   :   8  :       9           :     10      :     11    :    12
     *
  
      *format 1
    
     IMAGE_cloneID	MGC_ID	source_collection	source_plate	source_row	source_column	libr_id	species	vector	rearray_collection	rearray_plate	rearray_row	rearray_column
2899944                 5307            IRAK                 3              a	1	1422	human	pCMV-SPORT6	IRAT	1	a	4
         
    CLONE_ID	COLLECTION_NAME	PLATE	ROW_POS	COL_POS	LIBR_NAME	VECTOR_NAME
   2899899	IRAT	          1	  a	  1	NIH_MGC_10	pCMV-SPORT6
               
     * @param input The InputStream object containing the mgc clone information.
     * @param cloneCol - hashtable (key mgcid) of all mgc clones read from the file
     * @ param containerCol - collection of mgcContainer objects that hold these mgc clones.
     * @return true if successful; false otherwise.
     */
     private ArrayList readCloneInfo(InputStream input, String fileName, String report_file_name)
    {
         
        String line = null;
        String last_container = "";
        String current_container_label = "";
        ArrayList containers = new ArrayList();
        MgcContainer cont = null;MgcSample clone = null;
        int i = 0;
        int current_container_number = 0;
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        
        
        try
        {
            line = in.readLine();
            while((line = in.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String [] info = new String[30];
                i = 0;
                try
                {
                    while(st.hasMoreTokens())
                    {
                        info[i] = st.nextToken();
                        i++;
                    }
                }  catch(Exception e)                {continue;}
                
                //if container the same - create new MgcClone, always upper case
                //************************** change
                current_container_label = getCurrentContainerLabelFormatDependent(info, SUBMISSION_FORMAT);//info[1] + info[2];
                   
                if ( !last_container.equals(current_container_label))
                {
                    //label should be set later
                    cont = new MgcContainer( -1,fileName, new Location(Location.FREEZER) ,
                    current_container_label,"",       getMarker(info, SUBMISSION_FORMAT));
                    writeToFile("Get from import file mgc container\t"+current_container_label +"\n", report_file_name);
                    containers.add(cont);
                    current_container_number++;
                    last_container = current_container_label;
                    
                }
                
               
                clone = getMGCSampleFormatDependent(info, SUBMISSION_FORMAT); //new MgcSample( -1,  -1,
                        //Integer.parseInt(info[0]),
                        //info[6], info[3], Integer.parseInt(info[4]),
                        //MgcSample.STATUS_AVAILABLE);
                
                
                writeToFile("Get from import file mgc clone\t "+clone.toString() + "\n", report_file_name);
               cont.addSample(clone);
                        }
            input.close();
            return containers;
        }catch (Exception ex)
        {    try
             {input.close(); }catch(Exception e)
             {
                 System.out.println(e.getMessage() );
                 return null;}
             return null;        }
    }
     
     // function gets container id for different formats
     // change for new format
     private String getCurrentContainerLabelFormatDependent(String[] info, int format)//info[1] + info[2];
     {
         switch(format)
         {
             case FORMAT_0: return info[9]+info[10];
             case FORMAT_1: return info[1] + info[2];
             case FORMAT_2: return info[9]+info[10];
             default: return null;
         }
     }
     
     private String getMarker(String[] info, int format)//info[1] + info[2];
     {
         switch(format)
         {
             case FORMAT_0: return info[9];
             case FORMAT_1: return info[1] ;
             case FORMAT_2: return info[9];
             default: return null;
         }
     }
     //function fills MGCSample
     // change for new format
     /*format 2
     //Accession[0]	CDS start[1]	CDS stop[2]	gi[3]	Gene[4]	IMAGE[5]
     library ID[6]	orientation[7]	vector[8]	Collection[9]	Plate[10]
      Row[11]	Column[12]
      **/

     private MgcSample getMGCSampleFormatDependent(String[] info, int format) throws FlexDatabaseException
     {
         switch(format)
         {
             case FORMAT_0: return   new MgcSample( -1,  -1,  Integer.parseInt(info [1]), Integer.parseInt(info[0]), info[8], info[11], Integer.parseInt(info[12]), MgcSample.STATUS_AVAILABLE);
             case FORMAT_1: return new MgcSample( -1,  -1,Integer.parseInt(info[0]),info[6], info[3], Integer.parseInt(info[4]),MgcSample.STATUS_AVAILABLE);
             
             case FORMAT_2: return new MgcSample( -1, -1,  -1, Integer.parseInt(info[5]), info[8], info[11], Integer.parseInt(info[12]), MgcSample.STATUS_AVAILABLE, getOrientation(info[7]), Integer.parseInt(info[3]));
             default: return null;
         }
     }
     private int            getOrientation(String orientation)
     {
         if ( orientation.equalsIgnoreCase("forward")) return ORIENTATION_FORWARD;
         else if (orientation.equalsIgnoreCase("reverse")) return ORIENTATION_REVERSE;
         else return ORIENTATION_NOT_KNOWN;
     }
    /**
     * DEPRICATED
     *Function checks for duplication of MgcID to prevent inserting duplicated sequences into DB
     *depricated
     */
    /*
    private boolean getExistingClonesFromDB(Hashtable existingClones, Hashtable existingCloneSettings)
    {
        String sql = null;
        if ( m_data_format == FORMAT_OLD)
        {
            sql =    "select mc.mgcid as id, mc.sequenceid as seq, mc.orgrow as arow, mc.orgcol as acol, "+
        " mcont.oricontainer as cont from mgcclone mc, mgccontainer mcont, sample s, containerheader h where "+
        "(s.containerid = h.CONTAINERID and mcont.mgccontainerid = h.containerid and "+
        "mc.mgccloneid=s.sampleid )";
        }
        else if (m_data_format == FORMAT_NEW )
        {
             sql =    "select mc.imageid as id, mc.sequenceid as seq, mc.orgrow as arow, mc.orgcol as acol, "+
        " mcont.oricontainer as cont from mgcclone mc, mgccontainer mcont, sample s, containerheader h where "+
        "(s.containerid = h.CONTAINERID and mcont.mgccontainerid = h.containerid and "+
        "mc.mgccloneid=s.sampleid )";
        }
        
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
                existingCloneSettings.put(mc.getIdString() +"|"+mc.getContainerName() , mc);
                existingClones.put(mc.getIdString() , mc);
            }
            return true;
        }
        catch (Exception e)
        {
            errors_to_print.add("Can not open database for extracting existing clones.\n" );
            return false;
        }
        
    }
     **/
    
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
 /*  private boolean readSeqences(ArrayList containerCol, Hashtable sequenceCol)
    {
        GenbankGeneFinder gb = new GenbankGeneFinder();
        Vector genBankSeq = new Vector();
        Hashtable seqData = new Hashtable();
        int current_key = -1; String query_str = null;
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
                     if ( m_data_format == FORMAT_OLD)
                     {
                         current_key = ((MgcSample)sampl.get(sample_count)).getMgcId();
                         query_str = "\"MGC:" + current_key +"\"";
                     }
                     else  if ( m_data_format == FORMAT_NEW)
                     {
                         current_key = ((MgcSample)sampl.get(sample_count)).getImageId();
                         query_str = "\"IMAGE:" + current_key +"\"";
                     }
                     if (current_key < 1  ) continue;
                     fs = null;
                    try
                    {
                        genBankSeq = gb.search(query_str);

                        if (genBankSeq.isEmpty() ) continue;
                        for (int countGS = 0; countGS < genBankSeq.size(); countGS++)
                        {// extract only human and those where accession starts from 'BC'
                            GenbankSequence current_gbs = (GenbankSequence)genBankSeq.get(countGS);
                            //just 'BC' count
                            
                            if ( !current_gbs.getAccession().substring(0,2).equals("BC")      ) continue;
                            current_gi = current_gbs.getGi();
                            if (current_gi == null || current_gi.equals("") ) continue;
                            seqData = gb.searchDetail(current_gi);
                            //only human
                            if (((String)seqData.get("species")).indexOf("sapiens") != -1)
                            {
                                fs = createFlexSequence( seqData, current_gbs);
                                continue;
                            }
                        }
                        if (fs == null)//can not create sequence
                        {
                            errors_to_print.add("Can not find sequence for MGC : " + current_key);
                            continue;
                        }
                        if ( m_data_format == FORMAT_NEW )
                        {
                           String mgc_id = fs.getInfoValue(FlexSequence.MGC_ID) ;
                           if ( mgc_id != null && mgc_id.trim().length()>0)
                           {
                               try{sample.setMgcId( Integer.parseInt(mgc_id));}catch(Exception e){}
                           }
                        }
                        sequenceCol.put( Integer.toString(current_key), fs );
                        writeToFile("Get from ncbi sequence for mgc clone\t "+current_key  +"\n" );
                        current_gi = null;
                        
                    }catch(Exception e)
                    { errors_to_print.add("Can not find sequence for MGC : " + current_key + "\n");  }
                    
                }
            }//end sample count
        }//end loop container_count
        return true;
   
    }
    
    
    */
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
     
      private void readSeqences(ArrayList containerCol, Hashtable sequenceCol, String report_file_name) throws Exception
    {
        for (int container_count = 0; container_count < containerCol.size(); container_count++)
        {
            readSeqences((MgcContainer)containerCol.get(container_count),  sequenceCol,  report_file_name);
        }
      }
      
   private void readSeqences(MgcContainer container, Hashtable sequenceCol, String report_file_name) throws Exception
    {
        GenbankGeneFinder gb = new GenbankGeneFinder();
        Vector genBankSeq = new Vector();
        Hashtable seqData = new Hashtable();
        int current_key = -1; 
        String query_str = null;
        String current_gi = null;
        
        MgcSample sample = null;
        FlexSequence fs = null;
        
        
            Vector sampl = container.getSamples();
            for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)//sample count
            {
                sample = (MgcSample)sampl.get(sample_count);
                
                if (sample.getSequenceId() == -1)
                {
                    
                     query_str = getSearchStringForMGCClone(sample);
                     current_key = getSequenceSearchID(sample);
                    // if (current_key < 1  ) continue;
                     if ( query_str == null || query_str.equalsIgnoreCase("")) continue;
                     fs = null;
                    try
                    {
                        genBankSeq = gb.search(query_str);

                        if (genBankSeq.isEmpty() ) continue;
                        for (int countGS = 0; countGS < genBankSeq.size(); countGS++)
                        {// extract only human and those where accession starts from 'BC'
                            GenbankSequence current_gbs = (GenbankSequence)genBankSeq.get(countGS);
                            //just 'BC' count
                            
                            if ( !current_gbs.getAccession().substring(0,2).equals("BC")      ) continue;
                            current_gi = current_gbs.getGi();
                            if (current_gi == null || current_gi.equals("") ) continue;
                            seqData = gb.searchDetail(current_gi);
                            //only human
                            if (((String)seqData.get("species")).indexOf("sapiens") != -1)
                            {
                                fs = createFlexSequence( seqData, current_gbs);
                                continue;
                            }
                        }
                        if (fs == null)//can not create sequence
                        {
                            errors_to_print.add("Can not find sequence for MGC : " + current_key);
                            continue;
                        }
                        if ( SUBMISSION_FORMAT == FORMAT_1 || SUBMISSION_FORMAT == FORMAT_2)
                        {
                           String mgc_id = fs.getInfoValue(FlexSequence.MGC_ID) ;
                           if ( mgc_id != null && mgc_id.trim().length()>0)
                           {
                               try{sample.setMgcId( Integer.parseInt(mgc_id));}catch(Exception e){}
                           }
                        }
                        sequenceCol.put( Integer.toString(current_key), fs );
                        writeToFile("Get from ncbi sequence for mgc clone\t "+current_key  +"\n" , report_file_name);
                        current_gi = null;
                        
                    }catch(Exception e)
                    { 
                        errors_to_print.add("Can not find sequence for MGC : " + current_key + "\n");
                        throw new FlexCoreException("Cannot find sequences");
                    }
                    
                }
            }//end sample count
     
    }
    
    
    private String      getSearchStringForMGCClone(MgcSample sample)
    {
        String query_string = null;
        if ( sample.getGI() > 0) return String.valueOf(sample.getGI());
        else if (sample.getMgcId() > 0) return  "\"MGC:" + sample.getMgcId() +"\"";
        else  if ( sample.getImageId() > 0 ) return "\"IMAGE:" + sample.getImageId() +"\"";
        return query_string;
                     
    }
    
    private int         getSequenceSearchID(MgcSample sample)
    {
        if ( sample.getGI() > 0) return sample.getGI() ;
        else if (sample.getMgcId() > 0) return sample.getMgcId();
        else  if ( sample.getImageId() > 0 ) return sample.getImageId();
        return -1;
                     
    }
    
    /**Load sequence, mgc container and mgc sample information into database
     */
     private void uploadToDatabase( Connection conn, ArrayList containerCol,
    Hashtable sequenceCol, String report_file_name) throws Exception
     {
         for (int count = 0; count < containerCol.size(); count++)
         {
             uploadToDatabase(  conn,  (MgcContainer)containerCol.get(count),    sequenceCol,  report_file_name);
         }
     }
    private void uploadToDatabase( Connection conn, MgcContainer cont,
    Hashtable sequenceCol, String report_file_name)throws Exception
     {
        String current_key = null;
        int fs_key = -1;
        MgcSample current_clone = null;
     
         try
            {
                Vector sampl = cont.getSamples();
                
                for (int sample_count = 0; sample_count < sampl.size() ; sample_count++)
                {
                    current_clone = (MgcSample)sampl.get(sample_count);
                    if (current_clone.getSequenceId() == -1)
                    {
                       
                        current_key = String.valueOf( getSequenceSearchID(current_clone));
                        FlexSequence fs = (FlexSequence)sequenceCol.get( current_key);
                        if (fs != null && !fs.getQuality().equals(FlexSequence.QUESTIONABLE)  )
                        {
                           fs.insert(conn);
                            fs_key = fs.getId();
                            writeToFile("Insert into db sequence for mgc clone\t"+fs_key  + "\n", report_file_name);
                            current_clone.setSequenceId(fs_key);
                        }
                        if (fs == null) current_clone.setStatus(MgcSample.STATUS_NO_SEQUENCE);
                        if (fs != null && fs.getQuality().equals(FlexSequence.QUESTIONABLE ) ) current_clone.setStatus(MgcSample.STATUS_BAD_SEQUENCE);
                    }
                    
                }//end loop clone
                System.out.println(cont.toString());
                cont.insert(conn);
                writeToFile("Insert into db mgc container\t"+cont.getLabel() + "_"+cont.getId() +"\n", report_file_name);
                DatabaseTransaction.commit(conn);
                
            } 
            catch (Exception ex)
            {
                errors_to_print.add("Can not commit MGC container to database: " + cont.getLabel() + "\n");
                DatabaseTransaction.rollback(conn);
                throw new FlexDatabaseException("Can not commit MGC container to database: " + cont.getLabel() + "\n");
            }
       
    }
    
   
    
    
    
    
   /*-------------------------------------------------------------- 
    
    // unchangable part
    
    
    
     
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
    public FlexSequence createFlexSequence(Hashtable seqData, GenbankSequence genBankSeq)
    {
        //can get empty seqData filled by default values
        String seqText = (String)seqData.get("sequencetext");
        if (seqText == null || seqText.equals("") ) return null;
        
        int start = ((Integer)seqData.get("start")).intValue();
        int stop = ((Integer)seqData.get("stop")).intValue();
        String seqQuality;
        int cdsLength; int gccont = 0;
        Vector publicInfo = new Vector();
        
        Hashtable pubinfo_entry_gi = new Hashtable();
        pubinfo_entry_gi.put(FlexSequence.NAMETYPE,FlexSequence.GI);
        pubinfo_entry_gi.put(FlexSequence.NAMEVALUE,genBankSeq.getGi() );
        pubinfo_entry_gi.put(FlexSequence.DESCRIPTION,genBankSeq.getDescription() );
        publicInfo.add(pubinfo_entry_gi);
        
        
        Hashtable pubinfo_entry_gb = new Hashtable();
        pubinfo_entry_gb.put(FlexSequence.NAMETYPE,FlexSequence.GENBANK_ACCESSION);
        pubinfo_entry_gb.put(FlexSequence.NAMEVALUE,genBankSeq.getAccession() );
        publicInfo.add(pubinfo_entry_gb);
        
       if ( genBankSeq.getDescription().indexOf(" MGC:") != -1)
        {
            String tmp = genBankSeq.getDescription().substring(genBankSeq.getDescription().indexOf(" MGC:")+5);
            tmp = tmp.substring(0, tmp.indexOf(" "));
            try
            {
                int mgcid =  Integer.parseInt(tmp);
                Hashtable pubinfo_entry_mgcid = new Hashtable();
                pubinfo_entry_mgcid.put(FlexSequence.NAMETYPE,FlexSequence.MGC_ID);
                pubinfo_entry_mgcid.put(FlexSequence.NAMEVALUE, tmp );
                publicInfo.add(pubinfo_entry_mgcid);
            }catch(Exception e){}
    
        }
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
        
        cdsLength = (start==-1 || stop == -1)? 0 : stop - start +1;
        gccont = gc_content(seqText.substring(start-1, stop));
        FlexSequence seq = new FlexSequence(-1, FlexSequence.NEW,
        "Homo sapiens", null, seqText, start, stop,
        cdsLength, gccont, publicInfo);
        if(  seq.checkSequence() )
        {
            seq.setQuality(FlexSequence.GOOD);
            return seq;
        }
        else
        {
            return null;
        }
    }
    
    
   
    
    /* Function queries for all mgc containers needed for request
     *put on Queue all containers and sequences
     */
    private boolean putOnQueue(Connection conn, ArrayList containerCol)
    {
        
        //put mgc containers on queue
        Protocol protocol = null;
        Project mgcProject = null;
        Workflow mgcPlateHandleWorkflow = null;
        
        QueueItem queueItem = null;
        LinkedList queueItems = new LinkedList();
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
              
        try
        {
            protocol = new Protocol(  Protocol.CREATE_CULTURE_FROM_MGC);
            mgcProject = new Project(Project.MGC_PROJECT);
            mgcPlateHandleWorkflow = new Workflow(Workflow.MGC_PLATE_HANDLE_WORKFLOW);
            
        }catch(FlexDatabaseException ex)
        {
            errors_to_print.add("Can not get protocol for CREATE_CULTURE_FROM_MGC");
            System.out.println("ex");
            return false;
        }
              
        for (int cont_count = 0; cont_count < containerCol.size(); cont_count++)
        {
            // The project and workflow for these containers are set to MGC project and MGC plate handle workflow.
            queueItem = new QueueItem((Container) containerCol.get(cont_count),protocol, mgcProject, mgcPlateHandleWorkflow);
            queueItems.add(queueItem);
        }
        try
        {
            containerQueue.addQueueItems(queueItems, conn);
        }
        catch(Exception e)
        {
            errors_to_print.add("Can not put containers on queue for CREATE_CULTURE_FROM_MGC");
            return false;
        }
        DatabaseTransaction.commit(conn);      
        return true;
    }
    
  
    
    class MinClone
    {
        int m_id = -1;
        String m_container_name = null;
        String m_row = null;
        int m_col = -1;
        int m_seq_id = -1;
        
        public MinClone(int id, String cont, String row, int col, int seq_id)
        {
            m_id = id;
            m_container_name = cont;
            m_row = row;
            m_col = col;
            m_seq_id = seq_id;
            
        }
        
        public boolean isEqual(  String row, String col)
        {
            if ( m_row.equalsIgnoreCase(row) &&  m_col == Integer.parseInt(col))
                return true;
            else
                return false;
        }
        
        public String           getContainerName(){return m_container_name;}
        public int              getId()        { return m_id;}
        public String           getIdString()        { return Integer.toString(m_id);}
        public int              getSequenceId()       { return m_seq_id;}
    }
    
    private void writeToFile(String mes, String file_name)
    {
        try
        {
            //delete log file
            m_writer = new  FileWriter(file_name, true);
            m_writer.write(mes);
            m_writer.close();
        }catch(IOException e)
        { errors_to_print.add("Can not open log file");}
    }
    
    //calculates gc_content for the sequence shold be some other place
    private int gc_content(String seq)
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
        return i;
    }
    //****************************Testing*******************************
    
    public static void main(String args[])
    {
        
        String file = "E:\\HTaycher\\MGC\\NewDev8-06\\IRCM_real.txt";
        InputStream input;
        
        try
        {
            input = new FileInputStream(file);
            
        } catch (FileNotFoundException ex)
        {
            System.out.println(ex);
            return;
        }
        
        MgcMasterListImporter importer = new MgcMasterListImporter("htaycher");
        importer.importMgcCloneInfoIntoDB(input, file) ;
        System.exit(0);
    }
    
    
    
}

