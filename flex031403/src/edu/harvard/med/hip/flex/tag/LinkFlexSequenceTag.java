/*
 * File : LinkFlexSequenceTag.java
 * Classes : LinkFlexSequenceTag
 *
 * Description :
 *
 *    Insert description here.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-06-01 18:10:50 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 31, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    MMM-DD-YYYY : __USERINITIALS__ - Template created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.tag;


/**
 * Class description - JavaDoc 1 liner.
 *
 * Class description - Full description
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.1 $ $Date: 2001-06-01 18:10:50 $
 */

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import edu.harvard.med.hip.flex.Constants;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;


/**
 * Generate a URL-encoded hyperlink to the specified URI, with
 * associated query parameters selecting a specified User.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.1 $ $Date: 2001-06-01 18:10:50 $
 */

public class LinkFlexSequenceTag extends TagSupport {
    
    
    // ----------------------------------------------------- Instance Variables
    
    
    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
    MessageResources.getMessageResources
    ("org.apache.struts.webapp.example.ApplicationResources");
    
    
    /**
     * The attribute sequenceid.
     */
    private int sequenceId = -1;
    
    
    
    
    /**
     * Return the attribute sequenceId
     *
     * @return the sequence id to link to
     */
    public int getSequenceId() {
        
        return (this.sequenceId);
        
    }
    
    
    /**
     * Set the attribute name.
     *
     * @param id The id of the sequence
     */
    public void setSequenceId(int id) {
        
        this.sequenceId = id;
        
    }
    
    
    // --------------------------------------------------------- Public Methods
    
    
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
        url.append("/ViewSequence.do");
        
        
        url.append("?");
        
        url.append(Constants.FLEX_SEQUENCE_ID_KEY+"=");
        url.append(sequenceId);
        
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
            throw new JspException
            (messages.getMessage("link.io", e.toString()));
        }
        
        return (EVAL_PAGE);
        
    }
    
    
    /**
     * Release any acquired resources.
     */
    public void release() {
        
        super.release();
        
        this.sequenceId = -1;
        
    }
    
    
    
} // End class LinkSequenceTag


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
