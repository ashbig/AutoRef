/*
 * kenase.java
 *
 * Created on July 30, 2002, 3:47 PM
 */

package edu.harvard.med.hip.flex.special_projects;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.io.*;
/**
 *
 * @author  htaycher
 */
public class Kinase
{
    private int[]  m_requestId;
    /** Creates a new instance of kenase */
    public Kinase()
    {
    }
    
    
    public void setRequestId(int[] requestId)
    {
        m_requestId = requestId;
    }
    
    public void outputSequenceInfo(String fileName)
    {
        ArrayList seqInfo =  getSequenceInformation();
        printSequenceInfo( fileName,  seqInfo);
    }
    
    
    //*********************************************************
    private ArrayList getSequenceInformation()
    {
        ArrayList seqInfo = new ArrayList();
        
        String reqId ="";
        //get all sequence id
        ArrayList seqId = new ArrayList();
        for (int count = 0; count < m_requestId.length; count++)
        {
            reqId += m_requestId[count] ;
            if (count < m_requestId.length -1) reqId +=",";
        }
        
        String sql = "select distinct sequenceid from requestsequence where requestid in (" + reqId +")";
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            RowSet rs = t.executeQuery(sql);
            while(rs.next())
            {
                seqId.add( rs.getString("sequenceid") );
            }
            for (int count = 0; count < seqId.size(); count++)
            {
                FlexSequence fs = new FlexSequence(Integer.parseInt( (String) seqId.get(count) ) );
                seqInfo.add(fs);
            }
        }
        
        catch(Exception e)
        {}
        return seqInfo;
    }
    
    private void printSequenceInfo(String fileName, ArrayList seqInfo)
    {
        FileWriter writer = null;
        try
        {
            writer = new FileWriter(fileName);
            String title="Sequence Id\tDescription\tGI number\tAccession number\tLocus Link ID\t"
                        +"Gene Symbol \t Cds Start \t Cds stop \t Cds length \t Sequence length \tSequence text \n";
            writer.write(title);
            for (int count = 0; count < seqInfo.size(); count++)
            {
                writer.write (printSequence( (FlexSequence) seqInfo.get(count)) );
           
            }
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
        }
    }
    
    
    
    private String printSequence(FlexSequence fc)
    {
        String seqData = null;
        seqData = fc.getId() + "\t";
        seqData += fc.getDescription() + "\t";
        seqData += fc.getGi()+ "\t" ;
        seqData += fc.getAccession()+ "\t" ;
        seqData += fc.getLocusLinkId()+ "\t" ; 
        seqData += fc.getGenesymbolString()+ "\t" ;
        seqData += fc.getCdsstart()+ "\t" ;
        seqData += fc.getCdsstop()+ "\t" ;
        seqData += fc.getCdslength()+ "\t" ;
        seqData += fc.getSequenceLength()+ "\t" ;
        seqData += fc.getSequencetext()+ "\n" ;
        return seqData;
    }
    //**************************************************
    
    public static void main(String args[])
    {
        Kinase kn = new Kinase();
        int[] requestId = new int[]  {11,17,116,117};
        
        kn.setRequestId(requestId);
        kn.outputSequenceInfo("c:\\test.txt");
        System.exit(0);
    }
    
}
