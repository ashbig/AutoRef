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
    public static final double PRICE_PER_CLONE_MEMBER = 30.0;
    public static final double PRICE_PER_CLONE_NON_MEMEBER = 45.0;
    public static final double PRICE_PER_PLATE_MEMBER = 1200.0;
    public static final double PRICE_PER_PLATE_NON_MEMBER = 1500.0;
    
    /** Creates a new instance of ClonePriceCalculator */
    public ClonePriceCalculator() {
    }
    
    public double calculateClonePrice(int clonenum, int platesize, String group) {
        int platenum = clonenum/platesize;
        int leftclonenum = clonenum%platesize;
        double pricePerClone = getPricePerClone(group);
        double pricePerPlate = getPricePerPlate(group);
        return pricePerPlate*platenum+pricePerClone*leftclonenum;
    }
    
    public double getPricePerClone(String group) {
        for(int i=0; i<User.MEMBER.length; i++) {
            String g = User.MEMBER[i];
            if(g.equals(group)) {
                return PRICE_PER_CLONE_MEMBER;
            }
        }
        return PRICE_PER_CLONE_NON_MEMEBER;
    }
    
    public double getPricePerPlate(String group) {
        for(int i=0; i<User.MEMBER.length; i++) {
            String g = User.MEMBER[i];
            if(g.equals(group)) {
                return PRICE_PER_PLATE_MEMBER;
            }
        }
        return PRICE_PER_PLATE_NON_MEMBER;
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
