/*
 * GBQualifier.java
 *
 * Created on June 29, 2007, 1:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.ncbi_record;

/**
 *
 * @author htaycher
 */
public class GBQualifier 
{
    public static final String      Q_DB_REF = "db_xref";
    public static final String      Q_PROTEIN_ID = "protein_id";
    public static final String      Q_GENE = "gene";
    
    private String          m_name = null;
    private String          m_value = null;
    /** Creates a new instance of GBQualifier */
    public GBQualifier() {    }
    
    public void             setName(String v){  m_name = v;}
    public void             setValue(String v){  m_value = v;}
    public String           getName(){ return m_name;}
    public   String         getValue(){ return m_value;}
    
    
}
