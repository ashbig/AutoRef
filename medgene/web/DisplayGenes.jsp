<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Disease Search</title>
    </head>
    <body>
    <% int i = 0; %>
    <center>
    <h1>Top 
    <logic:present name="number">
        <bean:write name="number"/>
    </logic:present>
        Genes Associated With <bean:write name="disease"/>
    </h1>
    <h1>
        By Statistical Method Of "<bean:write name="stat" property="type"/>"
    </h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>
    
    <p>
    <TABLE width = "100%" align="center" border="1" cellpadding="2" cellspacing="0" >

    <COLGROUP>
        <COL width="3%">
        <COL width="15%">
        <COL width="5%">
        <COL width="10%">
        <COL width="30%">
        <COL width="27%">
        <COL width="5%">
        <COL width="5%">
    <THEAD>

    <TR bgcolor="gray">
        <TH>Rank</TH>
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
            <TD align="center"><% out.println(++i); %></TD>

            <logic:equal name="association" property="gene.type" value="GENE">
            <TD>
                <a href="DisplayLinks.do?hipGeneId=<bean:write name="association" property="gene.hipGeneId"/>"><bean:write name="association" property="gene.name"/>&nbsp</a>                          
            </TD>
            </logic:equal>
            <logic:equal name="association" property="gene.type" value="FAMILY">
            <TD>
                <bean:write name="association" property="gene.name"/>&nbsp                          
            </TD>
            </logic:equal>
            <TD>
                <bean:write name="association" property="geneIndex.searchType"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="gene.symbol"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.nicknamesString"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.gosString"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="data.doublehit"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>