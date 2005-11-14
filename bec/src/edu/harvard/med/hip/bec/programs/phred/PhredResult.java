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
        String line = null;
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
    
    
   
    
    public   boolean validate()throws BecUtilException
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
    
    
    //used for read quality assessment
     public static ArrayList parsePhredOutputForManyReads(String qfile, String sfile)throws BecUtilException
    {
        ArrayList q_reads = parseFileForManyReads(qfile, 1);
        ArrayList s_reads = parseFileForManyReads(sfile, 0);
        return combineReadsData(q_reads,s_reads);
        
    }
     
     private static ArrayList combineReadsData(ArrayList q_reads,ArrayList s_reads)throws BecUtilException
     {
         PhredResult q_result = null;
         PhredResult s_result = null;
         if ( q_reads.size() != s_reads.size()) throw new  BecUtilException("Not equal number of sequence and score record");
         for (int count = 0; count < q_reads.size(); count++)
         {
             q_result = (PhredResult)q_reads.get(count);
             s_result = (PhredResult)s_reads.get(count);
              if ( !q_result.getFileName().equalsIgnoreCase(s_result.getFileName()))
                throw new BecUtilException("Sequence record does not match score record. Sequence record " +s_result.getFileName()+", score record "+q_result.getFileName() );
             q_result.setSequence( s_result.getSequence());
             q_result.validate();
           
          }
         return q_reads;
     }
    
     public  static ArrayList parseFileForManyReads(String qfile, int mode)throws BecUtilException
    {
        BufferedReader  fin = null;
        String line = null;
        ArrayList headerinfo = new ArrayList();
        ArrayList reads = new ArrayList();
        PhredResult read = null;
        StringBuffer string_property = new StringBuffer();
        boolean isInsideRead = false;
        int read_length = 0; int read_trim_start = 0; int read_trim_end = 0;
        try
        {
            fin = new BufferedReader(new FileReader(qfile));
            while ((line = fin.readLine()) != null)
            {
              //header
                if ( line.startsWith(">")  )
                {
                    if ( read != null) 
                    {
                        if ( mode == 1)read.setQualityScores( string_property.toString());
                        else if ( mode == 0 )read.setSequence(string_property.toString());
                        reads.add(read);
                        string_property = new StringBuffer();
                    }
                    headerinfo = Algorithms.splitString( line.substring(1),  PHRED_SEPARATOR);
                    if (headerinfo.size() < 5) { isInsideRead = false; continue;}
                    else
                    {
                         read =  new PhredResult();
                        read.setFileName((String) headerinfo.get(0));
                         // Get read length.
                        read.setSequenceLength( Integer.parseInt( (String)headerinfo.get(1)));
                        // Get trimming start.
                        read.setTrimmingStart( Integer.parseInt( (String)headerinfo.get(2)));
                        // Get trimming end.
                        read.setTrimmingEnd(  Integer.parseInt( (String)headerinfo.get(3)));
                        isInsideRead = true;
                    }
                }
                else  if ( isInsideRead && !line.equals("") )
                {
                    string_property.append(line);
                }
            }
           
            fin.close();
            if ( read != null) 
            {
                 if ( mode == 1)read.setQualityScores( string_property.toString());
                 else if ( mode == 0 )read.setSequence(string_property.toString());
                 reads.add(read);
            }
             return reads;
        }
        catch(Exception e)
        {
            try
            {fin.close();}catch(Exception c)            {}
            throw new  BecUtilException("Cannot parse phred output");
        }
    }
     
     //-----------------------------
   public static void main(String args[])
 { 
     ArrayList result = null;
    try
      {
            BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
    
       result = PhredResult.parsePhredOutputForManyReads("c:\\tmp\\test.tex","c:\\tmp\\tst.t");
       
      }
      catch (Exception e)
      {
       e.printStackTrace();
      }
        System.exit(0);
   }
}
