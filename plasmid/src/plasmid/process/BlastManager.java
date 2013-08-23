/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import plasmid.blast.BlastHit;
import plasmid.blast.BlastParser;
import plasmid.blast.BlastProgram;
import plasmid.blast.BlastWrapper;
import plasmid.coreobject.Clone;
import plasmid.coreobject.CloneAnalysis;
import plasmid.coreobject.Dnasequence;
import plasmid.database.DatabaseManager.CloneManager;
import plasmid.database.DatabaseTransaction;
import plasmid.query.coreobject.CloneInfo;
import plasmid.util.StringConvertor;

/**
 *
 * @author DZuo
 */
public class BlastManager {

    public static final String INPUT_SEQUENCE = "Sequence";
    public static final String INPUT_ID = "Accession or GI";

    public static List getPrograms() {
        List programs = new ArrayList();
        programs.add(new BlastProgram(BlastWrapper.PROGRAM_BLASTN, BlastWrapper.PROGRAM_DISPLAY_BLASTN));
        programs.add(new BlastProgram(BlastWrapper.PROGRAM_TBLASTN, BlastWrapper.PROGRAM_DISPLAY_TBLASTN));
        programs.add(new BlastProgram(BlastWrapper.PROGRAM_TBLASTX, BlastWrapper.PROGRAM_DISPLAY_TBLASTX));
        return programs;
    }

    public static List getDatabases() {
        List dbs = new ArrayList();
        dbs.add(BlastWrapper.DATABASE_ALL);
        return dbs;
    }

    public static List getMaxseqs() {
        List seqs = new ArrayList();
        seqs.add("10");
        seqs.add("25");
        seqs.add("50");
        seqs.add("100");
        return seqs;
    }

    public static List getInputFormats() {
        List formats = new ArrayList();
        formats.add(INPUT_SEQUENCE);
        formats.add(INPUT_ID);
        return formats;
    }

