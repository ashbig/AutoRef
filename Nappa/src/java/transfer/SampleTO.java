/*
 * SampleTO.java
 *
 * Created on May 1, 2007, 3:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.StringConvertor;

/**
 *
 * @author dzuo
 */
public class SampleTO  implements Serializable{
    private static final double CULTURE_THREASHOLD = 0.5;
    private static final double DNA_THREASHOLD = 5.0;
    
    private static final String STATUS_GOOD = "Good";
    
    private static final String TYPE_GLYCEROL = "Glycerol";
    private static final String TYPE_EMPTY = "Empty";
    private static final String TYPE_CONTROL = "Control";
    private static final String TYPE_CULTURE = "Culture";
    private static final String TYPE_DNA = "DNA";
    
    private static final String FORM_GLYCEROL = "Glycerol";
    private static final String FORM_DNA = "DNA";
    private static final String FORM_EMPTY = "Empty";
    private static final String FORM_CULTURE = "Culture";
    
    private static final String NAME_GENERAL = "General";
    private static final String NAME_EMPTY = "Empty";
    
    private int sampleid;
    private String name;
    private String description;
    private int volume;
    private int quantity;
    private String unit;
    private String type;
    private String form;
    private String status;
    private int containerid;
    private int position;
    
    private Collection<ReagentTO> reagents;
    private ContainerheaderTO containerheader;
    private ContainercellTO cell;
    private Collection<ResultTO> results;
    private Collection<SamplepropertyTO> properties;
    
    private double culturecut;
    private double dnacut;
    
    private String newReagent;
    private ContainercellTO precell;
    private boolean hasPrecell;
    
    /** Creates a new instance of SampleTO */
    public SampleTO() {
        setReagents(new ArrayList<ReagentTO>());
        setResults(new ArrayList<ResultTO>());
        setProperties(new ArrayList<SamplepropertyTO>());
        setCulturecut(getCULTURE_THREASHOLD());
        setDnacut(getDNA_THREASHOLD());
    }

    public SampleTO(int id) {
        this.setSampleid(id);
        setReagents(new ArrayList<ReagentTO>());
        setResults(new ArrayList<ResultTO>());
        setProperties(new ArrayList<SamplepropertyTO>());
        setCulturecut(getCULTURE_THREASHOLD());
        setDnacut(getDNA_THREASHOLD());
    }
    
    public SampleTO(int id, String name, String description, int volume, int quantity, String unit, String type, String form, String status, int containerid, int position) {
        this.setSampleid(id);
        this.setName(name);
        this.setDescription(description);
        this.setVolume(volume);
        this.setQuantity(quantity);
        this.setUnit(unit);
        this.setType(type);
        this.setForm(form);
        this.setStatus(status);
        this.containerid = containerid;
        this.position = position;
        setReagents(new ArrayList<ReagentTO>());
        setResults(new ArrayList<ResultTO>());
        setProperties(new ArrayList<SamplepropertyTO>());
        setCulturecut(getCULTURE_THREASHOLD());
        setDnacut(getDNA_THREASHOLD());
    }
    
    public int getSampleid() {
        return sampleid;
    }

