<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>METAGENE : Disease Search</title>
    </head>
    <body>
    <center>
    <h1>Genes List</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>
    
    <p>
    <TABLE border="1" cellpadding="2" cellspacing="0">
    <TR bgcolor="gray">
        <TH>Key Search Term</TH>
        <TH>Search Type</TH>
        <TH>Gene Symbol</TH>
        <TH>All Search Terms</TH>
        <TH>GO Annotations</TH>
        <TH>Statistic Score</TH>
        <TH>Number of Papers</TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
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
            <TD>
                <bean:write name="association" property="gene.symbol"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.nicknamesString"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.gosString"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="data.doublehit"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>