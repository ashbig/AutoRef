/*
 * IndexerForBlastDB.java
 *
 * Created on March 17, 2003, 3:16 PM
 */

package edu.harvard.med.hip.bec.export;

import java.io.*;
import java.util.*;
import edu.harvard.med.hip.bec.util.*;

/**
 *
 * @author  htaycher
 */
public class IndexerForBlastDB
{
    public static final int GI_INDEX = 1;
    public static final int ACESSES_INDEX = 2;
    public static final int ACESSES_INDEX_BASE = 3;
  
    private static final String db_pass = "C:\\GenBankmonth\\est_human";
    
    private static final String GI_INDEX_DB = "C:\\GenBankmonth\\gi_index";
    private static final String ACESSES_INDEX_DB = "C:\\GenBankmonth\\as_index";
    private static final String ACESSESBASE_INDEX_DB = "C:\\GenBankmonth\\asbase_index";
    
    //hold index in memory
    private static final Hashtable m_giindex_hash = new Hashtable();
     private static final Hashtable m_acindex_hash = new Hashtable();
      private static final Hashtable m_acbaseindex_hash = new Hashtable();
    
    private static final char LINE_DELIMITER = '\n';
    private static final char FIELD_DELIMITER = '|';
    /** Creates a new instance of IndexerForBlastDB */
    public IndexerForBlastDB()
    {
    }
    
    
    //build GI or Acesses index for Blastdb as binary file
    /*
      >gi|28436319|gb|CB185049.1|CB185049 EST HOX11 RT1C Homo sapiens leukaemia cells RT-PCR Homo sapiens cDNA
CTGTTATCTTGGGCACGAACAATGCACCGGTAGGCTGGTGATCGGTGGCGGGAAGACTACTCCCGGGACATTGGGTACTA
AGTGGTGGCCGNTGCCGCCTTCTCTGCTGCCCGCTCCCGCCTTCCTCTGCTGGCCCTGGCTCCCTCTGCTCTCGTTTCCA
CTCTGGGCACTCAACTCTCCCTCTGGCGTGGATCCCTTCCCCCGTTCTCACTTTCTGCTCCCCTTAGGGTTCCCTGGATC
CTAGGCCACGATCCTTCCGTCCGGGCTGGGAAGCAGCCACAAATTGAGCCCAGGTCACCCCTATCAGAACCGGACGCCCC
CCAAGAAGAAGAAG*/
    public static void buildIndex()
    {
        BufferedReader  fin = null;
        FileOutputStream fout_gi = null;  FileOutputStream fout_ac = null;
        DataOutputStream dout_gi = null; DataOutputStream dout_ac = null;
        DataOutputStream dout_acbase = null;
        String line = null; long gi = -1; String ac = null;
        ArrayList data = null;
        long current_pos = 0;
        boolean new_seq = false; long start =- 1; long end =-1;
        try
        {
            dout_gi = new DataOutputStream( new FileOutputStream(GI_INDEX_DB));
            
            fout_ac = new FileOutputStream(ACESSES_INDEX_DB);
            dout_ac = new DataOutputStream( fout_ac );
            dout_acbase = new DataOutputStream( new FileOutputStream(ACESSESBASE_INDEX_DB));
            File dbfile = new File(db_pass);
            
            int separator  = dbfile.separator.length();
            fin = new BufferedReader(new FileReader(dbfile));
            while ( (line = fin.readLine()) != null)
            {
                
                if (line.startsWith(">"))
                {
                    if (start != -1)//skip first entry
                    {
                        end = current_pos - separator;// -LINE_DELIMITER_LENGTH;
                        writeIndexData(gi, start,end,dout_gi);
                        writeIndexData(ac, start,end,dout_ac);
                        writeIndexData(ac.substring(0, ac.indexOf('.')), start,end,dout_acbase);
                     //  System.out.println(gi + " " + ac + " " + start + " " + end);
                    }
                    
                    data = Algorithms.splitString(line,"|");
                    gi = Long.parseLong( (String ) data.get(1));
                    ac = (String) data.get(3);
                    
                    start = current_pos + line.length() +separator;//LINE_DELIMITER_LENGTH;
                }             
                
                current_pos += line.length() + separator;//LINE_DELIMITER_LENGTH;
                
            }
            //last record
            end = current_pos -1;
            writeIndexData(gi, start,end,dout_gi);
            writeIndexData(ac, start,end,dout_ac);
            writeIndexData(ac.substring(0, ac.indexOf('.')), start,end,dout_acbase);
           // System.out.println(gi + " " + ac + " " + start + " " + end);
           
            fin.close();dout_gi.close();dout_ac.close();dout_acbase.close();
        }
        catch (Exception e)
        {
            System.out.println("Cannot build index file");
            try
            {fin.close();dout_gi.close();dout_ac.close();dout_acbase.close();} catch(Exception e1)
            {}
        }
    }
    
    
    
    private static void writeIndexData(long gi, long start,long end,DataOutputStream dout_gi) throws Exception
    {
        dout_gi.writeLong(gi);
        dout_gi.writeLong(start);
        dout_gi.writeLong(end);
        dout_gi.writeChar(LINE_DELIMITER);
    }
    
