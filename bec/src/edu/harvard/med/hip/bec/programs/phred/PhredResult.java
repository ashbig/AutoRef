/*
 * PhredResult.java
 *
 * Created on April 17, 2003, 12:34 PM
 */

package edu.harvard.med.hip.bec.programs.phred;

import edu.harvard.med.hip.bec.bioutil.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;

import java.io.*;
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class PhredResult
{
    public static final String PHRED_SEPARATOR= " ";
    
    private String m_qualityScores = null;
    private String m_sequence = null;
    private int m_sequenceLength = -1;
    private int m_trimmingEnd = -1;
    private int m_trimmingStart = -1;
    private String m_filename = null;
    
    
    
    
    public PhredResult()
    {
    } // PhredOutput
    
    // getters & setters
    public String getQualityScores()    {  return m_qualityScores; }
    public String getSequence()    {  return m_sequence; }
    public int getTrimmingEnd()    {  return m_trimmingEnd; }
    public int getTrimmingStart()    {  return m_trimmingStart; }
    public int getSequenceLength()    {  return m_sequenceLength ; }
    public String   getFileName(){ return m_filename;}
    
    
    public void setQualityScores(String qualitym_scores)    {  m_qualityScores = qualitym_scores; }
    public void setSequence(String sequence)    {  m_sequence = sequence; }
    public void setSequenceLength(int sequence_length)    {  m_sequenceLength = sequence_length; }
    public void setTrimmingEnd(int trimming_end)    {  m_trimmingEnd = trimming_end; }
    public void setTrimmingStart(int trimming_start)    {  m_trimmingStart = trimming_start; }
    public void setFileName(String v){ m_filename = v;}
    
    public void parsePhredOutput(String qfile, String sfile)throws BecUtilException
    {
        parseQualityFile(qfile);
        parseSequenceFile(sfile);
        validate();
    }
    
   
    public  Read parsePhredOutputIntoRead(String qfile, String sfile)throws Exception
    {
        parsePhredOutput( qfile,  sfile);
        PhredOutputFileName prfn = new PhredOutputFileName(qfile, PhredOutputFileName.FORMAT_OURS);
        Read read = new Read();
     
        read.setType(prfn.getReadType()) ;
        // file name hase format ffffff.ab1(scf).qual  -> ffffff.ab1(scf)
        String fname= Algorithms.replaceString(qfile,".qual","");
        read.setTraceFileName(fname) ;
        //read.setMachine(prfn.get) ;
        //read.setCapilarity(String v)  ;
        read.setTrimStart(m_trimmingStart)  ;
        read.setTrimEnd(m_trimmingEnd) ;
        read.setSequence(m_sequence, m_qualityScores,-1);
        read.setFLEXReadInfo(prfn.getCloneidNumber(), prfn.getSequenceidNumber(), prfn.getPlateidNumber(), prfn.getWellidNumber());
        return read;
    }
    
    //******************************************************
    private void parseQualityFile(String qfile)throws BecUtilException
    {
        BufferedReader  fin = null;
        String line;
        StringBuffer scores = new StringBuffer();
        try
        {
            fin = new BufferedReader(new FileReader(qfile));
            while ((line = fin.readLine()) != null)
            {
                //header     and empty line
                if ( ! line.startsWith(">") && ! line.equals("") )
                {
                    scores.append(line);
                }
            }
            m_qualityScores = scores.toString();
            fin.close();
        }
        catch(Exception e)
        {
            try
            {fin.close();}catch(Exception c)            {}
            throw new  BecUtilException("Cannot parse phred output");
        }
    }
    
    private void parseSequenceFile(String sfile)throws BecUtilException
    {
        BufferedReader  fin = null;
        String line;
        StringBuffer seq = new StringBuffer();
        try
        {
            fin = new BufferedReader(new FileReader(sfile));
            while ((line = fin.readLine()) != null)
            {
              
                //header
                if ( line.startsWith(">")  )
                {
                    extractHeaderInfo(line);
                }
                else 
                    if ( !line.equals("") )
                    {
                        seq.append(line);
                    }
            }
            m_sequence = seq.toString();
            fin.close();
        }
        catch(Exception e)
        {
            try
            {fin.close();}catch(Exception c)
            {}
            throw new  BecUtilException("Cannot parse phred output");
        }
    }
    
    //format is the same for quality and sequence files
    private void extractHeaderInfo(String header) throws BecUtilException
    {
        try
        {
            // The format is as follows:
            // >125763_A08_168128_1_2580.ab1    255      0    255  ABI
            // >file_name read_length trimming_start trimming_end
            ArrayList headerinfo = Algorithms.splitString( header.substring(1),  PHRED_SEPARATOR);
            if (headerinfo.size() < 5) throw new BecUtilException("Error reading phred header");
            m_filename = (String) headerinfo.get(0);
            // Get read length.
            m_sequenceLength = Integer.parseInt( (String)headerinfo.get(1));
            // Get trimming start.
            m_trimmingStart = Integer.parseInt( (String)headerinfo.get(2));
            // Get trimming end.
            m_trimmingEnd = Integer.parseInt( (String)headerinfo.get(3));
        }
        catch(Exception e)
        {
            throw new BecUtilException("Error reading phred header");
        }
    } // extractHeaderInfo
    
    
    private boolean validate()throws BecUtilException
    {
        ArrayList  scores= Algorithms.splitString( m_qualityScores,  PHRED_SEPARATOR);
       
        if(scores.size() != m_sequence.length() )
        {
            throw new BecUtilException("SymbolLists must be of equal length "+ m_sequence.length()+" : " + scores.size());
        }
        
       
        if( !Alphabet.isIntegerArrayString(scores, 0,99))
        {
            throw new BecUtilException( "Expecting SymbolList quality to use the iteger alphabet.");
           
        }
        if( !Alphabet.isDNASequence(m_sequence, Alphabet.DNA_SEQUENCE_IS_GAP_NOTOK) )
        {
            throw new BecUtilException("Expecting SymbolList 'dna' to use the DNA alphabet.");
        }
        return true;
    }
    
    
    
   public static void main(String args[])
 { 
     PhredResult result = new PhredResult();
     Read r = null;
    try
      {
       String ql="C:\\bio\\plate_analysis\\clone_samples\\12894\\43935\\quality_dir\\7170_F12_1003_110994_R1.ab1.qual";
       String sf = "C:\\bio\\plate_analysis\\clone_samples\\12894\\43935\\sequence_dir\\7170_F12_1003_110994_R1.ab1.seq";
       
       r=result.parsePhredOutputIntoRead(ql,sf);
       
      }
      catch (Exception e)
      {
       e.printStackTrace();
      }
        System.exit(0);
   }
}
