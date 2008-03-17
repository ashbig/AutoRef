/*
 * Verifier.java
 *
 * Created on February 5, 2008, 2:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
    
package psi_data_converter.verification;

import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public abstract class Verifier 
{
    
    
    public enum ENUM_MATH {
        NONE, PLUS, MINUS, MULTIPLY, DEVIDE};
    public enum ENUM_REPLACE_TYPE{
        REPLACE_STRING, REPLACE_WITH_CALCULATION_INT
    }  ;  
        
        //public changeNumber()
    
    protected String          m_file_name ;
    public    String          getFileName(){ return m_file_name;}
     
     
      public Verifier(){}
    /** Creates a new instance of Verifier */
    public Verifier(String  file_name    ) 
    { 
        m_file_name =  file_name ; 
    }
    
    public static int defineColumnNumber(String line, String header)
    {
        String[] items = line.split("\t");
        for (int count = 0; count <   items.length; count++)
        {
            if ( items[count].equalsIgnoreCase(header)) return  count;
        }
        return -1;
    }
    
    
    public boolean      verifyVocabularyField(List<String[]> records, 
            String column_header,  String header, String[] field_voc,
             boolean isCaseSensitive, ArrayList<String> er_messages)
    {
        int column_number =  defineColumnNumber(column_header,  header);
        return verifyVocabularyField(records,  column_number,  field_voc ,isCaseSensitive, er_messages);
    }
    
     public boolean      verifyVocabularyField(List<String[]> records, 
            int column_number,  String[] field_voc, boolean isCaseSensitive ,
             ArrayList<String> er_messages)
    {
         String field_value ; boolean isFieldVerified = false;
         boolean result = true;
         for( String[] record : records )
         {
             field_value = record[column_number];
             isFieldVerified = false;
             for ( String v_value : field_voc )
             {
                 if ( ( ! isCaseSensitive &&   field_value.equals(v_value))
                 ||  (  isCaseSensitive && field_value.equalsIgnoreCase(v_value)))
                 {
                     isFieldVerified = true;
                     break;
                 }
             }
             if ( ! isFieldVerified ) 
             {
                 result = false;
                 er_messages.add("Field value wrong: "+isFieldVerified+" record: "+record.toString());
             }
         }
        return result;
    }
    
    
    public void         insertString(
            String apendtext, String header, 
            String header_of_insert_column_value, boolean isInsertAnotherColumnValue)throws Exception
    {
        BufferedReader output = null ; 
        BufferedWriter input = null;
        String tmpfile = m_file_name+"tmp.txt";
        String line ;  String[] items ; int column_line = -1 ;
        StringBuffer sbuf = new StringBuffer();
        int number_of_insert_column_value = -1;
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(m_file_name));
            input = new BufferedWriter(new FileWriter(tmpfile));
            line = output.readLine();
            column_line = defineColumnNumber(line,  header);
            number_of_insert_column_value = defineColumnNumber(line, header_of_insert_column_value);
           
            if ( column_line < 0  ) return;
             input.write( line ); input.write( "\n" );
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                sbuf = new StringBuffer();
                if (!isInsertAnotherColumnValue && apendtext != null)// insert
                {
                    if (line.lastIndexOf("\t") == line.length() - 1)
                    {
                        sbuf.append(line +  apendtext);
                    }
                    else
                        sbuf.append(line +"\t"+ apendtext);
                }
                            
                else
                {
                    for (int count = 0; count < items.length; count++)
                    {
                        if ( count == column_line && isInsertAnotherColumnValue && apendtext == null)
                        {
                           sbuf.append( items[number_of_insert_column_value].trim() +"\t");
                        }
                        else if (count == column_line && isInsertAnotherColumnValue && apendtext != null)
                        {
                           sbuf.append( apendtext +"\t");
                        }
                        //replace column value
                        else if (count == column_line && ! isInsertAnotherColumnValue && apendtext != null)
                        {
                           sbuf.append( apendtext +"\t");
                           continue;
                        }
                        sbuf.append(items[count].trim()+"\t");
                    }
                }
                input.write( sbuf.toString() );
                input.write("\n");
               
            }
            input.flush();
            input.close();
            output.close();
            
            
            java.nio.channels.FileChannel ic = new FileInputStream(tmpfile).getChannel();
            java.nio.channels.FileChannel oc = new FileOutputStream(m_file_name).getChannel();
            ic.transferTo(0, ic.size(), oc);
            ic.close();
            oc.close();
            
            // delete tmpfile
            File tmpf = new File(tmpfile);
            tmpf.delete();
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot modify file " + m_file_name +" \t"
                    +"insertString("+ m_file_name+" , "+
             apendtext+" , "+header+" , "+ isInsertAnotherColumnValue+" , "+header_of_insert_column_value+")");
        }
        finally
        {
            if (output != null) output.close();
            if (input != null) input.close();
        }
    }
    
    
     public void         replaceStrings(
            String header, List <String[]> old_new_values )throws Exception
     {
          replaceStrings(    header,  old_new_values,   0,  ENUM_MATH.NONE, ENUM_REPLACE_TYPE.REPLACE_STRING);
  
     }
     
      public void         replaceIntStringsValue(
            String header,  int change_value,  ENUM_MATH action )throws Exception
     {
         replaceStrings(     header, null, change_value,   action,  ENUM_REPLACE_TYPE.REPLACE_WITH_CALCULATION_INT);
     }
    
  
     public void         replaceStrings(
            String header, List <String[]> old_new_values,
             int change_value,  ENUM_MATH action,
             ENUM_REPLACE_TYPE replacetype)throws Exception
    {
        BufferedReader output = null ; 
        BufferedWriter input = null;
        String tmpfile = m_file_name+"tmp.txt";
        String line ;  String[] items ; int column_line = -1 ;
        int number_of_insert_column_value = -1;
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(m_file_name));
            input = new BufferedWriter(new FileWriter(tmpfile));
            line = output.readLine();
            column_line = defineColumnNumber(line,  header);
            if ( column_line < 0  ) return;
            
            input.write( line ); input.write( "\n" );
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
              
                switch ( replacetype )
                {
                    case REPLACE_STRING: 
                    {
               
                        for ( String[] old_new_value : old_new_values )
                        {
                            if ( items[column_line].equalsIgnoreCase(old_new_value[1]))
                            {
                                items[column_line] = old_new_value[0];
                                break;
                            }
                        }
                        break;
                    }
                    case REPLACE_WITH_CALCULATION_INT:
                    {
                        int old_value = Integer.parseInt( items[column_line] );
                        items[column_line] = String.valueOf( calculateNewInt( old_value,  change_value,   action));
                        break;
                    }
                }
                line = putStringArrayInString(items, "\t", false);
                input.write( line);
                input.write("\n");
                
               
            }
            input.flush();
            input.close();
            output.close();
            
            
            java.nio.channels.FileChannel ic = new FileInputStream(tmpfile).getChannel();
            java.nio.channels.FileChannel oc = new FileOutputStream(m_file_name).getChannel();
            ic.transferTo(0, ic.size(), oc);
            ic.close();
            oc.close();
            
            // delete tmpfile
            File tmpf = new File(tmpfile);
            tmpf.delete();
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot modify file (replaceString)" + m_file_name  );
        }
        finally
        {
            if (output != null) output.close();
            if (input != null) input.close();
        }
    }
     
     
     public static  String         putStringArrayInString(String[] tmp, String delim, boolean isDelimAfterLast)
     {
          StringBuffer sbuf = new StringBuffer() ;
          for (int count = 0; count < tmp.length; count++)
          {
              sbuf.append(tmp[count]);
              if (delim != null ) 
              {
                  if ( ! (isDelimAfterLast && count == tmp.length - 1))
                      sbuf.append(delim);
              }
          }
          return sbuf.toString();
     
       
     }
     
     private int         calculateNewInt(int old_value, int change_value,  ENUM_MATH action)
     {
         switch (action)
         {
             case PLUS: return old_value + change_value;
             case MINUS:  return old_value - change_value;
             case  MULTIPLY:  return old_value * change_value;
             case DEVIDE:  return old_value / change_value;
             default: return -1;
             
         }
     }
     
     public HashMap<String,String> readDataIntoHash(String file_name, String key_column_name ,
             String value_column_name , boolean isRaiseFlagIfDuplicates)
             throws Exception
     {
          BufferedReader output = null ; 
          HashMap<String,String> result = new HashMap();
         String line ;  String[] items ; int key_column = -1 ;
        int value_column = -1;
        // read author info and stor author 
        try
        {
            output = new BufferedReader(new FileReader(file_name));
            line = output.readLine();//header
            key_column = defineColumnNumber(line,  key_column_name);
            value_column = defineColumnNumber(line,  value_column_name);
      
            if ( key_column < 0  || value_column < 0) 
                throw new Exception("Cannot define key or value columns");
            
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                if (isRaiseFlagIfDuplicates && result.containsKey(items[key_column]))
                    throw new Exception("Dublicate  key: "+items[key_column]);
                result.put(items[key_column], items[value_column]);
            }
            output.close();
            return result;
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read file " + file_name  );
        }
        finally
        {
            if (output != null) output.close();
        }
     }
     
     
     public boolean verifyAllCloneIDDescribed(List<String[]> records, int cloneid_column_number,
             HashMap<String,String> clone_ids)
     {
         if ( records.size() != clone_ids.size()) return false;
         for( String[] record : records)
         {
             if( clone_ids.get(record[cloneid_column_number]) == null)
                    return false;
         }
         return true;
     }
     
     
     
     public void         replaceStrings(
            List<String[]> records, String file_header, String column_header,
             List <String[]> old_new_values , ArrayList<String> er_messages )
     {
          replaceStrings(   records, file_header,  column_header, old_new_values, 
                  0,  ENUM_MATH.NONE, ENUM_REPLACE_TYPE.REPLACE_STRING, er_messages);
  
     }
     
      public void         replaceIntStringsValue(
            List<String[]> records,  String file_header, String column_header, 
              int change_value,  ENUM_MATH action, ArrayList<String> er_messages )
     {
         replaceStrings(    records,  file_header,  column_header, null, 
                 change_value,   action,  ENUM_REPLACE_TYPE.REPLACE_WITH_CALCULATION_INT,
                 er_messages);
     }
    
  
     public void         replaceStrings(
             List<String[]> records, String file_header, String column_header, 
             List <String[]> old_new_values,
             int change_value,  ENUM_MATH action,
             ENUM_REPLACE_TYPE replacetype, ArrayList<String> er_messages)
    {
      
        int number_of_update_column =  defineColumnNumber(file_header,  column_header);
        if ( number_of_update_column < 0  ) 
        {
            er_messages.add("Cannot define column number");
            return;
        }
            
        for (String[] record : records )     
        {
                switch ( replacetype )
                {
                    case REPLACE_STRING: 
                    {
               
                        for ( String[] old_new_value : old_new_values )
                        {
                            if ( record [number_of_update_column].equalsIgnoreCase(old_new_value[1]))
                            {
                                record[number_of_update_column] = old_new_value[0];
                                break;
                            }
                        }
                        break;
                    }
                    case REPLACE_WITH_CALCULATION_INT:
                    {
                        int old_value = Integer.parseInt( record[number_of_update_column] );
                        record[number_of_update_column] = String.valueOf( calculateNewInt( old_value,  change_value,   action));
                        break;
                    }
                }
            }
      
    }
     
     
       public static  void         appendString( String apendtext,  List<String[]> records)
       {
           for ( String[] record : records)
           {
               record [record.length - 1] += apendtext;
           }
       }
 
     public static  void  appendValueOfColumn( int column_number,  List<String[]> records, 
             String delim, boolean isDelim)
       {
         String apendtext ="";
           for ( String[] record : records)
           {
               if ( isDelim)apendtext=delim;
               apendtext += record[column_number];
               record [record.length - 1] += apendtext;
               apendtext="";
           }
       }
    
  
    
}
