import java.util.ArrayList;
import java.util.List;
import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RVector;

public class Test {

    public static void main(String[] args) {
        // just making sure we have the right version of everything
        if (!Rengine.versionCheck()) {
            System.err.println("** Version mismatch - Java files don't match library version.");
            System.exit(1);
        }
        System.out.println("Creating Rengine (with arguments)");
        // 1) we pass the arguments from the command line
        // 2) we won't use the main loop at first, we'll start it later
        //    (that's the "false" as second argument)
        // 3) the callbacks are implemented by the TextConsole class above
        Rengine re = new Rengine(args, false, null);
        System.out.println("Rengine created, waiting for R");
        // the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }

        /* High-level API - do not use RNI methods unless there is no other way
        to accomplish what you want */
        try {
            REXP x;
            /**
            re.eval("brct.data <- read.table(\"Z:/DZuo/NAPPA/Tanya/Data20080625.txt\",sep=\"\t\", as.is=T, header=T)");
            re.eval("pdf(file=\"Z:/DZuo/NAPPA/Tanya/Histogram_dzuo.pdf\")");
            re.eval("arrname=names(brct.data)[12]");
            re.eval("with(brct.data, hist(log(brct.data[,12]),prob=T, main=arrname, xlab=\"Log Intensity\"))");
            re.eval("with(brct.data, lines(density(log(brct.data[,12]), na.rm=T)));");
            re.eval("dev.off()");
             * */
           
            //re.assign("work_dir", Constants.R_TMP);
            //re.assign("filename", "251");
            //re.eval("brct.data <- read.table(file=paste(work_dir, filename,sep=\"\") ,sep=\"\t\", as.is=T, header=T)");
            //re.eval("pdf(file=paste(work_dir, \"Histogram_dzuo.pdf\"))");
            re.eval("brct.data <- read.table(\"D:/dev/Test/NappaRepository/tmp/251\",sep=\"\t\", as.is=T, header=T)");
            re.eval("pdf(file=\"D:/dev/Test/NappaRepository/tmp/Histogram_dzuo.pdf\")");
            re.eval("minl=min(log(brct.data[,4]), na.rm=T)");
            re.eval("arrname=names(brct.data)[4]");
            re.eval("with(brct.data, hist(log(brct.data[,4]),prob=T, main=arrname, xlab=\"Log Intensity\"))");
            re.eval("with(brct.data, lines(density(log(brct.data[,4]), na.rm=T)))");
            
            List<String> controls = new ArrayList<String>();
            controls.add("mouse IgG");
            controls.add("No Spot");
            controls.add("pANT7nGFP");
            controls.add("human IgG");
            
            int i=0;
            for(String c:controls) {
                re.assign("control", c);
                re.assign("color", ""+(i+1));
                re.eval("abline(v=log(brct.data[brct.data$Name==control,4]), col=color)");
                
                if(i==0) {
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
            
            re.eval("legend(minl, 0.3, names,pch=pchv, col=colors)");
            //re.eval("legend(minl, 0.3, c(\"mouse IgG\",\"No Spot\",\"pANT7nGFP\",\"human IgG\"),pch=c(15,15,15,15), col=c(1,2,3,4))");
            re.eval("dev.off()"); 
            re.end();
            //System.out.println(x=re.eval("names(mytable)"));
        } catch (Exception e) {
            System.out.println("EX:" + e);
            e.printStackTrace();
        }

        re.end();
    }
}
