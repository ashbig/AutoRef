/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author DZuo
 */
public class VariableTO implements Serializable {
    private String type;
    private String value;
    private String extra;

    public VariableTO(String t, String v, String e) {
        setType(t);
        setValue(v);
        setExtra(e);
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
