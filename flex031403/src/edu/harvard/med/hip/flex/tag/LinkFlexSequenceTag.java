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
 * $Revision: 1.2 $
 * $Date: 2001-06-04 15:26:34 $
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
 * @version    $Revision: 1.2 $ $Date: 2001-06-04 15:26:34 $
 */

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;

import org.apache.struts.util.*;

/**
 * Generate a URL-encoded hyperlink to the specified URI, with
 * associated query parameters selecting a specified User.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2001-06-04 15:26:34 $
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
     * The name of the variable holding the sequence or which has a property
     * which holds the sequence to display.
     */
    private String sequenceName;
    
    
    /**
     * The name of the property (or nested property) that holds the sequence
     * from the <code>sequenceName</code> variable
     */
    private String seqProperty;
    
    
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
    
    /**
     * Return the attribute sequence name
     *
     * @return the name of the <code>flexSequence</code> variable to link to
     */
    public String getSequenceName() {
        
        return (this.sequenceName);
        
    }
    
    
    /**
     * Set the attribute sequence.
     *
     * @param seqName The name of the <code>FlexSequence</code> variable 
     *       to link to.
     */
    public void setSequenceName(String seqName) {
        
        this.sequenceName = seqName;
        
    }
    
    /**
     * Return the attribute seqProperty
     *
     * @return the name of the property that holds the sequence.
     */
    public String getSeqProperty() {
        
        return (this.seqProperty);
        
    }
    
    
    /**
     * Set the attribute seqProperty.
     *
     * @param seqProp The name of the property that holds the 
     *      <code>FlexSequence</code> variable to link to.
     */
    public void setSeqProperty(String seqProp) {
        
        this.seqProperty = seqProp;
        
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
        if(sequenceName == null) {
            url.append(sequenceId);
        } else {
            FlexSequence seqObj = null;
            try {
                seqObj = (FlexSequence)RequestUtils.lookup(pageContext,sequenceName, 
                seqProperty, null);
            } catch (ClassCastException cce) {
                throw new JspException("The specified property: " + 
                seqProperty + " does not contain a FlexSequence object");
            }
            url.append(seqObj.getId());
            
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
        this.seqProperty=null;
        this.sequenceName=null;
        
    }
    
    
    
} // End class LinkSequenceTag


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
