<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : rearray</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="RearraySelection.do">
    <h2><bean:message key="flex.name"/> : Rearray</h2>
    <hr>
    <html:errors/>

<p><b><u>Choose the rearray type:</u></b></p>
<dl>
    <dd><html:radio property="rearrayType" value="rearraySample"/>Rearray Samples
    <dd><html:radio property="rearrayType" value="rearrayClone"/>Rearray Clones
</dl>

    <html:submit/>

</html:form>
</body>
</html>