//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * PhdFilesTrimmer.java
 *
 * Created on September 27, 2004, 11:26 PM
 */

package edu.harvard.med.hip.bec.programs.assembler;

import java.io.*;
import java.util.*;
/**
 *
 * @author  HTaycher
 */
public class PhdFilesTrimmer {
    
    /** Creates a new instance of PhdFilesTrimmer */
    
    public static String       LINE_SEPARATOR = System.getProperty("line.separator") ;
    //quality constants
    public static final int             MIN_SEQ_LEN	=20;
    public static final int             MAX_ENZ_POS	=100;
    public static final double             MIN_PRB_VAL=0.05;
    public static final double             MIN_AVG_QUAL	=10.0;
    
    private double                          m_min_pbd_val = MIN_PRB_VAL;
    private double                          m_score = MIN_AVG_QUAL;
    
    
    public static void main(String arg[])
    {
        String directory_name = null;//arg[0];
        double score = -1;
        if ( arg.length > 1)
            score = Double.parseDouble( arg[1]);
        else
            score = PhdFilesTrimmer.MIN_AVG_QUAL;
        PhdFilesTrimmer tester = new PhdFilesTrimmer();
        
        //directory_name="C:\\bio\\plate_analysis\\clone_samples\\4426";//\\phd_dir\\6142_E05_3277_4426_F5.ab1.phd.1";
        directory_name="/c/bio/plate_analysis/clone_samples/3277/4426";
        directory_name = convertUnixFileNameIntoWindows( directory_name);
                
        tester.runTrimming(directory_name, score);
    }
    //---------------------
    
    public PhdFilesTrimmer(){}
    
    public void  runTrimming(String directory_name, double score)
    {
        try
        {
                m_score = score;
                m_min_pbd_val = (float)Math.pow( (double)10.0, (double)( -score / 10.0 ) );
                File phd_dir = new File(directory_name + File.separator +"phd_dir");
                File [] phdFiles = phd_dir.listFiles();
                for (int count = 0 ; count< phdFiles.length; count++)
                {
                    try
                    {
                        trimPhdFile(phdFiles[count] );
                    }
                    catch(Exception e){}
                }
        }
        catch(Exception e){}
    
    }
    
    private void trimPhdFile(File phdFile) throws Exception
    {
    
        BufferedReader input = null;
        BufferedWriter output = null;
        boolean isInsideRead = false;
        String line = null;
        ArrayList scored_elements = new ArrayList ();
        ScoredElement element = null;
        String phdFile_name = null;
        boolean isBeforeDNAEnd = true; boolean isBeforeDNAStart = true;
        try
        {
            phdFile_name = phdFile.getAbsolutePath();
            input = new BufferedReader(new FileReader(phdFile));
            File output_file = new File(phdFile_name + "_new");
            output = new BufferedWriter(new FileWriter(output_file));
            while ((line = input.readLine()) != null)
            {
   
                if ( isBeforeDNAStart || ( !isBeforeDNAEnd && !isBeforeDNAStart ) ) 
                {
                    output.write( line + LINE_SEPARATOR ) ;
                }
                else 
                {
                    element = new ScoredElement(line) ;
                    if (element == null) throw new Exception();
                    scored_elements.add(element );
                }
                if ( isBeforeDNAStart && line.indexOf("BEGIN_DNA" ) != -1) 
                    isBeforeDNAStart = false ;
                if (isBeforeDNAEnd && line.indexOf("END_DNA" ) != -1)
                {
                    scored_elements = trimScoredArray(scored_elements);
                    if (scored_elements.size() > 50 )
                        writeScoredArray(scored_elements, output);
                    isBeforeDNAEnd = false;
                    output.write( line + LINE_SEPARATOR ) ;
                }
            }
            output.flush();
            output.close();input.close();
            
            saveAsFile( phdFile_name, output_file.getAbsolutePath() );
            output_file.delete();
        }
        catch (Exception e)
        {
            
         }
    }
    
    public   ArrayList  trimScoredArray(ArrayList scored_elements)
    {
        ArrayList trimmed_scored_elements = new ArrayList();
        int[] trimmed_coordinates =  trimSequencePhredAlgorithm(scored_elements);
        for(int count = trimmed_coordinates[0]; count < trimmed_coordinates[1]; count++)
        {
            trimmed_scored_elements.add( scored_elements.get(count) );
        }
        return trimmed_scored_elements ;
    }
    
