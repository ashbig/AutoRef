<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Search FLEXGene Database</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Search FLEXGene Database</h2>
<hr>
<html:errors/>

<p>
<html:form action="/SearchFlex.do" focus="searchTerm" method="POST" enctype="multipart/form-data">

<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">Search Name:</td>
    <td><html:text property="searchName"/></td>
    <td></td>
    </tr>

    <tr>
    <td class="prompt">Search Term:</td>
    <td><html:select property="searchType">
        <html:options
        collection="searchTerms"
        property="searchName"
        labelProperty="searchValue"
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

    <tr>
    <td class="prompt">Blast Parameters:</td>
    <td></td><td></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Percent Identity (%):
    <td><html:text property="pid"/></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Alignment Length:
    <td><html:text property="length"/></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Maximum Hits:
    <td><html:text property="hits"/></td>
    </tr>

    <tr>
    <td></td>
    <td class="prompt">Database:
    <td><html:select property="searchDatabase">
        <html:options
        collection="searchDatabases"
        property="name"
        labelProperty="value"
        />
    </html:select></td>
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