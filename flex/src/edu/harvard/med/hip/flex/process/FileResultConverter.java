/*
 * FileResultConverter.java
 *
 * Created on March 11, 2005, 1:29 PM
 */

package edu.harvard.med.hip.flex.process;

import java.io.*;
import java.util.*;
/**
 *
 * @author  DZuo
 */
public abstract class FileResultConverter {
    
    protected InputStream input;
    protected String errorMessage;
    protected Map odList;
    protected ArrayList resultList;
    protected int size;
    
    /** Creates a new instance of FileResultConverter */
    public FileResultConverter() {
    }
  
    public FileResultConverter(InputStream input) {
        this.input = input;
        odList = new HashMap();
        resultList = new ArrayList();
    }
 
    public FileResultConverter(InputStream input, int size) {
        this.input = input;
        this.size = size;
        odList = new HashMap();
        resultList = new ArrayList();
    }   
    
    public InputStream getInput() {return input;}
    public Map getOdList() {return odList;}
    public ArrayList getResultList() {return resultList;}
    public void setSize(int s) {this.size = s;}
    public int getSize() {return size;}
    
    /**
     * Set the errorMessage to be the given value.
     * @param errorMessage The value to be set to.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Return the errorMessage.
     * @return The errorMessage.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    abstract public boolean parseFile();
    
    abstract public boolean convertResults();
}
