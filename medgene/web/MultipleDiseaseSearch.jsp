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
    <h1>Genes Associated with Multiple Diseases</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
    <p><br>Please type in all the disease names you want to search for,
    separated by comma (,). You may type in any name you like. The corresponding MeSH terms will be displayed. The order of
    the gene list will be sorted by the order of the entry.</p>

    <html:form action="MultipleDiseaseSearch.do">   
    <table width="80%" align="center">
        <tr>
            <td><br>Disease terms:</td>
            <td><br><html:text property="searchTerms" size="50" /></td>
        </tr>
        <tr>
            <td></td>
            <td><br><html:submit property="submit" value="continue"/></td>
        </tr>
    </table>
    </html:form>

<p>
<BR><BR><br><br><A HREF="http://www.nlm.nih.gov/mesh/" target="_blank"><b>Link to MeSH home</b></A><BR>
<BR>
<A HREF="DiseaseList.jsp" target="_blank"><b>Show all disease MeSH terms</b></A><br>
<br><jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>