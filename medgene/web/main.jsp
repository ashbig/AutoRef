<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>METAGENE : main</title>
    </head>
    <body>
    <center>
    <h1>Main Menu</h1>
    </center>

    <table width="80%" align="center" border="0"> 
    <p>HIP desease gene association database can help you to get the following information:</p>

    <html:form action="main.do">   
        <html:radio property="geneDiseaseSelect" value="geneDisease" />A list of genes associated with certain disease(s)<br>
        <html:radio property="geneDiseaseSelect" value="diseaseGene"/>A list of diseases associated with certain gene(s)<br>
        <p>
        Please make a selection and submit.
        <p>
        <html:submit property="submit" value="Submit"/>
    </html:form>

<p>
<jsp:include page="links.jsp" flush="true"/>

</td></tr></table></body>
</html>