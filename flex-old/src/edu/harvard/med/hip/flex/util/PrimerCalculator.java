/**
 *   Interface used for oligo primer calculation
 *   $Id: PrimerCalculator.java,v 1.1 2001-07-06 19:28:58 jmunoz Exp $
 *
 *   @File     	: PrimerCalculator.java 
 *   @Date     	: 04162001 
 *   @author	: Wendy Mar
 *
 *   modified 5/25/01 wmar
 */
 
package edu.harvard.med.hip.flex.util;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
 
public interface PrimerCalculator
{
	/**
	 * Calculate the five prime oligo of a given sequence
	 * @param sequence A Sequence object
	 * @return An Oligo object   
 	 */
	 public Oligo calculateFivepOligo (Sequence sequence) throws FlexDatabaseException;
	
	 /**
	 * Calculate the three prime closed oligo of a given sequence
	 * @param sequence A Sequence object
	 * @return An Oligo object   
 	 */
	 public Oligo calculateThreepCloseOligo(Sequence sequence) throws FlexDatabaseException;

	 /**
	 * Calculate the three prime open oligo of a given sequence
	 * @param sequence A Sequence object
	 * @return An Oligo object   
 	 */
	 public Oligo calculateThreepOpenOligo(Sequence sequence) throws FlexDatabaseException;

}

