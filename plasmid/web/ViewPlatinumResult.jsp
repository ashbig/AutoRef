<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %>

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
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="platinumServiceTitle.jsp" />

                    <html:errors/>

                    <p>
                    <table width="100%" border="0">
                        <tr> 
                            <td width="20%" class="formlabel">Validation Status:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="platinumServiceStatus"/> </td>
                        </tr>
                    </table>

                    <p class="homeMainText">Validation Summary: Pass-<bean:write name="pass"/>; Fail-<bean:write name="fail"/>; Manual-<bean:write name="manual"/></p>
                    <table width="100%" border="1">
                        <tr>
                            <td class="tableheader">CloneID</td>
                            <td class="tableheader">Validation Result</td>
                            <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                <td class="tableheader">Validation Method</td>
                                <td class="tableheader">Workflow</td>
                                <td class="tableheader">Researcher</td>
                            </logic:equal>
                            <td class="tableheader">Date</td>
                        </tr>
                        <logic:iterate name="<%=Constants.CLONEORDER%>" property="clones" id="c">
                            <logic:equal name="c" property="hasValidation" value="true">
                                <tr class="tableinfo"> 
                                    <td><bean:write name="c" property="clone.name"/></td>
                                    <td><bean:write name="c" property="validation.result"/></td>
                                    <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                        <td><bean:write name="c" property="validation.method"/></td>
                                        <td><bean:write name="c" property="validation.workflowStringForWeb"/></td>
                                        <td><bean:write name="c" property="validation.who"/></td>
                                    </logic:equal>
                                    <td><bean:write name="c" property="validation.when"/></td>
                                </tr>
                            </logic:equal>
                        </logic:iterate>
                    </table>

                    <p class="homeMainText">Validation History</p>

                    <logic:iterate name="<%=Constants.CLONEORDER%>" property="clones" id="c">
                        <table width="100%" border="0">
                            <tr>
                                <td width="20%" class="formlabel">PlasmID Clone ID: </td>
                                <td class="text"><bean:write name="c" property="clone.name"/></td>
                            </tr>
                        </table>

                        <logic:equal name="c" property="hasHistory" value="1">
                            <table width="100%" border="1">
                                <tr>
                                    <td class="tableheader">Sequence</td>
                                    <td class="tableheader">Validation Result</td>
                                    <td class="tableheader">Validation Method</td>
                                    <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                        <td class="tableheader">Workflow</td>
                                        <td class="tableheader">Researcher</td>
                                    </logic:equal>
                                    <td class="tableheader">Date</td>
                                </tr>

                                <logic:iterate name="c" property="history" id="v">
                                    <tr class="tableinfo"> 
                                        <td><bean:write name="v" property="readForWeb"/></td>
                                        <td><bean:write name="v" property="result"/></td>
                                        <td><bean:write name="v" property="method"/></td>
                                        <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                            <td><bean:write name="v" property="workflowStringForWeb"/></td>
                                            <td><bean:write name="v" property="who"/></td>
                                        </logic:equal>
                                        <td><bean:write name="v" property="when"/></td>
                                    </logic:iterate>
                            </table>
                        </logic:equal>
                        <logic:equal name="c" property="hasHistory" value="0">
                            <p class="text">No validation history.</p>
                        </logic:equal>
                    </logic:iterate>

                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" /></body>
    <HEAD>
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
    </HEAD>
</html>

