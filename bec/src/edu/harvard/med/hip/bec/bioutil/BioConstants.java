/*
 * Constants.java
 *
 * Created on October 19, 2002, 8:26 AM
 */

package edu.harvard.med.hip.bec.bioutil;

/**
 *
 * @author  Administrator
 */
public class BioConstants {
    
    
    public static final int SUBSTITUTION_TABLE_BLOSSOM62 = 0;
    /** Creates a new instance of Constants */
   /*#  Matrix made by matblas from blosum62.iij#  
    * column uses minimum score#  BLOSUM Clustered 
    Scoring Matrix in 1/2 Bit Units#  Blocks Database 
    = /data/blocks_5.0/blocks.dat#  Cluster Percentage: >= 62# 
    Entropy =   0.6979, Expected =  -0.5209   
    */
    
    private static final char[] AA= {  'A',  'R',  'N',  'D'  ,'C' , 'Q', 'E' , 'G', 'H',  'I',  'L',  'K',
                'M',  'F',  'P',  'S',  'T',  'W',  'Y',  'V',  'B',  'Z',  'X',  '*'};
                
    private static final int[][] BLOSSOM_62= {
        {4,-1,-2,-2,0,-1,-1,0,-2,-1,-1,-1,-1,-2,-1,1,0,-3,-2,0,-2,-1,0,-4},
        {-1,5,0,-2,-3,1,0,-2,0,-3,-2,2,-1,-3,-2,-1,-1,-3,-2,-3,-1,0,-1,-4},
        {-2,0,6,1,-3,0,0,0,1,-3,-3,0,-2,-3,-2,1,0,-4,-2,-3,3,0,-1,-4},
        {-2,-2,1,6,-3,0,2,-1,-1,-3,-4,-1,-3,-3,-1,0,-1,-4,-3,-3,4,1,-1,-4},
        {0,-3,-3,-3,9,-3,-4,-3,-3,-1,-1,-3,-1,-2,-3,-1,-1,-2,-2,-1,-3,-3,-2,-4},
        {-1,1,0,0,-3,5,2,-2,0,-3,-2,1,0,-3,-1,0,-1,-2,-1,-2,0,3,-1,-4},
        {-1,0,0,2,-4,2,5,-2,0,-3,-3,1,-2,-3,-1,0,-1,-3,-2,-2,1,4,-1,-4},
        {0,-2,0,-1,-3,-2,-2,6,-2,-4,-4,-2,-3,-3,-2,0,-2,-2,-3,-3,-1,-2,-1,-4},
        {-2,0,1,-1,-3,0,0,-2,8,-3,-3,-1,-2,-1,-2,-1,-2,-2,2,-3,0,0,-1,-4},
        {-1,-3,-3,-3,-1,-3,-3,-4,-3,4,2,-3,1,0,-3,-2,-1,-3,-1,3,-3,-3,-1,-4},
        {-1,-2,-3,-4,-1,-2,-3,-4,-3,2,4,-2,2,0,-3,-2,-1,-2,-1,1,-4,-3,-1,-4},
        {-1,2,0,-1,-3,1,1,-2,-1,-3,-2,5,-1,-3,-1,0,-1,-3,-2,-2,0,1,-1,-4},
        {-1,-1,-2,-3,-1,0,-2,-3,-2,1,2,-1,5,0,-2,-1,-1,-1,-1,1,-3,-1,-1,-4},
        {-2,-3,-3,-3,-2,-3,-3,-3,-1,0,0,-3,0,6,-4,-2,-2,1,3,-1,-3,-3,-1,-4},
        {-1,-2,-2,-1,-3,-1,-1,-2,-2,-3,-3,-1,-2,-4,7,-1,-1,-4,-3,-2,-2,-1,-2,-4},
        {-1,1,0,-1,0,0,0,-1,-2,-2,0,-1,-2,-1,4,1,-3,-2,-2,0,0,0,-4},
        {0,-1,0,-1,-1,-1,-1,-2,-2,-1,-1,-1,-1,-2,-1,1,5,-2,-2,0,-1,-1,0,-4},
        {-3,-3,-4,-4,-2,-2,-3,-2,-2,-3,-2,-3,-1,1,-4,-3,-2,11,2,-3,-4,-3,-2,-4},
        {-2,-2,-2,-3,-2,-1,-2,-3,2,-1,-1,-2,-1,3,-3,-2,-2,2,7,-1,-3,-2,-1,-4},
        {0,-3,-3,-3,-1,-2,-2,-3,-3,3,1,-2,1,-1,-2,-2,0,-3,-1,4,-3,-2,-1,-4},
        {-2,-1,3,4,-3,0,1,-1,0,-3,-4,0,-3,-3,-2,0,-1,-4,-3,-3,4,1,-1,-4},
        {-1,0,0,1,-3,3,4,-2,0,-3,-3,1,-1,-3,-1,0,-1,-3,-2,-2,1,4,-1,-4},
        {0,-1,-1,-1,-2,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2,0,0,-2,-1,-1,-1,-1,-1,-4},
        {-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,-4,1}};
    public static int getScore(char ori, char mut, int substitution_table)
    {
        if ( substitution_table == SUBSTITUTION_TABLE_BLOSSOM62)
            return BLOSSOM_62[getIndex(ori)][getIndex(mut)];
        else 
            return 0;
    }
    public static int getScore(char ori, char mut)
    {
        return getScore( ori,  mut, SUBSTITUTION_TABLE_BLOSSOM62);
    }
    
    private static int getIndex(char a)
    {
         int res =-1;
         for (int i = 0; i < AA.length; i++)
         {
             if (a == AA[i])
            {
                res = i;
                break;
             }
         }
         return res;
    }
    
    /*
    The amino acid codes are IUPAC recommendations for common amino acids:   
        A           Ala            Alanine     
        R           Arg            Arginine      
        N           Asn            Asparagine      
        D           Asp            Aspartic acid   
        C           Cys            Cysteine          
        Q           Gln            Glutamine         
        E           Glu            Glutamic acid     
        G           Gly            Glycine         
        H           His            Histidine        
        I           Ile            Isoleucine       
        L           Leu            Leucine         
        K           Lys            Lysine          
        M           Met            Methionine       
        F           Phe            Phenylalanine      
        P           Pro            Proline         
        S           Ser            Serine         
        T           Thr            Threonine       
        W           Trp            Tryptophan      
        Y           Tyr            Tyrosine        
        V           Val            Valine        
        B           Asx            Aspartic acid or Asparagine     
        Z           Glx            Glutamine or Glutamic acid      
        X           Xaa            Any or unknown amino acid
        */
}
