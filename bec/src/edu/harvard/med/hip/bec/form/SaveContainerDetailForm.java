/*
 * SaveContainerDetailForm.java
 *
 * Created on February 5, 2002, 11:54 AM
 */

package edu.harvard.med.hip.bec.form;

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
    private int executionid;
    
    /** Creates new SaveContaierDetailForm */
    public SaveContainerDetailForm() {
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }
    
    public int getExecutionid() {
        return executionid;
    }
}
