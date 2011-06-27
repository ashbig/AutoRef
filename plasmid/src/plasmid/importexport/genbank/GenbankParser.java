/*
 * GenbankParser.java
 *
 * Created on August 3, 2005, 11:40 AM
 */
package plasmid.importexport.genbank;

import java.net.*;
import java.io.*;
import java.util.*;
import plasmid.util.StringConvertor;
import plasmid.coreobject.Publication;

/**
 *
 * @author  DZuo
 */
public class GenbankParser {

    /** Creates a new instance of GenbankParser */
    public GenbankParser() {
    }

    public List getGenbankRecords(List ids) throws Exception {
        //for nt
        //String urlString = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&rettype=gb&retmode=text&id=";
        //for protein
        String urlString = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=protein&rettype=gb&retmode=text&id=";
        
        StringConvertor sc = new StringConvertor();
        urlString += sc.convertFromListToString(ids);
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        List l = parseGenbanks(in);
        in.close();
        return l;
    }

    public List parseGenbanksOffline(String filename) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        List l = parseGenbanks(in);
        in.close();
        return l;
    }
    
    public List parseGenbanks(BufferedReader in) throws Exception {
        List genbanks = new ArrayList();

        String line = null;
        String definition = "";
        String accession = "";
        String accessionVersion = "";
        String gi = "";
        String organism = "";
        String geneid = "";
        String genesymbol = "";
        int cdsstart = -1;
        int cdsstop = -1;
        String sequencetext = "";
        boolean isSeqStart = false;
        int cdscount = 0;
        boolean breakSeq = false;
        boolean definitionStop = false;
        List names = new ArrayList();
        List publications = new ArrayList();

        int attL1Start = -1;
        int attL1Stop = -1;
        int attL2Start = -1;
        int attL2Stop = -1;
        int linker5pStart = -1;
        int linker5pStop = -1;
        int linker3pStart = -1;
        int linker3pStop = -1;

        while ((line = in.readLine()) != null) {
            //System.out.println(line);
            if (line.trim().length() <= 0) {
                continue;
            }

            //ACCESSION   NM_026689
            if (line.trim().indexOf("ACCESSION") == 0 && accession.equals("")) {
                definitionStop = true;
                StringTokenizer tokens = new StringTokenizer(line);
                if (tokens.hasMoreTokens()) {
                    String ignore = tokens.nextToken();
                    accession = tokens.nextToken();
                }
            }
            //DEFINITION  Mus musculus RIKEN cDNA 0610009K11 gene (0610009K11Rik), mRNA.
            if (!definitionStop) {
                if (line.trim().indexOf("DEFINITION") == 0) {
                    definition = line.trim().substring(line.indexOf("DEFINITION") + 10).trim();
                } else {
                    definition += " " + line.trim();
                }
            }
            //VERSION     NM_026689.3  GI:40548428
            if (line.indexOf("VERSION") == 0 && accessionVersion.equals("")) {
                StringTokenizer st = new StringTokenizer(line);
                String ignore = st.nextToken();
                accessionVersion = st.nextToken();
                ignore = st.nextToken();
                st = new StringTokenizer(ignore, ":");
                ignore = st.nextToken();
                gi = st.nextToken();
            }
            //PUBMED   17207965
            if (line.indexOf("PUBMED") >= 0) {
                StringTokenizer st = new StringTokenizer(line);
                String ignore = st.nextToken();
                String pmid = st.nextToken();
                Publication p = new Publication();
                p.setPmid(pmid);
                publications.add(p);
            }
            //                     /organism="Mus musculus"
            if (line.trim().indexOf("/organism=") != -1) {
                organism = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
            }
            //                     /db_xref="GeneID:9948"
            if (line.indexOf("/db_xref=\"GeneID:") != -1 && geneid.equals("")) {
                geneid = line.substring(line.indexOf(":") + 1, line.lastIndexOf("\""));
            }
            //                     /gene="0610009K11Rik"
            if (line.indexOf("/gene=") != -1 && genesymbol.equals("")) {
                genesymbol = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
            }
            //                     /clone="IMAGE:3505128" or /clone="MGC:3308 IMAGE:3509626"
            if (line.indexOf("/clone=") != -1 && genesymbol.equals("")) {
                String s = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                StringTokenizer st = new StringTokenizer(s, "; ");
                while (st.hasMoreTokens()) {
                    String s1 = st.nextToken();
                    try {
                        StringTokenizer st1 = new StringTokenizer(s1, ":");
                        String nametype = st1.nextToken();
                        String namevalue = st1.nextToken();
                        GenbankInfoName name = new GenbankInfoName(nametype, namevalue);
                        names.add(name);
                    } catch (Exception ex) {
                    }
                }
            }

            //misc_feature    1..35    
           if (line.indexOf("misc_feature") >= 0) {
                StringTokenizer st = new StringTokenizer(line.trim());
                String ignore = st.nextToken();
                String cds = st.nextToken();
                st = new StringTokenizer(cds, "..");
                int start = Integer.parseInt(st.nextToken());
                int stop = Integer.parseInt(st.nextToken());
                if (attL1Start == -1 && attL1Stop == -1) {
                    attL1Start = start;
                    attL1Stop = stop;
                } else if (linker5pStart == -1 && linker5pStop == -1) {
                    linker5pStart = start;
                    linker5pStop = stop;
                } else if (linker3pStart == -1 && linker3pStop == -1) {
                    linker3pStart = start;
                    linker3pStop = stop;
                } else {
                    attL2Start = start;
                    attL2Stop = stop;
                }
            }
            //     CDS             83..1903
            if (line.indexOf("     CDS") == 0) {
                cdscount++;

                //genomic
                if (cdscount > 1) {
                    cdsstart = -1;
                    cdsstop = -1;
                    break;
                }
                //genomic
                if (line.indexOf("join") != -1 || line.indexOf("complement") != -1) {
                    continue;
                }

                StringTokenizer st = new StringTokenizer(line.trim());
                String cds = "";
                if (st.hasMoreTokens()) {
                    String ignore = st.nextToken();
                    cds = st.nextToken();
                }
                st = new StringTokenizer(cds, "..");
                if (st.hasMoreTokens()) {
                    String startStr = st.nextToken();
                    String newStart = startStr;
                    //missing start
                    if (startStr.indexOf("<") != -1) {
                        //newStart = startStr.substring(startStr.indexOf(";")+1);
                        newStart = "-2";
                    }

                    String stopStr = st.nextToken();
                    String newStop = stopStr;
                    //missing stop
                    if (stopStr.indexOf(">") != -1) {
                        //newStop = "-2";
                        newStop = stopStr.substring(1);
                    }
                    cdsstart = Integer.parseInt(newStart);
                    cdsstop = Integer.parseInt(newStop);
                }
            }
            //ORIGIN
            //1 tcgacgggcg acgggttccg ggaagggctg cgcggctgcg taggggtcgc aggtgatttc
            if (line.indexOf("ORIGIN") == 0) {
                isSeqStart = true;
                breakSeq = true;
                continue;
            }

            //end of a record
            if (line.indexOf("//") == 0 && breakSeq) {
                System.out.println("Genbank: " + gi);
                GenbankInfo info = new GenbankInfo(gi, definition, accession, accessionVersion, gi, organism, geneid, genesymbol, cdsstart, cdsstop, sequencetext);
                info.setNames(names);
                info.setPublications(publications);
                info.setAttL1Start(attL1Start);
                info.setAttL1Stop(attL1Stop);
                info.setAttL2Start(attL2Start);
                info.setAttL2Stop(attL2Stop);
                info.setLinker3pStart(linker3pStart);
                info.setLinker3pStop(linker3pStop);
                info.setLinker5pStart(linker5pStart);
                info.setLinker5pStop(linker5pStop);
                genbanks.add(info);

                definition = "";
                accession = "";
                accessionVersion = "";
                gi = "";
                organism = "";
                geneid = "";
                genesymbol = "";
                cdsstart = -1;
                cdsstop = -1;
                sequencetext = "";
                isSeqStart = false;
                cdscount = 0;
                breakSeq = false;
                definitionStop = false;
                names = new ArrayList();

                publications = new ArrayList();
                attL1Start = -1;
                attL1Stop = -1;
                attL2Start = -1;
                attL2Stop = -1;
                linker5pStart = -1;
                linker5pStop = -1;
                linker3pStart = -1;
                linker3pStop = -1;
            }

            if (isSeqStart) {
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    sequencetext = sequencetext + st.nextToken(" 1234567890");
                }
            }
        }

        return genbanks;
    }
}
