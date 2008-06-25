/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.coreobjectsforimport;

/**
 *
 * @author htaycher
 */
public class ImportCloneSequence
{
        private int                 i_sequence_id = -1;
        private String              i_sequence_text = null;
        private int                 i_cds_start = -1;    
        private int                 i_cds_stop =-1;
        private String              i_mut_linker_5p = null;
        private String              i_mut_linker_3p = null;
        private String              i_mut_cds = null;
        private boolean             i_is_discrepancies = false;
        
        public  ImportCloneSequence()
        {
            i_mut_linker_5p = "";
            i_mut_linker_3p = "";
            i_mut_cds = "";
        }
         public  ImportCloneSequence(int id)
        {
            i_sequence_id=id;
        }
        public int                 getSequenceID(){ return i_sequence_id ;}
        public String               getSequenceText(){ return i_sequence_text ;}
        public int                 getCDSStart(){ return i_cds_start ;}   
        public int                 getCDSStop(){ return i_cds_stop ;}
        public String              getMLinker5(){ return i_mut_linker_5p;}
        public String              getMutLinker3(){ return i_mut_linker_3p ;}
        public String              getMutCDS(){ return i_mut_cds ;}
        public boolean              isDiscrepancies(){ return i_is_discrepancies;}
        
        public void                 setSequenceID(int v){  i_sequence_id = v;}
        public  void                setSequenceText(String v){  i_sequence_text = v;}
        public void                 setCDSStart(int v){  i_cds_start = v;}   
        public void                 setCDSStop(int v){  i_cds_stop= v ;}
        public void                 setMLinker5(String v){  i_mut_linker_5p= v;}
        public void              setMutLinker3(String v){  i_mut_linker_3p= v ;}
        public void              setMutCDS(String v){  i_mut_cds = v;}
        public void                 setIsDiscrepancy(boolean v){ i_is_discrepancies = v;}
        
}
