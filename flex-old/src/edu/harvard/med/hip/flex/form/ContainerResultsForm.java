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
 * $Revision: 1.2 $
 * $Date: 2001-07-09 16:02:02 $
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
 * @version    $Revision: 1.2 $ $Date: 2001-07-09 16:02:02 $
 */

public class ContainerResultsForm extends ActionForm{
    
    // store the status indexed properties as a List
    private List statusList;
    
    // store the results as an index property
    private List resultList;
    
    // store the container for this form
    private Container container;
    
    // is this form editable
    private boolean editable = true;
    
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
        this.container = container;
        statusList = new LinkedList();
        resultList = new LinkedList();
        
        // set values in the form, if the container is not null.
        if(container != null) {
            reset(container);
        }
        
    }
    
    
    /**
     * Access for the container
     *
     * @return The container this form represents
     */
    public Container getContainer() {
        return this.container;
    }
    
    /**
     * Mutator for the container
     *
     * @param container, the container to set to
     */
    public void setContainer(Container container) {
        this.container = container;
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
     * Accessor for the editable flag.
     *
     * @return true if editable, false otherwise
     */
    public boolean isEditable() {
        return this.editable;
    }
    
    
    /**
     * Mutator for the editable flag.
     *
     * @param editable true if the form is editable, false otherwise.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.editable=true;
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
            resultList.add(i, "Many");
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
        return container == null ? 0 : container.getSamples().size();
    }
} // End class ContainerResultsForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
