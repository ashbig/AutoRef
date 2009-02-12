/*
 * ControlFileParser.java
 *
 * Created on October 24, 2007, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import core.ReagentInfo;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import transfer.ReagentTO;

/**
 *
 * @author dzuo
 */
public class ControlFileParser extends ReagentFileParser {
    
    /** Creates a new instance of ControlFileParser */
    public ControlFileParser() {
        super();
    }
    
   /* The control file is a tab-dilimited file with the following fields in order 
    * (NA means the field can be optional and NA will be used):
    * control, plate, well. The first line is the header information.
    */
    @Override
    public void parseFile(InputStream input) throws CloneFileParserException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String line = in.readLine();
            int num = 0;
            
            while((line = in.readLine()) != null && line.trim().length()>0) {
                StringTokenizer tokenizer = new StringTokenizer(line, "\t");
                String name = tokenizer.nextToken().trim();
                String plate = tokenizer.nextToken();
                String well = tokenizer.nextToken();
                ReagentInfo r = new ReagentInfo(name,ReagentTO.getTYPE_CONTROL(), null,plate,well);
                getReagents().add(r);
                getLabels().add(plate);
                num++;
            }
            
            in.close();
        } catch (Exception ex) {
            throw new CloneFileParserException(ex.getMessage());
        }
    }
}
