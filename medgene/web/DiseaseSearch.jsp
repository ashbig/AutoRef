<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>METAGENE : Disease Search</title>
    </head>
    <body>
    <center>
    <h1>Genes Associated with a Certain Disease</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
    <p>Please type in a disease name. You may type in any term you like and 
    will be confirmed with the corresponding MeSH term(s).</p>

    <html:form action="diseaseSearch.do">   
    <table width="80%" align="center">
        <tr>
            <td>Disease term:</td>
            <td><html:text property="searchTerm" size="50" /></td>
        </tr>
        <tr>
            <td></td>
            <td><html:submit property="submit" value="Submit"/></td>
        </tr>
    </table>
    </html:form>

<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>