<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page contentType="text/html"%>
<html>
<head><title>FLEXGene: Oligo Order Receiving Form</title></head>
<body>
<h2>FLEXGene: Oligo Order Receiving Form</h2>

<%-- <jsp:useBean id="beanInstanceName" scope="session" class="package.class" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
<hr>
<html:errors/>

    <form:form action="/ReceiveOligoOrders.do" focus="">
        
        <table>
            <tr>
                <td bgcolor="lightgrey"><b>Please enter plate IDs for all of the oligo plates received:</b></td>
            </tr>
            <tr>
                <td><form:textarea property="oligoPlateIds" size="80"/></td>
            </tr>

            <tr>
                <td bgcolor="lightgrey"><b>Receive Date:</b></td>
                <td><form:text property="receiveDate" size="40"/></td>
            </tr>

            <tr>
                <td><b>Who:</b></td>
                <td><html:text property="who" size="40"/></td>
            </tr>
        </table>
        <p>
        <center><input type=submit value="Submit"> <input type=reset value="Clear"></center>
    </form:form>
</body>
</html>
