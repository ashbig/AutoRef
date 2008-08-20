<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Name Types</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2>
<bean:message key="flex.name"/> :
<logic:present name="TABLE_NAME">
    <logic:equal name="TABLE_NAME"  value="<%= edu.harvard.med.hip.flex.core.Nametype.TABLE_NAME_NAMETYPE.SAMPLE_NAMETYPE.toString() %>" ><bean:message key="title.nametype.sample"/></logic:equal>
    <logic:equal name="TABLE_NAME"  value="<%= Nametype.TABLE_NAME_NAMETYPE.NAMETYPE.toString() %>" ><bean:message key="title.nametype.flexsequence"/></logic:equal>
    <logic:equal name="TABLE_NAME"  value="<%= Nametype.TABLE_NAME_NAMETYPE.CONTAINERHEADER_NAMETYPE.toString() %>" ><bean:message key="title.nametype.container"/></logic:equal>
    <logic:equal name="TABLE_NAME"  value="<%= Nametype.TABLE_NAME_NAMETYPE.SPECIES.toString() %>" ><bean:message key="title.nametype.species"/></logic:equal>
    <logic:equal name="TABLE_NAME"  value="<%= Nametype.TABLE_NAME_NAMETYPE.FLEXSTATUS.toString() %>" ><bean:message key="title.nametype.flexstatus"/></logic:equal>
    <logic:equal name="TABLE_NAME"  value="<%= Nametype.TABLE_NAME_NAMETYPE.SAMPLETYPE.toString() %>" ><bean:message key="title.nametype.sampletype"/></logic:equal>
    <logic:equal name="TABLE_NAME"  value="<%= Nametype.TABLE_NAME_NAMETYPE.CONTAINERTYPE.toString() %>" ><bean:message key="title.nametype.containertype"/></logic:equal>
  
</logic:present>    
<logic:notPresent name="TABLE_NAME">  Name Types  </logic:notPresent>    
     


</h2>
<hr>
<html:errors/>

<p>
<table width="70%" align="center">
<logic:iterate id="nametype" name="nametypes">
<tr><td><bean:write name="nametype" property="name"/></td>
<logic:empty name="nametype" property="description">  <td>&nbsp;</td> </logic:empty>
<logic:notEmpty name="nametype" property="description"> <td> <flex:write name="nametype" property="description"/>   </td>    </logic:notEmpty>       
            

</tr>
</logic:iterate>
</table>

</body>
</html:html>
