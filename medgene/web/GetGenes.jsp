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
    <h1>Genes Associated with Disease(s)</h1>
    </center>

    <table width="80%" align="center" border="0"> 
    <html:errors/>

    <p>Following are the correspondng MeSH term(s). Please choose one:</p>

    <html:form action="GetGenes.do">   

    <html:select property="diseaseTerm">
        <html:options
        collection="diseases"
        property="id"
        labelProperty="term"
        />
    </html:select>

    <p>Please choose a statistic method to rank the gene list:
    <html:select property="stat">
        <html:options
        collection = "stats"
        property = "id"
        labelProperty = "type" 
        />
    </html:select>
    [<a href="statistic_menu.jsp">help</a>]

    <p>Please choose the number of genes for your list:
    <html:select property="number">
        <html:option key="top 25" value="25"/>
        <html:option key="top 50" value="50"/>
        <html:option key="top 100" value="100"/>
    </html:select>

    <p>
    <html:submit property="submit" value="Get Genes"/>
    <html:submit property="submit" value="New Search"/>
    </html:form>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>