/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blast;

import config.ApplicationProperties;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author dongmei
 */
public class BlastWrapper {
    public static final String BLAST_PROGRAM_PATH = ApplicationProperties.getInstance().getProperties().getProperty("blastprogrampath");
    public static final String BLAST_DB_PATH = ApplicationProperties.getInstance().getProperties().getProperty("blastdbpath");
    public static final String BLAST_FILE_PATH = ApplicationProperties.getInstance().getProperties().getProperty("blastfilepath");

    //public static final String BLAST_PROGRAM_PATH = "C:\\NCBI\\blast-2.2.28+\\bin\\";
    //public static final String BLAST_FILE_PATH = "D:\\test\\";
    
    public static final String PROGRAM_BLASTN = "blastn";
    public static final String PROGRAM_TBLASTN = "tblastn";
    public static final String PROGRAM_TBLASTX = "tblastx";
    public static final String PROGRAM_DISPLAY_BLASTN = "BLASTN - nt query to nt db";
    public static final String PROGRAM_DISPLAY_TBLASTX = "TBLASTX - transl. (6 frames) nt query to transl (6) nt db";
    public static final String PROGRAM_DISPLAY_TBLASTN = "TBLASTN - protein query to translated (6 frames) nt db";
 
    public static final String DATABASE_ALL = "plasmid_all";
    
    public static final double DEFAULT_EXPECT = 10.0;
    public static final int DEFAULT_OUTPUT = 9;//tabular summary output
    public static final int PAIRWISE_OUTPUT = 0;
    public static final String BOOLEAN_TRUE = "T";
    public static final String BOOLEAN_FALSE = "F";
    public static final int DEFAULT_MAXSEQ = 10;
    public static final String DEFAULT_SPECIES_FOR_REPEATS = "repeat_9606";
    public static final int BLASTX_TABULAR_FORMAT = 6;
    public static final int WORDSIZE = 11;
    public static final int MAX_TARGET_SEQ = 100;
    
    private String program;
    private String database;
    private String sequence;
    //private int maxseqs;
    private double expect;
    private String isLowcomp;
  //  private String isSpeciesForRepeats;
  //  private String speciesForRepeats;
  //  private String isMaskLookup;
    private String isMaskLowercase;
    private String isMegablast;
    private int alignmentview;
    private String input;
    private String output;
    private String input2;
    private String bl2seqOutput;
    private String dbpath;
    private String filepath;
    private String lowcomp;
    private int wordsize;
    private int maxTargetSeq;
    
    public BlastWrapper() {
        setProgram(PROGRAM_BLASTN);
        setDatabase(DATABASE_ALL);
        //setMaxseqs(DEFAULT_MAXSEQ);
        setExpect(DEFAULT_EXPECT);
        setAlignmentview(DEFAULT_OUTPUT);
        setIsLowcomp(BOOLEAN_TRUE);
        //setIsSpeciesForRepeats(BOOLEAN_TRUE);
        //setSpeciesForRepeats(DEFAULT_SPECIES_FOR_REPEATS);
        //setIsMaskLookup(BOOLEAN_FALSE);
        setIsMaskLowercase(BOOLEAN_FALSE);
        setIsMegablast(BOOLEAN_FALSE);
        setDbpath(BLAST_DB_PATH);
        setFilepath(BLAST_FILE_PATH);
        lowcomp = "yes";
        wordsize = WORDSIZE;
        maxTargetSeq = MAX_TARGET_SEQ;
    }
    
    public BlastWrapper(String program, String database, String input, String output) {
        setProgram(program);
        setDatabase(database);
        setInput(input);
        setOutput(output);
        //setMaxseqs(DEFAULT_MAXSEQ);
        setExpect(DEFAULT_EXPECT);
        setAlignmentview(DEFAULT_OUTPUT);
        setIsLowcomp(BOOLEAN_TRUE);
        setIsMaskLowercase(BOOLEAN_FALSE);
        setIsMegablast(BOOLEAN_FALSE);
        setDbpath(BLAST_DB_PATH);
        setFilepath(BLAST_FILE_PATH);
        lowcomp = "yes";
        wordsize = WORDSIZE;
        maxTargetSeq = MAX_TARGET_SEQ;
    }
    
    public String getBl2seqCmd(String programPath) {
        return getBl2seqCmd(programPath, BLASTX_TABULAR_FORMAT);
    }
    
