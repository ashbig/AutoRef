<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %>

<html>
    <head>
        <title>Search Vector by Name</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script src="js/common.js"></script>
        <link href="layout.css" rel="stylesheet" type="text/css" />
        <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
        <!--<script type="text/javascript" src="SpryMenuBar.js"></script>  code from dreamweaver doesn't work, submenu action re-done with css  
        <script type="text/javascript">
        var MenuBar1 = new Spry.Widget.MenuBar("MenuBar1", {imgDown:"SpryMenuBarDownHover.gif", imgRight:"SpryMenuBarRightHover.gif"});</script> -->
    </head>
    <div class="gridContainer clearfix">

    <div class="content">
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="vSearchTitle.jsp" /> --%>
                    <html:form action="/vSearch">
                        <html:errors/>
                        <p>Use the text box below to search for information about plasmid backbones annotated in our database. Search is NOT case sensitive. </p>
                        <table width="100%" border="0">
                            <tr valign="top">
                                <td width="15%"><strong>Vector Name</strong>:</td>
                                <td width="85%">
                                    <html:text property="VN"/>
                                    <html:submit value=" Find "/>
                                    <html:radio property="search" value="N"/>Exact Match&nbsp;&nbsp;&nbsp;
                                    <html:radio property="search" value="Y"/>Non-exact Match
                                </td>
                            </tr>
                        </table>
                    </html:form>
                    <p></p>
                    <html:form action="/continueVSubmit">
                        <%--<html:submit value="Continue to Vector Submission Page"/>--%>
                        <p></p>
                        <logic:notPresent name="vSearchForm" property="results">
                            <logic:notEmpty name="vSearchForm" property="VN">
                                <center><font color="red"><b>No Vector Found. Please try again.</b></font></center>
                            </logic:notEmpty>
                        </logic:notPresent>
                        <logic:present name="vSearchForm" property="results">
                            <logic:empty name="vSearchForm" property="results">
                                <center><font color="red"><b>No Vector Found. Please try again.</b></font></center>
                            </logic:empty>
                            <logic:notEmpty name="vSearchForm" property="results">
                                <p>We found the following vectors. Please click the vector name to review the
                                    detailed information to make sure it is the same vector. </p>
                                <p><strong>NOTE: We DO NOT distribute all of these empty backbone vectors. This vector information is provided as a resource only.</strong></p>
                                <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                    <tr bgcolor="white">
                                        <td width="4%">&nbsp;</td>
                                        <td width="16%"><strong>Vector Name</strong></td>
                                        <td width="80%"><strong>Description</strong></td>
                                    </tr>
                                    <logic:iterate id="result" name="vSearchForm" property="results">
                                        <tr bgcolor="white">
                                            <td>
                                                <logic:notEqual name="result" property="status" value="<%=Constants.PENDING_X%>">
                                                    <%--<input type="radio" id="VID" name="VID" value="<bean:write name="result" property="vectorid"/>">--%>
                                                </logic:notEqual>&nbsp;
                                            </td>
                                            <td align="left">
                                                <html:link href="GetVectorDetail.do" paramId="vectorid" paramName="result" paramProperty="vectorid" target="VectorDetail">
                                                    <bean:write name="result" property="name"/>&nbsp;
                                                </html:link>
                                            </td>
                                            <td><bean:write name="result" property="description"/>&nbsp;</td>
                                        </tr>
                                    </logic:iterate>
                                </table>
                                <p></p>
                                <%--<html:submit value="Continue to Vector Submission Page"/>--%>
                            </logic:notEmpty>
                        </logic:present>
                    </html:form>
                </td>
            </tr>
        </table>
    </div> 
    <jsp:include page="footer.jsp" /></body>
    </div>
</html>

