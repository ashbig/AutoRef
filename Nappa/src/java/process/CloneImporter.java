/*
 * CloneImporter.java
 *
 * Created on July 18, 2007, 11:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import core.CloneInfo;
import core.ReagentInfo;
import java.util.List;
import transfer.CloneTO;
import transfer.ContainercellTO;
import transfer.GrowthconditionTO;
import transfer.ReagentTO;
import transfer.VectorTO;

/**
 *
 * @author dzuo
 */
public class CloneImporter extends ReagentImporter {
    
    /** Creates a new instance of CloneImporter */
    public CloneImporter(List<ReagentInfo> clones) {
        super(clones);
    }
    
    public ReagentTO createReagent(ReagentInfo r) {
        CloneInfo c = (CloneInfo)r;
        String name = c.getName();
        CloneTO clone = new CloneTO(c.getSrccloneid(),c.getSource(),c.getGenbank(),c.getGi(),c.getGeneid(),c.getSymbol(),new GrowthconditionTO(c.getGrowth()),new VectorTO(c.getVectorname()));
        clone.setName(name);
        return clone;
    }
    
    public ReagentTO findReagent(List l, ReagentInfo r) {
        for(int i=0; i<l.size(); i++) {
            CloneTO reagent = (CloneTO)l.get(i);
            if(reagent.getSrccloneid().equals(((CloneInfo)r).getSrccloneid())) {
                return reagent;
            }
        }
        return null;
    }
    
    public String getCellType() {
        return ContainercellTO.getTYPE_GENE();
    }
}
