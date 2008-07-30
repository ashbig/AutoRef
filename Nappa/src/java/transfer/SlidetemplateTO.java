/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class SlidetemplateTO implements Serializable {
    private SlidelayoutTO layout;
    private String name;
    private String description;
    private String researcher;
    private String date;
    private String status;

    public SlidetemplateTO(SlidelayoutTO l,String name, String desc, String researcher, String date, String status) {
        this.setLayout(l);
        this.setName(name);
        this.setDescription(desc);
        this.setResearcher(researcher);
        this.setDate(date);
        this.setStatus(status);
    }
    
    public SlidelayoutTO getLayout() {
        return layout;
    }

    public void setLayout(SlidelayoutTO layout) {
        this.layout = layout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResearcher() {
        return researcher;
    }

    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
