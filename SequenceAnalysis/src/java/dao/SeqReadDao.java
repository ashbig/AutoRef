/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import core.SeqRead;
import core.SeqValidation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Lab User
 */
public class SeqReadDao {

    public static final String TYPE_SEQ = "seq";
    public static final String TYPE_CDS = "cds";
    public static final String TYPE_PROTEIN = "proteinseq";

    public void queryGeneInfo(List<SeqRead> reads) throws DaoException {
        String sql = "select accession_nt, geneid from gene2rerseq where gi_nt=?";
        String sql2 = "select symbol, synonyms from geneinfo where geneid=?";

        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection connection = instance.getConnection();
        if (connection == null) {
            throw new DaoException("Cannot connect to database.");
        }

        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt2 = connection.prepareStatement(sql2);
            for (SeqRead read : reads) {
                List<SeqValidation> validations = read.getValidations();
                if (validations != null) {
                    for (SeqValidation validation : validations) {
                        if (validation != null) {
                            stmt.setString(1, validation.getSubjectid());
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                String accession = rs.getString(1);
                                String geneid = rs.getString(2);
                                if (accession != null) {
                                    validation.setAccession(accession);
                                }
                                if (geneid != null) {
                                    validation.setGeneid(geneid);
                                }

                                stmt2.setString(1, geneid);
                                rs2 = stmt2.executeQuery();
                                if (rs2.next()) {
                                    String symbol = rs2.getString(1);
                                    String synonyms = rs2.getString(2);
                                    if (symbol != null) {
                                        validation.setSymbol(symbol);
                                    }
                                    if (synonyms != null) {
                                        validation.setSynonyms(synonyms);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = "Error occured while getting the gene information from the database.";
            throw new DaoException(error);
        } finally {
            try {
                rs.close();
                rs2.close();
                stmt.close();
                stmt2.close();
                connection.close();
            } catch (Exception ex) {
            }
        }
    }

    public String queryRefseq(String gi, String type) throws DaoException {
        String sql = "select " + type + " from refseq where gi_nt=?";

        DatabaseConnection instance = DatabaseConnection.getInstance();
        Connection connection = instance.getConnection();
        if (connection == null) {
            throw new DaoException("Cannot connect to database.");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, gi);
            rs = stmt.executeQuery();
            String seq = "";
            if (rs.next()) {
                seq = rs.getString(1);
            }
            return seq;
        } catch (Exception ex) {
            ex.printStackTrace();
            String error = "Error occured while getting sequence from the database.";
            throw new DaoException(error);
        } finally {
            try {
                rs.close();
                stmt.close();
                connection.close();
            } catch (Exception ex) {
            }
        }
    }
}
