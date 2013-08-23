/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.blast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import plasmid.util.FlexProperties;

/**
 *
 * @author DZuo
 */
public class BlastWrapper {
    public static final String BLAST_PROGRAM_PATH = FlexProperties.getInstance().getProperty("blastprogrampath");
    public static final String BLAST_DB_PATH = FlexProperties.getInstance().getProperty("blastdbpath");
    public static final String BLAST_FILE_PATH = FlexProperties.getInstance().getProperty("blastfilepath");

    public static final String PROGRAM_BLASTN = "blastn";
    public static final String PROGRAM_TBLASTN = "tblastn";
    public static final String PROGRAM_TBLASTX = "tblastx";
    public static final String PROGRAM_BLASTX = "blastx";
    public static final String PROGRAM_DISPLAY_BLASTN = "BLASTN - nt query to nt db";
    public static final String PROGRAM_DISPLAY_TBLASTX = "TBLASTX - transl. (6 frames) nt query to transl (6) nt db";
    public static final String PROGRAM_DISPLAY_TBLASTN = "TBLASTN - protein query to translated (6 frames) nt db";
 
    public static final String DATABASE_ALL = "plasmid_all";
    
    public static final int DEFAULT_OUTPUT = 6;//tabular summary output
    public static final int PAIRWISE_OUTPUT = 0;
    public static final String MASK_LOW_COMP_YES = "yes";
    public static final String MASK_LOW_COMP_NO = "no";
    public static final String DEFAULT_MASK_LOW_COMP = MASK_LOW_COMP_YES;
    public static final int DEFAULT_MAXSEQ = 500;
    public static final String DEFAULT_SPECIES_FOR_REPEATS = "repeat_9606";
    
    private String program;
    private String database;
    private String sequence;
    private String isLowcomp;
    private int alignmentview;
    private String input;
    private String output;
    private String input2;
    private String dbpath;
    private String filepath;
    private int maxseqs;
    
    public BlastWrapper() {
        setProgram(PROGRAM_BLASTN);
        setDatabase(DATABASE_ALL);
        setAlignmentview(DEFAULT_OUTPUT);
        setIsLowcomp(DEFAULT_MASK_LOW_COMP);
        setDbpath(BLAST_DB_PATH);
        setFilepath(BLAST_FILE_PATH);
        setMaxseqs(DEFAULT_MAXSEQ);
    }
    
    public BlastWrapper(String program, String database, String input, String output) {
        setProgram(program);
        setDatabase(database);
        setInput(input);
        setOutput(output);
        setAlignmentview(DEFAULT_OUTPUT);
        setIsLowcomp(DEFAULT_MASK_LOW_COMP);
        setDbpath(BLAST_DB_PATH);
        setFilepath(BLAST_FILE_PATH);
        setMaxseqs(DEFAULT_MAXSEQ);
    }
    
    public String getBlastCmd() {
        String cmd = BLAST_PROGRAM_PATH+getProgram()+" -db "+getDatabase()+
                " -outfmt "+getAlignmentview()+" -max_target_seqs "+getMaxseqs()+
                " -query "+getInput()+" -out "+getOutput();
   
        if(getProgram().equals(BlastWrapper.PROGRAM_BLASTN)) {
            cmd += " -dust "+getIsLowcomp();
        } else {
            cmd += " -seg "+getIsLowcomp();
        }
        return cmd;
    }
    
    //-D 0-alignment view; 1-tabular format
    public String getBl2seqCmd() {
        String cmd = BLAST_PROGRAM_PATH+getProgram()+
                " -outfmt "+getAlignmentview()+" -max_target_seqs "+getMaxseqs()+
                " -query "+getInput()+" -out "+getOutput()+" -subject "+getInput2();
   
        if(getProgram().equals(BlastWrapper.PROGRAM_BLASTN)) {
            cmd += " -dust "+getIsLowcomp();
        } else {
            cmd += " -seg "+getIsLowcomp();
        }
        return cmd;
    }
    
    public void runBlast() throws Exception {
        String cmd = getBlastCmd();
        //System.out.println(cmd);
        executeBlast(cmd);
    }

    public void runBlast2Seq() throws Exception {
        String cmd = getBl2seqCmd();
        //System.out.println(cmd);
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
                //System.out.println(x);
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

    public int getAlignmentview() {
        return alignmentview;
    }

    public void setAlignmentview(int alignmentview) {
        this.alignmentview = alignmentview;
    }

    public String getIsLowcomp() {
        return isLowcomp;
    }

    public void setIsLowcomp(String isLowcomp) {
        this.isLowcomp = isLowcomp;
    }
    
    public void setIsLowcomp(boolean b) {
        if(b) {
            setIsLowcomp(MASK_LOW_COMP_YES);
        } else {
            setIsLowcomp(MASK_LOW_COMP_NO);
        }
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
     * @return the maxseqs
     */
    public int getMaxseqs() {
        return maxseqs;
    }

    /**
     * @param maxseqs the maxseqs to set
     */
    public void setMaxseqs(int maxseqs) {
        this.maxseqs = maxseqs;
    }
}
