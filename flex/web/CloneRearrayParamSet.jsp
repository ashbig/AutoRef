<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : rearray</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="CloneRearrayParamSet.do" enctype="multipart/form-data">
    <h2><bean:message key="flex.name"/> : Rearray Clones</h2>
    <hr>
    <html:errors/>

<html:hidden property="fileFormat"/>
<html:hidden property="destWellFormat"/>
<html:hidden property="project"/>
<html:hidden property="workflow"/>
<logic:present name="dist">
    <input type="hidden" name="dist" value="<bean:write name="dist"/>">
</logic:present>
<input type="hidden" name="workflowName" value="<bean:write name="workflowName"/>">
<input type="hidden" name="projectName" value="<bean:write name="projectName"/>">
<input type="hidden" name="workflowName" value="<bean:write name="workflowName"/>">
<logic:present name="rearrayOption">
    <input type="hidden" name="rearrayOption" value="<bean:write name="rearrayOption"/>">
</logic:present>
<html:hidden property="rearrayType"/>

<p><table>
    <tr>
        <td class="prompt">Project:</td>
        <td><bean:write name="projectName"/></td>
    </tr>
    <tr>
        <td class="prompt">Workflow:</td>
        <td><bean:write name="workflowName"/></td>
    </tr>
</table>

<p><b><u>Parameter setting:</u></b></p>
<dl><dd>
    <table>
        <tr>
            <td class="prompt">Select the source plate format:</td>
            <logic:equal name="dist" value="yes">
            <td><select name="sourceFormat">
                <option value="workingGlycerol"/>Working Glycerol
                <option value="workingDna"/>Working DNA
                </select>
            </td>
            </logic:equal>
            <logic:equal name="dist" value="no">
            <td><select name="sourceFormat">
                <option value="glycerol"/>Original Production Glycerol Stock
                <option value="dna"/>Original Production DNA
                <option value="workingGlycerol"/>Working Glycerol
                <option value="workingDna"/>Working DNA
                </select>
            </td>
            </logic:equal>
        </tr>
        <tr>
            <td class="prompt">New plate type for rearrayed plates:</td>
            <td><select name="plateType">
                <option value="96 WELL PLATE"/>96 Well Plate
                </select>
            </td>
        </tr>
    </table>
    
    <dd>
    <logic:equal name="fileFormat" value="format1">
    <p>
    <table bgcolor=e5f6ff><tr><td>
    <p><u><b>Rearray Options</b></u></p>
            <b>Create destination plates according to: </b>
            <select name="sortBy">
                <option value="1"/>Input file order
                <option value="2"/>Source plate with most samples first
                <option value="3"/>Saw-Tooth pattern
            </select>

        <p>
    <b><html:checkbox property="isArrangeBySize">Group the following sequences separately:</html:checkbox></b>
    <dl>
        <dd><html:checkbox property="isSmall">group 1</html:checkbox>
        <dd><html:checkbox property="isMedium">group 2</html:checkbox>
        <dd><html:checkbox property="isLarge">group 3</html:checkbox>
        <dd>
        <dd>CDS length to separate group 1 and group 2: <html:text property="lower"/>
        <dd>CDS length to separate group 2 and group 3: <html:text property="upper"/>
    </dl>
    <p><b><html:checkbox property="isArrangeByFormat">Separate by construct type (FUSION, CLOSE, etc.)</html:checkbox></b>
    <p><b><html:checkbox property="isControl">Leave empty wells for controls (positive control on first well, negative control on last well)</html:checkbox></b>
    <p><b><html:checkbox property="isFullPlate">Do not rearray partial plates (default will be partial plates)</html:checkbox></b>
    <p><b><html:checkbox property="isSourceDup">Allow duplicate samples in source plate</html:checkbox></b>
    </td></tr></table>
    </logic:equal>
</dl>
<p><b><u>Output files:   </u></b>
    <dl><dd>
    <table>
        <tr>
            <td class="prompt">Number of output files:</td>
            <td><select name="output">
                <option value="morefile" selected/>One file for each plate
                <option value="onefile"/>One file for all rearrayed plates
            </select>
            </td>
        </tr>
        <tr>
            <td class="prompt">Output file to be emailed to: </td>
            <td><input type=text size="50" name="userEmail" value="<bean:write name="userEmail"/>"></td>
        </tr>
    </table>
    </dl>

<p><b><u>Researcher ID:   <html:text property="researcherBarcode"/></u></b>
<p><b><u>Upload file:   <html:file property="inputFile" size="50"/></u></b>

<p>
<html:submit/>

</html:form>
</body>
</html>