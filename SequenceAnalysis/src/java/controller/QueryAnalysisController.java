
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import blast.BlastException;
import blast.BlastWrapper;
import config.ApplicationProperties;
import core.Refseq;
import core.Template;
import core.TemplateAnalysis;
import dao.DaoException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import process.Blaster;
import process.ProcessException;
import process.QueryManager;
import util.StringConvertor;

/**
 *
 * @author dongmei
 */
public class QueryAnalysisController {
    public static final String SEARCH_ORFEOME = "search_orf";
    public static final String NO_SEARCH_ORFEOME = "no_search_orf";
    public static final String SEARCH_GATEWAY = "search_gateway";
    public static final String NO_SEARCH_GATEWAY = "no_search_gateway";
    
    private int maxMutation;
    private double minCoverage;
    private boolean isLongest;
    private boolean isOrfeome;
    private String searchSelection;
    private boolean isGateway;
    private String gatewaySelection;

    public QueryAnalysisController() {
    }

    public QueryAnalysisController(int maxMutation, double minCoverage, boolean isLongest, boolean isOrfeome, 
            String selection, boolean isGateway, String gatewaySelection) {
        this.maxMutation = maxMutation;
        this.minCoverage = minCoverage;
        this.isLongest = isLongest;
        this.isOrfeome = isOrfeome;
        this.searchSelection = selection;
        this.isGateway = isGateway;
        this.gatewaySelection = gatewaySelection;
    }

    public List<TemplateAnalysis> queryTemplateAnalysis(List<String> terms, String type, boolean isDownload, 
            int begin, int end) throws DaoException {
        QueryManager manager = new QueryManager();
        return manager.queryTemplateAnalysis(terms, type, maxMutation, minCoverage, isLongest, isOrfeome, 
                searchSelection, isGateway, gatewaySelection, isDownload, begin, end);
    }

    public List<TemplateAnalysis> queryTemplateAnalysis(int first, int pageSize, String sortField,
            Map<String, String> filters) throws DaoException {
        QueryManager manager = new QueryManager();
        return manager.queryTemplateAnalysis(maxMutation, minCoverage, isLongest, isOrfeome, 
                searchSelection, isGateway, gatewaySelection, first, pageSize, sortField, filters);
    }

    public TemplateAnalysis queryTemplateAnalysis(int id) throws DaoException {
        QueryManager manager = new QueryManager();
        return manager.queryTemplateAnalysis(id);
    }

    public String getBlastxOutput(int id, String queryid, String queryseq, String subid, String subseq)
            throws DaoException, ProcessException, BlastException, IOException {
        String output = ApplicationProperties.getInstance().getProperties().getProperty("tmp") + StringConvertor.getUniqueName(id + "");
        Blaster blaster = new Blaster();
        blaster.runBlastx(queryid, queryseq, subid, subseq, output, BlastWrapper.PAIRWISE_OUTPUT);

        BufferedReader in = new BufferedReader(new FileReader(output));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine).append("\n");
        }
        in.close();

        blaster.delete(output);
        return sb.toString();
    }

    public void printTemplateAnalysis(List<TemplateAnalysis> tas, ServletOutputStream out) throws ControllerException {
        try {
            for(TemplateAnalysis ta:tas) {
                Refseq seq = ta.getRefseq();
                if(seq==null) {
                    out.print("\t\t\t\t\t\t\t\t\t\t\t\t");
                } else {
                    out.print(seq.getGeneid()+"\t"+seq.getSymbol()+"\t"+seq.getStatus()+"\t"+seq.getAccession()+"\t"+
                            seq.getGi()+"\t"+seq.getProteinacc()+"\t"+seq.getProteingi()+"\t"+seq.getCds()+"\t"+
                            seq.getCdslength()+"\t"+seq.getProteinseq()+"\t"+seq.getProteinlength()+"\t"+
                            seq.getLongestString()+"\t");
                }
                
                Template template = ta.getTemplate();
                if(template==null) {
                    out.println("\t\t\t\t\t\t\t\t\t");
                } else {
                    out.print(template.getOriginalcloneid()+"\t"+template.getVector()+"\t"+template.getSource()+"\t"+
                            template.getFormat()+"\t"+template.getSequence()+"\t"+template.getOrfeomeString()+"\t"+
                            template.getGatewayString()+"\t"+template.getPlate()+"\t"+template.getWell()+"\t");
                }
                
                out.println(ta.getAnalysistype()+"\t"+ta.getPid()+"\t"+ta.getAlength()+"\t"+ta.getMismatch()+"\t"+
                        ta.getGap()+"\t"+ta.getEvalue()+"\t"+ta.getScore()+"\t"+ta.getMutation()+"\t"+ta.getCoverage());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ControllerException("Error occured while writing the output file.");
        }
    }
    
    public int getMaxAnalysisid() throws DaoException {
        QueryManager manager = new QueryManager();
        return manager.queryMaxAnalysisid();
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
     * @return the searchSelection
     */
    public String getSearchSelection() {
        return searchSelection;
    }

    /**
     * @param searchSelection the searchSelection to set
     */
    public void setSearchSelection(String searchSelection) {
        this.searchSelection = searchSelection;
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
}
