<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

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
    <p>Please type in all the disease names you want to search for,
    separated by comma (,). You may type in any term you like and 
    will be confirmed with the corresponding MeSH terms. The order of
    the gene list will be sorted by the order of the entry.</p>

    <html:form action="MultipleDiseaseSearch.do">   
    <table width="80%" align="center">
        <tr>
            <td>Disease terms:</td>
            <td><html:text property="searchTerms" size="50" /></td>
        </tr>
        <tr>
            <td></td>
            <td><html:submit property="submit" value="Submit"/></td>
        </tr>
    </table>
    </html:form>

<p>
<BR><BR><A HREF="http://www.nlm.nih.gov/mesh/"><b>Link to MeSH home</b></A><BR>
<A HREF="DiseaseList.jsp"><b>Show all disease MeSH terms</b></A>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>