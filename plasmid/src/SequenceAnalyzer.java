
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import plasmid.blast.BlastHit;
import plasmid.blast.BlastInfo;
import plasmid.blast.BlastParser;
import plasmid.blast.BlastWrapper;
import plasmid.coreobject.Dnasequence;
import plasmid.database.DatabaseTransaction;

/**
 *
 * @author Dongmei
 */
public class SequenceAnalyzer {

    public static final String BLAST_PROGRAM_PATH = "C:\\NCBI\\blast-2.2.28+\\bin\\";
    public static final String BLAST_FILE_PATH = "D:\\dev\\blast\\tmp\\";
    public static final String FILE_PATH = "D:\\wade\\Interactome\\analysis\\";
    public static final String BLASTABLE_DB_PATH = "D:\\dev\\blast\\db\\";

    public String getBestMatchByBlast(Dnasequence sequence, String db) {
        try {
            BlastWrapper blaster = new BlastWrapper();
            String outputfile = BLAST_FILE_PATH + sequence.getReferenceid() + "_out.txt";
            blaster.setOutput(outputfile);
            String inputfile = makeQueryFile("" + sequence.getReferenceid(), sequence.getSequence());
            blaster.setInput(inputfile);
            blaster.setDatabase(db);
            //default set to yes
            blaster.setIsLowcomp(true);
            blaster.setProgram(BlastWrapper.PROGRAM_BLASTN);
            blaster.runBlast();

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
            out.write("SubjectID\tQueryID\n");

            String line = in.readLine();
            //queryid\tsequence
            while ((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                int queryid = Integer.parseInt(st.nextToken());
                String seq = st.nextToken();
                Dnasequence sequence = new Dnasequence();
                sequence.setReferenceid(queryid);
                sequence.setSequence(seq);
                String refseqid = getBestMatchByBlast(sequence, db);
                out.write(refseqid + "\t" + queryid + "\n");
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
            out.write("ID\tCloneid\tRefseqGI\tPercentid\tAlength\tMismatch\tGap\tEvalue\tScore\tqStart\tqEnd\tsStart\tsEnd\n");

            BlastWrapper blaster = new BlastWrapper();
            blaster.setIsLowcomp(true);
            //blaster.setLowcomp("no");
            //blaster.setWordsize(5);
            //blaster.setExpect(1000);
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                //ID    queryid	Plasmidcloneseq_Sequence    subjectid	ReferenceProtein_Sequence
                String[] column = line.split("\t", 5);
                String id = column[0];
                String queryid = column[1];
                String cloneseq = column[2];
                String subid = column[3];
                String proteinSeq = column[4];
                System.out.println(id);
                if (cloneseq == null || cloneseq.length() == 0 || proteinSeq == null || proteinSeq.length() == 0) {
                    continue;
                }
                String inputfile1 = makeQueryFile(queryid, cloneseq);
                String inputfile2 = makeQueryFile(subid, proteinSeq);
                String outputfile = BLAST_FILE_PATH + "blastout.txt";
                blaster.setInput(inputfile1);
                blaster.setInput2(inputfile2);
                blaster.setOutput(outputfile);
                blaster.setProgram(BlastWrapper.PROGRAM_BLASTN);
                blaster.runBlast2Seq();

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
                out.write(id + "\t" + queryid + "\t" + subid + "\t" + info.getPid()
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
                //System.out.println(cloneseq);
                if (cloneseq.length() == 0) {
                    continue;
                }
                String queryseq = column[2].trim().toLowerCase();
                //System.out.println(queryseq);
                if (queryseq.length() == 0) {
                    continue;
                }
                //String cloneseq1 = cloneseq.substring(0, cloneseq.length() - 3);
                String cloneseq1 = cloneseq;
                String queryseq1 = queryseq.substring(0, queryseq.length() - 3);
                if (queryseq1.equalsIgnoreCase(cloneseq1)) {
                    out.write(column[0] + "\tnt\n");
                }
                //System.out.println("done:"+column[0]);
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
            if (n % 1000 == 0) {
                makeFastaDatabase(seqs, output, true);
                seqs = new ArrayList<Dnasequence>();
            }
            String[] s = line.split("\t");
            //cloneid   sequence
            String id = s[0];
            String seq = s[1];
            //accession taxid   geneid  symbol  description seq
            //String id = "Accession:"+s[0]+"|taxid:"+s[1]+"|geneid:"+s[2]+"|symbol:"+s[3]+"|"+s[4];
            //String seq = s[5];
            Dnasequence sequence = new Dnasequence();
            sequence.setType(id);
            sequence.setSequence(seq);
            seqs.add(sequence);
            System.out.println(n + ": " + id);
        }

        if (seqs.size() > 0) {
            makeFastaDatabase(seqs, output, true);
        }
    }

    public void makeFastaDatabase(List<Dnasequence> sequences, String file, boolean append) throws Exception {
        FileWriter out = new FileWriter(new File(file), append);
        for (Dnasequence seq : sequences) {
            out.write(">" + seq.getType() + "\n");
            out.write(Dnasequence.convertToFasta(seq.getSequence()) + "\n\n");
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
            String[] s = line.split("\t");
            int id = Integer.parseInt(s[0]);
            int index = Integer.parseInt(s[1]) - 1;
            String seq = s[3].substring(0, index) + s[2] + s[3].substring(index + 1);
            out.write(id + "\t" + seq + "\n");
        }

        in.close();
        out.close();
    }

    public void selectClone(String input, String output) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(input));
        String line = in.readLine();
        HashMap info = new HashMap();
        //Geneid\tID\tCoverage\tMutation
        while ((line = in.readLine()) != null) {
            String[] s = line.split("\t");
            info.put(s[0], s);
        }
        in.close();

        Set genes = info.keySet();
        Iterator iter = genes.iterator();
        while (iter.hasNext()) {
            String gene = (String) iter.next();

        }

        FileWriter out = new FileWriter(new File(output));
        out.write("ID\tCoverage\n");
        out.close();
    }

    public void findFailedImageClone(String input, String output) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(input));
        PrintWriter out = new PrintWriter(new FileWriter(new File(output)));
        out.println("Image ID\tspecies\texternal_id\tacc_num\tproblem_type\tcomments\treported_by\tdate_entered\tdate_modified\tClone ID\tClone Name\tSource");

        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        String sql = "select cloneid,clonename,source from clone where cloneid in"
                + " (select cloneid from clonename where namevalue=? and nametype='IMAGE ID')"
                + " and status='AVAILABLE'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;

