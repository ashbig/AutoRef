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

    <center>
    <h1>Genes Associated with Multiple Diseases</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>

    <p>Following are the correspondng MeSH term(s). Please choose one from each group:</p>

    <html:form action="SelectMeshTerms.do">   

    <logic:iterate id="meshTerm" name="meshTerms">
    <p>
    <html:select property="diseaseTerms">
        <html:options
        collection="meshTerm"
        property="id"
        labelProperty="term"
        />
    </html:select>
    </p>
    </logic:iterate>

    <p>Please choose a statistic method to rank the gene list:
    <html:select property="stat">
        <html:options
        collection = "stats"
        property = "id"
        labelProperty = "type" 
        />
    </html:select>
    [<a href="statistic_menu.jsp">help</a>]

    <p>
    <html:submit property="submit" value="Get Genes"/>
    <html:submit property="submit" value="New Search"/>
    </html:form>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>