/*
 * PageInfo.java
 *
 * Created on February 12, 2004, 4:53 PM
 */

package edu.harvard.med.hip.flex.query.bean;

/**
 *
 * @author  DZuo
 */
public class PageInfo {
    private int name;
    private int value;
    
    /** Creates a new instance of PageInfo */
    public PageInfo() {
    }
    
    public PageInfo(int name, int value) {
        this.name = name;
        this.value = value;
    }
 
    public int getName() {return name;}
    public int getValue() {return value;}
    
    public void setName(int i) {this.name=i;}
    public void setValue(int i) {this.value=i;}
}
