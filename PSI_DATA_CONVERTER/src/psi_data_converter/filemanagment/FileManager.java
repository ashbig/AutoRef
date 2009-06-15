/*
 * FileManager.java
 *
 * Created on February 5, 2008, 9:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.filemanagment;

import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class FileManager {
    
    /** Creates a new instance of FileManager */
    public FileManager() {
    }
    
    
    public static String             concatenateFiles(ArrayList er_messages,
            String outputdir, String file_name_type, String new_file_header,
            boolean isDeleteOriginal) throws Exception
    {
        boolean isFirstLine = true;
        
     System.out.println(file_name_type);
        BufferedReader output = null;BufferedWriter input = null;
        String line ; String file_header = null;
        try
        {
            new_file_header= new_file_header.replaceAll(":","\t");
            File cur_dir = new File(outputdir);
            NameTypeFileFilter ffilter = new NameTypeFileFilter(file_name_type);
            File[] cur_dir_files = cur_dir.listFiles(ffilter);
            
            input = new BufferedWriter(new FileWriter(outputdir + File.separator + file_name_type +".txt", true));
            input.write(new_file_header);input.write("\n");
            for ( File cfile : cur_dir_files)
            {
                output = new BufferedReader(new FileReader(cfile));
                while ( (line = output.readLine() ) != null)
                {
                    if ( isFirstLine )  
                    {
                        if (file_header == null) file_header= line;
                        isFirstLine = false;
                        // verify that all headers are the same 
                        er_messages.add(line);
                        if (!line.equalsIgnoreCase(file_header))
                        {
                            er_messages.add("File header is different "+ cfile.getName() +"\n"+file_header +" \n"+line);
                        }
                        continue;
                    }
                   input.write(line); input.write("\n");//System.getProperty("line.separator"));
                }
                input.flush();
                output.close();
                isFirstLine = true;
            }
            if ( isDeleteOriginal )
            {
                for ( File cfile : cur_dir_files)
                {
                    cfile.delete();
                }
            }
            return file_header;
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot concatenate files " + file_name_type + e.getMessage());
        }
        finally
        {
            if (input!= null)input.close();
            if (output != null) output.close();
        }
    }
    
    
    public static void deleteDuplicatedRecords(ArrayList er_messages, File fl)throws Exception
    {
        BufferedReader input = null;BufferedWriter output = null;
        String line ;  ArrayList< String> temp = new ArrayList< String>();
       
        try
        {
            input = new BufferedReader(new FileReader(fl));
            temp.add(input.readLine()+"\n");
            while ( (line = input.readLine() ) != null)
            {
              if ( !temp.contains(line+"\n")) temp.add(line+"\n");//System.getProperty("line.separator"));
            }
         }
        catch(Exception e)
        {   
           throw new Exception ("Cannot read files " + fl.getName() + e.getMessage());
   
        }  
        finally        {  if (input!= null)input.close();}
        try
        {
            output = new BufferedWriter(new FileWriter(fl));
            for(String record : temp)
            {
                output.write(record);
            }
            output.flush();
        }
        catch(Exception e)
        {
             throw new Exception  ("Cannot write file " +  fl.getName() + e.getMessage());
        }
        finally        {            if (output != null) output.close();  
        }
       
    }
    
     public static String             concatenateFiles(ArrayList er_messages,
            ArrayList<String> file_names,
            boolean isDeleteOriginal) throws Exception
    {
        BufferedReader output = null;BufferedWriter input = null;
        String line ;  String cur_appending_file_name ="" ;
        
            if ( file_names == null || file_names.size() ==0) return "";
            if ( file_names.size() == 1) return file_names.get(0);
            
        try
        {
            input = new BufferedWriter(new FileWriter(file_names.get(0), true));
        }
        catch(Exception e)
        {           
            er_messages.add ("Cannot open file " +  file_names.get(0) + e.getMessage());  
            return "";
        }  
        try
        {
            file_names.remove(0);
            for ( String cur_file_name : file_names)
            {
                cur_appending_file_name = cur_file_name;
                output = new BufferedReader(new FileReader(cur_file_name));
                while ( (line = output.readLine() ) != null)
                {
                   input.write(line); input.write("\n");//System.getProperty("line.separator"));
                }
                input.flush();
                output.close();
                if ( isDeleteOriginal )
                {
                    File cfile = new File(cur_file_name);cfile.delete();
                }
            }
        }
        catch(Exception e)
        {
            er_messages.add ("Cannot concatenate file " +  cur_appending_file_name + e.getMessage());
        }
        finally
        {
            if (input!= null)input.close();
            if (output != null) output.close();    
        }
       
        return file_names.get(0);
        
        
    }
    
     
     
     public static void createCloneAuthorFile(String authorfile_name, String location_fn, 
             String cloneauthor_fn, String fheader) throws Exception
    {
          ArrayList authors = readAuthorFile( authorfile_name);
          if ( authors == null || authors.size() == 0)
              throw new Exception ("No author information have been read");
          writeCloneAuthorFile(authors,  location_fn,  cloneauthor_fn, fheader);
     }
    
    private static ArrayList readAuthorFile(String authorfile_name) throws Exception
    {
         String[] items ;
        BufferedReader output = null ; 
        String line ; ArrayList authors = new ArrayList();
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(authorfile_name));
            output.readLine();// read header
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                authors.add(items[0]);
            }
            output.close();
            return authors;
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read author info file " + authorfile_name);
        }
        finally
        {
            if (output != null) output.close();
        }
    }
    
    private static void  writeCloneAuthorFile(ArrayList<String> authors, String location_fn, 
            String cloneauthor_fn, String fheader) throws Exception
    {
        BufferedReader output = null ; BufferedWriter input = null;
        String line ;  String[] items ;
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(location_fn));
            input = new BufferedWriter(new FileWriter(cloneauthor_fn));
            input.write(fheader);input.write("\n");
            output.readLine();// read header
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                for (String author : authors)
                {
                    input.write(items[0] + "\t"+ author );
                    input.write("\n");
                }
            }
            input.flush();
            input.close();
            output.close();
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot write author info file " + cloneauthor_fn);
        }
        finally
        {
            if (output != null) output.close();
            if (input != null) input.close();
        }
    }
    
    
    public static void writeFile(List<String> fdata, String file_name,
            String header, boolean isAppend) throws IOException
    {
        BufferedWriter fout = new BufferedWriter(new FileWriter ( file_name, isAppend));
        if ( header != null){fout.write( header );fout.write("\n");}
        for (String item : fdata)
        {
             fout.write( item.trim() );fout.write("\n");
             fout.flush();
        }
        fout.flush();
        fout.close();

    }
    
    
     public static List             readFileIntoStringArray(String  filename, int[] column_numbers ,
             boolean isHeader) throws Exception
    {
            return    readFileIntoStringArray(  filename,  column_numbers, "\t", isHeader);
     }
    public static List             readFileIntoStringArray(String filename, int[] column_numbers, 
            String delim, boolean isHeader)throws Exception
    {
        List file_values = new ArrayList();
        BufferedReader input = null ; int count = 0;
        String line ;  String[] items ; String[] row_values;
        // read author info and stor author 
        try
        {
            input = new BufferedReader(new FileReader(filename));
            if (  isHeader)input.readLine() ;
            while ( (line = input.readLine() ) != null)
            {
                items = line.split("\t");
                row_values = new String[column_numbers.length];
                file_values.add(row_values);count = 0;
                if ( items.length < column_numbers.length) continue;
                for (int column_number : column_numbers)
                {
                    row_values[count++] = items[column_number].trim();
                }
            }
            input.close();
            return file_values; 
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read file " + filename);
        }
        finally
        {
             if (input != null) input.close();
        }
        
    }
    
    
    
    
    
     public static int  deleteEmptyEntries(String file_name, int column_to_check_for_empty_value,
             String definition_of_empty_value, List er_messages) throws Exception
    {
        ArrayList none_empty_records = readNonEmptyRecords( file_name,  column_to_check_for_empty_value,
              definition_of_empty_value);
        if (none_empty_records != null && none_empty_records.size() > 1)
        {
            String header = (String) none_empty_records.get(0);
            none_empty_records.remove(0);
            writeFile(none_empty_records,  file_name,  header, false);
            return 1;
        }
        else
        {
            er_messages.add("File "+ file_name +" does not have none empty records.");
            return 0;
        }
      
     }
    
    public static ArrayList readNonEmptyRecords(String file_name,
            int column_to_check_for_empty_value,
             String definition_of_empty_value) throws Exception
    {
        String[] items ;
        BufferedReader output = null ; 
        String line ; ArrayList records = new ArrayList();
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(file_name));
            records.add( output.readLine()) ;// read header
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                if (! items[column_to_check_for_empty_value].equalsIgnoreCase(definition_of_empty_value))
                    records.add(line);
            }
            output.close();
            return records;
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read file to check for empty records" +  file_name);
        }
        finally
        {
            if (output != null) output.close();
        }
    }
    
    public static void main(String[] args) 
    {
       psi_data_converter.util.SubmissionProperties     subprop = 
               psi_data_converter.util.SubmissionProperties.getInstance();
         
           String outputdir =  subprop.getProperty("FILES_OUTPUT_DIR") ;
       psi_data_converter.verification.CloneInfoVerificator cinfo = 
               new psi_data_converter.verification.CloneInfoVerificator(outputdir+File.separator+ subprop.getProperty("FILE_CLONE_INFO_NAME").trim() +".txt");
       String linker_file =  subprop.getProperty("LINKERS_FILE_LOCATION") ;
      List linker_values = null;
       int[] column_values = {0, 1};
  try
  {
       FileManager.readFileIntoStringArray(linker_file,column_values, true);
    }catch(Exception e){}
    }
}
