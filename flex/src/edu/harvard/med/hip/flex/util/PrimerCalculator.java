/**
 *   Interface used for oligo primer calculation
 *   Id: PrimerCalculator.java $
 *
 *   @File     	: PrimerCalculator.java 
 *   @Date     	: 04162001 
 *   @author	: Wendy Mar
 */
 
package edu.harvard.med.hip.flex.util;
import edu.harvard.med.hip.flex.core.*;
 
public interface PrimerCalculator
{
	/**
	 * Calculate the five prime oligo of a given sequence
	 * @param sequence A Sequence object
	 * @return An Oligo object   
 	 */
	 public Oligo calculateFivepOligo (Sequence sequence);
	
	 /**
	 * Calculate the three prime closed oligo of a given sequence
	 * @param sequence A Sequence object
	 * @return An Oligo object   
 	 */
	 public Oligo calculateThreepCloseOligo(Sequence sequence);

	 /**
	 * Calculate the three prime open oligo of a given sequence
	 * @param sequence A Sequence object
	 * @return An Oligo object   
 	 */
	 public Oligo calculateThreepOpenOligo(Sequence sequence);

}

