/*
 * DiscrepancyDefinition.java
 *
 * Created on May 14, 2003, 3:56 PM
 */

package edu.harvard.med.hip.bec.coreobjects.feature;



import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

import java.util.*;
/**
 *
 * @author  htaycher
 */
public class DiscrepancyDescription
{
    private AAMutation      m_aa = null;
    private RNAMutation     m_rna = null;
    private LinkerMutation  m_linker = null;
    
    private int             m_discrepancydefinition_type = TYPE_RNA;
    
    public static final int      TYPE_RNA = 0;
    public static final int      TYPE_LINKER = 1;
    
    /** Creates a new instance of DiscrepancyDefinition */
    public DiscrepancyDescription(RNAMutation rna, AAMutation a)
    {
        m_aa = a;
        m_rna = rna;
        m_discrepancydefinition_type = TYPE_RNA;
    }
    public DiscrepancyDescription(LinkerMutation linker)
    {
       m_linker = linker;
       m_discrepancydefinition_type = TYPE_LINKER;
    }
    
    public DiscrepancyDescription(){}
    
    
    public AAMutation       getAADiscrepancy(){ return m_aa;}
    public RNAMutation      getRNADiscrepancy(){ return m_rna;}
    public LinkerMutation   getLinkerDiscrepancy(){ return m_linker;}
    public int              getDiscrepancyDefintionType(){ return m_discrepancydefinition_type ;}
    public Mutation         getRNADefinition()
    {
        if ( m_discrepancydefinition_type == DiscrepancyDescription.TYPE_RNA)
        {
            return m_rna;
        }
        else if( m_discrepancydefinition_type == DiscrepancyDescription.TYPE_LINKER)
        {
            return m_linker;
        }
        return null;
    }
    public Mutation         getAADefinition()
    {
       return m_aa;
    }
    
    public void             setAADiscrepancy(AAMutation a){  m_aa = a;}
    public void             setRNADiscrepancy(RNAMutation a){  m_rna = a;m_discrepancydefinition_type = TYPE_RNA;}
    public void             setLinkerDiscrepancy(LinkerMutation a){  m_linker = a;m_discrepancydefinition_type = TYPE_LINKER;}
    
