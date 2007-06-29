/*
 * Tester.java
 *
 * Created on June 21, 2007, 11:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;

/**
 *
 * @author htaycher
 */
public class Tester {
    
    /** Creates a new instance of Tester */
   
    
     public static void main1(String[] args)
  {
  // System.out.println();
        FileInputStream input = null;
        FileOutputStream output = null;
        String f_name = null;
        FileStructure[]             file_structures = null;
        String line = null;BufferedReader in = null;
        BufferedWriter out = null;
        try
        {
            f_name="E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\cumulative_rearrayed_plates_20070601.txt";
            String f_outname = "E:\\HTaycher\\HIP projects\\MGC\\Submissions\\NewMGC\\new_plates.txt";
            input = new FileInputStream(f_name);
            in = new BufferedReader(new InputStreamReader(input));
            out = new BufferedWriter(new FileWriter(f_outname));
            while((line = in.readLine()) != null) 
            {
                boolean res = (line.indexOf("IRQM") != -1 ||
line.indexOf("IRBO") != -1 || line.indexOf("IRCG") != -1 ||
line.indexOf("IRCV") != -1 || line.indexOf("IRCW") != -1 ||
line.indexOf("OCAB") != -1 || line.indexOf("OCAC") != -1 ||
line.indexOf("IRQL") != -1);
                if (res)
                {
                       System.out.println(line);
                       out.write(line +"\n");
                       out.flush();
                }
            }
             output.close();
            input.close();
            
         /*   FileToRead    rf=  new FileToRead( input, true,
               FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION );
            rf.readFile(true,true,true);
            rf.getFileType();
            FileMapParser SAXHandler = new FileMapParser();
            SAXParser parser = new SAXParser();
            
            parser.setContentHandler(SAXHandler);
            parser.setErrorHandler(SAXHandler);
            f_name = "C:\\Projects\\FLEX\\flex\\docs\\mgc_submission_map.xml";
            InputSource in = new InputSource(new FileInputStream(f_name));
            String featureURI = "http://xml.org/sax/features/string-interning";
            parser.setFeature(featureURI, true);
            parser.parse(in);
        
            file_structures = SAXHandler.getFileStructures();*/
            

        }
        catch(Exception e)
        {}
        System.exit(0);
    }
     
  public static void main(String[] args)
  {
        FileInputStream input = null;
        String f_name = null;
        FileStructure[]             file_structures = null;
         try
        {
               FileMapParser SAXHandler = new FileMapParser();
            SAXParser parser = new SAXParser();
            
            parser.setContentHandler(SAXHandler);
            parser.setErrorHandler(SAXHandler);
            f_name = "C:\\Projects\\FLEX\\flex\\docs\\test_mgc_submission_map.xml";
            InputSource in = new InputSource(new FileInputStream(f_name));
            String featureURI = "http://xml.org/sax/features/string-interning";
            parser.setFeature(featureURI, true);
            parser.parse(in);
        
            file_structures = SAXHandler.getFileStructures();
            
            f_name = "E:\\HTaycher\\HIP projects\\MGC\\Submissions\\test.txt";
             input = new FileInputStream(f_name);
          
             FileReader freader = new FileReader();
             freader.setNumberOfWellsInContainer(96);
             freader.readFileIntoSetOfObjects( input, true,
            FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION, 
                    true, true,file_structures[ FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION]) ;
            
         /*   FileToRead    rf=  new FileToRead( input, true,
               FileStructure.FILE_TYPE_ONE_FILE_SUBMISSION );
            rf.readFile(true,true,true);
            rf.getFileType();
            FileMapParser SAXHandler = new FileMapParser();
            SAXParser parser = new SAXParser();
            
            parser.setContentHandler(SAXHandler);
            parser.setErrorHandler(SAXHandler);
            f_name = "C:\\Projects\\FLEX\\flex\\docs\\mgc_submission_map.xml";
            InputSource in = new InputSource(new FileInputStream(f_name));
            String featureURI = "http://xml.org/sax/features/string-interning";
            parser.setFeature(featureURI, true);
            parser.parse(in);
        
            file_structures = SAXHandler.getFileStructures();*/
            

        }
        catch(Exception e)
        {}
        System.exit(0);
    }
}
