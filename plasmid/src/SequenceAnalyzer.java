
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */import java.util.ArrayList;

import java.util.List;
import java.util.StringTokenizer;
import plasmid.blast.BlastHit;
import plasmid.blast.BlastInfo;
import plasmid.blast.BlastParser;
import plasmid.blast.BlastWrapper;
import plasmid.coreobject.Dnasequence;

/**
 *
 * @author Dongmei
 */
public class SequenceAnalyzer {

    public static final String BLAST_PROGRAM_PATH = "c:\\blast-2.2.26+\\bin\\";
    public static final String BLAST_FILE_PATH = "C:\\dev\\plasmid_support\\PlasmidAnalysis_201203\\blast\\";
    public static final String FILE_PATH = "C:\\dev\\plasmid_support\\PlasmidAnalysis_201203\\";

    public String getBestMatchByBlast(Dnasequence sequence, String db) {
        try {
            BlastWrapper blaster = new BlastWrapper();
            String outputfile = BLAST_FILE_PATH+sequence.getReferenceid()+"_out.txt";
            blaster.setOutput(outputfile);
            String inputfile = makeQueryFile("" + sequence.getReferenceid(), sequence.getSequence());
            blaster.setInput(inputfile);
            blaster.setDatabase(db);
            blaster.setLowcomp("yes");
            String cmd = blaster.getBlastnCmd(BLAST_PROGRAM_PATH);
            System.out.println(cmd);
            blaster.executeBlast(cmd);

            BlastParser parser = new BlastParser(outputfile);
            parser.setAlength(0);
            parser.setPid(0);
            parser.parseTabularOutput();
            List infos = parser.getInfos();
            if (infos == null || infos.isEmpty()) {
                return "";
            }

            BlastHit hit = (BlastHit) infos.get(0);
            List blastInfos = hit.getBlastinfos();
            if (blastInfos == null || blastInfos.isEmpty()) {
                return "";
            }
            BlastInfo info = (BlastInfo) blastInfos.get(0);
            //blaster.delete(inputfile);
            //blaster.delete(outputfile);
            return info.getSubjectid();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void findMatchingRefseq(String input, String output, String db) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(input));
            OutputStreamWriter out = new FileWriter(output);
            out.write("RefseqID\tCloneid\n");
            
            String line = in.readLine();
            //cloneid\tsequence
            while ((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                int cloneid = Integer.parseInt(st.nextToken());
                String seq = st.nextToken();
                Dnasequence sequence = new Dnasequence();
                sequence.setReferenceid(cloneid);
                sequence.setSequence(seq);
                String refseqid = getBestMatchByBlast(sequence, db);
                out.write(refseqid+"\t"+cloneid+"\n");
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void analyzeByBlast(String input, String output) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(input));
            OutputStreamWriter out = new FileWriter(output);
            out.write("ID\tPercentid\tAlength\tMismatch\tGap\tEvalue\tScore\tqStart\tqEnd\tsStart\tsEnd\n");

            BlastWrapper blaster = new BlastWrapper();
            blaster.setLowcomp("no");
            //blaster.setWordsize(5);
            //blaster.setExpect(1000);
            blaster.setAlignmentview(1);
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                //ID    queryid	Plasmidcloneseq_Sequence    subjectid	ReferenceProtein_Sequence
                String[] column = line.split("\t", 5);
                String queryid = column[1];
                String cloneseq = column[2];
                String subid = column[3];
                String proteinSeq = column[4];
                System.out.println(column[0] + "\t" + queryid + "\t" + subid);
                if (cloneseq == null || cloneseq.length() == 0 || proteinSeq == null || proteinSeq.length() == 0) {
                    continue;
                }
                String inputfile1 = makeQueryFile(queryid, cloneseq);
                String inputfile2 = makeQueryFile(subid, proteinSeq);
                String outputfile = BLAST_FILE_PATH + "blastout.txt";
                blaster.setInput(inputfile1);
                blaster.setInput2(inputfile2);
                blaster.setBl2seqOutput(outputfile);
                String cmd = blaster.getBl2seqCmd(BLAST_PROGRAM_PATH);
                //String cmd = blaster.getBlastxCmd(BLAST_PROGRAM_PATH);
                System.out.println(cmd);
                blaster.executeBlast(cmd);

                BlastParser parser = new BlastParser(outputfile);
                parser.setAlength(0);
                parser.setPid(0);
                parser.parseTabularOutput();
                List infos = parser.getInfos();
                if (infos == null || infos.size() == 0) {
                    continue;
                }

                BlastHit hit = (BlastHit) infos.get(0);
                List blastInfos = hit.getBlastinfos();
                if (blastInfos == null || blastInfos.size() == 0) {
                    continue;
                }
                BlastInfo info = (BlastInfo) blastInfos.get(0);
                //int mismatch = info.getMismatch() + info.getGap();
                out.write(column[0] + "\t" + info.getPid()
                        + "\t" + info.getAlength()
                        + "\t" + info.getMismatch()
                        + "\t" + info.getGap()
                        + "\t" + info.getEvalue()
                        + "\t" + info.getScore()
                        + "\t" + info.getQstart()
                        + "\t" + info.getQend()
                        + "\t" + info.getSstart()
                        + "\t" + info.getSend()
                        + "\n");
                //blaster.delete(inputfile1);
                //blaster.delete(inputfile2);
                //blaster.delete(outputfile);
            }

            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String makeQueryFile(String id, String sequence) throws Exception {
        String file = BLAST_FILE_PATH + id;
        FileWriter out = new FileWriter(new File(file));
        out.write(">" + id + "\n");
        out.write(sequence);
        out.close();
        return file;
    }

    public void analyzeByStringCompare(String input, String output) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(input));
            OutputStreamWriter out = new FileWriter(output);
            out.write("ID\tMatch\n");
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                //ID	Plasmidcloneseq_Sequence	queryclonesequence_Sequence	CDS Start	CDS Stop	ReferenceProtein_Sequence
                String[] column = line.split("\t", 3);
                String cloneseq = column[1].trim().toLowerCase();
                if (cloneseq.length() == 0) {
                    continue;
                }
                String queryseq = column[2].trim().toLowerCase();
                if (queryseq.length() == 0) {
                    continue;
                }
                String cloneseq1 = cloneseq.substring(0, cloneseq.length() - 3);
                //String cloneseq1 = cloneseq;
                String queryseq1 = queryseq.substring(0, queryseq.length() - 3);
                if (queryseq1.equals(cloneseq1)) {
                    out.write(column[0] + "\tnt\n");
                }
            }
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void generateFastaDatabase(String input, String output) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line = in.readLine();
        List<Dnasequence> seqs = new ArrayList<Dnasequence>();
        int n = 0;
        while ((line = in.readLine()) != null) {
            n++;
            if(n%1000==0) {
                makeFastaDatabase(seqs, output, true);
                seqs = new ArrayList<Dnasequence>();
            }
            StringTokenizer st = new StringTokenizer(line, "\t");
            int id = Integer.parseInt(st.nextToken());
            String seq = st.nextToken();
            Dnasequence sequence = new Dnasequence();
            sequence.setReferenceid(id);
            sequence.setSequence(seq);
            seqs.add(sequence);
            System.out.println(n+": "+id);
        }
        
        if(seqs.size()>0) {
            makeFastaDatabase(seqs, output, true);
        }
    }

    public void makeFastaDatabase(List<Dnasequence> sequences, String file, boolean append) throws Exception {
        FileWriter out = new FileWriter(new File(file), append);
        for (Dnasequence seq : sequences) {
            out.write(">" + seq.getReferenceid() + "\n");
            out.write(Dnasequence.convertToFasta(seq.getSequence()) + "\n");
        }
        out.close();
    }

    public List<Dnasequence> readSequences(String file) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line = in.readLine();
        List<Dnasequence> seqs = new ArrayList<Dnasequence>();
        while ((line = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, "\t");
            int id = Integer.parseInt(st.nextToken());
            String seq = st.nextToken();
            Dnasequence sequence = new Dnasequence();
            sequence.setReferenceid(id);
            sequence.setSequence(seq);
            seqs.add(sequence);
        }
        return seqs;
    }

    public void constructCloneSeq(String input, String output) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(input));
        FileWriter out = new FileWriter(new File(output));
        out.write("ID\tSequence\n");
        String line = in.readLine();
        
        while ((line = in.readLine()) != null) {
            String [] s = line.split("\t");
            int id = Integer.parseInt(s[0]);
            int index = Integer.parseInt(s[1])-1;
            String seq = s[3].substring(0, index)+s[2]+s[3].substring(index+1);
            out.write(id+"\t"+seq+"\n");
        }
        
        in.close();
        out.close();
    }
    
    public static void main(String args []) {
        String input = "C:\\dev\\plasmid_support\\PlasmidAnalysis_201203\\input_for_stringcompare.txt";
        String output = "C:\\dev\\plasmid_support\\PlasmidAnalysis_201203\\output_for_stringcompare.txt";
        String blastinput = "C:\\dev\\plasmid_support\\OC_missing\\analysis_input.txt";
        String blastoutput = "C:\\dev\\plasmid_support\\OC_missing\\analysis_output.txt";

        String refseqfile = "C:\\dev\\plasmid_support\\blastdb\\Clonesequence.txt";
        String refseqFastaFile = "C:\\dev\\plasmid_support\\blastdb\\plasmid_all";

        String blastRefseqInput = "C:\\dev\\plasmid_support\\OC_missing\\cloneseq_noref.txt";
        String blastRefseqOutput = "C:\\dev\\plasmid_support\\OC_missing\\matchingRefseq.txt";
        
        String cloneseqInput = FILE_PATH+"ORFeome8\\Input_for_clone_seq.txt";
        String cloneseqOutput = FILE_PATH+"ORFeome8\\Output_for_clone_seq.txt";
        
        SequenceAnalyzer analyzer = new SequenceAnalyzer();
        //analyzer.analyzeByStringCompare(input, output);
        analyzer.analyzeByBlast(blastinput, blastoutput);
        
        /**
        try {
            analyzer.constructCloneSeq(cloneseqInput, cloneseqOutput);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
         */
        
        /**
        try {
            List<Dnasequence> seqs = analyzer.readSequences(refseqfile);
            analyzer.makeFastaDatabase(seqs, refseqFastaFile, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
         */
        
        /**
        try {
            analyzer.generateFastaDatabase(refseqfile, refseqFastaFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
         */
        
        //analyzer.findMatchingRefseq(blastRefseqInput, blastRefseqOutput, FILE_PATH+"refseqAll.fasta");
        
        System.exit(0);
    }
}
