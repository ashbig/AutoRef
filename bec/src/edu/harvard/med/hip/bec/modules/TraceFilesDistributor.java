/*
 * TraceFilesDistributor.java
 *
 * Created on April 18, 2003, 2:59 PM
 */

package edu.harvard.med.hip.bec.modules;

import java.io.*;
import java.util.*;

import edu.harvard.med.hip.bec.programs.phred.*;
import  edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.file.*;
/* Distribute trace files and create subdirectories based on the
 * following trace naming convention:
 * 125600_A01_495168_1208972_F0
 * plateId_wellId_sequenceId_cloneId_strand(read number)
 * under each cloneId(isolateId) directory, 7 subdirectories are created.
 */
/**
 *
 * @author  htaycher
 */
public class TraceFilesDistributor
{
    private ArrayList m_error_messages = null;
    private boolean   m_isError_directory_exists = false;
    private boolean   m_isEmptySamples_directory_exists = false;
    
    /** Creates a new instance of TraceFilesDistributor */
    public TraceFilesDistributor()
    {
        m_error_messages= new ArrayList();
    }
    
    
    public ArrayList getErrorMesages()
    { return m_error_messages;}
    
     /* outputBaseDir specify the base directory for trace file distribution
      * inputTraceDir specify the directory where the trace files get dumped from sequencer
      * errorDir specify where the error log is stored for trace files failed Phred run
      */
    public ArrayList distributeChromatFiles(String inputTraceDir, String outputBaseDir, String wrongformatfiles, String emptysamples_directory)
    {
        ArrayList chromat_files = new ArrayList();
        //obtain file path for base directory for trace file distribution
        File baseDir = new File(outputBaseDir);
        
        //obtain trace file list from source trace file directory
        File sourceDir = new File(inputTraceDir); //trace file directory
        File [] sourceFiles = sourceDir.listFiles();
        
        TraceFilesDistributor distributor = new TraceFilesDistributor();
        
        //call file transfer and Phred moduels for each trace file
        //Phred output sequence and trace files are parsed and saved to reads.
        File destinationFile = null; //destination trace file dir after file transfer
        File traceFile = null;
        boolean isError_directory_exists = false;
        String destinationFileName  ; String destination_directory = null;
        for (int count = 0; count < sourceFiles.length; count++)
        {
            traceFile = sourceFiles[count];
            //check for file format & abi file
            if (! isTraceFile( traceFile,  wrongformatfiles)  )
            {
                continue;
            }
            
            //check for file format // control //empty samples
            if (! isWriteNamingFormat( traceFile,  wrongformatfiles,     emptysamples_directory,     destination_directory ) || destination_directory == null )
            {
                continue;
            }
            
                    
            
            try
            {
                //create file structure and distribute trace file into chromat_dir
                destinationFileName = distributor.distributeFile(traceFile,outputBaseDir,destination_directory);//baseDir);
                destinationFile = new File(destinationFileName);
                if (destinationFile != null)
                {
                    chromat_files.add(destinationFileName);
                }
                
            }
            catch(Exception e)
            {
                e.printStackTrace();
                m_error_messages.add("Cannot distribute file "+traceFile.getName());
            }
            
        } // for
        return chromat_files;
        
    }//processPipeline
    
    /*
    private void distributeFiles(File[] files, String base_directory)
    {
        for (int count = 0; count < files.length; count++)
        {
            try
            {
                distributeFile( files[count],  base_directory);
            }
            catch(Exception e)
            {}
        } // for
     
    } // distributeFiles
     */
    public  String distributeFile(File file, String base_directory, String destination_dir) throws BecUtilException,Exception
    {
        
        //create clone file distirution tree : each clone has 7 dir
        createDirectories(base_directory + File.separator+ destination_dir);
        
        //move trace file into chromat directory
        String chromatfilepath = base_directory +File.separator+destination_dir +File.separator+PhredWrapper.CHROMAT_DIR_NAME+File.separator+ file.getName();
        FileOperations.moveFile(file, new File(chromatfilepath));
        return chromatfilepath;
        
    } // distributeFiles
    
    
    //__________________________________________
    
    //generate file path for isolate directory
    public  String getDestinationFilePath(String trace_file_name)throws Exception
    {
        PhredOutputFileName pr = new PhredOutputFileName(trace_file_name, PhredOutputFileName.FORMAT_OURS);
        String sequenceId = pr.getSequenceid();
        String isolateId = pr.getCloneid();
        
        return pr.getSequenceid() +File.separator+pr.getCloneid();
        
    } // getDestinationFilePath
    
