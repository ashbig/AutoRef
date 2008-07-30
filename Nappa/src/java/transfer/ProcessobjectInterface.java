/*
 * ProcessobjectInterface.java
 *
 * Created on April 30, 2007, 1:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

/**
 *
 * @author dzuo
 */
public interface ProcessobjectInterface {
    
    String getIoflag();

    long getObjectid();

    long getOrder();

    String getType();

    void setIoflag(String ioflag);

    void setObjectid(long objectid);

    void setOrder(long order);

    void setType(String type);
    
}
