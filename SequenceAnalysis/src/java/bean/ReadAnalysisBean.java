/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import blast.BlastException;
import controller.ControllerException;
import controller.ReadAnalysisController;
import core.CoreException;
import core.SeqRead;
import core.SeqValidation;
import dao.DaoException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;
import process.ProcessException;
import process.SeqAnalysisManager;

/**
 *
 * @author Lab User
 */
@ManagedBean(name = "readAnalysisBean")
@SessionScoped
public class ReadAnalysisBean implements Serializable {

    private String dirName;
    private int trimLeft;
    private int alength;
    private double pid;
    private int phredLow;
    private int phredHigh;
    private String db;
    private boolean isShowResult;
    private List<SeqRead> reads;
    private SeqRead currentRead;
    private String blastoutput;
    private boolean bestMatch;
    private List<SeqRead> readsTable;

    public ReadAnalysisBean() {
        resetValue();
    }

    public void resetValue() {
        this.setTrimLeft(SeqAnalysisManager.TRIMLEFT);
        this.setAlength(SeqAnalysisManager.ALENGTH);
        this.setPid(SeqAnalysisManager.PID);
        this.setPhredLow(SeqAnalysisManager.PHRED_LOW);
        this.setPhredHigh(SeqAnalysisManager.PHRED_HIGH);
        this.db = null;
        this.isShowResult = false;
        this.reads = null;
        this.currentRead = null;
        this.setBestMatch(false);
        this.dirName = null;
    }

    public String goback() {
        resetValue();
        return "ReadAnalysis.xhtml";
    }

    public String gonext() {
        Random randomGenerator = new Random();
        dirName = "" + randomGenerator.nextInt();
        //new File(SeqAnalysisManager.SEQ_READ_DIR + dirName + "\\").mkdir();
        new File(SeqAnalysisManager.SEQ_READ_DIR + dirName + "/").mkdir();
        return "ReadAnalysis2.xhtml";
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            FacesMessage msg = new FacesMessage("Upload file " + event.getFile().getFileName() + "...successful");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (BeanException e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage("Cannot upload files");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void copyFile(String fileName, InputStream in) throws BeanException {
        try {
            // write the inputStream to a FileOutputStream
            //OutputStream out = new FileOutputStream(new File(SeqAnalysisManager.SEQ_READ_DIR + dirName + "\\" + fileName));
            OutputStream out = new FileOutputStream(new File(SeqAnalysisManager.SEQ_READ_DIR + dirName + "/" + fileName));

            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new BeanException("Cannot upload files.");
        }
    }

    public String analyze() {
        ReadAnalysisController controller = new ReadAnalysisController();
        try {
            reads = controller.analyzeReads(dirName, db, bestMatch);
            if (reads == null || reads.isEmpty()) {
                throw new Exception("No reads to be analyzed.");
            }
            convertToReadTable();
            setIsShowResult(true);
        } catch (ControllerException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            setIsShowResult(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            setIsShowResult(false);
        }
        deleteFiles(SeqAnalysisManager.SEQ_READ_DIR + dirName);
        return "home?faces-redirect=true";
    }

    private void convertToReadTable() throws CoreException {
        readsTable = new ArrayList<SeqRead>();
        for (SeqRead read : reads) {
            List<SeqValidation> validations = read.getValidations();
            for (SeqValidation v : validations) {
                SeqRead r = new SeqRead();
                r.setPhred(read.getPhred());
                r.setRead(read.getRead());
                r.setReadname(read.getReadname());
                r.setSequence(read.getSequence());
                r.setCurrentValidation(v);
                readsTable.add(r);
            }
        }
    }

    public void showRead() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String readname = (String) requestMap.get("currentReadid");
        ReadAnalysisController controller = new ReadAnalysisController();
        currentRead = controller.findRead(reads, readname);
    }

    public void viewBlastAlignment() {
        ReadAnalysisController controller = new ReadAnalysisController();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            String readname = (String) requestMap.get("positionid");
            currentRead = controller.findRead(readsTable, readname);
            blastoutput = controller.getBlastOutput(currentRead, db);
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (DaoException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (BlastException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (ProcessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error occured."));
        }
    }

    public static void deleteFiles(String dir) {
        try {
            File file = new File(dir);
            String files[] = file.list();

            for (String temp : files) {
                File fileDelete = new File(file, temp);
                fileDelete.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void download() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) context.getResponse();
            response.setContentType("Application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "inline;filename=Result" + "_report.xls");
            //response.setContentType("text/plain");
            //response.setHeader("Content-Disposition", "attachment;filename=" + dirName + "_report.txt");
            ServletOutputStream out = response.getOutputStream();
            ReadAnalysisController controller = new ReadAnalysisController();
            controller.download(out, readsTable);
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error occured while writing the file."));
        }
    }

    public String getBestMatchString() {
        if (bestMatch) {
            return "Yes";
        }
        return "No";
    }

    public String getDbRefseqHumanSeq() {
        return SeqAnalysisManager.REFSEQ_HUMAN_SEQ_DISPLAY;
    }

    public String getDbRefseqMouseSeq() {
        return SeqAnalysisManager.REFSEQ_MOUSE_SEQ_DISPLAY;
    }

    public String getDbRefseqHumanCds() {
        return SeqAnalysisManager.REFSEQ_HUMAN_CDS_DISPLAY;
    }

    public String getDbRefseqMouseCds() {
        return SeqAnalysisManager.REFSEQ_MOUSE_CDS_DISPLAY;
    }

    /**
     * @return the dirName
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * @param dirName the dirName to set
     */
    public void setDirName(String dirName) {
        this.dirName = dirName;
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

    /**
     * @return the db
     */
    public String getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(String db) {
        this.db = db;
    }

    /**
     * @return the isShowResult
     */
    public boolean isIsShowResult() {
        return isShowResult;
    }

    /**
     * @param isShowResult the isShowResult to set
     */
    public void setIsShowResult(boolean isShowResult) {
        this.isShowResult = isShowResult;
    }

    /**
     * @return the reads
     */
    public List<SeqRead> getReads() {
        return reads;
    }

    /**
     * @param reads the reads to set
     */
    public void setReads(List<SeqRead> reads) {
        this.reads = reads;
    }

    /**
     * @return the currentRead
     */
    public SeqRead getCurrentRead() {
        return currentRead;
    }

    /**
     * @param currentRead the currentRead to set
     */
    public void setCurrentRead(SeqRead currentRead) {
        this.currentRead = currentRead;
    }

    /**
     * @return the blastoutput
     */
    public String getBlastoutput() {
        return blastoutput;
    }

    /**
     * @param blastoutput the blastoutput to set
     */
    public void setBlastoutput(String blastoutput) {
        this.blastoutput = blastoutput;
    }

    /**
     * @return the bestMatch
     */
    public boolean isBestMatch() {
        return bestMatch;
    }

    /**
     * @param bestMatch the bestMatch to set
     */
    public void setBestMatch(boolean bestMatch) {
        this.bestMatch = bestMatch;
    }

    /**
     * @return the readsTable
     */
    public List<SeqRead> getReadsTable() {
        return readsTable;
    }

    /**
     * @param readsTable the readsTable to set
     */
    public void setReadsTable(List<SeqRead> readsTable) {
        this.readsTable = readsTable;
    }
}
