/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import plasmid.Constants;
import plasmid.blast.BlastParser;
import plasmid.blast.BlastWrapper;
import plasmid.coreobject.OrderCloneValidation;
import plasmid.coreobject.OrderClones;

/**
 *
 * @author DZuo
 */
public class SequenceAnalysisManager {

    public static final int ALENGTH = 100;
    public static final double PID = 90.0;
    public static final String SEQUENCE_PATH = Constants.SEQ_ANALYSIS_PATH;
    public static final int PHRED_LOW = 350;
    public static final int PHRED_HIGH = 650;
    private double pid;
    private int alength;

    public SequenceAnalysisManager() {
        setPid(this.PID);
        setAlength(this.ALENGTH);
    }

    public void getCloneSequences(List clones, String seqfilepath) throws Exception {
        File folder = new File(seqfilepath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < clones.size(); i++) {
            OrderClones orderClone = (OrderClones) clones.get(i);
            String clonename = orderClone.getClone().getName();
            for (int k = 0; k < listOfFiles.length; k++) {
                File file = listOfFiles[k];
                String filename = file.getName().trim();
                int sep = filename.indexOf(".");
                if (sep > 0) {
                    filename = filename.substring(0, sep);
                }
                sep = filename.indexOf("_");
                if (sep > 0) {
                    filename = filename.substring(0, sep);
                }
                sep = filename.indexOf("-");
                if (sep > 0) {
                    filename = filename.substring(0, sep);
                }
                if (file.isFile() && filename.equalsIgnoreCase(clonename)) {
                    OrderCloneValidation validation = new OrderCloneValidation(orderClone);
                    orderClone.setValidation(validation);
                    validation.setMethod(OrderProcessManager.PLATINUM_VALIDATION_METHOD_END_SEQ);

                    BufferedReader f = new BufferedReader(new FileReader(file));
                    String seq = "";
                    String line = null;
                    while ((line = f.readLine()) != null) {
                        if (line.indexOf(">") >= 0) {
                            try {
                                String[] s = line.split(" ");
                                int phred = Integer.parseInt(s[1]);
                                validation.setPhred(phred);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                throw new Exception("Wrong file format.");
                            }
                            continue;
                        }
                        seq = seq + line;
                    }
                    f.close();
                    file.delete();
                    validation.setSequence(seq);
                    break;
                }
            }
        }
    }

    public void runBlast(List clones) throws Exception {
        BlastManager bm = new BlastManager();
        for (int i = 0; i < clones.size(); i++) {
            OrderClones orderClone = (OrderClones) clones.get(i);
            OrderCloneValidation validation = orderClone.getValidation();
            if (validation == null) {
                continue;
            }

            int phred = validation.getPhred();
            if (phred < PHRED_LOW) {
                validation.setResult(OrderCloneValidation.RESULT_FAIL_LOWSCORE);
                continue;
            }
            if (phred < PHRED_HIGH) {
                validation.setResult(OrderCloneValidation.RESULT_MANUAL);
                continue;
            }

            int cloneid = validation.getCloneid();
            String cloneseq = bm.getCloneSequence(cloneid);
            if (cloneseq == null || cloneseq.trim().length() == 0) {
                cloneseq = bm.getReferenceSequence(cloneid);
            }

            if (cloneseq == null || cloneseq.trim().length() == 0) {
                continue;
            }

            String clonename = orderClone.getClone().getName();
            cloneseq = ">PlasmID|" + clonename + "\n" + cloneseq;
            String seq = validation.getSequence();
            String output = BlastWrapper.BLAST_FILE_PATH + clonename + ".out";
            if (seq != null && seq.trim().length() > 0) {
                String s = bm.runBl2seq(BlastWrapper.PROGRAM_BLASTN, cloneseq, ">" + clonename + "\n" + seq, true, output, BlastWrapper.PAIRWISE_OUTPUT, false);
                BlastParser parser = new BlastParser(output);
                parser.setAlength(getAlength());
                parser.setPid(getPid());
                parser.parseTabularOutput();
                List infos = parser.getInfos();

                if (infos.size() > 0) {
                    validation.setResult(OrderCloneValidation.RESULT_PASS);
                } else {
                    validation.setResult(OrderCloneValidation.RESULT_FAIL_MISMATCH);
                }
            }
        }
    }

    public double getPid() {
        return pid;
    }

    public void setPid(double pid) {
        this.pid = pid;
    }

    public int getAlength() {
        return alength;
    }

    public void setAlength(int alength) {
        this.alength = alength;
    }
}
