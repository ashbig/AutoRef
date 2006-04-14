/*
 * CloneSample.java
 *
 * Created on April 14, 2006, 2:58 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class CloneSample extends Sample {
    protected Clone clone;
    
    /** Creates a new instance of CloneSample */
    public CloneSample() {
    }
    
    public CloneSample(Sample s, Clone c) {
        super(s.getCloneid(), s.getType(), s.getStatus(), s.getCloneid(), s.getPosition(), s.getPositionx(), s.getPositiony(), s.getContainerid(), s.getContainerlabel());
        super.setContainerType(s.getContainerType());
        super.setResult(s.getResult());
        if(c != null) {
            clone = new Clone(c.getCloneid(), c.getName(), c.getType(), c.getVerified(), c.getVermethod(), c.getDomain(), c.getSubdomain(), c.getRestriction(), c.getComments(), c.getVectorid(), c.getVectorname(), c.getClonemap(), c.getStatus(), c.getSpecialtreatment(), c.getSource(), c.getDescription());
            clone.setAuthors(c.getAuthors());
            clone.setGrowths(c.getGrowths());
            clone.setHosts(c.getHosts());
            clone.setInserts(c.getInserts());
            clone.setNames(c.getNames());
            clone.setProperties(c.getProperties());
            clone.setPublications(c.getPublications());
            clone.setRecommendedGrowthCondition(c.getRecommendedGrowthCondition());
            clone.setSelections(c.getSelections());
            clone.setSynonyms(c.getSynonyms());
            clone.setVector(c.getVector());
        }
    }
    
    public Clone getClone() {return clone;}
    
    public void setClone(Clone c) {this.clone = c;}
}
