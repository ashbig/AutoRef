/*
 * File : FindResultTag.java
 * Classes : FindResultTag
 *
 * Description :
 *
 * Defines a page variable with a result in it.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2001-07-31 21:33:33 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 17, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-17-2001 : JMM - class created
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.util.RequestUtils;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;

/**
 * Defines a result object from a process object and a sample
 * @author Craig R. McClanahan
 * @version $Revision: 1.2 $ $Date: 2001-07-31 21:33:33 $
 */

public class FindResultTag extends TagSupport {

    /**
     * The name of the scripting variable that will be exposed as a page
     * scope attribute.
     */
    protected String id = null;

    public String getId() {
        return (this.id);
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * The name of the process to use in the lookup
     */
    protected String processName = null;

    public String getProcessName() {
        return (this.processName);
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }


    /**
     * The name of sample to use in the lookup
     */
    protected String sampleName = null;

    public String getSampleName() {
        return (this.sampleName);
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    
    /**
     * Retrieve the required property and expose it as a scripting variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        // find the sample
        
        Sample sample = (Sample)RequestUtils.lookup(this.pageContext, sampleName,null);
        
        // find the process
        Process process = (Process)RequestUtils.lookup(this.pageContext, processName, null);
        
        // retreive the result
        Result result = null;
        try {
        result = Result.findResult(sample,process);
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }

        // Expose this value as a page variable, it could be null
        if(result != null) {
            int inScope = PageContext.PAGE_SCOPE;
            this.pageContext.setAttribute(id, result, inScope); 
        } else {
            // if it is null, remove it from the page context.
            this.pageContext.removeAttribute(id);
        }
        return (SKIP_BODY);
    }


    /**
     * Release all allocated resources.
     */
    public void release() {

        super.release();
        id = null;
        sampleName = null;
        processName = null;
    }
}

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
