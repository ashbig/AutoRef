<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Sequence Search Result</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<html:errors/>

<logic:present name="output">
<TABLE border="0" width="100%">
    <tr>
        <logic:present name="id">
        <td>Sequence ID</td>
        </logic:present>

        <logic:present name="gi">
        <td>GI</td>
        </logic:present>

        <logic:present name="genename">
        <td>Gene Name</td>
        </logic:present>

        <logic:present name="genbank">
        <td>Genbank Accession</td>
        </logic:present>

        <logic:present name="genesymbol">
        <td>Gene Symbol</td>
        </logic:present>

        <logic:present name="pa">
        <td>PA Number</td>
        </logic:present>

        <logic:present name="sequence">
        <td>Sequence</td>
        </logic:present>

        <logic:present name="clonename">
        <td>Clone Name</td>
        </logic:present>

        <logic:present name="pubhit">
        <td>Public Hit</td>
        </logic:present>

        <logic:present name="cloneResult">
        <td>Clone Result</td>
        </logic:present>

        <logic:present name="flexstatus">
        <td>FLEX Status</td>
        </logic:present>

        <logic:present name="label">
        <td>Plate</td>
        </logic:present>

        <logic:present name="well">
        <td>Well</td>
        </logic:present>

        <logic:present name="isResultDisplay">
        <td>Result</td>
        </logic:present>

        <logic:present name="type">
        <td>Constduct Type</td>
        </logic:present>

        <logic:present name="oligo">
        <td>5p Oligo</td>
        <td>3p Oligo</td>
        </logic:present>


        <logic:present name="project">
        <td>Project</td>
        </logic:present>


        <logic:present name="workflow">
        <td>Workflow</td>
        </logic:present>
    </TR>
    <logic:iterate id="info" name="output">
    <tr>
        <logic:present name="id">
        <td><flex:write name="info" property="id"/></td>
        </logic:present>

        <logic:present name="gi">
        <td><flex:write name="info" property="gi"/></td>
        </logic:present>

        <logic:present name="genename">
        <td><flex:write name="info" property="geneName"/></td>
        </logic:present>

        <logic:present name="genbank">
        <td><flex:write name="info" property="genbankAcc"/></td>
        </logic:present>

        <logic:present name="genesymbol">
        <td><flex:write name="info" property="geneSymbol"/></td>
        </logic:present>

        <logic:present name="pa">
        <td><flex:write name="info" property="panumber"/></td>
        </logic:present>

        <logic:present name="sequence">
        <td><flex:write name="info" property="sequence"/></td>
        </logic:present>

        <logic:present name="clonename">
        <td><flex:write name="info" property="clonename"/></td>
        </logic:present>

        <logic:present name="pubhit">
        <td><flex:write name="info" property="pubhit"/></td>
        </logic:present>

        <logic:present name="cloneResult">
        <td><flex:write name="info" property="result"/></td>
        </logic:present>

        <logic:present name="flexstatus">
        <td><flex:write name="info" property="status"/></td>
        </logic:present>

        <logic:present name="label">
        <td><flex:write name="info" property="label"/></td>
        </logic:present>

        <logic:present name="well">
            <logic:equal name="info" property="well" value="-1">
                <td>&nbsp;</td>
            </logic:equal>
            <logic:notEqual name="info" property="well" value="-1">
                <td><flex:write name="info" property="well"/></td>
            </logic:notEqual>
        </logic:present>

        <logic:present name="isResultDisplay">
        <td><flex:write name="info" property="result"/></td>
        </logic:present>

        <logic:present name="type">
        <td><flex:write name="info" property="type"/></td>
        </logic:present>

        <logic:present name="oligo">
        <td><flex:write name="info" property="fivep"/></td>
        <td><flex:write name="info" property="threep"/></td>
        </logic:present>


        <logic:present name="project">
        <td><flex:write name="info" property="project"/></td>
        </logic:present>


        <logic:present name="workflow">
        <td><flex:write name="info" property="workflow"/></td>
        </logic:present>
    </tr>
    </logic:iterate>
</table>
</logic:present>

<logic:notPresent name="output">
<p>Your search returned no result.
</logic:notPresent>

</body>
</html:html>