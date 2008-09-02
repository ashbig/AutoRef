<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.workflow.*" %>

<html:html >
<head>
<title><bean:message key="flex.name"/> : Add Workflow Items</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
    
      
 
<%
  ConstantsImport.PROCESS_NTYPE cur_process = 
    ConstantsImport.PROCESS_NTYPE.valueOf( (String)request.getAttribute("forwardName"));%>
    <h2>  <bean:message key="flex.name"/> :      <%=cur_process.getTitle()%> </h2>

<hr>
<html:errors/>


<!--add name -->
 
 <p><html:form action="/AddWorkflowItems.do" enctype="multipart/form-data"> 
<h3>            <%= cur_process.getTitle()%>    </h3>
<logic:present name="forwardName">  <input type="hidden" name="forwardName"  value="<%= cur_process.getNextProcess()%>" > </logic:present>

 <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_FROM_TEMPLATE_INPUT.toString() %>">
<table>
    <tr>    <td class="prompt">Enter new workflow name:</td>
    <td><html:text property="workflowname" size="50" maxlength="200" /></td>    </tr>

    <tr>    <td class="prompt">Select wokflow type:</td>
    <td> <select name="workflowtype">
         <option value="<%= Workflow.WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION.toString() %>" >
            <%= Workflow.WORKFLOW_TYPE.TRANSFER_TO_EXPRESSION.getTitle()%></option>
        </select>
    </td>  <tr>
        
    <td class="prompt">Select template workflow</td>
    <td><html:select property="workflowid">
        <html:options
        collection="workflowtemplates"
        property="id"
        labelProperty="name"
        /></html:select></td></tr>
    
    <td class="prompt">Select vector transfer clones to</td>
    <td><html:select property="vectorid">
        <html:options
        collection="vectors"
        property="vectorid"
        labelProperty="name"
        /></html:select></td></tr>
     
</table><P><P>
<div align="center"><html:submit value="Confirm"/></div>
 </logic:equal>


 <!--////////-->
<logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_FROM_TEMPLATE_CONFIRM.toString() %>">
<html:hidden property="workflowname" />
<html:hidden property="workflowtype" />
<html:hidden property="workflowid" />
<html:hidden property="vectorid" />
<logic:present name="forwardName">  <input type="hidden" name="forwardName"  value="<%= cur_process.getNextProcess()%>" > </logic:present>


<table>
    <tr>    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td></tr>
    <tr>    <td class="prompt">Wokflow type:</td>
    <td><bean:write name="workflowtype" /></td></tr>
    <tr><td class="prompt">Template workflow:</td>
    <td><bean:write name="workflowtemplatename" />
    <input type=BUTTON value="Display Workflow" onCLick="window.open('AddWorkflowItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.DISPLAY_WORKFLOW.toString()%>&amp;workflowid=<bean:write name="workflowid" />','DisplayWorkflow','width=700,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
	</td></tr>
    <tr><td class="prompt">Vector transfer clones to:</td>
    <td><bean:write name="vectorname" /></td></tr>
     
</table><P><P>
<div align="center"><html:submit value="Submit"/></div>
 </logic:equal>
 
    <!--////////-->
<logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.ADD_WORKFLOW_TO_PROJECT_INPUT.toString() %>">
<logic:present name="forwardName">  <input type="hidden" name="forwardName"  value="<%= cur_process.getNextProcess()%>" > </logic:present>
<table>
    
    <tr><td class="prompt">Select project:</td>
    <td><html:select property="projectid">
        <html:options collection="projects"        property="id"        labelProperty="name"        />
        </html:select></td></tr>
  
   <tr><td class="prompt">Select workflow to add:</td>
    <td><html:select property="workflowid">
        <html:options collection="workflowtemplates"        property="id"        labelProperty="name"        />
        </html:select></td></tr>
    
    <tr><td class="prompt">Enter plate label code:</td>
    <td> <html:text property="projectworkflowcode" size="6" maxlength="6" />
    
</td></tr>
<tr><td colspan="2" ><P><P><b><i>Note:</i></b><br></td></tr>
<tr><td>
Suggested project bound plate code: 
</td><td><select size="3" name="code">
<% Vector pp = (Vector)request.getAttribute("projects");
Project p =null;
for (int t = 0; t < pp.size(); t++)
{ p = (Project) pp.get(t); %>
<option   > <%= p.getName() %>:&nbsp;&nbsp; <b><%= p.getCode() %></b></option>
<%}%>

</select>
       </td></tr>
 
 
<tr><td colspan="2">
<br>
All glycerol plates created in "Transform clones in expression ..." workflows will be named
according to the pattern YYY (three letter species code)XG ;<br>
For workflows that deal with expression clones please submit label code as YYY (three letter species code).

</td></tr>
     
</table><P><P>
<div align="center"><html:submit value="Confirm"/></div>


</logic:equal>


 <!--////////-->
<logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.ADD_WORKFLOW_TO_PROJECT_CONFIRM.toString() %>">
<html:hidden property="workflowid" />
<html:hidden property="projectid" />
<html:hidden property="projectworkflowcode" />
<logic:present name="forwardName">  <input type="hidden" name="forwardName"  value="<%= cur_process.getNextProcess()%>" > </logic:present>

<table>
    <tr>    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td></tr>
    <tr>    <td class="prompt">Wokflow name</td>
    <td><bean:write name="workflowname" /> <input type=BUTTON value="Display Workflow" onCLick="window.open('AddWorkflowItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.DISPLAY_WORKFLOW.toString()%>&amp;workflowid=<bean:write name="workflowid" />','DisplayWorkflow','width=700,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
	</td></tr>
    <tr><td class="prompt">Plate label code:</td>
    <td><bean:write name="projectworkflowcode" />
   </td></tr>
</table><P><P>
<div align="center"><html:submit value="Submit"/></div>
 
</logic:equal>
 <!--////////-->





</html:form>
</body>
</html:html>