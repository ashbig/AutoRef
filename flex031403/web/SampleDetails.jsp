<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.Process" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<bean:define name="<%=Constants.SAMPLE_KEY%>" id="sample"/>
<bean:define id="sequence" name="sample" property="flexSequence"/>

<bean:define name="<%=Constants.PROCESS_KEY%>" id="process"/>

<logic:present name="<%=Constants.RESULT_KEY%>">
    <bean:define name="<%=Constants.RESULT_KEY%>" id="result"/> 
</logic:present>

<bean:define name="sample" property="container" id="container"/>

<html>
<head><title>Sample Details</title></head>
<body>
<h2>Sample <bean:write name="sample" property="id"/></h2>
<br>
<table>
<tr>
    <th>sequence</th>
    <td>
        <flex:linkFlexSequence sequenceName="sequence">
            <bean:write name="sequence" property="id"/>
        </flex:linkFlexSequence>

    </td>
    <th>container</th>
    <td>
       <%-- <flex:linkContainer name="container">
            <bean:write name="container" property="label"/>
        </flex:linkContainer>
--%>
    </td>
</tr>
<tr>
    <th>
    </th>
    <td>
    </td>
</tr>
</table>

</body>
</html>

