// package flex.oligo

/**
 *   Interface used for oligo primer calculation 
 *   @author Wendy Mar
 */
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

