<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

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

</body>
</html:html>