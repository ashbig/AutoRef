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

<html>
<head>
    <title><bean:message key="flex.name"/> : Sample Result Details</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<h2><bean:message key="flex.name"/> : Sample Result Details</h2>
<hr>
<html:errors/>
<p>

<logic:equal name="<%=Constants.PROTOCOL_NAME_KEY%>" value="<%=Protocol.ENTER_CULTURE_FILE%>">
    <h3>Comfirm the culture plate results</h3>

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
</logic:equal>

<logic:equal name="<%=Constants.PROTOCOL_NAME_KEY%>" value="<%=Protocol.ENTER_DNA_RESULT%>">
    <h3>Comfirm the DNA plate results</h3>
</logic:equal>

<br>
<table>
    <tr>
        <td>Plate ID:</td> <td><bean:write name="uploadCultureResultForm" property="container.id"/></td>
        <td>Plate Label:</td> <td><bean:write name="uploadCultureResultForm" property="container.label"/></td>
    </tr>

    <tr>
        <td>Plate Type:</td> <td><bean:write name="uploadCultureResultForm" property="container.type"/></td>
        <td>Process Date:</td> <td><bean:write name="<%=Constants.QUEUE_ITEM_KEY%>" property="date"/></td><td></td>
    </tr>
</table>


<!-- table to enter info about the plate-->

<html:form action="ConfirmCultureResults.do" enctype="multipart/form-data">

<!-- hidden values needed for the insert-->

<table border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>Sample</th><th>Type</th><th>Cell</th><th>Result</th>
    </tr>

    <logic:iterate name="uploadCultureResultForm" property="container.samples" 
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
<!--
        <td>
            <logic:notEqual name="curSample" property="type" value="<%= Sample.EMPTY %>">
             <flex:write name="uploadCultureResultForm" property='<%="result["+ i +"]" %>'/>
             </logic:notEqual>
            <logic:equal name="curSample" property="type" value="<%= Sample.EMPTY %>">
             &nbsp;
             </logic:equal>
        </td>
 -->     
        <td>
             <flex:write name="uploadCultureResultForm" property='<%="result["+ i +"]" %>'/>
        </td>  
    </flex:row>
    </logic:iterate>
  
</table>
<br>
<html:submit/>
</html:form>
</body>
</html>