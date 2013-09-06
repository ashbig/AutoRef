/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import controller.QueryAnalysisController;
import core.TemplateAnalysis;
import dao.DaoException;
import java.util.ArrayList;  
import java.util.List;  
import java.util.Map;  
import org.primefaces.model.LazyDataModel;  
import org.primefaces.model.SortOrder;  

/**
 *
 * @author dongmei
 */
   
public class TemplateAnalysisLazyDataModel extends LazyDataModel<TemplateAnalysis> {  
      
    private List<TemplateAnalysis> data;
    private QueryAnalysisController controller;  
    
    public TemplateAnalysisLazyDataModel(QueryAnalysisController m) {  
        this.controller = m;  
        try {
            data = controller.queryTemplateAnalysis(1, 10, null, null);
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }  
      
    @Override  
    public TemplateAnalysis getRowData(String rowKey) {  
        for(TemplateAnalysis ta : data) {  
            String id = ""+ta.getId();
            if(id.equals(rowKey))  
                return ta;  
        }  
  
        return null;  
    }  
  
    @Override  
    public Object getRowKey(TemplateAnalysis ta) {  
        return ta.getId();  
    }  
  
    @Override  
    public List<TemplateAnalysis> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {  
        List<TemplateAnalysis> l = new ArrayList<TemplateAnalysis>();  
        try {
            l = controller.queryTemplateAnalysis(first, pageSize, sortField, filters);
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
        return l;
    }  
}  