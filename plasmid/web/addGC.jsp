<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Add Growth Condition</title>
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
                    <jsp:include page="addGCTitle.jsp" />
                    <html:form action="addGC.do">
                        <html:errors/>
                        <table width="100%" height="118" border="0" align="center">
                            <tr>
                                <td height="50" colspan="2" class="tableheader"><strong>Please enter Growth Condition information and click Add button to add growth condition to the system. Duplicate growth condition will be ignored.</strong></td>
                            </tr>
                            <tr>
                                <td align="right" valign="top">Growth Condition&nbsp; </td>
                                <td> <html:text property="GC"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Host Type&nbsp; </td>
                                <td>
                                    <logic:present name="<%=Constants.HTS%>">
                                        <bean:size id="size" name="<%=Constants.HTS%>"/>
                                        <logic:equal name="size" value="0">
                                            <html:text property="HT"/>
                                        </logic:equal>
                                        
                                        <logic:greaterThan name="size" value="0">
                                            <html:select property="HT">
                                                <html:options name="<%=Constants.HTS%>"/>
                                            </html:select>
                                        </logic:greaterThan>
                                    </logic:present>
                                    <logic:notPresent name="<%=Constants.HTS%>">
                                        <html:text property="HT"/>
                                    </logic:notPresent>
                                    <html:submit value="Add New Host Type"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Antibiotic Selection&nbsp; </td>
                                <td> <html:text property="AB"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Growth Condition Description&nbsp; </td>
                                <td> <html:textarea property="GCD" rows="5" cols="66"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right">Growth Condition Comment&nbsp; </td>
                                <td> <html:textarea property="GCC" rows="5" cols="66"/>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td class="text">
                                    <html:submit value="Add Growth Condition"/>&nbsp;
                                    <logic:present name="RU">
                                        <html:submit value="Return"/>
                                        <input type="hidden" name="RU" id="RU" value="<bean:write name="RU"/>"/>
                                    </logic:present>
                                    <logic:notPresent name="RU">
                                        <logic:notEmpty name="addGCForm" property="RU">
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

