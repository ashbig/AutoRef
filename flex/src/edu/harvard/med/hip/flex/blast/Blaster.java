/*
 * @(#)Blaster.java	1.00 04/30/01
 *
 * Copyright 2001-2001 Harvard Institute of Proteomics. All Rights Reserved.
 * 
 * Author Tao Wei
 *
 * $Id $
 */
//package org.harvard.hip.flex;

package edu.harvard.med.hip.flex.blast;

import java.io.*;
import java.util.*;
/**
 * This class takes responsibility of calling blast program. The blast results in
 * a specific output format is delievered to a BlastParser instance to extract
 * important information such as e-value, score and alignments. The information 
 * is represented by a set of <code>HomologItem</code> and <code>Alignment</code>
 * objects. Client program accesses these objects from the parser object reference.
 *
 * To use the class, 
 * 1. Instantiate a Blaster object with information of which blast program to use, 
 * which blastable database to search. 
 * 2. Set necessary optional blast parameters. 
 * 3. Call one of blast methods with a query sequence file name(s), output file
 * name is optional and its default is query.blast
 *
 * Note: this is only a wrapper for either blastn or blastp.
 *
 * @author  Tao Wei
 * @version 1.00
 */
public class Blaster extends java.lang.Object {
    // define a set of constants;
    public static final String QUERY_TYPE_DNA = "DNA";
    public static final String QUERY_TYPE_PROTEIN = "PROTEIN";
    public static final boolean BLAST_SUCESS = true;
    public static final boolean BLAST_FAILED = false; 
    public static final String BLAST_PROGRAM_BLASTN = "blastn";
    public static final String BLAST_PROGRAM_BLASTP = "blastp";    
    
    /** identify query sequence type. Default is DNA sequence
     */
    private String type = QUERY_TYPE_DNA;
    
    /** identify which blast program to search, default is blastn 
     */
    private String program = "-p" + BLAST_PROGRAM_BLASTN;
    //private String program = BLAST_PROGRAM_BLASTN;
    
    /** idenity the blastable database on the file system 
     */
    private String dbPath = null;
    
    /** set significant threshold. Default is 10.0 
     */
    private String expect = "-e10.0";
    //private String expect = "10.0";
    
    /** switch of sequence fileter on (T) or off (F). Default is T 
     */
    private String filter = "-FT";
    /** the number of homologs in output. Default is 5.
     */
    private String hits = "-v5 -b5";
    /** similarity matrix for scoring. Default is blosum62
     */
    private String matrix = "-MBLOSUM62";
    /** Penalty of open a gap. Zero invokes default.
     */
    private String gapOpen = "-G0";
    
    /** Penalty of extending existing gaps. Zero invokes default.
     */
    private String gapExtend = "-E0";
    
    /** Penalty for a nucleotide mismatch (blastn only) [Integer]
     *  Default is -3
     */
    private String mismatch = "-q-3";
    
    /** Reward for a nucleotide match (blastn only) [Integer]
     *  Default is 1
     */
    private String match = "-r1";
    
    /** Output format. Default is 0, pairwise local alignment
     */
    private String format = "-m0";
    
    /** output file name. 
     */
    private String outfile = null;
    
    /** Create a new Blaster instance with no parameter set up.
     *  To actually use the object to execute a blast search you need to
     *  call setters to set up parameters needed for a search such as
     *  set a blast program, set blastable database etc. 
     */
    public Blaster() {
    }
    
    /**
     * Creates new Blaster instance with minimum info. 
     * @param program, identify blast program to run
     * @param dbPath, identify blastable database
     */
    public Blaster(String program, String dbPath) {
        this.program = "-p" + program;
        this.dbPath = "-d" + dbPath;
        //this.program = program;
        //this.dbPath = dbPath;
    }
    
    /** This version of blast takes a list of query sequence files
     *  and blast every one of them. Outputs are saved as file with ".blast"
     *  extension. No file existing check. Thus it's client's responsibility
     *  of making sure the query sequence files do exist in the specified location.
     *
     *  @param queryList, a string array of query sequence file names
     */ 
    public void blast(String[] queryList) {
        for (int i=0; i<queryList.length; i++) {
            blast(queryList[i]);
        }
    }
   
    /** This version blast just take query sequence file and generate
     *  blast output in queryfile.blast. Again no file existing check.
     *
     *  @param query, query sequence file name
     */
    public boolean blast(String query) {
        boolean status;
        outfile = query + ".blast";
        status = blast(query, outfile);
        return status;
    }
    
