/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import bean.BeanException;
import blast.BlastException;
import blast.BlastHit;
import blast.BlastWrapper;
import config.ApplicationProperties;
import core.CoreException;
import core.SeqRead;
import core.SeqValidation;
import dao.DaoException;
import dao.SeqReadDao;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.PlatePositionConvertor;
import util.SeqAnalyzer;
import util.SeqReadParser;
import util.StringConvertor;
import util.UtilException;

/**
 *
 * @author Lab User
 */
public class SeqAnalysisManager {
    public static final String SEQ_READ_DIR=ApplicationProperties.getInstance().getProperties().getProperty("seqfilepath");
    public static final int TRIMLEFT = 185;
    public static final int ALENGTH = 100;
    public static final double PID = 90.0;
    public static final int PHRED_LOW = 350;
    public static final int PHRED_HIGH = 650;
    public static final String REFSEQ_HUMAN_SEQ = "refseq_human_seq_fasta";
    public static final String REFSEQ_HUMAN_SEQ_DISPLAY = "Human RefSeq Sequence";
    public static final String REFSEQ_MOUSE_SEQ = "refseq_mouse_seq_fasta";
    public static final String REFSEQ_MOUSE_SEQ_DISPLAY = "Mouse RefSeq Sequence";
    public static final String REFSEQ_HUMAN_CDS = "refseq_human_cds_fasta";
    public static final String REFSEQ_HUMAN_CDS_DISPLAY = "Human RefSeq CDS";
    public static final String REFSEQ_MOUSE_CDS = "refseq_mouse_cds_fasta";
    public static final String REFSEQ_MOUSE_CDS_DISPLAY = "Mouse RefSeq CDS";
    
    private int trimLeft;
    private int alength;
    private double pid;
    private int phredLow;
    private int phredHigh;

    public SeqAnalysisManager() {
        this.trimLeft = SeqAnalysisManager.TRIMLEFT;
        this.alength = SeqAnalysisManager.ALENGTH;
        this.pid = SeqAnalysisManager.PID;
        this.phredLow = SeqAnalysisManager.PHRED_LOW;
        this.phredHigh = SeqAnalysisManager.PHRED_HIGH;
    }

    public List<SeqRead> readSequences(String filepath) throws IOException, UtilException, CoreException, BeanException  {
        File folder = new File(filepath);
        File[] listOfFiles = folder.listFiles();
        SeqReadParser parser = new SeqReadParser();
        List<SeqRead> reads = new ArrayList<SeqRead>();

        if(listOfFiles == null) {
            throw new BeanException("No files are uploaded.");
        }
        
        for (int k = 0; k < listOfFiles.length; k++) {
            File file = listOfFiles[k];
            SeqRead read = parser.parseReadFile(file);
            reads.add(read);
        }
        return reads;
    }

    public void analyzeReads(List<SeqRead> reads, String db, boolean bestMatch) throws UtilException {
        SeqAnalyzer analyzer = new SeqAnalyzer();
        for (SeqRead read : reads) {
            List<SeqValidation> validations = new ArrayList<SeqValidation>();

            int phred = read.getPhred();
            if (phred < PHRED_LOW) {
                SeqValidation validation = new SeqValidation();
                validation.setResult(SeqValidation.RESULT_LOWSCORE);
                validations.add(validation);
                read.setValidations(validations);
                continue;
            }

            List<BlastHit> hits = analyzer.getMatchsByBlast(read, db, getTrimLeft(), getPid(), getAlength(), bestMatch);
            for (BlastHit hit : hits) {
                SeqValidation validation = getSeqValidation(hit, phred);
                validations.add(validation);
            }
            read.setValidations(validations);
        }
    }

    public SeqValidation getSeqValidation(BlastHit hit, int phred) {
        SeqValidation validation = new SeqValidation();
        String result = null;
        if (hit != null) {
            validation.setSubjectid(hit.getSubjectid());
            double pid = hit.getMaxpid();
            int alength = hit.getMaxpidLength();
            validation.setMaxpid(pid);
            validation.setMaxpidalength(alength);
            pid = hit.getMaxAlengthpid();
            alength = hit.getMaxAlength();
            validation.setMaxalength(alength);
            validation.setMaxalengthpid(pid);
            if (pid >= PID && alength >= ALENGTH) {
                result = SeqValidation.RESULT_MATCH;
            }
            if (result == null && pid >= PID && alength >= ALENGTH) {
                result = SeqValidation.RESULT_MATCH;
            }
        }
        if (result == null) {
            if (phred >= PHRED_HIGH) {
                result = SeqValidation.RESULT_NOMATCH;
            } else {
                result = SeqValidation.RESULT_MANUAL;
            }
        }
        validation.setResult(result);
        return validation;
    }

