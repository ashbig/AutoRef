/*
 * LinkerDiscrepancy.java
 *
 * Created on June 25, 2003, 3:39 PM
 */

package edu.harvard.med.hip.bec.coreobjects.feature;

/**
 *
 * @author  htaycher
 */
 import  edu.harvard.med.hip.bec.util.*;
    import  edu.harvard.med.hip.bec.database.*;
    import java.sql.*;
    public class LinkerMutation extends Mutation
    {
 
        /** Creates a new instance of AAMutation */
        public LinkerMutation(int id) throws BecDatabaseException
        {
            super(id);
        }
        
        public LinkerMutation(boolean is5prime) 
        {
            super(); 
            if (is5prime)
                m_type=LINKER_5P;
            else
                m_type = LINKER_3P;
        }
        
        public int              getChangeType()
        { 
            if (m_change_type ==-1) m_change_type = setType();
            return m_change_type;
        }
        
       
         public String toHTMLString()
         {
             return "<table border=0>"+"</table>";
         }
         
     public String toString()
    {
        String res= "\n\n\t\t\tLinker mutation.\n id: " +  m_id +
        "\t position: "+m_position +" length: "+ m_length+"\t type: "+getTypeAsString()
        +"\t mut ori: "+ m_change_mut
        +" \t ori: "+m_change_ori
        +"\t sequence id: "+ m_sequenceid
        
        + " \t mutation type: "+ getMutationTypeAsString() +"\t quality "+getQualityAsString() ;
        return res;
    }
    
         
        
         
        private int setType()
        {
            String o =m_change_ori.toUpperCase();
            String m = m_change_mut.toUpperCase();
            if (o.length() == 0 ) o = null;
            if (m.length() == 0) m = null;
            
            int type = Mutation.TYPE_NONE;
            if ( (o == null && m!= null) || (o != null && m == null)
            || (o != null && o.indexOf("-") != -1) || (m != null && m.indexOf("-") != -1)
            && m_type == Mutation.LINKER_3P)
            {
                return Mutation.TYPE_LINKER_3_INS_DEL;
            }
            else if ( (o == null && m!= null) || (o != null && m == null)
            || (o != null && o.indexOf("-") != -1) || (m != null && m.indexOf("-") != -1)
            && m_type == Mutation.LINKER_5P)
            {
                return Mutation.TYPE_LINKER_5_INS_DEL;
            }
            else if ( (o != null && m!= null) && (o.length() ==  m.length() )
                     && m_type == Mutation.LINKER_3P)
            {
                return Mutation.TYPE_LINKER_3_SUBSTITUTION;
            }
            else if ( (o != null && m!= null) && (o.length() ==  m.length() )
                     && m_type == Mutation.LINKER_5P)
            {
                 return Mutation.TYPE_LINKER_5_SUBSTITUTION;
            }
            
          
            return type;
        }
    

    
}