    /** This version of blast takes both query sequence file name and 
     *  specified output file name of a blast search. No file existing check. Make
     *  sure query sequence file existing and output can be written to the specified
     *  location.
     * 
     *  @param querySeqFname, query sequence file name in fasta format 
     *  @param outputFname, blast output file name
     */
    public boolean blast(String query, String output) {
        String blastcmd = null;
        outfile = output;
        
        if (dbPath == null) {
            throw new BlastError("Error: no blast database specified"); 
        } else if (!isProgramMatchType()) {
            throw new BlastError("Error: sequence type and blast program do not match.");
        } else { 
            blastcmd = makeBlastCmd(query, outfile);
        }
        try {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(blastcmd);
            
            BufferedInputStream bin = new BufferedInputStream(p.getErrorStream());
            int x;
            while ((x = bin.read()) != -1) {
                System.out.write(x);
            }
            p.waitFor();
            if (p.exitValue() != 0) {
                System.out.println("blast call failed");
                return Blaster.BLAST_FAILED;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("User requests stop blasting:");
        }
        return Blaster.BLAST_SUCESS;
    }

    private String makeBlastCmd(String query, String output) {
      
         String blastcmd = "/kotel/data/blast/blastall " +                  
                          program + " " +                
                          "-d" + dbPath +  " " +                
                          "-i" + query + " "  +         
                          "-o" + output + " "  +         
                          expect + " " +                
                          gapOpen + " " +                
                          gapExtend + " " +              
                          filter + " " +                 
                          hits;                    

/*     
         String blastcmd = "E:\\flexDev\\blast\\blastall " +                  
                          program + " " +                
                          "-d " + dbPath +  " " +                
                          "-i " + query + " "  +         
                          "-o " + output + " "  +         
                          expect + " " +                
                          gapOpen + " " +                
                          gapExtend + " " +              
                          filter + " " +                 
                          hits; 
 */
         return blastcmd;
    }
    
    private boolean isProgramMatchType() {
        if (type.equalsIgnoreCase(Blaster.QUERY_TYPE_DNA) &&
            (!program.equalsIgnoreCase("-p" + Blaster.BLAST_PROGRAM_BLASTN)) ||
            type.equalsIgnoreCase(Blaster.QUERY_TYPE_PROTEIN) &&
            (!program.equalsIgnoreCase("-p" + Blaster.BLAST_PROGRAM_BLASTP)))
            return false;
        else 
            return true;      
    }
    
    /** This method provided to delete a blast output file */
    public boolean delete() {
        File out = new File(outfile);
        return out.delete();
    }
    
    /** return blast output file name */
    public String getOutFile() { return outfile; }
    
    /** set blastable databases. If multiple databases used they shall be
     *  spearated by a space in a single string.
     */
    public void setDBPath(String dbPaths) { 
        //this.dbPath = "\"" + dbPaths + "\" ";
        this.dbPath = dbPaths;
        //System.out.println("db: " + this.dbPath);
    }
    
    /** set blast program to use according different type of sequence. 
     */
    public void setProgram(String program) { this.program = "-p " + program; }
    
    /** set E value threshold 
     */
    public void setExpect(double e) { this.expect = "-e " + e; }
    
    /** set filter on or off 
     */
    public void setFilter(String filter) { this.filter = "-F " + filter; }

    /** set the number of hits to report
     */
    public void setHits(int hits) {
        this.hits = "-v" + hits + " -b" + hits;
    }
    
    /** set similarity matrix for scoring for protein blast
     */
    public void setMatrix(String matrixName) {
        this.matrix = matrixName;
    }
    
    /** set gap open penalties */
    public void setGapOpen(int open) {
        this.gapOpen = "-G " + open;
    }
    
    /** set gap extending penalties */
    public void setGapExtend(int ext) {
        this.gapExtend = "-E " + ext;
    }
    
    /** set nucleotide match score */
    public void setMatch(int reward) {
        this.match = "-r " + reward;
    }
    
    /** set nucleotide mismatch penalties */
    public void setMismatch(int mis) {
        this.mismatch = "-q " + mis;
    }
    
    /** set sequence type */
    public void setType(String type) {
        if (type.equalsIgnoreCase("DNA"))
            this.type = Blaster.QUERY_TYPE_DNA;
        else if (type.equalsIgnoreCase("protein"))
            this.type = Blaster.QUERY_TYPE_PROTEIN;
        else 
            throw new BlastError("Unknown sequence type. Use either DNA or protein");
    }
    
    class BlastError extends Error {
        public BlastError() {
            super("There is an error in blast calling.");
        }
    
        public BlastError(String error) {
            super(error);
        }
    }
    
    // testing Blaster and BlastParser
    public static void main (String args[]) {        
        Blaster blaster = new Blaster();
        blaster.setHits(5);
        blaster.setDBPath("/usr/local/jakarta-tomcat-3.2.1/webapps/dzuo/WEB-INF/classes/blastdb/genes");
        //blaster.setType("dna");
        for (int i=0; i<args.length; i++) {
            //System.out.println("File name: " + args[i]);
            blaster.blast(args[i]);
            BlastParser parser = new BlastParser(args[i] + ".blast");
            try {
                parser.parseBlast();
                parser.displayParsed();
            } catch (ParseException e) {}
            //delete blast output file if you do not want it anymore                        
            if (blaster.delete())
                System.out.println(blaster.getOutFile() + " deleted");
            else
                System.out.println(blaster.getOutFile() + " couldn't be deleted");
            
        }
    }
}
