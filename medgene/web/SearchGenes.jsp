<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Gene Search</title>
    </head>
    <body>

    <center>
    <h1>Diseases Associated with a Particular Gene</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>
    <p>Following are the corresponding gene symbol(s). Please choose one:</p>

    <html:form action="SearchGenes.do">  

    <html:select property="gene">
        <html:options
        collection="geneIndexes"
        property="indexid"
        labelProperty="index"
        />
    </html:select>
    <p>Please choose a <a href="statistic_menu.jsp" target="_blank">statistical method</a> to rank the disease list:
    <html:select property="stat">
        <html:options
        collection = "stats"
        property = "id"
        labelProperty = "type" 
        />
    </html:select>

    <p>Please choose the number of diseases for your list:
    <html:select property="number">
        <html:option key="top 25" value="25"/>
        <html:option key="top 50" value="50"/>
        <html:option key="top 100" value="100"/>
        <html:option key="top 500" value="500"/>
        <html:option key="top 1000" value="1000"/>
    </html:select>

    <p>
    <html:submit property="submit" value="Get Diseases"/>
    <html:submit property="submit" value="New Search"/>


    </html:form>

<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>