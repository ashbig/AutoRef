 /*
     * AAMutation.java
     *
     * Created on October 19, 2002, 6:55 AM
     */
    
    package edu.harvard.med.hip.bec.coreobjects.feature;
    
    import  edu.harvard.med.hip.bec.util.*;
    import  edu.harvard.med.hip.bec.database.*;
    import java.sql.*;
    /**
     *
     * @author  Administrator
     */
    public class AAMutation extends Mutation
    {
 
        /** Creates a new instance of AAMutation */
        public AAMutation(int id) throws BecDatabaseException
        {
            super(id);
        }
        
         public AAMutation() {super(); m_type=AA;}
        public int              getChangeType()
        { 
            if (m_change_type ==-1) m_change_type = setType();
            return m_change_type;
        }
        
        /*
        
         public AAMutation(int num,int pos, int ln,String obj, String subj, 
         int hit, int seq, int id,int type, int hittype)
        {
            super( num, pos,  ln, obj,  subj,  hit,  seq,  id, AA,hittype);
            if (type ==-1) 
                    m_mutation_type = setType();
            else 
                 m_mutation_type = type;
            
        }
         **/
     
         public String toHTMLString()
         {
             return "<table border=0>"+ super.toHTMLString()+"</table>";
         }
         
         
         
        
         
        private int setType()
        {
            String o =m_change_ori.toUpperCase();
            String m = m_change_mut.toUpperCase();
            if (o.length() == 0 ) o = null;
            if (m.length() == 0) m = null;
            
            int type = Mutation.TYPE_NOT_DEFINE;
             
            if (m_position == 1 )
            {
                 if (o != null && m==null)
                {
                    return Mutation.TYPE_AA_NO_TRANSLATION ;
                }
                 else if(o != null && m != null &&  !o.substring( 0, 1).equals(m.substring( 0, 1)))
                {
                    return Mutation.TYPE_AA_NO_TRANSLATION ;
                }
                else if(o != null && m != null && o.equals(m ) )
                {
                    return Mutation.TYPE_AA_SILENT;
                }
            }
            //never executed - fix this
            else if(o != null &&  o.substring( 0, 1).equals("*") )
            {
                if (m != null && !o.substring( 0, 1).equals(m.substring( 0, 1)))
                {
                    return Mutation.TYPE_AA_POST_ELONGATION;
                }
                else if(m != null && o.equals(m) )
                {
                    return Mutation.TYPE_AA_SILENT_CONSERVATIVE;
                }
            }
            else if(o!= null && m != null && o.equals(m))
            {
                return Mutation.TYPE_AA_SILENT_CONSERVATIVE;
            }
            else if(m != null && m.equals("*"))
            {
                return Mutation.TYPE_AA_TRUNCATION;
            }
           
            else if(m == null || m.length() == 0 ||
                (o != null && m != null &&  o.length() > m.length() && m.charAt(0) !='*'))
            {
                type = Mutation.TYPE_AA_DELETION;
                if (o != null && m != null)//?????????  && $o !~ $m && $o !~ $m) 
                {
                    return Mutation.TYPE_AA_DELETION_COMPLEX; 
                }
            }
            else if(o== null || o.length() == 0 ||
                ( o != null && m!= null && o.length() < m.length()  &&  m.charAt( 0) != '*' ))  
            {
                type = Mutation.TYPE_AA_INSERTION;	
                if (o!=null && m!= null)//??????????????? && $o !~ $m && $o !~ $m)
                {
                    return Mutation.TYPE_AA_INSERTION_COMPLEX; 
                }
            }
            else if  (o != null && m!=null && !o.equals(m) && o.length() == 1 
                && m.length()  == 1 )
            {
                type = Mutation.TYPE_AA_SUBSTITUTION;
                int similarity_score = -1000;
                similarity_score = edu.harvard.med.hip.bec.bioutil.BioConstants.getScore(o.charAt(0),m.charAt(0));
                if (similarity_score != -1000) 
                {
                    type = (similarity_score < 0) ? Mutation.TYPE_AA_NONCONSERVATIVE : Mutation.TYPE_AA_CONSERVATIVE;
                }
            } 
            else 
            {
                type = Mutation.TYPE_AA_OUT_OF_FRAME_TRANSLATION;
            }
            return type;
        }
    }
