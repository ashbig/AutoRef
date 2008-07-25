/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.coreobject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DZuo
 */
public class Institution {
    public static final String CATEGORY_US_INSTITUTION = "US Institution";
    public static final String CATEGORY_INT_INSTITUTION = "International Institution";
    public static final String CATEGORY_COMPANY = "Company";
    public static final String CATEGORY_GOVERNMENT = "Government";
    public static final String ISMEMBER_YES = "Y";
    public static final String ISMEMBER_NO = "N";
    
    private String name;
    private String category;
    private String ismember;
    private String country;
    
    public Institution(String name, String category, String ismember) {
        this.name = name;
        this.category = category;
        this.ismember = ismember;
    }
    
    public static final List getInstitutionCategory() {
        List categories = new ArrayList();
        categories.add(CATEGORY_US_INSTITUTION);
        categories.add(CATEGORY_GOVERNMENT);
        categories.add(CATEGORY_INT_INSTITUTION);
        categories.add(CATEGORY_COMPANY);
        return categories;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsmember() {
        return ismember;
    }

    public void setIsmember(String ismember) {
        this.ismember = ismember;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
