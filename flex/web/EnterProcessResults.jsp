<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head><title>Enter Process Results</title></head>
<body>
    
<html:form action="EnterPlate.do" focus="plateBarcode">
    <center>
    <table>
    <html:errors/>
    <tr>
        <td colspan="2">
            <h3>Enter Process Results</h3>
        </td>
    </tr>
    <tr>
        <td>Plate Label:</td>
        <td><html:text property="plateBarcode"/></td>
    </tr>
    </table>
    <html:submit/>
    </center>
</html:form>
</body>
</html>
