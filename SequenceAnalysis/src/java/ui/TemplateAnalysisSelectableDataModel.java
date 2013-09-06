/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import core.TemplateAnalysis;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author dongmei
 */
public class TemplateAnalysisSelectableDataModel extends ListDataModel<TemplateAnalysis> 
implements SelectableDataModel<TemplateAnalysis>, Serializable {    
  
    public TemplateAnalysisSelectableDataModel() {  
    }  
  
    public TemplateAnalysisSelectableDataModel(List<TemplateAnalysis> data) {  
        super(data);  
    }  
      
    @Override  
    public TemplateAnalysis getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
          
        List<TemplateAnalysis> tas = (List<TemplateAnalysis>) getWrappedData();  
          
        for(TemplateAnalysis ta : tas) {  
            if((""+ta.getId()).equals(rowKey))  
                return ta;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(TemplateAnalysis ta) {  
        return ta.getId();  
    }  
}  
