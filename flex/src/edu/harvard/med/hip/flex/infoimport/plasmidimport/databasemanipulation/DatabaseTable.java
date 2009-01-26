/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation;

import java.util.*;
import java.io.*;
import java.sql.*;


import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author htaycher
 */
public class DatabaseTable {
    private String      m_name;
    private List<DatabaseColumn>    m_columns;
    
    
    public DatabaseTable(){m_columns=new ArrayList();}
    
    public DatabaseTable(String n){ m_name=n;m_columns=new ArrayList();}
    
    public String getName(){return m_name;}
    public void     setName(String v){ m_name=v;} 
    
    public List<DatabaseColumn> getColumns(){ return m_columns;}
    public void  setColumns(List<DatabaseColumn>  v){m_columns=v;}
    
    
     public DatabaseColumn getColumnByName(String name) 
     {
         if ( m_columns == null || m_columns.size() == 0) return null;
         for (DatabaseColumn column :m_columns)
         {
             if ( column.getName().toUpperCase().equals(name.toUpperCase()))
                 return column;
         }
         return null;
     }
    public void getStructure(Connection conn)throws Exception
    {
        if (m_name == null ) 
        {
             throw new Exception("Error occured while getting table structure: table name not set ");
        }
        if ( m_columns.size() != 0) return;
     
        String sql_table_structure=  "select COLUMN_NAME, DATA_TYPE,DATA_LENGTH,NULLABLE "
+" from USER_TAB_COLUMNS  where TABLE_NAME='"+m_name+"'";
         ResultSet rs = null;
        DatabaseColumn dc;
        try
        {
           rs = DatabaseTransactionLocal.executeQuery(sql_table_structure,conn);
           while(rs.next()) 
            {
                dc = new DatabaseColumn();
                dc.setDataType(rs.getString("DATA_TYPE"));
                dc.setIsNullable(rs.getString("NULLABLE").equals("N"));
                dc.setLength(rs.getInt("DATA_LENGTH"));
                dc.setName(rs.getString("COLUMN_NAME"));
                //System.out.println(dc.getName());
                m_columns.add(dc);
            }
           return;
        } catch (Exception sqlE) {
            throw new Exception("Error occured while getting table structure: "+
                    m_name+"\nSQL: "+sqlE.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    public void getContent(Connection conn)throws Exception
    {
        StringBuffer  columns = new StringBuffer();
        int c_count=0; ResultSet rs = null;
        if ( m_columns.size() == 0) getStructure(conn);
        for (DatabaseColumn cl : m_columns)
        {
            columns.append( "' ' || "+cl.getName()+" as item"+c_count);
            if ( c_count != m_columns.size()-1)
            columns.append(","); 
            c_count++;
        }
        String sql="select " +columns.toString()+" from "+m_name;
        c_count=m_columns.size();
        try
        {
           rs = DatabaseTransactionLocal.executeQuery(sql,conn);
           while(rs.next()) 
            {
                for (int cc=0; cc < c_count;cc++)
                {
                   m_columns.get(cc).getData().add(rs.getString("item"+cc) );
                }
            }
           return ;
        } catch (Exception sqlE) {
            throw new Exception("Error occured while getting table structure: "+
                    m_name+"\nSQL: "+sqlE.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
    }
    
    
    public DatabaseTable insertNewData(Connection conn, InputStream input)throws Exception
    {
        ArrayList<String[]> records = readFile(input);
        if ( m_columns.size() == 0) getStructure(conn);
       // map columns to records, format acording to datatype  & remove title record
        records = reformatInputData(records);
        String sql =  Algorithms.getString( records.get(0), ",");
        sql="insert into "+m_name+"(" +sql.substring(0, sql.length()-1)+
                ") values(";
        String temp="";int c_count=0;
        //remove header
        records.remove(0);
        
           for (String[] record : records)
           {
               try
                {   
                   for (String item : record)       {  temp += item+",";  }
                   temp=sql+temp.substring(0, temp.length()-1)+")";
                  // System.out.println(temp);
                    DatabaseTransaction.executeUpdate(temp,conn);
                    temp="";c_count=0;
                    
                 } 
                catch (Exception sqlE) 
                {
                    System.out.println(sqlE.getMessage());
                } 
           }
           
           records=null;
           return this;
       
        
    }
    
    private ArrayList<String[]> reformatInputData(ArrayList<String[]> records)
            throws Exception
    {
        // create array of collumns insink with records
        DatabaseColumn column=null;int c_count=0;
        ArrayList<String[]> result = new ArrayList<String[]>(records.size()-1);
        DatabaseColumn[] columns = new DatabaseColumn[records.get(0).length];
        
        for (String cname : records.get(0))
        {
            column = this.getColumnByName(cname.toUpperCase().trim());
            if ( column==null)throw new Exception ("Wrong file format: column not found. Table name "+m_name+", column name "+cname);
            columns[c_count++] = column;
         }
        boolean isHeaderRaw=true;
         for (String[] record : records)
        {
           if (isHeaderRaw){isHeaderRaw=false; continue;}
            for (int cc=0; cc < record.length;cc++)
            {
                column = columns[cc];
                // trim if needed 
                if ( column.getLength() < record[cc].length())
                    record[cc] = record[cc].substring(0, column.getLength());
                if ( OracleDefinitions.COLUMN_DATATYPE_STRING.isStringDataType(column.getDataType()))
                {
                    record[cc]="'"+record[cc]+"'";
                }
            }
           // result.add(new_record);
        }
        return records;
    }
    private ArrayList<String[]> readFile(InputStream input)throws Exception
    {
       BufferedReader  in =null;boolean isFirstLine =true;
       String line;ArrayList<String[]> result=new  ArrayList<String[]>();
       String temp[];int column_count=0;
       try
       {
           in = new BufferedReader(new InputStreamReader(input));
            while((line = in.readLine()) != null) 
            {
                if(isFirstLine){ m_name=line.trim();isFirstLine=false;}
                else
                {
                    temp=line.trim().split("\t");
                    if ( column_count==0)column_count=temp.length;
                    if (column_count!=temp.length)
                    {
                        throw new Exception("Wrong file format: check input file. Line: "+line);
                    }
                    result.add(temp);
                }
                
            }      
            in.close();            input.close();
            return result;
       }
        catch(Exception e)
        {
            throw new Exception ("Can not read file");
        }
    }
     public static void main(String args[]) 
    {
              edu.harvard.med.hip.flex.infoimport.plasmidimport.FLEXtoPLASMIDImporter pi = 
                      new edu.harvard.med.hip.flex.infoimport.plasmidimport.FLEXtoPLASMIDImporter();
             Connection plasmid_connection=null;
        
        try        {      
         //  edu.harvard.med.hip.flex.util.FlexProperties flexProps = 
        //           edu.harvard.med.hip.flex.util.StaticPropertyClassFactory.makePropertyClass("FlexProperties");
       
            plasmid_connection = DatabaseTransaction.getInstance().requestConnection();//pi.getPLASMIDConnection();
         
           
            
            DatabaseTable dt = new DatabaseTable(); 
            String cc = "C:\\bio\\test\\new_linker.txt";
            cc = "C:\\bio\\test\\clonename.txt";
            InputStream input = new FileInputStream(cc);
            dt.insertNewData(plasmid_connection, input );
            plasmid_connection.commit();  
        }
        catch(Exception e)    
        {   
            System.out.println(e.getMessage());
            System.exit(0);
        }

       
        System.exit(0);
    }
}
