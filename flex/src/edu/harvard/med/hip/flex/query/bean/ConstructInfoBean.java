/*
 * ConstructInfoBean.java
 *
 * Created on March 3, 2004, 10:36 AM
 */

package edu.harvard.med.hip.flex.query.bean;

import java.util.*;
import edu.harvard.med.hip.flex.core.ConstructInfo;

/**
 *
 * @author  DZuo
 */
public class ConstructInfoBean {
    private int sequenceid;
    private List constructInfos;
    
    /** Creates a new instance of ConstructInfoBean */
    public ConstructInfoBean() {
    }
    
    public ConstructInfoBean(int sequenceid, List constructInfos) {
        this.sequenceid = sequenceid;
        this.constructInfos = constructInfos;
    }
    
    public void setSequenceid(int sequenceid) {this.sequenceid=sequenceid;}
    public void setConstructInfos(List constructInfos) {this.constructInfos = constructInfos;}
    
    public int getSequenceid() {return sequenceid;}
    public List getConstructInfos() {return constructInfos;}
    
    public int getNumOfClones() {
        int n=0;
        if(constructInfos == null || constructInfos.size()==0) {
            return n;
        }
        
        for(int i=0; i<constructInfos.size(); i++) {
            ConstructInfo info = (ConstructInfo)constructInfos.get(i);
            int num = info.getNumOfClones();
            if(num == 0) {
                num = 1;
            }
            n = n+num;
        }
        
        return n;
    }
}
