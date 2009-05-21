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
import transfer.SlidecellTO;

/**
 *
 * @author DZuo
 */
public class HistogramController implements Serializable {

    public static final String FILE_FORMAT_JPG = "jpg";
    public static final String FILE_FORMAT_PDF = "pdf";

    public List<SlideTO> findSlides(String rootid, String protocol, boolean isSample, boolean isSlidecell) throws ControllerException {
        try {
            List<SlideTO> slides = ContainerDAO.getProcessSlidesWithRootid(rootid, protocol, isSample, isSlidecell);
            return slides;
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }

    public List<SampleTO> getSamples(int executionid, int slideid, boolean isSlidecell) throws ControllerException {
        try {
            List<SampleTO> samples = ContainerDAO.getSamples(slideid, executionid, isSlidecell);
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

    public void printPlotInputFile(List<SampleTO> samples, String filename, String slideid) throws ControllerException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(new File(filename)));
            out.println("Block\tPosition\tName\tType\t" + slideid);

            for (SampleTO sample : samples) {
                ContainercellTO cell = (ContainercellTO) sample.getCell();
                out.println(((SlidecellTO)cell).getBlocknum()+"\t"+cell.getPos() + "\t" + sample.getName() + "\t" + cell.getType() + "\t" + sample.getLastResult().getValue());
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

    public void printPlotInputFile(List<SlideTO> slides, String filename) throws ControllerException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(new File(filename)));
            out.print("Block\tPosition\tName\tType");
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
                out.print(((SlidecellTO)s.getCell()).getBlocknum()+"\t"+(i + 1) + "\t" + s.getName() + "\t" + s.getCell().getType());
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

    public void printPlotOutputFile(Rengine re, int m, int n, String inputfile, String outputfile, String fileformat, List<String> controls) {
        re.assign("filename", inputfile);
        re.assign("output", outputfile);
        re.eval("brct.data <- read.table(filename,sep=\"\t\", as.is=T, header=T)");
        if (FILE_FORMAT_PDF.equals(fileformat)) {
            re.eval("pdf(output)");
        } else {
            re.eval("png(output)");
        }
        
        int b = m+n-1;
        re.eval("minl=min(log(brct.data[,"+m+":"+b+"]), na.rm=T)-4");
        re.eval("maxl=max(log(brct.data[,"+m+":"+b+"]), na.rm=T)");

        for (int x = m; x < m+n; x++) {
            re.eval("j<-"+x);
            re.eval("arrname=names(brct.data)[j]");
            re.eval("plot(brct.data$Position, log(brct.data[,j]), type=\"n\", main=arrname, xlab=\"Spot\", ylab=\"NonNorm Log Intensity\", ylim=c(minl,maxl))");
            re.eval("med=rep(0,48)");
            re.eval("medx=rep(0,48)");
            re.eval("p25=rep(0,48)");
            re.eval("p75=rep(0,48)");

            for(int d=1; d<49; d++) {
                re.eval("k<-"+d);
                    re.eval("points(brct.data$Position[brct.data$Block==k], log(brct.data[brct.data$Block==k,j]), col=k)");
                    re.eval("med[k]=median(log(brct.data[brct.data$Block==k,j]), na.rm=T)");
                    re.eval("medx[k]=(k-1)*49+25");
                    re.eval("p25[k]=quantile(log(brct.data[brct.data$Block==k,j]), prob=0.25, na.rm=T)");
                    re.eval("p75[k]=quantile(log(brct.data[brct.data$Block==k,j]), prob=0.75, na.rm=T)");
            }
            
            int i = 0;
            for (String c : controls) {
                re.assign("control", c);
                re.assign("color", "" + (i + 1));
                //re.eval("abline(v=log(brct.data[brct.data$Name==control,j]), col=color)");
                re.eval("points(brct.data$Position[brct.data$Name==control], log(brct.data[brct.data$Name==control,j]), col=color, pch=15,cex=1.25)");
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

            re.eval("legend(0,(minl+3.5), names, pch=pchv, col=colors)");
            re.eval("lines(medx, med, lty=1, lwd=2, col=1)");
            re.eval("lines(medx, p25, lty=2, lwd=2, col=1)");
            re.eval("lines(medx, p75, lty=2, lwd=2, col=1)");
        }
        re.eval("dev.off()");
    }
}
