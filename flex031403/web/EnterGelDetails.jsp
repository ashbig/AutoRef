<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*"%>
<%@ page import="edu.harvard.med.hip.flex.process.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%-- Get the mode of this form, edit or ready only --%>
<logic:present parameter="<%=Constants.FORM_MODE_KEY%>">
    <bean:parameter id="mode" name="<%=Constants.FORM_MODE_KEY%>" />
</logic:present>

<%-- by default, this page is in edit mode --%>
<logic:notPresent parameter="<%=Constants.FORM_MODE_KEY%>">
    <bean:define id="mode" value="<%=Constants.EDIT_MODE%>" />
</logic:notPresent>



<html>
<head><title><bean:message key="flex.name"/> : Gel Result Details</title></head>
<body>
<h2><bean:message key="flex.name"/> : Gel Result Details</h2>
<hr>
<center><html:errors/></center>
<logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
    <h3>Enter The results of this gel</h3>
</logic:equal>

<logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
    <h3>Comfirm the gel result</h3>
</logic:equal>


<table>
    <tr>
        <td>Plate ID:</td> <td><bean:write name="gelEntryForm" property="container.id"/></td>
        <td>Plate Type:</td> <td><bean:write name="gelEntryForm" property="container.type"/></td>
    </tr>
    <tr>
        <td>Process Date:</td> <td><bean:write name="<%=Constants.QUEUE_ITEM_KEY%>" property="date"/></td><td></td>
    </tr>
</table>


<!-- table to enter info about the plate-->

<html:form action="SaveGelResult.do" enctype="multipart/form-data">

<logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
    <html:hidden property="editable" value="false"/>
</logic:equal>

<logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
    <html:hidden property="editable" value="true"/>
</logic:equal>

<table border="1">
    <tr>
        <th>Sample</th><th>Type</th><th>Cell</th><!--<th>Status</th>--><th>Result</th>
    </tr>
    
    <logic:iterate name="gelEntryForm" property="container.samples" id="curSample" indexId="i">
    <tr>
        <td>
            <bean:write name="curSample" property="id"/>
        </td>
        <td>
            <bean:write name="curSample" property="type"/>
        </td>
        <td>
            <bean:write name="curSample" property="position"/>
        </td>
       <%-- <td>
            <logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
                <html:select property='<%="status["+i+"]" %>'>
                    <html:option value="<%=Sample.GOOD%>">Good</html:option>
                    <html:option value="<%=Sample.BAD%>">Bad</html:option>
                </html:select>
            </logic:equal>
            
            <logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
                <bean:write name="gelEntryForm" property='<%="status["+i+"]" %>'/>
            </logic:equal>

        </td>
--%>
        <td>
            <logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
                <html:select property='<%="result["+ i +"]" %>'>
                    <html:option value="<%=Result.CORRECT%>">Correct</html:option>
                    <html:option value="<%=Result.INCORRECT%>">Incorrect</html:option>
                    <html:option value="<%=Result.MUL_W_CORRECT%>">Multiple with correct</html:option>
                    <html:option value="<%=Result.MUL_WO_CORRECT%>">Multiple without correct</html:option>
                    <html:option value="<%=Result.NO_PRODUCT%>">No product</html:option>
                    <html:option value="<%=Result.FAILED%>">Failed</html:option>
                    <html:option value="<%=Result.SUCCEEDED%>">Succeeded</html:option>
                </html:select>
             </logic:equal>
             
             <logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
                <bean:write name="gelEntryForm" property='<%="result["+ i +"]" %>'/>
             </logic:equal>

        </td>
        
    </tr>
    </logic:iterate>
    <tr>
        <logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
            <td>Please select the Gel Image you would like to upload:</td>
            <td><html:file property="gelImage" /></td>
        </logic:equal>
        <logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
            <td>Gel Image file:</td>
            <td><bean:write name="gelEntryForm" property="gelImage.fileName"/></td>
        </logic:equal>
    </tr>

</table>
<html:submit/>
</html:form>
</body>
</html>
