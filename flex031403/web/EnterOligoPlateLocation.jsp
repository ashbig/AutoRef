<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="java.util.*" contentType="text/html"%>
<html>
<head><title><bean:message key="flex.name"/> : Enter Oligo Plate Location Form</title></head>
<body>
<h2><bean:message key="flex.name"/> : Enter Oligo Plate Location Form</h2>
<hr>
<html:errors/>
<p>
<h3>Please specify the location for each oligo plate received:<p></h3>

    <% List ids = (List) request.getSession().getAttribute("plateList"); %>

    <html:form action="/EnterOligoPlateLocation.do" focus="locations">
        
        <table>
            <tr>
                <td bgcolor="lightgrey"><b>Plate Labels</b></td>
                <td bgcolor="lightgrey"><b>Location</b></td>
            </tr>
        <% ListIterator iter = ids.listIterator(); String id = null; %>
        <% while (iter.hasNext()) { id = (String) iter.next(); %>

            <tr>
                <td>
                    <b> <%= id %></b>
                </td>
                <td>
                    <html:select property="locations">
                    <html:option value="FREEZER">FREEZER</html:option>
                    <html:option value="REFRIGERATOR">REFRIGERATOR</html:option>
                    <html:option value="WORKBENCH">WORKBENCH</html:option>
                    <html:option value="UNAVAILABLE">UNAVAILABLE</html:option>
                    </html:select>
                </td>
           </tr>

<% } %>

        </table>
        <p>
        <input type=submit value="Confirm and Print Barcode">
    </html:form>
</body>
</html>
