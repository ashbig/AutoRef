/*
 * PlateImporter.java
 *
 * Created on May 23, 2005, 1:51 PM
 */

package plasmid.importexport;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.util.PlatePositionConvertor;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  DZuo
 */
public class PlateImporter {
    private PlateManager manager;
    
    /** Creates a new instance of PlateImporter */
    public PlateImporter(Connection conn) {
        this.manager = new PlateManager(conn);
    }
    
    public void importPlateAndSample(ImportTable table, Map cloneidmap) throws Exception {
        DefTableManager m = new DefTableManager();
        int containerid = m.getMaxNumber("containerheader", "containerid", DatabaseTransaction.getInstance());
        if(containerid == -1) {
            throw new Exception("Cannot get containerid from containerheader table.");
        }
        int sampleid = m.getMaxNumber("sample", "sampleid", DatabaseTransaction.getInstance());
        if(sampleid == -1) {
            throw new Exception("Cannot get sampleid from sample table.");
        }
        
        List plates = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        String currentLabel = null;
        String lastLabel = null;
        Container c = null;
        for(int n=0; n<contents.size(); n++) {
            List row = (List)contents.get(n);
            Sample currentSample = null;
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("label".equalsIgnoreCase(columnName)) {
                    currentLabel = columnInfo.trim();
                    if(!currentLabel.equals(lastLabel)) {
                        c = new Container();
                        c.setContainerid(containerid);
                        c.setType(Container.COSTAR_RD);
                        c.setCapacity(Container.getCapacity(Container.COSTAR_RD));
                        c.setLocation(Location.FREEZER);
                        c.setLabel(columnInfo);
                        c.setOricontainerid(columnInfo);
                        c.setStatus(Container.FILLED);
                        c.initiateSamples();
                        
                        for(int j=0; j<Container.getCapacity(c.getType()); j++) {
                            Sample s = new Sample();
                            s.setSampleid(sampleid);
                            s.setType(Sample.EMPTY);
                            s.setStatus(Sample.GOOD);
                            s.setPositions(j+1);
                            s.setContainerid(containerid);
                            s.setContainerlabel(c.getLabel());
                            c.addSample(s);
                            sampleid++;
                        }
                        
                        plates.add(c);
                        lastLabel = currentLabel;
                        containerid++;
                    }
                }
                if("position".equalsIgnoreCase(columnName)) {
                    int p = Integer.parseInt(columnInfo);
                    currentSample = c.getSample(p);
                    currentSample.setType(Sample.WORKING_GLYCEROL);
                    currentSample.setPositions(p);
                    
                }
                if("cloneid".equalsIgnoreCase(columnName)) {
                    if(cloneidmap == null) {
                        currentSample.setCloneid(Integer.parseInt(columnInfo));
                    } else {
                        currentSample.setCloneid(((Integer)cloneidmap.get(columnInfo)).intValue());
                    }
                }
            }
        }
        
        if(!manager.insertPlateAndSample(plates)) {
            throw new Exception("Error occured while inserting into CONTAINERHEADER and SAMPLE table.");
        }
    }
    
    public void importSample(ImportTable table, Map cloneidmap) throws Exception {
        DefTableManager m = new DefTableManager();
        int sampleid = m.getMaxNumber("sample", "sampleid", DatabaseTransaction.getInstance());
        if(sampleid == -1) {
            throw new Exception("Cannot get sampleid from sample table.");
        }
        
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        List samples = new ArrayList();
        for(int n=0; n<contents.size(); n++) {
            List row = (List)contents.get(n);
            Container c = null;
            Sample s = null;
            int cloneid = 0;
            String currentLabel=null;
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                
                if("label".equalsIgnoreCase(columnName)) {
                    currentLabel = columnInfo.trim();
                    c = manager.queryContainer(currentLabel, true);
                    if(c == null) {
                        throw new Exception("Error occured while querying container with label: "+currentLabel);
                    }
                }
                if("position".equalsIgnoreCase(columnName)) {
                    int p = Integer.parseInt(columnInfo);
                    s = c.getSample(p);
                    if(!Sample.EMPTY.equals(s.getType())) {
                        throw new Exception("Container "+currentLabel+" at position "+p+" is not empty.");
                    }
                }
                if("cloneid".equalsIgnoreCase(columnName)) {
                    if(cloneidmap == null) {
                        cloneid = Integer.parseInt(columnInfo);
                    } else {
                        cloneid = ((Integer)cloneidmap.get(columnInfo)).intValue();
                    }
                }
            }
            if(c == null) {
                throw new Exception("Invalid container");
            }
            if(s == null) {
                throw new Exception("Invalid sample");
            }
            if(cloneid == 0) {
                throw new Exception("Invalid cloneid");
            }
            
            s.setType(Sample.WORKING_GLYCEROL);
            s.setCloneid(cloneid);
            samples.add(s);
            sampleid++;
        }
        
        if(!manager.updateSamples(samples)) {
            throw new Exception("Error occured while inserting into SAMPLE table.");
        }
    }
}
