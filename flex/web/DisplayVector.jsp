<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Vector Information</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Vector Information</h2>
<hr>
<html:errors/>
<p>

    <table width="100%" align="center">
        <tr>
        <td class="prompt">Vector Name:</td>
        <td><bean:write name="vector" property="name"/></td>
        </tr>
        <tr>
        <td class="prompt">Vector Type:</td>
        <td><bean:write name="vector" property="type"/></td>
        </tr>
        <tr>
        <td class="prompt">Vector Source:</td>
        <td><bean:write name="vector" property="source"/></td>
        </tr>
        <tr>
        <td class="prompt">HIP Name:</td>
        <td><bean:write name="vector" property="hipname"/></td>
        </tr>
        <tr>
        <td class="prompt">Restriction:</td>
        <td><bean:write name="vector" property="restriction"/></td>
        </tr>
        <tr>
        <td class="prompt">Description:</td>
        <td><bean:write name="vector" property="description"/></td>
        </tr>
    </table>

<p>
    <bean:define id="vectorfeatures" name="vector" property="features"/>

    <table width="80%" align="center"><tr><td>
    <b><u>Vector Features:</u></b>
    <p>
    <table>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Status</th>
        </tr>
        <logic:iterate name="vectorfeatures" id="feature">
            <tr>
                <td><bean:write name="feature" property="name"/></td>
                <td><bean:write name="feature" property="description"/></td>
                <td><bean:write name="feature" property="status"/></td>
            </tr>
        </logic:iterate>
    </table>
    </td></tr></table>

    <p>
    <b>Vector Map:</b> &nbsp;&nbsp;&nbsp;
           <a href="http://kotel.med.harvard.edu/FLEXRepository/<bean:write name="vector" property="pathFile"/>">
                <bean:write name="vector" property="file"/>
            </a>
</body>
</html>
