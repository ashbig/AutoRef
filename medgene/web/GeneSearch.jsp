<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Gene Search</title>
    </head>
    <body>
    <center>
    <h1><br>Diseases Associated with a Particular Gene</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
    <p><br>Please type in a gene term.  The 
       corresponding official gene symbol(s) will be displayed.<br></p>

    <html:form action="GeneSearch.do">   
    <table width="80%" align="left">
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
            <td><br></td>
            <td><br><html:submit property="submit" value="continue"/></td>
        </tr>
    </table>
    </html:form>

<br><br><br><br><br><p>
<jsp:include page="links.jsp" flush="true"/>
</body>
</html>