    public void setSampleid(int sampleid) {
        this.sampleid = sampleid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getSTATUS_GOOD() {
        return STATUS_GOOD;
    }

    public Collection<ReagentTO> getReagents() {
        return reagents;
    }

    public void setReagents(Collection<ReagentTO> reagents) {
        this.reagents = reagents;
    }
    
    public void addReagent(ReagentTO r) {
        getReagents().add(r);
    }

    public static String getTYPE_GLYCEROL() {
        return TYPE_GLYCEROL;
    }

    public static String getNAME_GENERAL() {
        return NAME_GENERAL;
    }

    public int getContainerid() {
        return containerid;
    }

    public void setContainerid(int containerid) {
        this.containerid = containerid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ContainerheaderTO getContainerheader() {
        return containerheader;
    }

    public void setContainerheader(ContainerheaderTO containerheader) {
        this.containerheader = containerheader;
    }

    public ContainercellTO getCell() {
        return cell;
    }

    public void setCell(ContainercellTO cell) {
        this.cell = cell;
    }
    
    public String getReagentString() {
        List l = new ArrayList<String>();
        for(ReagentTO r:getReagents()) {
            l.add(r.getName());
        }
        return StringConvertor.convertFromListToString(l);
    }
    
    public String getReagentStringWithoutMM() {
        List l = new ArrayList<String>();
        for(ReagentTO r:getReagents()) {
            if(!ReagentTO.TYPE_MASTERMIX.equals(r.getType()))
                l.add(r.getName());
        }
        return StringConvertor.convertFromListToString(l);
    }

    public static String getTYPE_EMPTY() {
        return TYPE_EMPTY;
    }

    public static String getTYPE_CONTROL() {
        return TYPE_CONTROL;
    }

    public static String getTYPE_CULTURE() {
        return TYPE_CULTURE;
    }

    public static String getTYPE_DNA() {
        return TYPE_DNA;
    }

    public static String getFORM_GLYCEROL() {
        return FORM_GLYCEROL;
    }

    public static String getFORM_DNA() {
        return FORM_DNA;
    }

    public static String getFORM_EMPTY() {
        return FORM_EMPTY;
    }

    public static String getFORM_CULTURE() {
        return FORM_CULTURE;
    }

    public static String getNAME_EMPTY() {
        return NAME_EMPTY;
    }

    public Collection<ResultTO> getResults() {
        return results;
    }

    public void setResults(Collection<ResultTO> results) {
        this.results = results;
    }
    
    public void addResult(ResultTO result) {
        this.getResults().add(result);
    }

    public Collection<SamplepropertyTO> getProperties() {
        return properties;
    }

    public void setProperties(Collection<SamplepropertyTO> properties) {
        this.properties = properties;
    }
    
    public void addProperty(SamplepropertyTO p) {
        this.getProperties().add(p);
    }
    
    public boolean isCultureFail() {
        for(SamplepropertyTO property:getProperties()) {
            if(property.getType().equals(ResultTO.TYPE_CULTURE)) {
                if(Double.parseDouble(property.getValue()) < getCulturecut())
                    return true;
            }
        }
        return false;
    }
    
    public boolean isDnaFail() {
        for(SamplepropertyTO property:getProperties()) {
            if(property.getType().equals(ResultTO.TYPE_DNA)) {
                if(Double.parseDouble(property.getValue()) < getDnacut())
                    return true;
            }
        }
        return false;
    }

    public static double getCULTURE_THREASHOLD() {
        return CULTURE_THREASHOLD;
    }

    public static double getDNA_THREASHOLD() {
        return DNA_THREASHOLD;
    }

    public double getCulturecut() {
        return culturecut;
    }

    public void setCulturecut(double culturecut) {
        this.culturecut = culturecut;
    }

    public double getDnacut() {
        return dnacut;
    }

    public void setDnacut(double dnacut) {
        this.dnacut = dnacut;
    }
    
    public String getCultureResult() {
        for(SamplepropertyTO p:getProperties()) {
            if(p.getType().equals(ResultTO.TYPE_CULTURE))
                return p.getValue();
        }
        return "";
    }
    
    public String getDNAResult() {
        for(SamplepropertyTO p:getProperties()) {
            if(p.getType().equals(ResultTO.TYPE_DNA))
                return p.getValue();
        }
        return "";
    }

    public String getNewReagent() {
        return newReagent;
    }

    public void setNewReagent(String newReagent) {
        this.newReagent = newReagent;
    }

    public ContainercellTO getPrecell() {
        return precell;
    }

    public void setPrecell(ContainercellTO precell) {
        this.precell = precell;
    }

    public boolean isHasPrecell() {
        if(getPrecell() == null)
            return false;
        return true;
    }

    public void setHasPrecell(boolean hasPrecell) {
        this.hasPrecell = hasPrecell;
    }
}
