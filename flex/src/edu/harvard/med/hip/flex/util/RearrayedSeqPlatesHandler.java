/*
 * RearrayedSeqPlatesHandler.java
 *
 * Created on January 28, 2004, 1:14 PM
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.flex.core.Container;
import edu.harvard.med.hip.flex.special_projects.UpdateClonename;
import edu.harvard.med.hip.flex.process.SeqContainerMapper;
import edu.harvard.med.hip.flex.user.*;

/**
 *
 * @author  DZuo
 */
public class RearrayedSeqPlatesHandler {
    protected boolean isFile = false;
    SummaryTablePopulator populator = null;
    
    /** Creates a new instance of RearrayedSeqPlatesHandler */
    public RearrayedSeqPlatesHandler() {
    }
    
    public RearrayedSeqPlatesHandler(boolean isFile) {
        this.isFile = isFile;
    }
    
    public RearrayedSeqPlatesHandler(boolean isFile, SummaryTablePopulator populator) {
        this.isFile = isFile;
        this.populator = populator;
    }
    
    public void processSummaryTable(List containers, int strategyid, String cloneType) {
        if(populator == null)
            return;
        
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
    
    public void emailFailed(List containers) throws Exception {
        String message = "The following containers failed cloneid update:\n";
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            message = message+c.getId()+"\t"+c.getLabel()+"\n";
        }
        
        Mailer.sendMessage("dongmei_zuo@hms.harvard.edu","dongmei_zuo@hms.harvard.edu", "cloneid update failed",message);
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
