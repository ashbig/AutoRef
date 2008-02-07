/*
 * ZipFileProcessor.java
 *
 * Created on January 31, 2008, 1:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.filemanagment;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.apache.tools.tar.*;
/**
 *
 * @author htaycher
 */
public class ZipFileProcessor
{
    
    /** Creates a new instance of ZipFileProcessor */
    public ZipFileProcessor() {
    }
    
   


  public static final void copyInputStream(InputStream in, OutputStream out)
  throws IOException
  {
    byte[] buffer = new byte[1024];
    int len;

    while((len = in.read(buffer)) >= 0)
      out.write(buffer, 0, len);

    in.close();
    out.close();
  }

    public   void       unZipFiles(String dirname, String output_dir) throws IOException
    {
         ZipFile zipFile = null; ZipEntry zentry ;
         File[] files_in_dir;
         Enumeration<ZipEntry> fentries;
   
         try
         {
             File cur_dir = new File(dirname);
             files_in_dir = cur_dir.listFiles();
             for ( File fzip: files_in_dir)
             {
                 if (fzip.getName().contains(".zip"))
                 {
                    processZipFile(fzip, output_dir);
                 }
                 else if (fzip.getName().contains(".tar"))
                 {
                     processTarFile(fzip, output_dir);
                 }
                 
             }
             if (zipFile != null ) zipFile.close();
        } catch (IOException ioe)
        {
          System.err.println("Unhandled exception:");
          ioe.printStackTrace();
          throw new IOException("Cannot unzip file "+ ioe.getMessage());
        }
  
    }
    
    private void        processZipFile(File fzip, String output_dir) throws IOException
    {
         ZipEntry zentry ;
         ZipFile zipFile = new ZipFile(fzip);
         Enumeration<ZipEntry> fentries = (Enumeration<ZipEntry>)zipFile.entries();
         while(fentries.hasMoreElements())
         {
             zentry = fentries.nextElement();
             if (!zentry.isDirectory())
             {
                 copyInputStream(zipFile.getInputStream(zentry),
                    new BufferedOutputStream(new FileOutputStream( output_dir + File.separator+ zentry.getName())) );
             }
         }
    }
    
    private void        processTarFile(File fzip, String outputdir) throws IOException
    {
        File destPath ;FileOutputStream fout;
        TarEntry[] tarfiles ;
        TarInputStream tin = new TarInputStream(new FileInputStream(fzip));
         TarEntry tarEntry = tin.getNextEntry();
          if(new File(outputdir).exists())
          {
            while (tarEntry != null)
            {
                 if(!tarEntry.isDirectory())
                {
                    String dpath =  tarEntry.getName();
                   if (dpath.contains ("/" ))
                       dpath = outputdir + File.separator + dpath.substring(dpath.indexOf("/")+1);
                   destPath = new File(dpath);
                   if(!tarEntry.isDirectory())
                    {
                       //does not work with out this
                       System.out.println(destPath);
                       FileWriter ffo =                     new FileWriter(destPath);
                       //ffo.write("ll");
                       ffo.close();
                       fout = new FileOutputStream( destPath  );
                       tin.copyEntryContents(fout);
                       fout.close();
                    }
                }
                tarEntry = tin.getNextEntry();
            }
           
        }
       
    }


     public static void main(String[] args) 
    {
         String inputdir="C:\\tmp\\test";
         String outputdir =  "C:\\tmp\\test1";
         ZipFileProcessor zpr = new ZipFileProcessor();
         try
         { zpr.unZipFiles(inputdir,outputdir);
         }catch(Exception e){}
     }
}

