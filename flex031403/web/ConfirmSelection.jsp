<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title>FLEXGene : Cloning Request</title>
</head>
<body bgcolor="white">

<h2>FlexGene : Cloning Request : Confirm Selection</h2>
<hr>

<html:form action="/ConfirmSelection.do">

<logic:present name="goodSequences">

<p><b> The following sequences are good quality sequences:</b>
<table border=1>
<tr>
<th></th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<logic:iterate id="gs" name="goodSequences">
<tr>
<td><input name="selection" type="checkbox" value="<bean:write name="gs" property="key"/>"></td>
<td><a target=_new href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="gs" property="key"/>&dopt=GenBank"><bean:write name="gs" property="value.accession"/></a></td>
<td><bean:write name="gs" property="value.description"/></td>
<td><bean:write name="gs" property="value.gi"/></td>
<td><bean:write name="gs" property="value.species"/></td>
<td><bean:write name="gs" property="value.flexstatus"/></td>
<td><bean:write name="gs" property="value.quality"/></td>
</tr>
</logic:iterate>
</table>
</logic:present>

<logic:present name="sameSequence">
<p><b> The following sequences have exactly the same DNA sequence:</b>

<logic:iterate id="ss" name="sameSequence">
<input name="selection" type="checkbox" value="<bean:write name="ss" property="key"/>">

<table border=1>
<logic:iterate id="element" name="ss" property="value">
<tr>
<th>Flex ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<tr>
<td><font color=red><bean:write name="element" property="id"/></font></td>
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
<b>Query CDS length</b>: <bean:write name="homo" property="value.blastResults.cdslength"/><br>      [ <b><a target=_new href="ViewAlignment.do?gi=<bean:write name="homo" property="key"/>">View Alignment</a></b> ]

<table border=1>
<tr>
<td></td><th>Flex ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<logic:iterate id="h" name="homo" property="value.homolog">
<tr>
<td><input name="selection" type="checkbox" value="<bean:write name="h" property="gi"/>"></td>
<td><font color=red><bean:write name="h" property="id"/><br></font></td>
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
