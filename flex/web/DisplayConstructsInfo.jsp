<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Query Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query Results</h2>
<hr>
<html:errors/>

<p>

<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th rowspan="2">Sequence ID</th><th rowspan="2">Version</th><th rowspan="2">Project</th><th rowspan="2">Workflow</th><th rowspan="2">Status</th><th colspan="6">Available Clones</th>
    </tr>
    <tr bgcolor="#9bbad6">
    <th>Clone ID</th><th>Clone Name</th><th>Clone Type</th><th>Cloning Strategy</th><th>Vector</th><th>Status</th>
    </tr>

    <logic:iterate name="info" id="infoBean">
    <tr>
    <td rowspan="<bean:write name="infoBean" property="numOfClones"/>"><flex:write name="infoBean" property="sequenceid"/></td>
    
        <logic:iterate name="infoBean" property="constructInfos" id="constructInfo">
        <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="constructType"/></td>
        <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="projectName"/></td>
        <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="workflowName"/></td>
        <td rowspan="<bean:write name="constructInfo" property="numOfClones"/>"><flex:write name="constructInfo" property="status"/></td>
            
        <logic:equal name="constructInfo" property="numOfClones" value="0">
            <td>NA</td>
            <td>NA</td>
            <td>NA</td>
            <td>NA</td>
            <td>NA</td>
            <td>NA</td>
        </logic:equal>

            <logic:iterate name="constructInfo" property="clones" id="clone">
            <td>
            <A href="ViewClone.do?cloneid=<bean:write name="clone" property="cloneid"/>&isCloneStorageDisplay=1">
                <flex:write name="clone" property="cloneid"/>
            </A>
            </td>
            <td><flex:write name="clone" property="clonename"/></td>
            <td><flex:write name="clone" property="clonetype"/></td>
            <td><flex:write name="clone" property="cloningstrategy.name"/></td>
            <td><a target="_blank" href="ViewVector.do?vectorname=<bean:write name="clone" property="cloningstrategy.clonevector.name"/>">
                <flex:write name="clone" property="cloningstrategy.clonevector.name"/>
            </a></td>
            <td><flex:write name="clone" property="status"/></td>
            </tr>
            </logic:iterate>
        </tr>
        </logic:iterate>
        </tr>
    </logic:iterate>
</table>

</body>
</html>