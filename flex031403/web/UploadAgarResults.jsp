<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Upload Agar Plate Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="UploadAgarResults.do" focus="logFile" enctype="multipart/form-data">
    <h2><bean:message key="flex.name"/> : Upload Agar Plate Results</h2>
    <hr>
    <html:errors/>
   
    <p>

    <table>

    <tr>
        <td class="prompt">Plate enter the colony picking log file:</td>    
        <td><html:file property="colonyLogFile" /></td>
    </tr>

    <tr>
       <td class="prompt"><bean:message key="flex.researcher.barcode.prompt"/></td>
       <td><html:password property="researcherBarcode"/></td>
    </tr>
    </table>
    <br>

    <html:submit/>

</html:form>

<jsp:include page="QueueItemsDisplay.jsp" flush="true"/>

</body>
</html>
