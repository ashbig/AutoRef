<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.Vector"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> :  Create Process Plate</h2>
<hr>
<html:errors/>
<p>
<html:form action="/MgcGetLocation.do">

<% if (request.getAttribute("projectname") != null)
{ %>
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
<input type="hidden" name="workflowname" value="<bean:write name="workflowname"/>">
<input type="hidden" name="processname" value="<bean:write name="processname"/>">



<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td>&nbsp;<bean:write name="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td>&nbsp;<bean:write name="workflowname" /></td>
    </tr>
    <tr>
    <td class="prompt">Process name:</td>
    <td>&nbsp;<bean:write name="processname" /></td>
    </tr>
</table>


<%}%>
<P>
<P>

<% Vector r = (Vector)request.getSession().getAttribute("EnterSourcePlateAction.newContainers");
   
   if (r.size() == 0 )
{ %>

No plates have been created.

<%} else {%>


<table  cellpadding=0 cellspacing=2 border=1>
     
     <tr class="headerRow">
        <TH>&nbsp;&nbsp;FLEX Id&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Plate Label&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Location&nbsp;&nbsp;</TH>
     </TR>

    <logic:iterate  id="curContainer" name="EnterSourcePlateAction.newContainers"> 
         <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
               <TD align="right" >
                     &nbsp;<bean:write name="curContainer" property="id"/>&nbsp;
               </TD>
                <TD align="center">
                     &nbsp;<bean:write name="curContainer" property="label"/>&nbsp;
               </TD>
    		  <td>&nbsp;<html:select name="curContainer" property="location">
			    <html:options
				collection="locations"
				property="id"
				labelProperty="type"
			    />
			    </html:select>&nbsp;</td>
		   </tr>
          </flex:row>
    </logic:iterate>    
</table>
 <P><P>
 
    <html:submit property="submit" value="Continue"/>
 
<%}%>
</html:form>

</body>
</html>
