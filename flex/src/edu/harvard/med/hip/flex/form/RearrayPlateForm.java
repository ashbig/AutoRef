/*
 * RearrayPlateForm.java
 *
 * Created on April 16, 2003, 2:37 PM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.infoimport.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.core.Location;

/**
 *
 * @author  dzuo
 */
public class RearrayPlateForm extends RearrayForm {
    protected String location = Location.FREEZER;
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}
