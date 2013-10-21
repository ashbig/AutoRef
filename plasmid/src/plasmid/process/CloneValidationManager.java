/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import plasmid.blast.BlastParser;
import plasmid.blast.BlastWrapper;
import plasmid.coreobject.Clone;
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.OrderCloneValidation;
import plasmid.coreobject.OrderClones;
import plasmid.util.CloneValidationComparatorDesc;

/**
 *
 * @author dongmei
 */
public class CloneValidationManager {

    public static final int ALENGTH = 100;
    public static final double PID = 90.0;
    private double pid;
    private int alength;

    public CloneValidationManager() {
        setPid(this.PID);
        setAlength(this.ALENGTH);
    }

    public List<String> checkOrders(List<String> orderidList, List<CloneOrder> orders) {
        List<String> nofound = new ArrayList<String>();

        for (String orderid : orderidList) {
            int id = 0;
            try {
                id = Integer.parseInt(orderid);
            } catch (Exception ex) {
            }
            boolean found = false;
            for (CloneOrder order : orders) {
                if (id == order.getOrderid()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                nofound.add(orderid);
            }
        }
        return nofound;
    }

    public void readIsolates(List<OrderClones> clones, String seqfilepath) throws Exception {
        File folder = new File(seqfilepath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < clones.size(); i++) {
            OrderClones clone = (OrderClones) clones.get(i);
            String clonename = clone.getClone().getName();
            List validations = new ArrayList();
            clone.setValidations(validations);

            for (int k = 0; k < listOfFiles.length; k++) {
                File file = listOfFiles[k];
                String filename = file.getName().trim();
                String isolatename = filename;
                int sep = isolatename.indexOf(".");
                if (sep > 0) {
                    isolatename = isolatename.substring(0, sep);
                }
                sep = isolatename.indexOf("_");
                if (sep > 0) {
                    isolatename = isolatename.substring(0, sep);
                }
                sep = isolatename.indexOf("-");
                if (sep > 0) {
                    isolatename = isolatename.substring(0, sep);
                }
                if (file.isFile() && isolatename.equalsIgnoreCase(clonename)) {
                    OrderCloneValidation validation = new OrderCloneValidation();
                    validation.setCloneid(clone.getCloneid());
                    validation.setClonename(clonename);
                    validation.setReadname(filename);

                    BufferedReader f = new BufferedReader(new FileReader(file));
                    String seq = "";
                    String line = null;
                    String read = "";
                    while ((line = f.readLine()) != null) {
                        read += line + "\n";
                        if (line.indexOf(">") >= 0) {
                            try {
                                String[] s = line.split(" ");
                                //The first number is the phred score
                                int phred = Integer.parseInt(s[1]);
                                validation.setPhred(phred);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                throw new Exception("Wrong file format: "+filename);
                            }
                            continue;
                        }
                        seq = seq + line;
                    }
                    f.close();
                    file.delete();
                    validation.setSequence(seq);
                    validation.setRead(read);
                    clone.getValidations().add(validation);
                }
            }
        }
    }

    public List checkIsolates(List<OrderClones> clones) {
        List l = new ArrayList<Clone>();

        for (OrderClones clone : clones) {
            List validations = clone.getValidations();
            if (validations.size() < 2) {
                l.add(clone.getClone().getName());
            }
        }

        return l;
    }

    public void sortClones(List<OrderClones> clones) {
        for (OrderClones clone : clones) {
            List validations = clone.getValidations();
            Collections.sort(validations, new CloneValidationComparatorDesc());
            clone.setValidations(validations);
        }
    }

    public OrderCloneValidation analyzeIsolates(List<OrderCloneValidation> validations, String cloneseq) throws Exception {
        if (validations == null || validations.size() == 0) {
            return null;
        }

        String workflow = null;
        if (validations.size() > 0) {
            workflow = OrderCloneValidation.WORKFLOW_INITIAL;
        }
        if (validations.size() > 1) {
            workflow = OrderCloneValidation.WORKFLOW_TROUBLESHOOTING;
        }

        int manual = 0;
        int mismatch = 0;
        OrderCloneValidation mismatchIsolate = null;
        OrderCloneValidation manualIsolate = null;
        BlastManager bm = new BlastManager();
        OrderCloneValidation validation = null;

        for (int i = 0; i < validations.size(); i++) {
            validation = (OrderCloneValidation) validations.get(i);
            validation.setWorkflow(workflow);
            validation.setMethod(OrderProcessManager.PLATINUM_VALIDATION_METHOD_END_SEQ);

            if (cloneseq == null || cloneseq.trim().length() == 0) {
                validation.setResult(OrderCloneValidation.RESULT_MANUAL_NO_CLONE_SEQ);
                return validation;
            }

            if (validation.getPhred() < SequenceAnalysisManager.PHRED_LOW) {
                continue;
            } else {
                if (validation.getPhred() > SequenceAnalysisManager.PHRED_HIGH) {
                    String seq = validation.getSequence();
                    if (seq == null || seq.trim().length() == 0) {
                        manual = 1;
                        manualIsolate = validation;
                        manualIsolate.setResult(OrderCloneValidation.RESULT_MANUAL_NO_READ_SEQ);
                        continue;
                    }

                    String result = analyzeByBlast(bm, validation.getClonename(), cloneseq, seq);
                    validation.setResult(result);
                    if (OrderCloneValidation.RESULT_PASS.equals(result)) {
                        return validation;
                    } else {
                        if (mismatch == 0) {
                            mismatch = 1;
                            mismatchIsolate = validation;
                        }
                    }
                } else {
                    manual = 1;
                    manualIsolate = validation;
                    manualIsolate.setResult(OrderCloneValidation.RESULT_MANUAL);
                }
            }
        }

        if (manual == 1) {
            return manualIsolate;
        } else if (mismatch == 1) {
            return mismatchIsolate;
        } else {
            validation.setResult(OrderCloneValidation.RESULT_FAIL_LOWSCORE);
            return validation;
        }
    }

    public String analyzeByBlast(BlastManager bm, String clonename, String cloneseq, String seq) throws Exception {
        String output = BlastWrapper.BLAST_FILE_PATH + clonename + ".out";
        String s = bm.runBl2seq(BlastWrapper.PROGRAM_BLASTN, "PlasmID|" + clonename, cloneseq, clonename, seq, true, output, BlastWrapper.DEFAULT_OUTPUT, false);
        BlastParser parser = new BlastParser(output);
        if (cloneseq.length() >= ALENGTH) {
            parser.setAlength(getAlength());
        } else {
            parser.setAlength(cloneseq.length());
        }
        parser.setPid(getPid());
        parser.parseTabularOutput();
        List infos = parser.getInfos();

        if (infos.size() > 0) {
            return OrderCloneValidation.RESULT_PASS;
        } else {
            return OrderCloneValidation.RESULT_FAIL_MISMATCH;
        }
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
}
