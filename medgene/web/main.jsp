<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : main</title>
    </head>
    <body>
    <center>
    <h1>Main Menu</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <p>The MedGene database can help you to get the following information:</p>

    <p>A list of genes associated with</p>
    <html:form action="main.do">   
        <html:radio property="geneDiseaseSelect" value="geneDisease" />a particular disease<br>      
        <html:radio property="geneDiseaseSelect" value="multiDisease"/>multiple diseases<br>
        <html:radio property="geneDiseaseSelect" value="geneGene"/>a particular gene<br>
        <p> A list of diseases associated with</p>
        <html:radio property="geneDiseaseSelect" value="diseaseGene"/>a particular gene<br>
        <p>
        Please make a selection and submit.
        <p>

        <html:submit property="submit" value="continue"/>
    </html:form>

<p>
<jsp:include page="links.jsp" flush="true"/>

</td></tr></table></body>
</html>