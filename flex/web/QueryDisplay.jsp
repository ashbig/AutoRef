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

<h2><bean:message key="flex.name"/> : <bean:message key="flex.name"/> Sequence Search Result</h2>
<hr>
<html:errors/>

<logic:present name="output">
<TABLE border="1" cellpadding="2" cellspacing="0" width="100%">
    <tr class="headerRow">
        <logic:present name="id">
        <th>Sequence ID</th>
        </logic:present>

        <logic:present name="gi">
        <th>GI</th>
        </logic:present>

        <logic:present name="genename">
        <th>Gene Name</th>
        </logic:present>

        <logic:present name="genbank">
        <th>Genbank Accession</th>
        </logic:present>

        <logic:present name="genesymbol">
        <th>Gene Symbol</th>
        </logic:present>

        <logic:present name="pa">
        <th>PA Number</th>
        </logic:present>

        <logic:present name="sequence">
        <th>Sequence</th>
        </logic:present>

        <logic:present name="clonename">
        <th>Clone Name</th>
        </logic:present>

        <logic:present name="pubhit">
        <th>Public Hit</th>
        </logic:present>

        <logic:present name="cloneResult">
        <th>Clone Result</th>
        </logic:present>

        <logic:present name="flexstatus">
        <th>FLEX Status</th>
        </logic:present>

        <logic:present name="label">
        <th>Plate</th>
        </logic:present>

        <logic:present name="well">
        <th>Well</th>
        </logic:present>

        <logic:present name="isResultDisplay">
        <th>Result</th>
        </logic:present>

        <logic:present name="type">
        <th>Construct Type</th>
        </logic:present>

        <logic:present name="oligo">
        <th>5p Oligo</th>
        <th>3p Oligo</th>
        </logic:present>


        <logic:present name="project">
        <th>Project</th>
        </logic:present>


        <logic:present name="workflow">
        <th>Workflow</th>
        </logic:present>
    </TR>
    <logic:iterate id="info" name="output">
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <logic:present name="id">
        <td><a href="/FLEX/ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="info" property="id"/>" target="_blank"><flex:write name="info" property="id"/></td>
        </logic:present>

        <logic:present name="gi">
        <td><A target="_blank" HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="info" property="gi"/>&dopt=GenBank"> <flex:write name="info" property="gi"/></td>
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
            <logic:equal name="info" property="plateid" value="-1">
                <td>&nbsp;</td>
            </logic:equal>
            <logic:notEqual name="info" property="plateid" value="-1">
                <td><a href="/FLEX/ViewContainerDetails.do?CONTAINER_ID=<bean:write name="info" property="plateid"/>" target="_blank"><flex:write name="info" property="label"/></td>
            </logic:notEqual>
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
        <td><bean:write name="info" property="workflow"/></td>
        </logic:present>
    </flex:row>
    </logic:iterate>
</table>
</logic:present>

<logic:notPresent name="output">
<p>Your search returned no result.
</logic:notPresent>

</body>
</html:html>