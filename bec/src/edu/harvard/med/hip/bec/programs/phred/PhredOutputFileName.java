/*
 * PhredOutputFileName.java
 *
 * Created on April 17, 2003, 5:20 PM
 */

package edu.harvard.med.hip.bec.programs.phred;

/**
 *
 * @author  htaycher
 */
import java.io.*;
import java.util.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;

/* Parse information from trace files named according to trace naming convention:
 * 125600m_A01m_495168m_1208972_F0.ab1
 * plateidm_wellidm_sequenceidm_cloneidm_strand(read number)
 *
 */
public class PhredOutputFileName
{
    
    private static final String SEPARATOR = "_";
   
    
    public static final int    FORMAT_OURS = 0;
   
    //different nomenculature can be used - our used numbers
    private String m_filename_full = null;
    private String m_filename = null;
    private String m_plateid = null;
    private String m_wellid = null;
    private String m_sequenceid = null;
    private int    m_read_num = -1;
    private int    m_read_type = -1;
    private String m_cloneid = null;
    
    private int m_plateid_num = -1;
    private int m_wellid_num =-1;
    private int m_sequenceid_num=-1;
    private int    m_cloneid_num = -1;
   
    //parse input trace file name and construct SequenceFileName obj
    public PhredOutputFileName(String sequence_file_name)
    {
        m_filename_full = sequence_file_name;
        int sep_position = m_filename_full.lastIndexOf(File.separator);
        if (sep_position != -1)//full file name
        {
            m_filename = m_filename_full.substring(sep_position+1);
        }
        else
            m_filename = m_filename_full;
     
    } // SequenceFileName
    
    
    public PhredOutputFileName(String sequence_file_name, int mode) throws Exception
    {
        this(sequence_file_name);
        parse(mode);
        
    }
    
    public String getCloneid()    {        return m_cloneid;    } // getIsolateid
    public String getFileName()    {        return m_filename;    } // getName
    public String getPlateid()    {        return m_plateid;    } // getPlateid
    public String getWellid()    {        return m_wellid;    } // getWellid
    public String getSequenceid()    {        return m_sequenceid;    } // getSequenceid
    public int       getReadNumber()    {        return m_read_num;    } // getReadNum
    public int       getReadType()    {        return m_read_type;    } // getReadNum
 
    public int getCloneidNumber()    {        return m_cloneid_num    ;} // getIsolateid
    public int getPlateidNumber()    {        return m_plateid_num    ;} // getPlateid
    public int getWellidNumber()    {        return m_wellid_num    ;} // getWellid
    public int getSequenceidNumber()    {        return m_sequenceid_num    ;} 
    
    public boolean isWriteFileFormat(int mode)
    {
        try
        {
            parse(mode);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public void  parse( int mode) throws BecUtilException
    {
        String read_num ;
        
       m_filename = m_filename.toUpperCase();
        ArrayList arr = Algorithms.splitString( m_filename,  SEPARATOR);
        if (arr.size() != 5)
            throw new BecUtilException("Wrong file name.");
        m_plateid = (String) arr.get(0);
        m_wellid =  (String) arr.get(1);
        m_sequenceid =  (String) arr.get(2);
        m_cloneid =(String) arr.get(3);
        char[] temp = ((String) arr.get(4)).toCharArray();
        if (temp[0] == 'F' && temp[1] == '0') 
        {
            m_read_type = Read.TYPE_ENDREAD_FORWARD;
        }
        else if (temp[0] == 'R' && temp[1] == '0') 
        {
            m_read_type = Read.TYPE_ENDREAD_REVERSE;
        }
        else if (temp[0] == 'F' && temp[1] != '0') 
        {
            m_read_type = Read.TYPE_INNER_FORWARD;
            m_read_num = getReadNumber(temp);
        }
        else if (temp[0] == 'R' && temp[1] != '0') 
        {
            m_read_type = Read.TYPE_INNER_REVERSE;
            m_read_num = getReadNumber(temp);
        }
        if (mode == FORMAT_OURS)
        {
            try
            {
                
                m_plateid_num = Integer.parseInt(m_plateid);
                m_wellid_num = Algorithms.convertWellFromA8_12toInt(m_wellid);
                m_sequenceid_num= Integer.parseInt(m_sequenceid);
                m_cloneid_num = Integer.parseInt(m_cloneid);
            }
            catch(Exception e){ throw new BecUtilException("Wrong read ids. "+m_filename);}
        }
   
    }
    
    
    private int getReadNumber(char[] temp)throws BecUtilException
    {
        String read_num="";
        for (int count = 1; count < temp.length;count++)
        {
            if (temp[count] == '.') break;
            read_num += temp[count];
        }
        try
        {
            return Integer.parseInt( read_num);
        }
        catch(Exception e){ throw new BecUtilException("Wrong read number.");}
    }
    
     public static void main(String args[])
    {
        try{
        PhredOutputFileName p = new PhredOutputFileName("C:\\bio\\plate_analysis\\clone_samples\\1029\\114779\\chromat_dir\\7204_D11_1029_114779_F1.ab1",PhredOutputFileName.FORMAT_OURS);
        System.out.println( p.isWriteFileFormat(PhredOutputFileName.FORMAT_OURS));
        }catch(Exception e){}
        System.exit(0);
     }
   
}
