/*
 * RearrayedSeqPlatesHandler.java
 *
 * Created on January 28, 2004, 1:14 PM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.special_projects.UpdateClonename;
import edu.harvard.med.hip.flex.process.SeqContainerMapper;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;

/**
 *
 * @author  DZuo
 */
public class RearrayedSeqPlatesHandler {
    protected boolean isFile = false;
    SummaryTablePopulator populator = null;
    
    /** Creates a new instance of RearrayedSeqPlatesHandler */
    public RearrayedSeqPlatesHandler() {
        populator = new SummaryTablePopulator();
    }
    
    public RearrayedSeqPlatesHandler(boolean isFile) {
        this.isFile = isFile;
        populator = new SummaryTablePopulator();
    }
    
    public RearrayedSeqPlatesHandler(boolean isFile, SummaryTablePopulator populator) {
        this.isFile = isFile;
        this.populator = populator;
    }
    
    public void processSummaryTable(List containers, int strategyid, String cloneType) {
        if(populator.populateObtainedMasterClonesWithContainers(containers, strategyid, cloneType)) {
            populator.sendEmail(true, containers);
        } else {
            populator.sendEmail(false, containers);
        }
    }
    
    public void processSeqPlates(List containers, String researcherBarcode) {
        UpdateClonename uc = new UpdateClonename();
        List failed = uc.updateAllRearrayPlateCloneid(containers);
        
        try {
            if(failed.size() > 0) {
                emailFailed(failed);
            }
            
            //if(isFile) {
             //   emailFile(containers, researcherBarcode);
            //}
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public void insertSeqPlatesIntoClonestorage(List containers) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        
            populator.populateClonestorageTableWithContainers(containers, StorageType.WORKING, StorageForm.GLYCEROL, conn);
            
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
            
            try {
                sendEmail(containers, ex.getMessage());
            } catch (Exception exc) {
                System.out.println(exc);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void emailFailed(List containers) throws Exception {
        String message = "The following containers failed cloneid update:\n";
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            message = message+c.getId()+"\t"+c.getLabel()+"\n";
        }
        
        Mailer.sendMessage("dongmei_zuo@hms.harvard.edu","dongmei_zuo@hms.harvard.edu", "cloneid update failed",message);
    }
    
    public void sendEmail(List containers, String error) throws Exception {
        String message = "The following containers failed working storage insert:\n";
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            message = message+c.getId()+"\t"+c.getLabel()+"\n";
            message = message+"\nError: \n"+error+"\n";
        }
        
        Mailer.sendMessage("dongmei_zuo@hms.harvard.edu","dongmei_zuo@hms.harvard.edu", "working storage insert failed",message);
    }
    
    public void emailFile(List containers, String researcherBarcode) {
        User user = User.getUserFromBarcode(researcherBarcode);
        String filepath = SeqContainerMapper.FILEPATH;
        Collection fileCol = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            fileCol.add(new File(filepath+c.getLabel()));
        }
        try {
            Mailer.sendMessage(user.getUserEmail(),"dongmei_zuo@hms.harvard.edu","dongmei_zuo@hms.harvard.edu","Files for rearrayed sequencing plates","Attached are your files for rearrayed sequencing plates", fileCol);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static void main(String args[]) {
        Container c1 = new Container(7893, null, null, "SAE000727");
        Container c2 = new Container(7894, null, null, "SBF000727");
        List containers = new ArrayList();
        containers.add(c1);
        containers.add(c2);
        
        RearrayedSeqPlatesHandler handler = new RearrayedSeqPlatesHandler(true);
        handler.processSeqPlates(containers, "joy");
    }
}
