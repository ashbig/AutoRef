/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.VariableDAO;
import java.util.List;

/**
 *
 * @author DZuo
 */
public class ExperimentController {
    public static List<String> getVariableTypes() {
        return VariableDAO.getVariableTypes();
    }
}
