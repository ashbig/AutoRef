/*
 * DiscrepancyDefinition.java
 *
 * Created on May 14, 2003, 3:56 PM
 */

package edu.harvard.med.hip.bec.coreobjects.feature;

import java.util.*;
/**
 *
 * @author  htaycher
 */
public class DiscrepancyPair
{
    private AAMutation      m_aa = null;
    private RNAMutation     m_rna = null;
    /** Creates a new instance of DiscrepancyDefinition */
    public DiscrepancyPair(RNAMutation rna,AAMutation a)
    {
        m_aa = a;
        m_rna = rna;
    }
    public DiscrepancyPair(){}
    
    
    public AAMutation   getAADiscrepancy(){ return m_aa;}
    public RNAMutation  getRNADiscrepancy(){ return m_rna;}
    public void         setAADiscrepancy(AAMutation a){  m_aa = a;}
    public void         setRNADiscrepancy(RNAMutation a){  m_rna = a;}
    
    public static ArrayList   assembleDiscrepanciesInPairs(ArrayList discrepancies)
    {
        Mutation discrepancy = null;
        if (discrepancies == null) return null;
        int discrepancy_number  = -1;
        ArrayList pairs = new ArrayList();DiscrepancyPair new_pair = null;
        discrepancies =Mutation.sortDiscrepanciesByNumberAndType(discrepancies);
        //we can count that rna discrepancy comes first in pair
        for(int i = 0; i < discrepancies.size(); i++)
        {
            discrepancy = (Mutation)discrepancies.get(i);
            //start new pair
            if ( discrepancy_number  != discrepancy.getNumber())
            {
                new_pair = new DiscrepancyPair();
                pairs.add(new_pair);
                discrepancy_number  = discrepancy.getNumber();
            }
            if (discrepancies.get(i) instanceof RNAMutation)
            {
                new_pair.setRNADiscrepancy( (RNAMutation)discrepancies.get(i));
            }
            else if (discrepancies.get(i) instanceof AAMutation)
            {
                 new_pair.setAADiscrepancy( (AAMutation)discrepancies.get(i));
            }
           
        }
        return pairs;
    }
    
    
    public static ArrayList   getDiscrepancyPairsNoDuplicates(ArrayList arr2,ArrayList arr1)
    {
        if (arr1 == null && arr2 == null) return null;
        if (arr1 != null & arr2 == null) return assembleDiscrepanciesInPairs(arr1);
        if (arr1 == null && arr2 != null) return assembleDiscrepanciesInPairs(arr2);
        ArrayList res1 = assembleDiscrepanciesInPairs(arr1);
        ArrayList res2 = assembleDiscrepanciesInPairs(arr2);
        Hashtable discrepancy_positions = new Hashtable();
         DiscrepancyPair pair =  null; 
         
        for (int count = 0; count < res1.size(); count++)
        {
            pair =  (DiscrepancyPair) res1.get(count);
            discrepancy_positions.put(new Integer( pair.getRNADiscrepancy().getPosition()), pair);
        }
        
         for (int count = 0; count < res2.size(); count++)
         {
             pair =  (DiscrepancyPair) res2.get(count);
             if ( discrepancy_positions.containsKey(new Integer( pair.getRNADiscrepancy().getPosition())))
             {
                 if ( isMaxDiscrepancy (pair, (DiscrepancyPair)discrepancy_positions.get( new Integer( pair.getRNADiscrepancy().getPosition() ) )))
                 {
                    res1.add(pair);
                 }
             }
             else
                 res1.add(pair);
         }
        return res1;
        
    }
    
    public static ArrayList   getDiscrepancyNoDuplicates(ArrayList arr1)
    {
        if (arr1 == null || arr1.size() == 0) return null;
        ArrayList res = new ArrayList();
        
        Hashtable discrepancy_positions = new Hashtable();
         Mutation mut =  null; 
         
        for (int count = 0; count < arr1.size(); count++)
        {
            mut =  (Mutation) arr1.get(count);
            if ( discrepancy_positions.containsKey(new Integer( mut.getPosition())))
             {
                 if ( isMaxDiscrepancy (mut, (Mutation)discrepancy_positions.get( new Integer( mut.getPosition() ) )))
                 {
                    res.add(mut);
                 }
             }
             else
                 res.add(mut);
         }
        return res;
        
    }
    
   
    
   
    
      public static ArrayList sortByRNADiscrepancyByChangeTypeQuality(ArrayList discrepancies)
    {
        ArrayList result = new ArrayList();
        for (int i=0; i< discrepancies.size();i++)
            
            //sort array by cds length
            Collections.sort(discrepancies, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    RNAMutation discrepancy_1 =  ((DiscrepancyPair) o1).getRNADiscrepancy();
                    RNAMutation discrepancy_2 = ((DiscrepancyPair) o2).getRNADiscrepancy();
                    int res = discrepancy_1.getChangeType() - discrepancy_2.getChangeType();
                    if (res == 0)//same type
                    {
                        return discrepancy_1.getQuality() - discrepancy_2.getQuality();
                    }
                    else
                        return res;
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            
            return discrepancies;
    }
      
      
      private static boolean isMaxDiscrepancy(DiscrepancyPair pair_one, DiscrepancyPair pair_two)
      {
          if ( pair_one.getRNADiscrepancy().getQuality() > pair_two.getRNADiscrepancy().getQuality())
          {
              return true;
          }
          
          return false;
      }
       private static boolean isMaxDiscrepancy(Mutation mut_one, Mutation mut_two)
      {
          if ( mut_one.getQuality() > mut_two.getQuality())
          {
              return true;
          }
          
          return false;
      }

}

