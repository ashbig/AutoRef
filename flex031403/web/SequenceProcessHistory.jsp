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

<logic:present name="<%=Constants.SAMPLE_KEY%>">
    <bean:define id="querySample" name="<%=Constants.SAMPLE_KEY%>"/>
</logic:present>

<html>
<head>
    <title><bean:message key="flex.name"/> : Clone Process History</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
    <h2><bean:message key="flex.name"/> : Clone Process History</h2>
    <hr>
    <html:errors/>
    <p>
 <logic:present name="<%=Constants.FLEX_SEQUENCE_KEY%>"/>
        <bean:define name="<%=Constants.FLEX_SEQUENCE_KEY%>" id="sequence"/>    
        Process history shown for sequence
        <flex:linkFlexSequence sequenceName="sequence">
            <bean:write name="sequence" property="id"/>
        </flex:linkFlexSequence>
    </logic:present>

    </p>
    <br>
    <TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>Protocol</th>
        <th>Execution Date</th>
        <th>Subprotocol</th>
        <th>Researcher</th>
        <th>Container</th>
        <th>Well</th>
        <th>Sample</th>
        <th>Construct Type</th>
    </tr>
 
    <logic:iterate id="threadElem" name="<%=Constants.THREAD_KEY%>" property="elements">
        

        <bean:define id="process" name="threadElem" property="process"/>
        <bean:define id="protocol" name="process" property="protocol"/>
        <bean:define id="sample" name="threadElem" property="object"/>
        <bean:define id="seq" name="sample" property="flexSequence"/>
        <bean:define id="container" name="sample" property="container"/>
                                                                                     
        <flex:row property="object" match="querySample"  matchStyleClass="highlightRow" oddStyleClass="oddRow" evenStyleClass="evenRow">
            <td><bean:write name="protocol" property="processname"/></td>
            <td><bean:write name="process" property="date"/></td>
            <td><flex:write name="process" property="subprotocol"/></td>
            <td><bean:write name="process" property="researcher.name"/></td>
            <td><flex:linkContainer name="container" process="process">
                            <bean:write name="container" property="label"/>
                        </flex:linkContainer>
            </td>
            <td>
                    <bean:write name="sample" property="position"/>
            </td>
            <td>
                    <flex:linkSample name="sample" process="process">
                        <bean:write name="sample" property="id"/>
                    </flex:linkSample>
           </td>
           <td>
                <bean:write name="sample" property="construct.type"/>
           </td>
        </flex:row>
    </logic:iterate>
    </table>
</body>
</html>
