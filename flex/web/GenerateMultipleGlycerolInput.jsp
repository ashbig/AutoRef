<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Generate Multiple Copies of Glycerol Stock</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Generate Multiple Copies of Glycerol Stock</h2>
<hr>
<html:errors/>

<p>
<html:form action="/GenerateMultipleGlycerol.do" focus="srclabels" method="POST">

<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">
        Enter all the source container labels: (separated by spaces)
    </td>
    <td></td>
    </tr>

    <tr>
    <td><html:textarea property="srclabels" rows="10"/>
    <td></td>
    </tr>
    <tr>
    <td class="prompt">
        Enter number of copies you would like to make:
    </td>
    <td></td>
    </tr>

    <tr>
    <td><html:text property="num"/>
    <td></td>
    </tr>
</table>

    <p><em><bean:message key="flex.researcher.barcode.prompt"/></em>
    <html:password property="researcherBarcode" size="40"/>
    
<table>
    <tr>
    <td><html:submit property="submit" value="Submit"/></td>
    <td><html:reset/></td>
    </tr>
</table>
</html:form>

</body>
</html:html>