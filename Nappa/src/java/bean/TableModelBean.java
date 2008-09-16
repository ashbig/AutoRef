/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import core.Well;
import java.util.ArrayList;
import java.util.List;
import transfer.ContainerheaderTO;
import transfer.SampleTO;

/**
 *
 * @author DZuo
 */
public class TableModelBean {
    public static List convertContainerToDatamodel(ContainerheaderTO c) {
        List mappingsInTable = new ArrayList<List>();
        for(int i=0; i<c.getRownum(); i++) {
            List<SampleTO> cols = new ArrayList<SampleTO>();
            for(int j=0; j<c.getContainertype().getNumofcol(); j++) {
                SampleTO s = c.getSample(Well.convertWellToVPos(i+1, j+1, c.getRownum()));
                cols.add(s);
            }
            mappingsInTable.add(cols);
        }
        return mappingsInTable;
    }
}
