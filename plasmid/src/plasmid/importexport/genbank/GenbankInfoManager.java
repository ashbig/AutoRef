/*
 * GenbankInfoManager.java
 *
 * Created on August 3, 2005, 12:31 PM
 */
package plasmid.importexport.genbank;

import java.io.*;
import java.util.*;
import plasmid.coreobject.Publication;

/**
 *
 * @author  DZuo
 */
public class GenbankInfoManager {

    public static final String GENBANK_ACC = "GenBank Accession";
    public static final String GI = "GI";
    public static final String GENEID = "Gene ID";
    public static final String SYMBOL = "Gene Symbol";
    public static final String GENBANK_URL = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=";
    public static final String GENEID_URL = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=retrieve&dopt=graphics&list_uids=";
    public static final String NA = "NA";
    public static final String FASTA_TYPE_NT = "nt";
    public static final String FASTA_TYPE_AA = "aa";

    /** Creates a new instance of GenbankInfoManager */
    public GenbankInfoManager() {
    }

    public void printGenbankInfo(List infos, OutputStreamWriter f) throws Exception {
        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);
            f.write(info.getTerm() + "\t");

            if (info.getAccession().equals("")) {
                f.write(NA + "\t");
            } else {
                f.write(info.getAccession() + "\t");
            }
            if (info.getAccessionVersion().equals("")) {
                f.write(NA + "\t");
            } else {
                f.write(info.getAccessionVersion() + "\t");
            }
            if (info.getGi().equals("")) {
                f.write(NA + "\t");
            } else {
                f.write(info.getGi() + "\t");
            }
            if (info.getOrganism().equals("")) {
                f.write(NA + "\t");
            } else {
                f.write(info.getOrganism() + "\t");
            }
            if (info.getGeneid().equals("")) {
                f.write(NA + "\t");
            } else {
                f.write(info.getGeneid() + "\t");
            }
            if (info.getGenesymbol().equals("")) {
                f.write(NA + "\t");
            } else {
                f.write(info.getGenesymbol() + "\t");
            }
            if (info.getDefinition().equals("")) {
                f.write(NA + "\t");
            } else {
                f.write(info.getDefinition() + "\t");
            }
            //if (info.getCdsstart() == -1) {
            //    f.write(NA + "\t");
            //} else {
            f.write(info.getCdsstart() + "\t");
            //}
            //if (info.getCdsstop() == -1) {
            //    f.write(NA + "\t");
            // } else {
            f.write(info.getCdsstop() + "\t");
            //}
            f.write(info.getVector() + "\t");
            f.write(info.getAttL1Start() + "\t" + info.getAttL1Stop() + "\t" + info.getAttL2Start() + "\t" + info.getAttL2Stop() + "\t" + info.getLinker5pStart() + "\t" + info.getLinker5pStop() + "\t" + info.getLinker3pStart() + "\t" + info.getLinker3pStop() + "\t");
            if (info.getSequencetext().equals("")) {
                f.write(NA + "\n");
            } else {
                f.write(info.getSequencetext() + "\n");
            }
        }
    }

    public void printGenbankInfo(List infos, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        f.write("Sequence ID\tAccession\tAccession Version\tGI\tOrganism\tGene ID\tSymbol\tDescription\tCDS Start\tCDS Stop\tVector\tattL1Start\tattL1Stop\tattL2Start\tattL2Stop\tLinker5pStart\tLinker5pStop\tLinker3pStart\tLinker3pStop\tSequence\n");
        printGenbankInfo(infos, f);
        f.close();
    }

    public void printGenbankNames(List infos, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        f.write("Sequence ID\tName Type\tName Value\tName URL\n");

        //print Genbank Accession
        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);
            if (info.getAccession() != null && info.getAccession().trim().length() > 0) {
                f.write(info.getTerm() + "\t");
                f.write(GENBANK_ACC + "\t");
                f.write(info.getAccession() + "\t");
                f.write(GENBANK_URL + info.getGi() + "\n");
            }
        }

        //print GI
        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);
            if (info.getGi() != null && info.getGi().trim().length() > 0) {
                f.write(info.getTerm() + "\t");
                f.write(GI + "\t");
                f.write(info.getGi() + "\t");
                f.write(NA + "\n");
            }
        }

        //print Gene ID
        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);
            if (info.getGeneid() != null && info.getGeneid().trim().length() > 0) {
                f.write(info.getTerm() + "\t");
                f.write(GENEID + "\t");
                f.write(info.getGeneid() + "\t");
                f.write(GENEID_URL + info.getGeneid() + "\n");
            }
        }

        //print Gene Symbol
        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);
            if (info.getGenesymbol() != null && info.getGenesymbol().trim().length() > 0) {
                f.write(info.getTerm() + "\t");
                f.write(SYMBOL + "\t");
                f.write(info.getGenesymbol() + "\t");
                f.write(NA + "\n");
            }
        }

        //print other names
        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);
            List names = info.getNames();
            for (int n = 0; n < names.size(); n++) {
                GenbankInfoName name = (GenbankInfoName) names.get(n);
                f.write(info.getTerm() + "\t");
                f.write(name.getNametype() + "\t");
                f.write(name.getNamevalue() + "\t");
                f.write(NA + "\n");
            }
        }

        f.close();
    }

    public void printError(List errorList, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        for (int i = 0; i < errorList.size(); i++) {
            String s = (String) errorList.get(i);
            f.write(s + "\n");
        }
        f.close();
    }

    public void printPublications(List infos, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        f.write("Sequence ID\tPmid\n");

        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);
            List publications = info.getPublications();
            for (int n = 0; n < publications.size(); n++) {
                Publication p = (Publication) publications.get(n);
                f.write(info.getTerm() + "\t");
                f.write(p.getPmid() + "\n");
            }
        }

        f.close();
    }

    public void printGenbankSeqs(List infos, String filename) throws Exception {
        OutputStreamWriter f = new FileWriter(filename);
        f.write("Accession\tSequence\n");

        for (int i = 0; i < infos.size(); i++) {
            GenbankInfo info = (GenbankInfo) infos.get(i);

            f.write(info.getAccession() + "\t");
            f.write(info.getSequencetext() + "\n");
        }
        f.close();
    }

    public List readGenbank(String filename) throws Exception {
        List genbanks = new ArrayList();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line = null;
        while ((line = in.readLine()) != null) {
            if (line.trim().length() > 0) {
                genbanks.add(line.trim());
            }
        }

        in.close();

        return genbanks;
    }

    public void get5pUTR(String infile, String outfile) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(infile));
        OutputStreamWriter out = new FileWriter(outfile);
        out.write("Sequenceid\t5pUTR\n");
        String line = in.readLine();
        while ((line = in.readLine()) != null) {
            if (line.trim().length() > 0) {
                String[] s = line.split("\t");
                String id = s[0];
                int cdsstart = Integer.parseInt(s[8]);
                String seq = s[11];
                if (cdsstart > 0) {
                    String utr = seq.substring(0, cdsstart - 1);
                    out.write(id + "\t" + utr + "\n");
                }
            }
        }
        out.close();
        in.close();
    }

    public void parseFasta(String infile, String outfile, String type) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(infile));
        OutputStreamWriter out = new FileWriter(outfile);
        StringBuilder sb = new StringBuilder();
        String line = null;
        int n = 0;
        while ((line = in.readLine()) != null) {
            if (line.indexOf(">") == 0) {
                n++;
                if (n == 1000) {
                    String s = replaceString(sb, type);
                    out.write(s);
                    sb = new StringBuilder();
                    n = 0;
                }
            }
            sb.append(line.trim());
        }
        in.close();

        String s = replaceString(sb, type);
        out.write(s);
        out.close();
    }

    private String replaceString(StringBuilder sb, String type) {
        String s = sb.toString();
        s = s.replace(">", "\n>");
        s = s.replace("]", "]\t");
        s = s.replace("|", "\t");
        if(FASTA_TYPE_NT.equals(type)) {
            s = s.replace("_cdsid", "\t");
        }
        return s;
    }

    public static void main(String args[]) throws Exception {
        String genbankInput = "C:\\dev\\plasmid_support\\Gene_20130402\\PlasmidAnalysis\\cds3.fasta";
        String referenceFile = "C:\\dev\\plasmid_support\\Gene_20130402\\PlasmidAnalysis\\cds3.txt";
        String referenceNameFile = "C:\\dev\\plasmid_support\\kinase_gateway_201206\\referencename_5.txt";
        String publication = "C:\\dev\\plasmid_support\\ccsb_201210\\publication.txt";
        String sequenceFile = "C:\\dev\\plasmid_support\\PlasmidAnalysis201212\\RIKEN\\missing_sequence.txt";
        String infile = "C:\\dev\\plasmid_support\\Gene_20130402\\Genbank_all.txt";
        String outfile = "C:\\dev\\plasmid_support\\Gene_20130402\\Genbank_utr.txt";
        String fastaInput = "C:\\dev\\plasmid_support\\Gene_20130402\\PlasmidAnalysis\\cds2.fasta";;
        String fastaOutput = "C:\\dev\\plasmid_support\\Gene_20130402\\PlasmidAnalysis\\cds2.txt";;

        GenbankInfoManager manager = new GenbankInfoManager();
        GenbankParser parser = new GenbankParser();
        /**
        List genbanks = manager.readGenbank(genbankInput);
        if (genbanks == null) {
        System.out.println("Error occured while reading input file.");
        System.exit(0);
        }
        try {
        OutputStreamWriter f = new FileWriter(referenceFile);
        f.write("Sequence ID\tAccession\tAccession Version\tGI\tOrganism\tGene ID\tSymbol\tDescription\tCDS Start\tCDS Stop\tattL1Start\tattL1Stop\tattL2Start\tattL2Stop\tLinker5pStart\tLinker5pStop\tLinker3pStart\tLinker3pStop\tSequence\n");
        
        List ids = new ArrayList();
        //int count = 0;
        for (int i = 0; i < genbanks.size(); i++) {
        String genbank = (String) genbanks.get(i);
        ids.add(genbank);
        //count++;
        
        //if (count == 100) {
        List l = parser.getGenbankRecords(ids);
        manager.printGenbankInfo(l, f);
        ids = new ArrayList();
        //count = 0;
        //}
        }
        
        f.close();
        } catch (Exception ex) {
        ex.printStackTrace();
        System.exit(1);
        }*/
        /**
        List infos = new ArrayList();
        try {
        infos = parser.parseGenbanksOffline(genbankInput);
        //infos = parser.parseGenbankSeqsOffline(genbankInput);
        } catch (Exception ex) {
        ex.printStackTrace();
        System.exit(1);
        }
         * */
        manager.parseFasta(fastaInput, fastaOutput, FASTA_TYPE_NT);

        //manager.printGenbankInfo(infos, referenceFile);
        //manager.printGenbankNames(infos, referenceNameFile);
        //manager.printPublications(infos, publication);
        //manager.printGenbankSeqs(infos, sequenceFile);
        //manager.get5pUTR(infile, outfile);

        System.exit(0);
    }
}
