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
    </head>
    
    <body>
        <jsp:include page="internalHomeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="sampleTrackingMenu.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="updateCloneStorageTitle.jsp" />
                    <html:errors/>
                    
                    <html:form action="UpdateCloneStorageConfirm.do">
                    
                        <p class="formlabel">The following clone storage will be removed:</p>
                        <table width="100%" border="0">
                            <tr>
                                <td class="tableheader">Clone</td>
                                <td class="tableheader">Container</td>
                                <td class="tableheader">Position</td>
                                <td class="tableheader">Well</td>
                                <td class="tableheader">Type</td>
                            </tr>
                            
                            <logic:iterate name="selectedSamples" id="sample">
                                <tr class="tableinfo"> 
                                    <td><bean:write name="sample" property="clone.name"/></td>
                                    <td><bean:write name="sample" property="containerlabel"/></td>
                                    <td><bean:write name="sample" property="position"/></td>
                                    <td><bean:write name="sample" property="well"/></td>
                                    <td><bean:write name="sample" property="type"/></td>
                                </tr>
                            </logic:iterate>
                        </table>
                    
                        <p class="formlabel">The following clone storage will be added:</p>
                        <table width="100%" border="0">
                            <tr>
                                <td class="tableheader">Clone</td>
                                <td class="tableheader">Container</td>
                            </tr>
                            
                            <logic:iterate name="newContainers" id="container">
                                <tr class="tableinfo"> 
                                    <td><bean:write name="container" property="tubeSample.clone.name"/></td>
                                    <td><bean:write name="container" property="tubeSample.containerlabel"/></td>
                                </tr>
                            </logic:iterate>
                        </table>
                        
                        <p align="center"><html:submit styleClass="itemtext" value="Submit"/></p>
                    </html:form>
                    
                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" />
    </body>
</html>

