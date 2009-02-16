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
                    <html:form action="/pReceiveSearch" method="POST">
                        <html:errors/>
                        
                        <table width="100%" border="0">
                            <tr><td><h2>Receive Plasmids</h2></td></tr>
                            <tr><td><font size="-2">*Required field is in bold</font></td></tr>
                            <tr><td>Please enter the clone IDs (separate each clone ID by new line):</td></tr>
                            <tr>
                                <td>
                                    <table>
                                        <tr>
                                            <td>
                                                <html:textarea property="cloneid" cols="30" rows="5"/>
                                            </td>
                                            <td valign="top">
                                                <html:submit value="Find" onclick="return checkFind();"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </html:form>
                </td>
            </tr>
        </table>
        
        <jsp:include page="footer.jsp" />
        <script>
            function checkFind() {
                t = document.forms["pReceiveSearchForm"].elements["cloneid"].value;
                if ((t == null) || (t.length < 1)) {
                    alert("Please enter clone id before continue.");
                    return false;
                }
                return true;
            }
        </script>
    </body>
</html>

