<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Display Information</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Display Information</h2>
<hr>
<html:errors/>

<p>
<html:form action="/QueryDisplaySetup.do">

<logic:present name="isResultDisplay">
    <input type="hidden" name="isResultDisplay" value="<bean:write name="isResultDisplay"/>"/>
</logic:present>

<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">Sequence</td>
    <td><html:checkbox property="id"/>ID</td>
    <td><html:checkbox property="gi"/>GI</td>
    </tr>

    <tr>
    <td class="prompt"></td>
    <td><html:checkbox property="genename"/>Gene Name</td>
    <td><html:checkbox property="genbank"/>Genbank Accession</td>
    </tr>

    <tr>
    <td class="prompt"></td>
    <td><html:checkbox property="genesymbol"/>Gene Symbol</td>
    <td><html:checkbox property="pa"/>PA Number (for Pseudomonas only)</td>
    </tr>

    <tr>
    <td class="prompt">Plate and Sample</td>
    <td><html:checkbox property="label"/>Label</td>
    <td><html:checkbox property="well"/>Well</td>
    </tr>

    <tr>
    <td class="prompt"></td>
    <td><html:checkbox property="type"/>Construct Type (fusion or closed)</td>
    <td><html:checkbox property="oligo"/>Oligo Sequence</td>
    </tr>

    <tr>
    <td class="prompt">Project and Workflow</td>
    <td><html:checkbox property="project"/>Project Name</td>
    <td><html:checkbox property="workflow"/>Workflow Name</td>
    </tr>

    <tr>
    <td class="prompt">Dsiplay</td>
    <td><html:select property="display">
            <html:option value="html">HTML</html:option>
            <html:option value="text">Text</html:option>
        </html:select>
    </td>
    <td></td>
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