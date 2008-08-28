<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>

 <%@ page import="edu.harvard.med.hip.flex.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="java.util.*" %>
 
<%@ page import="edu.harvard.med.hip.flex.workflow.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>

        <title>Add items conformation</title>
    </head>
    <body>
  <h2>
      <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.vector.title"/>  
     </logic:equal>
     <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES.toString() %>">
        <bean:message key="flex.name"/> : <bean:message key="add.cloningstrategy.title"/> 
     </logic:equal>
     <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE.toString() %>">
        <bean:message key="flex.name"/> : <bean:message key="add.name.title"/> 
     </logic:equal>
       <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.linker.title"/> 
        </logic:equal>
          <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_FOR_SEQUENCING.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="put.plates.forsequencing.title"/> 
        </logic:equal>
            <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.plates.from.thirdparty"/> 
        </logic:equal>
        <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_IN_PIPELINE.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="put.plates.inpipeline.title"/> 
        </logic:equal> 
          <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_FROM_TEMPLATE.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.workflow.from.template.title"/> 
        </logic:equal> 


 <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.RUN_REPORT.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="run.report.title"/>  
     </logic:equal>
     <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.ADD_WORKFLOW_TO_PROJECT.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.workflow.to.project.title"/>  
     </logic:equal>
  </h2>
    <hr>
    <html:errors/>
  
<hr>

  <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/>  
     </logic:equal>
     <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES.toString() %>">
        <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
     </logic:equal>
     <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE.toString() %>">
        <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
     </logic:equal>
       <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
        </logic:equal>
          <logic:equal  name="forwardName"  value="<%= ConstantsImport.PROCESS_NTYPE.IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.items.notificaton"/> 
        </logic:equal>
     
          <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_FOR_SEQUENCING.toString() %>">
            <bean:message key="flex.name"/> : 
            <bean:message key="put.plates.forsequencing.notification"/> 
                <%= request.getAttribute( "container_labels") %>
        </logic:equal>
 
     <logic:equal name="forwardName"  value="<%= ConstantsImport.PROCESS_NTYPE.RUN_REPORT.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.report.notificaton"/>  
     </logic:equal>
       <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_IN_PIPELINE.toString() %>">
            <bean:message key="flex.name"/> : 
            <bean:message key="put.plates.inpipeline.notification"/> 
                <%= request.getAttribute( "container_labels") %>
        </logic:equal>
    
  <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_FROM_TEMPLATE.toString() %>">
              <jsp:include page="DisplayWorkflow.jsp" />
       </logic:equal> 
  <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.ADD_WORKFLOW_TO_PROJECT.toString() %>">
              
                  <h3> New workflow was added to project:</h3>
  <table>   <tr>    <td class="prompt">Project name:</td> <td><bean:write name="projectname" /></td></tr>
    <tr>    <td class="prompt">Wokflow name</td> <td><bean:write name="workflowname" />
       <input type=BUTTON value="Display Workflow" onCLick="window.open('AddWorkflowItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.DISPLAY_WORKFLOW.toString()%>&amp;workflowid=<bean:write name="workflowid" />','Display Workflow','width=700,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;" > 
</td></tr>
    <tr><td class="prompt">Label code:</td><td><bean:write name="projectworkflowcode" /></td></tr></table>
    
 </logic:equal> 
    </body>
</html>
