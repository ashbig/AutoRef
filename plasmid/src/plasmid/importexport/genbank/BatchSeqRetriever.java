/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.importexport.genbank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dongmei
 */
public class BatchSeqRetriever {

    public static final String BASEURL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi";
    public static final String CDS_NT = "fasta_cds_na";
    public static final String CDS_AA = "fasta_cds_aa";
    public static final String TYPE_NT = "nt";
    public static final String TYPE_AA = "aa";
    public static final String TYPE_CDS = "cds";
    public static final String TYPE_GB = "gb";
    
    public String retriveSequence(List<String> gis, String type) {
        String url = getUrl(type);
        int i = 0;
        for (String gi : gis) {
            if (i == 0) {
                url += gi;
            } else {
                url += "," + gi;
            }
            i++;
        }

        String output = "";
        try {
            URL genbank = new URL(url);
            URLConnection yc = genbank.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                output += inputLine+"\n";
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return output;
    }

    private String getUrl(String type) {
        if(TYPE_CDS.equals(type))
            return BASEURL + "?db=nuccore&rettype=" + CDS_NT + "&retmode=text&id=";
        else if(TYPE_NT.equals(type))
            return BASEURL + "?db=nucleotide&rettype=fasta&retmode=text&id=";
        else if(TYPE_AA.equals(type))
            return BASEURL + "?db=protein&rettype=fasta&retmode=text&id=";
        else
            return BASEURL + "?db=nuccore&rettype=" + TYPE_GB + "&retmode=text&id=";
    }
    public List<String> readInput(String filename) throws Exception {
        List<String> gis = new ArrayList();
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line = null;
        while ((line = in.readLine()) != null) {
            if (line.trim().length() > 0) {
                gis.add(line.trim());
            }
        }

        in.close();

        return gis;
    }

    public static void main(String args[]) {
        String input = "C:\\dev\\plasmid_support\\Gene_20130402\\PlasmidAnalysis\\giforcds.txt";
        String output = "C:\\dev\\plasmid_support\\Gene_20130402\\PlasmidAnalysis\\cds2.fasta";

        BatchSeqRetriever bsr = new BatchSeqRetriever();
        try {
            PrintWriter out = new PrintWriter(new File(output));
            List<String> gis = bsr.readInput(input);
            int n = 0;
            List<String> l = new ArrayList<String>();
            for (String gi : gis) {
                System.out.println("gi:"+gi);
                l.add(gi);
                n++;
                if (n == 1) {
                    String s = bsr.retriveSequence(l, BatchSeqRetriever.TYPE_CDS);
                    out.println(s);
                    n = 0;
                    l = new ArrayList<String>();
                }
            }
            if (!l.isEmpty()) {
                String s = bsr.retriveSequence(l, BatchSeqRetriever.TYPE_CDS);
                out.println(s);
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}
