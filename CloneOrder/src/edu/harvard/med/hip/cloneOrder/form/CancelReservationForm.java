/*
 * CancelReservationForm.java
 *
 * Created on March 28, 2003, 12:13 PM
 */

package edu.harvard.med.hip.cloneOrder.form;

import javax.servlet.http.*;
import javax.servlet.*;

import org.apache.struts.action.*;



/**
 *
 * @author  hweng
 */

public class CancelReservationForm extends ActionForm {
    
    protected int clonesetid;
    public int getClonesetid(){
        return clonesetid;
    }
    public void setClonesetid(int clonesetid){
        this.clonesetid = clonesetid;
    }
    
}
