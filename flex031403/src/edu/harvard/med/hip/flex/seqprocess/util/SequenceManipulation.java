/*
 * SequenceManipulation.java
 *
 * Created on October 21, 2002, 5:26 PM
 */

package edu.harvard.med.hip.flex.seqprocess.util;

/**
 *
 * @author  htaycher
 */
public class SequenceManipulation
{
    //type of translation to AA
     public static final int ONE_LETTER_TRANSLATION = 0;
   
    public  static final int THREE_LETTER_TRANSLATION = 1;
    public  static final int ONE_LETTER_TRANSLATION_NO_SPACE = 2;
    
    
    
    
    public static boolean isStopCodon(String codon)
    {
        codon=codon.toUpperCase();
        if (codon.equalsIgnoreCase("TGA") || codon.equalsIgnoreCase("TAA") || 
            codon.equalsIgnoreCase("TAG")  )
            return true;
        else
            return false;
    }
    
    //function creates complimentary strend for the given one
    public static String getCompliment(String old)
    {
        StringBuffer newStr = new StringBuffer();
        char ch;
        int i = old.length() - 1;
        while ( i >= 0 )
        {
            ch = old.charAt(i);
            switch(ch)
            {
                case 'a': case 'A': 
                {newStr.append( "T");break;}
                case 't' : case 'T': case  'u': case 'U':  
                {newStr.append("A");break;}
                case  'c' : case  'C': 
                {newStr.append("G");break;}
                case  'g' : case  'G': 
                {newStr.append("C");break;}
                case '['  : { newStr.append(']'); break;}
                case ']':   { newStr.append('['); break;}
                case '/' : { newStr.append(ch); break;}
                
            }
            i--;
        }
        return newStr.toString();
    }
    //function translate sequence to the amino acid
    //mode specify if output will in one / three letter format
    public static String getTranslation(String old, int mode)
    {
        StringBuffer  newSeq = new StringBuffer(old.length());
        int number_of_codons = old.length () / 3;
        int chPos = 0;
        for( int pos = 0; pos < number_of_codons; pos++)
        {
            String res = translateToAminoAcid(old.charAt(chPos++),
                                                old.charAt(chPos++),
                                                old.charAt(chPos++)
                                                , mode) ;
            newSeq.append(res );
        }
        return newSeq.toString();
    }


 //***************************************************************************
    
    private static  String translateToAminoAcid(char firstCh,
                                                char secondCh,
                                                char thirdCh, int mode)
    {
        int first_index = getIndexOfBase (firstCh);
        if (first_index >= 4) {        return " . ";        }

        int second_index = getIndexOfBase (secondCh);
        if (second_index >= 4) {        return " . ";        }

        int third_index = getIndexOfBase (thirdCh);
        if (third_index >= 4) {        return " . ";        }
        
        int codon_index = first_index * 16 + second_index * 4 + third_index;
        if (mode == THREE_LETTER_TRANSLATION)
            return amino_acid_abbreviated_names[codon_index];
        else if ( mode == ONE_LETTER_TRANSLATION_NO_SPACE)
            return  "" + amino_acid_symbol_names[codon_index];
        else
            return  " " + amino_acid_symbol_names[codon_index] + " ";
    }
    
     /**
   *  Given a base letter return its index where t = 0, c = 1, a = 2, g = 3, 4
   *  otherwise.
   *  See letter_index.
   **/
    private  static int getIndexOfBase ( char base) {
        switch (base) 
        {
            case 't':    case 'u':  case 'T':    case 'U':    return 0;
            case 'c':  case 'C':     return 1;
            case 'a':  case 'A':    return 2;
            case 'g':case 'G':      return 3;
        }
        return 4;
    }
   
    /**
   *  Return integer (0 - 22) representing  index of a codon symbol.
   **/
  private  static int getSymbolIndex (char one_letter_code) {
    switch (one_letter_code) {
                
        case 'A': return 0;
        case 'R': return 1;
        case 'N': return 2;
        case 'D': return 3;
        case 'C': return 4;
        case 'Q': return 5;
        case 'E': return 6;
        case 'G': return 7;
        case 'H': return 8;
        case 'I': return 9;
        case 'L': return 10;
        case 'K': return 11;
        case 'M': return 12;
        case 'F': return 13;
        case 'P': return 14;
        case 'S': return 15;
        case 'T': return 16;
        case 'W': return 17;
        case 'Y': return 18;
        case 'V': return 19;
        case '*': return 20;
        case '#': return 21;
        case '+': return 22;
        case '.': return 23;
        case 'U': return 24;
        default:
        throw new Error ("Internal error - illegal one letter codon symbol: " +
                       one_letter_code);
    }
  }

  
   
    
    
    
    /**
   *  This table is used for  lookup of codon translations.
   *  There is one entry for each codon and the
   *  entries are in this order: TTT, TTC, TTA, TTG, TCT, TCC, ...
   **/
  public final static   char[] amino_acid_symbol_names = {
    'F', 'F', 'L', 'L',
    'S', 'S', 'S', 'S',
    'Y', 'Y', '#', '+',
    'C', 'C', '*', 'W',

    'L', 'L', 'L', 'L',
    'P', 'P', 'P', 'P',
    'H', 'H', 'Q', 'Q',
    'R', 'R', 'R', 'R',

    'I', 'I', 'I', 'M',
    'T', 'T', 'T', 'T',
    'N', 'N', 'K', 'K',
    'S', 'S', 'R', 'R',

    'V', 'V', 'V', 'V',
    'A', 'A', 'A', 'A',
    'D', 'D', 'E', 'E',
    'G', 'G', 'G', 'G',
  };

    public  static final  String [] amino_acid_abbreviated_names = {
        "Phe", "Phe", "Leu", "Leu",
        "Ser", "Ser", "Ser", "Ser",
        "Tyr", "Tyr", "#", "+",
        "Cys", "Cys", "*", "W",

        "Leu", "Leu", "Leu", "Leu",
        "Pro", "Pro", "Pro", "Pro",
        "His", "His", "Gln", "Gln",
        "Arg", "Arg", "Arg", "Arg",

        "Ile", "Ile", "Ile", "Met",
        "Thr", "Thr", "Thr", "Thr",
        "Asn", "Asn", "Lys", "Lys",
        "Ser", "Ser", "Arg", "Arg",

        "Val", "Val", "Val", "Val",
        "Ala", "Ala", "Ala", "Ala",
        "Asp", "Asp", "Glu", "Glu",
        "Gly", "Gly", "Gly", "Gly",
       
    };
    
    
       /**
   *  The molecular weights of the amino acids.  The values correspond to the
   *  three letter codes at the same indices in amino_acid_abbreviated_names.
   *  For example "Met" corresponds to a weight of 149.22
   **/
  private final static float [] molecular_weights = 
  {
    89.09F,  174.21F, 132.12F, 133.10F, 121.15F,
    146.15F, 147.13F, 75.07F,  155.16F, 131.18F,
    131.18F, 146.19F, 149.22F, 165.19F, 115.13F,
    105.09F, 119.12F, 204.22F, 181.19F, 117.15F,
    0.0F,    0.0F,    0.0F,    0.0F,    334.1F
  };
    //************************************************************************
    
    
     public static void main(String [] args) {
         System.out.println(SequenceManipulation.getTranslation("aaatattcaaataaaattccattaattccattgtcggttataataaaaaccttatcgcta",ONE_LETTER_TRANSLATION_NO_SPACE));
     }
}
