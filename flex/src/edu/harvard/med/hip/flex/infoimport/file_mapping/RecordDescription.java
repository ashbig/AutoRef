/*
 * RecordDescription.java
 *
 * Created on June 22, 2007, 1:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.file_mapping;

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author htaycher
 */
public class RecordDescription 
{
    
        private FileStructureColumn i_column_descr = null;
        private int[]               i_column_numbers_for_field = null;
        
       
        public RecordDescription(int column_number, FileStructureColumn column_structure)
        {
            i_column_numbers_for_field = new int[1];
            i_column_numbers_for_field[0] = column_number;
            i_column_descr = column_structure;
            
        }
        public RecordDescription(int column_number, FileStructureColumn column_structure,
                int max_number_of_columns)
        {
            max_number_of_columns =  ( column_structure.getObjectPropertyOrder() > max_number_of_columns)?
                column_structure.getObjectPropertyOrder() : max_number_of_columns;
            i_column_numbers_for_field = new int[max_number_of_columns];
            //set all members to -1 for later verification
            for (int count = 0; count < i_column_numbers_for_field.length; count++)
            {
                i_column_numbers_for_field[count] = -1;
            }
            i_column_numbers_for_field[column_structure.getObjectPropertyOrder()] = column_number;
            i_column_descr = column_structure;
            
        }
         public void        setColumnNumber(int column_number, int property_order) throws Exception
         {
             if(  i_column_numbers_for_field[property_order] != -1)
                 throw new Exception("Wrong property order: position "+property_order +" column number "+column_number);
             i_column_numbers_for_field[property_order] = column_number;
         }  
         
         
        public  FileStructureColumn getFileColumnDescription(){ return i_column_descr ;}
        public int[]                getColumnsPerProperty(){ return i_column_numbers_for_field ;}
     
    }
    

