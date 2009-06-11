<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>

<html:html >
<head>
<title><bean:message key="flex.name"/> : Add Item</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
    <h2>
      
        <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS_INPUT.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.vector.title"/>  
     </logic:equal>
     <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES_INPUT.toString() %>">
        <bean:message key="flex.name"/> : <bean:message key="add.cloningstrategy.title"/> 
     </logic:equal>
     <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE_INPUT.toString() %>">
        <bean:message key="flex.name"/> : <bean:message key="add.name.title"/> 
     </logic:equal>
       <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS_INPUT.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.linker.title"/> 
        </logic:equal>
          <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_FOR_SEQUENCING_INPUT.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="put.plates.forsequencing.title"/> 
        </logic:equal>
          <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_IN_PIPELINE.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="put.plates.inpipeline.title"/> 
        </logic:equal>
           <logic:equal  name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.FLEX_TABLE_POPULATE_INPUT.toString() %>">
            <bean:message key="flex.name"/> : <bean:message key="add.flex.table.populate.title"/> 
        </logic:equal>
         
</h2>

<hr>
<html:errors/>


<!--add name -->
 <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE_INPUT.toString() %>" >
      
<p><bean:message key="add.name.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= ConstantsImport.PROCESS_NTYPE.IMPORT_INTO_NAMESTABLE%>" >   
<table>
    <tr>
        <td class="prompt"><bean:message key="add.name.prompt"/></td>
        <td><html:file property="inputFile" /></td>
        <td>[<a href="<bean:message key="add.name.sample.jsp"/>">sample file</a>]</td>
    </tr>
</table>
 <div align="center"><html:submit/></div>
</html:form>
 </logic:equal>
 
<logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS_INPUT.toString() %>">

<!--add linker -->
<p><bean:message key="add.linker.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_LINKERS%>" >   

<table>
    <tr>
        <td class="prompt"><bean:message key="add.linker.prompt"/></td>
        <td><html:file property="inputFile" /></td>
        <td>[<a href="<bean:message key="add.linker.sample.jsp"/>">sample file</a>]</td>
    </tr>
</table>
 <div align="center"><html:submit/></div>
</html:form>
 </logic:equal>
 <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS_INPUT.toString() %>">
      
<!--add vector -->
<p><bean:message key="add.vector.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= ConstantsImport.PROCESS_NTYPE.IMPORT_VECTORS%>" >   
<table>
<tr>
    <td class="prompt"><bean:message key="add.vector.prompt"/></td>
    <td><html:file property="inputFile" /></td>
    <td>[<a href="<bean:message key="add.vector.sample.jsp"/>">sample file</a>]</td>
</tr>
<tr>
    <td class="prompt"><bean:message key="add.vectorfeatures.prompt"/></td>
    <td><html:file property="inputFile1" /></td>
    <td>[<a href="<bean:message key="add.vectorfeatures.sample.jsp"/>">sample file</a>]</td>
</tr>
</table>
 <div align="center"><html:submit/></div>
</html:form>
 </logic:equal>

   <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES_INPUT.toString() %>">
<!--add cloningstrategy -->
<p><bean:message key="add.cloningstrategy.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= ConstantsImport.PROCESS_NTYPE.IMPORT_CLONING_STRATEGIES%>" >   
<table>
<tr>
    <td class="prompt"><bean:message key="add.cloningstrategy.prompt"/></td>
    <td><html:file property="inputFile" /></td>
    <td>[<a href="<bean:message key="add.cloningstrategy.sample.jsp"/>">sample file</a>]</td>
</tr>

</table>
    <div align="center"><html:submit/></div>
</html:form>
</logic:equal>

  <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_FOR_SEQUENCING_INPUT.toString() %>">
<!--add cloningstrategy -->
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= ConstantsImport.PROCESS_NTYPE.PUT_PLATES_FOR_SEQUENCING%>" >   
<table>
    <tr>
        <td class="prompt"><bean:message key="put.plates.forsequencing.prompt"/></td>
        <td><textarea rows=10  name="plateLabels"></textarea></td>

    </tr>
    <tr><td  class="prompt">Please enter sequencing facility name</td><td><html:text  property="facilityName" size="50"/><P></P><P></P></td></tr>

</table>
    <div align="center"><html:submit/></div>
</html:form>
</logic:equal>
 <logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.PUT_PLATES_IN_PIPELINE.toString() %>">


<!--add cloningstrategy -->
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<logic:present name="forwardName">  <input type="hidden" name="forwardName"  value="<%= ConstantsImport.PROCESS_NTYPE.PUT_PLATES_IN_PIPELINE%>" > </logic:present>
<logic:present name="projectid">    <input type="hidden" name="projectid" value="<bean:write name="projectid"/>"></logic:present>
<logic:present name="workflowid">    <input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>"></logic:present>
<logic:present name="processid">    <input type="hidden" name="processid" value="<bean:write name="processid"/>"></logic:present>

<table>
     <tr>    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>    </tr>
    <tr>    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>    </tr>

    <tr>    <td class="prompt">Process name:</td>
    <td>  <bean:write name="processname" /></td>  <tr>
        <td class="prompt"><bean:message key="put.plates.forsequencing.prompt"/></td>
        <td><textarea rows=10  name="plateLabels"></textarea><P></P><P></P></td>

    </tr>
   
</table>
    <div align="center"><html:submit/></div>
</html:form>
</logic:equal>

 
<!--add third party data -->
<logic:equal name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.FLEX_TABLE_POPULATE_INPUT.toString() %>">
<p><bean:message key="add.flex.table.populate.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%=  ConstantsImport.PROCESS_NTYPE.FLEX_TABLE_POPULATE_INPUT.getNextProcess().toString()%>" >   

<table>
    <tr>
        <td class="prompt"><bean:message key="add.flex.table.populate.prompt"/></td>
        <td><html:file property="inputFile" /></td>
        <td>[<a href="<bean:message key="add.flex.table.populate.sample.jsp"/>">sample file</a>]</td>
    </tr>
</table>
 <div align="center"><html:submit/></div>
</html:form>
 </logic:equal>
<p>

</body>
</html:html>