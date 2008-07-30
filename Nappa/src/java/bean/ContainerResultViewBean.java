/*
 * ContainerResultViewBean.java
 *
 * Created on December 11, 2007, 11:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bean;

import java.util.List;
import javax.faces.model.DataModel;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class ContainerResultViewBean {
    private String barcode;
    private DataModel header;
    private DataModel results;
    
    /** Creates a new instance of ContainerResultViewBean */
    public ContainerResultViewBean() {
    }
    
    public ContainerResultViewBean(String b, DataModel header, DataModel results) {
        this.setBarcode(b);
        this.setHeader(header);
        this.setResults(results);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String b) {
        this.barcode = b;
    }

    public DataModel getHeader() {
        return header;
    }

    public void setHeader(DataModel header) {
        this.header = header;
    }

    public DataModel getResults() {
        return results;
    }

    public void setResults(DataModel results) {
        this.results = results;
    }
    
    public SampleTO getRowLabel() {
        SampleTO plateValue = null;
        
        if(getResults().isRowAvailable()) {
            
            List list = (List)getResults().getRowData();
            
            plateValue = (SampleTO)list.get(0);
            
        }
        return plateValue;
    }
    
    public SampleTO getPlateValue() {
        SampleTO plateValue = null;
        
        if(getResults().isRowAvailable() && getHeader().isRowAvailable()) {
            
            List list = (List)getResults().getRowData();
            
            plateValue = (SampleTO)list.get(getHeader().getRowIndex());
            
        }
        return plateValue;
    }
}
