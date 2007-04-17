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
    public static final String VECTOR = "Vector";
    public static final String VECTORFEATURE = "Vector Feature";
    public static final String VECTORPROPERTY = "Vector Property";
    public static final String VECTORPARENT = "Vector Parent";
    public static final String AUTHOR = "Author";
    public static final String VECTORAUTHOR = "Vector Author";
    public static final String PUBLICATION = "Publication";
    public static final String VECTORPUBLICATION = "Vector Publication";
    public static final String CLONE = "Clone";
    public static final String GROWTHCONDITION = "Growth Condition";
    public static final String CLONESELECTION = "Clone Selection";
    public static final String CLONEGROWTH = "Clone Growth";
    public static final String CLONEHOST = "Clone Host";
    public static final String CLONENAMETYPE = "Clone Name Type";
    public static final String CLONENAME = "Clone Name";
    public static final String CLONEINSERT = "Clone Insert";
    public static final String REFSEQ = "Reference Sequence";
    public static final String INSERTREFSEQ = "Insert Reference Sequence";
    public static final String REFSEQNAMETYPE = "Reference Sequence Name Type";
    public static final String REFSEQNAME = "Reference Sequence Name";
    public static final String CLONEAUTHOR = "Clone Author";
    public static final String CLONEPUBLICATION = "Clone Publication";
    public static final String PLATE = "Plate";
    public static final String CLONEPROPERTY = "Clone Property";
    public static final String INSERTPROPERTY = "Insert Property";
    public static final String CLONECOLLECTION = "Clone Collection";
    public static final String CLONEINSERTONLY = "Clone Insert Only";
    
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
