/*
 * BioTrail.java
 *
 * Created on February 24, 2003, 4:56 PM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;

import java.util.ArrayList;
import edu.harvard.med.hip.bec.util.*;
/**
 *
 * @author  htaycher
 */
public class BioLinker
{
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String m_name = null;
    private String m_sequence = null;
 
    
    /** Creates a new instance of BioVector */
    protected BioLinker(int id, String name, String seq)
    {
    }
    
    public BioLinker(String name, String seq)
    {
    }
    
    public int getId()    { return m_id;}
    public String getName()    { return m_name;}
    public String getSequence()    { return m_sequence;}
   
    
    public static ArrayList getAllTails()
    {
        return null;
    }
    
}
