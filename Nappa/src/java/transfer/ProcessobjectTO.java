/*
 * ProcessobjectTO.java
 *
 * Created on April 30, 2007, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class ProcessobjectTO implements Serializable {
    protected static final String TYPE_CONTAINERSET = "Container Set";
    protected static final String TYPE_FILEREFERENCE = "File Reference";
    protected static final String TYPE_CONTAINERHEADER = "Container";
    private static final String TYPE_REAGENT = "Reagent";
    
    private static final String TYPE_SLIDE = "Slide";
    private static final String IO_INPUT = "I";
    protected static final String IO_OUTPUT = "O";
    protected static final String IO_BOTH = "B";

    public static String getTYPE_REAGENT() {
        return TYPE_REAGENT;
    }

    public static String getTYPE_SLIDE() {
        return TYPE_SLIDE;
    }
    
    protected int executionid;
    protected int objectid;
    protected String objectname;
    protected String objecttype;
    protected String ioflag;
    protected int level;
    protected int order;
    
    /** Creates a new instance of ProcessobjectTO */
    public ProcessobjectTO() {
    }
    
    public ProcessobjectTO(String type, String io) {
        this.setObjecttype(type);
        this.setIoflag(io);
    }
    
    public ProcessobjectTO(int id, String name, String type, String io, int level, int order) {
        this.objectid = id;
        this.objectname = name;
        this.objecttype = type;
        this.ioflag = io;
        this.level = level;
        this.order = order;
    }
    
    public int getObjectid() {
        return objectid;
    }
    
    public void setObjectid(int objectid) {
        this.objectid = objectid;
    }
    
    public String getIoflag() {
        return ioflag;
    }
    
    public void setIoflag(String ioflag) {
        this.ioflag = ioflag;
    }

    public static String getTYPE_CONTAINERSET() {
        return TYPE_CONTAINERSET;
    }

    public static String getTYPE_FILEREFERENCE() {
        return TYPE_FILEREFERENCE;
    }

    public static String getIO_INPUT() {
        return IO_INPUT;
    }

    public static String getIO_OUTPUT() {
        return IO_OUTPUT;
    }

    public static String getIO_BOTH() {
        return IO_BOTH;
    }

    public int getExecutionid() {
        return executionid;
    }

    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }

    public static String getTYPE_CONTAINERHEADER() {
        return TYPE_CONTAINERHEADER;
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }
    
}
