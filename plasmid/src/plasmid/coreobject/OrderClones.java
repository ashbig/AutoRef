/*
 * OrderClones.java
 *
 * Created on June 6, 2005, 11:29 AM
 */

package plasmid.coreobject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  DZuo
 */
public class OrderClones {
    private int orderid;
    private int cloneid;
    private String collectionname;
    private int quantity;
    private String plate;
    private String well;
    private String clonename;
    
    private Clone clone;
    private List validations;
    private OrderCloneValidation validation;
    private List history;
    private boolean shipped;
    private boolean inshipment;
    
    /** Creates a new instance of OrderClones */
    public OrderClones() {
        validations = new ArrayList();
        history = new ArrayList();
    }
    
    public OrderClones(int orderid, int cloneid, String collectionname, int quantity) {
        this.orderid = orderid;
        this.cloneid = cloneid;
        this.collectionname = collectionname;
        this.quantity = quantity;
        validations = new ArrayList();
        history = new ArrayList();
    }
    
    public int getOrderid() {return orderid;}
    public int getCloneid() {return cloneid;}
    public String getCollectionname() {return collectionname;}
    public int getQuantity() {return quantity;}
    public String getPlate() {return plate;}
    public String getWell() {return well;}
    
    public void setOrderid(int id) {this.orderid = id;}
    public void setCloneid(int id) {this.cloneid = id;}
    public void setCollectionname(String s) {this.collectionname = s;}
    public void setQuantity(int i) {this.quantity = i;}
    public void setPlate(String s) {this.plate = s;}
    public void setWell(String s) {this.well = s;}

    public Clone getClone() {
        return clone;
    }

    public void setClone(Clone clone) {
        this.clone = clone;
    }

    public List getValidations() {
        return validations;
    }

    public void setValidations(List validations) {
        this.validations = validations;
    }
    
    public void addValidation(OrderCloneValidation v) {
        this.validations.add(v);
    }
    
    public void addHistory(OrderCloneValidation v) {
        this.history.add(v);
    }

    public OrderCloneValidation getValidation() {
        return validation;
    }

    public void setValidation(OrderCloneValidation validation) {
        this.validation = validation;
    }
    
    public int getHasValidations() {
        if((this.getValidations()==null) || (this.getValidations().size()==0))
            return 0;
        else
            return 1;
    }
    
    public int getHasHistory() {
        if((this.getHistory()==null) || (this.getHistory().size()==0))
            return 0;
        else
            return 1;
    }

    /**
     * @return the history
     */
    public List getHistory() {
        return history;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(List history) {
        this.history = history;
    }
    
    public boolean getHasValidation() {
        if(validation ==null)
            return false;
        return true;
    }

    /**
     * @return the shipped
     */
    public boolean isShipped() {
        return shipped;
    }

    /**
     * @param shipped the shipped to set
     */
    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    /**
     * @return the clonename
     */
    public String getClonename() {
        return clonename;
    }

    /**
     * @param clonename the clonename to set
     */
    public void setClonename(String clonename) {
        this.clonename = clonename;
    }

    /**
     * @return the inshipment
     */
    public boolean isInshipment() {
        return inshipment;
    }

    /**
     * @param inshipment the inshipment to set
     */
    public void setInshipment(boolean inshipment) {
        this.inshipment = inshipment;
    }
}
