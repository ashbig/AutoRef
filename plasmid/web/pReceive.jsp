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
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="pReceiveTitle.jsp" />
                    <html:form action="/pReceive" method="POST" enctype="multipart/form-data">
                        <html:errors/>
                        <table width="100%" border="0">
                            <tr><td><h2>Receive Plasmids</h2></td></tr>
                            <tr><td><font size="-2">*Required field is in bold</font></td></tr>
                            <tr><td>Please enter the clone IDs (separate each clone ID by new line or white space):</td></tr>
                            <tr><td>
                                <input type="text" size="60" id="cloneid" name="cloneid" />
                                &nbsp;<html:submit value="Find" onclick="checkFind();"/>
                            </td></tr>
                            <logic:present name="Clone">
                                <tr height="18px"><td></td></tr>
                                <tr><td>
                                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="blue">
                                            <tr bgcolor="white" align="center" bgcolor="#CCCCCC">
                                                <td width="10%"><strong>Clone ID</strong></td>
                                                <td width="10%"><strong>Status</strong></td>
                                                <td width="25%"><strong>Host Strain</strong></td>
                                                <td width="25%"><strong>Restriction</strong></td>
                                                <td width="30%"><strong>Special MTA</strong></td>
                                            </tr>
                                            <tr bgcolor="white">
                                                <td><bean:write name="Clone" property="cloneid"/></td>
                                                <td>
                                                    <html:select property="status">
                                                        <option selected>AVAILABLE</option>
                                                        <option>NOT AVAILABLE</option>
                                                    </html:select>
                                                </td>
                                                <td>
                                                    <logic:empty name="Clone" property="hosts">
                                                        <html:select name="pReceiveForm" property="hs">
                                                            <logic:iterate id="hs" name="HS">
                                                                        <option value="<bean:write name="hs" />"><bean:write name="hs" /></option>
                                                                    </logic:iterate>
                                                        </html:select>
                                                    </logic:empty>
                                                    <logic:notEmpty name="Clone" property="hosts">
                                                        <html:select property="hs">
                                                            <logic:iterate id="host" name="Clone" property="hosts">
                                                                <option value="<bean:write name="host" property="hoststrain"/>"><bean:write name="host" property="hoststrain"/></option>
                                                            </logic:iterate>
                                                        </html:select>
                                                    </logic:notEmpty>
                                                </td>
                                                <td><html:select property="restriction">
                                                    <option value="Academic and non-profit lab" selected>Academic and non-profit lab</option>
                                                    <option value="Hip only">Hip only</option>
                                                    <option value="No restriction">No restriction</option>
                                                </html:select></td>
                                                <td>
                                                    <html:file property="file"/>
                                                </td>
                                            </tr>
                                        </table>
                                </td></tr>
                                <tr bgcolor="white" height="12px"><td></td></tr>
                                <tr bgcolor="white"><td>
                                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="blue">
                                            <tr bgcolor="white">
                                                <td width="17%" align="right"><strong>Who submitted:</strong></td>
                                                <td width="24%">
                                                    <input type="text" name="sender" id="sender">
                                                </td>
                                                <td width="16%" align="right"><strong>When submitted:</strong></td>
                                                <td width="24%">
                                                    <input type="text" name="sdate" id="sdate"><br><font size="-1">(Please enter like 01-DEC-2008)</font>
                                                </td>
                                            </tr>
                                            <tr bgcolor="white">
                                                <td align="right"><strong>Who received:</strong></td>
                                                <td>
                                                    <input type="text" name="receiver" id="receiver">
                                                </td>
                                                <td align="right"><strong>When received:</strong></td>
                                                <td>
                                                    <input type="text" name="rdate" id="rdate"><br><font size="-1">(Please enter like 01-DEC-2008)</font>
                                                </td>
                                            </tr>
                                        </table>

                                </td></tr>
                                <tr bgcolor="white" height="18px"><td></td></tr>
                                <tr bgcolor="white"><td><html:submit value="Submit" onclick="return checkForm();"/></td></tr>
                            </logic:present>
                        </table>
                    </html:form>
                </td>
            </tr>
        </table>

        <jsp:include page="footer.jsp" />
        <script>
            function checkFind() {
                t = document.getElementById("cloneid").value;
                if ((t == null) || (t.length < 1)) {
                    alert("Please enter clone id before continue.");
                    return false;
                }
                return true;
            }
            function checkForm() {
                if (document.getElementById("sender").value.length < 1) {
                    alert("Please enter user who submitted the clone.");
                    return false;
                }
                if (document.getElementById("sdate").value.length < 1) {
                    alert("Please enter when the clone was submitted.");
                    return false;
                }
                if (document.getElementById("receiver").value.length < 1) {
                    alert("Please enter user who received the clone.");
                    return false;
                }
                if (document.getElementById("rdate").value.length < 1) {
                    alert("Please enter when the clone was received.");
                    return false;
                }

                return true;
            }
        </script>
    </body>
</html>

