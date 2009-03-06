<%@ page language="java" %>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Successfully Receive Plasmids</title>
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
                    <jsp:include page="pReceiveSuccessTitle.jsp" />
                    <table width="100%" border="0">
                        <tr>
                            <td>
                                <logic:present name="CLONES">
                                    <logic:notEmpty name="CLONES">
                                        <table width="100%" border="0">
                                            <tr height="18px"><td><hr></td></tr>
                                            <tr><td>Find following clone(s) has been successfully submitted</td></tr>
                                            <tr height="18px"><td></td></tr>
                                            <tr>
                                                <td>
                                                    <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="blue">
                                                        <tr align="center" bgcolor="#CCCCCC">
                                                            <td width="10%"><strong>Clone ID</strong></td>
                                                            <td width="10%"><strong>Status</strong></td>
                                                            <td width="25%"><strong>Host Strain</strong></td>
                                                            <td width="25%"><strong>Restriction</strong></td>
                                                            <td width="30%"><strong>Special MTA</strong></td>
                                                        </tr>
                                                        <logic:iterate id="Clone" name="CLONES" indexId="CID">
                                                            <tr bgcolor="white">
                                                                <td>&nbsp;<a href="http://plasmid.med.harvard.edu/PLASMID/GetCloneDetail.do?cloneid=<bean:write name="Clone" property="cloneid"/>" target="clonedetail"><bean:write name="Clone" property="cloneid"/></a></td>
                                                                <td>
                                                                    &nbsp;<bean:write name="Clone" property="status"/>
                                                                </td>
                                                                <td>
                                                                    &nbsp;<bean:write name="Clone" property="hs"/>
                                                                </td>
                                                                <td>
                                                                    &nbsp;<bean:write name="Clone" property="restriction"/>
                                                                </td>
                                                                <td>
                                                                    &nbsp;<bean:write name="Clone" property="mta"/>
                                                                </td>
                                                            </tr>
                                                        </logic:iterate>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr bgcolor="white" height="12px"><td></td></tr>
                                            <tr bgcolor="white">
                                                <td>
                                                    <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="blue">
                                                        <tr bgcolor="white">
                                                            <td width="17%" align="right"><strong>Who submitted:</strong></td>
                                                            <td width="24%">
                                                                &nbsp;<bean:write name="sender"/>
                                                            </td>
                                                            <td width="16%" align="right"><strong>When submitted:</strong></td>
                                                            <td width="24%">
                                                                &nbsp;<bean:write name="sdate"/>
                                                            </td>
                                                        </tr>
                                                        <tr bgcolor="white">
                                                            <td align="right"><strong>Who received:</strong></td>
                                                            <td>
                                                                &nbsp;<bean:write name="receiver"/>
                                                            </td>
                                                            <td align="right"><strong>When received:</strong></td>
                                                            <td>
                                                                &nbsp;<bean:write name="rdate"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr bgcolor="white" height="18px"><td></td></tr>
                                        </table>
                                    </logic:notEmpty>
                                </logic:present>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <jsp:include page="footer.jsp" />
    </body>
</html>

