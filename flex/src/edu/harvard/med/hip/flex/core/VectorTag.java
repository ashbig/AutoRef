/*
 * VectorTag.java
 *
 * Created on July 5, 2007, 3:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author htaycher
 */
public class VectorTag 
{
    private String      m_name = null;
    private String      m_type = null;
    private String      m_vector_name = null;
    /** Creates a new instance of VectorTag */
    public VectorTag() {    }
    
     public String      getName(){ return m_name ;}
    public String       getType(){ return m_type ;}
    public String       getVectorName(){ return m_vector_name ;}
    
}
