
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import plasmid.blast.BlastHit;
import plasmid.blast.BlastInfo;
import plasmid.blast.BlastParser;
import plasmid.blast.BlastWrapper;

/**
 *
 * @author Dongmei
 */
public class SequenceAnalyzer {

    public static final String BLAST_PROGRAM_PATH = "C:\\blast-2.2.24\\bin\\";
    public static final String BLAST_FILE_PATH = "C:\\dev\\andreas\\blast\\";

    public void analyzeByBlast(String input, String output) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(input));
            OutputStreamWriter out = new FileWriter(output);
            out.write("ID\tMismatch\tAlength\tProteinLength\n");

            BlastWrapper blaster = new BlastWrapper();
            blaster.setProgram("blastx");
            blaster.setIsMegablast("F");
            blaster.setIsLowcomp("F");
            blaster.setAlignmentview(1);
            String line = in.readLine();
            while ((line = in.readLine()) != null) {
                //ID    queryid	Plasmidcloneseq_Sequence    subjectid	ReferenceProtein_Sequence
                String[] column = line.split("\t");
                String queryid = column[1];
                String cloneseq = column[2];
                String subid = column[3];
                String proteinSeq = column[4];
                System.out.println(column[0]+"\t"+queryid+"\t"+subid);
                if(cloneseq==null || cloneseq.length()==0 || proteinSeq==null || proteinSeq.length()==0) {
                    continue;
                }
                String inputfile1 = makeQueryFile(queryid, cloneseq);
                String inputfile2 = makeQueryFile(subid, proteinSeq);
                String outputfile = BLAST_FILE_PATH + "blastout.txt";
                blaster.setInput(inputfile1);
                blaster.setInput2(inputfile2);
                blaster.setBl2seqOutput(outputfile);
                String cmd = blaster.getBl2seqCmd(BLAST_PROGRAM_PATH);
                //System.out.println(cmd);
                blaster.executeBlast(cmd);

                BlastParser parser = new BlastParser(outputfile);
                parser.setAlength(0);
                parser.setPid(0);
                parser.parseTabularOutput();
                List infos = parser.getInfos();
                if(infos==null || infos.size()==0)
                    continue;
                
                BlastHit hit = (BlastHit) infos.get(0);
                List blastInfos = hit.getBlastinfos();
                if(blastInfos==null || blastInfos.size()==0) 
                    continue;
                BlastInfo info = (BlastInfo) blastInfos.get(0);
                int mismatch = info.getMismatch() + info.getGap();
                out.write(column[0] + "\t" + mismatch + "\t" + info.getAlength() + "\t" + proteinSeq.length() + "\n");
                
                blaster.delete(inputfile1);
                blaster.delete(inputfile2);
                blaster.delete(outputfile);
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
                String[] column = line.split("\t");
                String cloneseq = column[1].trim().toLowerCase();
                if (cloneseq.length() == 0) {
                    continue;
                }
                //String cloneseq1 = cloneseq.substring(0, cloneseq.length() - 3);
                String cloneseq1 = cloneseq;
                String queryseq = column[2].trim().toLowerCase();
                System.out.println(cloneseq1 + "," + queryseq);
                if (queryseq.indexOf(cloneseq1) >= 0) {
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

    public static void main(String args[]) {
        String input = "C:\\dev\\plasmidsupport\\Orfeome201103\\InputForStringCompare.txt";
        String output = "C:\\dev\\plasmidsupport\\Orfeome201103\\outputforstringcompare.txt";
        String blastinput = BLAST_FILE_PATH + "blastinput_1.txt";
        String blastoutput = BLAST_FILE_PATH + "blastoutput_1.txt";

        SequenceAnalyzer analyzer = new SequenceAnalyzer();
        analyzer.analyzeByStringCompare(input, output);
        //analyzer.analyzeByBlast(blastinput, blastoutput);

        System.exit(0);
    }
}
