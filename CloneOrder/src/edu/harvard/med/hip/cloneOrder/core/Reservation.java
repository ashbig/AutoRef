/*
 * Reservation.java
 *
 * Created on March 26, 2003, 6:53 PM
 */

package edu.harvard.med.hip.cloneOrder.core;

/**
 *
 * @author  hweng
 */
public class Reservation {
    
    protected int customerid;
    protected CloneSetInfo cloneset_info;
    protected String reservation_date;
    
    /** Creates a new instance of Reservation */
    public Reservation(CloneSetInfo cloneset, String date) {
        this.cloneset_info = cloneset;
        this.reservation_date = date;        
    }
    
    public int getCustomerid(){
        return this.customerid;
    }
    public CloneSetInfo getCloneset_info(){
        return this.cloneset_info;
    }
    public String getReservation_date(){
        return this.reservation_date;
    }
    
}
