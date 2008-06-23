//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * SequenceManipulation.java
 *
 * Created on October 21, 2002, 5:26 PM
 */

package psi_data_converter.util;



import java.io.*;
import java.util.*;
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
    
    //translation tables
    public  static final int TRANSLATION_TABLE_STANDARD = 0;
    
    
    
    public static int[]    complimentScores( int[] quality_scores_query )
    {
        int res[] = new int[quality_scores_query.length];
        int elements = quality_scores_query.length-1;
        for (int count = elements; count >= 0; count--)
        {
            res[elements - count] = quality_scores_query[count];
        }
        return res;
    }
    
    
   
    //function creates complimentary strend for the given one
    public static String getCompliment(String old)
    {
        StringBuffer newStr = new StringBuffer();
        char[] ch_array = old.toCharArray();
        for(int count =  ch_array.length -1 ; count >=0; count--)
        {
            switch(ch_array[count])
            {
                case 'a': case 'A':              {newStr.append( "T");break;}
                case 't' : case 'T': case  'u': case 'U':                {newStr.append("A");break;}
                case  'c' : case  'C':                {newStr.append("G");break;}
                case  'g' : case  'G':                {newStr.append("C");break;}
                case  'n' : case  'N':                {newStr.append("N");break;}
                case '['  :                { newStr.append(']'); break;}
                case ']':                { newStr.append('['); break;}
                case '/' :                { newStr.append(ch_array[count]); break;}
                
            }
         
        }
        return newStr.toString();
    }
    
    
    public static String getComplimentCaseSencetive(String old)
    {
        StringBuffer newStr = new StringBuffer();
        char[] ch_array = old.toCharArray();
        for ( int count =  ch_array.length - 1; count >= 0;count--)
        {
             switch(ch_array[count])
            {
                case 'a':{newStr.append( "t");break;}
                case 'A':{newStr.append( "T");break;}
                case 't' : case  'u':    {newStr.append("a");break;}
                case 'T': case 'U':   {newStr.append("A");break;}
                case  'C': {newStr.append("G");break;}
                case  'c' :   {newStr.append("g");break;}
                case  'g' :  {newStr.append("c");break;}
                case  'G':   {newStr.append("C");break;}
                case  'n' :  {newStr.append("n");break;}
                 case  'N':          {newStr.append("N");break;}
                case  'x':          {newStr.append("x");break;}
                 case  'X':          {newStr.append("X");break;}
                case '['  :                { newStr.append(']'); break;}
                case ']':                { newStr.append('['); break;}
                case '/' :            { newStr.append(ch_array[count]); break;}
                
            }
         }
        return newStr.toString();
    }
    
    public static boolean       isBaseAllowedByAmbiguity(char s_allel, char q_allel)
    {
        switch ( s_allel )
        {
            case 'B': if (q_allel == 'C' || q_allel=='G' || q_allel == 'T') return true; else return false;
            case 'D': if (q_allel == 'A' || q_allel=='G' || q_allel == 'T') return true;else return false;//=a, g, t
            case 'H': if (q_allel == 'A' || q_allel=='C' || q_allel == 'T') return true;else return false;//=a, c, t
            case 'K': if ( q_allel=='G' || q_allel == 'T') return true;else return false;//=g, t
            case 'M': if (q_allel == 'C' || q_allel=='A' ) return true;else return false;//=a, c
            case 'R': if (q_allel == 'A' || q_allel=='G' ) return true;else return false;//=a, g
            case 'S': if (q_allel == 'C' || q_allel=='G' ) return true;else return false;//=c, g
            case 'V': if (q_allel == 'C' || q_allel=='G' || q_allel == 'A') return true;else return false;//=a, c, g
            case 'W': if (q_allel == 'A' || q_allel == 'T') return true;else return false;//=a, t
            case 'Y': if (q_allel == 'C' ||  q_allel == 'T') return true;else return false;//=c, t
            default: return false;


        }
    }
    public static boolean       isValidDNASequence(String sequence)
    {
        char[] bases = sequence.toCharArray();
        for( int count = 0; count < bases.length; count++)
        {
            switch(bases[count])
            {
                case 'a': case 'A':
                case 't' : case  'u':  
                case 'T': case 'U':   
                case  'C':  case  'c' :   
                case  'g' :    case  'G': 
                case 'n':case 'N':
                { break;  }
                default:{ return false;}
            }
        }
       return true;
    }
    
     public static boolean       isValidDNASequenceFrameshiftIncluded(String sequence)
    {
        char[] bases = sequence.toCharArray();
        for( int count = 0; count < bases.length; count++)
        {
            switch(bases[count])
            {
                case 'a': case 'A':
                case 't' : case  'u':  
                case 'T': case 'U':   
                case  'C':  case  'c' :   
                case  'g' :    case  'G': 
                case 'n':case 'N':
                case '-':
                { break;  }
                default:{ return false;}
            }
        }
       return true;
    }
    
    
      public static String getSequenceTranslation(String seq)
      {
        return getSequenceTranslation( seq, ONE_LETTER_TRANSLATION_NO_SPACE, TRANSLATION_TABLE_STANDARD);
      }
      public static String getSequenceTranslation(String seq, int coding_table)
    {
        return getSequenceTranslation( seq, ONE_LETTER_TRANSLATION_NO_SPACE, coding_table);
      }
     public static String getSequenceTranslation(String seq, int mode, int coding_table)
    {
        StringBuffer newSeq = new StringBuffer();
        StringBuffer codon = new StringBuffer();
        char[] char_arr = seq.toCharArray();
        int i = 0;
        while ( i + 2 <  char_arr.length)
        {
          
           codon = new StringBuffer();
           codon.append( char_arr[i++] );
           codon.append( char_arr[i++] );
           codon.append( char_arr[i++] );
           newSeq.append( getTranslation(codon.toString(),  mode, coding_table) );
        }
        return newSeq.toString();
    }
    //function translate sequence to the amino acid
    //mode specify if output will in one / three letter format
      public static String getTranslation(String old, int mode)
    {
        return getTranslation( old,  mode, TRANSLATION_TABLE_STANDARD);
      }
    public static String getTranslation(String old, int mode, int coding_table)
    {
        StringBuffer  newSeq = new StringBuffer(old.length());
        int number_of_codons = old.length() / 3;
        int chPos = 0;
        for( int pos = 0; pos < number_of_codons; pos++)
        {
            String res = translateToAminoAcid(old.charAt(chPos++), old.charAt(chPos++), old.charAt(chPos++), mode, coding_table) ;
            //if no translation is availabel return null for non amb case
            if (res.equals("") )
            {
                if ( old.indexOf("-") != -1) return "";
                else if (old.indexOf("-") == -1 && old.indexOf("N") != -1) return "X";
            }
            newSeq.append(res );
        }
        return newSeq.toString();
    }
    
    
     public static  String translateCodonToAminoAcid(String codon)
    {
        return translateCodonToAminoAcid( codon, TRANSLATION_TABLE_STANDARD);
     }
    public static  String translateCodonToAminoAcid(String codon, int coding_table)
    {
        if (codon.length() != 3) return "";
        int first_index = getIndexOfBase(codon.charAt(0));
        int second_index = getIndexOfBase(codon.charAt(1));
        int third_index = getIndexOfBase(codon.charAt(2));
        
        if (first_index == 4 || second_index == 4 || third_index ==4) return "";
        int codon_index = first_index * 16 + second_index * 4 + third_index;
        
        if ( coding_table == TRANSLATION_TABLE_STANDARD)
            return  "" + amino_acid_symbol_names_standard[codon_index];
        else
            return "";
        
    }
    
    //***************************************************************************
    
    private static  String translateToAminoAcid(char firstCh,
    char secondCh,
    char thirdCh, int mode, int coding_table)
    {
        int first_index = getIndexOfBase(firstCh);
        if (first_index >= 4)
        {        return "";        }
        
        int second_index = getIndexOfBase(secondCh);
        if (second_index >= 4)
        {        return "";        }
        
        int third_index = getIndexOfBase(thirdCh);
        if (third_index >= 4)
        {        return "";        }
        
        int codon_index = first_index * 16 + second_index * 4 + third_index;
        if ( coding_table == TRANSLATION_TABLE_STANDARD)
        {
            if (mode == THREE_LETTER_TRANSLATION)
                return amino_acid_abbreviated_names_standard[codon_index];
            else if ( mode == ONE_LETTER_TRANSLATION_NO_SPACE)
                return  "" + amino_acid_symbol_names_standard[codon_index];
            else
                return  " " + amino_acid_symbol_names_standard[codon_index] + " ";
        }
        else
            return "";
    }
    
    
    
   /**
     *  Given a base letter return its index where t = 0, c = 1, a = 2, g = 3, 4
     *  otherwise.
     *  See letter_index.
     **/
    private  static int getIndexOfBase( char base)
    {
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
    private  static int getSymbolIndex(char one_letter_code)
    {
        switch (one_letter_code)
        {
            
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
                throw new Error("Internal error - illegal one letter codon symbol: " +
                one_letter_code);
        }
    }
    
    
    
    
   
    
    
    /**
     *  This table is used for  lookup of codon translations.
     *  There is one entry for each codon and the
     *  entries are in this order: TTT, TTC, TTA, TTG, TCT, TCC, ...
     **/
    public final static   char[] amino_acid_symbol_names_standard =
    {
        'F', 'F', 'L', 'L',
        'S', 'S', 'S', 'S',
        'Y', 'Y', '*', '*',
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
    
    public  static final  String [] amino_acid_abbreviated_names_standard =
    {
        "Phe", "Phe", "Leu", "Leu",
        "Ser", "Ser", "Ser", "Ser",
        "Tyr", "Tyr", "*", "*",
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
    
    
    
    public static String     getReTranslatedSequence(String seq)
       {
           StringBuilder  nseq = new StringBuilder(seq.length());
           seq=seq.toUpperCase();
           char [] seq_letters = seq.toCharArray();String[] res;
           for(char letter : seq_letters)
           {
               res=SequenceManipulation.getPossibleNuckleotideTranslation(letter);
               nseq.append(res[res.length-1]);
           }
           return nseq.toString();

       }
    
   public static String[] getPossibleNuckleotideTranslation(char aa)
   {
        switch (aa)
        {
          case 'A': { String[] result ={"GCT","GCA","GCG","GCC"	};     return result;}
case 'B': { String[] result ={""	};     return result;}
case 'C': { String[] result ={"TGT","TGC"	};     return result;}
case 'D': { String[] result ={"GAT","GAC"};     return result;}
case 'E': { String[] result ={"GAA","GAG"	};     return result;}
case 'F': { String[] result ={"TTT","TTC"	};     return result;}
case 'G': { String[] result ={"GGT","GGA","GGG","GGC"	};     return result;}
case 'H': { String[] result ={"CAT","CAC"	};     return result;}
case 'I': { String[] result ={"ATT","ATA","ATC"	};     return result;}
case 'J': { String[] result ={""	};     return result;}
case 'K': { String[] result ={"AAA","AAG"	};     return result;}
case 'L': { String[] result ={"TTG","TTA","CTT","CTC","CTA","CTG" 	};     return result;}
case 'M': { String[] result ={"ATG"	};     return result;}
case 'N': { String[] result ={"AAT","AAC"	};     return result;}
case 'O': { String[] result ={""	};     return result;}
case 'P': { String[] result ={"CCT","CCA","CCG","CCC"	};     return result;}
case 'Q': { String[] result ={"CAA","CAG"	};     return result;}
case 'R': { String[] result ={"CGT","CGC","CGA","CGG","AGG"	,"AGA"};     return result;}
case 'S': { String[] result ={"TCT","TCC","TCA","TCG","AGT","AGC"	};     return result;}
case 'T': { String[] result ={"ACT","ACA","ACG","ACC"	};     return result;}
case 'U': { String[] result ={""	};     return result;}
case 'V': { String[] result ={"GTT","GTC","GTA","GTG"};     return result;}
case 'W': { String[] result ={"TGG"	};     return result;}
case 'X': { String[] result ={""	};     return result;}
case 'Y': { String[] result ={"TAT","TAC"	};     return result;}
case 'Z': { String[] result ={""	};     return result;}
case '.': { String[] result ={""};     return result;}
case '*': { String[] result ={"TAA","TAG","TGA"	};     return result;}
           default:{ String[] result ={"_"};     return result;}
       }
     
   }
     //************************************************************************
    
    
    public static void main(String [] args)
    {
        String s = "GTACAAGAAAGCTGGGTCCTACGGTTCCAGTTCGACATAAAGATGCAAGCGCGGTACCAGCAAACGTAGCGGTTTCGCATCAGGCTTATATTTATGCTTGATATGCTTCACATCATAATCGGTCAACTCGCCTAATTTCGGTAGCTCGGCCCCTAACTGATGCTGACAGAACGCAATCAGGGGCATGGGGCAGGGGTGTTGTACCTGTTTTTGTTGTTCTTGATACCATACTCTTGCGATGGGCTGCACTTTTACGGGCCTTTTAATTTTCAGCCGTGTCTTTTCTGGCAAACGCTGCACATCATTTATTTCCAAGCTAATCAGTTCCCTCCATTGCTCACGCGTAAATGGAGGAACAGCTCGGCCAGCATTCAGGCTTTTTTCTAGCTGTAGCAATATATCTTCGCGAGTCACATTTTTAATAATGTGTTTGTTGGCCCAGCCAAAGCGCACTGAACTGGGGTTGATTAATGGGGTAATGGTGCGGTAGGTATTCAGGGTTATCAGGCCATGTAAGTGTGTGTGAACAAATTCAAAGCGTTGTTCGCTGGGTAAGCCTGATTCCACAGTAATAATATGTTCTAACTCAGCTTTTAACTGATTGGTTTTTTTTATTATATCTTGGCATTCAATGAGTTCTTTGTCGGTCACGGAGAAGCAGAGCACGCCAGGTAAGCGTAGTGCTGCTTTACTGCTAACATGCTGCCCTTGGTGATGAAGAAACAATCGTTGGAAATGTTGTAACCCCAACTTTTTGGCGTGTTCACCCTCGGTTATATTGACGGTAATTTGCTCAATGGGTTGATGCTCAGTGCCTTTTTCTATTTCAGGGAGAGAAAATACTCTGGCAGCAATTAGAGGTAAATCGTCCAATTGCTGGTGGAGTTGATGAAGTGTTACTTCTAACTCAGCAAAGCGAGTATTCATTCGTTCTATCAAATCATACTTGTTCATATTCTTTTCTAAATAAGGCGGCTTAGTTACAACATACGGTGGAGCCTGCTTTTTTGTAC";
         
        s="MFENEILFDNGQHKFMFLGWEEKEEEIVQTNQYLILDGNEGILLDPGGAHVFPRVMSNVAEVVDLSSIRHIFYTHQDPDVTSGILLWLSICENAKIYISSLWVRFLPHFGIYDQKRIVPISDKGTKIKLLSGNELEILPAHFLHSTGNFVLYDPVAKILFSGDIGAAVFEKGKRYRYVDDFERHLPLMEAFHKRYMSSNAACKKWVDMVSKKKIDMIAPQHGAVFRGESVKKFLEWFRNLKCGVDLIDNLYSL";
        //System.out.println(s); 
        System.out.println(SequenceManipulation.getReTranslatedSequence(s)); 
        
        
        String a1=SequenceManipulation.getReTranslatedSequence(s);

String a2="ATGTTCGAGAACGAGATCCTGTTCGACAACGGCCAGCACAAGTTCATGTTCCTGGGCTGGGAGGAGAAGGAGGAGGAGATCGTGCAGACCAACCAGTACCTGATCCTGGACGGCAACGAGGGCATCCTGCTGGACCCCGGCGGCGCCCACGTGTTCCCCAGAGTGATGAGCAACGTGGCCGAGGTGGTGGACCTGAGCAGCATCAGACACATCTTCTACACCCACCAGGACCCCGACGTGACCAGCGGCATCCTGCTGTGGCTGAGCATCTGCGAGAACGCCAAGATCTACATCAGCAGCCTGTGGGTGAGATTCCTGCCCCACTTCGGCATCTACGACCAGAAGAGAATCGTGCCCATCAGCGACAAGGGCACCAAGATCAAGCTGCTGAGCGGCAACGAGCTGGAGATCCTGCCCGCCCACTTCCTGCACAGCACCGGCAACTTCGTGCTGTACGACCCCGTGGCCAAGATCCTGTTCAGCGGCGACATCGGCGCCGCCGTGTTCGAGAAGGGCAAGAGATACAGATACGTGGACGACTTCGAGAGACACCTGCCCCTGATGGAGGCCTTCCACAAGAGATACATGAGCAGCAACGCCGCCTGCAAGAAGTGGGTGGACATGGTGAGCAAGAAGAAGATCGACATGATCGCCCCCCAGCACGGCGCCGTGTTCAGAGGCGAGAGCGTGAAGAAGTTCCTGGAGTGGTTCAGAAACCTGAAGTGCGGCGTGGACCTGATCGACAACCTGTACAGCCTG"; 
            char[] aa1=a1.toUpperCase().toCharArray();
    char[] aa2=a2.toUpperCase().toCharArray();
    String a1_c=""; String a2_c="";
    for (int c = 0; c < aa1.length;c++)
    {
        
        if (c!=0 && c % 3 == 0 && !a1_c.equals(a2_c)) 
            System.out.println(c +" "+a1_c+" "+a2_c);
        if (c % 3 == 0 && c!=0){a1_c="";a2_c="";}
        a1_c+=aa1[c];a2_c+=aa2[c];
        
    }
    
    }
}
