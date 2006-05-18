//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * BioFormatsFile.java
 *
 * Created on May 10, 2005, 4:18 PM
 */

package edu.harvard.med.hip.bec.bioutil;



import java.io.*;
import java.util.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
/**
 *
 * @author  htaycher
 */
public class BioFormatsFile {
    
    /** Creates a new instance of BioFormatsFile */
    public BioFormatsFile() {
    }
    
    
       //Print the sequence cds to a file in a fasta format.
    public static String writeFASTAFile(String dirname,String text, String prefics, String id) throws BecUtilException
    {
        java.util.Date d = new java.util.Date();
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("MM_dd_yyyy");
        String fileName = dirname+prefics+id;//System.currentTimeMillis();
        try
        {
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(fileName+".in")));
            pr.print( ">"+id);
            pr.println(edu.harvard.med.hip.bec.bioutil.SequenceManipulation.convertToFasta(text));
            pr.close();
            
            return fileName;
        }catch (IOException e)
        {
            throw new BecUtilException("Cannot make query file for "+fileName+"\n"+e.getMessage());
        }
    }
    
    
     //Print the sequence cds to a file in a fasta format.
    public static ArrayList readFASTAFile(String file_name) throws BecUtilException
    {
        try
        {
            InputStream file_stream = new FileInputStream(file_name);
            return readFASTAFile( file_stream);
        }catch (IOException e)
        {
            throw new BecUtilException("Cannot parse FASTA file.\n"+e.getMessage());
        }
    }
    
     public static ArrayList readFASTAFile(InputStream file_stream) throws BecUtilException
    {
        BufferedReader fin = null;
        ArrayList items= null; ArrayList item = null;
        String line = null;
        String item_context = null;
        BaseSequence sequence = null;
        try
        {
            fin = new BufferedReader(new InputStreamReader(file_stream));
            while ((line = fin.readLine()) != null)
            {
                if ( line.startsWith(">"))
                {
                    if ( items == null )items = new ArrayList();
                  
                    if ( item_context != null) sequence.setText(item_context);item_context = "";
                    sequence = new BaseSequence();
                    sequence.setId( Integer.parseInt((String) line.substring(1).trim()));
                    items.add(sequence);
                }
                else
                {
                    item_context += line.trim();
                }
            }
            //for last element
            if ( item_context != null && item_context.length()> 0) sequence.setText(item_context);
            return items;
            
        }catch (IOException e)
        {
            throw new BecUtilException("Cannot parse FASTA file.\n"+e.getMessage());
        }
    }
     
     
     
     
      public static void main(String[] args)
      {
        // TODO code application logic here
        try
        {

            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
            sysProps.verifyApplicationSettings();
            File f = new File("C:\\Bio\\sequences.txt");
            InputStream input = new FileInputStream(f);
            ArrayList ar = BioFormatsFile.readFASTAFile(input) ;
           
        }
        catch(Exception e){}
      }
   
}
