/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import plasmid.coreobject.Institution;
import plasmid.database.DatabaseException;
import plasmid.database.DatabaseTransaction;

/**
 *
 * @author Dongmei
 */
public class InstitutionManager extends TableManager {

    private List<String> foundInstitutions;
    private List<String> noFoundInstitutions;

    /** Creates a new instance of AuthorManager */
    public InstitutionManager(Connection conn) {
        super(conn);
    }

    public void queryInstitutionNames(List<String> institutions) throws DatabaseException {
        String sql = "select name from institution where lower(name)=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        setFoundInstitutions(new ArrayList<String>());
        setNoFoundInstitutions(new ArrayList<String>());
        String error = null;

        try {
            stmt = conn.prepareStatement(sql);
            for (String institution : institutions) {
                error = institution;
                if (institution == null || institution.trim().length() == 0) {
                    continue;
                }
                stmt.setString(1, institution.trim().toLowerCase());
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    String name = rs.getString(1);
                    getFoundInstitutions().add(name);
                } else {
                    getNoFoundInstitutions().add(institution);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Error occured while trying to find institution: " + error);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public void insertInstitutions(List<Institution> institutions) throws DatabaseException {
        String sql = "insert into institution(name,category,country,ismember) values(?,?,?,'N')";

        PreparedStatement stmt = null;
        String error = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (Institution institution : institutions) {
                error = institution.getName();
                stmt.setString(1, institution.getName());
                stmt.setString(2, institution.getCategory());
                stmt.setString(3, institution.getCountry());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Cannot add institution: " + error);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void insertMembers(List<String> institutions) throws DatabaseException {
        String sql = "insert into mtarule(mtaid, mtacode, INSTITUTIONNAME)" +
                " select distinct mtaid, mtacode,? from mtarule";
        String sql2 = "update institution set ismember='Y' where name=?";
        String sql3 = "select count(*) from mtarule where INSTITUTIONNAME=?";

        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        String error = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            for (String institution : institutions) {
                error = institution;

                stmt2.setString(1, institution);
                DatabaseTransaction.executeUpdate(stmt2);
                
                stmt3.setString(1, institution);
                rs = DatabaseTransaction.executeQuery(stmt3);
                if (rs.next()) {
                    int n = rs.getInt(1);
                    if (n == 0) {
                        stmt.setString(1, institution);
                        DatabaseTransaction.executeUpdate(stmt);
                    }
                } else {
                    throw new Exception("Error occured.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Cannot add member: " + error);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
        }
    }

    public List<String> getFoundInstitutions() {
        return foundInstitutions;
    }

    public void setFoundInstitutions(List<String> foundInstitutions) {
        this.foundInstitutions = foundInstitutions;
    }

    public List<String> getNoFoundInstitutions() {
        return noFoundInstitutions;
    }

    public void setNoFoundInstitutions(List<String> noFoundInstitutions) {
        this.noFoundInstitutions = noFoundInstitutions;
    }
}
