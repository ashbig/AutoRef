/**
 * $Id: NNPrimerCalculator.java,v 1.4 2001-07-20 19:53:14 dzuo Exp $
 * Neariest Neighborhood algorithm is used for current oligo primer calculation
 *
 * @File     	: NNPrimerCalculator.java 
 * @Date     	: 04162001
 * @author	: Wendy Mar 
 */
 
package flex.ApplicationCode.Java.util;
import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.database.*;
import java.math.*;
 
public class NNPrimerCalculator implements PrimerCalculator
{
	private static final double R = 1.9872;     // Gas Constant
	private static final double InitH = 0.6;    // Initial H value
	private static final double InitS = -9.0;   // Initial S value 
	private static double DesiredTM = 60.0;  // The desired Tm is around 60 C 

	private double[][] paramH;
	private double[][] paramS;
	//private double[][] paramG;
	
	/**
	 * Constructor.  Returns a NNPrimerCalculator object  
	 * @return  A NNPrimerCalculator object
	 */ 
	public NNPrimerCalculator ( )
	{
		//A = 0; T = 1; C= 2; G= 3 in the two dimensional array mapping
		paramH = new double[4][4];
		paramH[0][0] = -8.0;
		paramH[0][1] = -5.6;
		paramH[0][2] = -9.4;
		paramH[0][3] = -6.6;
		paramH[1][0] = -6.6;
		paramH[1][1] = -8.0;
		paramH[1][2] = -8.8;
		paramH[1][3] = -8.2;
		paramH[2][0] = -8.2;
		paramH[2][1] = -6.6;
		paramH[2][2] = -10.9;
		paramH[2][3] = -11.8;
		paramH[3][0] = -8.8;
		paramH[3][1] = -9.4;
		paramH[3][2] = -10.5;
		paramH[3][3] = -10.9;

		paramS = new double[4][4];
		paramS[0][0] = -21.9;
		paramS[0][1] = -15.2;
		paramS[0][2] = -25.5;
		paramS[0][3] = -16.4;
		paramS[1][0] = -18.4;
		paramS[1][1] = -21.9;
		paramS[1][2] = -23.5;
		paramS[1][3] = -21.0;
		paramS[2][0] = -21.0;
		paramS[2][1] = -16.4;
		paramS[2][2] = -28.4;
		paramS[2][3] = -29.0;
		paramS[3][0] = -23.5;
		paramS[3][1] = -25.5;
		paramS[3][2] = -26.4;
		paramS[3][3] = -28.4;

		//paramG = new double[4][4];
	}

	/**
	 * Converts the DNA neucleotide dimers into digital combanations
	 * @param dimer  A String object
	 * @return A String object
	 */
	private String convert (String dimer)
	{
		String digitalDimer = "";
		if (dimer.substring(0,1).equals("A")) {
			digitalDimer += "0"; 
		}
		else if (dimer.substring(0,1).equals("T")) {
			digitalDimer += "1";
		}
		else if (dimer.substring(0,1).equals("C")) {
			digitalDimer += "2";
		}
		else {
			digitalDimer += "3";
		}

		if (dimer.substring(1,2).equals("A")) {
			digitalDimer += "0"; 
		}
		else if (dimer.substring(1,2).equals("T")) {
			digitalDimer += "1";
		}
		else if (dimer.substring(1,2).equals("C")) {
			digitalDimer += "2";
		}
		else {
			digitalDimer += "3";
		}

		return digitalDimer;
	}

