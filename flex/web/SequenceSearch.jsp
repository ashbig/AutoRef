<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Cloning Request History</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cloning Request History: Sequence Search</h2>
<hr>
<html:errors/>
<p>
<html:form action="/SequenceSearch.do" focus="searchString">
<table>
    <tr>
    <td class="prompt">Please enter the search key word:</td>
    <td><html:text property="searchString" size="40"/></td>
    </tr>

    <logic:present name="species">
    <tr>
    <td class="prompt">Search Species:</td>
    <td><select name="species">
        <option value="all">All
        <logic:iterate id="oneSpecies" name="species" scope="request">
            <option value="<bean:write name="oneSpecies"/>"><bean:write name="oneSpecies"/>
        </logic:iterate>
        </select>
    </td>
    </tr>
    </logic:present>
  
    <tr>
    <td></td><td><html:submit property="submit" value="Search"/></td>
    </tr>
</table>
</html:form>

<p>
This search form is fully compatible with GenBank's search engine.
Please follow the following hint.
<dl>
<dt><i>Search hint:</i>
<dd>1. Multiple Genbank Accession numbers separated by comma (,)
<dd>2. Use exact Genbank Accession number
<dd>3. Use AND, OR, NOT for boolean searches
</dl>

<logic:present name="customerRequest" scope="request">
<p>You have the following cloning requests:
<table border="1" cellpadding="2" cellspacing="0" width="100%">
<tr class="headerRow"><th>Date</th><th>Total Sequences</th><th>Processed Sequences</th><th>Non-processed Sequences</th></tr>
<logic:iterate id="oneRequest" name="customerRequest">
<flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
<td><a href="DisplayRequest.do?requestid=<bean:write name="oneRequest" property="id"/>&requestDate=<bean:write name="oneRequest" property="date"/>"><bean:write name="oneRequest" property="date"/></a></td>
<td><bean:write name="oneRequest" property="numSequences"/></td>
<td><bean:write name="oneRequest" property="processedSequences"/></td>
<td><bean:write name="oneRequest" property="pendingSequences"/></td>
</flex:row>
</logic:iterate>
</table>
</logic:present>

</body>
</html:html>