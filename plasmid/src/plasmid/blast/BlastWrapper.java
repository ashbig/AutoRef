/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.blast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author DZuo
 */
public class BlastWrapper {
    //public static final String BLAST_DB_PATH = "/www/dev.plasmid.med.harvard.edu/docroot/blast/db/";
    //public static final String BLAST_FILE_PATH = "/www/dev.plasmid.med.harvard.edu/docroot/blast/file/";
    public static final String BLAST_PROGRAM_PATH = "D:\\dev\\Test\\blast\\bin\\";
    public static final String BLAST_DB_PATH = "D:\\dev\\Test\\blast\\database\\";
    public static final String BLAST_FILE_PATH = "D:\\dev\\Test\\blast\\files\\";

    public static final String PROGRAM_BLASTN = "blastn";
    public static final String PROGRAM_TBLASTN = "tblastn";
    public static final String PROGRAM_TBLASTX = "tblastx";
    
    public static final String DATABASE_ALL = "plasmid_all";
    
    public static final double DEFAULT_EXPECT = 10.0;
    public static final int DEFAULT_OUTPUT = 9;//tabular summary output
    public static final int PAIRWISE_OUTPUT = 0;
    public static final String BOOLEAN_TRUE = "T";
    public static final String BOOLEAN_FALSE = "F";
    public static final int DEFAULT_MAXSEQ = 10;
    public static final String DEFAULT_SPECIES_FOR_REPEATS = "repeat_9606";
    
    private String program;
    private String database;
    private String sequence;
    private int maxseqs;
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
    
    public BlastWrapper() {
        setProgram(PROGRAM_BLASTN);
        setDatabase(DATABASE_ALL);
        setMaxseqs(DEFAULT_MAXSEQ);
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
    }
    
    public BlastWrapper(String program, String database, String input, String output) {
        setProgram(program);
        setDatabase(database);
        setInput(input);
        setOutput(output);
        setMaxseqs(DEFAULT_MAXSEQ);
        setExpect(DEFAULT_EXPECT);
        setAlignmentview(DEFAULT_OUTPUT);
        setIsLowcomp(BOOLEAN_TRUE);
        setIsMaskLowercase(BOOLEAN_FALSE);
        setIsMegablast(BOOLEAN_FALSE);
        setDbpath(BLAST_DB_PATH);
        setFilepath(BLAST_FILE_PATH);
    }
    
    public String getBlastCmd() {
        String cmd = BLAST_PROGRAM_PATH+"blastall -p "+getProgram()+" -d "+getDatabase()+
                " -e "+getExpect()+" -m "+getAlignmentview()+" -F "+getIsLowcomp()+
                " -U "+getIsMaskLowercase()+" -n "+getIsMegablast()+
                " -b "+getMaxseqs()+" -i "+getInput()+" -o "+getOutput();
   
        return cmd;
    }
    
    public String getBl2seqCmd() {
        String cmd = BLAST_PROGRAM_PATH+"bl2seq -p "+getProgram()+" -i "+getInput()+
                " -j "+getInput2()+" -o "+getBl2seqOutput()+
                " -e "+getExpect()+" -F "+getIsLowcomp()+
                " -U "+getIsMaskLowercase()+" -m "+getIsMegablast();
    
        return cmd;
    }

    public void runBlast() throws Exception {
        String cmd = getBlastCmd();
        executeBlast(cmd);
    }

    public void runBlast2Seq() throws Exception {
        String cmd = getBl2seqCmd();
        executeBlast(cmd);
    }
    
    public void executeBlast(String cmd) throws Exception {
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
                throw new Exception("blast call failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new Exception("User requests stop blasting:");
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

    public int getMaxseqs() {
        return maxseqs;
    }

    public void setMaxseqs(int maxseqs) {
        this.maxseqs = maxseqs;
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
}