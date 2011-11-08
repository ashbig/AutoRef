<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
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
                    <jsp:include page="addInstitutionsTitle.jsp" />
                    <html:form action="AddInstitutions.do">
                        <html:errors/>
                        <p class="formlabel">Please enter the following information and submit:</p>
                        <table width="100%" height="118" border="0" align="center">
                            <tr>
                                <td class="tableheader">Institution</td>
                                <td class="tableheader">Category</td>
                                <td class="tableheader">Country</td>
                            </tr>
                        <%  int count = 0;%>
                            <logic:iterate name="nofound" id="it">
                                <tr>
                                    <td class="tableinfo"><bean:write name="it"/></td>
                                    <td class="tableinfo">
                                        <html:select property='<%= "category[" + count +"]" %>'>
                                            <html:options name="categories"/>
                                        </html:select>
                                    </td>
                                    <td class="tableinfo">
                                        <html:select property='<%= "country[" + count +"]" %>'>
                                            <html:options name="countries"/>
                                        </html:select>
                                    </td>
                                    <input type="hidden" name='<%= "institution[" + count +"]" %>' value="<bean:write name="it"/>"/>
                                </tr>
                                <% count++;%>
                            </logic:iterate>
                        </table>
                        <html:submit styleClass="text" value="Continue"/>
                </html:form></td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
</html>

