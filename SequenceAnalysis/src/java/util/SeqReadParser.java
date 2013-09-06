/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import core.CoreException;
import core.SeqRead;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Lab User
 */
public class SeqReadParser {

    public SeqRead parseReadFile(File file) throws IOException, UtilException, CoreException {
        SeqRead read = null;
        if (file.isFile()) {
            read = new SeqRead();
            BufferedReader f = new BufferedReader(new FileReader(file));
            String seq = "";
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = f.readLine()) != null) {
                sb.append(line+"\n");
                //>19827-24_pGem-CP1_H3 869 1455 42 855
                //PlateName-NumericWell_SampleName_AlphaNumericWell  phred
                if (line.indexOf(">") >= 0) {
                    try {
                        String[] s = line.split(" ");
                        int m = s[0].indexOf("-");
                        int n = s[0].indexOf("_");
                        int k = s[0].lastIndexOf("_");
                        String plate = s[0].substring(1, m);
                        int pos = Integer.parseInt(s[0].substring(m+1, n));
                        String well = s[0].substring(k+1);
                        String readname = s[0].substring(n+1, k);
                        int phred = Integer.parseInt(s[1]);
                        read.setPlate(plate);
                        read.setPos(pos);
                        read.setWell(well);
                        read.setReadname(readname);
                        read.setPhred(phred);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new UtilException("Wrong file format.");
                    }
                    continue;
                }
                seq = seq + line;
            }
            f.close();
            read.setSequence(seq);
            read.setRead(sb.toString());
        }
        return read;
    }
}
