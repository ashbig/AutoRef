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
    <html:form action="main.do">  
    <table width="100%"><tr><td bgcolor="#ccccff"> <p>A list of genes associated with</p> </td></tr></table>     
        <html:radio property="geneDiseaseSelect" value="geneDisease" />A particular disease<br>      
        <html:radio property="geneDiseaseSelect" value="multiDisease"/>Multiple diseases<br>
        <html:radio property="geneDiseaseSelect" value="geneGene"/>A particular gene<br><br>
    <table width="100%"><tr><td bgcolor="#ccccff"> <p>A list of diseases associated with</p> </td></tr></table>      
        <html:radio property="geneDiseaseSelect" value="diseaseGene"/>A particular gene<br><br>
    <table width="100%"><tr><td bgcolor="#ccccff"> <p>Analyze a gene list</p> </td></tr></table>       
        <html:radio property="geneDiseaseSelect" value="chipGeneDisease"/>Categorize genes from disease related high-throughput experiments such as micro-array <br>
        <html:radio property="geneDiseaseSelect" value="chipGeneGene"/>Categorize genes from gene related high-throughput experiments such as micro-array <br>
        <p>
        Please make a selection and submit.
        <p>

        <html:submit property="submit" value="continue"/>
    </html:form>

<p>
<jsp:include page="links.jsp" flush="true"/>

</td></tr></table></body>
</html>