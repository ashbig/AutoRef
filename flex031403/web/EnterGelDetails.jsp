<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*"%>
<%@ page import="edu.harvard.med.hip.flex.process.*"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>


<%-- Get the mode of this form, edit or ready only --%>
<logic:present parameter="<%=Constants.FORM_MODE_KEY%>">
    <bean:parameter id="mode" name="<%=Constants.FORM_MODE_KEY%>" />
</logic:present>

<%-- by default, this page is in edit mode --%>
<logic:notPresent parameter="<%=Constants.FORM_MODE_KEY%>">
    <bean:define id="mode" value="<%=Constants.EDIT_MODE%>" />
</logic:notPresent>



<html>
<head>
    <title><bean:message key="flex.name"/> : Gel Result Details</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="flex.name"/> : Gel Result Details</h2>
<hr>
<html:errors/>
<p>
<logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
    <h3>Enter the results of this gel</h3>
</logic:equal>

<logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
    <h3>Comfirm the gel result</h3>
</logic:equal>

<%-- if the stats are present display them in a table--%>
<logic:present name="<%=Constants.RESULT_STATS_KEY%>">
    <table>
        <logic:iterate name="<%=Constants.RESULT_STATS_KEY%>" id="curStat">
        <tr>
            <td class="label">
                <bean:write name="curStat" property="key"/>
            </td>
            <td>
                <bean:write name="curStat" property="value"/>
            </td>
        </tr>
        </logic:iterate>
    </table>
<br>
</logic:present>
<table border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td class="label">Plate ID:</td> <td><bean:write name="gelEntryForm" property="container.id"/></td>
        <td class="label">Plate Type:</td> <td><bean:write name="gelEntryForm" property="container.type"/></td>
    </tr>
    <tr>
        <td class="label">Process Date:</td> <td><bean:write name="<%=Constants.QUEUE_ITEM_KEY%>" property="date"/></td><td></td>
    </tr>
</table>


<!-- table to enter info about the plate-->

<html:form action="SaveGelResult.do" enctype="multipart/form-data">

<!-- hidden values needed for the insert-->
<html:hidden property="processDate"/>
<html:hidden property="researcherBarcode"/>
<html:hidden property="protocolString"/>

<logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
    <html:hidden property="editable" value="false"/>
</logic:equal>

<logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
    <html:hidden property="editable" value="true"/>
</logic:equal>

<table border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>Sample</th><th>Type</th><th>Cell</th><th>Result</th>
    </tr>
    
    <logic:iterate name="gelEntryForm" property="container.samples" id="curSample" 
    indexId="i" type="edu.harvard.med.hip.flex.core.Sample">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td>
            <bean:write name="curSample" property="id"/>
        </td>
        <td>
            <bean:write name="curSample" property="type"/>
        </td>
        <td>
            <bean:write name="curSample" property="position"/>
        </td>
      
        <td>
            <logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
                <%
                if(curSample.getType().toUpperCase().indexOf("EMPTY") !=-1) {
                %>
                    &nbsp;
                <%
                } else {
                %>
                <html:select property='<%="result["+ i +"]" %>'>
                    <%
                    if(curSample.getType().toUpperCase().indexOf("CONTROL")  !=-1) {
                    %>
                        <html:option value="<%=Result.SUCCEEDED%>">Succeeded</html:option>
                        <html:option value="<%=Result.FAILED%>">Failed</html:option>
                    <%
                    } else  {
                    %>
                    <html:option value="<%=Result.CORRECT%>">Correct</html:option>
                    <html:option value="<%=Result.INCORRECT%>">Incorrect</html:option>
                    <html:option value="<%=Result.MUL_W_CORRECT%>">Multiple with correct</html:option>
                    <html:option value="<%=Result.MUL_WO_CORRECT%>">Multiple without correct</html:option>
                    <html:option value="<%=Result.NO_PRODUCT%>">No product</html:option>
                    <%
                    }
                    %>
                </html:select>
                <%}%>
             </logic:equal>
             
             <logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
                <flex:write name="gelEntryForm" property='<%="result["+ i +"]" %>'/>
             </logic:equal>

        </td>
        
    </flex:row>
    </logic:iterate>
    <tr>
        <logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
            <td class="prompt">Please select the gel image you would like to upload:</td>
            <td colspan="3"><html:file property="formFile" /></td>
        </logic:equal>
        <logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
            <td class="prompt">Gel image file:</td>
            <td colspan="3"><bean:write name="gelEntryForm" property="formFile.fileName"/></td>
        </logic:equal>
    </tr>

</table>
<html:submit/>
</html:form>
</body>
</html>
