/*
 * QueryManager.java
 *
 * Created on September 9, 2002, 3:01 PM
 */

package edu.harvard.med.hip.flex.query;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class QueryManager {
    private String message;
    private ArrayList queryInfoList;
    
    /** Creates new QueryManager */
    public QueryManager() {
    }
    
    public boolean doQuery(ArrayList searchTerm, String searchType, String flexstatus, int project, int workflow, String plate) {
        if(searchTerm == null || searchTerm.isEmpty()) {
            setMessage("Search terms are not specified.");
            return false;
        }
        
        String searchList = convertToSqlList(searchTerm);
        
        if(searchList == null) {
            setMessage("Search terms are not specified.");
            return false;
        }
        
        if(flexstatus == null) {
            setMessage("FLEX STATUS cannot be null.");
            return false;
        }
        
        String sql;
        boolean isPending = false;
        if("REJECTED".equals(flexstatus) || "PENDING".equals(flexstatus)) {
            sql = "select distinct t.sequenceid, flexstatus, pname, n1.namevalue as gi,"+
            " n2.namevalue as genbankacc, n3.namevalue as genename,"+
            " n4.namevalue as genesymbol, n5.namevalue as panumber"+
            " from"+
            " (select f.sequenceid, f.flexstatus, p.name as pname, p.projectid"+
            " from flexsequence f, project p, requestsequence r"+
            " where f.flexstatus='"+flexstatus+"'"+
            " and f.sequenceid=r.sequenceid"+
            " and r.projectid=p.projectid"+
            " and f.sequenceid in"+
            " (select sequenceid from name"+
            " where nametype='"+searchType+"'"+
            " and namevalue in ("+searchList+"))) t,"+
            " givu n1, genbankvu n2, genenamevu n3, genesymbolvu n4, pavu n5"+
            " where t.sequenceid=n1.sequenceid(+)"+
            " and t.sequenceid=n2.sequenceid(+)"+
            " and t.sequenceid=n3.sequenceid(+)"+
            " and t.sequenceid=n4.sequenceid(+)"+
            " and t.sequenceid=n5.sequenceid(+)";
            
            isPending = true;
        } else {
            sql = "select distinct t.sequenceid, flexstatus, constructtype,\n"+
            " containerposition, containerid, label, pname, wname, five,\n"+
            " three, resultvalue, n1.namevalue as gi,\n"+
            " n2.namevalue as genbankacc, n3.namevalue as genename,\n"+
            " n4.namevalue as genesymbol, n5.namevalue as panumber\n"+
            " from"+
            " (select f.sequenceid, f.flexstatus, c.constructtype,\n"+
            " c.projectid, c.workflowid, s.containerposition, ch.containerid, ch.label,\n"+
            " ou.gatewaysequence as five, oc.gatewaysequence as three,\n"+
            " p.name as pname, w.name as wname, r.resultvalue\n"+
            " from flexsequence f, constructdesign c,\n"+
            " sample s, containercell cc, containerheader ch,\n"+
            " result r, project p, workflow w, oligo ou, oligo oc\n"+
            " where f.sequenceid=c.sequenceid(+)\n"+
            " and c.constructid=s.constructid(+)\n"+
            " and s.sampleid=cc.sampleid\n"+
            " and cc.containerid=ch.containerid\n"+
            " and c.projectid=p.projectid\n"+
            " and c.workflowid=w.workflowid\n"+
            " and c.oligoid_5p=ou.oligoid\n"+
            " and c.oligoid_3p=oc.oligoid\n"+
            " and s.sampleid=r.sampleid(+)\n"+
            " and f.flexstatus='"+flexstatus+"'\n"+
            " and f.sequenceid in\n"+
            " (select sequenceid from name\n"+
            " where nametype='"+searchType+"'\n"+
            " and namevalue in ("+searchList+"))) t,\n"+
            " givu n1, genbankvu n2, genenamevu n3, genesymbolvu n4, pavu n5\n"+
            " where t.sequenceid=n1.sequenceid(+)\n"+
            " and t.sequenceid=n2.sequenceid(+)\n"+
            " and t.sequenceid=n3.sequenceid(+)\n"+
            " and t.sequenceid=n4.sequenceid(+)\n"+
            " and t.sequenceid=n5.sequenceid(+)\n";
            
            if(project != -1)
                sql = sql+" and t.projectid="+project;
            
            if(workflow != -1)
                sql = sql+" and t.workflowid="+workflow;
            
            if("oligo".equals(plate))
                sql = sql+" and (t.label like '_OU%'"+
                " or t.label like '_OC%'"+
                " or t.label like '_OF%'"+
                " or t.label like 'OSPL%')";
            if("doligo".equals(plate))
                sql = sql+" and (t.label like '_DU%'"+
                " or t.label like '_DC%'"+
                " or t.label like '_DF%')";
            if("pcr1".equals(plate))
                sql = sql+" and (t.label like '_PA%'"+
                " or t.label like 'P1PL%')";
            if("pcr2".equals(plate))
                sql = sql+" and t.label like '_PB%'";
            if("gel".equals(plate))
                sql = sql+" and (t.label like '_GL%'"+
                " or t.label like 'GPL%')";
            if("filter".equals(plate))
                sql = sql+" and (t.label like '_FI%'"+
                " or t.label like 'FPL%')";
            if("bp".equals(plate))
                sql = sql+" and (t.label like '_BP%'"+
                " or t.label like 'BPL%')";
            if("capture".equals(plate))
                sql = sql+" and t.label like '_CR%'";
            if("transformation".equals(plate))
                sql = sql+" and (t.label like '_TR%'"+
                " or t.label like 'TPL%')";
            if("agar".equals(plate))
                sql = sql+" and (t.label like '_AA%'"+
                " or t.label like '_AB%')";
            if("culture".equals(plate))
                sql = sql+" and (t.label like '_LI%'"+
                " or t.label like 'CPL%')";
            if("dna".equals(plate))
                sql = sql+" and (t.label like '_DN%'"+
                " or t.label like 'DPL%')";
            if("glycerol".equals(plate))
                sql = sql+" and t.label like '_GS%'";
        }
     
        DatabaseTransaction dt = null;
        RowSet rs = null;
        try {
            dt = DatabaseTransaction.getInstance();
            rs = dt.executeQuery(sql);
            queryInfoList = new ArrayList();
            
            while(rs.next()) {
                int sequenceid = rs.getInt(1);
                String status = rs.getString(2);
                String type = null;
                int well = -1;
                int containerid = -1;
                String label = null;
                String pname = null;
                String wname = null;
                String fivep = null;
                String threep = null;
                String result = null;
                String gi = null;
                String genbank = null;
                String genename = null;
                String genesymbol = null;
                String panumber = null;
                
                if(isPending) {
                    pname = rs.getString(3);
                    gi = rs.getString(4);
                    genbank = rs.getString(5);
                    genename = rs.getString(6);
                    genesymbol = rs.getString(7);
                    panumber = rs.getString(8);
                } else {
                    type = rs.getString(3);
                    well = rs.getInt(4);
                    containerid = rs.getInt(5);
                    label = rs.getString(6);
                    pname = rs.getString(7);
                    wname = rs.getString(8);
                    fivep = rs.getString(9);
                    threep = rs.getString(10);
                    result = rs.getString(11);
                    gi = rs.getString(12);
                    genbank = rs.getString(13);
                    genename = rs.getString(14);
                    genesymbol = rs.getString(15);
                    panumber = rs.getString(16);
                }
                QueryInfo info = new QueryInfo(sequenceid, gi, genbank, containerid, label, well);
                info.setType(type);
                info.setProject(pname);
                info.setWorkflow(wname);
                info.setFivep(fivep);
                info.setThreep(threep);
                info.setResult(result);
                info.setGeneName(genename);
                info.setGeneSymbol(genesymbol);
                info.setPanumber(panumber);
                info.setStatus(status);
                queryInfoList.add(info);
            }
        } catch(Exception e) {
            setMessage(e.getMessage());
            return false;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return true;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
    
    public ArrayList getQueryInfoList() {
        return queryInfoList;
    }
    
    private String convertToSqlList(ArrayList searchTerm) {
        if(searchTerm == null || searchTerm.isEmpty())
            return null;
        
        String sqlList = null;
        
        for (int i=0; i<searchTerm.size(); i++) {
            String term = (String)(searchTerm.get(i));
            if(term != null) {
                if(sqlList == null) {
                    sqlList = "'"+term+"'";
                } else {
                    sqlList = sqlList+","+"'"+term+"'";
                }
            }
        }
        
        return sqlList;
    }
    
    public static void main(String args[]) {
        QueryManager manager = new QueryManager();
        ArrayList searchTerm = new ArrayList();
        
        /** Testing for "PENDING" status
        searchTerm.add("17389294");
        searchTerm.add("17389297");
        searchTerm.add("17389300");
        searchTerm.add("17389303");
        searchTerm.add("17389306");
        searchTerm.add("17389309");
        searchTerm.add("17389312");
        searchTerm.add("17389315");
        searchTerm.add("17389318");
        searchTerm.add("17389321");
        searchTerm.add("17389324");
        searchTerm.add("17389327");
        searchTerm.add("17389330");
        String searchType = "GI";
        String flexstatus = "PENDING";
        int project = -1;
        int workflow = -1;
        String plate = "gel";
         */
        
        /** Testing for "INPROCESS" status */
        searchTerm.add("10047079");
        searchTerm.add("10047083");
        searchTerm.add("10047105");
        searchTerm.add("1006708");
        searchTerm.add("1006710");
        searchTerm.add("1006712");
        searchTerm.add("1006714");
        searchTerm.add("1006716");
        searchTerm.add("1006718");
        searchTerm.add("1006720");
        searchTerm.add("1006721");
        searchTerm.add("1006723");
        String searchType = "GI";
        String flexstatus = "INPROCESS";
        int project = 5;
        int workflow = 6;
        String plate = "gel";                                                                      
        
        if(manager.doQuery(searchTerm, searchType, flexstatus, project, workflow, plate)) {
            ArrayList queryInfoList = manager.getQueryInfoList();
            if(queryInfoList==null) {
                System.out.println("Error: something wrong with query");
            } else {
                for(int i=0; i<queryInfoList.size(); i++) {
                    QueryInfo info = (QueryInfo)(queryInfoList.get(i));
                    System.out.print("Sequence ID: "+info.getId()+"\t");
                    System.out.print("GI: "+info.getGi()+"\t");
                    System.out.print("Gene Name: "+info.getGeneName()+"\t");
                    System.out.print("Genbank Accesion: "+info.getGenbankAcc()+"\t");
                    System.out.print("Gene Symbol: "+info.getGeneSymbol()+"\t");
                    System.out.print("PA Number: "+info.getPanumber()+"\t");
                    System.out.print("Plate ID: "+info.getPlateid()+"\t");
                    System.out.print("Plate Label: "+info.getLabel()+"\t");
                    System.out.print("Well: "+info.getWell()+"\t");
                    System.out.print("Construct Type: "+info.getType()+"\t");
                    System.out.print("5p Oligo: "+info.getFivep()+"\t");
                    System.out.print("3p Oligo: "+info.getThreep()+"\t");
                    System.out.print("Project: "+info.getProject()+"\t");
                    System.out.print("Workflow: "+info.getWorkflow()+"\t");
                    System.out.print("Result: "+info.getResult()+"\t");
                    System.out.print("Flex Status: "+info.getStatus()+"\t");
                    System.out.println();
                }
            }
        } else {
            System.out.println("Error: "+manager.getMessage());
        }
        
        System.exit(0);
    }
}
