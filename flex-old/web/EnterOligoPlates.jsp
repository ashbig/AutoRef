<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>FLEXGene : Create Process Plate </title>
</head>
<body bgcolor="white">

<h2>FlexGene : Create Process Plate</h2>
<hr>
<html:errors/>
<html:form action="/EnterOligoPlates.do" focus="fivepPlate">
<table>
    <tr>
    <td><b>Protocol:</b></td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td><b>Enter 5P oligo plate barcode:</b></td>
    <td><html:text property="fivepPlate" size="40"/></td>
    </tr>

    <tr>
    <td><b>Enter 3P open oligo plate barcode:</b></td>
    <td><html:text property="threepOpenPlate" size="40"/></td>
    </tr>

    <tr>
    <td><b>Enter 3P closed oligo plate barcode:</b></td>
    <td><html:text property="threepClosedPlate" size="40"/></td>
    </tr>

    <tr>
    <td><b>Select library:</b></td>
    <td><select name="cdnaLibrary">        
        <option value="human cDNA mix brain/placental_1:4">human cDNA mix brain/placental_1:4
        <option value="human placental cDNA template">Human placental cDNA template
        <option value="human brain cDNA template">Human brain cDNA template
        <option value="yeast genomic DNA">yeast genomic DNA
        <option value="yeast cDNA">yeast cDNA
        </select>
    </td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="PlatesetQueueItemsDisplay.jsp" flush="true" />

</body>
</html>

