/*
 * ResultManager.java
 *
 * Created on December 5, 2007, 10:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package process;

import core.Fileresult;
import java.util.List;
import transfer.ContainerheaderTO;
import transfer.ResultTO;
import transfer.SampleTO;
import transfer.SamplepropertyTO;

/**
 *
 * @author dzuo
 */
public class ResultManager {
    private String resulttype;
    private List<ResultTO> results;
    
    /** Creates a new instance of ResultManager */
    public ResultManager() {
    }
    
    public void populateResultsForContainer(ContainerheaderTO container, List<Fileresult> results, String resulttype) throws ProcessException {
        for(Fileresult result:results) {
            int pos = result.getPos();
            SampleTO sample = container.getSample(pos);
            if(sample == null) {
                throw new ProcessException("ResultManager: Cannot find sample at position "+pos+" for container "+container.getBarcode());
            }
            ResultTO r = new ResultTO(0, resulttype, result.getRead(), 0, sample.getSampleid());
            r.setSample(sample);
            sample.addResult(r);
            
            SamplepropertyTO p = new SamplepropertyTO(sample.getSampleid(), resulttype, result.getRead());
            p.setIsnew(true);
            sample.addProperty(p);
        }
    }
}
