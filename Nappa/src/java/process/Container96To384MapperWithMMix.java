/*
 * Container96To384MapperWithMMix.java
 *
 * Created on October 19, 2007, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import transfer.ContainerheaderTO;
import transfer.ReagentTO;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class Container96To384MapperWithMMix extends Container96To384Mapper {
    protected List<ReagentTO> mmixes;
            
    /** Creates a new instance of Container96To384MapperWithMMix */
    public Container96To384MapperWithMMix() {
        super();
    }
    
    public Container96To384MapperWithMMix(InputStream file, List<ReagentTO> mix) {
        super(file);
        mmixes = mix;
    }

    public List getMmixes() {
        return mmixes;
    }

    public void setMmixes(List mmixes) {
        this.mmixes = mmixes;
    }
    
    public void addOtherReagents(ContainerheaderTO container, SampleTO sample, int start, int end) {
        Iterator iter = getSrcContainers().iterator();
        Iterator iter2 = mmixes.iterator();
        int i=0;
        while(iter2.hasNext()) {
            ContainerheaderTO c = (ContainerheaderTO)iter.next();
            ReagentTO reagent = (ReagentTO)iter2.next();
            if(i == end)
                return;
            if(i >= start) {
                if(container.getBarcode().equals(c.getBarcode())) {
                    sample.addReagent(reagent);
                }
            }
            i++;
        }
    }
    
    public boolean isValid() {
        //return ((srcContainers.size() == destContainers.size()*4) && (srcContainers.size() == mmixes.size()));
        return srcContainers.size() == mmixes.size();
    }
}
