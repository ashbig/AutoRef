/*
 * File : SequenceHistoryForm.java
 * Classes : SequenceHistoryForm
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
 * $Date: 2001-07-03 14:55:44 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 3, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-03-2001 : JMM - Class created. 
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.flex.form;

import org.apache.struts.action.*;

/**
 * Form to enter info for searching for a sequence process history.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.1 $ $Date: 2001-07-03 14:55:44 $
 */

public class SequenceHistoryForm extends ActionForm {

    // The parameter we are using for searching - {UNIGENE, GENBANK, FLEX ID...}
    private String searchParam;
    
    // the value given for the search param
    private String paramValue;
    
    /**
     * Accessor for the search parameter.
     *
     * @return the search parameter
     */
    public String getSearchParam() {
        return this.searchParam;
    }
    
    /**
     * Mutator for the search parameter.
     *
     * @param param the value to set to.
     */
    public void setSearchParam(String param) {
        this.searchParam = param;
    }
    
    /**
     * Accessor for the param value.
     *
     * @return the parameter value.
     */
    public String getParamValue() {
        return this.paramValue;
    }
    
    /**
     * Mutator for the parameter value.
     *
     * @param value The value the search against.
     */
    public void setParamValue(String value) {
        this.paramValue=value;
    }
    
    
    
} // End class SequenceHistoryForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
