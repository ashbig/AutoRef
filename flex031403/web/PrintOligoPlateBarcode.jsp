<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@page import="java.util.*" contentType="text/html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
   <title><bean:message key="flex.name"/> : Print Oligo Plate Barcode Form</title>
   <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="flex.name"/> : Print Oligo Plate Barcode Form</h2>
<hr>
<html:errors/>
<p>
<h3>Please click the button to print out plate barcodes:<p></h3>
    <% List ids = (List) request.getSession().getAttribute("plateList"); %>

    <html:form action="/PrintOligoPlateBarcode.do" focus="location">
    <center>    
        <table>
            <tr>
                <td>Plate Labels</td>
            </tr>
           <% ListIterator iter = ids.listIterator(); String id = null; %>
           <% while (iter.hasNext()) { id = (String) iter.next(); %>

            <tr>
                <td>
                    <%= id %>
                </td>
           </tr>

          <% } %>

        </table>
          <p>
          <input type=submit value="Print Barcode">
        </center>
                
    </html:form>
</body>
</html>