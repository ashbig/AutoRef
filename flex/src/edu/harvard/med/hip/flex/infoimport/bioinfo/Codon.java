/*
 * Codon.java
 *
 * Created on July 17, 2007, 4:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.bioinfo;

/**
 *
 * @author htaycher
 */
public class Codon 
{
    
    public      static final int      PROPERTY_NOT_DEFINED = -10;
    public      static final int      PROPERTY_YES = 1;
    public      static final int      PROPERTY_NO  = -1;
    
    private     int         m_is_start_codon = PROPERTY_NO;
    private     int         m_is_stop_codon = PROPERTY_NO;
    private     String          m_nucl_sequence = null;
    private     char              m_protein_sequence = '\u0000';
    private     char                m_base1 ='\u0000';
     private     char                m_base2 ='\u0000';
    private     char                m_base3 ='\u0000';
    private     int             m_index = PROPERTY_NOT_DEFINED;
    
    /** Creates a new instance of Codon */
   
    public Codon(char aa,char start,char base1, char base2, char base3)
    {
        m_is_start_codon = (start == 'M')? PROPERTY_YES: PROPERTY_NO;
        m_is_stop_codon = (aa == '*') ? PROPERTY_YES: PROPERTY_NO;
        m_base1 = base1; m_base2 = base2; m_base3=base3;
        m_nucl_sequence = String.valueOf(base1)+base2+base3;
        m_protein_sequence =  aa;
        m_index = getBaseIndex(base1) * 16 + getBaseIndex(base2) * 4 + base3;
    }
    
    public Codon(String nucl_sequence)throws Exception
    {
        m_nucl_sequence = nucl_sequence;
        m_index =calculateCodonIndex(nucl_sequence);
        if (m_index == -1) throw new Exception("Wrong codon: "+nucl_sequence);
    }
    public int          getIndex(){ return m_index;}
    public int          isStartCodon(){ return m_is_start_codon ;}
    public int          isStopCodon(){ return  m_is_stop_codon;}
    public char         getAATranslation(){ return m_protein_sequence;}
    
     public String      toString()
     {
         return m_nucl_sequence +" "+ m_protein_sequence +" "+
                 ( m_is_start_codon == PROPERTY_NO ? false: true)+" "+ 
                 ( m_is_stop_codon == PROPERTY_NO ? false: true) ;
    
     }
    /////////////////////////////////////////////////////////////
    
    private static  int calculateCodonIndex(String codon)
    {
        if (codon.length() != 3) return PROPERTY_NOT_DEFINED;
        int first_index = getBaseIndex(codon.charAt(0));
        int second_index = getBaseIndex(codon.charAt(1));
        int third_index = getBaseIndex(codon.charAt(2));
        
        if (first_index == PROPERTY_NOT_DEFINED || second_index == PROPERTY_NOT_DEFINED
                || third_index ==PROPERTY_NOT_DEFINED) return PROPERTY_NOT_DEFINED;
        int codon_index = first_index * 16 + second_index * 4 + third_index;
        return codon_index;
    }
    
    
     private  static int getBaseIndex( char base)
    {
        switch (base)
        {
            case 't':    case 'u':  case 'T':    case 'U':    return 0;
            case 'c':  case 'C':     return 1;
            case 'a':  case 'A':    return 2;
            case 'g':case 'G':      return 3;
            default: return PROPERTY_NOT_DEFINED;
        }
        
    }
    
}
