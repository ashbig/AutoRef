/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import core.TemplateAnalysis;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author dongmei
 */
public class LazySorter implements Comparator<TemplateAnalysis> {

    private String sortField;
    
    private SortOrder sortOrder;
    
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(TemplateAnalysis ta1, TemplateAnalysis ta2) {
        try {
            Object value1 = TemplateAnalysis.class.getField(this.sortField).get(ta1);
            Object value2 = TemplateAnalysis.class.getField(this.sortField).get(ta2);

            int value = ((Comparable)value1).compareTo(value2);
            
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
