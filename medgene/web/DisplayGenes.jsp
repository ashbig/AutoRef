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
    <TR>
        <TH>Gene Name</TH>
        <TH>Search Type</TH>
        <TH>Gene Symbol</TH>
        <TH>Other Names</TH>
        <TH>GO Annotations</TH>
        <TH>Proteome Annotations</TH>
        <TH>Locus ID</TH>
        <TH>Unigene Clustering ID</TH>
        <TH>Refseq ID</TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
            <TD>
                <bean:write name="association" property="gene.name"/>&nbsp                           
            </TD>
            <TD>
                <bean:write name="association" property="geneIndex.type"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.symbol"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.nicknamesString"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.go"/>&nbsp
            </TD>
            <TD>
                <bean:write name="association" property="gene.locusid"/>&nbsp
            </TD>
        </tr>
    </logic:iterate> 
</TABLE>
</body>
</html>