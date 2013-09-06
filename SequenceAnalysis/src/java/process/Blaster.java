/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import blast.BlastException;
import blast.BlastWrapper;
import config.ApplicationProperties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import util.StringConvertor;

/**
 *
 * @author dongmei
 */
public class Blaster {

    public void runBlastx(String queryid, String queryseq, String subid, String subseq, String output, int outputFormat)
            throws IOException, ProcessException, BlastException {
        BlastWrapper blaster = new BlastWrapper();
        String inputfile1 = makeQueryFile(queryid, queryseq);
        String inputfile2 = makeQueryFile(subid, subseq);
        blaster.setInput(inputfile1);
        blaster.setInput2(inputfile2);
        blaster.setBl2seqOutput(output);
        String cmd = blaster.getBlastx2SeqCmd(BlastWrapper.BLAST_PROGRAM_PATH, outputFormat);
        //System.out.println(cmd);
        blaster.executeBlast(cmd);
        delete(inputfile1);
        delete(inputfile2);
    }

    public void runBlastn(String queryid, String queryseq, String subid, String subseq, String output, int outputFormat)
            throws IOException, ProcessException, BlastException {
        BlastWrapper blaster = new BlastWrapper();
        String inputfile1 = makeQueryFile(queryid, queryseq);
        String inputfile2 = makeQueryFile(subid, subseq);
        blaster.setInput(inputfile1);
        blaster.setInput2(inputfile2);
        blaster.setBl2seqOutput(output);
        String cmd = blaster.getBl2seqCmd(BlastWrapper.BLAST_PROGRAM_PATH, outputFormat);
        //System.out.println(cmd);
        blaster.executeBlast(cmd);
        delete(inputfile1);
        delete(inputfile2);
    }

    public String makeQueryFile(String id, String sequence) throws ProcessException {
        String file = ApplicationProperties.getInstance().getProperties().getProperty("tmp") + StringConvertor.getUniqueName(id);
        try {
            FileWriter out = new FileWriter(new File(file));
            out.write(">" + id + "\n");
            out.write(sequence);
            out.close();
        } catch (Exception ex) {
            throw new ProcessException("Cannot make query file: " + id);
        }
        return file;
    }

    public void delete(String file) {
        try {
            File out = new File(file);
            out.delete();
        } catch (Exception ex) {
        }
    }
}