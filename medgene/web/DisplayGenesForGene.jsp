
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Genes Associated With a Particular Gene</title>
    </head>
    <body>
    <center>
    <h1>Top <bean:write name="number"/> Genes Associated With <bean:write name="source_gene_symbol"/></h1> 
    <h1>By Statistical Method Of "<bean:write name="stat_type"/>"</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>     
    <html:errors/>
    
    <p>
    <TABLE width="100%" align="center" border="1" cellpadding="2" cellspacing="0">
    <COLGROUP>
        <COL width="15%">
        <COL width="5%">
        <COL width="10%">
        <COL width="33%">
        <COL width="25%">
        <COL width="6%">
        <COL width="6%">
    <THEAD>

    <TR bgcolor="gray">
        <TH><A HREF="KeySearchTerm.jsp" target="_blank">Key Search Term</A></TH>
        <TH><A HREF="SearchType.jsp" target="_blank">Search Type</A></TH>
        <TH><A HREF="GeneSymbol.jsp" target="_blank">Gene Symbol</A></TH>
        <TH><A HREF="AllSearchTerms.jsp" target="_blank">All Search Terms</A></TH>
        <TH><A HREF="GOAnnotation.jsp" target="_blank">GO Annotations</A></TH>
        <TH><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
        <TH><A HREF="NumberOfPapers.jsp" target="_blank">Number of Papers</A></TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
            <logic:equal name="association" property="target_gene.type" value="GENE">
            <TD>
                <a href="DisplayLinks.do?hipGeneId=<bean:write name="association" property="target_gene.hipGeneId"/>"><bean:write name="association" property="target_gene.name"/>&nbsp</a>                          
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
            <TD>
                <bean:write name="association" property="target_gene.nicknamesString"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="target_gene.gosString"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="stat_analysis.score"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="asso_data.doublehit"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>