<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Medline Paper Links</title></head>
<body>
<center>
<h1>Selected Paper Links (PMID)</h1>
</center>
<p>
    &nbsp;&nbsp;<b>Disease Mesh Term:</b>&nbsp;&nbsp;<bean:write name="disease_name"/><br>
    &nbsp;&nbsp;<b>Gene Name:</b>&nbsp;&nbsp;<bean:write name="gene_symbol"/><br>
    <% String url = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids="; %>
    <ul>
    <logic:iterate id="paper" name="medline_records"> 
        <li>
        <a href="<%= url %><bean:write name="paper"/>" target="_blank"><bean:write name="paper"/></a>
        </li>
    </logic:iterate> 
    </ul>

<p>
<br>
<jsp:include page="links.jsp" flush="true"/>

</body>
</html>
