/*
 * PageSize.java
 *
 * Created on February 12, 2004, 6:05 PM
 */

package edu.harvard.med.hip.flex.query.bean;

/**
 *
 * @author  DZuo
 */
public class PageSize {
    private int name;
    private int value;
    
    /** Creates a new instance of PageSize */
    public PageSize() {
    }
    
    public PageSize(int name, int value) {
        this.name=name;
        this.value=value;
    }
    
    public void setName(int name) {this.name=name;}
    public void setValue(int value) {this.value = value;}
    
    public int getName() {return name;}
    public int getValue() {return value;}
}
