<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*"%>
<%@ page import="edu.harvard.med.hip.flex.process.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%-- Get the mode of this form, edit or ready only --%>
<logic:present parameter="<%=Constants.FORM_MODE_KEY%>">
    <bean:parameter id="mode" name="<%=Constants.FORM_MODE_KEY%>" />
</logic:present>

<%-- by default, this page is in edit mode --%>
<logic:notPresent parameter="<%=Constants.FORM_MODE_KEY%>">
    <bean:define id="mode" value="<%=Constants.EDIT_MODE%>" />
</logic:notPresent>


<html>
<head>
    <title><bean:message key="flex.name"/> : DNA Gel Result Details</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="flex.name"/> : DNA Gel Result Details</h2>
<hr>
<html:errors/>
<p>
<logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
    <h3>Enter the results of this DNA gel</h3>
</logic:equal>

<logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
    <h3>Comfirm the DNA gel result</h3>
</logic:equal>
<p>
<html:form action="SaveGelResult.do" enctype="multipart/form-data">
    <table>
        <tr>
        <logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
            <td class="prompt">Please select the DNA gel image you would like to upload:</td>
            <td colspan="3"><html:file property="formFile" /></td>
        </logic:equal>
        <logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
            <td class="prompt">DNA gel image file:</td>
            <td colspan="3"><bean:write name="gelEntryForm" property="formFile.fileName"/></td>
        </logic:equal>
            
        </tr>
    </table>
</html:form>
</body>
</html>
