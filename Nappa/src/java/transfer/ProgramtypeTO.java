/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transfer;

/**
 *
 * @author dzuo
 */
public class ProgramtypeTO {
    public static final String TYPE_PLATE96TO384 = "96 Well Plate to 384 Well Plate";
    public static final String TYPE_384TOSLIDE = "Slide Print";

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
