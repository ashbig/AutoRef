/*
 * QueryProcessManager.java
 *
 * Created on October 13, 2005, 3:12 PM
 */

package plasmid.process;

import java.util.*;
import java.sql.*;
import java.io.*;

import plasmid.coreobject.*;
import plasmid.query.coreobject.*;
import plasmid.util.*;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.*;
import plasmid.query.handler.*;
import plasmid.form.VectorSearchForm;

/**
 *
 * @author  DZuo
 */
public class QueryProcessManager {
    
    /** Creates a new instance of QueryProcessManager */
    public QueryProcessManager() {
    }
    
    public List getAllEmptyVectors(List clonetypes, List restrictions) {
        return getAllEmptyVectors(clonetypes, restrictions, false);
    }
    
    public List getAllEmptyVectors(List clonetypes, List restrictions, boolean isPSI) {
        DatabaseTransaction t = null;
        Connection conn = null;
        List found = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            List cloneids = manager.queryCloneidsByCloneType(clonetypes, isPSI);
            Map clones = manager.queryAvailableClonesByCloneid(cloneids, false, true, false, restrictions, clonetypes, null);
            Set ks = clones.keySet();
            Iterator iter = ks.iterator();
            while(iter.hasNext()) {
                String k = (String)iter.next();
                CloneInfo c = (CloneInfo)clones.get(k);
                if(c != null) {
                    found.add(c);
                }
            }
            return found;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getCollections(String status, List restrictions) {
        DatabaseTransaction t = null;
        Connection conn = null;
        List found = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CollectionManager manager = new CollectionManager(conn);
            found = manager.getAllCollections(status, restrictions);
            return found;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public CollectionInfo getCollection(String name, String status, List restrictions, boolean isCloneRestore) {
        DatabaseTransaction t = null;
        Connection conn = null;
        CollectionInfo c = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CollectionManager manager = new CollectionManager(conn);
            c = manager.getCollection(name, status, restrictions, isCloneRestore, false);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return c;
    }
    
    public int getClonenumInCollection(String name) {
        return CollectionManager.getClonenumInCollection(name);
    }
    
    public List queryClonesByVector(User user, Set vectorids, String species, String status) {
        return queryClonesByVector(user,vectorids,species,status,true);
    }
    
    public List queryClonesByVector(User user, Set vectors, String species, String status, boolean vectorid) {
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        restrictions.add(Clone.NON_PROFIT);
        if(user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }
        
        VectorQueryHandler handler = new VectorQueryHandler();
        List clones = null;
        if(vectorid) {
            clones = handler.queryClones(vectors, restrictions, species, status);
        } else {
            clones = handler.queryClonesByVectornames(vectors, restrictions, species, status);
        }
        
        return clones;
    }
    
    public List queryCloneInfosByClones(List clones) {
        DatabaseTransaction t = null;
        Connection conn = null;
        List found = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            found = manager.performQueryClones(clones, true, true, true, false);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return found;
    }
    
    public List getVectorNamesFromClones(List clones) {
        List found = new ArrayList();
        Set vectors = new TreeSet();
        
        if(clones == null)
            return found;
        
        for(int i=0; i<clones.size(); i++) {
            Clone c = (Clone)clones.get(i);
            String vector = c.getVectorname();
            vectors.add(vector);
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager manager = new VectorManager(conn);
            found = manager.getVectorsByName(vectors);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return found;
    }
    
    public List getVectorQueryOperators(Map types, VectorSearchForm form) {
        Set categories = types.keySet();
        Iterator iter = categories.iterator();
        int i=0;
        int index=i;
        List operators = new ArrayList();
        while(iter.hasNext()) {
            String category = (String)iter.next();
            List properties = (List)types.get(category);
            String logicOperator = form.getLogicOperator(i);
            int j=0;
            List checkedProperties = new ArrayList();
            while(j<properties.size()) {
                VectorProperty vp = (VectorProperty)properties.get(j);
                String property = vp.getPropertyType();
                boolean b = form.getVectortype(index);
                if(b) {
                    checkedProperties.add(property);
                }
                index++;
                j++;
            }
            
            if(checkedProperties.size()>0) {
                QueryOperator q = new QueryOperator(checkedProperties, logicOperator);
                operators.add(q);
            }
            i++;
        }
        
        return operators;
    }
    
    public Set getVectoridFromQueryOperators(List operators, String logicOperator) {
        Set allVectorids = null;
        for(int i=0; i<operators.size();i++) {
            QueryOperator op = (QueryOperator)operators.get(i);
            String logicOp = op.getLogicOperator();
            List l = op.getValues();
            Set vectorids = null;
            for(int j=0; j<l.size(); j++) {
                String s = (String)l.get(j);
                Set vectoridSet = VectorManager.getVectoridByProperty(s);
                if(vectorids == null) {
                    vectorids = vectoridSet;
                } else {
                    if(logicOp.equals(Constants.AND)) {
                        vectorids.retainAll(vectoridSet);
                    } else {
                        vectorids.addAll(vectoridSet);
                    }
                }
            }
            if(allVectorids == null) {
                allVectorids = vectorids;
            } else {
                if(logicOperator.equals(Constants.AND)) {
                    allVectorids.retainAll(vectorids);
                } else {
                    allVectorids.addAll(vectorids);
                }
            }
        }
        
        return allVectorids;
    }
    
    public static List getCloneids(Collection cloneinfos) {
        List l = new ArrayList();
        if(cloneinfos == null)
            return l;
        
        Iterator iter = cloneinfos.iterator();
        while(iter.hasNext()) {
            CloneInfo c = (CloneInfo)iter.next();
            l.add((new Integer(c.getCloneid())).toString());
        }
        
        return l;
    }
    
    public Set processAdvancedQuery(Set foundSet, GeneQueryHandler handler, List restrictions, String species, boolean isClonename) throws Exception {
        StringConvertor sc = new StringConvertor();
        handler.setIsClonename(isClonename);
        
        if(foundSet == null) {
            handler.doQuery(restrictions, null, species, -1, -1, null, Clone.AVAILABLE);
            foundSet = new TreeSet(new CloneInfoComparator());
            foundSet.addAll(handler.convertFoundToCloneinfo());
        } else {
            List cloneids = QueryProcessManager.getCloneids(foundSet);
            int start=0;
            while(start<cloneids.size()) {
                int end = start+1000;
                if(end>cloneids.size())
                    end = cloneids.size();
                
                List l = cloneids.subList(start, end);
                String s = sc.convertFromListToSqlList(l);
                handler.doQuery(restrictions, null, species, -1, -1, null, Clone.AVAILABLE, "(select * from clone where cloneid in ("+s+"))");
                start += 1000;
                
                foundSet = new TreeSet(new CloneInfoComparator());
                foundSet.addAll(handler.convertFoundToCloneinfo());
            }
        }
        return foundSet;
    }
}
