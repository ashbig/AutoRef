/**
 * $Id: NNPrimerCalculator.java,v 1.17 2002-09-30 16:54:28 Elena Exp $
 * Neariest Neighborhood algorithm is used for current oligo primer calculation
 *
 * modified 12/13/01 All of the stop (close) oligos now use the universal stop
 * codon "TAG" instead of the native stop codon
 *
 * @File     	: NNPrimerCalculator.java
 * @Date     	: 04162001
 * @author	: Wendy Mar
 */

package edu.harvard.med.hip.flex.util;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import java.math.*;

public class NNPrimerCalculator implements PrimerCalculator
{
    private static final double R = 1.9872;     // Gas Constant
    private static final double InitH = 0.6;    // Initial H value
    private static final double InitS = -9.0;   // Initial S value
    private static double DESIREDTM = 60.0;  // The desired Tm is around 60 C
    private static final String  UniversalStop = "CTA"; //used for the Pseudomonas project
    
    private double[][] paramH;
    private double[][] paramS;
    
    
    private static final String TYPE_3_OPEN = "TYPE_3_OPEN";
    private static final String TYPE_3_CLOSED = "TYPE_3_CLOSED";
    private static final String TYPE_5_OPEN = "TYPE_5_OPEN";
    //private double[][] paramG;
    
    /**
     * Constructor.  Returns a NNPrimerCalculator object
     * @return  A NNPrimerCalculator object
     */
    public NNPrimerCalculator( )
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
     * Calculate the five prime oligo
     * @param sequence  A Sequence object
     * @return  An Oligo object represents a 5p oligo
     */
    public Oligo calculateFivepOligo(Sequence sequence) throws FlexDatabaseException
    {
        
        String subSeq = sequence.getSeqFragmentStart();
        System.out.println("seq 5p fragment: "+ subSeq);
        return calTm(subSeq, TYPE_5_OPEN);
    }
    
    /**
     * Calculate the three prime closed oligo
     * @param sequence  A Sequence object
     * @return  An Oligo object represents a 3p closed oligo
     */
    public Oligo calculateThreepCloseOligo(Sequence sequence) throws FlexDatabaseException
    {
     
        //convert seq fragment to its reverse compliment
        String subSeq = getReverseComplement(sequence.getSeqFragmentStop());
         System.out.println("The 3s reverse fragment is:" + subSeq);
        //new algorithem: get rid of the native stop codon
        //and use a universal stop "TAG" instead
        subSeq = UniversalStop + subSeq.substring(0, (subSeq.length()-3));
        System.out.println("The 3 final: " + subSeq);
        return calTm(subSeq, TYPE_3_CLOSED);
    }
    
    /**
     * Calculate the three open prime oligo
     * @param sequence  A Sequence object
     * @return  An Oligo object represents a 3p open oligo
     */
    public Oligo calculateThreepOpenOligo(Sequence sequence) throws FlexDatabaseException
    {
        String subSeq = null;
      
        subSeq = sequence.getSeqFragmentStop();
        System.out.println("The 3 final: " + subSeq);
        //get rid of the stop codon at the end of the seq
        subSeq = subSeq.substring(0, (subSeq.length()-3));
        
        //System.out.println("The 3op fragment is:");
        //System.out.println(subSeq);
        
        subSeq = getReverseComplement(subSeq);
        System.out.println("The 3 final: " + subSeq);
        return calTm(subSeq, TYPE_3_OPEN);
    }
    
    
    
    public double getDesiredTM()
    {     return DESIREDTM;       }
    public int adjustPosition(int pos)
    {    return pos;       }
    
    //*********************************private members ************************
    /**
     * Converts the DNA neucleotide dimers into digital combanations
     * @param dimer  A String object
     * @return A String object
     */
    private int convert(char dimer)
    {
        switch (dimer)
        {
            case 'A': case 'a':  return 0;
            case 'T': case 't':  return 1;
            case 'C': case 'c':  return 2;
            default:          return 3;
        }
    }
    
