/*
 * SequenceElement.java
 *
 * Created on August 5, 2003, 12:29 PM
 */

package edu.harvard.med.hip.bec.bioutil;

/**
 *
 * @author  HTaycher
 */
public class SequenceElement
{
    
   
        private int m_query_index = -1;
        private int m_subject_index = -1;
        private int m_base_score = 0;
        private char m_query_base ;
        private char m_subject_base;

        public SequenceElement( int query_index  ,int subject_index ,int base_score,char query_base,char subject_base)
        {
            m_query_index = query_index;
            m_subject_index = subject_index;
            m_base_score = base_score;
            m_query_base = query_base;
            m_subject_base = subject_base;

        }
         public SequenceElement(){}

         public int getQueryIndex (){ return m_query_index    ; }
        public int getSubjectIndex (){ return m_subject_index    ; }
        public int getBaseScore (){ return m_base_score    ; }
        public char getQueryChar (){ return m_query_base   ; }
        public char getSubjectChar (){ return m_subject_base   ; }

         public void setQueryIndex (int v){  m_query_index    = v; }
        public void setSubjectIndex (int v){  m_subject_index    = v; }
        public void setBaseScore (int v){  m_base_score    = v; }
        public void setQueryChar (char v){  m_query_base   = v; }
        public void setSubjectChar (char v){  m_subject_base   = v; }
    
     
}
