<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Process" %> 
<%@ page import="plasmid.coreobject.Clone" %>

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
                    <jsp:include page="updateCloneStatusTitle.jsp" />
                    <html:errors/>
                    <html:form action="UpdateCloneStatus.do">
                        <logic:present name="updateCloneStatusMessage">
                            <p class="alert"><bean:write name="updateCloneStatusMessage"/></p>
                        </logic:present>
                        <table width="100%" border="0">
                            <tr> 
                                <td colspan="2" class="formlabel">Please enter the PlasmID Clone IDs: (e.g. HsCD00000196, separate each ID with white space)</td>
                            </tr>
                            <tr> 
                                <td colspan="2">
                                    <html:textarea styleClass="itemtext" property="cloneList" rows="10" cols="50"/>
                                </td>
                            </tr>
                            <tr> 
                                <td width="15%" class="formlabel">Make clones: </td>
                                <td class="itemtext">
                                    <html:radio styleClass="itemtext" property="status" value="<%=Clone.AVAILABLE%>">Available</html:radio>
                                    <html:radio styleClass="itemtext" property="status" value="<%=Clone.NOT_AVAILABLE%>">Not Available</html:radio>
                                </td>
                            </tr>
                            <tr> 
                                <td colspan="2" class="formlabel">Please enter comments:</td>
                            </tr>
                            <tr> 
                                <td colspan="2">
                                    <html:textarea styleClass="itemtext" property="comments" rows="10" cols="50"/>
                                </td>
                            </tr>
                            <tr> 
                                <td colspan="2">
                                    <html:submit styleClass="itemtext" value="Submit"/>
                                </td>
                            </tr>
                        </table>
                    </html:form></td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" />
    </body>
</html>

