/*
 * ClonePriceCalculator.java
 *
 * Created on June 1, 2005, 1:33 PM
 */

package plasmid.util;

import plasmid.coreobject.User;
import plasmid.coreobject.CollectionInfo;

/**
 *
 * @author  DZuo
 */
public class ClonePriceCalculator {
    public static final double PRICE_PER_CLONE_DFHCC = 45.0;
    public static final double PRICE_PER_CLONE_COMMERCIAL = 60.0;
    public static final double PRICE_PER_CLONE_OTHER = 55.0;
    
    /** Creates a new instance of ClonePriceCalculator */
    public ClonePriceCalculator() {
    }
    
    public double calculateClonePrice(int clonenum, String group) {
        double pricePerClone = getPricePerClone(group);
        return clonenum*pricePerClone;
    }
    
    public double getPricePerClone(String group) {
        if(User.DFHCC.equals(group))
            return PRICE_PER_CLONE_DFHCC;
        if(User.OTHER.equals(group)) 
            return PRICE_PER_CLONE_COMMERCIAL;
        return PRICE_PER_CLONE_OTHER;
    }
    
    public double calculatePriceForCollection(CollectionInfo info, String group) {
        double price = info.getPrice();
        
        for(int i=0; i<User.MEMBER.length; i++) {
            String g = User.MEMBER[i];
            if(g.equals(group)) {
                price = info.getMemberprice();
                break;
            }
        }
        
        int quantity = info.getQuantity();
        return price*quantity;
    }
}
