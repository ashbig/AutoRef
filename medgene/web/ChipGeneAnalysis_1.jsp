<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Analyze a Gene List</title>
    </head>
    <body>
    <center>
    <h1>Analyze a Gene List</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>

    <p>Please type in the disease name for your high-throughput experiment data. <BR><br>
       You may type in any name you like.  The corresponding MeSH term(s) will be displayed.</p>

    <html:form action="chipGeneAnalysis_1.do">   
    <table width="80%" align="center">
        <tr>
            <td>Disease term:</td>
            <td><html:text property="searchTerm" size="50" /></td>
        </tr>
        <tr>
            <td></td>
            <td><html:submit property="submit" value="continue"/></td>
        </tr>
    </html:form>
<p>
    </table>
<BR><BR><A HREF="http://www.nlm.nih.gov/mesh/" target="_blank"><b>Link to MeSH home</b></A><BR>
<BR>
<A HREF="DiseaseList.jsp" target="_blank"><b>Show all disease MeSH terms</b></A>
<jsp:include page="links.jsp" flush="true"/>

</td></tr></table></body>
</html>