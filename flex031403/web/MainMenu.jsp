
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<body bgcolor="beige">
<center>
<table border=0 cellpadding=2 cellspacing=0 width=90%>
<tr>
<TD width=1></td>
<td>
<h3>FLEXGene</h3>
    

<logic:iterate name="menulist" id="menuItem" type="edu.harvard.med.hip.flex.user.MenuItem">
<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=0 bgcolor="beige" width="100%">
<TR>
<TD colspan=2></TD>
</TR>
<tr>
<td align=center>&nbsp;</TD> 
<td align=left>
    <html:link forward='<%=menuItem.getMenuItem()%>' target="display">
            <bean:write name="menuItem" property="description"/>
     </html:link>
</td>
</tr>
<TR>
<TD colspan=2></TD>
</TR>
<TR>
<TD colspan=2 align=center>
</TD>
</TR>    

</logic:iterate>
</table>
</center>
<struts:errors/>
</body>
</html>
