/*
 * File : ContainerResultsForm.java
 * Classes : ContainerResultsForm
 *
 * Description :
 *
 *      Form to enter results for a container.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.5 $
 * $Date: 2001-08-01 16:22:58 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on June 15, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jun-15-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */




package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;

/**
 * Form for entering results about a container.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.5 $ $Date: 2001-08-01 16:22:58 $
 */

public class ContainerResultsForm extends ResultForm {
    
    // store the status indexed properties as a List
    private List statusList;
    
    // store the results as an index property
    private List resultList;
    
    
    /**
     * Default Constructor
     */
    public ContainerResultsForm() {
        this(null);
    }
    
    /**
     * Constructor that takes in a container
     *
     * @param the container this form is representing
     */
    public ContainerResultsForm(Container container) {
        super(container);
        statusList = new LinkedList();
        resultList = new LinkedList();
        
        // set values in the form, if the container is not null.
        if(container != null) {
            reset(container);
        }
        
    }
    
    
    
    
    
    /**
     * Accessor for the status
     *
     * @param index The index of the status to get
     *
     * @return the status associated with the index property
     */
    public String getStatus(int index) {
        return (String)statusList.get(index);
    }
    
    /**
     * Mutataor for the index status.
     *
     * @param index The index to change.
     * @param value the value to set to.
     */
    public void setStatus(int index, String value) {
        statusList.set(index,value);
    }
    
    /**
     * Access for the result.
     *
     * @param index The index of the result to get
     *
     * @return the result associated with the indexed property
     */
    public String getResult(int index) {
        return (String)resultList.get(index);
    }
    
    /**
     * Mutator for the result.
     *
     * @param index The index of the result to set.
     * @param value the value to set to.
     */
    public void setResult(int index, String value) {
        resultList.set(index,value);
    }
    
    
    
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.setEditable(true);
    }
    
    /**
     * Method to reset the form with the provided container.
     *
     * @param container the container to get the info from.
     */
    public void reset(Container container) {
        //reset the lists
        statusList.clear();
        resultList.clear();
        
        Iterator sampleIter = container.getSamples().iterator();
        int i = 0;
        while(sampleIter.hasNext()) {
            Sample curSample = (Sample)sampleIter.next();
            resultList.add(i, "");
            statusList.add(i, curSample.getStatus());
            i++;
        }
    }
    
    /**
     * Get the number of indexes.
     *
     * @return the size of the lists
     */
    public int size() {
        return this.getContainer() == null ? 0 : this.getContainer().getSamples().size();
    }
} // End class ContainerResultsForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