	/**
	 * This function takes a string up to 60 bases from the sequence
	 * and calculate the oligo primer sequence and the Tm
	 * @param subSeq   A String object represents a 60-base
	 * fragment of the Sequence text.
	 * @param oligoType A String object indicates the types of oligos
	 * @return A Oligo object
	 */
	private Oligo calTm (String subSeq, String oligoType) throws FlexDatabaseException
	{
		double Tm = 0;
		double preTm = 0;
		double totalH = InitH;
		double totalS = InitS;
		int pos = 0;  // keep track the length of oligo, starting from position 0 
		String dimer; // a two-base dimer window sliding through the sequence 
		String indices; // a String representation of the two indexes from paramH and paramS
		String oligoSeq; // the sequence string for oligo primers
		int indexOne = 0; // the first index for the two-dimensional array paramH and paramS
		int indexTwo = 0; // the second index for the two-dimensional array paramH and ParamS
		Oligo oligo = null;		

		while (Tm < DesiredTM)
		{
			preTm = Tm;
			dimer = subSeq.substring(pos, pos+2); //slide dimers from seq			
			//System.out.println(dimer);			
			//get the digital representation of the "A,T,C,G"
			indices = convert(dimer);			
			//System.out.println(indices);
			//get the first index from indices and convert it to integer
			indexOne = Integer.parseInt(indices.substring(0,1));
			indexTwo = Integer.parseInt(indices.substring(1,2));
			totalH += paramH[indexOne][indexTwo];
			totalS += paramS[indexOne][indexTwo];
			
			//In the current calculation, the [Na+] is 5mM
			//Thus, Log[Na+]= -2.301
			//In other algorithm on the web, [Na+] is 50mM
			//Thus, Log[Na+]= -1.301
			//For the current formula, [DNA] is 100 uM, thus 0.1mM
			Tm = totalH * 1000/(totalS - R*9.21)-273.16;
			Tm = Tm - 12.5 * 2.301;
			pos++;
		} //while

		//determine whether the Tm or the preTm is closer to DesiredTM
		if (Math.abs(DesiredTM - Tm) >= Math.abs(DesiredTM - preTm))
		{
			Tm = preTm;
			pos = pos - 1;
		} //if

		// Tm calculation for oligos less than 21 bases seem to be overestimated
		// Tm should not be around 60 C as calculated, actual Tm is around 55 C instead 
		// Also, oligos less than 18 bases are not desirable due to high PCR non-specific binding
		// The length of short oligos are adjusted 
		if ((pos == 19) || (pos == 20)) {
    			pos = 21;
		}
		if ((pos == 17) || (pos == 18)){
			pos = 20;
		}
		if ((pos == 15) || (pos == 16)) {
			pos = 19;
		}
		if (pos <= 14){
			pos = 18;
		}

		// Tm calculation for oligos more than 40 bases seem to be underestimated
		// Also, longer primers tend to form internal loops
		// All the oligos should be no more 40 bases long
		if (pos > 40) {
			pos = 40;
		}

		// The oligo sequence is the substring of parameter seq60
		oligoSeq = subSeq.substring(0, pos+1);
		
		// new oligoID needs to be generated
		int oligoID = FlexIDGenerator.getID("oligo");
	
		oligo = new Oligo(oligoID, oligoType, oligoSeq, Tm);

		return oligo;
	} //calTm


	/**
	 * This function takes a nucleotide base
	 * and converts it into its complementary base.
	 * @param base  A String object
	 * @return A String object
	 */
	
	private String getComplementBase(String base)
 	{
  		String result = null;
  		if (base.equals("A"))
  		{
   			result = "T";
  		}
  		else if (base.equals("C"))
  		{
   			result = "G";
  		}
  		else if (base.equals("G"))
  		{
   			result = "C";
  		}
  		else if (base.equals("T"))
  		{
   			result = "A";
  		}
  		else
  		{
   			result = base;
  		}
  		return result;
 	} // getComplementBase


	/**
	 * This function takes the string from the top strand of the sequence
	 * and converts it into a reversed sequence.
	 * @param seq  A String object
	 * @return A String object
	 */
 	private String getReverse(String seq)
 	{
  		String result = "";
  		int index = 0;
  		int length = seq.length();
  		for (int i = 0; i < length; ++i)
  		{
   			index = length-i-1;
   			result += seq.substring(index,index+1);
  		}
  		return result;
 	} // getReverse

