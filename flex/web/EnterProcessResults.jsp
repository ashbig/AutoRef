<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title><bean:message key="flex.name"/> : Enter Process Results</title></head>
<body>
    
<html:form action="EnterPlate.do" focus="plateBarcode">
    <h2><bean:message key="flex.name"/> : Enter Process Results</h2>
    <hr>
    <html:errors/>
    <p>
    <center>
    <table>
    <logic:present name="<%=Constants.CONTAINER_KEY%>">
        <bean:write name="<%=Constants.CONTAINER_KEY%>" property="label"/> was processed sucessfully.
    </logic:present>
    <tr>
        <td colspan="2">
            <h3>Enter Process Results</h3>
        </td>
    </tr>
    <tr>
        <td>Result Type:</td>
        <td>
            <html:select property="protocolString">
                <html:option value="<%=Protocol.PERFORM_TRANSFORMATION%>"><%=Result.TRANSFORMATION_TYPE%></html:option>
                <html:option value="<%=Protocol.RUN_PCR_GEL%>"><%=Result.GEL_RESULT_TYPE%></html:option>
            </html:select>
        </td>
        
    </tr>
    <tr>
        <td>Plate Label:</td>
        <td><html:text property="plateBarcode"/></td>
    </tr>
    <tr>
       <td><bean:message key="flex.researcher.barcode.prompt"/>:</td>
       <td><html:text property="researcherBarcode"/></td>
    </tr>
    </table>
    <html:submit/>
    </center>
</html:form>
</body>
</html>
