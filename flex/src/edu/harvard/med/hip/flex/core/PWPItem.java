/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.workflow.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class PWPItem 
{
        private int m_projectid = -1;
        private int m_workflowid=-1;
        private int m_protocolid = -1;
        private String  m_name = null;
        private String m_value = null;
        private String i_key = null;
        
        
        public PWPItem(){}
        public PWPItem(int p, int w, int pp, String n, String v)
        {
             m_projectid = p;
             m_workflowid=w;
             m_protocolid = pp;
             m_name = n;
             m_value = v;
             i_key= ""+p +ProjectWorkflowProtocolInfo.PWP_SEPARATOR+ 
                     w + ProjectWorkflowProtocolInfo.PWP_SEPARATOR+
                    pp +ProjectWorkflowProtocolInfo.PWP_SEPARATOR+ 
                     n;
       
        }
        public int getProjectId (){ return m_projectid   ;}
        public int getWorkflowId (){return  m_workflowid ;}
        public int getProtocolId (){ return m_protocolid  ;}
        public String  getName (){return  m_name   ;}
        public String getValue (){ return m_value   ;}
        public String  getKey(){ return  i_key   ;}
        
         public void setProjectId (int v){  m_projectid=v   ;}
        public void setWorkflowId (int v){m_workflowid =v;}
        public void setProtocolId (int v){ m_protocolid =v ;}
        public void  setName (String v){  m_name  =v ;}
        public void  setValue (String v){  m_value  =v ;}
        public void setKey(String v){   i_key   =v;}
        
        
        public static PWPItem    getItem(String key, Collection<PWPItem> items)
        {
            if (items == null || items.size() == 0) return null;
            for (PWPItem item: items)
            {
                if ( item.getKey().equals(key))
                    return item;
            }
            return null;
        }
}
