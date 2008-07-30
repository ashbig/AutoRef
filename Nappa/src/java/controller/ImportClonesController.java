/*
 * ImportClonesController.java
 *
 * Created on July 16, 2007, 12:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import dao.CloneDAO;
import dao.ReagentDAO;
import io.CloneFileParser;
import io.ReagentFileParser;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.sql.Connection;
import process.CloneImporter;
import process.ReagentImporter;
import transfer.ContainerheaderTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class ImportClonesController extends ImportReagentsController implements Serializable {
    
    /** Creates a new instance of ImportClonesController */
    public ImportClonesController() {
        super();
    }
    
    public ReagentFileParser makeFileParser() {
        return new CloneFileParser();
    }
    
    public ReagentImporter makeReagentImporter(List reagents) {
        return new CloneImporter(reagents);
    }
    
    public ReagentDAO makeReagentDAO(Connection conn) {
        return new CloneDAO(conn);
    }
    
    public static void main(String args[]) {
        String clonefile = "C:\\dev\\test\\nappa\\cloneinfo.txt";
        // ContainertypeTO ctype = new ContainertypeTO(ContainertypeTO.TYPE_PLATE96, null, 8, 12);
        String labware = ContainerheaderTO.getLABWARE_PLATE96();
        String sampletype = SampleTO.getTYPE_GLYCEROL();
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
        
        ImportClonesController c = new ImportClonesController();
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
