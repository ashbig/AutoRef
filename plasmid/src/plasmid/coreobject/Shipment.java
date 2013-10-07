/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.coreobject;

import java.util.List;

/**
 *
 * @author Lab User
 */
public class Shipment {
    private int shipmentid;
    private String date;
    private String who;
    private String trackingnumber;
    private String method;
    private String account;
    private String comments;
    private int orderid;
    private int numOfClones;

    private List<Clone> clones;
    
    public Shipment(int shipid, String date, String who, String tracking, String method, String account, String comments) {
        this.shipmentid = shipid;
        this.date = date;
        this.who = who;
        this.trackingnumber = tracking;
        this.method = method;
        this.account = account;
        this.comments = comments;
    }
    
    /**
     * @return the shipmentid
     */
    public int getShipmentid() {
        return shipmentid;
    }

    /**
     * @param shipmentid the shipmentid to set
     */
    public void setShipmentid(int shipmentid) {
        this.shipmentid = shipmentid;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the who
     */
    public String getWho() {
        return who;
    }

    /**
     * @param who the who to set
     */
    public void setWho(String who) {
        this.who = who;
    }

    /**
     * @return the trackingnumber
     */
    public String getTrackingnumber() {
        return trackingnumber;
    }

    /**
     * @param trackingnumber the trackingnumber to set
     */
    public void setTrackingnumber(String trackingnumber) {
        this.trackingnumber = trackingnumber;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the orderid
     */
    public int getOrderid() {
        return orderid;
    }

    /**
     * @param orderid the orderid to set
     */
    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    /**
     * @return the clones
     */
    public List<Clone> getClones() {
        return clones;
    }

    /**
     * @param clones the clones to set
     */
    public void setClones(List<Clone> clones) {
        this.clones = clones;
    }

    /**
     * @return the numOfClones
     */
    public int getNumOfClones() {
        return numOfClones;
    }

    /**
     * @param numOfClones the numOfClones to set
     */
    public void setNumOfClones(int numOfClones) {
        this.numOfClones = numOfClones;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
