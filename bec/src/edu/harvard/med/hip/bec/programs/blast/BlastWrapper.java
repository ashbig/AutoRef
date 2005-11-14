/*
 * BlastWrapper.java
 *
 * Created on March 14, 2003, 11:14 AM
 */

package edu.harvard.med.hip.bec.programs.blast;


import java.io.*;
import java.util.*;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.utility.*;
/**
 *
 * @author  htaycher
 */
public class BlastWrapper
{
    /*public static final String HUMANDB_NAME="Homo sapiens";
    public static final String YEASTDB_NAME="Saccharomyces cerevisiae";
    public static final String PSEUDOMONASDB_NAME="Pseudomonas aeruginosa";
    public static final String MGCDB_NAME="MGC";
    public static final String YPDB_NAME="Yersinia Pastis";
    public static final String FTDB_NAME="Francisella tularensis";
    public static final String ClontechDB_NAME="Clontech";
    public static final String NIDDKDB_NAME="NIDDK";
   
     
     {
        if (ApplicationHostDeclaration.IS_BIGHEAD)
        {
            HUMANDB="d:\\blast_db\\Human\\genes";
            YEASTDB="d:\\blast_db\\Yeast\\genes";
            PSEUDOMONASDB="d:\\blast_db\\Pseudomonas\\genes";
            MGCDB="d:\\blast_db\\MGC\\genes";
            YPDB="d:\\blast_db\\YP\\genes";
            BLAST_PASS =  "D:\\bio_programs\\Blast\\";
            FTDB="d:\\blast_db\\FT\\genes";
            ClontechDB="d:\\blast_db\\Clontech\\genes";
            NIDDKDB="d:\\blast_db\\NIDDKDB\\genes";
        }
            
        else
        {
            BLAST_PASS =  "c:\\blastnew\\";
             HUMANDB="c:\\blast_db\\Human\\genes";
            YEASTDB="c:\\blast_db\\Yeast\\genes";
            PSEUDOMONASDB="c:\\blast_db\\Pseudomonas\\genes";
            MGCDB="c:\\blast_db\\MGC\\genes";
            YPDB="c:\\blast_db\\YP\\genes";
             FTDB="c:\\blast_db\\FT\\genes";
            ClontechDB="c:\\blast_db\\Clontech\\genes";
            NIDDKDB="c:\\blast_db\\NIDDKDB\\genes";
        }
    }
     
     
      public static  String HUMANDB=null;
    public static  String YEASTDB=null;
    public static  String PSEUDOMONASDB=null;
    public static  String MGCDB=null;
    public static  String YPDB=null;
    public static  String FTDB=null;
    public static  String ClontechDB=null;
    public static  String NIDDKDB=null;**/
    
   

    public static  String BLAST_PASS =  edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("BLAST_EXE_COMMON_PATH") + File.separator;
    
    /*
    public   static String getHumanDBLocation()
    {  
        BlastWrapper wr = new BlastWrapper();
        return HUMANDB;
    }
     public   static String getFTDBLocation()
    {  
        BlastWrapper wr = new BlastWrapper();
        return FTDB;
    }
      public   static String getClontechDBLocation()
    {  
        BlastWrapper wr = new BlastWrapper();
        return ClontechDB;
    }
       public   static String getNIDDKDBLocation()
    {  
        BlastWrapper wr = new BlastWrapper();
        return NIDDKDB;
    }
    public   static String getYeastDBLocation()
    {
        BlastWrapper wr = new BlastWrapper();
        return YEASTDB;
    }
    public   static String getPseudomonasDBLocation()
    {
        BlastWrapper wr = new BlastWrapper();
        return PSEUDOMONASDB;
    }
            
    public   static String getMGCDBLocation()
    {
        BlastWrapper wr = new BlastWrapper();
        return MGCDB;
    }
    public   static String getYPDBLocation()
    {
        BlastWrapper wr = new BlastWrapper();
        return YPDB;
    }
        */
     
    // define a set of constants;
    public static final String QUERY_TYPE_DNA = "DNA";
    public static final String QUERY_TYPE_PROTEIN = "PROTEIN";

    public static final String BLAST_PROGRAM_BLASTN = "blastn";
    public static final String BLAST_PROGRAM_BLASTP = "blastp";
    public static final String BLAST_PROGRAM_TBLASTX = "tblastx";
    public static final String BLAST_PROGRAM_BLASTX = "blastx";
    public static final String BLAST_EXEC_BLASTALL = "blastall";
    public static final String BLAST_EXEC_BLAST2SEQ = "bl2seq";
    // identify query sequence type. Default is DNA sequence
    private String type = QUERY_TYPE_DNA;

