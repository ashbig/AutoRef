<%@ page contentType="text/html"%>
<%@ page language="java" %>


<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.Process" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<bean:define name="<%=Constants.CONTAINER_KEY%>" id="container"/>
<logic:present name="<%=Constants.PROCESS_KEY%>">
    <bean:define id="process"name="<%=Constants.PROCESS_KEY%>"/>
</logic:present>

<html>
<head>
    <title><bean:message key="flex.name"/> : Mgc Container Details</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
<h2><bean:message key="flex.name"/> : Mgc Container Details</h2>
<hr>
<html:errors/>
<p>
<logic:present name="process">
    <h3>Results desplayed are for protocol 
    <bean:write name="process" property="protocol.processname"/>
    </h3>
</logic:present>


<%--Loop through all the containers and display all their details--%>

<!--display general info about the container.-->
<TABLE border="0" cellpadding="2" cellspacing="2">
    <tr>
        <td class="label">Id:</td>
        <td><bean:write name="container" property="id"/></td>
    </tr>
    <tr>
        <td class="label">Label:</td>
        <td><bean:write name="container" property="label"/></td>
    </tr>
    
    <tr>
        <td class="label">Location:</td>
        <td><bean:write name="container" property="location.type"/></td>
    </tr>
    <tr>
        <td class="label">Type:</td>
        <td><bean:write name="container" property="type"/></td>
    </tr>
    <tr>
        <td class="label">MGC Original Plate:</td>
        <td><bean:write name="container" property="originalContainer"/></td>
    </tr>
    <tr>
        <td class="label">MGC distribution file:</td>
        <td><bean:write name="container" property="fileName"/></td>
    </tr>
    <tr>
        <td>
            <html:form action="/PrintLabel.do">
                <html:hidden property="label" value="<%=((edu.harvard.med.hip.flex.core.Container)container).getLabel()%>"/>
                <html:submit value="Reprint Label"/>
            </html:form>
        </td>
      
    </tr>
</table>
<!-- display the container sample info.-->
<TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>ID</th> 
         <th>MGC Id</th>
        <th>Image Id</th>
        <th>Vector</th>
        <th>Type</th>
        <th>Position</th>
        <th>Status</th>
         <th>Sequence Id</th>
        <logic:present name="process">
            <th>Result</th>
        </logic:present>
    </tr>
    <logic:iterate id="sample" name="container" property="samples">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td><bean:write name="sample" property="id"/>        </td>
        <td><bean:write name="sample" property="mgcId"/></td>
        <td><bean:write name="sample" property="imageId"/></td>
        <td><bean:write name="sample" property="vector"/></td>    
        <td><bean:write name="sample" property="type"/></td>
        <td><bean:write name="sample" property="position"/></td>
        <td><bean:write name="sample" property="status"/></td>
          
        <td>
        <logic:present name="sample" property="sequenceId">
           <a href="/FLEX/ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="sample" property="sequenceId"/>   ">
                <bean:write name="sample" property="sequenceId"/>
           </a>
        </logic:present>
        <logic:notPresent name="sample" property="sequenceId">
            N/A
        </logic:notPresent>
        </td>   
    </flex:row>
    </logic:iterate>
</table>

</body>
</html>