/*
 * SequencePair.java
 *
 * Created on March 24, 2003, 3:33 PM
 */

package edu.harvard.med.hip.bec.modules;

import edu.harvard.med.hip.bec.coreobjects.sequence.*;
/**
 *
 * @author  htaycher
 */
public class SequencePair
{
    private AnalyzedScoredSequence m_clonesequence = null;
    private BaseSequence m_refsequence = null;
    /** Creates a new instance of SequencePair */
    public SequencePair(AnalyzedScoredSequence fl, BaseSequence tr)
    {
        m_clonesequence = fl;
        m_refsequence = tr;
    }
    
    public AnalyzedScoredSequence getQuerySequence(){ return m_clonesequence ;}
    public BaseSequence getRefSequence(){ return m_refsequence ;} 
    
}
