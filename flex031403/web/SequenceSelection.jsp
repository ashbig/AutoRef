<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Cloning Request History</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cloning Request History: Sequence Selection</h2>
<hr>
<html:errors/>
<p>
<h3>
We have found the following sequences. Please make your selection and we'll search
FLEXGene database for similar sequences.</h3>
<p>
<html:form action="/SequenceSelection.do">
<table>
<logic:iterate id="sr" name="searchResult">
<tr>
<td>
<input name="checkOrder" type="checkbox" value="<bean:write name="sr" property="gi"/>"</td>
<td><a target="_new" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="sr" property="gi"/>&dopt=GenBank"><bean:write name="sr" property="accession"/><a> | <b>Flex Status: <font color="red"><bean:write name="sr" property="flexstatus"/></font></b>
</td>
<tr>
<td></td><td><bean:write name="sr" property="description"/></td>
</tr>
<tr>
<td></td><td><bean:write name="sr" property="accession"/>| GI: <bean:write name="sr" property="gi"/></td>
</tr>
</logic:iterate> 
</table>

<p>
<center>
<input type=submit value="Submit"> 
<input type=reset value="Clear">
</center>

</html:form>

</body>
</html:html>