    public void updateGeneInfo(List<SeqRead> reads) throws DaoException {
        SeqReadDao dao = new SeqReadDao();
        dao.queryGeneInfo(reads);
    }
    
    public static final String getDB(String displayName) {
        if(REFSEQ_HUMAN_SEQ_DISPLAY.equals(displayName)) {
            return REFSEQ_HUMAN_SEQ;
        }
        if(REFSEQ_MOUSE_SEQ_DISPLAY.equals(displayName)) {
            return REFSEQ_MOUSE_SEQ;
        }
        if(REFSEQ_HUMAN_CDS_DISPLAY.equals(displayName)) {
            return REFSEQ_HUMAN_CDS;
        }
        if(REFSEQ_MOUSE_CDS_DISPLAY.equals(displayName)) {
            return REFSEQ_MOUSE_CDS;
        }
        return REFSEQ_HUMAN_SEQ;
    }
    
    public String runPairwiseBlast(String queryid, String queryseq, String subid, String subseq)
            throws DaoException, ProcessException, BlastException, IOException {
        String output = ApplicationProperties.getInstance().getProperties().getProperty("tmp") + StringConvertor.getUniqueName(queryid+"_"+subid);
        Blaster blaster = new Blaster();
        blaster.runBlastn(queryid, queryseq, subid, subseq, output, BlastWrapper.PAIRWISE_OUTPUT);

        BufferedReader in = new BufferedReader(new FileReader(output));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine).append("\n");
        }
        in.close();

        blaster.delete(output);
        return sb.toString();
    }
    
    /**
     * @return the trimLeft
     */
    public int getTrimLeft() {
        return trimLeft;
    }

    /**
     * @param trimLeft the trimLeft to set
     */
    public void setTrimLeft(int trimLeft) {
        this.trimLeft = trimLeft;
    }

    /**
     * @return the alength
     */
    public int getAlength() {
        return alength;
    }

    /**
     * @param alength the alength to set
     */
    public void setAlength(int alength) {
        this.alength = alength;
    }

    /**
     * @return the pid
     */
    public double getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(double pid) {
        this.pid = pid;
    }

    /**
     * @return the phredLow
     */
    public int getPhredLow() {
        return phredLow;
    }

    /**
     * @param phredLow the phredLow to set
     */
    public void setPhredLow(int phredLow) {
        this.phredLow = phredLow;
    }

    /**
     * @return the phredHigh
     */
    public int getPhredHigh() {
        return phredHigh;
    }

    /**
     * @param phredHigh the phredHigh to set
     */
    public void setPhredHigh(int phredHigh) {
        this.phredHigh = phredHigh;
    }

    public static void main(String args[]) {
        String db = "D:\\dev\\blast\\db\\refseq_human_seq_fasta";
        String seqfilepath = "D:\\wade\\SeqAnalysis\\raphael\\input\\81042 pfasta-Jun13-16-36-43\\";
        String outputfile = "D:\\wade\\SeqAnalysis\\raphael\\output\\81042 pfasta-Jun13-16-36-43_result.txt";

        SeqAnalysisManager manager = new SeqAnalysisManager();
        try {
            List<SeqRead> reads = manager.readSequences(seqfilepath);
            manager.analyzeReads(reads, db, true);

            BufferedWriter output = new BufferedWriter(new FileWriter(outputfile));
            output.write("Read\tPhred\tResult\tMaxPID/Alength\tMaxAlength/PID\tGI\n");
            for (SeqRead read : reads) {
                if (read != null) {
                    output.write(read.getReadname() + "\t" + read.getPhred());
                    SeqValidation v = read.getCurrentValidation();
                    if (v != null) {
                        output.write("\t" + v.getResult() + "\t" + v.getMaxpid() + "/" + v.getMaxpidalength() + "\t"
                                + v.getMaxalength() + "/" + v.getMaxalengthpid() + "\t" + v.getSubjectid());
                    }
                    output.write("\n");
                }
            }
            output.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UtilException ex) {
            ex.printStackTrace();
        } catch (CoreException ex) {
            ex.printStackTrace();
        } catch (BeanException ex) {
            ex.printStackTrace();
        }
    }
}
