/*
 * Renamer.java
 *
 * Created on August 28, 2003, 4:33 PM
 */

package edu.harvard.med.hip.bec.util;
import java.io.*;
/**
 *
 * @author  HTaycher
 */
public class Renamer
{
    
    /** Creates a new instance of Renamer */
   public static void main(String args[])
    {
        String dir = "F:\\pseudomonas_dump\\trace_dump";
         File sourceDir = new File(dir); //trace file directory

        File [] sourceFiles = sourceDir.listFiles();
        File cur_file = null;
        String dir_name = null;
         File destFile = null;//new File(String new_name);
        // source.renameTo(destFile);

        for (int count = 0; count <sourceFiles.length;count++)
        {
            try
            {
                cur_file = sourceFiles[count];
                //dir_name = sourceFiles[count].getPath();
                String filename = cur_file.getAbsolutePath();
                int start = -1;
                //remove primer name
                //3596-H12-14714-35375_SEQL-BR2.ab1 3596-H12-14714-35375_Gateway_FarF1F2.ab1
                if (filename.indexOf("SEQL-B") != -1)
                {
                    start = filename.indexOf("SEQL-B");
                    filename = filename.substring(0, start ) + filename.substring(start + "SEQL-B".length() );
                }
                else if (filename.indexOf("Gateway_FarF1") != -1)
                {
                    start = filename.indexOf("Gateway_FarF1");
                    filename = filename.substring(0, start ) + filename.substring(start + "Gateway_FarF1".length());
                }
                //replace - by _
                filename = Algorithms.replaceChar(filename,'-','_');
                //replace number by 0
                start = filename.indexOf(".ab") - 1;
                filename = filename.substring(0, start ) +"0"+ filename.substring(start +1);
                  destFile = new File(filename);
                cur_file.renameTo(destFile);
            }
            catch(Exception e){System.out.println(e.getMessage());}
            
        }
         System.exit(0);
    }
    
}
