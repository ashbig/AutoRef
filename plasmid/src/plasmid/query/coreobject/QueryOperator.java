/*
 * QueryOperator.java
 *
 * Created on February 16, 2006, 3:46 PM
 */

package plasmid.query.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class QueryOperator {
    private List values;
    private String logicOperator;
    
    /** Creates a new instance of QueryManipulator */
    public QueryOperator() {
    }
    
    public QueryOperator(List l, String s) {
        this.values = l;
        this.logicOperator = s;
    }
    
    public List getValues() {return values;}
    public String getLogicOperator() {return logicOperator;}
}
