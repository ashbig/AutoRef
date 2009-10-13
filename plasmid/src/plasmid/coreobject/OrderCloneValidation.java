/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.coreobject;

/**
 *
 * @author DZuo
 */
public class OrderCloneValidation {
    public static final String RESULT_PASS = "Pass";
    public static final String RESULT_FAIL = "Fail";
    
    private int orderid;
    private int cloneid;
    private String method;
    private String sequence;
    private String result;
    private String who;
    private String when;
    private int userid;

    private OrderClones orderclone;
    
    public OrderCloneValidation() {}
    
    public OrderCloneValidation(OrderClones c) {
        orderclone = c;
        this.orderid = c.getOrderid();
        this.cloneid = c.getCloneid();
    }
    
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getCloneid() {
        return cloneid;
    }

    public void setCloneid(int cloneid) {
        this.cloneid = cloneid;
    }

    public OrderClones getOrderclone() {
        return orderclone;
    }

    public void setOrderclone(OrderClones orderclone) {
        this.orderclone = orderclone;
    }
}