        String line = null;
        while ((line = in.readLine()) != null) {
            String[] s = line.split("\t");
            String imageid = s[0];
            System.out.println(imageid);
            stmt.setString(1, imageid);
            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                int cloneid = rs.getInt(1);
                String name = rs.getString(2);
                String source = rs.getString(3);
                out.println(line + "\t" + cloneid + "\t" + name + "\t" + source);
            }
            DatabaseTransaction.closeResultSet(rs);
        }
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);

        in.close();
        out.close();
    }

    public void parseSynonyms(String input, String output) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(input));
        FileWriter out = new FileWriter(new File(output));
        out.write("GeneID\tSynonym\n");

        //Geneid\tSynonyms
        String line = in.readLine();
        while ((line = in.readLine()) != null) {
            String[] s = line.split("\t");
            String ss = s[1].replace("|", ":");
            String[] synonyms = ss.split(":");
            for (int i = 0; i < synonyms.length; i++) {
                out.write(s[0] + "\t" + synonyms[i] + "\n");
            }
        }
        in.close();
        out.close();
    }

    public void aggregateField(String input, String output) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(input));
        FileWriter out = new FileWriter(new File(output));
        out.write("GeneID\tGO\n");

        //Geneid\tSynonyms
        String line = in.readLine();
        String geneid = null;
        String go = "";
        while ((line = in.readLine()) != null) {
            String[] s = line.split("\t");
            if (s[0].equals(geneid)) {
                go = go + "|" + s[1];
            } else {
                if (geneid != null) {
                    out.write(geneid + "\t" + go + "\n");
                }
                geneid = s[0];
                go = s[1];
            }
        }
        in.close();
        out.close();
    }

    public static void main(String args[]) {
        String input = "C:\\dev\\plasmid_support\\ccsb_201210\\stringcompare_input.txt";
        String output = "C:\\dev\\plasmid_support\\ccsb_201210\\stringcompare_output.txt";
        String blastinput = SequenceAnalyzer.FILE_PATH + "blast_cds_input.txt";
        String blastoutput = SequenceAnalyzer.FILE_PATH + "blast_cds_output.txt";

        String refseqfile = "D:\\wade\\Interactome\\analysis\\domain_input.txt";
        String refseqFastaFile = "D:\\wade\\Interactome\\analysis\\domain_input_fasta";

        String blastRefseqInput = "D:\\dev\\Gene_20130402\\PlasmidAnalysis\\analysis\\plasmid_human_blast_input6.txt";
        String blastRefseqOutput = "D:\\dev\\Gene_20130402\\PlasmidAnalysis\\analysis\\plasmid_human_analysis_aa2.txt";

        String cloneseqInput = FILE_PATH + "ORFeome8\\Input_for_clone_seq.txt";
        String cloneseqOutput = FILE_PATH + "ORFeome8\\Output_for_clone_seq.txt";

        String failedImageInput = "C:\\dev\\plasmid_support\\production\\platinum_failed_201207\\problemClones_20120808.txt";
        String failedImageOutput = "C:\\dev\\plasmid_support\\production\\platinum_failed_201207\\problemClones_output.txt";

        String synonymInput = "D:\\wade\\Gene_20130402\\data\\synonym_input.txt";
        String synonymOutput = "D:\\wade\\Gene_20130402\\data\\synonyms.txt";
        String goinput = "D:\\wade\\Gene_20130402\\data\\domain_input.txt";
        String gooutput = "D:\\wade\\Gene_20130402\\data\\domain_output.txt";

        SequenceAnalyzer analyzer = new SequenceAnalyzer();
        //analyzer.analyzeByStringCompare(input, output);
        analyzer.analyzeByBlast(blastinput, blastoutput);

        /**
         * try { analyzer.constructCloneSeq(cloneseqInput, cloneseqOutput); }
         * catch (Exception ex) { ex.printStackTrace(); System.exit(1); }
         */
        /**
         * try { List<Dnasequence> seqs = analyzer.readSequences(refseqfile);
         * analyzer.makeFastaDatabase(seqs, refseqFastaFile, false); } catch
         * (Exception ex) { ex.printStackTrace(); System.exit(1); }
        try {
            analyzer.generateFastaDatabase(refseqfile, refseqFastaFile);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
         */

        //analyzer.findMatchingRefseq(blastRefseqInput, blastRefseqOutput, BLASTABLE_DB_PATH+"refseq_human_proteinseq_fasta");
        /**
         * try { analyzer.findFailedImageClone(failedImageInput,
         * failedImageOutput); } catch (Exception ex) { ex.printStackTrace();
         * System.exit(1); }
         */
        /**
         * try { //analyzer.parseSynonyms(synonymInput, synonymOutput);
         * analyzer.aggregateField(goinput, gooutput); } catch (Exception ex) {
         * ex.printStackTrace(); System.exit(1); }
         */
        System.exit(0);
    }
}
