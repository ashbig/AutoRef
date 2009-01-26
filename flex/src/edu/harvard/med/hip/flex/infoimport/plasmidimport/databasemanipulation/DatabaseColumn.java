/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation;

import java.util.*;
/**
 *
 * @author htaycher
 */
public class DatabaseColumn {
    
    private String m_name;
    private boolean m_nullable;
    private String m_datatype;
    private int     m_length;
    private ArrayList<String> m_data;
    
    DatabaseColumn(){m_data=new ArrayList();}
    DatabaseColumn(String v, String v1, boolean v2, int v4)
    { m_name =v;m_nullable =v2; m_datatype =v1;  m_length =v4;m_data=new ArrayList();}
    
    public String getName(){ return m_name;}
    public void setName(String v){ m_name = v;}
    
    public boolean getIsNullable(){ return m_nullable;}
    public void setIsNullable(boolean v){  m_nullable = v;}
    
    public String getDataType(){ return m_datatype;}
    public void setDataType(String v){ m_datatype=v;}
    
    public int getLength(){ return m_length;}
    public void setLength(int v){      m_length=v;}
    
    public ArrayList<String> getData(){ return m_data;}
    public void     setData(ArrayList<String> s){m_data=s;}
            

}