    // identify which blast program to search, default is blastn

    private String program = "-p " + BLAST_PROGRAM_BLASTN;
    //identify which blast program to use blastall / bl2seq
    private String executable =  BLAST_EXEC_BLASTALL;

    public static final int BLASTALL =  2;
    public static final int BLAST2SEQ =  1;
    
    //return codes from blaster
    public static final int BLAST_SUCESS = 1;
    public static final String BLAST_FAILED = "Blast failed";
    public static final String BLAST_FAILED_NO_DB = "Blast failed: no database specified";
    public static final String BLAST_FAILED_INTERUPTED = "Blast was interapted";
    public static final String BLAST_FAILED_IO = "Blast failed: IO exception";
    
    
    //public static final String BLAST_PASS =  "/kotel/data/blast/";
   
    
    private int m_blast_type = BLASTALL;
    // -p  Program Name String (e.g. blastn, blastp, blastx, etc.)
    private String m_program = "blastn";
    //-d  DatabaseName String (e.g. embl, swissprot....etc.) nr default is not a viable choice on enterprise
    private String m_db = null;
    //-i  Query filename Filename (and path if necessary) stdin
    private String m_inputf = null;
    //-j  for bl2seq only second subject file
    private String m_subjectinputf = null;
    //-e   Expectation value Real number 10.0
    private double m_exsp = 10.0;
    //-m  Alignment view options integer between 0 and 9 0 see table 2 for more detail
    private int m_format = 0;
    //-o  BLAST report Output filename  Filename (and path if necessary) stdout (usually to screen) if you want your results saved into a file, you must specify a filename
    private String m_outputf = null;
    //-F  Filter query sequence T or F (for true or false) T (i.e. filtering is on) DUST is used with blastn, SEG with others
    private String m_filter = "T";
    //-G  Cost to open a gap integer 0
    private int m_opengap = 0;
    //-E  Cost to extend a gap integer 0
    private int m_extgap = 0;
    //-X  X dropoff value for gapped alignment (in bits) integer 0
    private int m_dropgap = 0;
    //-I  show GIs in deflines T or F (for true or false) F GI = NI in EMBL. Tracks versions of an entry.
    private String m_gi="F";
    //-q  Penalty for a nucleotide mismatch integer -3 for use with blastn only
    private int m_nmismatch = -3;
    //-r   Reward for a nucleotide match integer 1 for use with blastn only
    private int m_nmatch = 1;
    //-v  Number of one line descriptions integer 500
    private int m_descline = 500;
    //-f  Threshold for extending hits integer 0
    private int m_hitext = 0;
    //-b   Number of alignments to show  integer 250
    private int m_hitnumber = 250;
    //-g  Perfom gapped alignment  T or F (for True or False) T not available with tblastx
    private String m_gapedal = "T";
    //-Q  Query Genetic code to use integer 1 (Universal) for blastx and tblast[nx] only

    //-D  DB Genetic code integer 1 (Universal) tblastn and tblastx only

    //-a  number of processors to use integer 1

    //-O  Seq. Align File Filename (and path if necessary) optional
    //- no default

    //-J  Believe the query defline  T or F (for True or False) F

    //-M  Matrix matrix name (and path if necessary) BLOSUM62 only certain combination of gap penalties and matrices are supported*
    private String m_matrix = "BLOSUM62";
    //-W  Word size integer 0 default values are used if 0 is chosen. Defaults are 11 for nucs and 3 for proteins**
    private int m_wordsize = 0;
    //-z  Effective length of the database integer 0 0 means use the real size of the database ( important for the statistics
    //-leave as default)

    //-K  Number of best hits from a region to keep integer 100
    private int m_numbesthits = 100;
    //-P  0 for multiple hits 1-pass, 1 for single hit 1-pass, 2 for 2-pass integer 0

    //-Y  Effective length of the search space real 0.0

    //-L  Location on query sequence string

    //-S  Query strands to search against database 1(top strand),2(bottom strand) or 3(both) 3 for blast[nx], and tblastx

    //-T  Produce HTML output  T or F (for True or False) F see table 2 for more detail
    private String m_htmlout = "F";
    //-l  Restrict search of database to list of GIs string

    //-U  use lower case filtering of FASTA sequence T or F (for True or False) F

    //-y  Dropoff (X) for blast extensions in bits  real 0.0

    //-Z  X dropoff value for final gapped alignment (in bits) real 0.0

    //-R  PSI
    //-TBLASTN checkpoint file File In

    //-n  Megablast search T or F (for True or False) F

    //-A  Multiple Hits window size  integer 40 Zero for single hit algorithm

