/*
 * File : LinkFileRefTag.java
 * Classes : LinkFileRefTag
 *
 * Description :
 *
 *  Links to a file in the repository.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-07-12 20:34:23 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 12, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-12-2001 : JMM - Class created. 
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
import edu.harvard.med.hip.flex.file.*;

import org.apache.struts.util.*;

/**
 * Link to a file refernce.
 *
 * @author $Author: jmunoz $
 * @version $Revision: 1.1 $ $Date: 2001-07-12 20:34:23 $
 */

public class LinkFileRefTag extends TagSupport {
    
    /**
     * The name of the variable that is holding the File reference object
     */
    private String name;
    
    
    /**
     * The name of the property (or nested property) that holds the file 
     * reference from the <code>name</code> variable
     */
    private String property;
    
    
    
    /**
     * Accessor for the name of the variable holding the sample object.
     *
     * @return the name of the <code>Sample</code> variable to link to
     */
    public String getName() {
        
        return (this.name);
        
    }
    
    
    /**
     * Mutator for the name of the sample variable.
     *
     * @param name The name of the <code>Sample</code> variable 
     *       to link to.
     */
    public void setName(String name) {
        
        this.name = name;
        
    }
    
    /**
     * Accessor for the property.
     *
     * @return the name of the property that holds the <code>Sample</code> 
     *      object
     */
    public String getProperty() {
        
        return (this.property);
        
    }
    
    
    /**
     * Set the attribute property.
     *
     * @param prop The name of the property that holds the 
     *      <code>Sample</code> variable to link to.
     */
    public void setProperty(String prop) {
        
        this.property = prop;
        
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
        StringBuffer url = new StringBuffer();
        
        
        if (name!=null) {
            FileReference file = null;
            try {
                file = (FileReference)RequestUtils.lookup(pageContext,name, 
                property, null);
            } catch (ClassCastException cce) {
                throw new JspException("The specified variable does not contain a Sample object\n"+cce.getMessage());
            }
            url.append(file.getURL());
            
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
        
        
        this.property=null;
        this.name=null;
        
    }
    
    
    
} // End class LinkSequenceTag


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
