/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import core.TemplateAnalysis;
import dao.DaoException;
import dao.DatabaseConnection;
import dao.TemplateDao;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dongmei
 */
public class QueryManager {

    public List<TemplateAnalysis> queryTemplateAnalysis(List<String> terms, String type, int maxMutation,
            double minCoverage, boolean isLongest,  boolean isOrfeome, String searchSelection, boolean isGateway,
            String gatewaySelection, boolean isDownload, int begin, int end) 
            throws DaoException {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection conn = instance.getConnection();
        if (conn == null) {
            throw new DaoException("Cannot connect to the database.");
        }

        TemplateDao dao = new TemplateDao(conn);
        List<TemplateAnalysis> ta = dao.queryTemplateAnalysis(getUniqueTerms(terms), type, maxMutation, minCoverage, isLongest, 
                isOrfeome, searchSelection, isGateway, gatewaySelection, isDownload, begin, end);
        
        instance.closeConnection(conn);
        return ta;
    }

    public List<TemplateAnalysis> queryTemplateAnalysis(int maxMutation, double minCoverage, boolean isLongest,
            boolean isOrfeome, String searchSelection, boolean isGateway, String gatewaySelection,
            int first, int pageSize, String sortField, Map<String, String> filters) throws DaoException {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection conn = instance.getConnection();
        if (conn == null) {
            throw new DaoException("Cannot connect to the database.");
        }

        TemplateDao dao = new TemplateDao(conn);
        List<TemplateAnalysis> ta = dao.queryTemplateAnalysis(maxMutation, minCoverage, isLongest, isOrfeome, searchSelection, 
                isGateway, gatewaySelection, first, pageSize, sortField, filters);
       
        instance.closeConnection(conn);
        return ta;
    }

    public TemplateAnalysis queryTemplateAnalysis(int id) throws DaoException {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection conn = instance.getConnection();
        if (conn == null) {
            throw new DaoException("Cannot connect to the database.");
        }

        TemplateDao dao = new TemplateDao(conn);
        TemplateAnalysis ta = dao.queryTemplateAnalysis(id);
       
        instance.closeConnection(conn);
        return ta;
    }
    
    private List getUniqueTerms(List terms) {
        Set s = new TreeSet(terms);
        return new ArrayList(s);
    }
        
    public int queryMaxAnalysisid() throws DaoException {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection conn = instance.getConnection();
        if (conn == null) {
            throw new DaoException("Cannot connect to the database.");
        }

        TemplateDao dao = new TemplateDao(conn);
        int id = dao.queryMaxAnalysisid();
        
        instance.closeConnection(conn);
        return id;
    }
}