    public String getBl2seqCmd(String programPath, int outputFormat) {
        String cmd = programPath+"blastn -query "+getInput()+
                " -subject "+getInput2()+" -out "+getBl2seqOutput()+ 
                " -dust "+getLowcomp()+
                //" -word_size "+getWordsize()+
                //" -evalue "+getExpect()+
                " -outfmt "+outputFormat;
        
        return cmd;
    }
    
    public String getBlastx2SeqCmd(String programPath) {
        return getBlastx2SeqCmd(programPath, BLASTX_TABULAR_FORMAT);
    }
    
    public String getBlastx2SeqCmd(String programPath, int outputFormat) {
        String cmd = programPath+"blastx -query "+getInput()+
                " -subject "+getInput2()+" -out "+getBl2seqOutput()+ 
                " -seg "+getLowcomp()+
                " -outfmt "+outputFormat;
        
        return cmd;
    }
    
    public String getBlastxCmd(String programPath) {
        String cmd = programPath+"blastx -query "+getInput()+
                " -db "+getDatabase()+" -out "+getOutput()+
                " -seg "+getLowcomp()+
                " -outfmt "+BLASTX_TABULAR_FORMAT+" -max_target_seqs "+getMaxTargetSeq();
        
        return cmd;
    }
    
    public String getBlastnCmd(String programPath) {
        String cmd = programPath+"blastn -query "+getInput()+
                " -db "+getDatabase()+" -out "+getOutput()+
                " -dust "+getLowcomp()+
                " -outfmt "+BLASTX_TABULAR_FORMAT+" -max_target_seqs "+getMaxTargetSeq();
        
        return cmd;
    }

    public void runBlast2Seq(String programPath) throws BlastException {
        String cmd = getBl2seqCmd(programPath);
        executeBlast(cmd);
    }
    
    public void executeBlast(String cmd) throws BlastException {
        try {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(cmd);
            
            BufferedInputStream bin = new BufferedInputStream(p.getErrorStream());
            int x;
            while ((x = bin.read()) != -1) {
                System.out.println(x);
            }
            p.waitFor();
            if (p.exitValue() != 0) {
                throw new BlastException("blast call failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new BlastException("User requests stop blasting:");
        }
    }    
    
    public boolean delete(String file) {
        File out = new File(file);
        return out.delete();
    }
    
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public double getExpect() {
        return expect;
    }

    public void setExpect(double expect) {
        this.expect = expect;
    }

    public int getAlignmentview() {
        return alignmentview;
    }

    public void setAlignmentview(int alignmentview) {
        this.alignmentview = alignmentview;
    }

    public String getIsMegablast() {
        return isMegablast;
    }

    public void setIsMegablast(String isMegablast) {
        this.isMegablast = isMegablast;
    }

    public String getIsLowcomp() {
        return isLowcomp;
    }

    public void setIsLowcomp(String isLowcomp) {
        this.isLowcomp = isLowcomp;
    }

    public String getIsMaskLowercase() {
        return isMaskLowercase;
    }

    public void setIsMaskLowercase(String isMaskLowercase) {
        this.isMaskLowercase = isMaskLowercase;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getInput2() {
        return input2;
    }

    public void setInput2(String input2) {
        this.input2 = input2;
    }

    public String getBl2seqOutput() {
        return bl2seqOutput;
    }

    public void setBl2seqOutput(String bl2seqOutput) {
        this.bl2seqOutput = bl2seqOutput;
    }

    public String getDbpath() {
        return dbpath;
    }

    public void setDbpath(String dbpath) {
        this.dbpath = dbpath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * @return the lowcomp
     */
    public String getLowcomp() {
        return lowcomp;
    }

    /**
     * @param lowcomp the lowcomp to set
     */
    public void setLowcomp(String lowcomp) {
        this.lowcomp = lowcomp;
    }

    /**
     * @return the wordsize
     */
    public int getWordsize() {
        return wordsize;
    }

    /**
     * @param wordsize the wordsize to set
     */
    public void setWordsize(int wordsize) {
        this.wordsize = wordsize;
    }

    /**
     * @return the maxTargetSeq
     */
    public int getMaxTargetSeq() {
        return maxTargetSeq;
    }

    /**
     * @param maxTargetSeq the maxTargetSeq to set
     */
    public void setMaxTargetSeq(int maxTargetSeq) {
        this.maxTargetSeq = maxTargetSeq;
    }
}
