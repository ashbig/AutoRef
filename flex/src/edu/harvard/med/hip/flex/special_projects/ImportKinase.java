/*
 * ImportKinase.java
 *
 * Created on May 14, 2003, 1:18 PM
 */

package edu.harvard.med.hip.flex.special_projects;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class ImportKinase {
    
    /** Creates a new instance of ImportKinase */
    public ImportKinase() {
    }
    
    public Container performImport(InputStreamReader input, Connection conn) throws Exception {
        int threadid = FlexIDGenerator.getID("threadid");
        String label = Container.getLabel("H", "MG", threadid, null);
        Location l = new Location(Location.CODE_FREEZER);
        Container c = new Container("96 WELL PLATE", l, label+".1", threadid);
        
        BufferedReader in = new BufferedReader(input);
        
        String line = null;
        while((line = in.readLine()) != null){
            System.out.println(line);
            StringTokenizer st = new StringTokenizer(line, "\t");
            String info[] = new String[4];
            int i = 0;
            while(st.hasMoreTokens()) {
                info[i] = st.nextToken();
                i++;
            }
            
            Sample s = new Sample(Sample.ISOLATE, Integer.parseInt(info[1]), c.getId(), Integer.parseInt(info[3]), -1, Sample.GOOD);
            c.addSample(s);
        }
        in.close();
        
        c.insert(conn);
        return c;
    }
    
    public static void main(String [] args) {
        String file = "G:\\kinase_plate3_rearray.txt";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            FileReader input = new FileReader(file);
            FileConvert convert = new FileConvert();
            ImportKinase importer = new ImportKinase();
            Container c = importer.performImport(input, conn);
            DatabaseTransaction.commit(conn);            
            System.out.println("New Container: "+c.getId());
            System.out.println("New Container Label: "+c.getLabel());
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } catch (IOException ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        System.exit(0);
    }
}