    public  void          writeScoredArray(ArrayList scored_elements,
                            BufferedWriter output) throws Exception
    {
        ScoredElement element = null;
        for (int count = 0; count < scored_elements.size() ; count++)
        {
            element = (ScoredElement) scored_elements.get(count);
            output.write( element.toString() );
        }
        output.flush();
    }
    
    
    
    public  int[]     trimSequencePhredAlgorithm  (ArrayList scored_elements )
    {
        //** Find the maximum scoring subsequence.
          int start       = 0;          int end       = 0;
          int tbg       = 0;
          float probScore = 0.0F;          float maxScore  = 0.0F;
          float qualScore = 0.0F;          float maxQualScore = 0.0F;
          int count = 0;
          ScoredElement element = null;
          int[] trimmed_coordinates = new int[2];
          for(; count < scored_elements.size(); ++count )
          {
            element = (ScoredElement) scored_elements.get(count);
            probScore +=  m_min_pbd_val - (float)Math.pow( (double)10.0, (double)( -element.getScore() / 10.0 ) );
            qualScore += (float)element.getScore();
            if( probScore <= 0.0 )
            {
              probScore = 0.0F;
              qualScore = 0.0F;
              tbg = count + 1;
            }
            if( probScore > maxScore )
            {
              start          = tbg;
              end          = count;
              maxScore     = probScore;
              maxQualScore = qualScore;
            }
          }
           /*
          ** Filter out very short sequences and sequences
          ** with low overall quality.
          */
          if( end - start + 1 < MIN_SEQ_LEN ||
              ( maxQualScore / (float)( end - start + 1 ) ) < m_score )
          {
            start = 0;
            end = 0;
          }
         trimmed_coordinates[0] = start;
         trimmed_coordinates[1] = end;
         return trimmed_coordinates;
   }
    
   private static void saveAsFile( String new_file_name, String old_file_name) throws Exception
   {
        File new_file = null; File old_file = null;
        BufferedWriter new_file_writer = null;
        BufferedReader old_file_reader = null;
        String line = null;
        try
        {
            new_file =   new File(new_file_name); old_file = new File(old_file_name); 
            new_file_writer = new BufferedWriter( new FileWriter(new_file) );
            old_file_reader = new BufferedReader(new FileReader( old_file ) );
            while ((line = old_file_reader.readLine()) != null)
            {
                new_file_writer.write( line + LINE_SEPARATOR);
             }
            new_file_writer.flush();new_file_writer.close();
            old_file_reader.close();
        }
        catch(Exception e)
        { 
        }
       
    } 
     
    private static String convertUnixFileNameIntoWindows(String filename)
    {
        //                /c/bio/plate_analysis/clone_samples/3277/4426
        String res = null;
        String directory_structure = null;
        if (filename.charAt(0) == '/') filename = filename.substring(1);
        int drive_separator = filename.indexOf( '/' );
        res = filename.substring(0, drive_separator )+":" + File.separator;
        directory_structure = filename.substring( drive_separator + 1);
        directory_structure = directory_structure.replace('/','\\');
        
        return res + directory_structure ;
    }
    public class ScoredElement 
    {
        private char i_base = '\u0000';
        private int  i_score = -1;
        private String  i_number = null;
        
        public ScoredElement(String element)
        {
            ArrayList temp = splitString(element);
            if (temp == null || temp.size() != 3) return;
            i_base = ((String)temp.get(0)).charAt(0);
            i_score = Integer.parseInt( (String)temp.get(1));
            i_number = (String)temp.get(2);
        }
        
        public          int   getScore(){ return i_score;}
        public          String   getNumber(){ return i_number ; }
        public          char  getChar(){ return i_base;}
        
        public          String  toString(){ return i_base +" "+i_score +" "+i_number + LINE_SEPARATOR; }
        
        private          ArrayList splitString(String value)
        {
            ArrayList res = new ArrayList();
            StringTokenizer st  =  new StringTokenizer(value);
            while(st.hasMoreTokens())
            {
                String val = st.nextToken().trim();
                res.add( val );
            }
            return res;
        }
    }
}