    public String fetchNCBIseqs(String ids) throws Exception {
        String s = parseIds(ids);
        URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&rettype=fasta&id=" + s);
        URLConnection c = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                c.getInputStream()));
        String seqs = "";
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            seqs += inputLine + "\n";
        }
        in.close();

        return seqs;
    }

    public String parseIds(String ids) throws Exception {
        StringTokenizer st = new StringTokenizer(ids);
        List l = new ArrayList();
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            l.add(s);
        }

        return StringConvertor.convertFromListToSqlList(l).replaceAll(" ", "");
    }

    public List runBlast(String program, String database, String sequence,
            double pid, int alength, boolean isLowcomp) throws Exception {
        String queryfile = makeQueryFile(sequence);
        String outputfile = queryfile + ".out";

        BlastWrapper blaster = new BlastWrapper(program, BlastWrapper.BLAST_DB_PATH + database, queryfile, outputfile);
        blaster.setIsLowcomp(isLowcomp);
        List infos = null;

        try {
            blaster.runBlast();
            BlastParser parser = new BlastParser(outputfile);
            parser.setAlength(alength);
            parser.setPid(pid);
            parser.parseTabularOutput();
            infos = parser.getInfos();
            blaster.delete(queryfile);
            blaster.delete(outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("blast error occured.\n" + ex.getMessage());
        }

        return infos;
    }

    public List getFoundClones(List infos, List restrictions, List clonetypes, String species) throws Exception {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Cannot get dabase connection.");
        }

        CloneManager manager = new CloneManager(conn);
        Set cloneids = new TreeSet();
        for (int i = 0; i < infos.size(); i++) {
            BlastHit info = (BlastHit) infos.get(i);
            String cloneid = info.getSubjectid();
            cloneids.add(cloneid);
        }
        Map foundClones = manager.queryClonesByCloneid(new ArrayList(cloneids), true, true, false, true, restrictions, clonetypes, species, Clone.AVAILABLE, false, true);

        try {
            DatabaseTransaction.closeConnection(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List found = new ArrayList();
        for (int i = 0; i < infos.size(); i++) {
            BlastHit info = (BlastHit) infos.get(i);
            String cloneid = info.getSubjectid();
            CloneInfo clone = (CloneInfo) foundClones.get(cloneid);
            if (clone != null) {
                info.setCloneinfo(clone);
                info.setTerm(info.getQueryid());
                found.add(info);
            }
        }
        return found;
    }

    public String runBl2seq(String program, String id1, String seq1, String id2, String seq2,
            boolean isLowcomp, String output, int outputformat, boolean isDelete)
            throws Exception {
        String file1 = makeQueryFile(id1, seq1);
        String file2 = makeQueryFile(id2, seq2);

        BlastWrapper blaster = new BlastWrapper();
        blaster.setProgram(program);
        blaster.setInput(file1);
        blaster.setInput2(file2);
        blaster.setOutput(output);
        blaster.setIsLowcomp(isLowcomp);
        blaster.setAlignmentview(outputformat);

        String s = "";
        try {
            blaster.runBlast2Seq();

            String line = null;
            BufferedReader in = new BufferedReader(new FileReader(output));
            while ((line = in.readLine()) != null) {
                s += line + "\n";
            }
            in.close();
            blaster.delete(file1);
            blaster.delete(file2);
            if (isDelete) {
                blaster.delete(output);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("blast error occured.\n" + ex.getMessage());
        }

        return s;
    }

    public String runBl2seq(String program, String id1, String seq1, String id2, String seq2, boolean isLowcomp) throws Exception {
        return runBl2seq(program, id1, seq1, id2, seq2, isLowcomp, BlastWrapper.BLAST_FILE_PATH + "out", BlastWrapper.PAIRWISE_OUTPUT, true);
    }

    public String runPairwiseBlast(String queryid, String queryseq, String subid, String subseq, String type)
            throws IOException, ProcessException, Exception {
        String output = BlastWrapper.BLAST_FILE_PATH + queryid + "_" + subid + "_" + type;
        BlastWrapper blaster = new BlastWrapper();
        String inputfile1 = makeQueryFile(queryid, queryseq);
        String inputfile2 = makeQueryFile(subid, subseq);
        blaster.setInput(inputfile1);
        blaster.setInput2(inputfile2);
        blaster.setOutput(output);
        blaster.setAlignmentview(BlastWrapper.PAIRWISE_OUTPUT);
        if (CloneAnalysis.TYPE_AA.equals(type)) {
            blaster.setProgram(BlastWrapper.PROGRAM_BLASTX);
        }
        blaster.runBlast2Seq();
        blaster.delete(inputfile1);
        blaster.delete(inputfile2);

        BufferedReader in = new BufferedReader(new FileReader(output));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine).append("\n");
        }
        in.close();

        blaster.delete(output);
        return sb.toString();
    }

    public String makeQueryFile(String sequence) throws ProcessException {
        return makeQueryFile("query", sequence);
    }

    public String makeQueryFile(String id, String sequence) throws ProcessException {
        Random generator = new Random();
        int i = generator.nextInt();
        String file = BlastWrapper.BLAST_FILE_PATH + i;
        try {
            FileWriter out = new FileWriter(new File(file));
            if (id != null) {
                out.write(">" + id + "\n");
            }
            out.write(sequence);
            out.close();
        } catch (Exception ex) {
            throw new ProcessException("Cannot make query file: " + id);
        }
        return file;
    }

    public String getQuerySequence(String queryid, String sequence) {
        StringTokenizer st = new StringTokenizer(sequence, ">");
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (s.indexOf(queryid) != -1) {
                return ">" + s;
            }
        }
        return sequence;
    }

    public String getCloneSequenceWithBlastHeader(int cloneid, String clonename) {
        return ">PlasmID|" + clonename + "\n" + this.getCloneSequence(cloneid);
    }

    public String getCloneSequence(int cloneid) {
        CloneManager m = new CloneManager();
        String s = m.queryCloneSequenceByCloneid(cloneid);
        return Dnasequence.convertToFasta(s);
    }

    public String getReferenceSequence(int cloneid) {
        CloneManager m = new CloneManager();
        String s = m.queryReferenceSequenceByCloneid(cloneid);
        return Dnasequence.convertToFasta(s);
    }

    public static boolean isAminoAcidSeq(String aa) {

        String str = aa;


        if (str.matches(".*(b|[d-f]|h|i|[k-n]|[p-s]|v|w|y|z|B|[D-F]|H|I|[K-N]|[P-S]|V|W|Y|Z).*")) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isNucleotideSeq(String nt) {

        String str = nt;
        if (str.matches(".*(a|c|g|t|u|n|A|C|G|T|U|N).*")) {
            return true;
        } else {
            return false;
        }

    }
}
