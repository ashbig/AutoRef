<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><%System.out.println("LLL");%>
<title><bean:message key="flex.name"/> : Create Empty Plates </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Empty Plates</h2>
<hr>
<html:errors/>

<html:form action="/norgenrearray/GenerateEmptyContainers.do" focus="numberOfPlates">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="workflowname" value="<%= request.getAttribute("workflowname")%>" >
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
<input type="hidden" name="protocolid" value="<bean:write name="protocolid"/>">
<input type="hidden" name="protocolname" value="<bean:write name="protocolname"/>">
 
<table>
 <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname"/></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><%= request.getAttribute("workflowname")%></td>
    </tr>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="protocolname"/></td>
    </tr>
   <tr>
    <td class="prompt">Enter number of empty container<br> you would like to create*:</td>
    <td><html:text property="numberOfPlates" size="10"/></td>
    </tr>
 
    
<tr> <td class="prompt">Select plates location:</td>
    <td><html:select property="sourceLocation"   >
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select>
    </td>    </tr>
    <tr><td class="prompt"> <bean:message key="flex.researcher.barcode.prompt"/>
    </td><td><html:password property="researcherBarcode" size="40"/>
</td></tr>
     <tr>
        <br><br><td colspan="2"><i><b>Note:</b></i> FLEX will create new containers only if provided number is bigger than number of existing containers.</td>
    </tr>
   
    <tr> <td colspan=2 align="center"><html:submit property="submit" value="Submit"/></td>
    </tr>
</table>
</html:form>
<%System.out.println("LLL1");%>
<jsp:include page="../QueueItemsDisplay.jsp" flush="true" />

</body>
</html>

