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
      
        <logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_VECTORS) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.vector.title"/>  
     </logic:equal>
     <logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES) %>">
        <bean:message key="flex.name"/> : <bean:message key="add.cloningstrategy.title"/> 
     </logic:equal>
     <logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) %>">
        <bean:message key="flex.name"/> : <bean:message key="add.name.title"/> 
     </logic:equal>
       <logic:equal  name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_LINKERS) %>">
            <bean:message key="flex.name"/> : <bean:message key="add.linker.title"/> 
        </logic:equal>
          <logic:equal  name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_PUT_PLATES_FOR_SEQUENCING) %>">
            <bean:message key="flex.name"/> : <bean:message key="put.plates.forsequencing.title"/> 
        </logic:equal>
</h2>

<hr>
<html:errors/>


<!--add name -->
 <logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) %>" >
      
<p><bean:message key="add.name.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= -ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE%>" >   
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
 
<logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_LINKERS ) %>">

<!--add linker -->
<p><bean:message key="add.linker.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= -ConstantsImport.PROCESS_IMPORT_LINKERS%>" >   

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
 <logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_VECTORS) %>">
      
<!--add vector -->
<p><bean:message key="add.vector.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= -ConstantsImport.PROCESS_IMPORT_VECTORS%>" >   
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

   <logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES) %>">
<!--add cloningstrategy -->
<p><bean:message key="add.cloningstrategy.help"/>
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= -ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES%>" >   
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

  <logic:equal name="forwardName"  value="<%= String.valueOf(ConstantsImport.PROCESS_PUT_PLATES_FOR_SEQUENCING) %>">
<!--add cloningstrategy -->
<p><html:form action="/AddItems.do" enctype="multipart/form-data"> 
<input type="hidden" name="forwardName"  value="<%= -ConstantsImport.PROCESS_PUT_PLATES_FOR_SEQUENCING%>" >   
<table>
    <tr>
        <td class="prompt"><bean:message key="put.plates.forsequencing.prompt"/></td>
        <td><textarea rows=10  name="plateLabels"></textarea></td>

    </tr>
    <tr><td  class="prompt">Please enter sequencing facility name</td><td><html:text  property="facilityName" size="50"/></td></tr>

</table>
    <div align="center"><html:submit/></div>
</html:form>
</logic:equal>

<p>

</body>
</html:html>