    private static void writeIndexData(String ac, long start,long end,DataOutputStream dout_ac) throws Exception
    {
        char[] arr_ac = ac.toCharArray();
        for (int i = 0; i < arr_ac.length;i++)
        {
            dout_ac.writeChar( arr_ac[i]);
        }
        
        dout_ac.writeChar(FIELD_DELIMITER);
        dout_ac.writeLong(start);
       // dout_ac.writeChar('|');
        dout_ac.writeLong(end);
        dout_ac.writeChar(LINE_DELIMITER);
        
    }
    //-------------------------------------------------------
    //bring index to memory
    public static void getIndexInMemory(int index_type)
    {
        switch (index_type)
        {
            case IndexerForBlastDB.GI_INDEX:
            {
               readGIIndex(null);
               break;
            }
            
            case IndexerForBlastDB.ACESSES_INDEX:
            {
               readAcessesionIndex(null, IndexerForBlastDB.ACESSES_INDEX);
               break;
            }
            case IndexerForBlastDB.ACESSES_INDEX_BASE:
            {
               readAcessesionIndex(null, IndexerForBlastDB.ACESSES_INDEX_BASE);
               break;
            }
        }
    }
    //--------------------------------------------------------------
    private static  long[] getSequencePosition( String id, int index_type)
    {
        long[] coordinates = new long[2];
        
        switch (index_type)
        {
            case GI_INDEX:
            {
                coordinates = readGIIndex(id);
                break;
            }
            case ACESSES_INDEX:
            {
                coordinates = readAcessesionIndex(id,ACESSES_INDEX);
                 break;
            }
            case ACESSES_INDEX_BASE:
            {
                coordinates = readAcessesionIndex(id,ACESSES_INDEX_BASE);
                break;
            }
        }
        
        return coordinates;
    }
    
    private static  long[] readGIIndex( String id)
    {
        long[] coordinates = {-1,-1};
        //first try to get from memory
        if (m_giindex_hash.size() != 0)
        {
            coordinates = (long[])m_giindex_hash.get(id);
            if (coordinates[0] != -1 && coordinates[1] != -1)
                return coordinates;
        }
        
        //
        long gi_id = -1;
        if (id != null) 
        {
             gi_id = Long.parseLong(id);
        }
        
        FileInputStream fis;
        DataInputStream dis;
        try
        {
            fis = new FileInputStream(GI_INDEX_DB);
            dis = new DataInputStream( fis );
            
            long gi; long coord1; long coord2;
            
            while ( true )
            {
                gi = dis.readLong();
                coord1  = dis.readLong();
                coord2 = dis.readLong();
                dis.readChar();
                if (id != null && gi == gi_id)
                {
                    coordinates[0] = coord1;
                    coordinates[1] = coord2;
                    break;
                }
                if (id == null)//take index in memory
                {
                    coordinates[0] = coord1;
                    coordinates[1] = coord2;
                    m_giindex_hash.put(String.valueOf(gi), coordinates);
                    coordinates = new long[2];
                }
            }
        } catch (EOFException eof)
        {System.out.println( "EOF reached " ); }
        catch (IOException ioe)
        {System.out.println( "IO error: " + ioe );}
        
        return coordinates;
    }
    
    
    
    private static  long[] readAcessesionIndex( String id, int mode)
    {
        long[] coordinates = {-1,-1};
        
        if (IndexerForBlastDB.ACESSES_INDEX == mode && m_acindex_hash.size() != 0)
        {
            coordinates = (long[])m_acindex_hash.get(id);
            if (coordinates[0] != -1 && coordinates[1] != -1)
                return coordinates;
        }
        if (IndexerForBlastDB.ACESSES_INDEX_BASE == mode && m_acbaseindex_hash.size() != 0)
        {
            coordinates = (long[])m_acbaseindex_hash.get(id);
            if (coordinates[0] != -1 && coordinates[1] != -1)
                return coordinates;
        }
        FileInputStream fis = null;
        DataInputStream dis = null;
        String ac = "";
        try
        {
            if (mode == IndexerForBlastDB.ACESSES_INDEX)
                fis = new FileInputStream(ACESSES_INDEX_DB);
            else if (mode ==IndexerForBlastDB.ACESSES_INDEX_BASE)
                fis = new FileInputStream(ACESSESBASE_INDEX_DB);
            dis =  new DataInputStream(fis);
            char cur_char ;long coord1; long coord2; StringBuffer acb = new StringBuffer();
            while ( true )
            {
               //read acession
                while ( (cur_char = dis.readChar()) != FIELD_DELIMITER)
                {
                    acb.append( cur_char);
                }
               ac=acb.toString();
                coord1  = dis.readLong();
                coord2 = dis.readLong();
                dis.readChar();
                
                if (id != null && ac.equalsIgnoreCase(id))
                {
                    coordinates[0] = coord1;
                    coordinates[1] = coord2;
                    break;
                }
                if (id == null)//take index in memory
                {
                    coordinates[0] = coord1;
                    coordinates[1] = coord2;
                     if (mode == IndexerForBlastDB.ACESSES_INDEX)
                        m_acindex_hash.put(ac, coordinates);
                    else if (mode ==IndexerForBlastDB.ACESSES_INDEX_BASE)
                        m_acbaseindex_hash.put(ac, coordinates);
                    coordinates = new long[2];
                }
                ac  = ""; 
            }
            dis.close();
        }
        catch (EOFException eof)
            {System.out.println( "EOF reached " ); }
        catch (IOException ioe)
            {System.out.println( "IO error: " + ioe );}
        finally  
        {
            try {dis.close();}catch(Exception e){}
        }
        return coordinates;
    }
    
