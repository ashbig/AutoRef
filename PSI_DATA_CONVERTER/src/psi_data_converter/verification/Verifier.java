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
   
     
     
     
    /** Creates a new instance of Verifier */
    public Verifier(String  file_name    ) 
    { 
        m_file_name =  file_name ; 
    }
    
    public int defineColumnNumber(String line, String header)
    {
        String[] items = line.split("\t");
        for (int count = 0; count <   items.length; count++)
        {
            if ( items[count].equalsIgnoreCase(header)) return  count;
        }
        return -1;
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
     
     
     private String         putStringArrayInString(String[] tmp, String delim, boolean isDelimAfterLast)
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
}
