<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Gene Search</title>
    </head>
    <body>
    <center>
    <h1>Top <bean:write name="number"/> Genes </h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>
    <br>

    <logic:notEqual name="hs_geneIndexes" value="no">   
    <br>
    <hr>
    <b>Note:</b> If you want to find whether your interested genes are on this page, 
    just click Edit on your browser's menu bar and use Find to search the current page. 
    <hr><br><br>
    <logic:iterate id="_assoc" name="all_associations">
    <% int i = 0; %>
    <table width="40%" align="left" border="0">
    <tr><td>Input gene: </td> <td> <bean:write name="input_type"/> <b><bean:write name="searchTerm"/></b></td></tr>
    <tr><td>Human homolog: </td> <td> <bean:write name="_assoc" property="gene_index"/> </td></tr>
    <tr><td>Statistical method: </td> <td> <bean:write name="stat" property="type"/> </td></tr>
    </table>
    <br><br><br><br><br>

    <p>
    <TABLE width = "100%" align="center" border="1" cellpadding="2" cellspacing="0" >

    <COLGROUP>
        <COL width="3%">
        <COL width="12%">
        <COL width="5%">
        <COL width="10%">
        <COL width="29%">
        <COL width="26%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
    <THEAD>

    <TR bgcolor="#cccccc">
        <TH>NO.</TH>
        <TH><A HREF="KeySearchTerm.jsp" target="_blank">Key Search Term</A></TH>
        <TH><A HREF="SearchType.jsp" target="_blank">Search Type</A></TH>
        <TH><A HREF="GeneSymbol.jsp" target="_blank">Gene Symbol</A></TH>
        <TH><A HREF="AllSearchTerms.jsp" target="_blank">All Search Terms</A></TH>
        <TH><A HREF="GOAnnotation.jsp" target="_blank">GO Annotations</A></TH>
        <TH><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
        <TH><A HREF="NumberOfPapers.jsp" target="_blank">Papers</A></TH>
        <TH><A HREF="DefaultRefSeqID.jsp" target="_blank">Default RefSeq ID</A></TH>
    </TR>
 
    <logic:iterate id="association" name="_assoc" property="associations"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <logic:equal name="association" property="target_gene.type" value="GENE">
            <TD>
                <a href="DisplayLinks.do?hipGeneId=<bean:write name="association" property="target_gene.hipGeneId"/>" target="_blank"><bean:write name="association" property="target_gene.name"/>&nbsp</a>                          
            </TD>
            <TD> By gene term </TD>
            </logic:equal>
            <logic:equal name="association" property="target_gene.type" value="FAMILY">
            <TD>
                <bean:write name="association" property="target_gene.name"/>&nbsp                          
            </TD>
            <TD> By family term </TD>
            </logic:equal>

            <TD align="center"> 
                <bean:write name="association" property="target_gene.symbol"/>&nbsp
            </TD>

            <logic:equal name="association" property="target_gene.type" value="GENE">
            <TD>
                <bean:write name="association" property="target_gene.nicknamesString"/>&nbsp
            </TD>
            </logic:equal>
            <logic:equal name="association" property="target_gene.type" value="FAMILY">
            <TD>
                <logic:iterate id="child" name="association" property="target_gene.nicknames"> 
                    <a href="DisplayLinks.do?geneSymbol=<bean:write name="child"/>" target="_blank">
                    <bean:write name="child"/></a>&nbsp
                </logic:iterate>
            </TD>
            </logic:equal>

            <TD>
                <bean:write name="association" property="target_gene.gosString"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="stat_analysis.score"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="asso_data.doublehit"/>&nbsp
            </TD>
            <TD><a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=<bean:write name="association" property="target_gene.refSeq_NM2"/>" target="_blank">
                <font size=2> <bean:write name="association" property="target_gene.refSeq_NM"/> </font> </a>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>

    <br><br><br>

    </logic:iterate> 
    </logic:notEqual>

    <logic:equal name="hs_geneIndexes" value="no">  
    <table width="40%" align="left" border="0">
    <tr><td>Input gene: </td> <td> <bean:write name="input_type"/> <b><bean:write name="searchTerm"/></b></td></tr>
    </table><br><br><p> There is no human homolog found by using HomoloGene at NCBI. Therefore, no associated diseases are displayed.
    </logic:equal>

<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>

