<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Add Host Strain</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
                    <jsp:include page="addHSTitle.jsp" />
                    <html:form action="addHS.do">
                        <html:errors/>
                        <table width="100%" height="118" border="0" align="center">
                            <tr>
                                <td height="10" colspan="2" class="tableheader"><strong>Please enter all host strains (separate them by a new line) and click Add button to add host strain(s) to the system. Duplicate host strain(s) will be ignored.</strong></td>
                            </tr>
                            <tr>
                                <td height="100">&nbsp; </td>
                                <td height="100"> <html:textarea property="HS" cols="30" rows="5"/>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td class="text">
                                    <html:submit value="Add Host Strain"/>&nbsp;
                                    <logic:present name="RU">
                                        <html:submit value="Return"/>
                                        <input type="hidden" name="RU" id="RU" value="<bean:write name="RU"/>"/>
                                    </logic:present>
                                    <logic:notPresent name="RU">
                                        <logic:notEmpty name="addHSForm" property="RU">
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

