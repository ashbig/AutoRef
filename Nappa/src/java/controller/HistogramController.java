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
import org.rosuda.JRI.Rengine;
import transfer.ContainercellTO;
import transfer.ContainerheaderTO;
import transfer.SampleTO;
import transfer.SlideTO;

/**
 *
 * @author DZuo
 */
public class HistogramController implements Serializable {

    public static final String FILE_FORMAT_JPG = "jpg";
    public static final String FILE_FORMAT_PDF = "pdf";

    public List<SlideTO> findSlides(String rootid, String protocol, boolean isSample) throws ControllerException {
        try {
            List<SlideTO> slides = ContainerDAO.getProcessSlidesWithRootid(rootid, protocol, isSample);
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

    public void printHistogramInputFile(List<SampleTO> samples, String filename, String slideid) throws ControllerException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(new File(filename)));
            out.println("Position\tName\tType\t" + slideid);

            for (SampleTO sample : samples) {
                ContainercellTO cell = (ContainercellTO) sample.getCell();
                out.println(cell.getPos() + "\t" + sample.getName() + "\t" + cell.getType() + "\t" + sample.getLastResult().getValue());
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Cannot print the file.\n" + ex.getMessage());
        }
    }

    public void printHistogramInputFile(List<SlideTO> slides, String filename) throws ControllerException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(new File(filename)));
            out.print("Position\tName\tType");
            for (SlideTO slide : slides) {
                out.print("\t" + slide.getBarcode());
            }
            out.println();

            SlideTO firstSlide = (SlideTO) slides.get(0);
            ContainerheaderTO container = firstSlide.getContainer();
            List<SampleTO> firstSamples = container.getSamples();
            int n = firstSamples.size();
            for (int i = 0; i < n; i++) {
                SampleTO s = container.getSample(i + 1);
                out.print((i + 1) + "\t" + s.getName() + "\t" + s.getCell().getType());
                for (SlideTO slide : slides) {
                    out.print("\t" + slide.getContainer().getSample(i + 1).getLastResult().getValue());
                }
                out.println();
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Cannot print the file.\n" + ex.getMessage());
        }
    }

    public void printHistogramOutputFile(Rengine re, int m, int n, String inputfile, String outputfile, String fileformat, List<String> controls) {
        re.assign("filename", inputfile);
        re.assign("output", outputfile);
        //re.assign("titlename", "Slide: " + slidebarcode);
        re.eval("brct.data <- read.table(filename,sep=\"\t\", as.is=T, header=T)");
        if (FILE_FORMAT_PDF.equals(fileformat)) {
            re.eval("pdf(output)");
        } else {
            re.eval("png(output)");
        }

        for (int x = m; x < m+n; x++) {
            re.eval("j<-"+x);
            re.eval("minl=min(log(brct.data[,j]), na.rm=T)");
            re.eval("arrname=names(brct.data)[j]");
            re.eval("with(brct.data, hist(log(brct.data[,j]),prob=T, main=arrname, xlab=\"Log Intensity\"))");

            int i = 0;
            for (String c : controls) {
                re.assign("control", c);
                re.assign("color", "" + (i + 1));
                re.eval("abline(v=log(brct.data[brct.data$Name==control,j]), col=color)");

                if (i == 0) {
                    re.eval("names<-c(control)");
                    re.eval("colors<-c(color)");
                    re.eval("pchv<-c(15)");
                } else {
                    re.eval("names<-append(names,control)");
                    re.eval("colors<-append(colors,color)");
                    re.eval("pchv<-append(pchv,15)");
                }

                i++;
            }

            re.eval("with(brct.data, lines(density(log(brct.data[,j]), na.rm=T)))");
            re.eval("legend(minl, 0.3, names,pch=pchv, col=colors)");
        }
        re.eval("dev.off()");
    }
}
