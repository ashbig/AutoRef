/*
 * FileConvert.java
 *
 * Created on April 16, 2003, 3:25 PM
 */

package edu.harvard.med.hip.flex.special_projects;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class FileConvert {
    
    /** Creates a new instance of FileConvert */
    public FileConvert() {
    }
    
    public StringBuffer convertToRearrayInput(InputStreamReader input) {
        ArrayList samples = getSamples(input);
        
        if(samples == null) {
            System.out.println("Error while reading file");
            return null;
        }
        
        if(!convertSamples(samples)) {
            System.out.println("Error while querying database.");
            return null;
        }
        
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<samples.size(); i++) {
            PlateSampleInfo sample = (PlateSampleInfo)samples.get(i);
            sb.append(sample.getSeqid()+"\t"+sample.getContainerid()+"\t"+sample.getPosition()+"\t1\t"+sample.getOligo5p()+"\t"+sample.getOligo3p()+"\n");
        }
        
        return sb;
    }
    
    private ArrayList getSamples(InputStreamReader input) {
        BufferedReader in = new BufferedReader(input);
  
        String line = null;
        ArrayList samples = new ArrayList();
        try {
            while((line = in.readLine()) != null){
                System.out.println(line);
                StringTokenizer st = new StringTokenizer(line);
                String info[] = new String[7];
                int i = 0;
                while(st.hasMoreTokens()) {
                    info[i] = st.nextToken();
                    i++;
                }
                
                PlateSampleInfo sample = new PlateSampleInfo(info[0], info[1], info[2],info[3],info[4],info[5],info[6]);
                samples.add(sample);
            }
            in.close();
            return samples;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            try {
                in.close();
            } catch(Exception e1) {
                System.out.println(e1.getMessage());
                return null;
            }
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    protected boolean convertSamples(ArrayList samples) {
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String sql1 = "select containerid"+
        " from containerheader"+        
        " where label=?";
        String sql2 = "select s.sampleid from sample s, containerheader c"+
                    " where s.containerid=c.containerid"+
                    " and c.label=?"+
                    " and s.containerposition=?";
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt1 = conn.prepareStatement(sql1);
            stmt2 = conn.prepareStatement(sql2);
            
            for(int i=0; i<samples.size(); i++) {
                PlateSampleInfo sample = (PlateSampleInfo)samples.get(i);
                int seqid = Integer.parseInt(sample.getSequence());
                int position = getPosition(sample.getWell());
                sample.setSeqid(seqid);
                sample.setPosition(position);
                
                stmt1.setString(1, sample.getLabel());                
                rs1 = DatabaseTransaction.executeQuery(stmt1);
                if(rs1.next()) {
                    int containerid = rs1.getInt(1);
                    sample.setContainerid(containerid);
                }
                
                stmt2.setString(1, sample.getOligo5plabel());
                stmt2.setInt(2, Integer.parseInt(sample.getOligo5pwell()));
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                if(rs2.next()) {
                    int oligo5p = rs2.getInt(1);
                    sample.setOligo5p(oligo5p);
                }
                               
                stmt2.setString(1, sample.getOligo3plabel());
                stmt2.setInt(2, Integer.parseInt(sample.getOligo3pwell()));
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                if(rs2.next()) {
                    int oligo3p = rs2.getInt(1);
                    sample.setOligo3p(oligo3p);
                }    
            }
            
            return true;
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            return false;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        } finally {
            DatabaseTransaction.closeResultSet(rs1);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(stmt1);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public int getPosition(String well) {
        int position = -1;
        well = well.toLowerCase();
        int row = (int)well.charAt(0);
        int column = Integer.parseInt(well.substring(1));
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
        
        
        row_value = row - a_value + 1;
        
        return (column - 1) * 8 +  row_value ;
    }
    
    public static void main(String args[]) {
        String file = "G:\\convert_to_close.txt";
        String output = "G:\\close.out";
        
        try {
        FileReader input = new FileReader(file);
        FileConvert convert = new FileConvert();
        StringBuffer sb = convert.convertToRearrayInput(input);
        PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(output)));
        pr.print(sb.toString());
        pr.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        System.exit(0);
    }
}
