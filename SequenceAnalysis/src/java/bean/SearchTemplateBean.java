/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import blast.BlastException;
import controller.QueryAnalysisController;
import core.TemplateAnalysis;
import dao.DaoException;
import dao.TemplateDao;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import process.ProcessException;
import process.QueryManager;
import ui.TemplateAnalysisSelectableDataModel;
import util.StringConvertor;
import util.UtilException;

/**
 *
 * @author dongmei
 */
@ManagedBean(name = "searchTemplateBean")
@SessionScoped
public class SearchTemplateBean implements Serializable {

    private String searchType;
    private String searchTermString;
    private int maxMutation;
    private double minCoverage;
    private boolean isLongest;
    private DataModel<TemplateAnalysis> data;
    //private List<TemplateAnalysis> data;
    private TemplateAnalysis selected;
    private String blastOutput;
    private int geneCount;
    private boolean isShowResult;
    private boolean isOrfeome;
    private String orfeomeSelection;
    private boolean isGateway;
    private String gatewaySelection;

    public SearchTemplateBean() {
        resetValue();
    }

    public final void resetValue() {
        searchType = TemplateDao.SEARCH_TYPE_GENEID;
        searchTermString = null;
        maxMutation = -1;
        minCoverage = 0;
        isLongest = false;
        data = null;
        selected = null;
        blastOutput = null;
        geneCount = 0;
        setIsShowResult(false);
        setIsOrfeome(false);
        setOrfeomeSelection(QueryAnalysisController.SEARCH_ORFEOME);
        setIsGateway(false);
        setGatewaySelection(QueryAnalysisController.SEARCH_GATEWAY);
    }

