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
    public static final double PRICE_PER_CLONE_HOST = 6.0;
    public static final double PRICE_PER_CLONE_DFHCC = 52.0;
    public static final double PRICE_PER_CLONE_COMMERCIAL = 78.0;
    public static final double PRICE_PER_CLONE_OTHER = 65.0;
    
    // In 10/28/2014 the dna resource core shifted the qc
    // cost into the clone cost itself. This is why the variables
    // below are marked as zero dollars. 
    public static final double PRICE_PLATINUM_HOST = 0.0;
    public static final double PRICE_PLATINUM_DFHCC = 0.0;
    public static final double PRICE_PLATINUM_COMMERCIAL = 0.0;
    public static final double PRICE_PLATINUM_OTHER = 0.0;

    /** Creates a new instance of ClonePriceCalculator */
    public ClonePriceCalculator() {
    }

    public double calculateClonePrice(int clonenum, User user) {
        double pricePerClone = getPricePerClone(user);
        return clonenum * pricePerClone;
    }

    public double calculateClonePrice(int clonenum, String usergroup, String ismember) {
        double pricePerClone = getPricePerClone(usergroup, ismember);
        return clonenum * pricePerClone;
    }

    public double getPricePerClone(User user) {
        return getPricePerClone(user.getGroup(), user.getIsmember());
    }

    public double getPricePerClone(String usergroup, String ismember) {
        if (User.HOSTLAB.equals(usergroup)) {
            return PRICE_PER_CLONE_HOST;
        }
        if (User.isMemberForCloneOrder(usergroup, ismember)) {
            return PRICE_PER_CLONE_DFHCC;
        }
        if (User.OTHER.equals(usergroup)) {
            return PRICE_PER_CLONE_COMMERCIAL;
        }
        return PRICE_PER_CLONE_OTHER;
    }

    public double calculatePriceForCollection(CollectionInfo info, User user) {
        double price = info.getPrice();
        int quantity = info.getQuantity();

        if (User.OTHER.equals(user.getGroup())) {
            price = info.getCommercialprice();
            return price * quantity;
        }

        if (user.isMemberForCloneOrder()) {
            price = info.getMemberprice();
            return price * quantity;
        }

        return price * quantity;
    }

    public static double calculatePlatinumCost(User user, int numOfClones) {
        return calculatePlatinumCost(user.getGroup(), user.getIsmember(), numOfClones);
    }

    public static double calculatePlatinumCost(String usergroup, String ismember, int numOfClones) {
        if (User.HOSTLAB.equals(usergroup)) {
            return PRICE_PLATINUM_HOST*numOfClones;
        }
        if (User.isMemberForCloneOrder(usergroup, ismember)) {
            return PRICE_PLATINUM_DFHCC*numOfClones;
        }
        if (User.OTHER.equals(usergroup)) {
            return PRICE_PLATINUM_COMMERCIAL*numOfClones;
        }
        return PRICE_PLATINUM_OTHER*numOfClones;
    }
}
