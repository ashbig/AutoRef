/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.process;

import java.sql.Connection;
import java.util.List;
import plasmid.coreobject.Institution;
import plasmid.database.DatabaseException;
import plasmid.database.DatabaseManager.InstitutionManager;
import plasmid.database.DatabaseTransaction;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class InstitutionProcessManager {
    public void addInstitutions(List<Institution> institutions) throws DatabaseException {
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
             conn = t.requestConnection();
            InstitutionManager manager = new InstitutionManager(conn);
            manager.insertInstitutions(institutions);
            DatabaseTransaction.commit(conn);
        } catch (DatabaseException ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List<String> addEmtaMembers(List<String> institutions) throws DatabaseException, Exception {
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
             conn = t.requestConnection();
            InstitutionManager manager = new InstitutionManager(conn);
            manager.queryInstitutionNames(institutions);
            List<String> noFound = manager.getNoFoundInstitutions();
            if(noFound.size()>0) {
                throw new DatabaseException("The following institutions are not found in the database. Please add them to the database first: \n"+StringConvertor.staticConvertFromListToString(noFound));
            }
            List<String> found = manager.getFoundInstitutions();
            manager.insertMembers(found);
            DatabaseTransaction.commit(conn);
            return found;
        } catch (DatabaseException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error occured while adding EP-MTA members");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List<String> getNoFoundInstitutions(List<String> names) throws DatabaseException, Exception {
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
             conn = t.requestConnection();
            InstitutionManager manager = new InstitutionManager(conn);
            manager.queryInstitutionNames(names);
            List<String> noFound = manager.getNoFoundInstitutions();
            return noFound;
        } catch (DatabaseException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error occured while checking institutions.");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
