/*
 * GBFeature.java
 *
 * Created on June 29, 2007, 1:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.ncbi_record;

/**
 *
 * @author htaycher
 */
public class GBFeature
{
     
    public static final String         FEATURE_TYPE_CDS = "CDS";
    public static final String         FEATURE_TYPE_GENE = "gene";
    public static final String         FEATURE_TYPE_SOURCE ="source";
    
    private String      m_key = null;
    private String      m_interval_to = null;
    private String      m_interval_from = null;
    /** Creates a new instance of GBFeature */
    public GBFeature() {
    }
    
    public String      getKey(){ return  m_key ;}
    public String      getStop(){ return  m_interval_to ;}
    public String      getStart(){ return m_interval_from ;}
    
    public void      setKey(String v){   m_key = v;}
    public void      setStop(String v){   m_interval_to =v ;}
    public void      setStart(String v){  m_interval_from =v ;}
    
}
