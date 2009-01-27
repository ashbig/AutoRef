/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.blast;

/**
 *
 * @author DZuo
 */
public class BlastProgram {
    private String name;
    private String value;
    
    public BlastProgram(String name, String value) {
        setName(name);
        setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
