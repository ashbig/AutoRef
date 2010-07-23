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
    </head>
    
    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <logic:equal name="registrationForm" property="update" value="true">
                        <jsp:include page="updateAccountTitle.jsp" />
                    </logic:equal>
                    <logic:notEqual name="registrationForm" property="update" value="true">
                        <jsp:include page="registrationTitle.jsp" />
                    </logic:notEqual>
                    <html:form action="Registration.do">
                        <input type="hidden" name="forward" value="confirm">
                        <p class="homeMainText">Please enter the following information. (* indicates required 
                        field) </p>
                        <html:errors/>
                        <table width="100%" border="0">
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">*First Name:</td>
                                <td width="70%" align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="firstname" size="30" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">*Last Name:</td>
                                <td width="70%" align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="lastname" size="30" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">*Email:</td>
                                <td width="70%" align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="email" size="40" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">*Phone:</td>
                                <td width="70%" align="left" valign="baseline"> 
                                    <html:text maxlength="50" property="phone" size="40" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="top">*PI Institution or Company Name:</td>
                                <td width="70%" align="left" valign="baseline"> 
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
                                                    <html:option value="">---------- US Institutions ----------</html:option>
                                                    <html:options name="us"/>
                                                    <html:option value="">---------- Government ----------</html:option>
                                                    <html:options name="government"/>
                                                    <html:option value="">---------- International Institutions ----------</html:option>
                                                    <html:options name="international"/>
                                                    <html:option value="">---------- Companies ----------</html:option>
                                                    <html:options name="company"/>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="formlabel" colspan="2">Other Institution (not listed above):</td>
                                        </tr>
                                        <tr>
                                            <td class="text" colspan="2">
                                                <html:text maxlength="250" property="institution3" size="100" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="formlabel">Choose category:</td>
                                            <td class="text">
                                                <html:select property="category" styleClass="text">
                                                    <html:option value="">-- Please Select --</html:option>
                                                    <html:options name="categories"/>
                                                </html:select>            
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr></tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">Please choose your PI:<br>
                                    (*PI is required for HIP member, DF/HCC member, Harvard affiliate and Other academic groups)
                                </td>
                                <td width="70%" align="left" valign="baseline"> 
                                    <html:select property="piname" styleClass="text">
                                        <html:option value=""/>
                                        <html:options collection="pis" property="nameInstitution"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="top">If you don't find your PI from the 
                                above list, please enter your PI information here:</td>
                                <td width="70%" align="left" valign="baseline"> <table width="100%" border="0" bordercolor="#000000">
                                        <tr> 
                                            <td width="25%" class="formlabel"> *First Name:</td>
                                            <td width="75%">
                                                <html:text maxlength="50" property="pifirstname" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td class="formlabel"> *Last Name:</td>
                                            <td>
                                                <html:text maxlength="50" property="pilastname" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td class="formlabel"> *Email:</td>
                                            <td>
                                                <html:text maxlength="50" property="piemail" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                </table></td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">Please choose the appropriate 
                                group you or your PI belong to:</td>
                                <td width="70%" align="left" valign="baseline">
                                    <html:select property="group" styleClass="text">
                                        <html:option value="">-- Please Select --</html:option>
                                        <html:options name="groups"/>
                                    </html:select>
                                    <a href="PIList.html" target="_blank">check if your PI is a DF/HCC memeber</a>
                                </td>        
                            </tr>
                            
                            <tr class="formlabel"> 
                                <td width="30%" align="right">&nbsp;</td>
                                <td width="70%">&nbsp;</td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">*Please enter a password:</td>
                                <td width="70%" align="left" valign="baseline"> 
                                    <html:password maxlength="50" property="password" size="30" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">*Please enter the password 
                                again</td>
                                <td width="70%" align="left" valign="baseline"> 
                                    <html:password maxlength="50" property="password2" size="30" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <logic:equal name="registrationForm" property="update" value="true">
                                    <td width="30%" align="right" valign="baseline">
                                        <html:submit styleClass="text" value="Update"/>
                                    </td>
                                </logic:equal>
                                <logic:notEqual name="registrationForm" property="update" value="true">
                                    <td width="30%" align="right" valign="baseline">
                                        <html:submit styleClass="text" value="Register"/>
                                    </td>
                                </logic:notEqual>
                                <td width="70%" align="left" valign="baseline">
                                    <html:reset styleClass="text" value="Clear"/>
                                </td>
                            </tr>
                        </table>
                </html:form></td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
    <HEAD>
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
    </HEAD>
</html>

