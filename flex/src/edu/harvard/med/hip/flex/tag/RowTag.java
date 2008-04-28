package edu.harvard.med.hip.flex.tag;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts.taglib.logic.IterateTag;
import org.apache.struts.util.RequestUtils;

/**
 * <p>This tag generates table rows (i.e. &lt;tr&gt;....&lt;/tr&gt; elements) with the
 * background color set differently for alternating odd and even rows. This tag only operates
 * properly if embedded in an IterateTag.</p>
 *
 * <p>The following parameters can be specified for this Tag:</p>
 * <ul>
 * <li><code>oddColor </code> - The color for Odd numbered rows
 * <li><code>evenColor</code> - The color for Even numbered rows
 * <li><code>oddStyleClass</code> - The style class for Odd numbered rows
 * <li><code>evenStyleClass</code> - The style class for Even numbered rows
 * <li><code>align</code> - The alignment for the table row
 * <li><code>valign</code> - The vertical alignment for the table row
 * </ul>
 *
 * <p>Additionally this tag inherits the Event Handler and Style attributes
 * from the BaseHandlerTag which can also be specified</p>
 *
 * @author Amarda Business Systems Ltd
 * @version 1.0
 */

public class RowTag extends org.apache.struts.taglib.html.BaseHandlerTag {
    
    // ----------------------------------------------------- Instance Variables
    
    protected final static String QUOTE   = "\"";
    
    
    /**
     * Name of the variable to check for equality agaisnt
     */
    protected String match=null;
    
    /**
     * Return the name of the object to compare to to highlight.
     */
    public String getMatch() {
        return this.match;
    }
    
    
    /**
     * set the object name to compare to
     * @param name of the object to compare to
     */
    public void setMatch(String name) {
        this.match=name;
    }
    
    protected String property=null;
    
    /**
     * return the name of the property to compare to in the the iterate tag.
     */
    public String getProperty() {
        return this.property;
    }
    
    /**
     * set the name of the property to compare to in the iterate tag.
     */
    public void setProperty(String prop) {
        this.property=prop;
    }
    
    /**
     *  Color of Odd rows in a table
     */
    protected String oddColor = null;
    
    /**
     * Return the color of Odd rows
     */
    public String getOddColor() {
        return (this.oddColor);
    }
    
    /**
     * Set the color of Odd rows
     *
     * @param color HTML bgcolor value for Odd rows
     */
    public void setOddColor(String color) {
        this.oddColor = color;
    }
    
    /**
     *  Color of Even rows in a table
     */
    protected String evenColor = null;
    
    /**
     *  Return the color of Even rows
     */
    public String getEvenColor() {
        return (this.evenColor);
    }
    
    /**
     * Set the color of Even rows
     *
     * @param color HTML bgcolor value for Even rows
     */
    public void setEvenColor(String color) {
        this.evenColor = color;
    }
    
    /**
     *  StyleClass of Odd rows in a table
     */
    protected String oddStyleClass = null;
    
    /**
     * Return the Style Class of Odd rows
     */
    public String getOddStyleClass() {
        return (this.oddStyleClass);
    }
    
    /**
     * Set the Style Class of Odd rows
     *
     * @param styleClass HTML Style Class value for odd rows
     */
    public void setOddStyleClass(String styleClass) {
        this.oddStyleClass = styleClass;
    }
    
    /**
     *  StyleClass of matched rows in a table
     */
    protected String matchStyleClass = null;
    
    /**
     * Return the Style Class of matched rows
     */
    public String getMatchStyleClass() {
        return (this.matchStyleClass);
    }
    
    /**
     * Set the Style Class of matched rows
     *
     * @param styleClass HTML Style Class value for matched rows
     */
    public void setMatchStyleClass(String styleClass) {
        this.matchStyleClass = styleClass;
    }
    
    /**
     *  Style Class of Even rows in a table
     */
    protected String evenStyleClass = null;
    
    /**
     *  Return the Style Class of Even rows
     */
    public String getEvenStyleClass() {
        return (this.evenStyleClass);
    }
    
    /**
     * Set the styleClass of Even rows
     *
     * @param styleClass HTML Style Class value for Even rows
     */
    public void setEvenStyleClass(String styleClass) {
        this.evenStyleClass = styleClass;
    }
    
    /**
     *  Alignment of the table row
     */
    protected String align = null;
    
    /**
     *  Return the Alignment
     */
    public String getAlign() {
        return (this.align);
    }
    
    /**
     * Set the Alignment
     *
     * @param Value for Alignment
     */
    
    public void setAlign(String align) {
        this.align = align;
    }
    /**
     *  Vertical Alignment of the table row
     */
    protected String valign = null;
    
