<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*" %>

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
    <title><bean:message key="flex.name"/> : Culture Result Details</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="flex.name"/> : Culture Result Details</h2>
<hr>
<html:errors/>
<p>

<logic:equal name="mode" value="<%=Constants.EDIT_MODE%>">
    <h3>Enter the culture plate results</h3>
</logic:equal>

<logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
    <h3>Comfirm the culture plate results</h3>
</logic:equal>

<%-- if the stats are present display them in a table--%>
<logic:present name="<%=Constants.RESULT_STATS_KEY%>">
    <table>
        <logic:iterate name="<%=Constants.RESULT_STATS_KEY%>" id="curStat">
        <tr>
            <td class="prompt">
                <bean:write name="curStat" property="key"/>
            </td>
            <td>
                <bean:write name="curStat" property="value"/>
            </td>
        </tr>
        </logic:iterate>
    </table>
</logic:present>
<br>
<table>
    <tr>
        <td>Plate ID:</td> <td><bean:write name="cultureEntryForm" property="container.id"/></td>
        <td>Plate Type:</td> <td><bean:write name="cultureEntryForm" property="container.type"/></td>
    </tr>

    <tr>
        <td>Process Date:</td> <td><bean:write name="<%=Constants.QUEUE_ITEM_KEY%>" property="date"/></td><td></td>
    </tr>
</table>


<!-- table to enter info about the plate-->

<html:form action="SaveCultureResult.do" enctype="multipart/form-data">

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

    <logic:iterate name="cultureEntryForm" property="container.samples" 
    id="curSample" indexId="i" type="edu.harvard.med.hip.flex.core.Sample">
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
                <%-- 
                        This hack should be replaces by a method to a sample
                        type object so the result values CV can be populated
                        from the db 
                --%>

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
                    } else if (curSample.getType().toUpperCase().indexOf("EMPTY") !=-1) {
                    %>
                        <html:option value=""></html:option>
                    <%
                    } else  {
                    %>
                    <html:option value="<%=Result.GROW%>"></html:option>
                    <html:option value="<%=Result.NOGROW%>"></html:option>
                    
                    <%
                    }
                    %>
                </html:select>
                <%}%>
             </logic:equal>
             
             <logic:equal name="mode" value="<%=Constants.READ_ONLY_MODE%>">
                <flex:write name="cultureEntryForm" property='<%="result["+ i +"]" %>'/>
             </logic:equal>

        </td>
        
    </flex:row>
    </logic:iterate>
  

</table>
<br>
<html:submit/>
</html:form>
</body>
</html>