    //create sequence_dir, quality_dir, contig_dir and consensus_dir
    private  void createDirectories(String destination_parent_directory)throws Exception
    {
        try
        {
            
            FileOperations.createDirectory(destination_parent_directory+File.separator+PhredWrapper.CHROMAT_DIR_NAME, true);
            FileOperations.createDirectory(destination_parent_directory+File.separator+PhredWrapper.SEQUENCE_DIR_NAME,true);
            FileOperations.createDirectory(destination_parent_directory+File.separator+PhredWrapper.PHD_DIR_NAME,true);
            FileOperations.createDirectory(destination_parent_directory+File.separator+PhredWrapper.EDIT_DIR_NAME,true);
            FileOperations.createDirectory(destination_parent_directory+File.separator+PhredWrapper.QUALITY_DIR_NAME,true);
            FileOperations.createDirectory(destination_parent_directory+File.separator+PhredWrapper.CONTIG_DIR_NAME,true);
            FileOperations.createDirectory( destination_parent_directory+File.separator+PhredWrapper.CONSENSUS_DIR_NAME,true);
        }
        catch(Exception e)
        {}
        /*
        File chromatdir = new File(destination_parent_directory,PhredWrapper.CHROMAT_DIR_NAME);
        FileOperations.createDirectory(chromatdir);
        File seqDir = new File(destination_parent_directory,PhredWrapper.SEQUENCE_DIR_NAME);
        FileOperations.createDirectory(seqDir);
        File phdDir = new File(destination_parent_directory,PhredWrapper.PHD_DIR_NAME);
        FileOperations.createDirectory(phdDir);
        File editDir = new File(destination_parent_directory,PhredWrapper.EDIT_DIR_NAME);
        FileOperations.createDirectory(editDir);
        File qualDir = new File(destination_parent_directory,PhredWrapper.QUALITY_DIR_NAME);
        FileOperations.createDirectory(qualDir);
        File contigDir = new File(destination_parent_directory,PhredWrapper.CONTIG_DIR_NAME);
        FileOperations.createDirectory(contigDir);
        File consensusDir = new File( destination_parent_directory,PhredWrapper.CONSENSUS_DIR_NAME);
        FileOperations.createDirectory(consensusDir);
         **/
    }//createAdditionalDirectories
    
    private boolean isTraceFile(File traceFile, String wrongformatfiles)
    {
        if (!traceFile.getName().endsWith(".ab1"))
        {
            m_error_messages.add("File "+traceFile.getName() +" is not abi file");
            moveFile(traceFile, wrongformatfiles, m_isError_directory_exists);
            return false;
        }
        return true;
    }
    
    
    private boolean isWriteNamingFormat(File traceFile, String wrongformatfiles,
    String emptysamples_directory,
    String destination_directory )
    {
        //check for file format
        PhredOutputFileName pr = new PhredOutputFileName(traceFile.getName());
        if ( pr.isWriteFileFormat( PhredOutputFileName.FORMAT_OURS ))
        {
            //check if control - put them into error directory
            if ( pr.getCloneidNumber() == 0 && pr.getSequenceidNumber() == 0)
            {
                
                moveFile(traceFile, wrongformatfiles, m_isError_directory_exists);
                return false;
            }
            // check if empty well
            else if (pr.getCloneidNumber() == 0 && pr.getSequenceidNumber() != 0)
            {
                moveFile(traceFile, emptysamples_directory, m_isEmptySamples_directory_exists);
                return false;
            }
            //file ok
            destination_directory = pr.getSequenceid() +File.separator+pr.getCloneid();
            return true;
        }
        else
        {
            m_error_messages.add("File "+traceFile +" has wrong format.");
            moveFile(traceFile, wrongformatfiles, m_isError_directory_exists);
            return false;
            
        }
   
    }
    
    
    private void moveFile(File moving_file, String directory_move_to, boolean isDirectoryExists)
    {
        try
        {
            if ( ! isDirectoryExists)
            {
                FileOperations.createDirectory(directory_move_to,true);
                isDirectoryExists= true;
            }
            FileOperations.moveFile(moving_file, new File(directory_move_to  +File.separator+ moving_file.getName()));
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot move file "+moving_file.getName());
        }
        
    }
}
