/*
 * SequencingFileCreator.java
 *
 * Created on April 24, 2003, 4:58 PM
 */

package edu.harvard.med.hip.flex.export;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class SequencingFileCreator {
    public ArrayList plates;
    
    /** Creates a new instance of SequencingFileCreator */
    public SequencingFileCreator(ArrayList plates) {
        this.plates = plates;
    }
    
    public Hashtable getSequencingPlateInfo() {
        StringBuffer sb = new StringBuffer();
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "select sampleid, sampletype, containerid,"+
        " containerposition, cd.constructid, cd.sequenceid, cloneid"+
        " from clonesequencing c, sample s, constructdesign cd"+
        " where s.containerid = "+
        " (select distinct sequencingcontainerid"+
        " from clonesequencing"+
        " where sequencingcontainerlabel=?)"+
        " and c.sequencingsampleid(+)=s.sampleid"+
        " and s.constructid=cd.constructid(+)"+
        " order by containerposition";
        Hashtable plateInfo = new Hashtable();
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<plates.size(); i++) {
                String label = (String)plates.get(i);
                stmt.setString(1, label);
                rs = t.executeQuery(stmt);
                ArrayList samples = new ArrayList();
                
                while(rs.next()) {
                    int sampleid = rs.getInt(1);
                    String sampletype = rs.getString(2);
                    int containerid = rs.getInt(3);
                    int position = rs.getInt(4);
                    int constructid = rs.getInt(5);
                    int sequenceid = rs.getInt(6);
                    int cloneid = rs.getInt(7);
                    CloneSample sample = new CloneSample(sampleid,position,containerid,cloneid);
                    sample.setSequenceid(sequenceid);
                    sample.setConstructid(constructid);
                    sample.setType(sampletype);
                    samples.add(sample);
                }
                plateInfo.put(label, samples);
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return plateInfo;
    }
    
    public static void main(String args[]) {
        String file = "G:\\yeast_seq_end_plate_label.txt";
        ArrayList plates = new ArrayList();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = in.readLine()) != null) {
                plates.add(line);
            }
            in.close();
        }catch (Exception ex) {
            System.out.println(ex);
        }
        
        PrintWriter pr = null;
        SequencingFileCreator creator = new SequencingFileCreator(plates);
        Hashtable plateInfo = creator.getSequencingPlateInfo();
        Enumeration labels = plateInfo.keys();
        while(labels.hasMoreElements()) {
            String label = (String)labels.nextElement();
            ArrayList samples = (ArrayList)plateInfo.get(label);
            
            try {
                pr = new PrintWriter(new BufferedWriter(new FileWriter(label+".txt")));
                pr.println("Container Label:\t"+label);
                
                for(int i=0; i<samples.size(); i++) {
                    CloneSample sample = (CloneSample)samples.get(i);
                    
                    if(i==0) {
                        pr.println("Container ID:\t"+sample.getContainerid());
                        pr.println();
                        pr.println("Well ID\tSequence ID\tClone ID");
                    }
                    
                    if(Sample.CONTROL_POSITIVE.equals(sample.getType()) || Sample.CONTROL_NEGATIVE.equals(sample.getType())) {
                        pr.println(sample.getPosition());
                    } else if(Sample.EMPTY.equals(sample.getType())) {
                        pr.println(sample.getPosition()+"\t"+sample.getSequenceid());
                    } else {
                        pr.println(sample.getPosition()+"\t"+sample.getSequenceid()+"\t"+sample.getCloneid());
                    }
                }
                
                pr.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }        
}