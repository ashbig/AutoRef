/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import controller.QueryAnalysisController;
import core.Refseq;
import core.Template;
import core.TemplateAnalysis;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dongmei
 */
public class TemplateDao {

    public static final String SEARCH_TYPE_GENEID = "geneid";
    public static final String SEARCH_TYPE_SYMBOL = "symbol";
    public static final String SEARCH_TYPE_ACCESSION = "accession";
    public static final String SEARCH_TYPE_GI = "gi";
    public static final String SEARCH_TYPE_PACCESSION = "proteinacc";
    public static final String SEARCH_TYPE_PGI = "proteingi";
    private Connection connection;

    public TemplateDao(Connection connection) {
        this.connection = connection;
    }

    public List<TemplateAnalysis> queryTemplateAnalysis(int maxMutation, double minCoverage, boolean isLongest,
            boolean isOrfeome, String searchSelection, boolean isGateway, String gatewaySelection,
            int first, int pageSize, String sortField, Map<String, String> filters) throws DaoException {
        String sql = "select a.mutation, a.coverage, r.geneid, r.accession, r.proteinacc, r.cdslength,"
                + " r.longest, t.originalcloneid, t.vector, t.source, t.format, a.id, r.symbol,"
                + " @rownum:=@rownum+1 rank"
                + " from templateanalysis a, refseq r, template t, (SELECT @rownum:=0) rt"
                + " where a.refseqid=r.id and t.cloneid=a.cloneid";

        sql = appendSql(sql, null, null, maxMutation, minCoverage, isLongest, isOrfeome, searchSelection,
                isGateway, gatewaySelection, 0, 0, false);
        
        if (filters != null) {
            for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                try {
                    String filterProperty = it.next();
                    String filterValue = filters.get(filterProperty);
                    sql += " and " + filterProperty + "=" + filterValue;
                } catch (Exception e) {
                }
            }
        }
        if (sortField != null) {
            sql += " order by " + sortField;
        } else {
            sql += " order by a.id";
        }

