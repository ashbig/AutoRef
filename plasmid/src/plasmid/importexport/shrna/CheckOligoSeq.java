/*
 * CheckOligoSeq.java
 *
 * Created on August 11, 2005, 11:24 AM
 */

package plasmid.importexport.shrna;

import java.util.*;
import java.io.*;

/**
 *
 * @author  DZuo
 */
public class CheckOligoSeq {
    private List input;
    private List output;
    private List error;
    
    /** Creates a new instance of CheckOligoSeq */
    public CheckOligoSeq(List input) {
        this.input = input;
    }
    
    public List getOutput() {return output;}
    public List getError() {return error;}
    
    public void checkOligo() throws Exception {
        output = new ArrayList();
        error = new ArrayList();
        
        for(int i=0; i<input.size(); i++) {
            String line = (String)input.get(i);
            System.out.println(line);
            StringTokenizer tokenizer = new StringTokenizer(line, "\t");
            
            int position = 0;
            String seq = null;
            String oligo = null;
            int n = 0;
            while(tokenizer.hasMoreTokens()) {
                String s = tokenizer.nextToken();
                n++;
                
                if(n == 4)
                    position = Integer.parseInt(s);
                if(n == 7)
                    oligo = s;
                if(n == 13)
                    seq = s;
            }
            
            try {
                if(seq.substring(position-1, position+19).compareToIgnoreCase(oligo.substring(4, 24)) == 0)
                    output.add(line);
                else
                    error.add(line);
            } catch (StringIndexOutOfBoundsException ex) {
                error.add(line);
            }
        }
    }
        
    public void getOligoPosition() throws Exception {
        output = new ArrayList();
        error = new ArrayList();
        
        for(int i=0; i<input.size(); i++) {
            String line = (String)input.get(i);
            System.out.println(line);
            StringTokenizer tokenizer = new StringTokenizer(line, "\t");
            
            int position = 0;
            String seq = null;
            String oligo = null;
            int n = 0;
            while(tokenizer.hasMoreTokens()) {
                String s = tokenizer.nextToken();
                n++;
                
                if(n == 4)
                    position = Integer.parseInt(s);
                if(n == 7)
                    oligo = s;
                if(n == 13)
                    seq = s;
            }
            
            try {
                int pos = seq.toUpperCase().indexOf(oligo.substring(4, 24).toUpperCase())+1;
                if(pos >= 0)
                    output.add(line+"\t"+pos);
                else
                    error.add(line);
            } catch (StringIndexOutOfBoundsException ex) {
                error.add(line);
            }
        }
    }
        
    public void printFile(List l, String file) throws Exception {
        OutputStreamWriter f = new FileWriter(file, true);
        for(int i=0; i<l.size(); i++) {
            String s = (String)l.get(i);
            f.write(s+"\n");
        }
        f.close();
    }
    
    public static void main(String args[]) {
        String inputFile = "G:\\plasmid\\TRC\\input.txt";
        String outputFile = "G:\\plasmid\\TRC\\output.txt";
        String errorFile = "G:\\plasmid\\TRC\\error.txt";
        
        BufferedReader in = null;
        List info = new ArrayList();
        CheckOligoSeq checker = null;
        int n=0;
        int total = 0;
        
        try {
            in = new BufferedReader(new FileReader(inputFile));
            String line = null;
            
            while((line = in.readLine()) != null) {
                total++;
                System.out.println(total);
                info.add(line);
                n++;
                
                if(n == 1000) {
                    checker = new CheckOligoSeq(info);
                    //checker.checkOligo();
                    checker.getOligoPosition();
                    checker.printFile(checker.getOutput(), outputFile);
                    checker.printFile(checker.getError(), errorFile);
                    info = new ArrayList();
                    n=0;
                }
            }
            
            checker = new CheckOligoSeq(info);
            //checker.checkOligo();
            checker.getOligoPosition();
            checker.printFile(checker.getOutput(), outputFile);
            checker.printFile(checker.getError(), errorFile);
            
            in.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
