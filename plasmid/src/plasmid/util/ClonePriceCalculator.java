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
    public static final double PRICE_PER_CLONE_DFHCC = 45.0;
    public static final double PRICE_PER_CLONE_COMMERCIAL = 61.0;
    public static final double PRICE_PER_CLONE_OTHER = 55.0;

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
        if (user.isMember()) {
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

        if (user.isMember()) {
            price = info.getMemberprice();
            return price * quantity;
        }

        return price * quantity;
    }
}
