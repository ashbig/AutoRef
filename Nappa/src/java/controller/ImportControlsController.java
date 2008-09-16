/*
 * ImportControlsController.java
 *
 * Created on October 24, 2007, 3:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import io.ControlFileParser;
import io.ReagentFileParser;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import transfer.ContainerheaderTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class ImportControlsController extends ImportReagentsController implements Serializable{
    
    /** Creates a new instance of ImportControlsController */
    public ImportControlsController() {
        super();
    }
    
    @Override
    public ReagentFileParser makeFileParser() { 
        return new ControlFileParser();
    }
    
    public static void main(String args[]) {
        String clonefile = "C:\\dev\\test\\nappa\\control.txt";
       // ContainertypeTO ctype = new ContainertypeTO(ContainertypeTO.TYPE_PLATE96, null, 8, 12);
        String labware = ContainerheaderTO.getLABWARE_PLATE384();
        String sampletype = SampleTO.getTYPE_CONTROL();
        String sampleform = SampleTO.getFORM_GLYCEROL();
        ProcessprotocolTO protocol = new ProcessprotocolTO(ProcessprotocolTO.IMPORT_PLATES, null, null);
        ResearcherTO researcher = new ResearcherTO("dzuo", null, null, null, "dzuo");
        
        InputStream in = null;
        try {
            in = new FileInputStream(clonefile);
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
        
        ImportControlsController c = new ImportControlsController();
       // c.setContainertype(ctype);
        c.setSampletype(sampletype);
        c.setSampleform(sampleform);
        c.setLocation(ContainerheaderTO.getLOCATION_FREEZER());
        c.setFile(in);
        c.setFilename(clonefile);
        c.setProtocol(protocol);
        c.setWho(researcher);
        
        try {
            c.doProcess();
            c.persistProcess();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
