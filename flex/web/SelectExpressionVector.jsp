<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Expression Plate </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Expression Plate</h2>
<hr>
<html:errors/>
<p>
<logic:present name="plateBarcode" scope="request">
<ul>
<li><red>Plate <bean:write name="plateBarcode"/> has been sucessfully created and results have been entered.</red>
</ul>
</logic:present>
<p>
<html:form action="/CreateExpressionPlate.do" focus="researcherBarcode">
<html:hidden property="sourcePlate"/>

<table>
    <tr>
    <td class="prompt">Master plate barcode:</td>
    <td><bean:write name="sourcePlate"/></td>
    </tr>
    <tr>
    <td class="prompt">Choose expression vector:</td>
    <td><html:select property="vectorname">
        <html:options
        collection="vectors"
        property="nameAndId"
        labelProperty="name"
        />
        </html:select>
    </td>
    </tr>

    <tr>
    <td class="prompt">Enter the researcher barcode:</td>
    <td><html:password property="researcherBarcode" size="30"/></td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Create Expresson Plate"/></td>
    </tr>
</table>
</html:form>

<p>
<hr>
<p>
<b>Vector Information:<b>
<TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>Name</th>
        <th>Source</th>
        <th>Type</th>
        <th>HIP Name</th>
        <th>Description</th>
        <th>Restriction</th>
    </tr>
<logic:iterate id="vector" name="vectors" >
    <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
        <td>
           <a target="_blank" href="ViewVector.do?vectorname=<bean:write name="vector" property="name"/>">
                <flex:write name="vector" property="name"/>
            </a>
        </td>
        <td>
            <flex:write name="vector" property="source"/>
        </td>
        <td>
            <flex:write name="vector" property="type"/>
        </td>
        <td>
            <flex:write name="vector" property="hipname"/>
        </td>
        <td>
            <flex:write name="vector" property="description"/>
        </td>
        <td>
            <flex:write name="vector" property="restriction"/>
        </td>
    </flex:row>
</logic:iterate>
</table>

</body>
</html>

