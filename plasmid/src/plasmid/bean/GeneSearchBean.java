/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.bean;

import java.io.IOException;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.TabChangeEvent;
import plasmid.Constants;
import plasmid.coreobject.Clone;
import plasmid.coreobject.CloneAnalysis;
import plasmid.coreobject.CloneInformation;
import plasmid.coreobject.Gene2Refseq;
import plasmid.coreobject.Geneinfo;
import plasmid.coreobject.ShoppingCartItem;
import plasmid.coreobject.User;
import plasmid.coreobject.VectorProperty;
import plasmid.database.DatabaseException;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.process.GeneSearchManager;
import plasmid.process.ProcessException;
import plasmid.util.StringConvertor;

/**
 *
 * @author Lab User
 */
@ManagedBean(name = "geneSearchBean")
@SessionScoped
public class GeneSearchBean implements Serializable {

    private String genenames;
    private String caseSensitive;
    private List<Geneinfo> genes;
    private List<Gene2Refseq> seqs;
    private List<CloneInformation> clones;
    private String blastoutput;
    private int cloneid;
    private int gi;
    private String clonename;
    private String accession;
    private String proteinaccession;
    private String type;
    private boolean showResult;
    private Map<String, Map> allVptypes;
    private List<String> vpForType;
    private List<String> vpForAssay;
    private List<String> vpForCloning;
    private List<String> vpForExpression;
    private boolean filterToggle;
    private int genePanel;
    private boolean refseqView;
    private int refseqPanel;
    private boolean cloneView;
    private List<String> species;

    public GeneSearchBean() {
        this.genenames = null;
        this.caseSensitive = "yes";
        genes = null;
        seqs = null;
        clones = null;
        showResult = false;
        blastoutput = null;
        cloneid = 0;
        gi = 0;
        clonename = null;
        accession = null;
        proteinaccession = null;
        type = null;
        filterToggle = true;
        genePanel = -1;
        refseqPanel = -1;
        refseqView = true;
        cloneView = true;

        try {
            allVptypes = GeneSearchManager.getAllVptypes();
        } catch (DatabaseException ex) {
            ex.printStackTrace();
        }
    }

