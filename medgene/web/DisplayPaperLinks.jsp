<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Medline Paper Links (PMID)</title></head>
<body>
<center>
<h1>Selected Paper Links</h1>
</center>
<p>
    <% String url = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=nucleotide&list_uids="; %>
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
