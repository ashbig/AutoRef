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
    
    public void outputSequenceInfoSeqIds(String fileName, ArrayList arr)
    {
        ArrayList seqInfo =  arr;
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
                System.out.println(fs.getId());
            }
        }
        
        catch(Exception e)
        {}
        return seqInfo;
    }
    
    
    
    public ArrayList getSequenceInformationBasedOnSequenceID(int[]  sequenceId)
    {
        ArrayList seqInfo = new ArrayList();
        
        try
        {
            
            for (int count = 0; count < sequenceId.length; count++)
            {
                FlexSequence fs = new FlexSequence(sequenceId[count]);
                System.out.println(sequenceId[count]);
                seqInfo.add(fs);
            }
        }
        
        catch(Exception e)
        {}
        return seqInfo;
    }
    
    public void printSequenceInfo(String fileName, ArrayList seqInfo)
    {
        FileWriter writer = null;
        try
        {
            writer = new FileWriter(fileName);
            String title="Sequence Id\tDescription\tGI number\tAccession number\tLocus Link ID\t"
                        +"Gene Symbol \t Cds Start \t Cds stop \t Cds length \t Sequence length \tCodding Sequence\tSequence text \n";
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
         System.out.println("printing "+fc.getId());
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
        seqData += fc.getSequencetext().substring(fc.getCdsstart()-1,fc.getCdsstop())+ "\t" ;
        seqData += fc.getSequencetext()+"\n";
        return seqData;
    }
    //**************************************************
    
    public static void main(String args[])
    {
        System.out.println("here");
        Kinase kn = new Kinase();
      //  int[] requestId = new int[]  {11,17,116,117};
        
       // kn.setRequestId(requestId);
       // kn.outputSequenceInfo("c:\\test.txt");
        
        int[] sequenceId = new int[]  {17568	,
17616	,
17614	,
17606	,
17600	,
17576	,
17548	,
18155	,
18157	,
17544	,
18239	,
18136	,
18247	,
18133	,
17533	,
18206	,
18209	,
18093	,
17947	,
18039	,
18037	,
18019	,
17940	,
18065	,
18036	,
17824	,
17932	,
17897	,
17996	,
17973	,
17925	,
17875	,
17877	,
17855	,
17853	,
17783	,
17863	,
17822	,
17768	,
17747	,
17852	,
17847	,
17751	,
17769	,
17780	,
17802	,
17692	,
18610	,
18609	,
18602	,
18588	,
18516	,
18487	,
18580	,
18399	,
18415	,
18786	,
18381	,
18269	,
18787	,
18296	,
18804	,
18687	
};
        ArrayList arr = kn.getSequenceInformationBasedOnSequenceID(sequenceId);
        kn.printSequenceInfo("g:\\test.txt", arr);
        
        System.exit(0);
    }
    
}
