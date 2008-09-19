/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import java.util.List;
import transfer.ContainerheaderTO;
import transfer.SampleTO;
import transfer.SlidecellTO;

/**
 *
 * @author DZuo
 */
public abstract class ViageneTemplateGenerator {
    public static final String BLOCKWISE = "Blockwise";
    public static final String ONEGRID = "One-big-grid";
    public static final String MULTIPLEROI = "Multiple ROIs";

    public String generateTemplate(ContainerheaderTO container) {
        StringBuffer out = new StringBuffer("MicroViegne\tVersion\t2160\t\tType\tProtein\tCulture\ten-US\n");
        out.append("Row Count\t"+container.getNumofspots()+"\tColumn Count\t");
        out.append(getColcount(container));
        out.append("\n");
        out.append("Main Row\tMain Col\tSub Row\tSub Col\tSlide\tGene ID\tControl\tDilution\n");
        
        List<SampleTO> samples = container.getSamples();
        sortSamples(samples);
        for (SampleTO sample : samples) {
            SlidecellTO cell = (SlidecellTO) sample.getCell();
               out.append(getLine(cell, container, sample));
        }
        return out.toString();
    }
    
    public abstract int getColcount(ContainerheaderTO container);
    public abstract String getLine(SlidecellTO cell, ContainerheaderTO container, SampleTO sample);
    public abstract void sortSamples(List<SampleTO> samples);
}
