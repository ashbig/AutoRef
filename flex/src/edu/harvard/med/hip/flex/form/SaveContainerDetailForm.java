/*
 * SaveContainerDetailForm.java
 *
 * Created on February 5, 2002, 11:54 AM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SaveContainerDetailForm extends ActionForm {
    private int id;
    
    /** Creates new SaveContaierDetailForm */
    public SaveContainerDetailForm() {
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