        sql = "select n.* from (" + sql + ") as n where rank between " + first + " and " + (first + pageSize);
        System.out.println(sql);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<TemplateAnalysis> analysisList = new ArrayList<TemplateAnalysis>();
        try {
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                TemplateAnalysis ta = retrieveTemplateAnalysis(rs);
                analysisList.add(ta);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = "Error occured while searching the database.";
            throw new DaoException(error);
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception ex) {
            }
        }
        return analysisList;
    }

    public List<TemplateAnalysis> queryTemplateAnalysis(List<String> terms, String type, int maxMutation,
            double minCoverage, boolean isLongest, boolean isOrfeome, String searchSelection, boolean isGateway,
            String gatewaySelection, boolean isDownload, int begin, int end)
            throws DaoException {
        String sql = "select a.mutation, a.coverage, r.geneid, r.accession, r.proteinacc, r.cdslength,"
                + " r.longest, t.originalcloneid, t.vector, t.source, t.format, a.id, r.symbol"
                + " from templateanalysis a, refseq r, template t"
                + " where a.refseqid=r.id and t.cloneid=a.cloneid";
        if (isDownload) {
            sql = "select a.refseqid, a.cloneid, a.pid, a.alength, a.mismatch, a.gap, a.evalue, a.score,"
                    + " a.mutation, a.coverage, a.analysistype, r.geneid, r.symbol, r.status, r.accession, r.gi,"
                    + " r.proteinacc, r.proteingi, r.cds, r.proteinseq, r.cdslength, r.proteinlength, r.longest,"
                    + " t.originalcloneid, t.vector, t.source, t.format, t.sequence, t.isorfeome, a.id,"
                    + " t.isgateway, t.plate, t.pos, t.posx, t.posy"
                    + " from templateanalysis a, refseq r, template t"
                    + " where a.refseqid=r.id and t.cloneid=a.cloneid";
        }

        sql = appendSql(sql, terms, type, maxMutation, minCoverage, isLongest, isOrfeome, searchSelection,
                isGateway, gatewaySelection, begin, end, true);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<TemplateAnalysis> analysisList = new ArrayList<TemplateAnalysis>();
        String errorTerm = "";
        try {
            stmt = connection.prepareStatement(sql);
            if (terms != null && terms.size() > 0) {
                for (String term : terms) {
                    errorTerm = term;
                    stmt.setString(1, term);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        if (isDownload) {
                            TemplateAnalysis ta = retrieveTemplateAnalysisAll(rs);
                            analysisList.add(ta);
                        } else {
                            TemplateAnalysis ta = retrieveTemplateAnalysis(rs);
                            analysisList.add(ta);
                        }
                    }
                }
            } else {
                rs = stmt.executeQuery();
                while (rs.next()) {
                    if (isDownload) {
                        TemplateAnalysis ta = retrieveTemplateAnalysisAll(rs);
                        analysisList.add(ta);
                    } else {
                        TemplateAnalysis ta = retrieveTemplateAnalysis(rs);
                        analysisList.add(ta);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = "Error occured while searching the database.";
            if (terms != null && terms.size() > 0) {
                error += " The search term that failed search is " + errorTerm + ".";
            }
            throw new DaoException(error);
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception ex) {
            }
        }
        return analysisList;
    }

    public TemplateAnalysis queryTemplateAnalysis(int id) throws DaoException {
        String sql = "select a.refseqid, a.cloneid, a.pid, a.alength, a.mismatch, a.gap, a.evalue, a.score,"
                + " a.mutation, a.coverage, a.analysistype, r.geneid, r.symbol, r.status, r.accession, r.gi,"
                + " r.proteinacc, r.proteingi, r.cds, r.proteinseq, r.cdslength, r.proteinlength, r.longest,"
                + " t.originalcloneid, t.vector, t.source, t.format, t.sequence, t.isorfeome, a.id,"
                + " t.isgateway, t.plate, t.pos, t.posx, t.posy"
                + " from templateanalysis a, refseq r, template t"
                + " where a.refseqid=r.id and t.cloneid=a.cloneid and a.id=" + id;

        Statement stmt = null;
        ResultSet rs = null;
        TemplateAnalysis analysis = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                analysis = retrieveTemplateAnalysisAll(rs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = "Error occured while searching the database.";
            throw new DaoException(error);
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (Exception ex) {
            }
        }
        return analysis;
    }

    private String appendSql(String sql, List<String> terms, String type, int maxMutation,
            double minCoverage, boolean isLongest, boolean isOrfeome, String searchSelection,
            boolean isGateway, String gatewaySelection, int begin, int end, boolean isSort) throws DaoException {

        if (terms != null && terms.size() > 0) {
            if (type == null) {
                throw new DaoException("Search type is not provided.");
            }
            if (SEARCH_TYPE_GENEID.equals(type.trim())) {
                sql += " and r.geneid=?";
            } else if (SEARCH_TYPE_SYMBOL.equals(type.trim())) {
                sql += " and r.symbol=?";
            } else if (SEARCH_TYPE_ACCESSION.equals(type.trim())) {
                sql += " and r.accession=?";
            } else if (SEARCH_TYPE_GI.equals(type.trim())) {
                sql += " and r.gi=?";
            } else if (SEARCH_TYPE_PACCESSION.equals(type.trim())) {
                sql += " and r.proteinacc=?";
            } else if (SEARCH_TYPE_PGI.equals(type.trim())) {
                sql += " and r.proteingi=?";
            } else {
                throw new DaoException("Invalid search type.");
            }
        }

        if (maxMutation != -1) {
            sql += " and a.mutation<=" + maxMutation;
        }

        sql += " and a.coverage>=" + minCoverage;

        if (isLongest) {
            sql += " and r.longest=1";
        }

        if (isOrfeome) {
            if (QueryAnalysisController.SEARCH_ORFEOME.equals(searchSelection)) {
                sql += " and t.isorfeome=1";
            } else if (QueryAnalysisController.NO_SEARCH_ORFEOME.equals(searchSelection)) {
                sql += " and t.isorfeome is null";
            }
        }

        if (isGateway) {
            if (QueryAnalysisController.SEARCH_GATEWAY.equals(gatewaySelection)) {
                sql += " and t.isgateway=1";
            } else if (QueryAnalysisController.NO_SEARCH_GATEWAY.equals(gatewaySelection)) {
                sql += " and t.isgateway is null";
            }
        }

        if (begin > 0) {
            sql += " and a.id between " + begin + " and " + end;
        }

        if (isSort) {
            sql += " order by a.id";
        }

        return sql;
    }

    private TemplateAnalysis retrieveTemplateAnalysis(ResultSet rs) throws Exception {
        int mutation = rs.getInt(1);
        double coverage = rs.getDouble(2);
        int geneid = rs.getInt(3);
        String accession = rs.getString(4);
        String proteinacc = rs.getString(5);
        int cdslength = rs.getInt(6);
        int longest = rs.getInt(7);
        String cloneid = rs.getString(8);
        String vector = rs.getString(9);
        String source = rs.getString(10);
        String format = rs.getString(11);
        int id = rs.getInt(12);
        String symbol = rs.getString(13);

        TemplateAnalysis ta = new TemplateAnalysis();
        ta.setId(id);
        ta.setMutation(mutation);
        ta.setCoverage(coverage);

        Refseq refseq = new Refseq();
        refseq.setGeneid(geneid);
        refseq.setAccession(accession);
        refseq.setProteinacc(proteinacc);
        refseq.setCdslength(cdslength);
        refseq.setLongest(longest);
        refseq.setSymbol(symbol);

        Template template = new Template();
        template.setOriginalcloneid(cloneid);
        template.setVector(vector);
        template.setSource(source);
        template.setFormat(format);

        ta.setRefseq(refseq);
        ta.setTemplate(template);

        return ta;
    }

    private TemplateAnalysis retrieveTemplateAnalysisAll(ResultSet rs) throws Exception {
        int refseqid = rs.getInt(1);
        int cloneid = rs.getInt(2);
        double pid = rs.getDouble(3);
        int alength = rs.getInt(4);
        int mismatch = rs.getInt(5);
        int gap = rs.getInt(6);
        double evalue = rs.getDouble(7);
        double score = rs.getDouble(8);
        int mutation = rs.getInt(9);
        double coverage = rs.getDouble(10);
        String analysistype = rs.getString(11);
        int geneid = rs.getInt(12);
        String symbol = rs.getString(13);
        String status = rs.getString(14);
        String accession = rs.getString(15);
        String gi = rs.getString(16);
        String proteinacc = rs.getString(17);
        String proteingi = rs.getString(18);
        String cds = rs.getString(19);
        String proteinseq = rs.getString(20);
        int cdslength = rs.getInt(21);
        int proteinlength = rs.getInt(22);
        int longest = rs.getInt(23);
        String originalcloneid = rs.getString(24);
        String vector = rs.getString(25);
        String source = rs.getString(26);
        String format = rs.getString(27);
        String sequence = rs.getString(28);
        int isorfeome = rs.getInt(29);
        int id = rs.getInt(30);
        int isgateway = rs.getInt(31);
        String plate = rs.getString(32);
        int pos = rs.getInt(33);
        String posx = rs.getString(34);
        int posy = rs.getInt(35);

        Refseq refseq = new Refseq(refseqid, geneid, symbol, status, accession, gi, proteinacc, proteingi, cds, proteinseq, cdslength, proteinlength, longest);
        Template template = new Template(cloneid, originalcloneid, vector, source, format, geneid, sequence, isorfeome);
        template.setIsgateway(isgateway);
        template.setPlate(plate);
        template.setPosition(pos);
        template.setPosx(posx);
        template.setPosy(posy);
        TemplateAnalysis analysis = new TemplateAnalysis(id, refseqid, geneid, analysistype, pid, alength, mismatch, gap, evalue, score, mutation, coverage);
        analysis.setRefseq(refseq);
        analysis.setTemplate(template);
        return analysis;
    }

    public int queryMaxAnalysisid() {
        String sql = "select max(id) from templateanalysis";
        Statement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            return 0;
        }
        return id;
    }
}
