
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body bgcolor="#9bbad6">
<center>
<div>
<table>
<tr>
    <TD>
        <center><h3><bean:message key="flex.name"/></h3></center>
    </td>
</tr>
<tr>
    <td><hr></td>
</tr>
<logic:iterate name="menulist" id="menuItem" type="edu.harvard.med.hip.flex.user.MenuItem">
<tr>
    <td class="label">
        <small>
        <html:link forward='<%=menuItem.getMenuItem()%>' target="display"> 
            <bean:write name="menuItem" property="description"/>
        </html:link>
        </small>
    </td>
</tr>
<tr>
    <td>&nbsp</td>
</tr>           
        </logic:iterate>

            <tr>
                <td class="label">
                <small>
                <html:link forward='logout' target="_top"> 
                   Logout
                </html:link>
                </small>
                </td>
            </tr> 
<tr>
    <td><hr></td>
</tr>
<tr>
    <td>&nbsp</td>
</tr> 
<tr>
    <td>
        <small>
        <address><a href="mailto:flexgene_support@hms.harvard.edu">FLEXGene Support</a></address>
        </small>
     </td>
</tr>
<tr>
    <td>&nbsp</td>
</tr>

</table>

<small>** This system and the underlying database was built in conjunction with
<a href="http://www.3rdmill.com" target="_blank">3rd Millennium Inc.</a> **</small>
<p><a href="http://www.3rdmill.com" target="_blank">
    <img height=40 
        src="3rdhoriz.gif" 
        width=150 border=0>
    </a>

</div>
</center>
</body>
</html>
