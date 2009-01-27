/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
        URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&rettype=fasta&id="+s);
        URLConnection c = url.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                c.getInputStream()));
        String seqs = "";
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            seqs += inputLine+"\n";
        }
        in.close();
        
        return seqs;
    }
    
    public String parseIds(String ids) throws Exception {
        StringTokenizer st = new StringTokenizer(ids);
        List l = new ArrayList();
        while(st.hasMoreTokens()) {
            String s = st.nextToken();
            l.add(s);
        }
        
        return StringConvertor.convertFromListToSqlList(l).replaceAll(" ", "");
    }
    
    public List runBlast(String program, String database, String sequence,
            double expect,double pid, int alength, 
            boolean isLowcomp, boolean ismaskLowercase, boolean isMegablast) throws Exception {
        String queryfile = makeQueryFile(sequence);
        String outputfile = queryfile+".out";
            
        BlastWrapper blaster = new BlastWrapper(program, BlastWrapper.BLAST_DB_PATH+database, queryfile, outputfile);
        blaster.setExpect(expect);
        blaster.setIsLowcomp(getBooleanValue(isLowcomp));
        blaster.setIsMaskLowercase(getBooleanValue(ismaskLowercase));
        blaster.setIsMegablast(getBooleanValue(isMegablast));
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
            throw new Exception("blast error occured.\n"+ex.getMessage());
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
        for(int i=0; i<infos.size(); i++) {
            BlastHit info = (BlastHit)infos.get(i);
            String cloneid = info.getSubjectid();
            cloneids.add(cloneid);
        }
        Map foundClones = manager.queryClonesByCloneid(new ArrayList(cloneids), true, true, false, true, restrictions, clonetypes, species, Clone.AVAILABLE, true);
        
        try {
            DatabaseTransaction.closeConnection(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        List found = new ArrayList();
        for(int i=0; i<infos.size(); i++) {
            BlastHit info = (BlastHit)infos.get(i);
            String cloneid = info.getSubjectid();
            CloneInfo clone = (CloneInfo)foundClones.get(cloneid);
            if(clone!=null) {
                info.setCloneinfo(clone);
                found.add(info);
            } 
        }
        return found;
    }
            
    public String runBl2seq(String program, String seq1, String seq2,
            double expect, boolean isLowcomp, 
            boolean ismaskLowercase, boolean isMegablast) throws Exception {
        String file1 = makeQueryFile(seq1);
        String file2 = makeQueryFile(seq2);
        String output = BlastWrapper.BLAST_FILE_PATH+"out";
            
        BlastWrapper blaster = new BlastWrapper();
        blaster.setProgram(program);
        blaster.setInput(file1);
        blaster.setInput2(file2);
        blaster.setBl2seqOutput(output);
        //blaster.setMaxseqs(maxseqs);
        blaster.setExpect(expect);
        blaster.setIsLowcomp(getBooleanValue(isLowcomp));
        blaster.setIsMaskLowercase(getBooleanValue(ismaskLowercase));
        blaster.setIsMegablast(getBooleanValue(isMegablast));
        
        String s = "";
        try {
            blaster.runBlast2Seq();
            
            String line = null;
            BufferedReader in = new BufferedReader(new FileReader(output));
            while((line=in.readLine()) != null) {
                s += line+"\n";
            }
            in.close();
            blaster.delete(file1);
            blaster.delete(file2);
            blaster.delete(output);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("blast error occured.\n"+ex.getMessage());
        }
        
        return s;
    }
    
    public String makeQueryFile(String sequence) throws Exception {
        Random generator = new Random();
        int i = generator.nextInt();
        String file = BlastWrapper.BLAST_FILE_PATH+"query"+i;
        FileWriter out = new FileWriter(new File(file));
        out.write(sequence);
        out.close();
        return file;
    }
    
    public String getQuerySequence(String queryid, String sequence) {
        StringTokenizer st = new StringTokenizer(sequence, ">");
        while(st.hasMoreTokens()) {
            String s = st.nextToken();
            if(s.indexOf(queryid)!=-1) {
                return ">"+s;
            }
        }
        return sequence;
    }
    
    public String getCloneSequence(int cloneid, String clonename) {
        CloneManager m = new CloneManager();
        String s = m.queryCloneSequenceByCloneid(cloneid);
        return ">PlasmID|"+clonename+"\n"+Dnasequence.convertToFasta(s);
    }
    
    private String getBooleanValue(boolean b) {
        if(b)
            return BlastWrapper.BOOLEAN_TRUE;
        else
            return BlastWrapper.BOOLEAN_FALSE;
    }
}
