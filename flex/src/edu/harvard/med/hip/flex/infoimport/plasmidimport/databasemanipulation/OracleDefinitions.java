/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation;

/**
 *
 * @author htaycher
 */
public class OracleDefinitions {
    
    public enum COLUMN_DATATYPE_STRING
    {
        VARCHAR2,
        NVARCHAR2,
        VARCHAR,
        CHAR,
        NCHAR;
        
        COLUMN_DATATYPE_STRING(){}
        
        public static boolean isStringDataType(String s)
        {
            try
            {
                COLUMN_DATATYPE_STRING.valueOf(s);
                return true;
            }
            catch(Exception e)
            {
                return false;
            }
        }
    }

}
