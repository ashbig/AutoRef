<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Cloning Request</title>
</head>
<body bgcolor="white">

<h2><bean:message key="flex.name"/> : Cloning Request : Confirm Selection</h2>
<hr>
<html:errors/>
<p>
<html:form action="/ConfirmSelection.do">

<logic:present name="goodSequences">

<b> The following sequences are good quality sequences:</b>
<table border=1>
<tr>
<th></th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<logic:iterate id="gs" name="goodSequences">
<tr>
<logic:equal name="gs" property="species" value="Homo sapiens">
<td><input name="selection" type="checkbox" value="<bean:write name="gs" property="gi"/>"></td>
</logic:equal>
<logic:notEqual name="gs" property="species" value="Homo sapiens">
<td></td>
</logic:notEqual>
<td><a target=_new href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="gs" property="gi"/>&dopt=GenBank"><bean:write name="gs" property="accession"/></a></td>
<td><bean:write name="gs" property="description"/></td>
<td><bean:write name="gs" property="gi"/></td>
<td><bean:write name="gs" property="species"/></td>
<td><bean:write name="gs" property="flexstatus"/></td>
<td><bean:write name="gs" property="quality"/></td>
</tr>
</logic:iterate>
</table>
</logic:present>

<logic:present name="sameSequence">
<p><b> The following sequences have exactly the same DNA sequence:</b>

<P>
<logic:iterate id="ss" name="sameSequence">
<input name="selection" type="checkbox" value="<bean:write name="ss" property="key"/>">

<table border=1>
<tr>
<th>Flex ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<logic:iterate id="element" name="ss" property="value">
<tr>
<logic:equal name="element" property="id" value="-1">
<td><font color=red><bean:write name="element" property="id"/></font></td>
</logic:equal>
<logic:notEqual name="element" property="id" value="-1">
<td><font color=red><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="element" property="id"/>"><bean:write name="element" property="id"/></a></font></td>
</logic:notEqual>
<td><a target=_new href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="element" property="gi"/>&dopt=GenBank"><bean:write name="element" property="accession"/></a></td>
<td><bean:write name="element" property="description"/></td>
<td><bean:write name="element" property="gi"/></td>
<td><bean:write name="element" property="species"/></td>
<td><bean:write name="element" property="flexstatus"/></td>
<td><bean:write name="element" property="quality"/></td>
</tr>
</logic:iterate>
</table>
</logic:iterate>
</logic:present>

<logic:present name="homologs">
<p><b> The following sequences are very similar: </b>

<logic:iterate id="homo" name="homologs">
<p>
<b>Evalue</b>: <bean:write name="homo" property="value.blastResults.evalue"/><br>
<b>Identity</b>: <bean:write name="homo" property="value.blastResults.identity"/><br>
<b>Query CDS length</b>: <bean:write name="homo" property="value.blastResults.cdslength"/>      
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp
[ <b><a target=_new href="ViewAlignment.do?gi=<bean:write name="homo" property="key"/>">View Alignment</a></b> ]

<table border=1>
<tr>
<td></td><th>Flex ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<logic:iterate id="h" name="homo" property="value.homolog">
<tr>
<logic:equal name="h" property="species" value="Homo sapiens">
<td><input name="selection" type="checkbox" value="<bean:write name="h" property="gi"/>"></td>
</logic:equal>
<logic:notEqual name="h" property="species" value="Homo sapiens">
<td></td>
</logic:notEqual>
<logic:equal name="h" property="id" value="-1">
<td><font color=red><bean:write name="h" property="id"/><br></font></td>
</logic:equal>
<logic:notEqual name="h" property="id" value="-1">
<td><font color=red><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="h" property="id"/>"><bean:write name="h" property="id"/></a><br></font></td>
</logic:notEqual>
<td><a target=_new href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="h" property="gi"/>&dopt=GenBank"><bean:write name="h" property="accession"/><br></a></td>
<td><bean:write name="h" property="description"/></td>
<td><bean:write name="h" property="gi"/></td>
<td><bean:write name="h" property="species"/></td>
<td><bean:write name="h" property="flexstatus"/></td>
<td><bean:write name="h" property="quality"/></td>
</tr>

</logic:iterate>
</table>

</logic:iterate>
</table>
</logic:present>

<logic:present name="badSequences">
<p><b> The following sequences are questionable sequences:</b>
<table border=1>
<th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
<logic:iterate id="bs" name="badSequences">
<tr>
<td><a target=_new href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="bs" property="key"/>&dopt=GenBank"><bean:write name="bs" property="value.accession"/></a></td>
<td><bean:write name="bs" property="value.description"/></td>
<td><bean:write name="bs" property="value.gi"/></td>
<td><bean:write name="bs" property="value.species"/></td>
<td><bean:write name="bs" property="value.flexstatus"/></td>
<td><bean:write name="bs" property="value.quality"/></td>
</tr>
</logic:iterate>

</table>
</logic:present>

<p>
<center><input type=submit value="Submit"> <input type=reset value="Clear"></center>

</html:form>

</body>
</html:html>
