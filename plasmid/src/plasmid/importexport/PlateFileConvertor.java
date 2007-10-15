/*
 * PlateFileConvertor.java
 *
 * Created on September 20, 2007, 10:41 AM
 */

package plasmid.importexport;

import java.io.*;
import java.util.*;
import java.sql.*;
import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class PlateFileConvertor {
    private List clones;
    private List clonesInPlasmid;
    private List clonesNotinPlasmid;
    
    /** Creates a new instance of PlateFileConvertor */
    public PlateFileConvertor() {
        clones = new ArrayList();
        clonesInPlasmid = new ArrayList();
        clonesNotinPlasmid = new ArrayList();
    }
    
    public List getClones() {return clones;}
    public List getClonesInPlasmid() {return clonesInPlasmid;}
    public List getClonesNotinPlasmid() {return clonesNotinPlasmid;}
    
    public void readCloneFile(String input) throws Exception {     
        BufferedReader in = new BufferedReader(new FileReader(input));  
        String line = in.readLine();
        StringTokenizer st = null;
        
        while((line = in.readLine()) != null) {
            st = new StringTokenizer(line);
            try {
                while(st.hasMoreTokens()) {
                    String label = st.nextToken().trim();
                    String position = st.nextToken().trim();
                    String cloneid = st.nextToken().trim();
                    PlateInfo info = new PlateInfo(label, position, cloneid);
                    clones.add(info);
                }
            } catch (NoSuchElementException ex) {
                throw new Exception(ex);
            }
        }
        in.close();
    }
    
    public void convertClones() throws Exception {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        String sql = "select cloneid from clonename where nametype='HIP Clone ID' and namevalue=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        for(int i=0; i<clones.size(); i++) {
            PlateInfo p = (PlateInfo)clones.get(i);
            stmt.setString(1, p.getCloneid());
            rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                String s = rs.getString(1);
                p.setCloneid(s);
                clonesInPlasmid.add(p);
            } else {
                clonesNotinPlasmid.add(p);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
    }

    public void printClones(List l, String file) throws Exception {
        OutputStreamWriter out = new FileWriter(file);
        out.write("Plate384\n");
        out.write("label\tposition\tcloneid\n");
        
        for(int i=0; i<l.size(); i++) {
            PlateInfo info = (PlateInfo)l.get(i);
            out.write(info.getLabel()+"\t"+info.getPosition()+"\t"+info.getCloneid()+"\n");
        }
        out.close();
    }
    
    public static final void main(String args[]) {
        String input = "G:\\plasmid\\Import_384_2007_09\\plate384_2007_09.txt";
        String output = "G:\\plasmid\\Import_384_2007_09\\plate384_2007_09_output.txt";
        String error = "G:\\plasmid\\Import_384_2007_09\\plate384_2007_09_error.txt";
        
        PlateFileConvertor convertor = new PlateFileConvertor();
        try {
            System.out.println("reading clone file");
            convertor.readCloneFile(input);
            System.out.println("converting clone file");
            convertor.convertClones();
            System.out.println("printing clone file");
            convertor.printClones(convertor.getClonesInPlasmid(), output);
            convertor.printClones(convertor.getClonesNotinPlasmid(), error);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.exit(0);
        }
    }
}
