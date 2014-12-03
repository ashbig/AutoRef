<%@ page language="java" %>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Receive Plasmids</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="1000" height="406" border="2" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="pReceiveTitle.jsp" />

                    <logic:notPresent name="CLONES">
                        No clone available for submit. Please <a href="pReceiveSearch.jsp">go back</a> and check again.
                    </logic:notPresent>
                    <logic:present name="CLONES">
                        <logic:notEmpty name="CLONES">
                            <html:errors/>
                            <html:form method="post" action="/pReceive">
                                <table width="100%" border="0">
                                    <tr height="18px"><td><hr></td></tr>
                                    <tr><td>Find following clone(s) with entered clone id(s):<br><bean:write name="cloneid"/></td></tr>
                                    <tr height="18px"><td></td></tr>
                                    <tr>
                                        <td>
                                            <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="blue">
                                                <tr align="center" bgcolor="#CCCCCC">
                                                    <td width="2%">&nbsp;</td>
                                                    <td width="10%"><strong>Clone ID</strong></td>
                                                    <td width="10%"><strong>Status</strong></td>
                                                    <td width="25%"><strong>Host Strain</strong></td>
                                                    <td width="25%"><strong>Restriction</strong></td>
                                                    <td width="30%"><strong>Special MTA</strong></td>
                                                </tr>
                                                <logic:iterate id="Clone" name="CLONES" indexId="CID">
                                                    <tr bgcolor="white">
                                                        <td><input type="radio" name="CID" id="CID" value="<bean:write name="CID"/>"></td>
                                                        <td><bean:write name="Clone" property="name"/></td>
                                                        <td>
                                                            <html:select name="Clone" property="status">
                                                                <option value="AVAILABLE" selected>AVAILABLE</option>
                                                                <option value="NOT AVAILABLE">NOT AVAILABLE</option>
                                                            </html:select>
                                                        </td>
                                                        <td>
                                                            <html:select name="Clone" property="hs">
                                                                <html:options name="HS"/>
                                                            </html:select>
                                                        </td>
                                                        <td>
                                                            <html:select name="Clone" property="restriction">
                                                                <html:options name="RES"/>
                                                            </html:select>
                                                        </td>
                                                        <td>
                                                            <html:select name="Clone" property="mta">
                                                                <html:options name="MTA"/>
                                                            </html:select>
                                                        </td>
                                                    </tr>
                                                </logic:iterate>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr bgcolor="white">
                                        <td>
                                            <html:submit value="Remove" onclick="return checkRemove();"/>&nbsp;
                                        </td>
                                    </tr>                                    
                                    <tr bgcolor="white" height="12px"><td></td></tr>
                                    <tr bgcolor="white">
                                        <td>
                                            <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="blue">
                                                <tr bgcolor="white">
                                                    <td width="17%" align="right"><strong>Who submitted:</strong></td>
                                                    <td width="24%">
                                                        <html:text property="sender"/>
                                                    </td>
                                                    <td width="16%" align="right"><strong>When submitted:</strong></td>
                                                    <td width="24%">
                                                        <html:select property="sdm">
                                                            <html:options property="m"/>
                                                        </html:select>&nbsp;
                                                        <html:select property="sdd">
                                                            <html:options property="d"/>
                                                        </html:select>&nbsp;
                                                        <html:select property="sdy">
                                                            <html:options property="y"/>
                                                        </html:select>
                                                    </td>
                                                </tr>
                                                <tr bgcolor="white">
                                                    <td align="right"><strong>Who received:</strong></td>
                                                    <td>
                                                        <html:text property="receiver"/>
                                                    </td>
                                                    <td align="right"><strong>When received:</strong></td>
                                                    <td>
                                                        <html:select property="rdm">
                                                            <html:options property="m"/>
                                                        </html:select>&nbsp;
                                                        <html:select property="rdd">
                                                            <html:options property="d"/>
                                                        </html:select>&nbsp;
                                                        <html:select property="rdy">
                                                            <html:options property="y"/>
                                                        </html:select>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <html:hidden property="allstatus"/>
                                    <html:hidden property="allhs"/>
                                    <html:hidden property="allrestriction"/>
                                    <html:hidden property="allmta"/>
                                    <tr bgcolor="white" height="18px"><td></td></tr>
                                    <tr bgcolor="white">
                                        <td>
                                            <html:submit value="Submit" onclick="return checkForm();"/>
                                        </td>
                                    </tr>
                                </table>
                            </html:form>
                        </logic:notEmpty>
                    </logic:present>
                </td>
            </tr>
        </table>

        <jsp:include page="footer.jsp" />
        <logic:present name="CLONES">
            <script>


                function checkForm() {
                    if (document.forms["pReceiveForm"].elements["sender"].value.length < 1) {
                        alert("Please enter user who submitted the clone.");
                        return false;
                    }
                    if (document.forms["pReceiveForm"].elements["receiver"].value < 1) {
                        alert("Please enter user who received the clone.");
                        return false;
                    }

                    var sdm = document.forms["pReceiveForm"].elements["sdm"].value;
                    var sdd = document.forms["pReceiveForm"].elements["sdd"].value;
                    var sdy = document.forms["pReceiveForm"].elements["sdy"].value;
                    var rdm = document.forms["pReceiveForm"].elements["rdm"].value;
                    var rdd = document.forms["pReceiveForm"].elements["rdd"].value;
                    var rdy = document.forms["pReceiveForm"].elements["rdy"].value;
                    if ((sdm.length < 1) || (sdd.length < 1) || (sdy.length < 1)) {
                        alert("Please enter when the clone was submitted.");
                        return false;
                    }
                    if ((rdm.length < 1) || (rdd.length < 1) || (rdy.length < 1)) {
                        alert("Please enter when the clone was received.");
                        return false;
                    }

                    var s1 = document.getElementsByName("status");
                    var s2 = document.getElementsByName("hs");
                    var s3 = document.getElementsByName("restriction");
                    var s4 = document.getElementsByName("mta");
                    var j = s1.length;
                    var s1v = "", s2v = "", s3v = "", s4v = "";
                    for (var i=0; i<j; i++) {
                        s1v = s1v + s1[i].value + " \n";
                        s2v = s2v + s2[i].value + " \n";
                        s3v = s3v + s3[i].value + " \n";
                        s4v = s4v + s4[i].value + " \n";
                    }
                    document.forms["pReceiveForm"].elements["allstatus"].value = s1v;
                    document.forms["pReceiveForm"].elements["allhs"].value = s2v;
                    document.forms["pReceiveForm"].elements["allrestriction"].value = s3v;
                    document.forms["pReceiveForm"].elements["allmta"].value = s4v;
                    return true;
                }

                function checkRemove() {
                    var s = document.getElementsByName("CID");
                    var j = s.length;
                    for (i = 0; i < j; i++) {
                        if (s[i].checked)
                            return true;
                    }
                    alert("Please select a clone before remove.");
                    return false;
                }
            </script>
        </logic:present>
    </body>
</html>

