<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Process" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">
    
    <body>
        <jsp:include page="internalHomeTitle.jsp" />
        <table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="sampleTrackingMenu.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="updateCloneStorageTitle.jsp" />--%>
                    <html:errors/>
                    
                    <p class="formlabel">Please select the clone storage that will be removed:</p>
                    
                    <html:form action="UpdateCloneStorage.do">
                        <%  int count = 0;%>
                        <logic:iterate name="cloneSamples" id="clone">
                            <p class="text">Clone: <bean:write name="clone" property="name"/></p>
                            <table width="100%" border="0">
                                <tr>
                                    <td class="tableheader">&nbsp;</td>
                                    <td class="tableheader">Container</td>
                                    <td class="tableheader">Position</td>
                                    <td class="tableheader">Well</td>
                                    <td class="tableheader">Type</td>
                                </tr>
                                
                                <logic:iterate name="clone" property="samples" id="sample">
                                    <tr class="tableinfo"> 
                                        <td><html:multibox property="selectedSampleids"><bean:write name="sample" property="sampleid"/></html:multibox></td>
                                        <td><bean:write name="sample" property="containerlabel"/></td>
                                        <td><bean:write name="sample" property="position"/></td>
                                        <td><bean:write name="sample" property="well"/></td>
                                        <td><bean:write name="sample" property="type"/></td>
                                    </tr>
                                </logic:iterate>
                                <tr class="tableinfo"> 
                                    <td colspan="5">Please enter the 2D-barcode of the new working storage: 
                                    <html:text property='<%= "label[" + count +"]" %>' /></td>
                                </tr>
                                <% count++;%>
                                <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
                            </table>
                        </logic:iterate>
                        <p align="center"><html:submit styleClass="itemtext" value="Continue"/></p>
                    </html:form>
                    
                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" />
    </body>
</div>
</html>

