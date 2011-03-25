/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.importexport.genbank;

/**
 *
 * @author Dongmei
 */
public class GenbankInfoName {
    private String nametype;
    private String namevalue;

    public GenbankInfoName(String type, String value) {
        this.nametype = type;
        this.namevalue = value;
    }
    
    public String getNametype() {
        return nametype;
    }

    public void setNametype(String nametype) {
        this.nametype = nametype;
    }

    public String getNamevalue() {
        return namevalue;
    }

    public void setNamevalue(String namevalue) {
        this.namevalue = namevalue;
    }
}
