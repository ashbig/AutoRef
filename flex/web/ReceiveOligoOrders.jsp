<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
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
                <td bgcolor="lightgrey"><b>Please enter plate IDs for all of the oligo plates received:</b><p></td>
            </tr>
            <tr>
                <td><html:textarea property="oligoPlateIds" rows="12" cols="50"/><p></td>
            </tr>

            <tr>
                <td bgcolor="lightgrey"><b>Receive Date:&nbsp;&nbsp;&nbsp;</b>
                    <html:text property="receiveDate" size="30"/><p></td>
            </tr>

            <tr>
                <td bgcolor="lightgrey"><b>User Barcode:&nbsp;&nbsp;&nbsp;</b>
                    <html:text property="researcherBarcode" size="30"/><p></td>
            </tr>
        </table>
        <p>
        <input type=submit value="Submit"> <input type=reset value="Clear">
    </html:form>
</body>
</html>
