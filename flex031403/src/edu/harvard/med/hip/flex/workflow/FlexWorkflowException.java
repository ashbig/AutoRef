/*
 * FlexWorkflowException.java
 *
 * Created on July 31, 2001, 4:03 PM
 */

package edu.harvard.med.hip.flex.workflow;

/**
 *
 * @author  dzuo
 * @version 
 */
public class FlexWorkflowException extends Exception {

	public FlexWorkflowException(String s) {

		super("FlexProcessException: "+s);

	}

}