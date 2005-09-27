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
    private int             m_discrepancydefinition_type = TYPE_NOT_DEFINED;
    
    public static final int      TYPE_AA = 0;
    public static final int      TYPE_NOT_AA_LINKER = 1;
    public static final int      TYPE_NOT_AA_AMBIQUOUS = 2;
     public static final int      TYPE_NOT_AA_REFSEQUENCE_AMB = 3;
   
    public static final int      TYPE_NOT_DEFINED = -1;
    
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
         if (  discrepancies.size()==1 && m_discrepancydefinition_type == TYPE_NOT_DEFINED
                && ((RNAMutation)rna_discr.get(0)).getCodonMut().indexOf("N") != -1)
                        m_discrepancydefinition_type = TYPE_NOT_AA_AMBIQUOUS;
        if (discrepancies.size()==1 && m_discrepancydefinition_type == TYPE_NOT_DEFINED
                && ((RNAMutation)rna_discr.get(0)).getChangeType() == Mutation.TYPE_RNA_REFSEQUENCE_AMB)
                        m_discrepancydefinition_type = TYPE_NOT_AA_REFSEQUENCE_AMB;
        if (m_discrepancydefinition_type == TYPE_AA)
        { 
            rna_discr = Mutation.sortDiscrepanciesByPosition(rna_discr);
            m_rna_describes_aa.addAll(rna_discr);
        }
        else if ( (m_discrepancydefinition_type == TYPE_NOT_AA_AMBIQUOUS ||
         m_discrepancydefinition_type == TYPE_NOT_AA_REFSEQUENCE_AMB) &&  rna_discr.size()==1)
        {
            m_rna_without_aa = (RNAMutation)rna_discr.get(0);
        }
    
        
    }
    
    public ArrayList        getAllDiscrepancies()
    {
       ArrayList all_discr = new ArrayList();
        switch (m_discrepancydefinition_type )
        {
            case TYPE_AA :
            { 
               all_discr.addAll(m_rna_describes_aa);
               all_discr.add(m_aa);
               break;
            }
            case TYPE_NOT_AA_LINKER:
            {
                all_discr.add(m_linker);
                break;
            }
            case TYPE_NOT_AA_AMBIQUOUS:
            case TYPE_NOT_AA_REFSEQUENCE_AMB:
            {
                all_discr.add(m_rna_without_aa);
                break;
            }
        }
        return   all_discr;
    }
    
    public void             addRNADiscrepancy(RNAMutation rna){ m_rna_describes_aa.add(rna);}
    public ArrayList        getRNACollection(){ return m_rna_describes_aa;}
    public Mutation         getAADefinition()    {       return m_aa;    }
    public int              getDiscrepancyDefintionType(){ return m_discrepancydefinition_type ;}
    public Mutation         getRNADefinition()
    {
        if ( m_discrepancydefinition_type == DiscrepancyDescription.TYPE_NOT_AA_AMBIQUOUS ||
            m_discrepancydefinition_type == DiscrepancyDescription.TYPE_NOT_AA_REFSEQUENCE_AMB)
        {
            return m_rna_without_aa;
        }
        else if( m_discrepancydefinition_type == DiscrepancyDescription.TYPE_NOT_AA_LINKER)
        {
            return m_linker;
        }
        return null;
    }
    
    
    
     public int        getQuality()
    {
        switch (m_discrepancydefinition_type )
        {
            case TYPE_AA :
            { 
               return  m_aa.getQuality(); 
            }
            case TYPE_NOT_AA_LINKER:
            {
                return  m_linker.getQuality();
            }
            case TYPE_NOT_AA_AMBIQUOUS:
            case TYPE_NOT_AA_REFSEQUENCE_AMB:
            {
                return m_rna_without_aa.getQuality();
            }
            default: return Mutation.QUALITY_NOTKNOWN;
        }
      
    }
    
     public boolean    isPolymorphism()
    {
        switch (m_discrepancydefinition_type )
        {
            case TYPE_AA :
            { 
                RNAMutation cur_rna_discr = null;
               for (int rna_count = 0; rna_count <  m_rna_describes_aa.size(); rna_count++)
               {
                   cur_rna_discr = (RNAMutation)m_rna_describes_aa.get(rna_count);
                   if ( cur_rna_discr.getPolymorphismFlag() != Mutation.FLAG_POLYM_YES )
                       return false;
               }
               return true;
            }
            case TYPE_NOT_AA_LINKER:
            case TYPE_NOT_AA_AMBIQUOUS:
            case TYPE_NOT_AA_REFSEQUENCE_AMB:
            {
               return false;
            }
            default: return false;
        }
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
        if ( ( arr1 == null || arr1.size() == 0 ) && ( arr2 == null || arr2.size() == 0)) return null;
        if ( (arr1 != null && arr1.size() > 0) && ( arr2 == null || arr2.size() == 0 )) 
            return assembleDiscrepancyDefinitions(arr1);
        if ( ( arr1 == null || arr1.size() == 0 ) && ( arr2 != null && arr2.size() > 0))
            return assembleDiscrepancyDefinitions(arr2);
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
        boolean   isIgnorIfPolymorphism = false;
        DiscrepancyDescription discr_definition = null;
        int[] total_discrepancy_numbers_pass_high_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        int[] total_discrepancy_numbers_pass_low_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        boolean isProcessAsMissense = ( cutoff_spec.getParameterByName("FS_C_PASS_H") == null ) ;
        
        for (int count = 0; count < discrepancy_descriptions.size(); count++)
        {
            global_change_type = Mutation.TYPE_NOT_DEFINE;
            discr_definition = (DiscrepancyDescription)discrepancy_descriptions.get(count);
            
            if ( discr_definition.getAADefinition() != null)
            {
                quality = discr_definition.getAADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getAADefinition().getChangeType(),isProcessAsMissense);
            //penalize for amb inside aa
                if ( discr_definition.getRNACollection() != null)
                {
                    for (int amb_count = 0; amb_count < discr_definition.getRNACollection().size(); amb_count++)
                    {
                        Mutation mut = (Mutation) discr_definition.getRNACollection().get(amb_count);
                        if (mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_CDS ||
                            mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_START_CODON 
                            || mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_STOP_CODON 
                            ||    mut.getChangeType() ==  Mutation.TYPE_N_FRAMESHIFT_INSERTION 
                            || mut.getChangeType() ==   Mutation.TYPE_N_INFRAME_INSERTION   )
                        {
                             int amb_quality = mut.getQuality();
                             int amb_global_change_type = Mutation.getMacroChangeType(mut.getChangeType(),isProcessAsMissense);
                              if (amb_global_change_type != Mutation.TYPE_NOT_DEFINE)
                                {
                                    if ( amb_quality == Mutation.QUALITY_LOW)
                                        total_discrepancy_numbers_pass_low_quality[-amb_global_change_type ]++;
                                    else
                                        total_discrepancy_numbers_pass_high_quality[-amb_global_change_type]++;
                                }
                        }
                    }
                }
            }
            else if(discr_definition.getAADefinition() == null && discr_definition.getRNADefinition() != null)
            {
                quality = discr_definition.getRNADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getRNADefinition().getChangeType(),isProcessAsMissense);
            }
            if (global_change_type != Mutation.TYPE_NOT_DEFINE)
            {
                isIgnorIfPolymorphism = cutoff_spec.isIgnorPolymorphismByType(global_change_type);
                if ( isIgnorIfPolymorphism && discr_definition.isPolymorphism() )// need to check if discrepancy is polymorphism and continue if yes
                {
                    continue;
                }
                if ( quality == Mutation.QUALITY_LOW)
                    total_discrepancy_numbers_pass_low_quality[-global_change_type ]++;
                else
                    total_discrepancy_numbers_pass_high_quality[-global_change_type]++;
            }
            
        }
        //check if limit reached
        int cut_off_hq =  0; int cut_off_lq = 0;
        for (int count = 1; count <= Mutation.MACRO_SPECTYPES_COUNT; count++)
        {
           if ( total_discrepancy_numbers_pass_high_quality[count] ==  0 && 
                    total_discrepancy_numbers_pass_low_quality[count] == 0 ) continue;
     
            cut_off_hq = cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_HIGH,-count , FullSeqSpec.MODE_PASS);
            cut_off_lq = cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_LOW,-count , FullSeqSpec.MODE_PASS);
            
            if ( cut_off_hq != FullSeqSpec.CUT_OFF_VALUE_NOT_FOUND && total_discrepancy_numbers_pass_high_quality[count] > cut_off_hq )
            {
                return true;
            }
            if ( cut_off_lq != FullSeqSpec.CUT_OFF_VALUE_NOT_FOUND && total_discrepancy_numbers_pass_low_quality[count] > cut_off_lq )
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
        boolean isIgnorIfPolymorphism = false;
 //get total number of discrepancies per each global type
        int quality = Mutation.QUALITY_NOTKNOWN;
        DiscrepancyDescription discr_definition = null;
        int[] total_discrepancy_numbers_pass_high_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
        int[] total_discrepancy_numbers_pass_low_quality = new int[Mutation.MACRO_SPECTYPES_COUNT + 1];
         boolean isProcessAsMissense = ( cutoff_spec.getParameterByName("FS_C_PASS_H") == null ) ;
       
        for (int count = 0; count < discrepancy_descriptions.size(); count++)
        {
            global_change_type = Mutation.TYPE_NOT_DEFINE;
            discr_definition = (DiscrepancyDescription)discrepancy_descriptions.get(count);
            
            if ( discr_definition.getAADefinition() != null)
            {
                quality = discr_definition.getAADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getAADefinition().getChangeType(),isProcessAsMissense);
            //penalize for amb inside aa
                if ( discr_definition.getRNACollection() != null)
                {
                    for (int amb_count = 0; amb_count < discr_definition.getRNACollection().size(); amb_count++)
                    {
                        Mutation mut = (Mutation) discr_definition.getRNACollection().get(amb_count);
                        if (mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_CDS ||
                            mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_START_CODON 
                            || mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_STOP_CODON 
                            ||    mut.getChangeType() ==  Mutation.TYPE_N_FRAMESHIFT_INSERTION 
                            || mut.getChangeType() ==   Mutation.TYPE_N_INFRAME_INSERTION   )
                        {
                             int amb_quality = mut.getQuality();
                             int amb_global_change_type = Mutation.getMacroChangeType(mut.getChangeType(),isProcessAsMissense);
                              if (amb_global_change_type != Mutation.TYPE_NOT_DEFINE)
                                {
                                    if ( amb_quality == Mutation.QUALITY_LOW)
                                        total_discrepancy_numbers_pass_low_quality[-amb_global_change_type ]++;
                                    else
                                        total_discrepancy_numbers_pass_high_quality[-amb_global_change_type]++;
                                }
                        }
                    }
                }
            }
            else if(discr_definition.getAADefinition() == null &&   discr_definition.getRNADefinition() != null)
            {
                quality = discr_definition.getRNADefinition().getQuality();
                global_change_type = Mutation.getMacroChangeType(discr_definition.getRNADefinition().getChangeType(),isProcessAsMissense);
            }
            if (global_change_type != Mutation.TYPE_NOT_DEFINE)
            {
                isIgnorIfPolymorphism = cutoff_spec.isIgnorPolymorphismByType(global_change_type);
                if ( isIgnorIfPolymorphism && discr_definition.isPolymorphism() )// need to check if discrepancy is polymorphism and continue if yes
                {
                    continue;
                }
                
                if ( quality == Mutation.QUALITY_LOW)
                    total_discrepancy_numbers_pass_low_quality[-global_change_type ]++;
                else
                    total_discrepancy_numbers_pass_high_quality[-global_change_type]++;
              
            }
            
        }
        //check if limit reached
        int disc_low_pass = 0;
        int disc_high_pass =0;
        int disc_low_fail = 0;
        int disc_high_fail =0;
        for (int count = 1; count <= Mutation.MACRO_SPECTYPES_COUNT; count++)
        {
            // no discrepancy in the type
            if ( total_discrepancy_numbers_pass_high_quality[count] ==  0 && 
                    total_discrepancy_numbers_pass_low_quality[count] == 0 ) continue;
            
            disc_low_pass =  cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_LOW,-count , FullSeqSpec.MODE_PASS);
            disc_high_pass = cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_HIGH,-count , FullSeqSpec.MODE_PASS);
            disc_low_fail =  cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_LOW,-count , FullSeqSpec.MODE_FAIL);
            disc_high_fail = cutoff_spec.getDiscrepancyNumberByType(Mutation.QUALITY_HIGH,-count , FullSeqSpec.MODE_FAIL);

    
            if ( (total_discrepancy_numbers_pass_high_quality[count] !=  0 && 
                    total_discrepancy_numbers_pass_high_quality[count] >=  disc_high_fail) 
            ||
            ( total_discrepancy_numbers_pass_low_quality[count] != 0 && 
                    total_discrepancy_numbers_pass_low_quality[count] >= disc_low_fail))
            {
                return BaseSequence.QUALITY_BAD;
            }
            if ( (total_discrepancy_numbers_pass_high_quality[count] != 0
            && total_discrepancy_numbers_pass_high_quality[count] >  disc_high_pass 
            && total_discrepancy_numbers_pass_high_quality[count] < disc_high_fail)
            || 
            (  total_discrepancy_numbers_pass_low_quality[count] != 0
            && total_discrepancy_numbers_pass_low_quality[count] >  disc_low_pass 
            && total_discrepancy_numbers_pass_low_quality[count] < disc_low_fail))
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
         boolean isProcessAsMissense = ( spec.getParameterByName("ER_C_H") == null);
        for (int count = 0; count< discrepancy_descriptions.size(); count++)
        {
            penalty = EndReadsSpec.PENALTY_NOT_DEFINED;
            discr_definition = (DiscrepancyDescription)discrepancy_descriptions.get(count);
            if ( discr_definition.getAADefinition() != null)
            {
                global_change_type = Mutation.getMacroChangeType(discr_definition.getAADefinition().getChangeType(),isProcessAsMissense);
                if ( global_change_type != Mutation.TYPE_NOT_DEFINE)
                {
                    penalty = spec.getPenalty(discr_definition.getAADefinition().getQuality(),    global_change_type);// by aa mutation
                }
                if ( discr_definition.getRNACollection() != null)
                {
                    for (int amb_count = 0; amb_count < discr_definition.getRNACollection().size(); amb_count++)
                    {
                        Mutation mut = (Mutation) discr_definition.getRNACollection().get(amb_count);
                        if (mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_CDS ||
                            mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_START_CODON 
                            || mut.getChangeType() ==  Mutation.TYPE_N_SUBSTITUTION_STOP_CODON 
                            ||    mut.getChangeType() ==  Mutation.TYPE_N_FRAMESHIFT_INSERTION 
                            || mut.getChangeType() ==   Mutation.TYPE_N_INFRAME_INSERTION   )
                        {
                           int amb_global_change_type = Mutation.getMacroChangeType(mut.getChangeType(),isProcessAsMissense);
                           if ( amb_global_change_type != Mutation.TYPE_NOT_DEFINE)
                            {
                                int amb_penalty = spec.getPenalty(mut.getQuality(),    amb_global_change_type);// by aa mutation
                                total_penalty += amb_penalty ;
                           }
                        }
                    }
                }
            }
            else if ( discr_definition.getAADefinition() == null && discr_definition.getRNADefinition() != null)
            {
                global_change_type = Mutation.getMacroChangeType(discr_definition.getRNADefinition().getChangeType(),isProcessAsMissense);
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
    
    
    // returns 
    // 0 - in the same region
    // -1   - this  is in the left region vs. object
    // this in the right region vs object
    public int isInSameRegion(  DiscrepancyDescription object)
    {
        // region schema    -1 (linker 5) |     0 (gene)    |  1 (linker 3)
        int this_region = 0; int object_region = 0;
        int this_type = this.getDiscrepancyDefintionType();
        int object_type = object.getDiscrepancyDefintionType();
        int this_linker_type = -1;int object_linker_type = -1;
        if ( this_type == TYPE_NOT_AA_LINKER )
            this_linker_type = ( (LinkerMutation)this.getRNADefinition()).getType() ;
        if ( object_type == TYPE_NOT_AA_LINKER )
             object_linker_type = ( (LinkerMutation)object.getRNADefinition()).getType() ;
        
       
        if ( this_type != TYPE_NOT_AA_LINKER) this_region = 0;
        if ( object_type != TYPE_NOT_AA_LINKER) this_region = 0;
        if (  this_type == TYPE_NOT_AA_LINKER && this_linker_type == Mutation.LINKER_3P) this_region = 1;
        if (  object_type == TYPE_NOT_AA_LINKER && object_linker_type == Mutation.LINKER_3P) this_region = 1;
        if (  this_type == TYPE_NOT_AA_LINKER && this_linker_type == Mutation.LINKER_5P) this_region = -1;
        if (  object_type == TYPE_NOT_AA_LINKER && object_linker_type == Mutation.LINKER_5P) this_region = -1;
        
        if ( (this_region - object_region ) == 0 ) return 0;
        else  return ((  this_region - object_region ) > 0 ) ? 1:-1;
      
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
    
    
     //function counts number of discrepancies of each type
    public static int[][] getDiscrepanciesSeparatedByType(ArrayList discr_definitions,
                                                          boolean isSeparateByQuality,
                                                          boolean isProcessAsMissense)
    {
        if (discr_definitions == null || discr_definitions.size() == 0) return null;
        
        Mutation discr = null;DiscrepancyDescription dd = null;
        int change_type = Mutation.TYPE_NOT_DEFINE;
        int quality = Mutation.QUALITY_NOTKNOWN;
        int res[][] = new int[Mutation.MACRO_SPECTYPES_COUNT][2] ;
        for (int count = 0; count < discr_definitions.size(); count++)
        {
            try
            {
            dd = (DiscrepancyDescription) discr_definitions.get(count);
            if( dd.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA )
            { 
               discr = (Mutation)dd.getAADefinition() ;
               change_type = Mutation.getMacroChangeType( discr.getChangeType() ,isProcessAsMissense);
               if ( change_type == Mutation.TYPE_NOT_DEFINE)
               {
                   change_type = Mutation.getMacroChangeType( ((Mutation)dd.getRNACollection().get(0)).getChangeType() ,isProcessAsMissense);
               }
            }
            else   if( dd.getDiscrepancyDefintionType() ==  DiscrepancyDescription.TYPE_NOT_AA_LINKER
                || dd.getDiscrepancyDefintionType() ==  DiscrepancyDescription.TYPE_NOT_AA_AMBIQUOUS )
            {
                discr = dd.getRNADefinition();
                change_type = Mutation.getMacroChangeType( discr.getChangeType(),isProcessAsMissense );
            }
            if (isSeparateByQuality)
            {
                if (discr.getQuality() == Mutation.QUALITY_HIGH || discr.getQuality() == Mutation.QUALITY_NOTKNOWN)
                {
                   quality = 0;
                }
                else
                {
                    quality = 1;
                }
            }
            else quality = 0;
            res[-change_type ][ quality]++;
            }
            catch(Exception tt)
            {
                System.out.println(count);
            }
        }
           
        return res;
    }
    
    
    //returns array where first member - number of high quality discrepancies,
    // second - number of high quality discrepancies
    
     public static int[]             getTotalNumberOfDiscrepanciesByQuality(ArrayList discrepancies)
    {
        int result[] = new int[2];
        if ( discrepancies == null ||  discrepancies.size() == 0 ) return result;
        DiscrepancyDescription dd = null;
        int high_dd_count = 0; int low_dd_count = 0; 
        for (int count = 0; count < discrepancies.size(); count++)
        {
            dd = (DiscrepancyDescription )discrepancies.get(count);
            if ( dd.getQuality() == Mutation.QUALITY_LOW ) low_dd_count++ ;
            else high_dd_count++;
        }
        result[0] = high_dd_count; result[1] = low_dd_count;
        return result;
    }
     public static String discrepancySummaryReport(ArrayList discr_definitions,
                                                        int discrepancy_region_type,
                                                        boolean isHighQuality,
                                                        boolean isHTML,
                                                        boolean isSeparateByQuality,
                                                       boolean isProcessAsMissense)
    {
         StringBuffer report = new StringBuffer();
        try
        {
            if (discr_definitions == null || discr_definitions.size()==0) return "";
           
            String quality_as_string = "";boolean isExists = false;
            //int res[][] = new int[Mutation.MACRO_SPECTYPES_COUNT][2] ;
            int[][] discrepancy_count  = DiscrepancyDescription.getDiscrepanciesSeparatedByType(
                                                                discr_definitions,
                                                                isSeparateByQuality,
                                                                isProcessAsMissense);
            
            int quality = ( isHighQuality )? 0: 1;
            //take into account that change type negative !!!!!!!!!!!!!!!
            for (int change_type=0; change_type< Mutation.MACRO_SPECTYPES_COUNT;change_type++)
            {
                if (discrepancy_count[change_type][quality] != 0)
                {
                    if ( !Mutation.isMacroMutationTypeInGeneRegion(discrepancy_region_type, -change_type) ) continue;
                    if (isHTML)
                    {
                        quality_as_string = (quality  == 0)? "Quality: High":"Quality: Low";
                        report.append("<tr><td>"+Mutation.getMacroMutationTypeAsString(-change_type)+"</td>");
                        report.append("<td>"+discrepancy_count[change_type][quality]+"</td>");
                        report.append("<td>"+quality_as_string+"</td></tr>");
                    }
                    else
                    {
                        quality_as_string = (quality  == 0)? "High":"Low";
                        report.append(Mutation.getMacroMutationTypeAsString(-change_type));
                        report.append("("+quality_as_string+"): ");
                        report.append(discrepancy_count[change_type][quality]+"|");

                    }
                }
            }
            return report.toString();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return report.toString();
    }
     
     public static String discrepancySummaryReport( int[][] discrepancy_count,
                                                        int discrepancy_region_type,
                                                        boolean isHighQuality,
                                                        boolean isHTML)
    {
         StringBuffer report = new StringBuffer();
        try
        {
            if (discrepancy_count == null) return "";
           
            String quality_as_string = "";boolean isExists = false;
            
            int quality = ( isHighQuality )? 0: 1;
            //take into account that change type negative !!!!!!!!!!!!!!!
            for (int change_type=0; change_type< Mutation.MACRO_SPECTYPES_COUNT;change_type++)
            {
                if (discrepancy_count[change_type][quality] != 0)
                {
                    if ( !Mutation.isMacroMutationTypeInGeneRegion(discrepancy_region_type, -change_type) ) continue;
                    if (isHTML)
                    {
                        quality_as_string = (quality  == 0)? "Quality: High":"Quality: Low";
                        report.append("<tr><td>"+Mutation.getMacroMutationTypeAsString(-change_type)+"</td>");
                        report.append("<td>"+discrepancy_count[change_type][quality]+"</td>");
                        report.append("<td>"+quality_as_string+"</td></tr>");
                    }
                    else
                    {
                        quality_as_string = (quality  == 0)? "High":"Low";
                        report.append(Mutation.getMacroMutationTypeAsString(-change_type));
                        report.append("("+quality_as_string+"): ");
                        report.append(discrepancy_count[change_type][quality]+"|");

                    }
                }
            }
            return report.toString();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return report.toString();
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

       
       
       public static Object  detailedDiscrepancyreport(ArrayList discr_definition_arr, Object type)
       {
           if ( discr_definition_arr == null || discr_definition_arr.size()==0) return null;
           StringBuffer result = new StringBuffer();
           DiscrepancyDescription dd = null;
           ArrayList discrepancy_reports_cds = new ArrayList();
           String discrepancy_discription = null;
           ArrayList discrepancy_reports_5_linker = new ArrayList();
           ArrayList discrepancy_reports_3_linker = new ArrayList();
           Mutation discr = null;
          
           for (int count = 0; count < discr_definition_arr.size(); count++)
           {
               dd = (DiscrepancyDescription)discr_definition_arr.get(count);
               discrepancy_discription = "";
                switch ( dd.getDiscrepancyDefintionType() )
                {
                    case TYPE_AA :
                    { 
                       for (int rna_count = 0; rna_count < dd.getRNACollection().size(); rna_count ++)
                       {
                           discrepancy_discription += Mutation.getDescription((RNAMutation)dd.getRNACollection().get(rna_count)) ;
                           if ( rna_count != dd.getRNACollection().size()-1 ) discrepancy_discription += ",";
                       }
                       discrepancy_discription = discrepancy_discription.toLowerCase();
                       if ( discrepancy_discription.indexOf("ins@") == -1 && discrepancy_discription.indexOf("del@") == -1)
                       {
                        discrepancy_discription += ","+ Mutation.getDescription( dd.getAADefinition()) ;
                       }
                       discrepancy_discription +=  ";";
                       discrepancy_reports_cds.add(discrepancy_discription);
                       break;
                    }
                    case TYPE_NOT_AA_LINKER:
                    {
                        if ( dd.getRNADefinition().getType() == Mutation.LINKER_5P)
                        {
                            discrepancy_reports_5_linker.add( Mutation.getDescription(dd.getRNADefinition()).toLowerCase() +";");
                        }
                        if ( dd.getRNADefinition().getType() == Mutation.LINKER_3P)
                        {
                            discrepancy_reports_3_linker.add(Mutation.getDescription(dd.getRNADefinition()).toLowerCase()+";");
                        }
                        break;
                    }
                    case TYPE_NOT_AA_AMBIQUOUS:
                    {
                        discr= dd.getRNADefinition();
                        String temp = "";
                        if ( ( discr.getSubjectStr() == null || discr.getSubjectStr().trim().length() == 0 )
                        || ( discr.getQueryStr() == null || discr.getQueryStr().trim().length() == 0 ))
                        {
                            temp = "amb";
                        }
        
                        discrepancy_reports_cds.add(temp+ Mutation.getDescription(discr).toLowerCase() +";");
                        break;
                    }
                    case TYPE_NOT_AA_REFSEQUENCE_AMB:
                    {
                        discr= dd.getRNADefinition();
                        String temp = "";
                        if ( ( discr.getSubjectStr() == null || discr.getSubjectStr().trim().length() == 0 )
                        || ( discr.getQueryStr() == null || discr.getQueryStr().trim().length() == 0 ))
                        {
                            temp = "refseq_amb";
                        }
        
                        discrepancy_reports_cds.add(temp+ Mutation.getDescription(discr).toLowerCase() +";");
                        break;
                    }
                }
            }
           
           if ( type instanceof String)
           {
               discrepancy_discription= "";
               for ( int count = 0 ; count < discrepancy_reports_5_linker.size(); count++)
               {
                   discrepancy_discription += (String)discrepancy_reports_5_linker.get(count);
               }
               discrepancy_discription += Constants.TAB_DELIMETER;
                for ( int count = 0 ; count < discrepancy_reports_cds.size(); count++)
               {
                   discrepancy_discription += (String)discrepancy_reports_cds.get(count);
               }
               discrepancy_discription += Constants.TAB_DELIMETER;
               
               for ( int count = 0 ; count < discrepancy_reports_3_linker.size(); count++)
               {
                   discrepancy_discription += (String)discrepancy_reports_3_linker.get(count);
               }
               return discrepancy_discription; 
           }
           else if ( type instanceof ArrayList)
           {
               discrepancy_reports_5_linker.addAll( discrepancy_reports_cds);
               discrepancy_reports_5_linker.addAll(discrepancy_reports_3_linker);
               return discrepancy_reports_5_linker;
           }
           return null;
       }
       //-------------------------------------------
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

