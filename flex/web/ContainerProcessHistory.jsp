<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

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
    <html:errors/>
    <p>
    <table>
    <tr>
        <th>Protocol</th>
        <th>Execution Date</th>
        <th>Subprotocol</th>
        <th>Researcher</th>
        <th>Notes</th>
        <th>Container</th>
        <th>Container Type</th>
        <th>Container Label</th>
    </tr>
    <logic:iterate id="threadElem" name="<%=Constants.THREAD_KEY%>" property="elements">
        <tr>
        <bean:define id="process" name="threadElem" property="process"/>
        <bean:define id="protocol" name="process" property="protocol"/>
        <bean:define id="container" name="threadElem" property="object"/>
            <td><bean:write name="protocol" property="processname"/></td>
            <td><bean:write name="process" property="date"/></td>
            <td><bean:write name="process" property="subprotocol"/></td>
            <td><bean:write name="process" property="researcher.name"/></td>
            <td><bean:write name="process" property="extrainfo"/></td>
            <td><flex:linkContainer name="container" process="process">
                            <bean:write name="container" property="id"/>
                        </flex:linkContainer>
            </td>
            <td><bean:write name="container" property="type"/></td>
            <td><bean:write name="container" property="label"/></td>
        </tr>
    </logic:iterate>
    </table>
</body>
</html>