<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : rearray</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="Rearray.do"  enctype="multipart/form-data">
    <h2><bean:message key="flex.name"/> : Process Rearray</h2>
    <hr>
    <html:errors/>

<table>   
    <tr>
       <p> <td class="prompt">Please enter the rearray file:</td>    
        <td><html:file property="requestFile" /></td>
    </tr>
    <tr>
        <td class="prompt">Number of wells on plate:</td>    
        <td ><input type="text" name="wellsOnPlate" value = "96" align="right"/></td>
    </tr>

<tr>
    <td class="prompt">Put sequences on queue for processing?</td>
        <td >
            <input type="radio" name="isPutOnQueue"  value="true">Yes
            <input type="radio" name="isPutOnQueue" checked value="false">No
        </td>
</tr>
    <tr>

    <td class="prompt">Is full plate required?</td>
    <td>
        <html:radio property="isFullPlate" value="true"/>Yes
        <html:radio property="isFullPlate" value="false"/>No
    </td>
</tr>

<tr>
     <td class="prompt">Are controls required?</td>
     <td>
        <html:radio property="isControls" value="true"/>Yes
        <html:radio property="isControls" value="false"/>No
    </td>
</tr>


<tr>
     <td class="prompt">Is sort by saw-tooth patern?</td>
         <td>
        <html:radio property="isSortBySawToothpatern" value="true"/>Yes
        <html:radio property="isSortBySawToothpatern" value="false"/>No
    </td>
</tr>

   <tr>
    <td class="prompt">Please choose location for the new containers:</td>
    <td><html:select property="location">
        <html:options
        collection="locations"
        property="type"
        labelProperty="type"
        />
    </html:select>
    </td>
    </tr>

   <tr>
    <td class="prompt">Please choose the project:</td>
    <td><html:select property="projectid">
        <html:options
        collection="projects"
        property="id"
        labelProperty="name"
        />
    </html:select>
    </td>
    </tr>

   <tr>
    <td class="prompt">Please choose the workflow:</td>
    <td><html:select property="workflowid">
        <html:options
        collection="workflows"
        property="id"
        labelProperty="name"
        />
    </html:select>
    </td>
    </tr>

    </table>

        <b>Choose sequences that you want to group together:</b> 
        (sequences you don't choose will be grouped separately)
        <dl>
        <dd><html:checkbox property="small" />small genes (0 <= CDS < 2000)
        <dd><html:checkbox property="medium" />medium genes (2000 <= CDS < 4000)
        <dd><html:checkbox property="large" />large genes (CDS >= 4000)
        </dl>
    <br>

    <html:submit/>

</html:form>