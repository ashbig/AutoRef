<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Add Vector Property Type</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="addVPTTitle.jsp" />
                    <html:form action="addVPT.do">
                        <html:errors/>
                        <table width="100%" height="118" border="0" align="center">
                            <tr>
                                <td height="10" colspan="2" class="tableheader"><strong>Please enter all vector property types (separate property types with a new line) and click Add button to add property type(s) to the system. Duplicate property type(s) will be ignored.</strong></td>
                            </tr>
                            <tr>
                                <td height="100">&nbsp; </td>
                                <td height="100">&nbsp;
                                    <logic:present name="CAT">
                                        <input type="hidden" id="CAT" name="CAT" value="<bean:write name="CAT"/>"/>
                                        <logic:equal name="CAT" value="A">
                                            Assay
                                        </logic:equal>
                                        <logic:equal name="CAT" value="C">
                                            Cloning System
                                        </logic:equal>
                                        <logic:equal name="CAT" value="E">
                                            Expression
                                        </logic:equal>
                                    </logic:present>
                                    <logic:notPresent name="CAT">
                                        <logic:empty name="addVPTForm" property="CAT">
                                        <html:radio property="CAT" value="A"/>&nbsp;Assay<br>
                                        <html:radio property="CAT" value="C"/>&nbsp;Cloning System<br>
                                        <html:radio property="CAT" value="E"/>&nbsp;Expression
                                        </logic:empty>
                                        <logic:notEmpty name="addVPTForm" property="CAT">
                                            <html:hidden property="CAT"/>
                                        <logic:equal name="addVPTForm" property="CAT" value="A">
                                            Assay
                                        </logic:equal>
                                        <logic:equal name="addVPTForm" property="CAT" value="C">
                                            Cloning System
                                        </logic:equal>
                                        <logic:equal name="addVPTForm" property="CAT" value="E">
                                            Expression
                                        </logic:equal>
                                        </logic:notEmpty>

                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr>
                                <td height="100">&nbsp; </td>
                                <td height="100"> <html:textarea property="VPT" cols="80" rows="5"/>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td class="text">
                                    <html:submit value="Add Property Type"/>&nbsp;
                                    <logic:present name="RU">
                                        <html:submit value="Return"/>
                                        <input type="hidden" name="RU" id="RU" value="<bean:write name="RU"/>"/>
                                    </logic:present>
                                    <logic:notPresent name="RU">
                                        <logic:notEmpty name="addVPTForm" property="RU">
                                            <html:submit value="Return"/>
                                            <html:hidden property="RU"/>
                                        </logic:notEmpty>
                                    </logic:notPresent>
                                </td>
                            </tr>
                        </table>
                </html:form></td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
</html>

