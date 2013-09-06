/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import blast.BlastHit;
import blast.BlastParser;
import blast.BlastWrapper;
import core.SeqRead;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Lab User
 */
public class SeqAnalyzer {

    public List<BlastHit> getMatchsByBlast(SeqRead read, String db, int trimLeft, double pid, int alength, boolean bestMatch) throws UtilException {
        try {
            BlastWrapper blaster = new BlastWrapper();
            String outputfile = BlastWrapper.BLAST_FILE_PATH + read.getReadname() + "_out.txt";
            blaster.setOutput(outputfile);
            String inputfile = makeQueryFile("" + read.getReadname(), read.getSequence().substring(trimLeft));
            blaster.setInput(inputfile);
            blaster.setDatabase(db);
            //default set to yes
            blaster.setLowcomp("yes");
            //blaster.setLowcomp("no");
            if(bestMatch) {
                blaster.setMaxTargetSeq(1);
            }
            String cmd = blaster.getBlastnCmd(BlastWrapper.BLAST_PROGRAM_PATH);
            //String cmd = blaster.getBlastxCmd(BLAST_PROGRAM_PATH);
            blaster.executeBlast(cmd);
            //System.out.println(cmd);

            BlastParser parser = new BlastParser(outputfile);
            parser.setAlength(alength);
            parser.setPid(pid);
            parser.parseTabularOutput();
            List infos = parser.getInfos();
            return infos;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new UtilException("Cannot run blast for "+read.getReadname()+"\n"+ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new UtilException("Cannot run blast for "+read.getReadname()+"\n"+ex.getMessage());
        }
    }

    public String makeQueryFile(String id, String sequence) throws UtilException {
        String file = BlastWrapper.BLAST_FILE_PATH + id;
        try {
            FileWriter out = new FileWriter(new File(file));
            out.write(">" + id + "\n");
            out.write(sequence);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new UtilException("Cannot make query file.");
        }
        return file;
    }
}
