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
    <html:errors/>
    </center>

    <p>
    <TABLE border="1" cellpadding="2" cellspacing="0">
    <TR bgcolor="gray">
        <TH>Gene Name</TH>
        <TH>Search Type</TH>
        <TH>Gene Symbol</TH>
        <TH>Other Names</TH>
        <TH>GO Annotations</TH>
        <TH>Proteome Annotations</TH>
        <TH>Locus ID</TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
            <TD>
                <a href="DisplayLinks.do?hipGeneId=<bean:write name="association" property="gene.hipGeneId"/>"><bean:write name="association" property="gene.name"/>&nbsp</a>                          
            </TD>
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
                <bean:write name="association" property="gene.proteomesString"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.locusid"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>

<jsp:include page="links.jsp" flush="true"/>
</body>
</html>