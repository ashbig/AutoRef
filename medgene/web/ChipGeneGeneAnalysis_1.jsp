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
    <p>Please type in the gene name for your high-throughput experiment data.  The 
       corresponding official gene symbol(s) will be displayed.</p>

    <html:form action="chipGeneGeneAnalysis_1.do">   
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
            <td><html:submit property="submit" value="continue"/></td>
        </tr>
    </table>
    </html:form>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>