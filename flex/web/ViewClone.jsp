<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Clone Info</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Clone Info</h2>
<hr>
<html:errors/>
<p>
<TABLE WIDTH=100% ALIGN=CENTER>
    <TR>
        <TD class="label">Clone ID:</td>
        <td><bean:write name="clone" property="cloneid"/></TD>
        <TD class="label">FLEX Clone ID:</td>
        <td><bean:write name="clone" property="clonename"/></TD>
    </TR>
    <TR>
        <TD class="label">Clone Type:</td>
        <td><bean:write name="clone" property="clonetype"/></TD>
        <TD class="label">Master Clone ID:</td>
        <td><bean:write name="clone" property="mastercloneid"/></TD>
    </TR>
    <TR>
        <TD class="label">Clone Status:</td>
        <td><bean:write name="clone" property="status"/></TD>
        <TD class="label">Reference FLEX Sequence ID:</td>
        <td><A target="_blank" HREF="ViewSequence.do?FLEX_SEQUENCE_ID=<bean:write name="clone" property="refsequenceid"/>">
            <bean:write name="clone" property="refsequenceid"/>
            </A>
        </TD>
    </TR>
    <TR>
        <TD class="label">Species:</td>
        <td><bean:write name="clone" property="species"/></TD>
        <TD class="label">Version:</td>
        <td><bean:write name="clone" property="constructtype"/></TD>
    </TR>
    <TR>
        <TD class="label">Cloning Strategy:</td>
        <TD>
            <bean:write name="clone" property="cloningstrategy.name"/>
        </TD>
        <TD class="label">Vector:</td>
        <TD>
            <a target="_blank" href="ViewVector.do?vectorname=<bean:write name="clone" property="cloningstrategy.clonevector.name"/>">
                <bean:write name="clone" property="cloningstrategy.clonevector.name"/>
            </a>
        </TD>
    </TR>
    <TR>
        <TD class="label">5' Linker Sequence:</td>
        <TD colspan="2">
            <bean:write name="clone" property="cloningstrategy.linker5p.sequence"/>
        </TD>
    </tr>
    <tr>
        <TD class="label">3' Linker Sequence:</td>
        <TD colspan="2">
            <bean:write name="clone" property="cloningstrategy.linker3p.sequence"/>
        </TD>
    </TR>
    <TR>
        <TD class="label">Result Against Expected Sequence:</td>
        <td><bean:write name="clone" property="resultexpect"/></TD>
        <TD class="label">Match Expected Sequence:</td>
        <td><bean:write name="clone" property="matchexpect"/></TD>
    </TR>
    <TR>
        <TD class="label">Genbank Hit:</td>
        <td><bean:write name="clone" property="pubhit"/></TD>
        <TD class="label">Result Against Genbank Sequence:</td>
        <td><bean:write name="clone" property="resultpubhit"/></TD>
    </TR>
    <TR>
        <TD class="label">Match Genbank Sequence:</td>
        <td><bean:write name="clone" property="matchpubhit"/></TD>
    </TR>
</TABLE>

<p>
<b>Available Clones:</b>
    <bean:define id="allstorages" name="clone" property="storages"/>

    <table width="80%" align="center"><tr><td>
        <table width="100%" align="center">
            <tr>
                <th>Plate</th>
                <th>Well</th>
                <th>Type</th>
                <th>Form</th>
            </tr>
            <logic:iterate name="allstorages" id="storage">
            <tr>
                <td><a target="_blank" href="ViewContainerDetails.do?CONTAINER_ID=<bean:write name="storage" property="containerid"/>">
                        <bean:write name="storage" property="label"/>
                    </a>
                </td>
                <td><a target="_blank" href="ViewSampleDetails.do?SAMPLE_ID=<bean:write name="storage" property="sampleid"/>">
                        <bean:write name="storage" property="position"/>
                    </a>
                </td>
                <td><bean:write name="storage" property="storageType"/></td>
                <td><bean:write name="storage" property="storageForm"/></td>
            </tr>
            </logic:iterate>
        </table>
    </td></tr></table>

<p>
<b>Sequence:</b>
<p>
<bean:write name="clone" property="fastaSequence"/>

</body>
</html>