/*
 * FileManipulation.java
 *
 * Created on April 18, 2003, 3:40 PM
 */

package edu.harvard.med.hip.bec.file;

/**
 *
 * @author  htaycher
 */
import java.io.*;
import edu.harvard.med.hip.utility.*;

public class FileOperations
{
    
        /**
     * Buffer size when reading from input stream.
     */
    private final static int BUFFER_SIZE = 1024;
    private final static boolean     m_isSameComputer = false;
     {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
             m_isSameComputer = true;
        else
            m_isSameComputer = false;
    }
    public static void createDirectory(String directory_path, boolean exsits_exit)   throws Exception
    {
        File directory = new File(directory_path);
        if (directory.exists())
        {
            if (directory.isDirectory())
            {
                if (exsits_exit)
                    return;
                else
                {
                    throw (new IllegalArgumentException(
                    "The given directory already exists."));
                }
            }
        }
        try
        {
            directory.mkdirs();
        }
        catch(Exception e)
        {
            throw (new Exception("The given directory name could not be created."));
        }
    } // createDirectoryOrFail
    
    
    
    
    //ok for small files : write functions for large ones using System
    public static void copyFile(File source_file, File destination_file,  boolean overwrite) throws Exception
    {
        moveFile(source_file,  destination_file, overwrite, false);
    } // copyFileToFile
    
    
    
    public static void moveFile(File source_file, File destination_file)
    throws Exception
    {
        moveFile(source_file,  destination_file, false, true);
    } // moveFileToFile
    
    /**
     * Move a file from one location to another.  An attempt is made to rename
     * the file and if that fails, the file is copied and the old file deleted.
     *
     * @param from file which should be moved.
     * @param to desired destination of the file.
     * @param overwrite If false, an exception will be thrown rather than overwrite a file.
     * @throws IOException if an error occurs.
     */
    public static void moveFile(File from, File to, boolean overwrite, boolean mode_remove) throws IOException
    {
        if (to.exists())
        {
            if (overwrite)
            {
                if (!to.delete())
                {
                    throw new IOException( "Cannot delete file");
                }
            }
            else
            {
                throw new IOException("File with this name already exist.");
            }
        }
        
        if (m_isSameComputer)
        {
            if (from.renameTo(to)) return;//on the same machine
        }
        else
        {
        
            InputStream in = null;
            OutputStream out = null;
            try
            {
                in = new FileInputStream(from);
                out = new FileOutputStream(to);
                copy(in, out);
                out.flush();
                in.close();
                in = null;
               
                out.close();
                out = null;
                if (mode_remove && !from.delete())
                {
                    throw new IOException("Cannot delete original file");
                }
            } finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    } catch (IOException ignore)
                    {
                    }
                    in = null;
                }
                if (out != null)
                {
                    try
                    {
                        out.flush();
                        out.close();
                    } catch (IOException ignore)
                    {
                    }
                    out = null;
                }
            }
        }
    }
    
    /**
     * Copy the data from the input stream to the output stream.
     *
     * @param in data source
     * @param out data destination
     * @throws IOException in an input or output error occurs
     */
    private static void copy(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while((read = in.read(buffer)) != -1)
        {
            out.write(buffer);
        }
    }
    
    //__________________________________________________________________________
    public static void main(String args[])
    
    {
        try
        {
            File sourceFile = new File("testfile.txt");
            File destinationpath = new File("testdir\\testfile.txt");
            moveFile(sourceFile,destinationpath);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    } // test
} // Filer



