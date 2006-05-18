//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
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
    private ArrayList   m_name_of_files_to_distribute = null;
    private boolean     m_isInnerReads = false;

    /** Creates a new instance of TraceFilesDistributor */
    public TraceFilesDistributor()
    {
        m_error_messages= new ArrayList();
    }

    public void      setNameOfFilesToDistibute(ArrayList file_names){m_name_of_files_to_distribute = file_names;}
    public void      setIsInnerReads(boolean ir){m_isInnerReads = ir;}
    public ArrayList getErrorMesages()    { return m_error_messages;}

     /* outputBaseDir specify the base directory for trace file distribution
      * inputTraceDir specify the directory where the trace files get dumped from sequencer
      * errorDir specify where the error log is stored for trace files failed Phred run
      */
    public ArrayList distributeEndReadsChromatFiles(String inputTraceDir, String outputBaseDir)
    {
        ArrayList chromat_files = new ArrayList();
         PhredOutputFileName pr = null;
        //obtain file path for base directory for trace file distribution
        File baseDir = new File(outputBaseDir);

        //obtain trace file list from source trace file directory
        File sourceDir = new File(inputTraceDir); //trace file directory

        File [] sourceFiles = sourceDir.listFiles();
        if ( sourceFiles == null) return new ArrayList();
      //  TraceFilesDistributor distributor = new TraceFilesDistributor();

        //call file transfer and Phred moduels for each trace file
        //Phred output sequence and trace files are parsed and saved to reads.
        File destinationFile = null; //destination trace file dir after file transfer
        File traceFile = null;
        boolean isError_directory_exists = false;
        String destinationFileName  ; String destination_dir = null;
        String file_info = null;
        for (int count = 0; count < sourceFiles.length; count++)
        {
            traceFile = sourceFiles[count];
            //initial version : remove extension
            file_info = traceFile.getName().substring(0, traceFile.getName().indexOf('.'));
            //remove time stamp part
            file_info = file_info.substring(0, file_info.lastIndexOf("_"));
              // check this file shoud be distibuted
            if ( m_name_of_files_to_distribute == null ||
                (m_name_of_files_to_distribute != null &&
                   !m_name_of_files_to_distribute.contains( file_info )
                   ))
                continue;
            try
            {
                //create file structure and distribute trace file into chromat_dir
                pr = new PhredOutputFileName(traceFile.getName(),PhredOutputFileName.FORMAT_OURS );
                destination_dir = pr.getSequenceid() +File.separator+pr.getCloneid();
                destinationFileName = distributeFile(traceFile,outputBaseDir,destination_dir);//baseDir);//distributor.distributeFile(traceFile,outputBaseDir,destination_dir);//baseDir);
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

      /* outputBaseDir specify the base directory for trace file distribution
      * inputTraceDir specify the directory where the trace files get dumped from sequencer
      * errorDir specify where the error log is stored for trace files failed Phred run
      */
    public void distributeInternalReadsChromatFiles(String inputTraceDir, String outputBaseDir)
    {
       PhredOutputFileName pr = null;
        //obtain file path for base directory for trace file distribution
        File baseDir = new File(outputBaseDir);
        //obtain trace file list from source trace file directory
        File sourceDir = new File(inputTraceDir); //trace file directory
        File [] sourceFiles = sourceDir.listFiles();
        if ( sourceFiles == null) return;
        //call file transfer and Phred moduels for each trace file
        //Phred output sequence and trace files are parsed and saved to reads.
        File destinationFile = null; //destination trace file dir after file transfer
        File traceFile = null;
        boolean isError_directory_exists = false;
        String destinationFileName  ; String destination_dir = null;
        for (int count = 0; count < sourceFiles.length; count++)
        {
            traceFile = sourceFiles[count];
              // check this file shoud be distibuted
           try
            {
                //create file structure and distribute trace file into chromat_dir
                pr = new PhredOutputFileName(traceFile.getName(),PhredOutputFileName.FORMAT_OURS );
               //end read
                if ( pr.getReadNumber() == -1) continue;
                destination_dir = pr.getSequenceid() +File.separator+pr.getCloneid();
                destinationFileName = distributeFile(traceFile,outputBaseDir,destination_dir, true,true);//baseDir);//distributor.distributeFile(traceFile,outputBaseDir,destination_dir);//baseDir);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                m_error_messages.add("Cannot distribute file "+traceFile.getName());
            }
        } // for
     }//processPipeline

      /* outputBaseDir specify the base directory for trace file distribution
      * inputTraceDir specify the directory where the trace files get dumped from sequencer
      * errorDir specify where the error log is stored for trace files failed Phred run
      */
    public void distributeNotActiveChromatFiles(String inputTraceDir,  String wrongformatfiles, String emptysamples_directory, String control_samples)
    {
        ArrayList chromat_files = new ArrayList();

        //obtain trace file list from source trace file directory
        File sourceDir = new File(inputTraceDir); //trace file directory
        //distribute wrong file format files
        distributeWrongNamingFormatFiles( wrongformatfiles ,  sourceDir);
        //distribute empty samples files
        distributeEmptySampleFiles( emptysamples_directory , sourceDir);
        //distribute control files
        distributeControlFiles( control_samples ,  sourceDir);
    }

  
    public  String distributeFile(File file, String base_directory, String destination_dir) throws BecUtilException,Exception
    {

        //create clone file distirution tree : each clone has 7 dir
        createDirectories(base_directory + File.separator+ destination_dir);

        //move trace file into chromat directory
        String chromatfilepath = base_directory +File.separator+destination_dir +File.separator+PhredWrapper.CHROMAT_DIR_NAME+File.separator+ file.getName();
        FileOperations.moveFile(file, new File(chromatfilepath), true,false);
        return chromatfilepath;

    } // distributeFiles
    public  String distributeFile(File file, String base_directory, String destination_dir, boolean isOverwrite, boolean isDelete) throws BecUtilException,Exception
    {

        //create clone file distirution tree : each clone has 7 dir
        createDirectories(base_directory + File.separator+ destination_dir);
        //move trace file into chromat directory
        String chromatfilepath = base_directory +File.separator+destination_dir +File.separator+PhredWrapper.CHROMAT_DIR_NAME+File.separator+ file.getName();
        FileOperations.moveFile(file, new File(chromatfilepath), isOverwrite,isDelete);
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
       
    }//createAdditionalDirectories

    private boolean isTraceFile(File traceFile, String wrongformatfiles)
    {
       if (!( traceFile.getName().endsWith(".ab1") || traceFile.getName().endsWith(".scf")) )
        {
           m_error_messages.add("File "+traceFile.getName() +" is not trace file");
           moveFile(traceFile, wrongformatfiles, m_isError_directory_exists);
           return false;
        }
        return true;
    }


    private void distributeEmptySampleFiles(String emptysamples_directory , File sourceDir)
    {
        FilenameFilter filter = new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                 PhredOutputFileName pr = new PhredOutputFileName(name);
                 if ( pr.isWriteFileFormat( PhredOutputFileName.FORMAT_OURS ))
                 {
                    //check if control - put them into error directory
                    if ( pr.getCloneidNumber() == 0 && pr.getSequenceidNumber() != 0)
                    {
                        return true;
                    }
                    else
                        return false;
                 }
                 else
                     return false;
               }
        };

        File[] empty_files = sourceDir.listFiles(filter);
        if ( empty_files == null) return;
        for (int file_count = 0; file_count < empty_files.length; file_count++)
        {
             moveFile(empty_files[file_count], emptysamples_directory, m_isEmptySamples_directory_exists);
        }

    }

    private void distributeControlFiles(String wrongformatfiles , File sourceDir)
    {
        FilenameFilter filter = new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                 PhredOutputFileName pr = new PhredOutputFileName(name);
                 if ( pr.isWriteFileFormat( PhredOutputFileName.FORMAT_OURS ))
                 {
                    //check if control - put them into error directory
                    if ( pr.getCloneidNumber() == 0 && pr.getSequenceidNumber() == 0)
                    {
                        return true;
                    }
                    else
                        return false;
                 }
                 else
                     return false;
               }
        };

        File[] control_files = sourceDir.listFiles(filter);
        if ( control_files == null) return;
        for (int file_count = 0; file_count < control_files.length; file_count++)
        {
             moveFile(control_files[file_count], wrongformatfiles, m_isError_directory_exists);
        }

    }


    private void distributeWrongNamingFormatFiles(String wrongformatfiles , File sourceDir)
    {
        FilenameFilter filter = new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                if (   !( name.endsWith(".ab1")  ||  name.endsWith(".scf")) )
                {
                   return true;
                }
                PhredOutputFileName pr = new PhredOutputFileName(name);
                if ( pr.isWriteFileFormat( PhredOutputFileName.FORMAT_OURS ))
                    return false;
                else
                     return true;
            }
        };

        File[] wrong_namingformat__files = sourceDir.listFiles(filter);
        if ( wrong_namingformat__files == null) return;
        for (int file_count = 0; file_count < wrong_namingformat__files.length; file_count++)
        {
             m_error_messages.add("File "+wrong_namingformat__files[file_count].getName() +" has wrong format.");
            moveFile(wrong_namingformat__files[file_count], wrongformatfiles, m_isError_directory_exists);
        }

    }

    //retunr destination directory name if file OK
    //otherwise null
    private String isWriteNamingFormat(File traceFile, String wrongformatfiles,
    String emptysamples_directory )
    {
        //check for file format
        PhredOutputFileName pr = new PhredOutputFileName(traceFile.getName());
        if ( pr.isWriteFileFormat( PhredOutputFileName.FORMAT_OURS ))
        {
            //check if control - put them into error directory
            if ( pr.getCloneidNumber() == 0 && pr.getSequenceidNumber() == 0)
            {

                moveFile(traceFile, wrongformatfiles, m_isError_directory_exists);
                return null;
            }
            // check if empty well
            else if (pr.getCloneidNumber() == 0 && pr.getSequenceidNumber() != 0)
            {
                moveFile(traceFile, emptysamples_directory, m_isEmptySamples_directory_exists);
                return null;
            }
            //file ok
          //  destination_directory = pr.getSequenceid() +File.separator+pr.getCloneid();
            return pr.getSequenceid() +File.separator+pr.getCloneid();
        }
        else
        {
            m_error_messages.add("File "+traceFile +" has wrong format.");
            moveFile(traceFile, wrongformatfiles, m_isError_directory_exists);
            return null;

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
            FileOperations.moveFile(moving_file, new File(directory_move_to  +File.separator+ moving_file.getName()), true, true);
        }
        catch(Exception e)
        {
            m_error_messages.add("Cannot move file "+moving_file.getName());
        }

    }
}
