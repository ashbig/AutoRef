<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.form.EnterResultForm" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Enter Expression Plate Results </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Enter Expression Plate Results </h2>
<hr>
<html:errors/>
<p>

<table>
    <tr>
        <td class="prompt">Plate:</td>
        <td><bean:write name="newPlate" /></td>        
    </tr>
</table>

<html:form action="/SetDisplay.do">
<input type="hidden" name="newPlate" value="<bean:write name="newPlate" />">
<html:hidden property="nextForward"/>

<table>
    <tr>
        <td colspan=2 class="prompt">Check the attributes to be displayed on next page:   </td>     
    </tr>

    <tr>
        <td><html:checkbox property="well"/>Well</td>
        <td><html:checkbox property="sampleid"/>Sample ID</td>
    </tr>
    <tr>
        <td><html:checkbox property="geneSymbol"/>Gene Symbol</td>
        <td><html:checkbox property="pa"/>PA Number (for pseudomonas)</td>
    </tr>
    <tr>
        <td><html:checkbox property="sgd"/>SGD (for yeast)</td>
        <td><html:checkbox property="masterClone"/>Master Clone ID</td>
    </tr>
    <tr>
        <td><html:checkbox property="researcher"/>Author</td>
        <td><html:checkbox property="restriction"/>Restriction Digest</td>
    </tr>
    <tr>
        <td><html:checkbox property="pcr"/>PCR</td>
        <td><html:checkbox property="colony"/>Colony</td>
    </tr>
    <tr>
        <td><html:checkbox property="florescence"/>Fluorescence</td>
        <td><html:checkbox property="protein"/>Protein Expression</td>
    </tr>
    <tr>
        <td><html:checkbox property="status"/>Status</td>
    </tr>
    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>

</table>
</html:form>

<table>
    <tr>     
        <html:form action="/PrintLabel.do">
        <input type="hidden" name="label" value="<bean:write name="newPlate"/>"/>
        <td><html:submit value="Print Barcode"/></td>
        </html:form>
    </tr>
</table>

</body>
</html>