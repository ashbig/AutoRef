<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Find Author By Name Result</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script>
            function checkForm() {
                a = document.getElementsByName("AID");
                for (i=0; i<a.length; i++) {
                    if (a[i].checked)
                        return true;
                }
                alert("Please select an author before continue.")
                return false;
            }
        </script>
    </head>

    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
        <html:form action="/findAuthor" method="POST">
            <logic:present name="RU">
                <input type="hidden" name="RU" id="RU" value="<bean:write name="RU"/>"/>
            </logic:present>
            <logic:notPresent name="RU">
                <html:hidden property="RU" value="/vInput6"/>
            </logic:notPresent>
            <html:errors/>
            <table width="800" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
                <logic:present name="Authors">
                    <tr><td>Please select from the author list and click "Select" button.</td></tr>
                    <tr height="18px"><td></td></tr>
                    <tr>
                        <td><html:submit value="Select" onclick="return checkForm();"/>&nbsp;<html:submit value="Search Again"/></td>
                    </tr>
                    <tr>
                        <td width="100%" align="center" valign="top">
                            <table border="1" width="100%">
                                <tr>
                                    <th>&nbsp;</th>
                                    <th>Author Name</th>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Phone</th>
                                    <th>Fax</th>
                                    <th>Email</th>
                                    <th>Address</th>
                                    <th>Web Address</th>
                                    <th>Description</th>
                                </tr>
                                <logic:iterate id="author" name="Authors" indexId="AID">
                                    <tr>
                                        <td><input type="radio" id="AID" name="AID" value="<bean:write name="AID"/>"/>&nbsp;</td>
                                        <td><bean:write name="author" property="name"/>&nbsp;</td>
                                        <td><bean:write name="author" property="firstname"/>&nbsp;</td>
                                        <td><bean:write name="author" property="lastname"/>&nbsp;</td>
                                        <td><bean:write name="author" property="tel"/>&nbsp;</td>
                                        <td><bean:write name="author" property="fax"/>&nbsp;</td>
                                        <td><bean:write name="author" property="email"/>&nbsp;</td>
                                        <td><bean:write name="author" property="address"/>&nbsp;</td>
                                        <td><bean:write name="author" property="www"/>&nbsp;</td>
                                        <td><bean:write name="author" property="description"/>&nbsp;</td>
                                    </tr>
                                </logic:iterate>
                            </table>

                        </td>
                    </tr>
                    <tr><td><html:submit value="Select" onclick="return checkForm();"/>&nbsp;<html:submit value="Search Again"/></td></tr>
                </logic:present>
                <logic:notPresent name="Authors">
                    <tr><td>There is no author match the given name. Please return and try again.</td></tr>
                    <tr><td><html:submit value="Return"/></td></tr>
                </logic:notPresent>
            </table>
        </html:form>
                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" />
    </body>
</html>

