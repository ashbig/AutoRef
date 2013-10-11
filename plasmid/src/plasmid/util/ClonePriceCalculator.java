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
    public static final double PRICE_PER_CLONE_HOST = 0.0;
    public static final double PRICE_PER_CLONE_DFHCC = 46.0;
    public static final double PRICE_PER_CLONE_COMMERCIAL = 70.0;
    public static final double PRICE_PER_CLONE_OTHER = 58.0;
    public static final double PRICE_PLATINUM_HOST = 6.0;
    public static final double PRICE_PLATINUM_DFHCC = 6.0;
    public static final double PRICE_PLATINUM_COMMERCIAL = 8.0;
    public static final double PRICE_PLATINUM_OTHER = 7.0;

    /** Creates a new instance of ClonePriceCalculator */
    public ClonePriceCalculator() {
    }

    public double calculateClonePrice(int clonenum, User user) {
        double pricePerClone = getPricePerClone(user);
        return clonenum * pricePerClone;
    }

    public double getPricePerClone(User user) {
        if (User.HOSTLAB.equals(user.getGroup())) {
            return PRICE_PER_CLONE_HOST;
        }
        if (user.isMemberForCloneOrder()) {
            return PRICE_PER_CLONE_DFHCC;
        }
        if (User.OTHER.equals(user.getGroup())) {
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
        if (User.HOSTLAB.equals(user.getGroup())) {
            return PRICE_PLATINUM_HOST*numOfClones;
        }
        if (user.isMemberForCloneOrder()) {
            return PRICE_PLATINUM_DFHCC*numOfClones;
        }
        if (User.OTHER.equals(user.getGroup())) {
            return PRICE_PLATINUM_COMMERCIAL*numOfClones;
        }
        return PRICE_PLATINUM_OTHER*numOfClones;
    }
}
