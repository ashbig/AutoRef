/*
 * SequenceInfo.java
 *
 * Created on March 13, 2003, 10:35 AM
 */

package edu.harvard.med.hip.flex.query;
import edu.harvard.med.hip.flex.core.*;
/**
 *
 * @author  hweng
 */
public class SequenceInfo {
    
    protected Sequence sequence;
    protected Construct construct;
    protected Sample sample;
    
    /** Creates a new instance of SequenceInfo */
    public SequenceInfo(Sequence s, Construct c, Sample sam) {
        sequence = s;
        construct = c;
        sample = sam;
    }
    
    public SequenceInfo(Sequence s){
        sequence = s;
    }
    
    public Sequence getSequence(){return sequence;}
    public Construct getConstruct(){return construct;}
    public Sample getSample(){return sample;}
    
}
