
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
<div class="nav">
<table class="nav" border=0 cellpadding=1 cellspacing=0 width=100%>
<tr>
    <TD>
        <center><h3><bean:message key="flex.name"/></h3></center>
    
        <TABLE>
        <logic:iterate name="menulist" id="menuItem" type="edu.harvard.med.hip.flex.user.MenuItem">
        
                <html:link forward='<%=menuItem.getMenuItem()%>' target="display"> 
                   <b> <bean:write name="menuItem" property="description"/></b><p>
                </html:link>
                <html:link forward='logout' target="_top"> 
                   <b>Logout</b><p>
                </html:link>   
        
        </logic:iterate>
        </TABLE>
    </td>
    <td>
    </td>
</table>
</div>
</center>

</body>
</html>