    /**
     *  Return the Vertical Alignment
     */
    public String getValign() {
        return (this.valign);
    }
    
    /**
     * Set the Vertical Alignment
     *
     * @param Value for Vertical Alignment
     */
    
    public void setValign(String valign) {
        this.valign = valign;
    }
    
    
    // ----------------------------------------------------- Public Methods
    
    /**
     * Start of Tag processing
     *
     * @exception JspException if a JSP exception occurs
     */
    public int doStartTag() throws JspException {
        
        // Continue processing this page
        return (EVAL_BODY_TAG);
      //  return (EVAL_BODY_AGAIN);
        
    }
    /**
     * End of Tag Processing
     *
     * @exception JspException if a JSP exception occurs
     */
    public int doEndTag() throws JspException {
        
        StringBuffer buffer = new StringBuffer();
        
        // Create a <tr> element based on the parameters
        buffer.append("<tr");
        
        // Prepare this HTML elements attributes
        prepareAttributes(buffer);
        
        buffer.append(">");
        
        // Add Body Content
        if (bodyContent != null)
            buffer.append(bodyContent.getString().trim());
        
        buffer.append("</tr>");
        
        // Render this element to our writer
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(buffer.toString());
        }
        catch (IOException e) {
            throw new JspException("Exception in RowTag doEndTag():"+e.toString());
        }
        
        return EVAL_PAGE;
    }
    /**
     * Prepare the attributes of the HTML element
     */
    protected void prepareAttributes(StringBuffer buffer) throws JspException{
        
        // Determine if it is an "Odd" or "Even" row
        boolean evenNumber = (getRowNumber() % 2) == 0 ? true : false;
        
        // determine if this is a matching row and should be highlighted
        boolean matching = false;
        IterateTag iterTag =
        (IterateTag)findAncestorWithClass(this, IterateTag.class);
        
        if(this.getMatch() != null && ! this.getMatch().equals("")) {
            Object curObj = RequestUtils.lookup(this.pageContext, iterTag.getId(), this.property, null);
            Object matchObj = RequestUtils.lookup(this.pageContext, this.getMatch(), null);
            if(curObj.equals(matchObj)) {
                matching = true;
            }
        }
        if(matching) {
            buffer.append(prepareMatchClass());
        } else {
            // Append bgcolor parameter
            buffer.append(prepareBgcolor(evenNumber));
            
            // Append CSS class parameter
            buffer.append(prepareClass(evenNumber));
        }
        // Append "align" parameter
        buffer.append(prepareAttribute("align", align));
        
        // Append "valign" parameter
        buffer.append(prepareAttribute("valign", valign));
        
        // Append Event Handler details
        buffer.append(prepareEventHandlers());
        
        // Append Style details
        buffer.append(prepareStyles());
        
    }
    
    /**
     * Format attribute="value" from the specified attribute & value
     */
    protected String prepareAttribute(String attribute, String value) {
        
        return value == null ? "" : " " + attribute + "=" + QUOTE + value + QUOTE;
        
    }
    
    /**
     * Format the bgcolor attribute depending on whether
     * the row is odd or even.
     *
     * @param evenNumber Boolean set to true if an even numbered row
     *
     */
    protected String prepareBgcolor(boolean evenNumber) {
        
        if (evenNumber)
            return prepareAttribute("bgcolor", evenColor);
        else
            return prepareAttribute("bgcolor", oddColor);
        
    }
    
    /**
     * Format the Style sheet class attribute depending on whether
     * the row is odd or even.
     *
     * @param evenNumber Boolean set to true if an even numbered row
     *
     */
    protected String prepareClass(boolean evenNumber) {
        
        if (evenNumber)
            return prepareAttribute("class", evenStyleClass);
        else
            return prepareAttribute("class", oddStyleClass);
        
        
    }
    
    /**
     * format the style sheet class attribute for matching rows
     */
    protected String prepareMatchClass() {
        return prepareAttribute("class", matchStyleClass);
    }
    
    /**
     * Determine the Row Number - from the IterateTag
     */
    protected int getRowNumber() {
        
        // Determine if embedded in an IterateTag
        Tag tag = findAncestorWithClass(this, IterateTag.class);
        if (tag == null)
            return 1;
        // Determine the current row number
        IterateTag iterator = (IterateTag)tag;
        //        return iterator.getLengthCount() + 1;
        return iterator.getIndex() + 1;
    }
    
    /**
     * Release resources after Tag processing has finished.
     */
    public void release() {
        
        super.release();
        
        oddColor       = null;
        evenColor      = null;
        oddStyleClass  = null;
        evenStyleClass = null;
        matchStyleClass= null;
        property       = null;
        match          = null;
        align          = null;
        valign         = null;
        
    }
}