<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title>FLEXGene : Cloning Request</title>
</head>
<body bgcolor="white">

<h2>FlexGene : Cloning Request : Sequence Search</h2>
<hr>

<html:errors/>

<html:form action="/SequenceSearch.do" focus="searchString">
    <p><b>Please enter the search key word:</b>
    <html:text property="searchString" size="40"/>
    <html:submit property="submit" value="Search"/>
</html:form>

<p>
<dl>
<dt><i>Search hint:</i>
<dd>1. Multiple Genbank Accession numbers separated by comma (,)
<dd>2. Use exact Genbank Accession number
<dd>3. Use AND, OR, NOT for boolean searches
</dl>

<logic:present name="customerRequest">
<p><b>You have the following cloning requests:</b>
<logic:iterate id="oneRequest" name="customerRequest">
<p>Date: <bean:write name="oneRequest" property="date"/>
<table border=1 align=center width=90%>
<th>Sequence ID</th><th>Description</th><th>Status</th>

<logic:iterate id="sequence" name="oneRequest" property="sequences">
<tr>
<td><a href="FlexSequenceUI.jsp?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="sequence" property="id"/>"><bean:write name="sequence" property="id"/></a></td>
<td><bean:write name="sequence" property="description"/></td>
<td><bean:write name="sequence" property="flexstatus"/></td>
</tr>
</logic:iterate>

</table>

</logic:iterate>
</logic:present>

</body>
</html:html>