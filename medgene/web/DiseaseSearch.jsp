<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Disease Search</title>
    </head>
    <body>
    <center><br>
    <h1>Genes Associated with a Particular Disease</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
    <p><br>Please type in a disease name. You may type in any name you like.  The corresponding MeSH term(s) will be displayed.</p>

    <html:form action="diseaseSearch.do">   
    <table width="80%" align="center">
        <tr>
            <td><br>Disease term:</td>
            <td><br><html:text property="searchTerm" size="50" /></td>
        </tr>
        <tr>
            <td><br><br></td>
            <td><html:submit property="submit" value="continue"/></td>
        </tr>
    </html:form>
    <p>
    </table>
<BR><BR><br><br><A HREF="http://www.nlm.nih.gov/mesh/" target="_blank"><b>Link to MeSH home</b></A>
<BR><BR>
<A HREF="DiseaseList.jsp" target="_blank"><b>Show all disease MeSH terms</b></A>
<br><br><br>
<jsp:include page="links.jsp" flush="true"/>

</td></tr></table></body>
</html>