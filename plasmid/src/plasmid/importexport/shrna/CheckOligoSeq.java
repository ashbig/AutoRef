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
                
                if(n == 10)
                    position = Integer.parseInt(s);
                if(n == 13)
                    oligo = s;
                if(n == 19)
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
                
                if(n == 10)
                    position = Integer.parseInt(s);
                if(n == 13)
                    oligo = s;
                if(n == 19)
                    seq = s;
            }
            
            try {
                int pos = seq.toUpperCase().indexOf(oligo.substring(4, 24).toUpperCase())+1;
                if(pos >= 0)
                    output.add(line+"\t"+pos);
                else;
                error.add(line);
            } catch (StringIndexOutOfBoundsException ex) {
                error.add(line);
            }
        }
    }
    
    public void buildRefseqName() throws Exception {
        output = new ArrayList();
        
        for(int i=0; i<input.size(); i++) {
            String line = (String)input.get(i);
            System.out.println(line);
            StringTokenizer tokenizer = new StringTokenizer(line, "\t");
            String accession = tokenizer.nextToken();
            String gi = tokenizer.nextToken();
            String outputline = gi+"\tGenBank Accession\t"+accession+"\thttp://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val="+gi+"\n";
            outputline = outputline + gi+"\tGI\t"+gi+"\tNA";
            output.add(outputline);
        }
    }
    
    public void getRegion() throws Exception {
        output = new ArrayList();
        error = new ArrayList();
        
        for(int i=0; i<input.size(); i++) {
            String line = (String)input.get(i);
            System.out.println(line);
            StringTokenizer tokenizer = new StringTokenizer(line, "\t");
            
            int position = 0;
            int start = 0;
            int end = 0;
            int n = 0;
            String region = null;
            while(tokenizer.hasMoreTokens()) {
                String s = tokenizer.nextToken();
                n++;
                
                if(n == 10)
                    position = Integer.parseInt(s);
                if(n == 17)
                    start = Integer.parseInt(s);
                if(n == 18)
                    end = Integer.parseInt(s);
            }
            
            try {
                if(position+19<start)
                    region = "5UTR";
                else if(position>=start && position+19<=end+2)
                    region = "CDS";
                else if(position>end+2)
                    region = "3UTR";
                else if(position<start && position+19>=start)
                    region = "5UTR+CDS";
                else if(position>start && position+19>end+2)
                    region = "CDS+3UTR";
                else
                    region = "UNKNOWN";
                
                output.add(line+"\t"+region);
            } catch (StringIndexOutOfBoundsException ex) {
                error.add(line);
            }
        }
    }
    
    public void buildCloneInsert() throws Exception {
        output = new ArrayList();
        
        for(int i=0; i<input.size(); i++) {
            String line = (String)input.get(i);
            System.out.println(line);
            StringTokenizer tokenizer = new StringTokenizer(line, "\t");
            String cloneid = tokenizer.nextToken();
            String position = tokenizer.nextToken();
            String oligo = tokenizer.nextToken();
            String accession = tokenizer.nextToken();
            String gi = tokenizer.nextToken();
            String species = tokenizer.nextToken();
            String region = tokenizer.nextToken();
            
            String outputline = cloneid+"\t1\t"+oligo.length()+"\t"+species+"\tNA\tNA"+"\t"+cloneid+"\t"+oligo+"\tNA\tNA\tNA\t"+gi+"\t"+accession+"\tNo\tNo\t"+region+"\t"+gi;
            output.add(outputline);
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
        // String inputFile = "G:\\plasmid\\TRC\\input.txt";
        // String outputFile = "G:\\plasmid\\TRC\\output.txt";
        // String errorFile = "G:\\plasmid\\TRC\\error.txt";
        String insertFile = "G:\\plasmid\\TRC\\insert.txt";
        String insertoutput = "G:\\plasmid\\TRC\\insertout.txt";
        //String refseqname = "G:\\plasmid\\TRC\\refseqname.txt";
        //String refseqnameout = "G:\\plasmid\\TRC\\refseqnameout.txt";
        
        BufferedReader in = null;
        List info = new ArrayList();
        CheckOligoSeq checker = null;
        int n=0;
        int total = 0;
        
        try {
            //in = new BufferedReader(new FileReader(inputFile));
            in = new BufferedReader(new FileReader(insertFile));
            //in = new BufferedReader(new FileReader(refseqname));
            String line = null;
            
            while((line = in.readLine()) != null) {
                total++;
                System.out.println(total);
                info.add(line);
                n++;
                
                if(n == 1000) {
                    checker = new CheckOligoSeq(info);
                    //checker.checkOligo();
                    //checker.getOligoPosition();
                    //checker.getRegion();
                    //checker.printFile(checker.getOutput(), outputFile);
                    //checker.printFile(checker.getError(), errorFile);
                    checker.buildCloneInsert();
                    checker.printFile(checker.getOutput(), insertoutput);
                    //checker.buildRefseqName();
                    //checker.printFile(checker.getOutput(), refseqnameout);
                    info = new ArrayList();
                    n=0;
                }
            }
            
            checker = new CheckOligoSeq(info);
            //checker.checkOligo();
            //checker.getOligoPosition();
            //checker.getRegion();
            //checker.printFile(checker.getOutput(), outputFile);
            //checker.printFile(checker.getError(), errorFile);
            checker.buildCloneInsert();
            checker.printFile(checker.getOutput(), insertoutput);
            //checker.buildRefseqName();
            //checker.printFile(checker.getOutput(), refseqnameout);
            in.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
