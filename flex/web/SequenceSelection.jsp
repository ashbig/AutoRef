<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title>FLEXGene : Cloning Request</title>
</head>
<body bgcolor="white">

<h2>FlexGene : Cloning Request : Sequence Selection</h2>
<hr>

<html:form action="/SequenceSelection.do">

<logic:iterate id="sr" name="searchResult">
<p>
<table>
<tr>
<td>
<input name="checkOrder" type="checkbox" value="<bean:write name="sr" property="value.gi"/>"</td>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="sr" property="value.gi"/>&dopt=GenBank"><bean:write name="sr" property="value.accession"/><a> | <b>Flex Status: <font color="red"><bean:write name="sr" property="value.flexstatus"/></font></b>
</td>
<tr>
<td></td><td><bean:write name="sr" property="value.description"/></td>
</tr>
<tr>
<td></td><td><bean:write name="sr" property="value.accession"/>| GI: <bean:write name="sr" property="value.gi"/></td>
</tr>
</table>
</logic:iterate> 

<p>
<center>
<input type=submit value="Submit"> 
<input type=reset value="Clear">
</center>

</html:form>

</body>
</html:html>
