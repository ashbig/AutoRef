<%@ page contentType="text/html"%>
<%@ page language="java" %>


<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%-- The container that was searched --%>
<logic:present name="<%=Constants.CONTAINER_KEY%>"> 
    <bean:define name="<%=Constants.CONTAINER_KEY%>" id="searchContainer"/>
</logic:present>
<html>
<head>
    <title><bean:message key="flex.name"/> : Container Process History</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
    <h2><bean:message key="flex.name"/> : Container Process History</h2>
    <hr>
    <html:errors/>
    <p>
    <table border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>Protocol</th>
        <th>Execution Date</th>
        <th>Subprotocol</th>
        <th>Researcher</th>
        <th>Container</th>
        <th>Container Type</th>
    </tr>
    <logic:iterate id="threadElem" name="<%=Constants.THREAD_KEY%>" property="elements">
        
        <bean:define id="process" name="threadElem" property="process"/>
        <bean:define id="protocol" name="process" property="protocol"/>
        <bean:define id="container" name="threadElem" property="object"/>
        <flex:row property="object" match="searchContainer" matchStyleClass="highlightRow" oddStyleClass="oddRow" evenStyleClass="evenRow">

            <td><flex:write name="protocol" property="processname"/></td>
            <td><flex:write name="process" property="date"/></td>
            <td><flex:write name="process" property="subprotocol"/></td>
            <td><flex:write name="process" property="researcher.name"/></td>
            <td><flex:linkContainer name="container" process="process">
                            <bean:write name="container" property="label"/>
                        </flex:linkContainer>
            </td>
            <td><flex:write name="container" property="type"/></td>
        </flex:row>
    </logic:iterate>
    </table>
</body>
</html>