    /** Creates a new instance of BlastWrapper */
    public BlastWrapper()
    {
    }


    /** This method provided to delete a blast output file */
    public boolean delete()
    {
        File out = new File(m_outputf);
        return out.delete();
    }


    /** This version of blast takes a list of query sequence files
     *  and blast every one of them. Outputs are saved as file with ".blast"
     *  extension. No file existing check. Thus it's client's responsibility
     *  of making sure the query sequence files do exist in the specified location.
     *
     *  @param queryList, a string array of query sequence file names
     */
    public void run(String[] queryList) throws BecUtilException
    {
        for (int i=0; i<queryList.length; i++)
        {
            m_inputf = queryList[i];
            run();
        }
    }


   


    /** This version of blast takes both query sequence file name and
     *  specified output file name of a blast search. No file existing check. Make
     *  sure query sequence file existing and output can be written to the specified
     *  location.
     *
     *  @param querySeqFname, query sequence file name in fasta format
     *  @param outputFname, blast output file name
     *
     *return codes :
        -2 - no db specified
     */
    public void run() throws BecUtilException
    {
        String blastcmd = null;
        if (m_blast_type == BLASTALL  && m_db == null )
        {
        
            throw new BecUtilException( BLAST_FAILED_NO_DB);
        }
        blastcmd = makeBlastCmd();
        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(blastcmd);

            BufferedInputStream bin = new BufferedInputStream(p.getErrorStream());
            int x;
            while ((x = bin.read()) != -1)
            {
                //System.out.write(x);
            }
            p.waitFor();
            if (p.exitValue() != 0)
            {

                throw new BecUtilException( BLAST_FAILED);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new BecUtilException(BLAST_FAILED_IO);
        } catch (InterruptedException e)
        {
            throw new BecUtilException(BLAST_FAILED_INTERUPTED);
        }
       // return BLAST_SUCESS;
    }




  		public String getProgramName (){ return m_program  ;}
  	    //-d  DatabaseName String (){e.g. embl, swissprot....etc.) nr default is not a viable choice on enterprise
  	    public String getDB (){ return m_db  ;}
  	    //-i  Query filename Filename (){and path if necessary) stdin
  	    public String getInputFN (){ return m_inputf  ;}
            public String getOutpitFN (){ return m_outputf  ;}
            //-j for bl2seq subject input file
            public String getSubjectInputFN (){ return m_subjectinputf;}
  	    //-e   Expectation value Real number 10.0
  	    public double getExpect (){ return m_exsp  ;}
  	    //-m  Alignment view options integer between 0 and 9 0 see table 2 for more detail
  	    public int getFormat (){ return m_format  ;}
  	    //-o  BLAST report Output filename  Filename (){and path if necessary) stdout (){usually to screen) if you want your results saved public int geto a file, you must specify a filename
  	    public String getOutputFN (){ return m_outputf  ;}
  	    //-F  Filter query sequence T or F (){for true or false) T (){i.e. filtering is on) DUST is used with blastn, SEG with others
  	    public String getFilter (){ return m_filter  ;}
  	    //-G  Cost to open a gap public int geteger 0
  	    public int getOpenGap (){ return m_opengap  ;}
  	    //-E  Cost to extend a gap public int geteger 0
  	    public int getExtGap (){ return m_extgap  ;}
  	    //-X  X dropoff value for gapped alignment (){in bits) public int geteger 0
  	    public int getDropOff (){ return m_dropgap  ;}
  	    //-I  show GIs in deflines T or F (){for true or false) F GI  ;} NI in EMBL. Tracks versions of an entry.
  	    public String get (){ return m_gi ;}
  	    //-q  Penalty for a nucleotide mismatch public int geteger -3 for use with blastn only
  	    public int getMismatchPen (){ return m_nmismatch  ;}
  	    //-r   Reward for a nucleotide match public int geteger 1 for use with blastn only
  	    public int getMatchP (){ return m_nmatch  ;}
  	    //-v  Number of one line descriptions public int geteger 500
  	    public int getDescriptionLines (){ return m_descline  ;}
  	    //-f  Threshold for extending hits public int geteger 0
  	    public int getThreshHits (){ return m_hitext  ;}
  	    //-b   Number of alignments to show  public int geteger 250
  	    public int getHitNumber (){ return m_hitnumber  ;}
  	    //-g  Perfom gapped alignment  T or F (){for True or False) T not available with tblastx
  	    public String getGappedAl (){ return m_gapedal  ;}
  	    public String getMatrix (){ return m_matrix  ;}
  	    //-W  Word size public int geteger 0 default values are used if 0 is chosen. Defaults are 11 for nucs and 3 for proteins**
  	    public int getWordSize (){ return m_wordsize  ;}
  	    public int getBestHits (){ return m_numbesthits  ;}
  	    public String getHTMLformat (){ return m_htmlout  ;}


