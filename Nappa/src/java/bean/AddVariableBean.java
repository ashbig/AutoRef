/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import dao.VariableDAO;

/**
 *
 * @author DZuo
 */
public class AddVariableBean {
    private String type;
    private String message;

    public AddVariableBean() {
        type = null;
        message = null;
    }
    
    public void addVarType() {
        message = "Variable type: "+type+ " has been added.";
        try {
            if(VariableDAO.isTypeExistIgnoreCase(getType())) {
                message = "Variable type: "+type+" exists in the database.";
                return;
            }
            VariableDAO.addVartype(type);
        } catch (Exception ex) {
            ex.printStackTrace();
            message = "Cannot add variable type: "+type;
        }
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
