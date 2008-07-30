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
public class ProgramcontainerTO implements Serializable {
    public static final String PLATE96 = "96-Well Plate";
    public static final String PLATE384 = "384-Well Plate";
    public static final String SLIDE = "Slide";
    public static final String INPUT = ProcessobjectTO.getIO_INPUT();
    public static final String OUTPUT = ProcessobjectTO.getIO_OUTPUT();
    
    protected String programname;
    protected String name;
    protected String type;
    protected int order;
    protected String ioflag;

    public ProgramcontainerTO(String programname, String name, String type, int order, String ioflag) {
        this.setProgramname(programname);
        this.setName(name);
        this.setType(type);
        this.setOrder(order);
        this.setIoflag(ioflag);
    }
    
    public int getRownum() {
        if(PLATE96.equals(this.type)) {
            return 8;
        }
        if(PLATE384.equals(this.type)) {
            return 16;
        }
        return 0;
    }
    
    public int getColnum() {
        if(PLATE96.equals(this.type)) {
            return 12;
        }
        if(PLATE384.equals(this.type)) {
            return 24;
        }
        return 0;
    }
    
    public String getProgramname() {
        return programname;
    }

    public void setProgramname(String programname) {
        this.programname = programname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getIoflag() {
        return ioflag;
    }

    public void setIoflag(String ioflag) {
        this.ioflag = ioflag;
    }
}