	/**
	 * This function takes the reversed sequence
	 * and converts it into a reverse complement sequence.
	 * @param seq  A String object
	 * @return A String object
	 */
 	private String getReverseComplement(String seq)
 	{
  		String result = "";
  		String reverseSeq = getReverse(seq);
  		for (int i = 0; i < reverseSeq.length(); ++i)
  		{
   			result += getComplementBase(reverseSeq.substring(i,i+1));
  		} // for
  		return result;
 	} // getReverseComplement

	/**
	 * Calculate the five prime oligo
	 * @param sequence  A Sequence object
	 * @return  An Oligo object represents a 5p oligo
	 */
	public Oligo calculateFivepOligo (Sequence sequence) throws FlexDatabaseException
	{
		Oligo fivepOligo = null;
		String type = "fivep";
		String subSeq;
		
		subSeq = sequence.getSeqFragmentStart();
		
		fivepOligo = calTm(subSeq, type);
				

		return fivepOligo;		
	}

	/**
	 * Calculate the three prime closed oligo
	 * @param sequence  A Sequence object
	 * @return  An Oligo object represents a 3p closed oligo
	 */
	public Oligo calculateThreepCloseOligo (Sequence sequence) throws FlexDatabaseException
	{
		Oligo threepOligo = null;
		String type = "threeStop";
		String subSeq;
		//System.out.println("The 3s fragment is:");
		//System.out.println(sequence.getSeqFragmentStop());
		//convert seq fragment to its reverse compliment
		subSeq = getReverseComplement(sequence.getSeqFragmentStop());
		threepOligo = calTm(subSeq, type);

		return threepOligo;		
	} 
 
	/**
	 * Calculate the three open prime oligo
	 * @param sequence  A Sequence object
	 * @return  An Oligo object represents a 3p open oligo
	 */
	public Oligo calculateThreepOpenOligo (Sequence sequence) throws FlexDatabaseException
	{
		Oligo threepOpenOligo = null;
		String type = "threeOpen";
		String subSeq;		

		subSeq = sequence.getSeqFragmentStop();
		//get rid of the stop codon at the end of the seq
		subSeq = subSeq.substring(0, (subSeq.length()-3));

		//System.out.println("The 3op fragment is:");
		//System.out.println(subSeq);

		subSeq = getReverseComplement(subSeq);
		threepOpenOligo = calTm(subSeq, type);

		return threepOpenOligo;		
	} 

	public static void main(String[] args)
	{
		NNPrimerCalculator calculator = new NNPrimerCalculator();
		try {
			calculator.test();
		} catch (FlexDatabaseException e) {
			System.err.println(e.getMessage());
		}
	}

	//Test methuod for oligo calculation
	public void test () throws FlexDatabaseException
	{	
		String seqText = "ATGGCGTTTCTCCGAAGCATGTGGGGCGTGCTGACTGCCCTGGGAAGGTCTGGAGCAGAGCTGTGCACCGGCTGTGGAAGTCGACTGCGCTCCCCCTTCAGGTAG";
		String seqID = "HSQ1";

		int Start = 0;
		int Stop = 104;
		Sequence testSeq = new Sequence(seqID,Start,Stop,seqText);
		Oligo result = calculateFivepOligo(testSeq);
		System.out.println(result.getSequence());
		System.out.println(result.getOligoLength());
		System.out.println(result.getGatewaySequence());
		System.out.println(result.getType());
		System.out.println(result.getTm());

		Oligo result1 = calculateThreepCloseOligo(testSeq);
		System.out.println(result1.getSequence());
		System.out.println(result1.getOligoLength());
		System.out.println(result1.getGatewaySequence());
		System.out.println(result1.getType());
		System.out.println(result1.getTm());

		Oligo result2 = calculateThreepOpenOligo(testSeq);
		System.out.println(result2.getSequence());
		System.out.println(result2.getOligoLength());
		System.out.println(result2.getGatewaySequence());
		System.out.println(result2.getType());
		System.out.println(result2.getTm());
	}

}
