<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%-- 
Define the type of result to enter, if none is given, gell result is the default
--%>
<bean:define name="<%=Constants.PROTOCOL_NAME_KEY%>" id="resultType"/>

<html>
<head>
    <title><bean:message key="flex.name"/> : Enter Process Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="EnterPlate.do" focus="plateBarcode">
    <h2><bean:message key="flex.name"/> : Enter Process Results</h2>
    <hr>
    <html:errors/>
   
    <p>
   
    

    <table>
    <tr>
        <td colspan="2">
            <h3>Enter Process Results for Protocol:   <bean:write name="resultType"/></h3>
        </td>
    </tr>
    <tr>
        <td class="prompt">Plate Label:</td>
        <td><html:text property="plateBarcode"/></td>
    </tr>
    <tr>
       <td class="prompt"><bean:message key="flex.researcher.barcode.prompt"/></td>
       <td><html:text property="researcherBarcode"/></td>
    </tr>
    </table>
    <br>
    
    <html:submit/>
    
    <%-- Hidden field from the previous page for the result type--%>
    <html:hidden property="protocolString" value="<%=resultType.toString()%>"/>
    
<br>
<jsp:include page="QueueItemsDisplay.jsp" flush="true"/>

</html:form>
</body>
</html>