    public static ArrayList   assembleDiscrepanciesInPairs(ArrayList discrepancies)
    {
        Mutation discrepancy = null;
        if (discrepancies == null) return null;
        int discrepancy_number  = -1;
        ArrayList pairs = new ArrayList();DiscrepancyDescription new_pair = null;
        discrepancies =Mutation.sortDiscrepanciesByNumberAndType(discrepancies);
        //we can count that rna discrepancy comes first in pair
        for(int i = 0; i < discrepancies.size(); i++)
        {
            discrepancy = (Mutation)discrepancies.get(i);
            //start new pair
            if ( discrepancy_number  != discrepancy.getNumber())
            {
                new_pair = new DiscrepancyDescription();
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
            else if (discrepancies.get(i) instanceof LinkerMutation)
            {
                 new_pair.setLinkerDiscrepancy( (LinkerMutation)discrepancies.get(i));
            }
           
        }
        return pairs;
    }
    
    //get in array of discrepancydefinitions
    //remember linker3 and gene specific mutations have their one position definition system
    public static ArrayList   getDiscrepancyPairsNoDuplicates(ArrayList arr2,ArrayList arr1)
    {
        if (arr1 == null && arr2 == null) return null;
        if (arr1 != null & arr2 == null) return assembleDiscrepanciesInPairs(arr1);
        if (arr1 == null && arr2 != null) return assembleDiscrepanciesInPairs(arr2);
        ArrayList res1 = assembleDiscrepanciesInPairs(arr1);
        ArrayList res2 = assembleDiscrepanciesInPairs(arr2);
        Hashtable discrepancy_positions = new Hashtable();
        Hashtable discrepancy_positions_linker3 = new Hashtable();
         DiscrepancyDescription pair =  null; 
         ArrayList result = new ArrayList();
         int position = 0;
        for (int count = 0; count < res1.size(); count++)
        {
            pair =  (DiscrepancyDescription) res1.get(count);
            position = pair.getRNADefinition().getPosition();
            if (pair.getRNADefinition().getType() != Mutation.LINKER_3P)
            {
                discrepancy_positions.put( new Integer(position) , pair);
            }
            else
            {
                discrepancy_positions_linker3.put( new Integer(position) , pair);
            }
        }
        
         for (int count = 0; count < res2.size(); count++)
         {
             pair =  (DiscrepancyDescription) res2.get(count);
             position = pair.getRNADefinition().getPosition();
             
             if (pair.getRNADefinition().getType() != Mutation.LINKER_3P
                && discrepancy_positions.containsKey(new Integer( position)))
             {
                 if ( isMaxDiscrepancy (pair, (DiscrepancyDescription)discrepancy_positions.get( new Integer(position) )))
                 {
                     discrepancy_positions.put(new Integer( position), pair);
                 }
                else
                     discrepancy_positions.put(new Integer( position), pair);
             }
             else if(pair.getRNADefinition().getType() == Mutation.LINKER_3P
                && discrepancy_positions_linker3.containsKey(new Integer( position)))
             {
                 if ( isMaxDiscrepancy (pair, (DiscrepancyDescription)discrepancy_positions.get( new Integer(position) )))
                 {
                     discrepancy_positions_linker3.put(new Integer( position), pair);
                 }
                else
                     discrepancy_positions_linker3.put(new Integer( position), pair);
             }
         }
         for (Enumeration e = discrepancy_positions.elements() ; e.hasMoreElements() ;) 
         {
             result.add(e.nextElement());
         }
         for (Enumeration e = discrepancy_positions_linker3.elements() ; e.hasMoreElements() ;) 
         {
             result.add(e.nextElement());
         }

        return  result;
        
    }
    //take in array of mutations
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
                    discrepancy_positions.put(new Integer( mut.getPosition()),mut);
                 }
             }
             else
             {
                 discrepancy_positions.put(new Integer( mut.getPosition()),mut);
             }
         }
         for (Enumeration e = discrepancy_positions.elements() ; e.hasMoreElements() ;) 
         {
             res.add(e.nextElement());
         }
        return res;
        
    }
    
   
    /*
   
    
      public static ArrayList sortByRNADefinitionByChangeTypeQuality(ArrayList discrepancies)
    {
        ArrayList result = new ArrayList();
        for (int i=0; i< discrepancies.size();i++)
            
            //sort array by cds length
            Collections.sort(discrepancies, new Comparator()
            {
                public int compare(Object o1, Object o2)
                {
                    Mutation discrepancy_1 =  ((DiscrepancyDescription) o1).getRNADefinition();
                    Mutation discrepancy_2 = ((DiscrepancyDescription) o2).getRNADefinition();
                    int res = discrepancy_1.getChangeType() - discrepancy_2.getChangeType();
                    if (res == 0)//same type
                    {
                        return discrepancy_1.getQuality() - discrepancy_2.getQuality();
                    }
                    else
                        return res;
                }
                // Note: this comparator imposes orderings that are inconsistent with equals. 
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
            
            
            return discrepancies;
    }
      */
    public static boolean isMaxNumberOfDiscrepanciesReached(ArrayList discrepancy_descriptions ,
                                                            FullSeqSpec cutoff_spec )
                                                            throws BecDatabaseException
    {
        boolean result = false;
        if ( discrepancy_descriptions == null || discrepancy_descriptions.size() ==0)
            return false;
        int global_change_type = Mutation.TYPE_NOT_DEFINE;
 //get total number of discrepancies per each global type
        int quality = Mutation.QUALITY_NOTKNOWN;
        DiscrepancyDescription pair = null;
        int[] total_discrepancy_numbers_pass_high_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        int[] total_discrepancy_numbers_pass_low_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        for (int pair_count = 0; pair_count < discrepancy_descriptions.size(); pair_count++)
        {
            global_change_type = Mutation.TYPE_NOT_DEFINE;
            pair = (DiscrepancyDescription)discrepancy_descriptions.get(pair_count);
            quality = pair.getRNADefinition().getQuality();
            if ( pair.getAADefinition() != null)
            {
                global_change_type = Mutation.getMacroChangeType(pair.getAADefinition().getChangeType());
            }
            if (pair.getAADefinition() == null ||  global_change_type == Mutation.TYPE_NOT_DEFINE)
            {
                global_change_type = Mutation.getMacroChangeType(pair.getRNADefinition().getChangeType());
            }
            if (global_change_type != Mutation.TYPE_NOT_DEFINE)
            {
                if ( quality == Mutation.QUALITY_HIGH || quality == Mutation.QUALITY_NOTKNOWN)
                {
                    total_discrepancy_numbers_pass_high_quality[-global_change_type]++;
                }
                else if (quality == Mutation.QUALITY_LOW)
                {
                    total_discrepancy_numbers_pass_low_quality[-global_change_type ]++;
                }
            }
            
        }
        //check if limit reached
        for (int count = 1; count <= Mutation.MACRO_SPECTYPES_COUNT; count++)
        {
            if (total_discrepancy_numbers_pass_high_quality[count] > cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_HIGH,-count , FullSeqSpec.MODE_PASS) )
            {
                return true;
            }
            if (total_discrepancy_numbers_pass_low_quality[count] > cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_LOW,-count , FullSeqSpec.MODE_PASS) )
            {
                return true;
            }
        }
             
         
        return result;
    }
    
    
      public static int defineQuality(ArrayList discrepancy_descriptions ,
                                                            FullSeqSpec cutoff_spec )
                                                            throws BecDatabaseException
    {
       
        if ( discrepancy_descriptions == null || discrepancy_descriptions.size() ==0)
            return BaseSequence.QUALITY_GOOD;
        int global_change_type = Mutation.TYPE_NOT_DEFINE;
 //get total number of discrepancies per each global type
        int quality = Mutation.QUALITY_NOTKNOWN;
        DiscrepancyDescription pair = null;
        int[] total_discrepancy_numbers_pass_high_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        int[] total_discrepancy_numbers_pass_low_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        for (int pair_count = 0; pair_count < discrepancy_descriptions.size(); pair_count++)
        {
            global_change_type = Mutation.TYPE_NOT_DEFINE;
            pair = (DiscrepancyDescription)discrepancy_descriptions.get(pair_count);
            quality = pair.getRNADefinition().getQuality();
            if ( pair.getAADefinition() != null)
            {
                global_change_type = Mutation.getMacroChangeType(pair.getAADefinition().getChangeType());
            }
            if (pair.getAADefinition() == null ||  global_change_type == Mutation.TYPE_NOT_DEFINE)
            {
                global_change_type = Mutation.getMacroChangeType(pair.getRNADefinition().getChangeType());
            }
            if (global_change_type != Mutation.TYPE_NOT_DEFINE)
            {
                if ( quality == Mutation.QUALITY_HIGH || quality == Mutation.QUALITY_NOTKNOWN)
                {
                    total_discrepancy_numbers_pass_high_quality[-global_change_type]++;
                }
                else if (quality == Mutation.QUALITY_LOW)
                {
                    total_discrepancy_numbers_pass_low_quality[-global_change_type ]++;
                }
            }
            
        }
        //check if limit reached
        int disc_low_pass = 0;
        int disc_high_pass =0;
        int disc_low_fail = 0;
        int disc_high_fail =0;
        for (int count = 1; count <= Mutation.MACRO_SPECTYPES_COUNT; count++)
        {
            disc_low_pass =  cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_LOW,-count , FullSeqSpec.MODE_PASS);
            disc_high_pass = cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_HIGH,-count , FullSeqSpec.MODE_PASS);
            disc_low_fail =  cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_LOW,-count , FullSeqSpec.MODE_FAIL);
            disc_high_fail = cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_HIGH,-count , FullSeqSpec.MODE_FAIL);

    
            if ( (total_discrepancy_numbers_pass_high_quality[count] !=  0 && 
                    total_discrepancy_numbers_pass_high_quality[count] >  disc_high_fail) 
            ||
            ( total_discrepancy_numbers_pass_low_quality[count] != 0 && 
                    total_discrepancy_numbers_pass_low_quality[count] > disc_low_fail))
            {
                return BaseSequence.QUALITY_BAD;
            }
            if ( (total_discrepancy_numbers_pass_high_quality[count] != 0
            && total_discrepancy_numbers_pass_high_quality[count] >  disc_high_pass 
            && total_discrepancy_numbers_pass_high_quality[count] <= disc_high_fail)
            || 
            (  total_discrepancy_numbers_pass_low_quality[count] != 0
            && total_discrepancy_numbers_pass_low_quality[count] >  disc_low_pass 
            && total_discrepancy_numbers_pass_low_quality[count] <= disc_low_fail))
            {
                return BaseSequence.QUALITY_REVIEW;
            }
        }
             
         
        return BaseSequence.QUALITY_GOOD;
    }
      
      
    public static  int getPenalty(   ArrayList discrepancy_descriptions,
                                EndReadsSpec spec)
                                throws BecDatabaseException
    {
        if ( discrepancy_descriptions == null || discrepancy_descriptions.size() == 0)
            return 0;
         int discrepancy_number = 0; int total_penalty = 0 ;
         int penalty = EndReadsSpec.PENALTY_NOT_DEFINED;
         DiscrepancyDescription pair = null; int score = 0;
         int global_change_type = Mutation.TYPE_NOT_DEFINE;
        for (int pair_count = 0; pair_count< discrepancy_descriptions.size(); pair_count++)
        {
            penalty = EndReadsSpec.PENALTY_NOT_DEFINED;
            pair = (DiscrepancyDescription)discrepancy_descriptions.get(pair_count);
            if ( pair.getAADefinition() != null)
            {
                global_change_type = Mutation.getMacroChangeType(pair.getAADefinition().getChangeType());
                if ( global_change_type != Mutation.TYPE_NOT_DEFINE)
                {
                    penalty = spec.getPenalty(pair.getAADefinition().getQuality(),    global_change_type);// by aa mutation
                }
                else
                {
                    global_change_type = Mutation.getMacroChangeType(pair.getRNADefinition().getChangeType());
                    penalty = spec.getPenalty(pair.getRNADefinition().getQuality(),global_change_type);// by rna mutation
                }
            }
            else
            {
                global_change_type = Mutation.getMacroChangeType(pair.getRNADefinition().getChangeType());
                penalty = spec.getPenalty(pair.getRNADefinition().getQuality(),global_change_type);// by rna mutation
            }
            total_penalty += penalty ;
         }
       
        return -total_penalty;
    }
       
      //---------------------------------------------------------------------
      private static boolean isMaxDiscrepancy(DiscrepancyDescription pair_one, DiscrepancyDescription pair_two)
      {
          return  pair_one.getRNADefinition().getQuality() > pair_two.getRNADefinition().getQuality();
           
      }
       private static boolean isMaxDiscrepancy(Mutation mut_one, Mutation mut_two)
      {
          if ( mut_one.getQuality() > mut_two.getQuality())
          {
              return true;
          }
          
          return false;
      }

       
        public static void main(String args[])
        {
            try
            {
       
              CloneSequence cl= new CloneSequence(13858);
               ArrayList discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepanciesInPairs( cl.getDiscrepancies());
               edu.harvard.med.hip.bec.coreobjects.spec.FullSeqSpec spec = (FullSeqSpec ) Spec.getSpecById(11);
               int c = defineQuality( discrepancy_descriptions ,spec);
              System.out.print(c);   
                 cl= new CloneSequence(13839);
                discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepanciesInPairs( cl.getDiscrepancies());
           //    edu.harvard.med.hip.bec.coreobjects.spec.FullSeqSpec spec = (FullSeqSpec ) Spec.getSpecById(11);
                c = defineQuality( discrepancy_descriptions ,spec);
               
                 System.out.print(c);
                 cl= new CloneSequence(13872);
                discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepanciesInPairs( cl.getDiscrepancies());
          //     edu.harvard.med.hip.bec.coreobjects.spec.FullSeqSpec spec = (FullSeqSpec ) Spec.getSpecById(11);
                c = defineQuality( discrepancy_descriptions ,spec);
                        //discrepancies.addAll( read.getSequence().getDiscrepancies() );
       
                    System.out.print(c);

            }
            catch(Exception e)
            {}
        }
     }

