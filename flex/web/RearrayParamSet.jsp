<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : rearray</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="RearrayParamSet.do" enctype="multipart/form-data">
    <h2><bean:message key="flex.name"/> : Rearray</h2>
    <hr>
    <html:errors/>

<html:hidden property="fileFormat"/>
<html:hidden property="plateFormat"/>
<html:hidden property="wellFormat"/>
<html:hidden property="destWellFormat"/>
<html:hidden property="project"/>
<html:hidden property="workflow"/>

<p><b><u>Parameter setting:</u></b></p>
<dl><dd>
    <table>
        <tr>
            <td class="prompt">New plate type for rearrayed plates:</td>
            <td><select name="plateType">
                <option value="96 WELL PLATE"/>96 Well Plate
                </select>
            </td>
        </tr>
        <tr>
            <td class="prompt">New sample type for rearrayed plates:</td>
            <td><select name="sampleType">
                <option value="dna"/>DNA
                <option value="glycerol"/>Glycerol
                </select>
            </td>
        </tr>
        <tr>
            <td class="prompt">Location for the new containers:</td>
            <td><html:select property="location">
                <html:options collection="Rearray.locations" property="type"/>
                </html:select>
            </td>
        </tr>
        
        <logic:equal name="fileFormat" value="format1">
        <tr>
            <td class="prompt">Sort by:</td>
            <td><select name="sortBy">
                <option value="1"/>None
                <option value="2"/>Number of samples on source plate
                <option value="3"/>Saw-Tooth pattern
                </select>
            </td>
        </tr>
        </logic:equal>
    </table> 
</dl>

<p>
<dl>
    <logic:equal name="fileFormat" value="format1">
    <dd><b><html:checkbox property="isArrangeBySize">Group the following sequences separately:</html:checkbox></b>
    <dl>
        <dd><html:checkbox property="isSmall">small genes</html:checkbox>
        <dd><html:checkbox property="isMedium">medium genes</html:checkbox>
        <dd><html:checkbox property="isLarge">large genes</html:checkbox>
        <dd>
        <dd>CDS length to separate small and medium genes: <html:text property="lower"/>
        <dd>CDS length to separate medium and large genes: <html:text property="upper"/>
    </dl>
    <dd><b><html:checkbox property="isArrangeByFormat">Group by construct type (FUSION, CLOSE, etc.)</b></html:checkbox>
    <dd><b><html:checkbox property="isControl">Require controls (positive control on first well, negative control on last well)</b></html:checkbox>
    <dd><b><html:checkbox property="isFullPlate">Require full plate</b></html:checkbox>
    </logic:equal>
    
    <dd><b><html:checkbox property="isOligo">Generate rearrayed oligo plate</b></html:checkbox>
    <dl>
        <dd><html:radio property="isNewOligo" value="false"/>from existing oligo plates
        <dd><html:radio property="isNewOligo" value="true"/>from new oligos
        <dd>Choose oligo format: <select name="oligoFormat">
                                    <option value="both"/>Both
                                    <option value="fusion"/>Fusion
                                    <option value="close"/>Closed
                                 </select>
    </dl>
</dl>
<p><b><u>Output file:   </u></b>
    <select name="output">
        <option value="morefile" selected/>One file for each plate
        <option value="onefile"/>One file for all rearrayed plates
    </select>
<p><b><u>Researcher barcode:   <html:text property="researcherBarcode"/></u></b>
<p><b><u>Upload file:   <html:file property="inputFile"/></u></b>

<p>
<html:submit/>

</html:form>
</body>
</html>