/*
 * File : LinkContainerTag.java
 * Classes : LinkContainerTag
 *
 * Description :
 *
 * Tag to link to a container detail page.  Either provide an container id
 * or pass in the container object itself.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-06-29 19:12:31 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 31, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-27-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.tag;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

import org.apache.struts.util.*;

/**
 * Link to a container detail page.
 *
 * @author $Author: dongmei_zuo $
 * @version $Revision: 1.2 $ $Date: 2001-06-29 19:12:31 $
 */

public class LinkContainerTag extends TagSupport {
    
    

    
    
    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
    MessageResources.getMessageResources
    ("org.apache.struts.webapp.example.ApplicationResources");
    
    
    /**
     * The attribute containerid
     */
    private int containerId = -1;
    
    
    /**
     * The name of the variable that is holding the container object
     */
    private String name;
    
    
    /**
     * The name of the property (or nested property) that holds container
     * from the <code>name</code> variable
     */
    private String property;
    
    /**
     * The name of the process object.
     */
    private String process;
    
    /**
     * Accessor for the contaienr id.
     *
     * @return the cotnainer id to link to
     */
    public int getContainerId() {
        
        return (this.containerId);
        
    }
    
    
    /**
     * mutator for the sequence id.
     *
     * @param id The id of the container
     */
    public void setContainerId(int id) {
        
        this.containerId = id;
        
    }
    
    /**
     * Accessor for the name of the variable holding the container object.
     *
     * @return the name of the <code>Container</code> variable to link to
     */
    public String getName() {
        
        return (this.name);
        
    }
    
    
    /**
     * Mutator for the name of the container variable.
     *
     * @param name The name of the <code>Container</code> variable 
     *       to link to.
     */
    public void setName(String name) {
        
        this.name = name;
        
    }
    
    /**
     * Accessor for the property.
     *
     * @return the name of the property that holds the <code>Container</code> 
     *      object
     */
    public String getProperty() {
        
        return (this.property);
        
    }
    
    
    /**
     * Set the attribute property.
     *
     * @param prop The name of the property that holds the 
     *      <code>Container</code> variable to link to.
     */
    public void setProperty(String prop) {
        
        this.property = prop;
        
    }
    
    /**
     * Accessor for the process property
     *
     * @return process property
     */
    public String getProcess() {
        return this.process;
    }
    
    
    /**
     * Mutator for the process property.
     *
     * @param processName the name of the process object
     */
    public void setProcess(String processName) {
        this.process = processName;
    }
    
    /**
     * Render the beginning of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        
        // Generate the URL to be encoded
        HttpServletRequest request =
        (HttpServletRequest) pageContext.getRequest();
        StringBuffer url = new StringBuffer(request.getContextPath());
        url.append("/ViewContainerDetails.do");
        
        
        url.append("?");
        
        url.append(Constants.CONTAINER_ID_KEY+"=");
        if(name == null && containerId!=-1) {
            url.append(containerId);
        } else if (name!=null) {
            Container container = null;
            try {
                container = (Container)RequestUtils.lookup(pageContext,name, 
                property, null);
            } catch (ClassCastException cce) {
                throw new JspException("The specified variable does not contain a Container object\n"+cce.getMessage());
            }
            url.append(container.getId());
            
        }
        if(process!=null || process.length() >0) {
            Process processObj = null;
            try {
               processObj=(Process)RequestUtils.lookup(pageContext,this.process, 
               null);
            } catch (ClassCastException cce) {
                throw new JspException("The specified variable does not contain a Process object\n"+cce.getMessage());
            }
            url.append("&"+Constants.PROCESS_ID_KEY+"="+processObj.getExecutionid());
        }
        
        // Generate the hyperlink start element
        HttpServletResponse response =
        (HttpServletResponse) pageContext.getResponse();
        StringBuffer results = new StringBuffer("<a href=\"");
        results.append(response.encodeURL(url.toString()));
        results.append("\">");
        
        // Print this element to our output writer
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        // Evaluate the body of this tag
        return (EVAL_BODY_INCLUDE);
        
    }
    
    
    
    /**
     * Render the end of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        
        
        // Print the ending element to our output writer
        JspWriter writer = pageContext.getOut();
        try {
            writer.print("</a>");
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        
        return (EVAL_PAGE);
        
    }
    
    
    /**
     * Release any acquired resources.
     */
    public void release() {
        
        super.release();
        
        this.containerId = -1;
        this.property=null;
        this.name=null;
        
    }
    
    
    
} // End class LinkSequenceTag


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
