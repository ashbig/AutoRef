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
    private ArrayList       m_rna_describes_aa = null;
    private Mutation        m_rna_without_aa = null;
    private LinkerMutation  m_linker = null;
    private int             m_discrepancydefinition_type = TYPE_AA;
    
    public static final int      TYPE_AA = 0;
    public static final int      TYPE_NOT_AA_LINKER = 1;
    public static final int      TYPE_NOT_AA_AMBIQUOUS = 2;
    
    /** Creates a new instance of DiscrepancyDefinition */
    /*
    public DiscrepancyDescription(RNAMutation rna, AAMutation a)
    {
        m_aa = a;
        m_rna_describes_aa = new ArrayList();
        m_rna_describes_aa.add( rna);
        m_discrepancydefinition_type = TYPE_AA;
    }
     public DiscrepancyDescription(ArrayList rna, AAMutation a)
    {
        m_aa = a;
        m_rna_describes_aa = rna;
        m_discrepancydefinition_type = TYPE_AA;
    }
    public DiscrepancyDescription(LinkerMutation linker)
    {
       m_linker = linker;
       m_discrepancydefinition_type = TYPE_NOT_AA_LINKER;
    }
    public DiscrepancyDescription(RNAMutation rna)
    {
       m_rna_without_aa = rna;
       m_discrepancydefinition_type = TYPE_NOT_AA_AMBIQUOUS;
    }
    */
    public DiscrepancyDescription(){}
    
    public void             setDiscrepancies(ArrayList discrepancies)
    {  
        ArrayList rna_discr = new ArrayList();
        for (int count = 0; count < discrepancies.size(); count++)
        {
            if (discrepancies.get(count) instanceof RNAMutation)
            {
                rna_discr.add( (RNAMutation)discrepancies.get(count));
            }
            else if (discrepancies.get(count) instanceof AAMutation)
            {
                 m_aa = (AAMutation)discrepancies.get(count);
                 m_rna_describes_aa = new ArrayList();
                 m_discrepancydefinition_type = TYPE_AA;
            }
            else if (discrepancies.get(count) instanceof LinkerMutation)
            {
                 m_linker = (LinkerMutation)discrepancies.get(count);
                 m_discrepancydefinition_type = TYPE_NOT_AA_LINKER;
                 
            }
        }
        if (m_discrepancydefinition_type == TYPE_AA)
        { 
            rna_discr = Mutation.sortDiscrepanciesByPosition(rna_discr);
            
            m_rna_describes_aa.addAll(rna_discr);
        }
        
    }
    public ArrayList        getRNACollection(){ return m_rna_describes_aa;}
    public Mutation         getAADefinition()    {       return m_aa;    }
    public int              getDiscrepancyDefintionType(){ return m_discrepancydefinition_type ;}
    public Mutation         getRNADefinition()
    {
        if ( m_discrepancydefinition_type == DiscrepancyDescription.TYPE_NOT_AA_AMBIQUOUS)
        {
            return m_rna_without_aa;
        }
        else if( m_discrepancydefinition_type == DiscrepancyDescription.TYPE_NOT_AA_LINKER)
        {
            return m_linker;
        }
        return null;
    }
    
    
    public static ArrayList   assembleDiscrepancyDefinitions(ArrayList discrepancies)
    {
        if (discrepancies == null || discrepancies.size() == 0) return null;
        
        ArrayList definitions = new ArrayList();
        ArrayList discr_in_definition = new ArrayList();
        DiscrepancyDescription new_definition = null;
        discrepancies = Mutation.sortDiscrepanciesByNumberAndType(discrepancies);
        int discrepancy_number  = ((Mutation)discrepancies.get(0)).getNumber();
        //we can count that rna discrepancy comes first in set for those that have aa && rna definition
        for(int count = 0; count < discrepancies.size(); count++)
        {
            //start new definition
            if ( discrepancy_number  != ((Mutation)discrepancies.get(count)).getNumber())
            {
                new_definition = new DiscrepancyDescription();
                new_definition.setDiscrepancies(discr_in_definition);
                definitions.add(new_definition);
                discr_in_definition = new ArrayList();
                discrepancy_number  = ((Mutation)discrepancies.get(count)).getNumber();
            }
            discr_in_definition.add(discrepancies.get(count));
  
        }
        if (discr_in_definition!= null && discr_in_definition.size() > 0)
        {  
            new_definition = new DiscrepancyDescription();
            new_definition.setDiscrepancies(discr_in_definition);
            definitions.add(new_definition);
        }
        return definitions;
    }
    
    //get in array of discrepancydefinitions
    //remember linker3 and gene specific mutations have their one position definition system
    public static ArrayList   getDiscrepancyDescriptionsNoDuplicates(ArrayList arr2,ArrayList arr1)
    {
        if (arr1 == null && arr2 == null) return null;
        if (arr1 != null & arr2 == null) return assembleDiscrepancyDefinitions(arr1);
        if (arr1 == null && arr2 != null) return assembleDiscrepancyDefinitions(arr2);
        ArrayList res1 = assembleDiscrepancyDefinitions(arr1);
        ArrayList res2 = assembleDiscrepancyDefinitions(arr2);
         DiscrepancyDescription discr_definition_res1 =  null; 
         DiscrepancyDescription discr_definition_res2 =  null; 
         ArrayList result = new ArrayList();
         int position = 0;
         int length = (res1.size() <= res2.size()? res1.size(): res2.size());
         int res1_pointer = 0; int res2_pointer = 0;
         int isEqualDiscrepancies = 0;
        while(true)
        {
            discr_definition_res1 =  (DiscrepancyDescription) res1.get(res1_pointer);
            discr_definition_res2 =  (DiscrepancyDescription) res2.get(res2_pointer);
            //equal discreapancies? 
            isEqualDiscrepancies = discr_definition_res1.isEqual(discr_definition_res2);
            if ( isEqualDiscrepancies < 0)//first discr located before second
            {
                result.add( discr_definition_res1  );
                res1_pointer++; 
                if ( res1_pointer >= res1.size() ) break;
            }
            else if ( isEqualDiscrepancies > 0)//second discr located before second
            {
                result.add( discr_definition_res2 );
                res2_pointer++;
                if ( res2_pointer >= res2.size() ) break;
            }
            else if ( isEqualDiscrepancies == 0)// discr located at the same place
            {
                  // select one that has higher quality
                result.add( getMaxQualityDiscrepancy(discr_definition_res1,discr_definition_res2) );
                res1_pointer++; res2_pointer++;
                if ( res1_pointer >= res1.size() || res2_pointer >= res2.size() ) break;
            }
        }
         //put the rest
         for (int count = res1_pointer; count < res1.size(); count++)
         {
             result.add(res1.get(count) );
         }
         for (int count = res2_pointer; count < res2.size(); count++)
         {
             result.add( res2.get(count) );
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
        DiscrepancyDescription discr_definition = null;
        int[] total_discrepancy_numbers_pass_high_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        int[] total_discrepancy_numbers_pass_low_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        for (int count = 0; count < discrepancy_descriptions.size(); count++)
        {
            global_change_type = Mutation.TYPE_NOT_DEFINE;
            discr_definition = (DiscrepancyDescription)discrepancy_descriptions.get(count);
            
            if ( discr_definition.getAADefinition() != null)
            {
                quality = discr_definition.getAADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getAADefinition().getChangeType());
            }
            else if(discr_definition.getAADefinition() == null && discr_definition.getRNADefinition() != null)
            {
                quality = discr_definition.getRNADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getRNADefinition().getChangeType());
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
        DiscrepancyDescription discr_definition = null;
        int[] total_discrepancy_numbers_pass_high_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        int[] total_discrepancy_numbers_pass_low_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        for (int count = 0; count < discrepancy_descriptions.size(); count++)
        {
            global_change_type = Mutation.TYPE_NOT_DEFINE;
            discr_definition = (DiscrepancyDescription)discrepancy_descriptions.get(count);
            
            if ( discr_definition.getAADefinition() != null)
            {
                quality = discr_definition.getAADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getAADefinition().getChangeType());
            }
            else if(discr_definition.getAADefinition() == null &&   discr_definition.getRNADefinition() != null)
            {
                quality = discr_definition.getRNADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getRNADefinition().getChangeType());
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
         DiscrepancyDescription discr_definition = null; int score = 0;
         int global_change_type = Mutation.TYPE_NOT_DEFINE;
        for (int count = 0; count< discrepancy_descriptions.size(); count++)
        {
            penalty = EndReadsSpec.PENALTY_NOT_DEFINED;
            discr_definition = (DiscrepancyDescription)discrepancy_descriptions.get(count);
            if ( discr_definition.getAADefinition() != null)
            {
                global_change_type = Mutation.getMacroChangeType(discr_definition.getAADefinition().getChangeType());
               if ( global_change_type != Mutation.TYPE_NOT_DEFINE)
                {
                    penalty = spec.getPenalty(discr_definition.getAADefinition().getQuality(),    global_change_type);// by aa mutation
                }
                /*
                else
                {
                    global_change_type = Mutation.getMacroChangeType(pair.getRNADefinition().getChangeType());
                    penalty = spec.getPenalty(pair.getRNADefinition().getQuality(),global_change_type);// by rna mutation
                }
               **/
            }
            else if ( discr_definition.getAADefinition() == null && discr_definition.getRNADefinition() != null)
            {
                global_change_type = Mutation.getMacroChangeType(discr_definition.getRNADefinition().getChangeType());
                penalty = spec.getPenalty(discr_definition.getRNADefinition().getQuality(),global_change_type);// by rna mutation
            }
            total_penalty += penalty ;
         }
       
        return -total_penalty;
    }
       
    public int getPosition()
    {
        if ( m_discrepancydefinition_type == DiscrepancyDescription.TYPE_AA)
             return this.getAADefinition().getPosition();
        else
            return this.getRNADefinition().getPosition();
            
    }
    public int isInSameRegion(  DiscrepancyDescription object)
    {
        int this_type = this.getDiscrepancyDefintionType();
        int object_type = object.getDiscrepancyDefintionType();
        int this_linker_type = -1;int object_linker_type = -1;
        if ( this_type == TYPE_NOT_AA_LINKER )
            this_linker_type = ( (LinkerMutation)this.getRNADefinition()).getType() ;
        if ( object_type == TYPE_NOT_AA_LINKER )
             object_linker_type = ( (LinkerMutation)object.getRNADefinition()).getType() ;
        
        if ( (this_type == TYPE_AA && object_type == TYPE_AA)
            || ( ( this_type == TYPE_NOT_AA_LINKER &&  object_type == TYPE_NOT_AA_LINKER ) && ( object_linker_type ==  this_linker_type ) ))
            return 0;
        if ( ( this_type == TYPE_AA && object_type == TYPE_NOT_AA_LINKER && object_type == Mutation.LINKER_3P)
                || ( object_type == TYPE_AA && this_type == TYPE_NOT_AA_LINKER && this_type == Mutation.LINKER_5P))
            return -1;
        if (( this_type == TYPE_AA && object_type == TYPE_NOT_AA_LINKER && object_type == Mutation.LINKER_5P)
            || ( object_type == TYPE_AA && this_type == TYPE_NOT_AA_LINKER && this_type == Mutation.LINKER_3P))
            return 1;
        else
            return 0;
        
    }
    public int isEqual(DiscrepancyDescription object)
    {
          int isInSameRegion = this.isInSameRegion(object);
          int this_position = this.getPosition() ;
          int object_position = object.getPosition();
          
          if ( isInSameRegion == 0) //same type of discrepancy -> compare position
          {
              return this_position - object_position;
          }
          else 
              return isInSameRegion;
            
          
    }
     //---------------------------------------------------------------------
    private static DiscrepancyDescription getMaxQualityDiscrepancy(DiscrepancyDescription description_one, DiscrepancyDescription description_two)
      {
          int quality_one = Mutation.QUALITY_NOTKNOWN;
          int quality_two = Mutation.QUALITY_NOTKNOWN;
          if ( description_one.getAADefinition() != null )
          {
              quality_one = description_one.getAADefinition().getQuality() ;
              quality_two=          description_two.getAADefinition().getQuality();
          }
          else
          {
               quality_one=  description_one.getRNADefinition().getQuality() ; 
                quality_two   =     description_two.getRNADefinition().getQuality();
          }
          
          if ( quality_one >= quality_two)
              return  description_one;
          else
                  return description_two;
           
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
       
              CloneSequence cl= new CloneSequence(1695);
              System.out.println(Mutation.toHTMLString(cl.getDiscrepancies()));
              ArrayList discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( cl.getDiscrepancies());
              /// edu.harvard.med.hip.bec.coreobjects.spec.FullSeqSpec spec = (FullSeqSpec ) Spec.getSpecById(11);
             
             //  int c = defineQuality( discrepancy_descriptions ,spec);
              System.out.print("L");   
                CloneSequence cl1= new CloneSequence(13839);
                discrepancy_descriptions = DiscrepancyDescription.assembleDiscrepancyDefinitions( cl.getDiscrepancies());
            System.out.println(Mutation.toHTMLString(cl1.getDiscrepancies()));
                //    edu.harvard.med.hip.bec.coreobjects.spec.FullSeqSpec spec = (FullSeqSpec ) Spec.getSpecById(11);
                  discrepancy_descriptions = DiscrepancyDescription.getDiscrepancyDescriptionsNoDuplicates(cl.getDiscrepancies(),cl1.getDiscrepancies());
             //   c = defineQuality( discrepancy_descriptions ,spec);
                System.out.print("L");   
                

            }
            catch(Exception e)
            {}
        }
     }

