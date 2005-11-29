<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Plate Condensation</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<h2><bean:message key="flex.name"/> : Plate Condensation</h2>
<hr>
<html:errors/>

<p><table>
<logic:iterate name="containerMaps" id="containerMap">
    <tr>
        <td class="promp" valign=="top">Source Containers:</td>
        <td>
            <logic:iterate name="containerMap" property="src" id="srcContainer">
                <a href="ViewContainerDetails.do?<%=Constants.CONTAINER_ID_KEY%>=<bean:write name="srcContainer" property="id"/>"><bean:write name="srcContainer" property="label"/></a><br>
            </logic:iterate>
        </td>
        <td class="promp" valign="top">Destination Containers:</td>
        <td>
            <logic:iterate name="containerMap" property="dest" id="destContainer">
                <a href="ViewContainerDetails.do?<%=Constants.CONTAINER_ID_KEY%>=<bean:write name="destContainer" property="id"/>">><bean:write name="destContainer" property="label"/></a><br>
            </logic:iterate>
        </td>    
    </tr>
</logic:iterate>

</table>

</body>
</html>