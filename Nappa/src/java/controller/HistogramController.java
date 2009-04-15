/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ContainerDAO;
import dao.DaoException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import transfer.ContainercellTO;
import transfer.SampleTO;
import transfer.SlideTO;

/**
 *
 * @author DZuo
 */
public class HistogramController implements Serializable {

    public List<SlideTO> findSlides(String rootid, String protocol) throws ControllerException {
        try {
            List<SlideTO> slides = ContainerDAO.getProcessSlidesWithRootid(rootid, protocol);
            return slides;
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }

    public List<SampleTO> getSamples(int executionid, int slideid) throws ControllerException {
        try {
            List<SampleTO> samples = ContainerDAO.getSamples(slideid, executionid);
            return samples;
        } catch (DaoException ex) {
            throw new ControllerException(ex.getMessage());
        }
    }

    public void printHistogramInputFile(List<SampleTO> samples, String filename) throws ControllerException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(new File(filename)));
            out.println("Position\tType\tResult");

            for (SampleTO sample : samples) {
                ContainercellTO cell = (ContainercellTO) sample.getCell();
                out.println(cell.getPos() + "\t" + cell.getType() + "\t" + sample.getLastResult().getValue());
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Cannot print the file.\n" + ex.getMessage());
        }
    }

    public void printHistogramOutputFile(Rengine re, String inputfile, String outputfile) {
        re.assign("filename", inputfile);
        re.assign("output", outputfile);
        re.eval("brct.data <- read.table(filename,sep=\"\t\", as.is=T, header=T)");
        re.eval("png(output)");
        re.eval("arrname=names(brct.data)[3]");
        re.eval("with(brct.data, hist(log(brct.data[,3]),prob=T, main=arrname, xlab=\"Log Intensity\"))");
        re.eval("with(brct.data, lines(density(log(brct.data[,3]), na.rm=T)))");
        re.eval("dev.off()");
    }
}
