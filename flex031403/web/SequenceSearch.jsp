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
<table>
    <tr>
    <td><b>Please enter the search key word:</b></td>
    <td><html:text property="searchString" size="40"/></td>
    </tr>

    <logic:present name="species">
    <tr>
    <td><b>Search Species:</b></td>
    <td><select name="species">
        <option value="all">All
        <logic:iterate id="oneSpecies" name="species" scope="request">
            <option value="<bean:write name="oneSpecies"/>"><bean:write name="oneSpecies"/>
        </logic:iterate>
    </td>
    </tr>
    </logic:present>
  
    <tr>
    <td></td><td><html:submit property="submit" value="Search"/></td>
    </tr>
</table>
</html:form>

<p>
<dl>
<dt><i>Search hint:</i>
<dd>1. Multiple Genbank Accession numbers separated by comma (,)
<dd>2. Use exact Genbank Accession number
<dd>3. Use AND, OR, NOT for boolean searches
</dl>

<logic:present name="customerRequest" scope="request">
<p><b>You have the following cloning requests:</b>
<logic:iterate id="oneRequest" name="customerRequest">
<p>Date: <bean:write name="oneRequest" property="date"/>
<table border=1 align=center width=90%>
<th>Sequence ID</th><th>Description</th><th>Status</th>

<logic:iterate id="sequence" name="oneRequest" property="sequences">
<tr>
<td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="sequence" property="id"/>"><bean:write name="sequence" property="id"/></a></td>
<td><bean:write name="sequence" property="description"/></td>
<td><bean:write name="sequence" property="flexstatus"/></td>
</tr>
</logic:iterate>

</table>

</logic:iterate>
</logic:present>

</body>
</html:html>