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

<bean:define name="<%=Constants.SAMPLE_KEY%>" id="sample"/>
<bean:define id="sequence" name="sample" property="flexSequence"/>

<bean:define name="<%=Constants.PROCESS_KEY%>" id="process"/>

<logic:present name="<%=Constants.RESULT_KEY%>">
    <bean:define name="<%=Constants.RESULT_KEY%>" id="result"/> 
</logic:present>

<bean:define name="sample" property="container" id="container"/>

<html>
<head><title><bean:message key="flex.name"/> : Sample Details</title></head> 
<body>
<h2><bean:message key="flex.name"/> : Sample Details</h2>
<hr>
<html:errors/>
<p>Sample ID <bean:write name="sample" property="id"/></p>

<table>
<h3>Sample details</h3>
<tr>
    <th>Sequence</th>
    <td>
        <flex:linkFlexSequence sequenceName="sequence">
            <bean:write name="sequence" property="id"/>
        </flex:linkFlexSequence>

    </td>
    
    <th>Type</th>
    <td><bean:write name="sample" property="type"/></td>
    
    <th>Status</th>
    <td><bean:write name="sample" property="status"/></td>
    
    <th>Container</th>
    <td>
        <flex:linkContainer name="container" process="process">
            <bean:write name="container" property="label"/>
        </flex:linkContainer>

    </td>
    
    <logic:notEqual name="sample" property="constructid" value="-1">
        <th>Construct</th>
        <td><bean:write name="sample" property="constructid"/></td>
    </logic:notEqual>

    <logic:notEqual name="sample" property="oligoid" value="-1">
        <th>Oligo</th>
        <td><bean:write name="sample" property="oligoid"/></td>
    </logic:notEqual>

    <th>Well</th>
    <td><bean:write name="sample" property="position"/></td>
</tr>
</table>
 <h3>Result for Protocol 
        <bean:write name="process" property="protocol"/>
 </h3>
<logic:present name="result">
    <bean:write name="result" property="value"/>
    <%-- No go through and display the files --%>
    <table>
        <tr>
        <th>File Type</th>
        <th>File</th>
        </tr>
    <logic:iterate name="result" property="fileReferences" id="fileRef">
        <tr>
            <td><bean:write name="fileRef" property="fileType"/></td>
            <td>
                <html:link page='<%="/"+fileRef.toString()%>'>
                    <bean:write name="fileRef" property="baseName"/>
                </html:link>    
            </td>
        </tr>
    </logic:iterate>
    </table>
</logic:present>

<logic:notPresent name="result">
    No results for this protocol
</logic:notPresent>


</body>
</html>

