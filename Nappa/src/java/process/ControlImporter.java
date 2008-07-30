/*
 * ControlImporter.java
 *
 * Created on November 2, 2007, 3:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import core.ReagentInfo;
import java.util.List;
import transfer.ContainercellTO;

/**
 *
 * @author dzuo
 */
public class ControlImporter extends ReagentImporter {
    
    /** Creates a new instance of ControlImporter */
    public ControlImporter(List<ReagentInfo> controls) {
        super(controls);
    }
    
    public String getCellType() {
        return ContainercellTO.getTYPE_CONTROL();
    }
}
