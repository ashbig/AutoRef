<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>METAGENE : Gene Search</title>
    </head>
    <body>
    <center>
    <h1>Diseases Associated with Gene(s)</h1>
    </center>

    <table width="80%" align="center" border="0"> 
    <html:errors/>
    <p>Please type in a gene term and you will be confirmed with the 
       corresponding official gene symbol(s).</p>

    <html:form action="GeneSearch.do">   
    <table width="80%" align="center">
        <tr>
            <td><html:select property="term">
                <html:option value="Gene Name" />
                <html:option value="Gene Symbol" />
                <html:option value="Locus ID" />
                </html:select>
            </td>
            <td><html:text property="searchTerm" size="50"/></td>
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