/*
 * TranslationTable.java
 *
 * Created on July 17, 2007, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.bioinfo;


import java.util.*;
import java.io.*;

import edu.harvard.med.hip.flex.infoimport.*;
/**
 *
 * @author htaycher
 */
public class TranslationTable 
{
    public static final  String                AAs= "AAs" ;
    public static final String                 Starts = "Starts" ;
    public static final String                 Base1 = "Base1";  
    public static final String                 Base2 = "Base2"; 
    public static final String                 Base3 = "Base3";         
    
    // move to System properties
    private static final String TR_TABLE_FILE = "config/bio_amino_acid_translation_table.properties";
    private  static  Hashtable             i_tanslation_tables = null;
  
    private String          m_name = null;
    private Codon[]         m_codons = null;
    /** Creates a new instance of TranslationTable */
    public TranslationTable(String name, String aa, String starts,
            String base1, String base2, String base3 ) throws Exception
    {
        char[] ar_aa = aa.toCharArray();
        char[] ar_start = starts.toCharArray();
        char[] ar_base1 = base1.toCharArray();
        char[] ar_base2 = base2.toCharArray();
        char[] ar_base3 = base3.toCharArray();
        int[]  lengths = {ar_aa.length , ar_start.length,ar_base1.length , ar_base2.length , ar_base3.length};
        if ( ! ConstantsImport.areEqual(lengths))
            throw new Exception("Cannot creat translation table from data: \n" + aa +"\n"+starts +"\n"+ base1+"\n"+base2+"\n"+base3);
        Codon codon = null;
        m_codons = new Codon[ar_aa.length];
        for (int count = 0; count < ar_aa.length; count++)
        {
            codon = new Codon(ar_aa[count], ar_start [count],ar_base1[count],ar_base2[count],ar_base3[count]);
            m_codons[count] = codon;
        }
        m_name = name;
    }
    
    public int        isStopCodon(Codon codon)
    {
        switch (  codon.getIndex())
        { 
            case Codon.PROPERTY_NOT_DEFINED: return Codon.PROPERTY_NOT_DEFINED;
            default: return m_codons[ codon.getIndex()].isStopCodon();
        } 
    }
    
     public int        isStartCodon(Codon codon)
    {
        switch (  codon.getIndex())
        { 
            case Codon.PROPERTY_NOT_DEFINED: return Codon.PROPERTY_NOT_DEFINED;
            default: return m_codons[ codon.getIndex()].isStartCodon();
        } 
    }
     
     public String        translateCodon(Codon codon)
    {
        switch (  codon.getIndex())
        { 
            case Codon.PROPERTY_NOT_DEFINED: return "";
            default: return String.valueOf( m_codons[ codon.getIndex()].getAATranslation());
        } 
    }
     
     
     public String      toString()
     {
         StringBuffer res = new StringBuffer();
         res.append( m_name +"\n");
         for (int count = 0; count < m_codons.length; count++)
         {
             res.append( m_codons[count].toString() +"\n");
         }
         return res.toString();
     }
     
      public   static  Hashtable      getTranlationTables()throws Exception
      {    
          if ( i_tanslation_tables == null)
              buildTranslationTables();
          return i_tanslation_tables;     
      }
     
     private  static  void                   buildTranslationTables() throws Exception
     {
           InputStream iStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(TR_TABLE_FILE);
           if ( iStream == null)
                   System.err.println("Unable to load properites file");
           Properties prop = null;         
           try 
            {
                prop= new Properties();
                prop.load(iStream);
           }catch(Exception e){throw new Exception("Cannot load translation tables.");}
         
           i_tanslation_tables=  new Hashtable();
         
          String key  = null;String original_key  = null;
          String[] tr_table = null;
          String table_name = null;
          Hashtable temp_table = new Hashtable();
          for (Enumeration e_prop = prop.keys() ; e_prop.hasMoreElements() ;)
          {
               original_key = (String) e_prop.nextElement();//N.AAs ;N.Starts ;N.Base1; N.Base2; N.Base3
       System.out.println(original_key) ;    
               if ( !original_key.startsWith("TABLE_")) continue;
               
               key = original_key.substring( original_key.indexOf('_') + 1);
               table_name =  key.substring(0, key.indexOf('.') );
               tr_table = (String[] )temp_table.get(table_name);
               if (tr_table == null  )
               {
                   tr_table = new String[5 + 1];
                   temp_table.put( table_name, tr_table);
               }
               tr_table[ getEntryIndex(key.substring(key.indexOf('.') + 1 )) ] = prop.getProperty(original_key);
          }
          TranslationTable new_table = null;
           for (Enumeration e_prop = temp_table.keys() ; e_prop.hasMoreElements() ;)
          {
               key = (String) e_prop.nextElement();
               tr_table= (String[] )temp_table.get(key);
               try
               {
                   new_table = new  TranslationTable(key, tr_table[0], tr_table[1],
                        tr_table[2], tr_table[3], tr_table[4] );
                   i_tanslation_tables.put(key, new_table);
                   System.out.println( new_table.toString());
               }
               catch(Exception e ){ throw new Exception (e.getMessage());}
               
          }
// build tables
     }
     
     private static int         getEntryIndex(String key)
     {
         if ( key.intern() == TranslationTable.AAs) return 0 ;
         if ( key.intern() == TranslationTable.Starts) return 1 ;
         if ( key.intern() == TranslationTable.Base1) return 2; 
         if (key.intern() == TranslationTable.Base2) return 3; 
         if ( key.intern() == TranslationTable.Base3) return 4;
         return 5;
     }
     
    
}