    /**
     * This function takes a string up to 60 bases from the sequence
     * and calculate the oligo primer sequence and the Tm
     * @param subSeq   A String object represents a 60-base
     * fragment of the Sequence text.
     * @param oligoType A String object indicates the types of oligos
     * @return A Oligo object
     */
    private Oligo calTm(String subSeq, String oligoType) throws FlexDatabaseException
    {
        //System.out.println("sequence length is: "+subSeq.length());
        double Tm = 0;
        double preTm = 0;
        double totalH = InitH;
        double totalS = InitS;
        int pos = 0;  // keep track the length of oligo, starting from position 0
        // a two-base dimer window sliding through the sequence
        int dimerFirst;
        int dimerSecond;
        
        String oligoSeq; // the sequence string for oligo primers
        int indexOne = 0; // the first index for the two-dimensional array paramH and paramS
        int indexTwo = 0; // the second index for the two-dimensional array paramH and ParamS
        Oligo oligo = null;
     
        while (Tm < DESIREDTM)
        {
       
            //System.out.println("TM is: "+Tm);
            //System.out.println("position is: "+pos);
            if (  (pos + 2 ) >= subSeq.length() )
            {
                pos = subSeq.length() -1 ;
                break;
            }
            preTm = Tm; 
     
            dimerFirst = convert( subSeq.charAt(pos) ); //slide dimers from seq
            dimerSecond = convert( subSeq.charAt(pos + 1) );
            //System.out.println(dimer);
            //System.out.println(indices);
            totalH += paramH[dimerFirst][dimerSecond];
            totalS += paramS[dimerFirst][dimerSecond];
            
            //standard condition: [Na+] is 50mM, Thus, Log[Na+]= -1.301
            //The salt adjusting factor is 12.5
            //For the old formula, [DNA] is 100 uM, thus 0.1mM
            //For the current formula, [DNA] is 0.2 uM, ln(C)=-15.4
            Tm = totalH * 1000/(totalS - R*15.4)-273.16 - 12.5 * 1.301;
            pos++;
        } //while
        
        //determine whether the Tm or the preTm is closer to DesiredTM
        if (Math.abs(DESIREDTM - Tm) >= Math.abs(DESIREDTM - preTm))
        {
            Tm = preTm;
            pos = pos - 2;
        } //if
        
        // Tm calculation for oligos less than 21 bases seem to be overestimated
        // Tm should not be around 60 C as calculated, actual Tm is around 55 C instead
        // Also, oligos less than 18 bases are not desirable due to high PCR non-specific binding
        // The length of short oligos are adjusted
           // Tm calculation for oligos more than 38 bases seem to be underestimated
        // Also, longer primers tend to form internal loops
        // All the oligos should be no more 42 bases long
        if ((pos == 19) || (pos == 20))       { pos = 21; }
        else if((pos == 17) || (pos == 18))   { pos = 20; }
        else if((pos == 15) || (pos == 16))   { pos = 19; }
        else if(pos <= 14)                    { pos = 18; }
        else if (pos > 38)                    { pos = 38; }
        
        // The oligo sequence is the substring of parameter seq50
        // Always replace the first three chars with ATG if it is 5p
        if(TYPE_5_OPEN ==oligoType)
        {
           oligoSeq = "ATG" + subSeq.substring(3, pos+1);
        } 
        else
        {
            oligoSeq = subSeq.substring(0, pos+1);
        }
        
        oligo = new Oligo(oligoType, oligoSeq, Tm);
        //System.out.println("OligoId: " + oligoId);
        //System.out.println("OligoSeq: " + oligoSeq);
        
        return oligo;
    } //calTm
    
    
    /**
     * This function takes a nucleotide base
     * and converts it into its complementary base.
     * @param base  A String object
     * @return A String object
     */
    
    private char getComplementBase(char base)
    {
        
        switch (base)
        {
            case 'A': case 'a' : return 'T';
            case 'C': case 'c' : return 'G';
            case 'G': case 'g' : return 'C';
            case 'T': case 't' : return 'A';
            default:          return base;
        }
    } // getComplementBase
    
    
    
    
    /**
     * This function takes the reversed sequence
     * and converts it into a reverse complement sequence.
     * @param seq  A String object
     * @return A String object
     */
    private String getReverseComplement(String seq)
    {
        String result = "";
        String reverseSeq = Algorithms.reverseString(seq);
        for (int i = 0; i < reverseSeq.length(); ++i)
        {
            result += getComplementBase(reverseSeq.charAt(i));
        } // for
        return result;
    } // getReverseComplement
    
    
    
    //**************************************************************
    
