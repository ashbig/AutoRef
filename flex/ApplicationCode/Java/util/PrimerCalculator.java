/**
 *   Interface used for oligo primer calculation
 *   Id: PrimerCalculator.java $
 *
 *   @File     	: PrimerCalculator.java 
 *   @Date     	: 04162001 
 *   @author	: Wendy Mar
 */
 
package flex.ApplicationCode.Java.util;
import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.database.*;
 
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

