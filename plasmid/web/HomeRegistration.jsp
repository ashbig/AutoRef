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
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <link href="layout.css" rel="stylesheet" type="text/css" />
        <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
    </head>
    <div class="gridContainer clearfix">
        <div class=content">
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table id='content' width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td> --%>
                <td width="100%" align="left" valign="top">
                    <%--
                    <logic:equal name="registrationForm" property="update" value="true">
                        <jsp:include page="updateAccountTitle.jsp" />
                    </logic:equal>
                    <logic:notEqual name="registrationForm" property="update" value="true">
                        <jsp:include page="registrationTitle.jsp" />
                    </logic:notEqual>
                    --%>
                    <html:form action="Registration.do">
                        <input type="hidden" name="forward" value="confirm">
                        <p class="mainbodytext">Please enter the following information. (* indicates required 
                        field) </p>
                        <html:errors/>
                        <table style="margin-bottom:1em;" width="940px" border="0">
                            <colgroup>
                                <col width="170px">
                                <col width="300px">
                                <col width="auto">
                            </colgroup>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*First Name:</td>
                                <td align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="firstname" size="40" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*Last Name:</td>
                                <td align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="lastname" size="40" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*Email:</td>
                                <td align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="email" size="40" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*Phone:</td>
                                <td align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="phone" size="40" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*User Group:</td>
                                <td align="left" valign="baseline">
                                    <html:select property="group" styleClass="text">
                                        <html:option value="">-- Please Select --</html:option>
                                        <html:options name="groups"/>
                                    </html:select>
                                </td>        
                            </tr>
                            <tr><td>&nbsp;</td></tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="top">*Institution/<br>Company Name:</td>
                                <td align="left" valign="baseline"> 
                                    <table width="100%" border="0" bordercolor="#000000">
                                        <tr>
                                            <td class="formlabel" colspan="2">Expedited MTA Member: [<a class="text" href="memberinfo.html" target="_blank">?</a>]</td>
                                        </tr>
                                        <tr>
                                            <td class="text" colspan="2">
                                                <html:select property="institution1" styleClass="text">
                                                    <html:option value="">-- Please Select --</html:option>
                                                    <html:options name="members"/>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="formlabel" colspan="2">Non Member: [<a class="text" href="nonmemberinfo.html" target="_blank">?</a>]</td>
                                        </tr>
                                        <tr>
                                            <td class="text" colspan="2">
                                                <html:select property="institution2" styleClass="text">
                                                    <html:option value="">---------- Please Select ----------</html:option>
                                                    <html:options name="institutions"/>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="formlabel" colspan="2"><font color="grey">Institution not listed above:</td>
                                        </tr>
                                        <tr>
                                            <td class="text" colspan="2">
                                                <html:text maxlength="100" property="institution3" size="94" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="formlabel" align="left" width="25%">Choose category:</td>
                                            <td class="text" align="left">
                                                <html:select property="category" styleClass="text">
                                                    <html:option value="">-- Please Select --</html:option>
                                                    <html:options name="categories"/>
                                                </html:select>            
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td>&nbsp;</td></tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*PI or Supervisor:
                                </td>
                                <td align="left" valign="baseline"> 
                                    <html:select property="piname" styleClass="text">
                                        <html:option value=""/>
                                        <html:options collection="pis" property="nameInstitution"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td>&nbsp;</td>
                                <td align="left" valign="top"><font color="grey"> &nbsp;PI not in list above</td>
                            </tr>
                            <tr>                                
                                <td>&nbsp;</td>
                                <td align="left" valign="baseline"> 
                                    <table width="100%" border="0" bordercolor="#000000">
                                        <tr> 
                                            <td align="left" width="25%" class="formlabel"><font color="grey">First Name:</td>
                                            <td align="left" width="75%">
                                                <html:text maxlength="50" property="pifirstname" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td align="left" class="formlabel"><font color="grey">Last Name:</td>
                                            <td>
                                                <html:text maxlength="50" property="pilastname" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td align="left" class="formlabel"><font color="grey">Email:</td>
                                            <td>
                                                <html:text maxlength="50" property="piemail" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                </table></td>
                            </tr>
                            <tr><td>&nbsp;</td></tr>

                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*Password:</td>
                                <td align="left" valign="baseline"> 
                                    <html:password maxlength="50" property="password" size="30" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*Re-enter password</td>
                                <td align="left" valign="baseline"> 
                                    <html:password maxlength="50" property="password2" size="30" styleClass="text"/>
                                </td>
                            </tr>
                            <tr><td>&nbsp;</td></tr>
                            <tr class="formlabel"> 
                                <logic:equal name="registrationForm" property="update" value="true">
                                    <td align="right" valign="baseline">
                                        <html:submit styleClass="text" value="Update"/>
                                    </td>
                                </logic:equal>
                                <logic:notEqual name="registrationForm" property="update" value="true">
                                    <td align="right" valign="baseline">
                                        <html:submit styleClass="text" value="Register"/>
                                    </td>
                                </logic:notEqual>
                                <td align="left" valign="baseline">
                                    <html:reset styleClass="text" value="Clear"/>
                                </td>
                            </tr>
                        </table>
                </html:form></td>
            </tr>
        </table>
        </div>
    <jsp:include page="footer.jsp" /></body>
    <HEAD>
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
    </HEAD>
    </div>
</html>

