/*
 * ImportTable.java
 *
 * This class represents a generic database table structure with table name, column names
 * and column information. All the column information will be String type. The null value
 * is represented by String "N/A".
 *
 * Created on March 31, 2005, 10:47 AM
 */

package plasmid.importexport;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ImportTable {
    private String tableName;
    private ArrayList columnNames;
    private ArrayList columnInfo;
    
    /** Creates a new instance of ImportTable */
    public ImportTable() {
    }
    
    public ImportTable(String tableName, ArrayList columnNames, ArrayList columnInfo) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnInfo = columnInfo;
    }
    
    public String getTableName() {return tableName;}
    public ArrayList getColumnNames() {return columnNames;}
    public ArrayList getColumnInfo() {return columnInfo;}
}
