<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Full Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Process full plates</h2>
<hr>
<html:errors/>
<p>
<html:form action="MgcOrderOligo.do">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="processname" value="<bean:write name="processname"/>">

<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="processname" /></td>
    </tr>
    <tr>
    <td class="prompt">Number of clones:</td>
    <td><bean:write name="sequences_count" /></td>
    </tr>
<tr>
    <td class="prompt">Number of full plates:</td>
    <td><bean:write name="full_plates" /></td>
    </tr>
</table>
<% 
ArrayList plates = (ArrayList)request.getAttribute("platesInfo") ;
if (plates.size() > 0)
{      
 
 %>
<h3>Plates Information:</h3>

<table  cellpadding=0 cellspacing=2 border=1>
    <tr class="headerRow">
        <TH>Plate Number</TH>
        <TH>Sequences</TH>
        <TH>Marker</TH>
    </TR>
<% 
int plate_number = 1;
for (int plate_count = 0 ; plate_count < plates.size();      )
{
%>
<tr>
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
            <TD> <%= plate_number %>  </TD>
            <TD> <%= plates.get(plate_count)     %> </TD>
            <TD> <%= plates.get(plate_count + 1) %>    </TD>
          </flex:row>
          </tr>
  
<% 
plate_count += 2;
 plate_number++;
}
%>
</TABLE>

<table>
 <td class="prompt">Is full plate required?</td>
    <td>
         
         <html:radio property="isFullPlate" value="false"/>Yes
       <html:radio property="isFullPlate" value="true"/>No
    </td>
</tr> 
</table>


<p>
    <html:submit property="submit" value="Submit"/>
   
<%}%>
</html:form>

</body>
</html>