/*
 * File : PrintLabelForm.java
 * Classes : PrintLabelForm
 *
 * Description :
 *
 * This form holds the barcode to be printed.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-07-26 22:07:13 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 26, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-27-2001 : JMM - Class created. 
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
 * Holds the label to be printed.
 *
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.1 $ $Date: 2001-07-26 22:07:13 $
 */

public class PrintLabelForm extends ActionForm {

    /**
     * The label to be printed.
     */
    private String label;
    
    /**
     * mutator for label.
     *
     * @param what to set the label to.
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Gets the label.
     *
     * @return String that is the label to be printed.
     */
    public String getLabel(){
        return this.label;
    }

} // End class PrintLabelForm


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
