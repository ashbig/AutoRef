/*
 * ImportClontech.java
 *
 * Created on June 20, 2003, 10:58 AM
 */

package edu.harvard.med.hip.flex.special_projects;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.utility.Logger;
import edu.harvard.med.hip.flex.util.FlexIDGenerator;
import java.util.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class ImportClontech {
    private List clones;
    
    public List getClones() {return clones;}
    
    /** Creates new ImportCloneInfo */
    public ImportClontech() {
    }
    
    public void readFile(String file) throws Exception {
        String line;
        clones = new ArrayList();
        
        try {
            BufferedReader in = new BufferedReader((new FileReader(file)));
            //String ignore = in.readLine();
            
            while((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "!");
                
                try {
                    if(st.hasMoreTokens()) {
                        //int order = Integer.parseInt((String)st.nextToken());
                        int sampleid = Integer.parseInt((String)st.nextToken());
                        int sequenceid = Integer.parseInt((String)st.nextToken());
                        
                        /**
                         * String genbank = (String)st.nextToken();
                         *
                         * int remove = genbank.indexOf(".");
                         * if(remove != -1) {
                         * genbank = genbank.substring(0, remove);
                         * }
                         *
                         * String version = null;
                         * if(st.hasMoreTokens()) {
                         * version = (String)st.nextToken();
                         * }
                         */
                        String resultMGC = null;
                        if(st.hasMoreTokens()) {
                            resultMGC = (String)st.nextToken();
                            if("NA".equals(resultMGC))
                                resultMGC = null;
                        }
                        
                        String commentMGC = null;
                        if(st.hasMoreTokens()) {
                            commentMGC = (String)st.nextToken();
                        }
                         /*
                        String mutprimer = null;
                        if(st.hasMoreTokens()) {
                            mutprimer = (String)st.nextToken();
                        }
                         */
                        
                        String resultRef = null;
                        if(st.hasMoreTokens()) {
                            resultRef = (String)st.nextToken();
                            if("NA".equals(resultRef))
                                resultRef = null;
                        }
                        
                        String commentGenbank = null;
                        if(st.hasMoreTokens()) {
                            commentGenbank = (String)st.nextToken();
                            if("NA".equals(commentGenbank))
                                commentGenbank = null;
                        }
                        
                        String linker5p = null;
                        if(st.hasMoreTokens()) {
                            linker5p = (String)st.nextToken();
                            if("NA".equals(linker5p))
                                linker5p = null;
                        }
                        
                        String linker3p = null;
                        if(st.hasMoreTokens()) {
                            linker3p = (String)st.nextToken();
                            if("NA".equals(linker3p))
                                linker3p = null;
                        }
                        
                        String genechange = null;
                        if(st.hasMoreTokens()) {
                            genechange = (String)st.nextToken();
                            if("NA".equals(genechange))
                                genechange = null;
                        }
                        /*
                        String genbank = null;
                        if(st.hasMoreTokens()) {
                            genbank = (String)st.nextToken();
                            if("NA".equals(genbank))
                                genbank = null;
                        }
                        */
                        ClontechInfo clone = new ClontechInfo(0,sampleid,sequenceid,null,null,resultMGC,commentMGC,null,resultRef,commentGenbank,linker5p,linker3p,genechange);
                        //clone.setGenbank(genbank);
                        clones.add(clone);
                        
                    }
                } catch (NoSuchElementException ex) {
                    System.out.println(ex);
                }
            }
            in.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public List getSequences() {
        List newClones = clones;
        Collections.sort(newClones, new SeqidComparator());
        int last = 0;
        List ids = new ArrayList();
        for(int i=0; i<newClones.size(); i++) {
            ClontechInfo info = (ClontechInfo)newClones.get(i);
            int id= info.getSeqid();
            
            if(last != id || last == 0) {
                last = id;
                ids.add(new Integer(id));
            }
        }
        
        return ids;
    }
    
    public void updateDB(Connection conn, String file, Logger log) throws Exception {
        String sql = "select sequencingid from clonesequencing where sequencingsampleid=?";
        String sql2 = "insert into clonesequence(sequenceid,sequencetype,resultexpect,resultpubhit,"+
        " matchexpect,matchpubhit,sequencingid,genechange,linker5p,linker3p,cdsstart,cdslength)"+
        " values(?,'FULL SEQUENCE',?,?,?,?,?,?,?,?,?,?)";
        String sql3 = "insert into clonesequencetext(sequenceid, sequenceorder, sequencetext)\n"+
        " values(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        
        ResultSet rs = null;
        
        for(int n=0; n<clones.size(); n++) {
            ClontechInfo info = (ClontechInfo)clones.get(n);
            int sampleid = info.getSampleid();
            String sequencetext = getSequence(sampleid, file);
            
            if(sequencetext == null || sequencetext.length() == 0) {
                System.out.println("No sequence found: "+sampleid);
                log.logging((new Integer(sampleid)).toString());
                continue;
            }
            
            /**
            String result = checkQuality(sequencetext);
            if(result.trim().length() > 0) {
                System.out.println("Bad sequence: "+sampleid+"\n"+result);
                log.logging(sampleid+result);
                continue;
            }
            */
            
            stmt.setInt(1, sampleid);
            rs = DatabaseTransaction.executeQuery(stmt);
            int sequencingid = 0;
            
            if(rs.next()) {
                sequencingid = rs.getInt(1);
            }
            
            if(sequencingid == 0) {
                throw new Exception("No clone found for sample: "+sampleid);
            }
            
            stmt2.setString(2, info.getResultMGC());
            stmt2.setString(3, info.getResult());
            stmt2.setString(4, info.getCommentMGC());
            stmt2.setString(5, info.getCommentRef());
            stmt2.setInt(6, sequencingid);
            stmt2.setString(7, info.getGenechange());
            stmt2.setString(8, info.getLinker5p());
            stmt2.setString(9, info.getLinker3p());
            stmt2.setInt(10, -1);
            stmt2.setInt(11, -1);
            //stmt2.setString(12, info.getGenbank());
            
            int sequenceid = FlexIDGenerator.getID("CLONESEQUENCEID");
            stmt2.setInt(1, sequenceid);
            DatabaseTransaction.executeUpdate(stmt2);
           
            int i=0;
            while(sequencetext.length()-4000*i>4000) {
                String text = sequencetext.substring(4000*(i), 4000*(i+1)).toUpperCase();
                stmt3.setInt(1, sequenceid);
                stmt3.setInt(2,  i+1);
                stmt3.setString(3, text);
                DatabaseTransaction.executeUpdate(stmt3);
                
                i++;
            }
            String text = sequencetext.substring(4000*(i));
            stmt3.setInt(1, sequenceid);
            stmt3.setInt(2,  i+1);
            stmt3.setString(3, text);
            DatabaseTransaction.executeUpdate(stmt3);
            System.out.println(n+": update");
        }
    }
    
    public void readSequence(String file) {
        String line;
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            PrintWriter logger = new PrintWriter(new BufferedWriter(new FileWriter("G:\\CTSequences.txt")));
            
            String seq = "";
            while((line = in.readLine()) != null) {
                if(line.indexOf(">") != -1 && seq.length() == 0) {
                    String sampleid = line.substring(1, line.indexOf("_"));
                    logger.print(sampleid+"\t");
                    
                    if(seq.length() > 0) {
                        logger.println(seq);
                        seq = "";
                    }
                    continue;
                }
                
                seq = seq+line;
            }
            in.close();
            logger.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public String getSequence(int sampleid, String file) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String seq = "";
        String match = ">"+sampleid;
        boolean isStart = false;
        String line = null;
        
        while((line = in.readLine()) != null) {
            if(line.indexOf(match) != -1) {
                isStart = true;
                continue;
            }
            
            if(isStart && line.indexOf(">") != -1) {
                in.close();
                return seq;
            }
           
            if(isStart) {
                seq = seq+line;
            }
        }
        
        in.close();
        return seq;
    }
    
    private String checkQuality(String seq) {
        StringBuffer result = new StringBuffer();
        int cdsstart = 54;
        int cdslength = seq.length()-90;
        int cdsstop = cdsstart+cdslength;
        if( cdslength % 3 != 0) {
            result.append("\tWrong CDS length\n");
        }
        else {
            String start_codon = seq.substring(cdsstart-1, cdsstart+2);
            if ( !start_codon.equalsIgnoreCase("atg") && !start_codon.equalsIgnoreCase("gtg")) {
                result.append("\tWrong start: "+start_codon+"\n");
            }
            else {
                String stop_codon = seq.substring(cdsstop-4,cdsstop-1);
                if (!stop_codon.equalsIgnoreCase("ttg")&&!stop_codon.equalsIgnoreCase("tta") && !stop_codon.equalsIgnoreCase("taa") && !stop_codon.equalsIgnoreCase("tag") && !stop_codon.equalsIgnoreCase("tga")) {
                    result.append("\tWrong stop: "+stop_codon+"\n");
                }
                else {
                    int first = cdsstart-1;
                    int second = cdsstart+2;
                    while (second < cdsstop-1) {
                        String codon = seq.substring(first,second);
                        //System.out.println("codon: "+codon);
                        if (codon.equalsIgnoreCase("taa") || codon.equalsIgnoreCase("tag") || codon.equalsIgnoreCase("tga")) {
                            result.append("\tInternal stop: "+first+"\n");
                            break;
                        }
                        first = first+3;
                        second = second+3;
                    }
                }
            }
        }
        return result.toString();
    }
    
    public class ClontechInfo {
        private int order;
        private int sampleid;
        private int seqid;
        private String genbank;
        private String version;
        private String resultMGC;
        private String commentMGC;
        private String mutPrimer;
        private String result;
        private String commentRef;
        private String linker5p;
        private String linker3p;
        private String genechange;
        private String sequence;
        
        public ClontechInfo(int order, int sampleid, int seqid, String genbank, String version, String resultMGC, String commentMGC, String mutPrimer, String result, String commentRef, String linker5p,String linker3p,String genechange) {
            this.order = order;
            this.sampleid = sampleid;
            this.seqid = seqid;
            this.genbank = genbank;
            this.version = version;
            this.resultMGC = resultMGC;
            this.commentMGC = commentMGC;
            this.mutPrimer = mutPrimer;
            this.result = result;
            this.commentRef = commentRef;
            this.linker5p = linker5p;
            this.linker3p = linker3p;
            this.genechange = genechange;
        }
        
        public int getSeqid() {return seqid;}
        public int getSampleid() {return sampleid;}
        public String getGenbank() {return genbank;}
        public String getVersion() {return version;}
        public String getResultMGC() {return resultMGC;}
        public String getCommentMGC() {return commentMGC;}
        public String getMutPrimer() {return mutPrimer;}
        public String getResult() {return result;}
        public String getCommentRef() {return commentRef;}
        public String getLinker5p() {return linker5p;}
        public String getLinker3p() {return linker3p;}
        public String getGenechange() {return genechange;}
        public String getSequence() {return sequence;}
        public void setSequence(String sequence) {this.sequence = sequence;}
        public void setGenbank(String genbank) {this.genbank = genbank;}
    }
    
    public static class SeqidComparator implements Comparator {
        
        public SeqidComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            int id1 = ((ClontechInfo)p1).getSeqid();
            int id2 = ((ClontechInfo)p2).getSeqid();
            
            if(id1 < id2)
                return -1;
            
            if(id1 == id2)
                return 0;
            
            if(id1 > id2)
                return 1;
            
            return -1;
        }
    }
    
    
    public static void main(String arg[]) {
        String file = "G:\\ct_rejected_mgc.txt";
        String outfile = "G:\\seqids.txt";
        
        String seqfile = "G:\\CTSequence.txt";
        String logfile = "G:\\clontech.log";
        
        Logger log = new Logger(logfile);
        log.start();
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            ImportClontech importer = new ImportClontech();
            //importer.readSequence(seqfile);
            importer.readFile(file);
            importer.updateDB(conn, seqfile, log);
            //DatabaseTransaction.commit(conn);
            //        List seqs = importer.getSequences();
            
            //        PrintWriter out = null;
            //        try {
            //            out = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
            //            for(int i=0; i<seqs.size(); i++) {
            //                out.println(seqs.get(i));
            //            }
            
            //            out.close();
            //        }catch (Exception ex) {
            //          System.out.println(ex);
            //    }
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            log.end();
            System.exit(0);
        }
    }
}
