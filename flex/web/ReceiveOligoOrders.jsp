<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page contentType="text/html"%>
<html>
<head><title><bean:message key="flex.name"/> : Oligo Order Receiving Form</title></head>
<body>
<h2><bean:message key="flex.name"/> : Oligo Order Receiving Form</h2>
<hr>
<html:errors/>
<p>
    <html:form action="/ReceiveOligoPlates.do" focus="oligoPlateIds">
        
        <table>
            <tr>
                <td>Please enter plate IDs for all of the oligo plates received:<p></td>
            </tr>
            <tr>
                <td><html:textarea property="oligoPlateIds" rows="12" cols="50"/><p></td>
            </tr>

            <tr>
                <td>Receive Date:&nbsp;&nbsp;&nbsp;
                    <html:text property="receiveDate" size="30"/><p></td>
            </tr>

            <tr>
                <td>User Barcode:&nbsp;&nbsp;&nbsp;
                    <html:text property="researcherBarcode" size="30"/><p></td>
            </tr>
        </table>
        <p>
        <input type=submit value="Submit"> <input type=reset value="Clear">
    </html:form>
</body>
</html>
