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
    <h3>Results displayed are for protocol 
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
        <th>&nbsp;&nbsp;ID&nbsp;&nbsp;</th> 
         <th>&nbsp;&nbsp;MGC Id&nbsp;&nbsp;</th>
        <th>&nbsp;&nbsp;IMAGE Id&nbsp;&nbsp;</th>
        <th>&nbsp;&nbsp;Vector&nbsp;&nbsp;</th>
        <th>&nbsp;&nbsp;Type&nbsp;&nbsp;</th>
        <th>&nbsp;&nbsp;Position&nbsp;&nbsp;</th>
        <th>&nbsp;&nbsp;Status&nbsp;&nbsp;</th>
         <th>&nbsp;&nbsp;Sequence Id&nbsp;&nbsp;</th>
        <logic:present name="process">
            <th>Result</th>
        </logic:present>
    </tr>
    <logic:iterate id="sample" name="container" property="samples">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td>&nbsp;<bean:write name="sample" property="id"/>&nbsp;        </td>
        <td align="right">&nbsp;<bean:write name="sample" property="mgcId"/>&nbsp;</td>
        <td align="right">&nbsp;<bean:write name="sample" property="imageId"/>&nbsp;</td>
        <td>&nbsp;<bean:write name="sample" property="vector"/>&nbsp;</td>    
        <td>&nbsp;<bean:write name="sample" property="type"/>&nbsp;</td>
        <td align="right"><bean:write name="sample" property="position"/></td>
        <td>&nbsp;<bean:write name="sample" property="status"/>&nbsp;</td>
          
        <td align="right">
        <logic:present name="sample" property="sequenceId">
            <logic:equal  name="sample" property="sequenceId" value="0" >
                N/A
            </logic:equal>

            <logic:notEqual name="sample" property="sequenceId" value="0" > 
               <a href="/FLEX/ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="sample" property="sequenceId"/>   ">
                    &nbsp;<bean:write name="sample" property="sequenceId"/>&nbsp;
               </a>
            </logic:notEqual> 
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