    /*
    private static  long[] readAcessesionIndex( String id, int mode)
    {
        long[] coordinates = {-1,-1};
        
        if (IndexerForBlastDB.ACESSES_INDEX == mode && m_acindex_hash.size() != 0)
        {
            coordinates = (long[])m_acindex_hash.get(id);
            if (coordinates[0] != -1 && coordinates[1] != -1)
                return coordinates;
        }
        if (IndexerForBlastDB.ACESSES_INDEX_BASE == mode && m_acbaseindex_hash.size() != 0)
        {
            coordinates = (long[])m_acbaseindex_hash.get(id);
            if (coordinates[0] != -1 && coordinates[1] != -1)
                return coordinates;
        }
        FileReader fis = null;
        BufferedReader dis = null;
        String line = null;ArrayList data = null;
        try
        {
            if (mode == IndexerForBlastDB.ACESSES_INDEX)
                fis = new FileReader(ACESSES_INDEX_DB);
            else if (mode ==IndexerForBlastDB.ACESSES_INDEX_BASE)
                fis = new FileReader(ACESSESBASE_INDEX_DB);
            dis =  new BufferedReader(fis);
            while ( (line = dis.readLine()) != null)
            {
                data = Algorithms.splitString(line, "|");
                String ac = (String ) data.get(0);
                long coord1 = Long.parseLong( (String) data.get(1));
                long    coord2 = Long.parseLong( (String) data.get(2));
                if (id != null && ac.equalsIgnoreCase(id))
                {
                    coordinates[0] = coord1;
                    coordinates[1] = coord2;
                    break;
                }
                if (id == null)//take index in memory
                {
                    coordinates[0] = coord1;
                    coordinates[1] = coord2;
                     if (mode == IndexerForBlastDB.ACESSES_INDEX)
                        m_acindex_hash.put(ac, coordinates);
                    else if (mode ==IndexerForBlastDB.ACESSES_INDEX_BASE)
                        m_acbaseindex_hash.put(ac, coordinates);
                    coordinates = new long[2];
                }
            }
        } catch (EOFException eof)
        {System.out.println( "EOF reached " ); }
        catch (IOException ioe)
        {System.out.println( "IO error: " + ioe );}
        
        return coordinates;
    }
    */
    //-----------------------------------------------------------------------
    public static String getSequence(String id, int index_type) throws BecUtilException
    {
        long[] coordinates = getSequencePosition( id,  index_type);
        long start = coordinates[0];
        long end = coordinates[1];
        if (start==0 || end ==0 || start ==end) return null;
        try
        {
            // Create the byte array to hold the data
            byte[] bytes = new byte[(int)(end -  start)];
            
            File f = new File(db_pass);
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            boolean existing = f.exists();
            if (existing)
            {
                // Seek to end of file
                raf.seek(start);
                raf.readFully(bytes);
            }
            raf.close();
            return new String(bytes);
        } catch (Exception e)
        {
            throw new BecUtilException("Cannot get sequence");
        }
        
       
    }
    
    
    
    
    public static void main(String [] args)
    {
        //IndexerForBlastDB.buildIndex();
      //  System.out.println(System.currentTimeMillis());
      // getIndexInMemory(IndexerForBlastDB.GI_INDEX);
      // System.out.println(System.currentTimeMillis());
       //getIndexInMemory(IndexerForBlastDB.ACESSES_INDEX_BASE);
       // getIndexInMemory(IndexerForBlastDB.ACESSES_INDEX);
       // System.out.println(System.currentTimeMillis());
     
     try{System.out.println( IndexerForBlastDB.getSequence("28911568",GI_INDEX)) ;}
     catch(Exception e){}
    //  System.out.println(System.currentTimeMillis());
    //  System.out.println( "--------------");
    //  System.out.println( IndexerForBlastDB.getSequence("28436320",GI_INDEX)) ;
    //   System.out.println( "--------------");
     //  System.out.println( IndexerForBlastDB.getSequence("28289948",GI_INDEX)) ;
     //  System.out.println( "--------------");
     //  System.out.println( IndexerForBlastDB.getSequence("28289949",GI_INDEX)) ;
   //       System.out.println( IndexerForBlastDB.getSequence("CB185049.1",ACESSES_INDEX)) ;
 //  System.out.println( IndexerForBlastDB.getSequence("CB185049",ACESSES_INDEX_BASE));
       System.exit(0);
            
    }
}
