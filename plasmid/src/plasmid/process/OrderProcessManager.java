/*
 * OrderProcessManager.java
 *
 * Created on May 20, 2005, 9:23 AM
 */

package plasmid.process;

import java.util.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class OrderProcessManager {
    
    /** Creates a new instance of OrderProcessManager */
    public OrderProcessManager() {
    }
    
    public List processOrder(List items) {
        if(items == null || items.size() == 0)
            return null;
        
        List rt = new ArrayList();
        for(int i=0; i<items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem)items.get(i);
            
            //calculate price
        }
        
        return rt;
    }
    
    public double getTotalPrice(List items) {
        return 0.0;
    }
    
    public int getTotalQuantity(List items) {
        return 0;
    }
}
