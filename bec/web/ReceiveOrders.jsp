<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page contentType="text/html"%>
<html>
<head>
    <title><bean:message key="bec.name"/> : Order Receiving Form</title>
    <LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="bec.name"/> : Order Receiving Form</h2>
<hr>
<html:errors/>
<p>
    <html:form action="/ReceivePlates.do" focus="plateIds">
        
        <table>
            <tr>
                <td class="prompt">Please enter plate IDs for all of the plates received:<p></td>
            </tr>
            <tr>
                <td><html:textarea property="plateIds" rows="10" cols="35"/><p></td>
            </tr>

            <tr>
                <td class="prompt">Receive Date:&nbsp;&nbsp;&nbsp;
                    <html:text property="receiveDate" size="30"/><p></td>
            </tr>

            <tr>
                <td class="prompt"><bean:message key="bec.researcher.barcode.prompt"/>&nbsp;&nbsp;&nbsp;
                    <html:password property="researcherBarcode" size="30"/><p></td>
            </tr>
        </table>
        <p>
        <input type=submit value="Submit"> <input type=reset value="Clear">
    </html:form>

    <!-- <jsp:include page="QueueItemsDisplay.jsp" flush="true"/> -->
</body>
</html>
