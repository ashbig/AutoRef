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
        <table width="100%" border="0" align="left">
            <tr><td width="100%" align="left" valign="top">
                    <html:form action="Registration.do">
                        <input type="hidden" name="forward" value="confirm">
                        <p class="mainbodytext">Please enter the following information.<br>(* indicates required field) </p>
                        <html:errors/>
                        <table name="main" width="100%" border="0">
                                <table name="basicinfo" border="1" rules="none" frame="box" style="margin-bottom:1em;" width="940px">
                                    <colgroup>
                                        <col width="150px">
                                        <col width="850px">
                                    </colgroup>
                                    <tr class="formlabel"> 
                                        <td align="left" valign="baseline">*First Name:</td>
                                        <td align="left" valign="baseline"> 
                                             <input type="text" maxlength="50" name="firstname" size="30" styleClass="text" required/>
                                        </td>
                                    </tr>
                                    <tr class="formlabel"> 
                                        <td align="left" valign="baseline">*Last Name:</td>
                                        <td align="left" valign="baseline"> 
                                           <input type="text" maxlength="50" name="lastname" size="30" styleClass="text" required/>
                                        </td>
                                    </tr>
                                    <tr class="formlabel"> 
                                        <td align="left" valign="baseline">*Email:</td>
                                        <td align="left" valign="baseline"> 
                                            <input type="email" maxlength="50" name="email" size="40" styleClass="text" required/>
                                        </td>
                                    </tr>
                                    <tr class="formlabel"> 
                                        <td align="left" valign="baseline">*Phone:</td>
                                        <td align="left" valign="baseline"> 
                                            <input type="tel" maxlength="50" name="phone" size="40" styleClass="text" required/>
                                        </td>
                                    </tr>
                                </table>
                                <table name="institution" border="1" rules="none"frame="box" style="margin-bottom:1em;" width="940px">
                                <tr class="formlabel"> 
                                    <td align="left" valign="top">*Institution/<br>Company Name:</td>
                                    <td align="left" valign="baseline"> 
                                        <table width="100%" border="0" bordercolor="#000000">
                                            <colgroup>
                                                <col width="150px">
                                                <col width="850px">
                                            </colgroup>
                                            <tr>
                                                <td class="formlabel" colspan="2"><input type="radio" name="institution" value="-- Please Select --" required> Expedited MTA Member: [<a class="text" href="memberinfo.html" target="_blank">?</a>]</td>
                                            </tr>
                                            <tr>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                                <td class="text">
                                                    <html:select property="institution1" styleClass="text">
                                                        <html:option value="">-- Please Select --</html:option>
                                                        <html:options name="members"/>
                                                    </html:select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="formlabel" colspan="2"><input type="radio" name="institution" value="-- Please Select --" required> Non Member: [<a class="text" href="nonmemberinfo.html" target="_blank">?</a>]</td>
                                            </tr>
                                            <tr>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                                <td class="text">
                                                    <html:select property="institution2" styleClass="text">
                                                        <html:option value="">---------- Please Select ----------</html:option>
                                                        <html:options name="institutions"/>
                                                    </html:select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="formlabel" colspan="2"><input type="radio" name="institution" value="" required> Other Institution (not listed above):</td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td class="text" colspan="2">
                                                    <html:text maxlength="100" property="institution3" size="50" styleClass="text"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="formlabel">Choose category:</td>
                                            </tr>
                                            <tr>
                                            <td></td>
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
                                </table>

                            <table name="pi" border="1" style="margin-bottom:1em;" rules="none" frame="box" width="940px">
                                <tr class="formlabel"> 
                                            <td align="left" valign="baseline">* User Group:</td>
                                            <td align="left" valign="baseline">
                                                <html:select property="group" styleClass="text">
                                                    <html:option value="">-- Please Select --</html:option>
                                                    <html:options name="groups"/>
                                                </html:select>
                                            </td>        
                                </tr>   
                                <tr><td>&nbsp;</td></tr>
                                
                                <tr class="formlabel">
                            <td align="left" valign="baseline"> 
                                *PI or<br>Supervisor:
                                </td>
                                <td align="left" valign="baseline"><input type="radio" name="pinameinput" required> 
                                    <html:select property="piname" styleClass="text">
                                        <html:option value=""/>
                                        <html:options collection="pis" property="nameInstitution"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td>&nbsp;</td>
                                <td align="left" valign="top"><input type="radio" name="pinameinput" required> Not listed:</td>
                            </tr>
                                <td align="left" valign="baseline" colspan="3">
                                    <table width="100%" border="0" bordercolor="#000000">
                                        <colgroup>
                                            <col width="295px">
                                            <col width="100px">
                                            <col width="auto">
                                        </colgroup>
                                        <tr> 
                                            <td>&nbsp;</td>
                                            <td class="formlabel"> First Name:</td>
                                            <td>
                                                <html:text maxlength="50" property="pifirstname" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td>&nbsp;</td>
                                            <td class="formlabel"> Last Name:</td>
                                            <td>
                                                <html:text maxlength="50" property="pilastname" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td>&nbsp;</td>
                                            <td class="formlabel"> Email:</td>
                                            <td>
                                                <html:text maxlength="50" property="piemail" size="30" styleClass="text"/>
                                            </td>
                                        </tr>
                                                            </tr>
                                                                   
                                        </table>

                            </table>
                                </td>
                            <table border="1" rules="none" frame="box" width="940px">
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*Please enter a password:</td>
                                <td align="left" valign="baseline"> 
                                    <html:password maxlength="50" property="password" size="30" styleClass="text"/>
                                </td>
                            </tr>
                            <tr class="formlabel"> 
                                <td align="left" valign="baseline">*Please enter the password again</td>
                                <td align="left" valign="baseline"> 
                                    <html:password maxlength="50" property="password2" size="30" styleClass="text"/>
                                </td>
                            </tr>
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

