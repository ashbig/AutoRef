//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * Alphabet.java
 *
 * Created on April 17, 2003, 3:57 PM
 */

package edu.harvard.med.hip.bec.util;

import java.util.*;


/**
 *
 * @author  htaycher
 */
public class Alphabet
{
    
   public static boolean isIntegerArrayString(ArrayList arr, int min, int max)
    throws BecUtilException
   {
       String val = null; int ival = 0;
       for (int i = 0; i < arr.size(); i++)
       {
           try
           {
                val = (String) arr.get(i);
           }
           catch(Exception e){System.out.println("Array not string array."); return false;}
           try
           {
               ival = Integer.parseInt(val);
           }
           catch(Exception e){System.out.println("Array member not integer " + i);return false;}
           if ( ! (ival >=min && ival < max) )   
           { 
               System.out.println("Array member value does not fit the min - max range");
             return false;
           }
       }
       return true;
   }
   public static boolean isInteger(ArrayList arr)
   {
       String val = null; int ival = 0;
       for (int i = 0; i < arr.size(); i++)
       {
           try
           {
                val = (String) arr.get(i);
           }
           catch(Exception e){System.out.println("Array not string array."); return false;}
           try
           {
               ival = Integer.parseInt(val);
           }
           catch(Exception e){System.out.println("Array member not integer " + i);return false;}
        
       }
       return true;
   }
    
   
   
   
   //****************************************************************
    public static final int DNA_SEQUENCE_IS_GAP_OK = 1;
    public static final int DNA_SEQUENCE_IS_GAP_NOTOK = 2;
    
    public static boolean isDNASequence(String seq, int mode)
    {
        char[] seq_char = seq.toCharArray();
        int answer = -1;
        for (int i = 0; i < seq_char.length; i++)
        {
            answer = getIndexOfBase( seq_char [i] );
            if ( mode == DNA_SEQUENCE_IS_GAP_NOTOK && !( answer >0 &&  answer < 6)) 
            {
                return false;
            }
            else if (mode == DNA_SEQUENCE_IS_GAP_OK && ( answer >0 && answer < 7) ) 
            {
                return false;
            }
            
        }
        return true;
    }
    
   private  static int getIndexOfBase( char base)
    {
        switch (base)
        {
            case 't':    case 'u':  case 'T':    case 'U':    return 1;
            case 'c':  case 'C':     return 2;
            case 'a':  case 'A':    return 3;
            case 'g':case 'G':      return 4;
            case 'n': case'N': return 5;
            case '-': return 6; //gap
        }
        return 10;
    }
}