    //Test methuod for oligo calculation
    public void test() throws FlexDatabaseException
    {
        /*
        String seqText = "ATGGCGTTTCTCCGAAGCATGTGGGGCGTGCTGACTGCCCTGGGAAGGTCTGGAGCAGAGCTGTGCACCGGCTGTGGAAGTCGACTGCGCTCCCCCTTCAGGTAG";
        seqText += "CGGGAGGCGGGGACCCACCTGGAAGCGCCGCGGCGCCGCTATCGAGCTTCCTGCAGCGGTGGCCACCCGAGCAAGTGCCGTGGCGGGGGCGGAGAGCGGCCACGGCGGCGGCGCCTCCCCAAGTGGCCCGTTGCGTCCGACTCCAGCCTGGCAACAGAGCGAGACTCCATCTCAAATAAATAAATAAATAAATAAATAAATAAATAAATAAATAAATAAAAATGTGGAATGAATTAGGCAAGTTGGGCTGCTAATGCCTTGCCACTGAATTGAACAGCCACAGACAAACGAGAATGCACTTCTCAGGGCAAAAGAACAAATATTGATGAAGTCAATCCCAACATGCTCATTCCTTTTCCCTAATCTCATCTATTAGATGAGTTCCTCCTTCTCCCAAAGAGGAGTAGGTGAGAGGAGGTGAGAAAGAGGCCATGTCCCACTCTCCTGTGCTTCCAGGGATCAGAATTTCCCTCCCTATTAGGGAAATGCGTTTAAAAAAATTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTAAAAGTTGAGGAGTTTATTAGGGAAATATGAGAGGCATAGACACTCCAAGTGACAGAAAGAAAAGTCTGAAAATGTCCCTTCAAGCCAAGTGGGGGCCTGGCCTTGACCTCTCCAAATCAACAAGAAACTGGTGGGTTAGCAACAACATTCTCTGGCAGCCACATTGCCAGGGCATGAGTGTCTTGACCAGGACTGCCCCGCACTTCCCACCAAAGGTGGGGAGGAGACAAAGACTGTTCACAGAAGCAGTGCAAAGGCAATGAGAACTTTAAGGAAAGTTTGAGAGAGAGAGAAAGAAAGATAGAGGTGAGGAGGACCTTCACAAAGAGTCCCAGGCTTTTGGCTGTGAATGTCTCAAATACATTGACAAGTAGATGTATAAAATGTTACTGAAAAGGTAAAATGCCTAACGTCGTTTCCAACGGTTCCTCTGAACTTCTTCCCACATACCACACCACACCCCAGATGGCAGAGCCCAAAGGCCACACTTTTGAAAAAAGAAAAACAAGAATAAGCCCTGTTGCTCTTTAAGGAGAAAGGAAGGAGCTGAAGGCTGCTGGGGCCTTTCCCATGTGGCCTGTGTTGTGTAAAGCAACTTCCCAGCAGCAGCACGGCACTGTTCTAGGTGAGTGTCTCACCTTTTGTCACCCGAGCTTCAATGTACTCTATTCTCCGTTCAAGGGCTGTCAATTTCTCGTTTAGTGTTGCAAGTCTTGAACGACAAGACATATCGAACGAGTTGAGAAAGTCTGCGATTTTCTTGATGCTGCTGGTGATTATCTCAATGTACTCCCGGTTAGCCCAGTCCTGGTGAATCTCCCGCTGCACCGGATCCTCCTGTCCCGCCATGGCCG";
        System.out.println("seqText length is: "+seqText.length());
        int seqID = 219;
        
        int Start = 432;
        int Stop = 563;
        Sequence testSeq = new Sequence(seqID, Start, Stop);
         **/
        FlexSequence Seq = new FlexSequence(34166);
        String seqText = Seq.getSequencetext();
        Sequence testSeq = new Sequence(34166,Seq.getCdsstart(),Seq.getCdsstop());
        
        try
        {
              System.out.println("The : " + testSeq.getSeqText(38223) );
            System.out.println("Calculating fivep oligo...");
            Oligo result = calculateFivepOligo(testSeq);
            System.out.println(result.getSequence());
            System.out.println(result.getOligoLength());
            //		System.out.println(result.getGatewayOligoSequence());
     //       System.out.println(result.getType());
            System.out.println(result.getTm());
            
            System.out.println("Calculating threep closed oligo...");
            Oligo result1 = calculateThreepCloseOligo(testSeq);
            System.out.println(result1.getSequence());
            System.out.println(result1.getOligoLength());
            //		System.out.println(result1.getGatewayOligoSequence());
        //    System.out.println(result1.getType());
            System.out.println(result1.getTm());
            
            System.out.println("Calculating threep open oligo...");
            Oligo result2 = calculateThreepOpenOligo(testSeq);
            System.out.println(result2.getSequence());
            System.out.println(result2.getOligoLength());
            //		System.out.println(result2.getGatewayOligoSequence());
        //    System.out.println(result2.getType());
            System.out.println(result2.getTm());
        }
        catch(FlexDatabaseException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    ///*******************************************************************
    public static void main(String [] args)
    {
        NNPrimerCalculator calculator = new NNPrimerCalculator();
        try
        {
            calculator.test();
        } catch (Exception ex)
        {
            System.out.println(ex);
        }
        System.exit(0);
    }
}
