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

<bean:define name="<%=Constants.SAMPLE_KEY%>" id="sample"/>

<logic:present name="sample" property="flexSequence">
    <bean:define id="sequence" name="sample" property="flexSequence"/>
</logic:present>

<logic:present name="<%=Constants.PROCESS_KEY%>">
    <bean:define name="<%=Constants.PROCESS_KEY%>" id="process"/>   
</logic:present>

<logic:present name="<%=Constants.RESULT_KEY%>">
    <bean:define name="<%=Constants.RESULT_KEY%>" id="result"/> 
</logic:present>

<bean:define name="sample" property="container" id="container"/>

<html>
<head>
    <title><bean:message key="flex.name"/> : Sample Details</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head> 
<body>
<h2><bean:message key="flex.name"/> : Sample Details</h2>
<hr>
<html:errors/>
<p>


<TABLE border="1" cellpadding="2" cellspacing="0">
<tr class="headerRow">
    <th>Sample Id</th>
    <th>Sequence</th>
    <th>Type</th>
    <th>Status</th>
    <th>Container</th>
     <logic:notEqual name="sample" property="constructid" value="-1">
        <th>Construct</th>
    </logic:notEqual>
    <logic:notEqual name="sample" property="oligoid" value="-1">
        <th>Oligo</th>
    </logic:notEqual>
    <th>Well</th>
    </tr>
    <tr class="evenRow">
    <td>
        <bean:write name="sample" property="id"/>
    </td>
    <td>
        <logic:present name="sample" property="flexSequence">
            <flex:linkFlexSequence sequenceName="sequence">
                <bean:write name="sequence" property="id"/>
            </flex:linkFlexSequence>
        </logic:present>
        <logic:notPresent name="sample" property="flexSequence">
            N/A
        </logic:notPresent>

    </td>
    
    
    <td><bean:write name="sample" property="type"/></td>
    
    
    <td><bean:write name="sample" property="status"/></td>
    
    
    <td>
        <logic:present name="process">
            <flex:linkContainer name="container" process="process"> 
                <bean:write name="container" property="label"/>
            </flex:linkContainer>
        </logic:present>

        <logic:notPresent name="process">
            <flex:linkContainer name="container">
                <bean:write name="container" property="label"/>
            </flex:linkContainer>
        </logic:notPresent>
    
    </td>
    
    <logic:notEqual name="sample" property="constructid" value="-1">
        <td><bean:write name="sample" property="constructid"/></td>
    </logic:notEqual>

    <logic:notEqual name="sample" property="oligoid" value="-1">
        <td><bean:write name="sample" property="oligoid"/></td>
    </logic:notEqual>

    <td><bean:write name="sample" property="position"/></td>
</tr>
</table>

<logic:present name="process">
    <h3>Result for Protocol 
        <bean:write name="process" property="protocol"/>
    </h3>

<p>
<logic:present name="result">
    
    <bean:write name="result" property="value"/>
    <%-- No go through and display the files --%>
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow">
            <th>File Type</th>
            <th>File</th>
        </tr>
    <logic:iterate name="result" property="fileReferences" id="fileRef">
        <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
            <td><bean:write name="fileRef" property="fileType"/></td>
            <td>
                <flex:linkFileRef name="fileRef">
                    <bean:write name="fileRef" property="baseName"/>
                </flex:linkFileRef>
            </td>
        </flex:row>
    </logic:iterate>
    </table>
</logic:present>

<logic:notPresent name="result">
    No results for this protocol
</logic:notPresent>

</logic:present>

</body>
</html>

