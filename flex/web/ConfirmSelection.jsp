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

<table border=1>
<tr>
<th></th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>
</tr>

<logic:iterate id="gs" name="goodSequences">
<table border=1>
<th></th><th>Genbank Acc</th><th>Description</th><th>GI</th><th>Organism</th><th>Flex Status</th><th>Quality</th>

<tr>
<td><input name="good" type="checkbox" value="<bean:write name="gs" property="key"/>"></td>
<td><a target=_new href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="gs" property="key"/>&dopt=GenBank"><bean:write name="gs" property="value.accession"/></a></td>
<td><bean:write name="gs" property="value.description"/></td>
<td><bean:write name="gs" property="value.gi"/></td>
<td><bean:write name="gs" property="value.species"/></td>
<td><bean:write name="gs" property="value.flexstatus"/></td>
<td><bean:write name="gs" property="value.quality"/></td>
</tr>
</logic:iterate>
</table>


<p>
<center><input type=submit value="Submit"> <input type=reset value="Clear"></center>

</html:form>

</body>
</html:html>
