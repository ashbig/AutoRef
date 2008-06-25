/*
 * ReportProperties.java
 *
 * Created on May 27, 2008, 4:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.report;

import edu.harvard.med.hip.flex.util.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class ReportProperties  extends FlexProperties 
{
    private HashMap <String, String[] >     m_column_order_per_report ;
    private ArrayList<String>   m_error_messages ;
    
    public final static String REPORT_COLUMN_ORDER_TYPE_FILE ="config/ReportColumnOrder.properties";

    private static ReportProperties pInstance = null;
     protected InputStream getInputStream() {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(REPORT_COLUMN_ORDER_TYPE_FILE));
    }
    
    /**
     * Gets the instance of systemproperties.
     *
     * @return the single SystemProperites instance.
     */
    public static ReportProperties getInstance() {
        if(pInstance == null) {
            pInstance = new ReportProperties();
        }
        return pInstance;
    } 
    
    
     public String[]          getColumnOrderForReport(String report_prefix) 
     {
         return m_column_order_per_report.get(report_prefix);
     }
     public boolean    verifyProperties()
    {
         m_error_messages= new ArrayList<String>();
         //verify was run already
         if (m_column_order_per_report != null) return true;
          ArrayList<String[]> report_column_order;
         String key_value; String[] key_description; String[] column_entry;
         HashMap<String, ArrayList >     column_order_per_report = new HashMap<String, ArrayList >();
         Enumeration en= pInstance.getKeys();
         for (  ; en.hasMoreElements() ;)
           {
                key_value = (String) en.nextElement();
                key_description = (String[]) key_value.split("-");
                if (key_description.length != 2)
                    m_error_messages.add("Key "+key_value+" has wrong format.");
                if (   column_order_per_report.get(key_description[0]) == null)
                {
                    report_column_order = new ArrayList<String[]>();
                    column_order_per_report.put(key_description[0], report_column_order);
                }
               report_column_order = column_order_per_report.get(key_description[0]);
               column_entry = new String[2];
               column_entry[0] = key_description[1].trim();
               column_entry[1] = ReportProperties.getInstance().getProperty(key_description[0]+"-"+key_description[1]);
               if ( column_entry != null) report_column_order.add(column_entry); 
         }
         m_column_order_per_report = new HashMap<String, String[] > ( column_order_per_report.size()) ;
         // put in processed order
          String[] column_order={};int column_number ;
         for ( String key : column_order_per_report.keySet() )
         {
             report_column_order = column_order_per_report.get(key);
             if ( m_column_order_per_report.get(key)== null)
             {
                 column_order = new String[ report_column_order.size()+1];
                 m_column_order_per_report.put(key, column_order);
              }
            /* for (   String[] column_def : report_column_order)
             {
                System.out.println(column_def+" "+column_def[0]+" "+column_def[1]);
             }*/
             for (   String[] column_def : report_column_order)
             {
                 try
                 {
                     column_number = Integer.valueOf( column_def[1].trim()) ;
                     //System.out.println(column_number+" "+column_def[0]);
                 }
                 catch(Exception e)
                 {
                     m_error_messages.add("Key "+key+"."+column_def[0]+" has wrong format.");
                     continue;
                 }
                 if (  column_number >  column_order.length-1 ||
                         column_order [ column_number ]!= null )
                 {
                     m_error_messages.add("Key "+key+"."+column_def[0]+" has wrong order.");
                 }
                 else
                 {
                     column_order[column_number] = column_def[0];
                 }
                 
             }
         }
         
        for (int i = 0; i < m_error_messages.size() ; i++)
        { System.out.println((String)m_error_messages.get(i));}
   
        if ( m_error_messages.size() > 0 ) return false;
        return true;
       
     }

     public static void main(String [] args) 
    {
        try
        {
            String dbname = null;String dbpath = null;
            FlexProperties sysProps =  ReportProperties.getInstance(  );
            ((ReportProperties)sysProps).verifyProperties();
            String[]   rep =        ((ReportProperties)sysProps).getColumnOrderForReport("G");
            for (String t: rep)
            { System.out.println(t);}
            System.out.println("C----");
             rep =        ((ReportProperties)sysProps).getColumnOrderForReport("C");
            for (String t: rep)
            { System.out.println(t);}
            System.out.println("A-----");
             rep =        ((ReportProperties)sysProps).getColumnOrderForReport("A");
            for (String t: rep)
            { System.out.println(t);}
        }
        catch(Exception e )
        {
        }
     }
}
