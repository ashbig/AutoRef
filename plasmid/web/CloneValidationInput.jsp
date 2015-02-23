<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">


    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table id='content' width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="cloneValidationTitle.jsp" />--%>
                    <html:form action="CloneValidationInput.do">
                        <html:errors/>
                        <table width="100%" height="118" border="0" align="center">
                            <tr> 
                                <td height="10" colspan="2" class="tableheader"><strong>Please enter order IDs (separate each order ID with white space or new line)</strong></td>
                            </tr>
                            <tr> 
                                <td height="100">&nbsp; </td>
                                <td height="100"> <html:textarea property="orderids" cols="30" rows="5"/> 
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td class="text"><html:submit value="Submit"/></td>
                            </tr>
                        </table>
                    </html:form>

                    <logic:present name="<%=Constants.CLONEORDER%>">
                        <logic:iterate name="<%=Constants.CLONEORDER%>" id="cloneorder">
                            <p class="formlabel">Order ID: <bean:write name="cloneorder" property="orderid"/></p>
                            <table width="100%" border="1">
                                <tr>
                                    <td class="tableheader">CloneID</td>
                                    <td class="tableheader">Workflow</td>
                                    <td class="tableheader">Validation Result</td>
                                    <td class="tableheader">Isolate</td>
                                    <td class="tableheader">Phred</td>
                                </tr>

                                <logic:iterate name="cloneorder" property="clones" id="clone">
                                    <logic:equal name="clone" property="hasValidation" value="true">
                                        <tr class="tableinfo"> 
                                            <td><bean:write name="clone" property="clone.name"/></td>
                                            <td><bean:write name="clone" property="validation.workflow"/></td>
                                            <td><bean:write name="clone" property="validation.result"/></td>
                                            <td><bean:write name="clone" property="validation.readname"/></td>
                                            <td><bean:write name="clone" property="validation.phred"/></td>
                                        </tr>
                                    </logic:equal>
                                    <logic:equal name="clone" property="hasValidation" value="false">
                                        <tr class="tableinfo"> 
                                            <td><bean:write name="clone" property="clone.name"/></td>
                                            <td>NA</td>
                                            <td>NA</td>
                                            <td>NA</td>
                                            <td>NA</td>
                                        </tr>
                                    </logic:equal>
                                </logic:iterate>
                            </table>
                        </logic:iterate>
                    </logic:present>

                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" /></body>
     </div>
</html>

