<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Database Sequence Search</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Database Sequence Search</h2>
<hr>
<html:errors/>

<p>
<html:form action="/SequenceQuery.do" focus="searchTerm" method="POST" enctype="multipart/form-data">
<input type="hidden" name="querySelect" value="<bean:write name="querySelect"/>"/>
<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">Search Term:</td>
    <td><html:select property="searchType">
        <html:options
        collection="nametypes"
        property="name"
        labelProperty="name"
        />
    </html:select></td>
    <td></td>
    </tr>

    <tr>
    <td></td>
    <td><html:radio property="searchTermType" value="nonfile">
        Enter all the search terms: (separated by spaces)
        </html:radio>
    </td>
    <td><html:radio property="searchTermType" value="file">
        Upload the file containing all the search terms: (separated by spaces)
        </html:radio>
    </td>
    </tr>

    <tr>
    <td></td>
    <td><html:textarea property="searchTerm" rows="10"/>
    </td>
    <td valign="top"><html:file property="filename" />
    </td>
    </tr>
</table>

<p>
<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">Project:</td>
    <td><html:select property="project">
        <html:option value="-1">All</html:option>
        <html:options
        collection="projects"
        property="id"
        labelProperty="name"
        />
    </html:select></td>
    <td></td>
    </tr>
 
    <tr>
    <td></td>
    <td><html:submit property="submit" value="Continue"/></td>
    <td><html:reset/></td>
    </tr>
</table>
</html:form>

</body>
</html:html>