/*
 * ImportBC.java
 *
 * Created on January 14, 2004, 3:56 PM
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
 * @author  DZuo
 */
public class ImportBC {
    
    /** Creates a new instance of ImportBC */
    public ImportBC() {
    }
    private List clones;
    
    public List getClones() {return clones;}
       
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
                        int cloneid = Integer.parseInt((String)st.nextToken());

                        String resultMGC = null;
                        if(st.hasMoreTokens()) {
                            resultMGC = (String)st.nextToken();
                            if("NA".equals(resultMGC))
                                resultMGC = null;
                        }
                        
                        String resultRef = null;
                        if(st.hasMoreTokens()) {
                            resultRef = (String)st.nextToken();
                            if("NA".equals(resultRef))
                                resultRef = null;
                        }

                        String genbank = null;
                        if(st.hasMoreTokens()) {
                            genbank = (String)st.nextToken();
                            if("NA".equals(genbank))
                                genbank = null;
                        }
                       
                        String gi = null;
                        if(st.hasMoreTokens()) {
                            gi = (String)st.nextToken();
                            if("NA".equals(gi))
                                gi = null;
                        }
                        
                        String seq = (String)st.nextToken();
                        String quality = checkQuality(seq);
                        if(quality.trim().length() > 0) {
                            System.out.println(cloneid+": "+quality);
                            //System.exit(0);
                        }
                        
                        DescrepancyInfo clone = new DescrepancyInfo(cloneid, genbank, gi, resultRef, resultMGC, seq, 1, seq.length());
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

    public void updateDB(Connection conn, Logger log) throws Exception {
        String sql = "select sequencingid from clonesequencing where cloneid=?";
        String sql2 = "insert into clonesequence(sequenceid,sequencetype,resultexpect,resultpubhit,"+
        " sequencingid,pubhit,pubhitgi,cdsstart,cdslength)"+
        " values(?,'FULL SEQUENCE',?,?,?,?,?,?,?)";
        String sql3 = "insert into clonesequencetext(sequenceid, sequenceorder, sequencetext)\n"+
        " values(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        
        ResultSet rs = null;
        
        for(int n=0; n<clones.size(); n++) {
            DescrepancyInfo info = (DescrepancyInfo)clones.get(n);
            int cloneid = info.getCloneid();
            String sequencetext = info.getSequence();
            
            if(sequencetext == null || sequencetext.length() == 0) {
                System.out.println("No sequence found: "+cloneid);
                log.logging((new Integer(cloneid)).toString());
                continue;
            }
            
            stmt.setInt(1, cloneid);
            rs = DatabaseTransaction.executeQuery(stmt);
            int sequencingid = 0;
            
            if(rs.next()) {
                sequencingid = rs.getInt(1);
            }
            
            if(sequencingid == 0) {
                throw new Exception("No sequencing record found for clone: "+cloneid);
            }
            
            stmt2.setString(2, info.getResultExpect());
            stmt2.setString(3, info.getResultPubhit());
            stmt2.setInt(4, sequencingid);
            stmt2.setString(5, info.getGenbank());
            stmt2.setString(6, info.getGi());
            stmt2.setInt(7, info.getCdsstart());
            stmt2.setInt(8, info.getCdslength());
            
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

    private String checkQuality(String seq) {
        StringBuffer result = new StringBuffer();
        int cdsstart = 1;
        int cdslength = seq.length();
        int cdsstop = cdsstart+cdslength-1;
        if( cdslength % 3 != 0) {
            result.append("\tWrong CDS length\n");
        }
        else {
            String start_codon = seq.substring(cdsstart-1, cdsstart+2);
            if ( !start_codon.equalsIgnoreCase("atg") && !start_codon.equalsIgnoreCase("gtg")) {
                result.append("\tWrong start: "+start_codon+"\n");
            }
            else {
                String stop_codon = seq.substring(cdsstop-3,cdsstop);
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
    
    public class DescrepancyInfo {
        private int cloneid;
        private String genbank;
        private String gi;
        private String resultExpect;
        private String resultPubhit;
        private String sequence;
        private int cdsstart;
        private int cdslength;
        
        public DescrepancyInfo(int cloneid,
                            String genbank,
                            String gi,
                            String resultExpect,
                            String resultPubhit,
                            String sequence,
                            int cdsstart,
                            int cdslength) {
            this.cloneid=cloneid;
            this.genbank = genbank;
            this.gi = gi;
            this.resultExpect = resultExpect;
            this.resultPubhit = resultPubhit;
            this.sequence = sequence;
            this.cdsstart = cdsstart;
            this.cdslength = cdslength;
        }
        
        public int getCloneid() {return cloneid;}
        public String getGenbank() {return genbank;}
        public String getGi() {return gi;}
        public String getResultExpect() {return resultExpect;}
        public String getResultPubhit() {return resultPubhit;}
        public int getCdsstart() {return cdsstart;}
        public int getCdslength() {return cdslength;}
        public String getSequence() {return sequence;}
    }
    
    
    public static void main(String arg[]) {
        //String file = "G:\\ct_rejected_mgc.txt";
        String file = "G:\\clontech_unseq.txt";
        String logfile = "G:\\clontech.log";
        
        Logger log = new Logger(logfile);
        log.start();
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            ImportBC importer = new ImportBC();
            //importer.readSequence(seqfile);
            importer.readFile(file);
            importer.updateDB(conn, log);
            DatabaseTransaction.commit(conn);
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