/*
 * OrfCloneImporter.java
 *
 * Created on January 23, 2007, 11:19 AM
 */

package edu.harvard.med.hip.flex.infoimport;

import java.util.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.FlexSequence;
import edu.harvard.med.hip.flex.workflow.Project;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.process.Request;


/**
 *
 * @author  DZuo
 */
public class OrfCloneImporter {
    
    /** Creates a new instance of OrfCloneImporter */
    public OrfCloneImporter() {
    }
    
    public Hashtable getSequenceInfo(List accs) throws Exception {
        Hashtable seqs = new Hashtable();
        List processedAccs = new ArrayList();
        
        for(int i=0; i<accs.size(); i++) {
            String refAcc = (String)accs.get(i);
            if(processedAccs.contains(refAcc)) {
                System.out.println("Accession exists: "+refAcc);
                continue;
            }
            
            processedAccs.add(refAcc);
            
            GenbankGeneFinder geneFinder = new GenbankGeneFinder();
            System.out.println("search sequence: "+refAcc);
            Hashtable info = geneFinder.searchDetail(refAcc);
            //System.out.println(info);
            int cdsstart = ((Integer)info.get("start")).intValue();
            int cdsstop = ((Integer)info.get("stop")).intValue();
            Vector names = new Vector();
            Hashtable h = new Hashtable();
            
            h.put(FlexSequence.NAMETYPE, FlexSequence.GENBANK_ACCESSION);
            h.put(FlexSequence.NAMEVALUE, (String)info.get("accession"));
            h.put(FlexSequence.NAMEURL, "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val="+(String)info.get("gi"));
            h.put(FlexSequence.DESCRIPTION, (String)info.get("definition"));
            names.add(h);
            
            h = new Hashtable();
            h.put(FlexSequence.NAMETYPE, FlexSequence.GI);
            h.put(FlexSequence.NAMEVALUE, (String)info.get("gi"));
            h.put(FlexSequence.NAMEURL, "");
            h.put(FlexSequence.DESCRIPTION, "");
            names.add(h);
            
            if(info.get("locus_link") != null) {
                h = new Hashtable();
                h.put(FlexSequence.NAMETYPE, FlexSequence.LOCUS_ID);
                h.put(FlexSequence.NAMEVALUE, (String)info.get("locus_link"));
                h.put(FlexSequence.NAMEURL, "");
                h.put(FlexSequence.DESCRIPTION, "");
                names.add(h);
            }
            
            if(info.get("gene_name") != null) {
                h = new Hashtable();
                h.put(FlexSequence.NAMETYPE, FlexSequence.GENE_SYMBOL);
                h.put(FlexSequence.NAMEVALUE, (String)info.get("gene_name"));
                h.put(FlexSequence.NAMEURL, "");
                h.put(FlexSequence.DESCRIPTION, "");
                names.add(h);
            }
            
            FlexSequence seq = new FlexSequence(-1,SequenceImporter.STATUS,(String)info.get("species"), null,
            (String)info.get("sequencetext"), cdsstart, cdsstop, cdsstop-cdsstart+1,
            -1, names,null, (String)info.get("chromosome"));
            seq.getGccontent();
            seqs.put((String)info.get("gi"), seq);
        }
        
        return seqs;
    }
    
    public void performImport(Hashtable seqs, Project p, String username, Connection conn) throws Exception {
        SequenceImporter seqimp = new SequenceImporter(p);
        RequestImporter reqimp = new RequestImporter(p);
        
        Request request = new Request(username, p);
        Hashtable requests = new Hashtable();
        
        System.out.println("number of sequences: "+seqs.size());
        seqimp.setSequences(seqs);
        if(seqimp.performImport(conn)) {
            Vector results = seqimp.getResults();
            Enumeration enu = results.elements();
            while(enu.hasMoreElements()) {
                SequenceImporterLogger logger = (SequenceImporterLogger)enu.nextElement();
                System.out.println("Import: "+logger.getSequenceid()+"\t"+logger.getFlexid());
                FlexSequence s = new FlexSequence(logger.getFlexid());
                request.addSequence(new FlexSequence(logger.getFlexid()));
            }
            requests.put(username, request);
            
            reqimp.setRequests(requests);
            if(reqimp.performImport(conn)) {
                System.out.println("Import finished.");
            } else {
                throw new Exception("Import aborted.");
            }
        } else {
            throw new Exception("Import aborted.");
        }
    }
    
    public static void main(String args[]) {
        String fileName = "C:\\Documents and Settings\\dzuo\\My Documents\\work\\production\\ORFclone\\OCAB1-6_2007_01.txt";
        int projectid = 21;
        String username="dzuo";
        
        OrfFileParser parser = new OrfFileParser();
        OrfCloneImporter importer = new OrfCloneImporter();
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            System.out.println("paring orf file.");
            parser.parseOrfFile(fileName);
            List accs = parser.getGenbanks();
            Hashtable seqs = importer.getSequenceInfo(accs);
            Project p = new Project(projectid);
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            importer.performImport(seqs, p, username, conn);
            DatabaseTransaction.commit(conn);
            System.out.println("Import finished.");
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println("Import aborted.");
            ex.printStackTrace();
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}
