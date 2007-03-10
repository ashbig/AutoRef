<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="java.util.*" contentType="text/html"%>
<html>
<head>
    <title><bean:message key="flex.name"/> : Enter Oligo Plate Location Form</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="flex.name"/> : Enter Plate Location Form</h2>
<hr>
<html:errors/>
<p>
<h3>Please specify the location for each  plate received:<p></h3>

    <% List ids = (List) request.getSession().getAttribute("plateList"); %>

    <html:form action="/EnterOligoPlateLocation.do" focus="locations">
        
        <table>
            <tr>
                <td>Plate Labels</td>
                <td>Location</td>
            </tr>
        <% ListIterator iter = ids.listIterator(); String id = null; %>
        <% while (iter.hasNext()) { id = (String) iter.next(); %>

            <tr>
                <td>
                    <%= id %>
                </td>
                <td>
                    <select name="locations">
                    <option value="FREEZER">FREEZER</option>
                    <option value="REFRIGERATOR">REFRIGERATOR<option>
                    <option value="WORKBENCH">WORKBENCH<option>
                    <option value="UNAVAILABLE">UNAVAILABLE<option>
                    </select>
                </td>
           </tr>

<% } %>

        </table>
        <p>
        <input type=submit value="Confirm and Print Barcode">
    </html:form>
</body>
</html>
