<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Cloning Request History</title>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cloning Request History: Confirm Selection</h2>
<hr>
<html:errors/>
<p>
<b>You have submitted the following sequences. They are grouped into different
categories based on similarity to FLEXGene sequences and qualities. Please make
appropriated selection and submit your cloning request to FLEXGene.</b>

<p>
<logic:present name="submittedSequences">
<logic:iterate id="submittedSeq" name="submittedSequences">
<bean:write name="submittedSeq" property="accession" />
<br>
</logic:iterate>
</logic:present>

<html:form action="/ConfirmSelection.do">

<logic:present name="newSequences">
<p>
<h3>Category: Good quality sequences not already present in FLEXGene</h3>
<table border=1>
<tr>
<th></th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<logic:iterate id="ns" name="newSequences">
<tr>
<logic:equal name="ns" property="speciesCategory" value="allowed">
<td><input name="selection" type="checkbox" value="<bean:write name="ns" property="gi"/>"></td>
</logic:equal>
<logic:notEqual name="ns" property="speciesCategory" value="allowed">
<td></td>
</logic:notEqual>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="ns" property="gi"/>&dopt=GenBank"><bean:write name="ns" property="accession"/></a></td>
<td><bean:write name="ns" property="description"/></td>
<td><bean:write name="ns" property="gi"/></td>
<td><bean:write name="ns" property="species"/></td>
<td><bean:write name="ns" property="flexstatus"/></td>
<td><bean:write name="ns" property="quality"/></td>
</tr>
</logic:iterate>
</table>
</logic:present>

<logic:present name="displaySameSequence">
<p>
<h3>Category: Good quality sequences with CDS already present in FLEXGene</h3>
<table border=1>
<tr>
<th></th><th>FLEXGene ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>

<logic:present name="goodSequences">
<logic:iterate id="gs" name="goodSequences">
<tr>
<logic:equal name="gs" property="speciesCategory" value="allowed">
<td><input name="selection" type="checkbox" value="<bean:write name="gs" property="gi"/>"></td>
</logic:equal>
<logic:notEqual name="gs" property="speciesCategory" value="allowed">
<td></td>
</logic:notEqual>
<logic:equal name="gs" property="id" value="-1">
<td><font color=red><bean:message key="flex.notapplicable" /></font></td>
</logic:equal>
<logic:notEqual name="gs" property="id" value="-1">
<td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="gs" property="id"/>"><bean:write name="gs" property="id"/></a></td>
</logic:notEqual>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="gs" property="gi"/>&dopt=GenBank"><bean:write name="gs" property="accession"/></a></td>
<td><bean:write name="gs" property="description"/></td>
<td><bean:write name="gs" property="gi"/></td>
<td><bean:write name="gs" property="species"/></td>
<td><bean:write name="gs" property="flexstatus"/></td>
<td><bean:write name="gs" property="quality"/></td>
</tr>
</logic:iterate>
</logic:present>

<logic:present name="sameSequence">
<logic:iterate id="ss" name="sameSequence">
<tr>
<td ROWSPAN=2>
<input name="selection" type="checkbox" value="<bean:write name="ss" property="key"/>">
</td>
<logic:iterate id="element" name="ss" property="value">
<logic:equal name="element" property="id" value="-1">
<td><font color=red><bean:message key="flex.notapplicable" /></font></td>
</logic:equal>
<logic:notEqual name="element" property="id" value="-1">
<td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="element" property="id"/>"><bean:write name="element" property="id"/></a></td>
</logic:notEqual>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="element" property="gi"/>&dopt=GenBank"><bean:write name="element" property="accession"/></a></td>
<td><bean:write name="element" property="description"/></td>
<td><bean:write name="element" property="gi"/></td>
<td><bean:write name="element" property="species"/></td>
<td><bean:write name="element" property="flexstatus"/></td>
<td><bean:write name="element" property="quality"/></td>
</tr>
</logic:iterate>
</logic:iterate>
</logic:present>

<logic:present name="cdsMatchSequences">
<logic:iterate id="cdsMatch" name="cdsMatchSequences">
<logic:iterate id="h" name="cdsMatch" property="value.homolog">
<tr>
<logic:equal name="h" property="id" value="-1">
<td><font color=red><bean:message key="flex.notapplicable" /><br></font></td>
</logic:equal>
<logic:notEqual name="h" property="id" value="-1">
<td ROWSPAN=2><input name="selection" type="checkbox" value="<bean:write name="h" property="gi"/>"></td>
<td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="h" property="id"/>"><bean:write name="h" property="id"/></a><br></td>
</logic:notEqual>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="h" property="gi"/>&dopt=GenBank"><bean:write name="h" property="accession"/><br></a></td>
<td><bean:write name="h" property="description"/></td>
<td><bean:write name="h" property="gi"/></td>
<td><bean:write name="h" property="species"/></td>
<td><bean:write name="h" property="flexstatus"/></td>
<td><bean:write name="h" property="quality"/></td>
</tr>
</logic:iterate>
</logic:iterate>
</logic:present>
</table>
</logic:present>

<logic:present name="homologs">
<p>
<h3>Category: Good quality sequences with related FLEXGene sequence</h3>

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
<td></td><th>FLEXGene ID</th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>
<logic:iterate id="h" name="homo" property="value.homolog">
<tr>
<logic:equal name="h" property="speciesCategory" value="allowed">
<td><input name="selection" type="checkbox" value="<bean:write name="h" property="gi"/>"></td>
</logic:equal>
<logic:notEqual name="h" property="speciesCategory" value="allowed">
<td></td>
</logic:notEqual>
<logic:equal name="h" property="id" value="-1">
<td><font color=red><bean:message key="flex.notapplicable" /><br></font></td>
</logic:equal>
<logic:notEqual name="h" property="id" value="-1">
<td><a href="ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<bean:write name="h" property="id"/>"><bean:write name="h" property="id"/></a><br></td>
</logic:notEqual>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="h" property="gi"/>&dopt=GenBank"><bean:write name="h" property="accession"/><br></a></td>
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
<p>
<h3>Category: Questionable quality sequences</h3>
<table border=1>
<th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
<logic:iterate id="bs" name="badSequences">
<tr>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="bs" property="key"/>&dopt=GenBank"><bean:write name="bs" property="value.accession"/></a></td>
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
