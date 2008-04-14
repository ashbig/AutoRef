/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author DZuo
 */
public class Containerlabelmap {
    private String root;
    private int id;

    public Containerlabelmap(String root, int id) {
        this.root = root;
        this.id = id;
    }
    
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
