/*
 * ImportFileReader.java
 *
 * Created on March 31, 2005, 10:39 AM
 */

package plasmid.importexport;

import java.io.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ImportFileReader {
    public static final String DILIM = "\t";
    public static final String NA = "NA";
    private String errorMessage;
    private ImportTable table;
    
    /** Creates a new instance of ImportFileReader */
    public ImportFileReader() {
    }
    
    public void setErrorMessage(String s) {this.errorMessage = s;}
    public String getErrorMessage() {return errorMessage;}
    public ImportTable getTable() {return table;}
    
    /**
     * The file will have the following format:
     *  first line: table name
     *  second line: tab delimited column name
     *  rest: tab delimited text
     */
    public boolean readFile(String file) {                
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String tableName = in.readLine();
            String columnNameLine = in.readLine();
            StringTokenizer st = new StringTokenizer(columnNameLine, DILIM);
            ArrayList columnNames = new ArrayList();
            try {
                while(st.hasMoreTokens()) {
                    String s = st.nextToken();
                    columnNames.add(s);
                }
            } catch (NoSuchElementException ex) {
                setErrorMessage(ex.getMessage());
                return false;
            }
            
            ArrayList columnInfo = new ArrayList();
            String line;
            while((line = in.readLine()) != null) {
                st = new StringTokenizer(line, DILIM);
                ArrayList rowInfo = new ArrayList();
                try {
                    while(st.hasMoreTokens()) {
                        String text = st.nextToken();
                        if(NA.equals(text))
                            text = null;
                        
                        rowInfo.add(text);
                    }
                    columnInfo.add(rowInfo);
                } catch (NoSuchElementException ex) {
                    setErrorMessage(ex.getMessage());
                    return false;
                }
            }
            in.close();
            table = new ImportTable(tableName, columnNames, columnInfo);
        } catch (Exception ex) {
            setErrorMessage(ex.getMessage());
            return false;
        }
        
        return true;
    }
}
