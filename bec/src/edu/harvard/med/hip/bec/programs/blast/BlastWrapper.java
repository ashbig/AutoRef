/*
 * BlastWrapper.java
 *
 * Created on March 14, 2003, 11:14 AM
 */

package edu.harvard.med.hip.bec.programs.blast;


import java.io.*;
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class BlastWrapper
{
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

    private int BLASTALL =  2;
    private int BLAST2SEQ =  1;
    
    //return codes from blaster
      public static final int BLAST_SUCESS = 1;
    public static final int BLAST_FAILED = 0;
    public static final int BLAST_FAILED_NO_DB = -1;
    public static final int BLAST_FAILED_INTERUPTED = -3;
    public static final int BLAST_FAILED_IO = -4;
    
    
    public static final String BLAST_PASS =  "c:\\Program Files\\blast\\";
    //public static final String BLAST_PASS =  "/kotel/data/blast/";
   
    
    private int m_blast_type = BLASTALL;
    // -p  Program Name String (e.g. blastn, blastp, blastx, etc.)
    private String m_program = "blastn";
    //-d  DatabaseName String (e.g. embl, swissprot....etc.) nr default is not a viable choice on enterprise
    private String m_db = null;
    //-i  Query filename Filename (and path if necessary) stdin
    private String m_inputf = null;
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
    public void blast(String[] queryList)
    {
        for (int i=0; i<queryList.length; i++)
        {
            blast(queryList[i]);
        }
    }


    /** This version blast just take query sequence file and generate
     *  blast output in queryfile.blast. Again no file existing check.
     *
     *  @param query, query sequence file name
     */
    public int blast(String query)
    {

        m_outputf = query + ".blast";
       return blast(query, m_outputf);

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
    public int blast(String query, String output)
    {
        String blastcmd = null;
        m_outputf = output;

        if (m_db == null)
        {
            return BLAST_FAILED_NO_DB;
        }
        blastcmd = makeBlastCmd(query, m_outputf);

        try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(blastcmd);

            BufferedInputStream bin = new BufferedInputStream(p.getErrorStream());
            int x;
            while ((x = bin.read()) != -1)
            {
                System.out.write(x);
            }
            p.waitFor();
            if (p.exitValue() != 0)
            {

                return BLAST_FAILED;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
            return BLAST_FAILED_IO;
        } catch (InterruptedException e)
        {
            return BLAST_FAILED_INTERUPTED;
        }
        return BLAST_SUCESS;
    }




  		public String getProgramName (){ return m_program  ;}
  	    //-d  DatabaseName String (){e.g. embl, swissprot....etc.) nr default is not a viable choice on enterprise
  	    public String getDB (){ return m_db  ;}
  	    //-i  Query filename Filename (){and path if necessary) stdin
  	    public String getInputFN (){ return m_inputf  ;}
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


//*****************************************************************
    private String makeBlastCmd(String query, String output)
    {
        String blastcmd = null;
        blastcmd =   "-p"   + m_program + "-d"  + m_db + "-i"   + m_inputf + "-e"    + m_exsp + "-m"   + m_format 
 + "-o"   + m_outputf + "-F"  + m_filter + "-G"   + m_opengap + "-E"   + m_extgap 
 + "-X"  + m_dropgap + "-I"  + m_gi + "-q" + m_nmismatch + "-r"  + m_nmatch + "-v"  + m_descline 
 + "-f"   + m_hitext + "-b"  + m_hitnumber + "-g"   + m_gapedal + "-M"  + m_matrix + "-W"   + m_wordsize 
 + "-K"   + m_numbesthits + "-T"   + m_htmlout ;
      //bl2seq -p blastn -e 10e-50 -i plt1_A01 -j plt1_A01.seq -o plt1_A01.n

       
        if (m_blast_type == BLASTALL)
             blastcmd = BLAST_PASS + "blastall " + blastcmd;
        else if (m_blast_type == BLAST2SEQ )
            blastcmd = BLAST_PASS + "bl2seq " + blastcmd;
        System.out.println(blastcmd);
        return blastcmd;
    }




}