	 public void setProgramName (String v){  m_program  = v;}
		    //-d  DatabaseName String ( v){e.g. embl, swissprot....etc.) nr default is not a viable choice on enterprise
		    public void setDB (String v){  m_db  = v;}
		    //-i  Query filename Filename ( v){and path if necessary) stdin
		    public void setInputFN (String v){  m_inputf  = v;}
                
                  //-j subject input file  
                   public void setSubjectInputFN (String v){   m_subjectinputf = v;}
		    //-e   Expectation value Real number 10.0
		    public void setExpect ( double v){  m_exsp  = v;}
		    //-m  Alignment view options integer between 0 and 9 0 see table 2 for more detail
		    public void setFormat (int v){  m_format  = v;}
		    //-o  BLAST report Output filename  Filename (){and path if necessary) stdout (){usually to screen) if you want your results saved public int geto a file, you must specify a filename
		    public void setOutputFN (String v){  m_outputf  = v;}
		    //-F  Filter query sequence T or F (){for true or false) T (){i.e. filtering is on) DUST is used with blastn, SEG with others
		    public void setFilter (String v){  m_filter  = v;}
		    //-G  Cost to open a gap public int geteger 0
		    public void setOpenGap (int v){  m_opengap  = v;}
		    //-E  Cost to extend a gap public int geteger 0
		    public void setExtGap (int v){  m_extgap  = v;}
		    //-X  X dropoff value for gapped alignment (){in bits) public int geteger 0
		    public void setDropOff (int v){  m_dropgap  = v;}
		    //-I  show GIs in deflines T or F (){for true or false) F GI  = v;} NI in EMBL. Tracks versions of an entry.
		    public void setGI (String v){  m_gi = v;}
		    //-q  Penalty for a nucleotide mismatch public int geteger -3 for use with blastn only
		    public void setMismatchPen (int v){  m_nmismatch  = v;}
		    //-r   Reward for a nucleotide match public int geteger 1 for use with blastn only
		    public void setMatchP (int v){  m_nmatch  = v;}
		    //-v  Number of one line descriptions public int geteger 500
		    public void setDescriptionLines (int v){  m_descline  = v;}
		    //-f  Threshold for extending hits public int geteger 0
		    public void setThreshHits (int v){  m_hitext  = v;}
		    //-b   Number of alignments to show  public int geteger 250
		    public void setHitNumber (int v){  m_hitnumber  = v;}
		    //-g  Perfom gapped alignment  T or F (){for True or False) T not available with tblastx
		    public void setGappedAl (String v){  m_gapedal  = v;}
		    public void setMatrix (String v){  m_matrix  = v;}
		    //-W  Word size public int geteger 0 default values are used if 0 is chosen. Defaults are 11 for nucs and 3 for proteins**
		    public void setWordSize (int v){  m_wordsize  = v;}
		    public void setBestHits (int v){  m_numbesthits  = v;}
	    public void setHTMLformat (String v){  m_htmlout  = v;}
            public void setBlastType(int v){ m_blast_type = v;}

//*****************************************************************
    private String makeBlastCmd()
    {
        String blastcmd = "";
        blastcmd =   " -p "   + m_program +  " -i "   + m_inputf + " -e "    + m_exsp 
 + " -o "   + m_outputf + " -F "  + m_filter + " -G "   + m_opengap + " -E "   + m_extgap 
 + " -X "  + m_dropgap  + " -q " + m_nmismatch + " -r "  + m_nmatch  
  + " -g "   + m_gapedal + " -M "  + m_matrix + " -W "   + m_wordsize 
  + " -T "   + m_htmlout ;
      //bl2seq -p blastn -e 10e-50 -i plt1_A01 -j plt1_A01.seq -o plt1_A01.n

       
        if (m_blast_type == BLASTALL)
             blastcmd = BLAST_PASS + "blastall" + " -d "  + m_db + blastcmd + " -K "   + m_numbesthits
             + " -f "   + m_hitext + " -b "  + m_hitnumber+ " -v "  + m_descline+ " -m "   + m_format
             + " -I "  + m_gi;
        else if (m_blast_type == BLAST2SEQ )
            blastcmd = BLAST_PASS + "bl2seq "  + " -j " + m_subjectinputf +" "+blastcmd;
         return blastcmd;
    }


  public static void main(String [] args)
    {
        try
        {
            
        }
        catch(Exception e){}
  }

}
