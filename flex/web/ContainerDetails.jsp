<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.Process" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>


<logic:present name="<%=Constants.PROCESS_KEY%>">
    <bean:define id="process"name="<%=Constants.PROCESS_KEY%>"/>
</logic:present>

<html>
<head>
    <title><bean:message key="flex.name"/> : Container Details</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
<h2><bean:message key="flex.name"/> : Container Details</h2>
<hr>
<html:errors/>
<p>
<logic:present name="process">
    <h3>Results desplayed are for protocol 
    <bean:write name="process" property="protocol.processname"/>
    </h3>
</logic:present>



<%--Loop through all the containers and display all their details--%>
<logic:iterate id="container" name="<%=Constants.CONTAINER_LIST_KEY%>">
<!--display general info about the container.-->
<TABLE border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td class="label">Id:</td>
        <td><bean:write name="container" property="id"/></td>
    </tr>
    <tr>
        <td class="label">Plate Set Id:</td>
        <td><bean:write name="container" property="platesetid"/></td>
    </tr>
    <tr>
        <td class="label">Type:</td>
        <td><bean:write name="container" property="type"/></td>
    </tr>
    <tr>
        <td class="label">Location:</td>
        <td><bean:write name="container" property="location.type"/></td>
    </tr>
    <tr>
        <td class="label">Label:</td>
        <td><bean:write name="container" property="label"/></td>
    </tr>
</table>
<br>
<!-- display the container sample info.-->
<TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>ID</th> 
        <th>Type</th>
        <th>Position</th>
        <th>Status</th>
        <th>Construct</th>
        <th>Oligo</th>
        <logic:present name="process">
            <th>Result</th>
        </logic:present>
    </tr>
    <logic:iterate id="sample" name="container" property="samples">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td>
            <logic:present name="process">
                <flex:linkSample name="sample" process="process">
                    <bean:write name="sample" property="id"/>
                </flex:linkSample>
            </logic:present>
            
            <logic:notPresent name="process">
                <flex:linkSample name="sample">
                    <bean:write name="sample" property="id"/>
                </flex:linkSample>
            </logic:notPresent>
        </td>
        <td><bean:write name="sample" property="type"/></td>
        <td><bean:write name="sample" property="position"/></td>
        <td><bean:write name="sample" property="status"/></td>
        <logic:equal name="sample" property="constructid" value="-1">
            <td>&nbsp;</td>
        </logic:equal>
        <logic:notEqual name="sample" property="constructid" value="-1">
            <td><bean:write name="sample" property="constructid"/></td>
        </logic:notEqual>
        <logic:equal name="sample" property="oligoid" value="-1">
            <td>&nbsp;</td>
        </logic:equal>

        <logic:notEqual name="sample" property="oligoid" value="-1">
            <td><bean:write name="sample" property="oligoid"/></td>
        </logic:notEqual>
        
        
            
        <td>
          <logic:present name="process">
            <flex:findResult processName="process" sampleName="sample" id="result"/>
            <logic:present name="result">
                <flex:write name="result"/>
            </logic:present>
            <logic:notPresent name="result">
                &nbsp;
            </logic:notPresent>
          </logic:present>
          
          <logic:notPresent name="process">
            &nbsp;
          </logic:notPresent>
            </td>
        
    </flex:row>
    </logic:iterate>
</table>
<br>
<CENTER><h3>Files</h3></CENTER>
<br>
<%-- Display all files associated with this container --%>
<TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="rowHeader">
        <th>Name</th><th>Type</th>
    </tr>
<logic:iterate id="file" name="container" property="fileReferences">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td>
            <flex:linkFileRef name="file">
                <bean:write name="file" property="baseName"/>
            </flex:linkFileRef>
        </td>
        <td><bean:write name="file" property="fileType"/></td>
    </flex:row>
</logic:iterate>
</table>
</logic:iterate>
</body>
</html>