
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

<logic:iterate name="menulist" id="menuItem" type="edu.harvard.med.hip.flex.user.MenuItem">
<tr>
    <td>
        <html:link forward='<%=menuItem.getMenuItem()%>' target="display"> 
            <bean:write name="menuItem" property="description"/>
        </html:link>
     </td>
</tr>
            
        </logic:iterate>
            <tr>
                <td>
                <html:link forward='logout' target="_top"> 
                   Logout
                </html:link>
                </td>
            </tr>
        
</table>
</div>
</center>

</body>
</html>
