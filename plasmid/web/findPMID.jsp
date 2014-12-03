<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Find Publication by PMID Result</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script>
            function checkForm() {
                a = document.getElementsByName("PID");
                for (i=0; i<a.length; i++) {
                    if (a[i].checked)
                        return true;
                }
                alert("Please select a publication before continue.")
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
                    <html:form action="/findPMID" method="POST">
                        <logic:present name="RU">
                            <input type="hidden" name="RU" id="RU" value="<bean:write name="RU"/>"/>
                        </logic:present>
                        <logic:notPresent name="RU">
                            <html:hidden property="RU" value="/vInput7"/>
                        </logic:notPresent>
                        <html:errors/>
                        <table width="800" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
                            <logic:present name="PMs">
                                <tr><td>Please select from the publication list and click "Select" button.</td></tr>
                                <tr height="18px"><td></td></tr>                                
                                <tr>
                                    <td><html:submit value="Select" onclick="return checkForm();"/>&nbsp;<html:submit value="Search Again"/></td>
                                </tr>
                                <tr height="18px"><td></td></tr>                                
                                <tr>
                                    <td width="100%" align="center" valign="top">
                                        <table width="100%" border="1">
                                            <tr>
                                                <th>&nbsp;</th>
                                                <th>PMID</th>
                                                <th>Title</th>
                                            </tr>
                                            <logic:iterate id="pm" name="PMs" indexId="PID">
                                                <tr>
                                                    <td><input type="radio" id="PID" name="PID" value="<bean:write name="PID"/>"/></td>
                                                    <td><bean:write name="pm" property="pmid"/></td>
                                                    <td><bean:write name="pm" property="title"/></td>
                                                </tr>
                                            </logic:iterate>
                                        </table>
                                    </td>
                                </tr>
                                <tr height="18px"><td></td></tr>                                
                                <tr><td><html:submit value="Select" onclick="return checkForm();"/>&nbsp;<html:submit value="Search Again"/></td></tr>
                            </logic:present>
                            <logic:notPresent name="PMs">
                                <tr><td>
                                        There is no publication match the given PMID. Please return and try again.
                                </td></tr>
                                <tr><td>
                                        <html:submit value="Return"/>
                                </td></tr>
                            </logic:notPresent>
                        </table>
                    </html:form>
                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" />
    </body>
</html>

