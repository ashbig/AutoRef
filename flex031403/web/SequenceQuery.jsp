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

    <tr>
    <td class="prompt">FLEXGene Status:</td>
    <td><html:select property="flexstatus">
        <html:options
        name="flexstatus"
        />
    </html:select></td>
    <td></td>
    </tr> 
</table>

<p>
<i>For FLEXGene status as "INPROCESS" only:</i>
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
    <td class="prompt">Workflow:</td>
    <td><html:select property="workflow">
        <html:option value="-1">All</html:option>
        <html:options
        collection="workflows"
        property="id"
        labelProperty="name"
        />
    </html:select></td>
    <td></td>
    </tr>

    <tr>
    <td class="prompt">Have gone through:</td>
    <td><html:select property="plate">
        <html:option value="oligo">Oligo Plate (Original)</html:option>
        <html:option value="oligoD">Oligo Plate (Diluted)</html:option>
        <html:option value="pcr1">PCR Plate (Step1)</html:option>
        <html:option value="pcr2">PCR Plate (Step2)</html:option>
        <html:option value="gel">PCR Gel Plate</html:option>
        <html:option value="filter">Filter Plate</html:option>
        <html:option value="bp">BP Reaction Plate</html:option>
        <html:option value="cr">Capture Reaction Plate</html:option>
        <html:option value="tr">Transformation Plate</html:option>
        <html:option value="agar">Agar Plate</html:option>
        <html:option value="culture">Culture Plate</html:option>
        <html:option value="dna">DNA Plate</html:option>
        <html:option value="glycerol">Glycerol Plate</html:option>
        </html:select>
    </td>
    <td></td>
    </tr>

    <tr>
    <td class="prompt">Display results if any?</td>
    <td><html:radio property="isResultDisplay" value="true">
        Yes
        </html:radio>
    </td>
    <td><html:radio property="isResultDisplay" value="false">
        No
        </html:radio>
    </td>
    </tr>
 
    <tr>
    <td></td>
    <td><html:submit property="submit" value="Continue"/></td>
    <td><html:submit property="reset" value="Reset"/></td>
    </tr>
</table>
</html:form>

</body>
</html:html>