<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head><title><bean:message key="flex.name"/> : Container Process History</title></head>
<body>
    <h2><bean:message key="flex.name"/> : Container Process History</h2>
    <hr>
    <br>
    <table>
    <tr>
        <th><CENTER>Protocol</CENTER></th>
        <th><CENTER>Execution Date</CENTER></th>
        <th><CENTER>Subprotocol</CENTER></th>
        <th><CENTER>Researcher</CENTER></th>
        <th><CENTER>Notes</CENTER></th>
        <th><CENTER>Container</CENTER></th>
        <th><CENTER>Container Type</CENTER></th>
        <th><CENTER>Container Label</CENTER></th>
    </tr>
    <logic:iterate id="threadElem" name="<%=Constants.THREAD_KEY%>" property="elements">
        <tr>
        <bean:define id="process" name="threadElem" property="process"/>
        <bean:define id="protocol" name="process" property="protocol"/>
        <bean:define id="container" name="threadElem" property="object"/>
            <td><CENTER><bean:write name="protocol" property="processname"/></CENTER></td>
            <td><CENTER><bean:write name="process" property="date"/></CENTER></td>
            <td><CENTER><bean:write name="process" property="subprotocol"/></CENTER></td>
            <td><CENTER><bean:write name="process" property="researcher.name"/></CENTER></td>
            <td><CENTER><bean:write name="process" property="extrainfo"/></CENTER></td>
            <td><CENTER><flex:linkContainer name="container" process="process">
                            <bean:write name="container" property="id"/>
                        </flex:linkContainer>
                </CENTER></td>
            <td><CENTER><bean:write name="container" property="type"/></CENTER></td>
            <td><CENTER><bean:write name="container" property="label"/></CENTER></td>
        </tr>
    </logic:iterate>
    </table>
</body>
</html>
