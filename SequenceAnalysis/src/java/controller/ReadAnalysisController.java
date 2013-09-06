/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import bean.BeanException;
import blast.BlastException;
import blast.BlastWrapper;
import core.CoreException;
import core.SeqRead;
import core.SeqValidation;
import dao.DaoException;
import dao.SeqReadDao;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletOutputStream;
import process.ProcessException;
import process.SeqAnalysisManager;
import util.SeqReadSorter;
import util.UtilException;

/**
 *
 * @author Lab User
 */
public class ReadAnalysisController {

    public List<SeqRead> analyzeReads(String dirName, String db, boolean bestMatch) throws ControllerException {
        SeqAnalysisManager manager = new SeqAnalysisManager();
        try {
            List<SeqRead> reads = manager.readSequences(SeqAnalysisManager.SEQ_READ_DIR + dirName);
            manager.analyzeReads(reads, BlastWrapper.BLAST_DB_PATH + SeqAnalysisManager.getDB(db), bestMatch);
            manager.updateGeneInfo(reads);
            Collections.sort(reads, new SeqReadSorter());
            return reads;
        } catch (UtilException ex) {
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        } catch (DaoException ex) {
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        } catch (CoreException ex) {
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        } catch (BeanException ex) {
            ex.printStackTrace();
            throw new ControllerException(ex.getMessage());
        }
    }

    public void download(ServletOutputStream out, List<SeqRead> reads) throws ControllerException {
        try {
            out.println("Read\tPhred\tResult\tGI\tGenBank Accession\tGene ID\tSymbol\tSynonyms");
            for (SeqRead read : reads) {
                out.print(read.getReadname() + "\t" + read.getPhred());
                SeqValidation v = read.getCurrentValidation();
                if (v != null) {
                    out.print("\t" + v.getResult() + "\t" + v.getSubjectid() + "\t"
                            + v.getAccession() + "\t" + v.getGeneid() + "\t" + v.getSymbol() + "\t" + v.getSynonyms());
                }
                out.println();
            }
        } catch (IOException ex) {
            throw new ControllerException(ex.getMessage());
        }
    }

    public SeqRead findRead(List<SeqRead> reads, String readname) {
        try {
            for (SeqRead read : reads) {
                if (read.getReadname().equals(readname)) {
                    return read;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    public String getBlastOutput(SeqRead read, String db)
            throws DaoException, ProcessException, BlastException, IOException {
        String type = SeqReadDao.TYPE_SEQ;;
        if (SeqAnalysisManager.REFSEQ_HUMAN_SEQ_DISPLAY.equals(db) 
                || SeqAnalysisManager.REFSEQ_MOUSE_SEQ_DISPLAY.equals(db)) {
            type = SeqReadDao.TYPE_SEQ;
        }
        if (SeqAnalysisManager.REFSEQ_HUMAN_CDS_DISPLAY.equals(db)
                || SeqAnalysisManager.REFSEQ_MOUSE_CDS_DISPLAY.equals(db)) {
            type = SeqReadDao.TYPE_CDS;
        }
        SeqReadDao dao = new SeqReadDao();
        SeqValidation v = read.getCurrentValidation();
        String seq = dao.queryRefseq(read.getCurrentValidation().getSubjectid(), type);
        SeqAnalysisManager manager = new SeqAnalysisManager();
        return manager.runPairwiseBlast(read.getReadname(), read.getSequence(), read.getCurrentValidation().getSubjectid(), seq);
    }
}