    public void doSearch() {
        filterToggle = true;
        genePanel = -1;
        refseqPanel = -1;
        refseqView = true;
        cloneView = true;
        showResult = false;
        genes = null;
        seqs = null;
        clones = null;
        GeneSearchManager manager = new GeneSearchManager();
        boolean isCaseSensitive = true;
        if ("no".equals(caseSensitive)) {
            isCaseSensitive = false;
        }
        try {
            StringConvertor convertor = new StringConvertor();
            List<String> terms = convertor.convertFromStringToList(genenames, ",");
            List<String> vptypes = getVptypes();
            genes = manager.searchGenes(terms, isCaseSensitive, vptypes, getRestrictions(), species);
            showResult = true;
        } catch (DatabaseException ex) {
            FacesMessage msg = new FacesMessage(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void findRefseqs(int geneid) {
        GeneSearchManager manager = new GeneSearchManager();
        clones = null;
        cloneView = true;
        try {
            List<String> vptypes = getVptypes();
            seqs = manager.searchRefseqs(geneid, vptypes, getRestrictions());
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            FacesMessage msg = new FacesMessage(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException ex) {
            ex.printStackTrace();
            FacesMessage msg = new FacesMessage(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void findPlasmids(String gi) {
        GeneSearchManager manager = new GeneSearchManager();
        try {
            List<String> vptypes = getVptypes();
            clones = manager.searchClones(gi, vptypes, getRestrictions());
            setInCartClones();
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            FacesMessage msg = new FacesMessage(ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * public void updateSearch(ActionEvent event) { filterToggle = true;
     * genePanel = -1; refseqPanel = -1; refseqView = true; cloneView = true;
     * GeneSearchManager manager = new GeneSearchManager(); try { List<String>
     * vptypes = getVptypes(); if (vptypes == null || vptypes.isEmpty()) {
     * return; }
     *
     * clones = manager.updateClones(clones, vptypes); } catch
     * (DatabaseException ex) { ex.printStackTrace(); FacesMessage msg = new
     * FacesMessage(ex.getMessage());
     * FacesContext.getCurrentInstance().addMessage(null, msg); }
    }
     */
    private List<String> getVptypes() {
        List<String> vptypes = new ArrayList<String>();
        if (vpForType != null) {
            vptypes.addAll(vpForType);
        }
        if (vpForExpression != null) {
            vptypes.addAll(vpForExpression);
        }
        if (vpForAssay != null) {
            vptypes.addAll(vpForAssay);
        }
        if (vpForCloning != null) {
            vptypes.addAll(vpForCloning);
        }
        return vptypes;
    }

    private List<String> getRestrictions() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        User user = (User) map.get(Constants.USER_KEY);
        List<String> restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        restrictions.add(Clone.NON_PROFIT);
        if (user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }
        return restrictions;
    }

    private void setInCartClones() {
        for (CloneInformation clone : clones) {
            clone.setInCart(false);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        Map sessionMap = context.getExternalContext().getSessionMap();
        List<ShoppingCartItem> shoppingcart = (List) sessionMap.get(Constants.CART);
        if(shoppingcart == null)
            return;
        
        for (ShoppingCartItem clone : shoppingcart) {
            CloneInformation c = findClone(clones, clone.getItemid());
            if (c != null) {
                c.setInCart(true);
            }
        }
    }

    private CloneInformation findClone(List<CloneInformation> cloneList, String cloneid) {
        for (CloneInformation clone : cloneList) {
            if (("" + clone.getCloneid()).equals(cloneid)) {
                return clone;
            }
        }
        return null;
    }

    public void onTabChange(TabChangeEvent event) {
        String tabid = event.getTab().getId();
        FacesMessage msg = new FacesMessage("Content updated: " + tabid);
        FacesContext.getCurrentInstance().addMessage(null, msg);

        try {
            resetTabs(tabid);
            if (tabid.equals("geneview")) {
                //restoreTarget();
            }
            if (tabid.equals("cloneview")) {
                //restoreClone();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //message = "Error occured while getting target";;
        }
    }

    private void resetTabs(String tabid) {
        if (!"geneview".equals(tabid)) {
            //target = null;
        }
        if (!"cloneview".equals(tabid)) {
            //setConstructs(null);
        }
    }

    public void viewAlignment(ActionEvent actionEvent) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            setType(CloneAnalysis.TYPE_CDS);
            setCloneid(Integer.parseInt((String) requestMap.get("cloneid")));
            setGi(Integer.parseInt((String) requestMap.get("gi")));
            setClonename((String) requestMap.get("clonename"));
            setAccession((String) requestMap.get("accession"));
            setProteinaccession((String) requestMap.get("proteinaccession"));

            GeneSearchManager manager = new GeneSearchManager();
            if (CloneAnalysis.TYPE_AA.equals(getType())) {
                blastoutput = manager.getBlastOutput(getCloneid(), getGi(), getClonename(), getProteinaccession(), getType());
            } else {
                blastoutput = manager.getBlastOutput(getCloneid(), getGi(), getClonename(), getAccession(), getType());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (ProcessException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error occured."));
        }
    }

    public void displayAlignment(String displayType) {
        this.setType(displayType);
        try {
            GeneSearchManager manager = new GeneSearchManager();
            if (CloneAnalysis.TYPE_AA.equals(getType())) {
                blastoutput = manager.getBlastOutput(getCloneid(), getGi(), getClonename(), getProteinaccession(), getType());
            } else {
                blastoutput = manager.getBlastOutput(getCloneid(), getGi(), getClonename(), getAccession(), getType());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (ProcessException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error occured."));
        }
    }

    public void addToCart() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            String cloneid = (String) requestMap.get("cloneid");
            ShoppingCartItem item = new ShoppingCartItem(0, cloneid, 1, ShoppingCartItem.CLONE);
            Map sessionMap = context.getExternalContext().getSessionMap();
            List shoppingcart = (List) sessionMap.get(Constants.CART);
            if (shoppingcart == null) {
                shoppingcart = new ArrayList();
            }
            ShoppingCartItem.addToCart(shoppingcart, item);
            setInCartClones();
            sessionMap.put(Constants.CART, shoppingcart);
            sessionMap.put(Constants.CART_STATUS, Constants.UPDATED);
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error occured."));
        }
    }

    public boolean isRenderCDS() {
        if (CloneAnalysis.TYPE_CDS.equals(type)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isRenderNT() {
        if (CloneAnalysis.TYPE_NT.equals(type)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isRenderAA() {
        if (CloneAnalysis.TYPE_AA.equals(type)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return the genenames
     */
    public String getGenenames() {
        return genenames;
    }

    /**
     * @param genenames the genenames to set
     */
    public void setGenenames(String genenames) {
        this.genenames = genenames;
    }

    /**
     * @return the genes
     */
    public List<Geneinfo> getGenes() {
        return genes;
    }

    /**
     * @return the caseSensitive
     */
    public String getCaseSensitive() {
        return caseSensitive;
    }

    /**
     * @param caseSensitive the caseSensitive to set
     */
    public void setCaseSensitive(String caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * @return the clones
     */
    public List<CloneInformation> getClones() {
        return clones;
    }

    /**
     * @return the blastoutput
     */
    public String getBlastoutput() {
        return blastoutput;
    }

    /**
     * @param blastoutput the blastoutput to set
     */
    public void setBlastoutput(String blastoutput) {
        this.blastoutput = blastoutput;
    }

    /**
     * @return the seqs
     */
    public List<Gene2Refseq> getSeqs() {
        return seqs;
    }

    /**
     * @param seqs the seqs to set
     */
    public void setSeqs(List<Gene2Refseq> seqs) {
        this.seqs = seqs;
    }

    public String getTypeNT() {
        return CloneAnalysis.TYPE_NT;
    }

    public String getTypeCDS() {
        return CloneAnalysis.TYPE_CDS;
    }

    public String getTypeAA() {
        return CloneAnalysis.TYPE_AA;
    }

    /**
     * @return the cloneid
     */
    public int getCloneid() {
        return cloneid;
    }

    /**
     * @param cloneid the cloneid to set
     */
    public void setCloneid(int cloneid) {
        this.cloneid = cloneid;
    }

    /**
     * @return the gi
     */
    public int getGi() {
        return gi;
    }

    /**
     * @param gi the gi to set
     */
    public void setGi(int gi) {
        this.gi = gi;
    }

    /**
     * @return the clonename
     */
    public String getClonename() {
        return clonename;
    }

    /**
     * @param clonename the clonename to set
     */
    public void setClonename(String clonename) {
        this.clonename = clonename;
    }

    /**
     * @return the accession
     */
    public String getAccession() {
        return accession;
    }

    /**
     * @param accession the accession to set
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * @return the proteinaccession
     */
    public String getProteinaccession() {
        return proteinaccession;
    }

    /**
     * @param proteinaccession the proteinaccession to set
     */
    public void setProteinaccession(String proteinaccession) {
        this.proteinaccession = proteinaccession;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the showResult
     */
    public boolean isShowResult() {
        return showResult;
    }

    /**
     * @param showResult the showResult to set
     */
    public void setShowResult(boolean showResult) {
        this.showResult = showResult;
    }

    /**
     * @return the allVptypes
     */
    public Map<String, Map> getAllVptypes() {
        return allVptypes;
    }

    public Map<String, String> getAllVpForType() {
        try {
            return allVptypes.get(VectorProperty.TYPE);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getAllVpForAssay() {
        try {
            return allVptypes.get(VectorProperty.ASSAY);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getAllVpForCloning() {
        try {
            return allVptypes.get(VectorProperty.CLONING);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Map<String, String> getAllVpForExpression() {
        try {
            return allVptypes.get(VectorProperty.EXPRESSION);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void geneViewTabChange() {
        refseqView = true;
        cloneView = true;
        refseqPanel = -1;
        seqs = null;
        clones = null;
    }

    public void seqViewTabChange() {
        refseqView = true;
        cloneView = true;
        clones = null;
    }

    /**
     * @return the vpForType
     */
    public List<String> getVpForType() {
        return vpForType;
    }

    /**
     * @param vpForType the vpForType to set
     */
    public void setVpForType(List<String> vpForType) {
        this.vpForType = vpForType;
    }

    /**
     * @return the vpForAssay
     */
    public List<String> getVpForAssay() {
        return vpForAssay;
    }

    /**
     * @param vpForAssay the vpForAssay to set
     */
    public void setVpForAssay(List<String> vpForAssay) {
        this.vpForAssay = vpForAssay;
    }

    /**
     * @return the vpForCloning
     */
    public List<String> getVpForCloning() {
        return vpForCloning;
    }

    /**
     * @param vpForCloning the vpForCloning to set
     */
    public void setVpForCloning(List<String> vpForCloning) {
        this.vpForCloning = vpForCloning;
    }

    /**
     * @return the vpForExpression
     */
    public List<String> getVpForExpression() {
        return vpForExpression;
    }

    /**
     * @param vpForExpression the vpForExpression to set
     */
    public void setVpForExpression(List<String> vpForExpression) {
        this.vpForExpression = vpForExpression;
    }

    /**
     * @return the filterToggle
     */
    public boolean isFilterToggle() {
        return filterToggle;
    }

    /**
     * @param filterToggle the filterToggle to set
     */
    public void setFilterToggle(boolean filterToggle) {
        this.filterToggle = filterToggle;
    }

    /**
     * @return the refseqView
     */
    public boolean isRefseqView() {
        return refseqView;
    }

    /**
     * @param refseqView the refseqView to set
     */
    public void setRefseqView(boolean refseqView) {
        this.refseqView = refseqView;
    }

    /**
     * @return the cloneView
     */
    public boolean isCloneView() {
        return cloneView;
    }

    /**
     * @param cloneView the cloneView to set
     */
    public void setCloneView(boolean cloneView) {
        this.cloneView = cloneView;
    }

    /**
     * @return the genePanel
     */
    public int getGenePanel() {
        return genePanel;
    }

    /**
     * @param genePanel the genePanel to set
     */
    public void setGenePanel(int genePanel) {
        this.genePanel = genePanel;
    }

    /**
     * @return the refseqPanel
     */
    public int getRefseqPanel() {
        return refseqPanel;
    }

    /**
     * @param refseqPanel the refseqPanel to set
     */
    public void setRefseqPanel(int refseqPanel) {
        this.refseqPanel = refseqPanel;
    }

    /**
     * @return the species
     */
    public List<String> getSpecies() {
        return species;
    }

    /**
     * @param species the species to set
     */
    public void setSpecies(List<String> species) {
        this.species = species;
    }
}
