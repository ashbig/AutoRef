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
import java.util.*;
import edu.harvard.med.hip.utility.*;

public class FileOperations
{
    
        /**
     * Buffer size when reading from input stream.
     */
    private final static int BUFFER_SIZE = 1024;
    private static boolean      m_isSameComputer = false;
     {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
             m_isSameComputer = true;
        else
            m_isSameComputer = false;
    }
    
    public static boolean isFileExists(String file_name)
    {
        File fl = new File(file_name);
        return fl.exists();
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
    
    
    public static File writeFile(ArrayList file_entries, String title, String file_name) throws Exception
    {
        File fl = null;
        FileWriter fr = null;
        
        try
        {
            fl =   new File(file_name);
            fr =  new FileWriter(fl);
            fr.write(title);
            for (int count = 0; count < file_entries.size(); count++)
            {
                fr.write((String) file_entries.get(count)+"\n");
            }
            fr.flush();
            fr.close();
             return fl;
        }
        catch(Exception e)
        { 
            try { fr.close();}catch(Exception n){};
            throw new Exception ("Cannot create file "+ file_name); }
       
    }
    
    //ok for small files : write functions for large ones using System
    public static void copyFile(File source_file, File destination_file,  boolean overwrite) throws Exception
    {
        //moveFile(source_file,  destination_file, overwrite, false);
        if (destination_file.exists() && !overwrite)
             throw new IOException("File with this name already exist.");
       InputStream in = null;
       OutputStream out = null;
       try
       {
            in = new FileInputStream(source_file);
            out = new FileOutputStream(destination_file);
            copy(in, out);
            out.flush();    
            in.close();            in = null;
            out.close();       out = null;
        }
        catch(Exception e)
        {
           new IOException( "Cannot copy file");
        }
       finally
       {
            if (in != null) try { in.close();  } catch (IOException ignore){}
            if (out != null)try  {     out.close();} catch (IOException ignore)  {  }
       }
       
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
        if (to.exists() && !overwrite)
             throw new IOException("File with this name already exist.");
        if (m_isSameComputer)
        {
            if (from.renameTo(to)) return;//on the same machine
        }
        else
        {
            InputStream in = null;           OutputStream out = null;
            try
            {
                in = new FileInputStream(from);  out = new FileOutputStream(to);
                copy(in, out);
                out.flush();
                in.close();                in = null;
                out.close();                out = null;
            } 
            catch(Exception e)
            {
                if (in != null)  try{ in.close(); } catch (IOException ignore){}
                if (out != null) try{ out.close(); } catch (IOException ignore){}
                throw new IOException("Cannot copy original file");
            }
            if (mode_remove)
            {
                try
                {
                    from.delete();
                }
                catch (Exception e)
                {
                    throw new IOException("Cannot delete original file");
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
            File sourceFile = new File("c:\\bio\\test.txt");
            File destination_file = new File("c:\\test.txt");
            //copyFile( sourceFile,  destination_file,   true) ;
            boolean overwrite = true;
            boolean mode_remove = true;
            moveFile(sourceFile, destination_file,overwrite,  mode_remove) ;
 
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    } // test
} // Filer