    public String search() {
        List<String> terms = null;
        //if (searchTermString != null && searchTermString.trim().length() > 0) {
        try {
            terms = StringConvertor.convertFromStringToList(searchTermString, "\n\t\b ");
        } catch (UtilException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return null;
        }

        QueryAnalysisController controller = new QueryAnalysisController(maxMutation, minCoverage, isLongest,
                isOrfeome, orfeomeSelection, isGateway, gatewaySelection);
        try {
            List<TemplateAnalysis> ta = controller.queryTemplateAnalysis(terms, searchType, false, 0, 0);
            if (ta == null || ta.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("No clones found."));
                isShowResult = false;
                return null;
            }
            isShowResult = true;
            calculateGeneCount(ta);
            data = new TemplateAnalysisSelectableDataModel(ta);
            return "SearchTemplateResult.xhtml";
        } catch (DaoException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return null;
        }
        //} else {//lazy loading
        //    data = new TemplateAnalysisLazyDataModel(controller);
        //    return "SearchTemplateResult.xhtml";
        //}
    }

    public void download() {
        List<String> terms = null;
        try {
            terms = StringConvertor.convertFromStringToList(searchTermString, "\n\t\b ");
        } catch (UtilException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return;
        }

        QueryAnalysisController controller = new QueryAnalysisController(maxMutation, minCoverage, isLongest,
                isOrfeome, orfeomeSelection, isGateway, gatewaySelection);
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) context.getResponse();
            //response.setContentType("Application/vnd.ms-excel");
            //response.setHeader("Content-Disposition", "inline;filename=clones.xls");
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment;filename=clones.txt");
            ServletOutputStream out = response.getOutputStream();
            out.println("Gene ID\tSymbol\tStatus\tAccession\tGI\tProtein Accession\tProtein GI\tCDS\t"+
                    "CDS Length\tProtein Sequence\tProtein Length\tLongest\tClone ID\tVector\tSource\t"+
                    "Format\tSequence\tORFeome\tGateway\tPlate\tWell\tAnalysis Type\tPID\tAlignment Length\tMismatch\tGap\t"+
                    "Evalue\tScore\tMutation\tCoverage");

            int maxAnalysisid = controller.getMaxAnalysisid();
            List<TemplateAnalysis> ta = null;
            int i = 1;
            while (i <= maxAnalysisid) {
                int begin = i;
                int end = i+999;
                ta = controller.queryTemplateAnalysis(terms, searchType, true, begin, end);
                i = i+1000;
                if (ta == null || ta.isEmpty()) {
                    continue;
                }
                controller.printTemplateAnalysis(ta, out);
            }
            out.flush();
            out.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (DaoException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error occured while writing the file."));
        }
    }

    public void viewBlastAlignment() {
        QueryAnalysisController controller = new QueryAnalysisController();
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            int id = Integer.parseInt((String) requestMap.get("selectedid"));
            TemplateAnalysis ta = controller.queryTemplateAnalysis(id);
            blastOutput = controller.getBlastxOutput(ta.getId(), ta.getTemplate().getOriginalcloneid(), ta.getTemplate().getSequence(), ta.getRefseq().getProteinacc(), ta.getRefseq().getProteinseq());
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (DaoException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (BlastException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (ProcessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error occured."));
        }
    }

    public void showDetail() {
        int id = selected.getId();
        QueryManager manager = new QueryManager();
        try {
            selected = manager.queryTemplateAnalysis(id);
        } catch (DaoException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ex.getMessage()));
        }
    }

    private void calculateGeneCount(List<TemplateAnalysis> ta) {
        Set genes = new TreeSet();
        for (TemplateAnalysis a : ta) {
            String geneid = "" + a.getRefseq().getGeneid();
            genes.add(geneid);
        }
        geneCount = genes.size();
    }

    /**
     * @return the SEARCH_TYPE_GENEID
     */
    public String getSearchTypeGeneid() {
        return TemplateDao.SEARCH_TYPE_GENEID;
    }

    /**
     * @return the SEARCH_TYPE_SYMBOL
     */
    public String getSearchTypeSymbol() {
        return TemplateDao.SEARCH_TYPE_SYMBOL;
    }

    /**
     * @return the SEARCH_TYPE_ACCESSION
     */
    public String getSearchTypeAccession() {
        return TemplateDao.SEARCH_TYPE_ACCESSION;
    }

    /**
     * @return the SEARCH_TYPE_GI
     */
    public String getSearchTypeGi() {
        return TemplateDao.SEARCH_TYPE_GI;
    }

    /**
     * @return the SEARCH_TYPE_PACCESSION
     */
    public String getSearchTypePaccession() {
        return TemplateDao.SEARCH_TYPE_PACCESSION;
    }

    /**
     * @return the SEARCH_TYPE_PGI
     */
    public String getSearchTypePgi() {
        return TemplateDao.SEARCH_TYPE_PGI;
    }

    /**
     * @return the searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * @return the searchTermString
     */
    public String getSearchTermString() {
        return searchTermString;
    }

    /**
     * @param searchTermString the searchTermString to set
     */
    public void setSearchTermString(String searchTermString) {
        this.searchTermString = searchTermString;
    }

    /**
     * @return the maxMutation
     */
    public int getMaxMutation() {
        return maxMutation;
    }

    /**
     * @param maxMutation the maxMutation to set
     */
    public void setMaxMutation(int maxMutation) {
        this.maxMutation = maxMutation;
    }

    /**
     * @return the minCoverage
     */
    public double getMinCoverage() {
        return minCoverage;
    }

    /**
     * @param minCoverage the minCoverage to set
     */
    public void setMinCoverage(double minCoverage) {
        this.minCoverage = minCoverage;
    }

    /**
     * @return the isLongest
     */
    public boolean isIsLongest() {
        return isLongest;
    }

    /**
     * @param isLongest the isLongest to set
     */
    public void setIsLongest(boolean isLongest) {
        this.isLongest = isLongest;
    }

    /**
     * @return the data
     */
    public DataModel<TemplateAnalysis> getData() {
        return data;
    }

    /**
     * @return the selected
     */
    public TemplateAnalysis getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(TemplateAnalysis selected) {
        this.selected = selected;
    }

    /**
     * @return the blastOutput
     */
    public String getBlastOutput() {
        return blastOutput;
    }

    /**
     * @param blastOutput the blastOutput to set
     */
    public void setBlastOutput(String blastOutput) {
        this.blastOutput = blastOutput;
    }

    /**
     * @return the geneCount
     */
    public int getGeneCount() {
        return geneCount;
    }

    /**
     * @param geneCount the geneCount to set
     */
    public void setGeneCount(int geneCount) {
        this.geneCount = geneCount;
    }

    public String getIsLongestString() {
        if (isLongest) {
            return "Yes";
        }
        return "No";
    }

    /**
     * @return the isShowResult
     */
    public boolean isIsShowResult() {
        return isShowResult;
    }

    /**
     * @param isShowResult the isShowResult to set
     */
    public void setIsShowResult(boolean isShowResult) {
        this.isShowResult = isShowResult;
    }

    /**
     * @return the isOrfeome
     */
    public boolean isIsOrfeome() {
        return isOrfeome;
    }

    /**
     * @param isOrfeome the isOrfeome to set
     */
    public void setIsOrfeome(boolean isOrfeome) {
        this.isOrfeome = isOrfeome;
    }

    /**
     * @return the orfeomeSelection
     */
    public String getOrfeomeSelection() {
        return orfeomeSelection;
    }

    /**
     * @param orfeomeSelection the orfeomeSelection to set
     */
    public void setOrfeomeSelection(String orfeomeSelection) {
        this.orfeomeSelection = orfeomeSelection;
    }

    public String getSearchOrf() {
        return QueryAnalysisController.SEARCH_ORFEOME;
    }

    public String getNoSearchOrf() {
        return QueryAnalysisController.NO_SEARCH_ORFEOME;
    }

    /**
     * @return the isGateway
     */
    public boolean isIsGateway() {
        return isGateway;
    }

    /**
     * @param isGateway the isGateway to set
     */
    public void setIsGateway(boolean isGateway) {
        this.isGateway = isGateway;
    }

    /**
     * @return the gatewaySelection
     */
    public String getGatewaySelection() {
        return gatewaySelection;
    }

    /**
     * @param gatewaySelection the gatewaySelection to set
     */
    public void setGatewaySelection(String gatewaySelection) {
        this.gatewaySelection = gatewaySelection;
    }

    public String getSearchGateway() {
        return QueryAnalysisController.SEARCH_GATEWAY;
    }

    public String getNoSearchGateway() {
        return QueryAnalysisController.NO_SEARCH_GATEWAY;
    }
}
