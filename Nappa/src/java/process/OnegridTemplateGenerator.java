/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import java.util.Collections;
import java.util.List;
import transfer.ContainerheaderTO;
import transfer.SampleTO;
import transfer.SlidecellTO;
import util.SlidecellPosComparator;

/**
 *
 * @author DZuo
 */
public class OnegridTemplateGenerator extends ViageneTemplateGenerator {
    public int getColcount(ContainerheaderTO container) {
        return container.getNumofcol()*container.getNumofcolinblock();
    }
    
    public String getLine(SlidecellTO cell, ContainerheaderTO container, SampleTO sample) {
        return "1\t1\t"+((cell.getBlockx()-1)*container.getNumofrowinblock()+cell.getBlockwellx())+"\t"+((cell.getBlocky()-1)*container.getNumofcolinblock()+cell.getBlockwelly())+"\t1\t"+sample.getReagentStringWithoutMM()+"\t0\t100\n";          
    }
    
    public void sortSamples(List<SampleTO> samples) {
        Collections.sort(samples, new SlidecellPosComparator());
    }
}