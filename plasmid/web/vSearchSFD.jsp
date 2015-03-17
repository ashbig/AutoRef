<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %>

<html>
    <head>
        <title>Search vector by name for distribution</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script src="js/common.js"></script>
        <script>
            function checkForm2() {
                vid = document.getElementsByName("VID") ;
                if ((typeof(vid) == 'undefined') || (vid.length < 1)) {
                    alert("Please select a vector before continue.")
                    return false;
                }                
                for (i = 0; i < vid.length; i++) {
                    if (vid[i].checked)
                        return true;
                }
                alert("Please select a vector before continue.")
                return false;
            }
            
        </script>
<link href="layout.css" rel="stylesheet" type="text/css" />
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
        <!--<script type="text/javascript" src="SpryMenuBar.js"></script>  code from dreamweaver doesn't work, submenu action re-done with css  
        <script type="text/javascript">
        var MenuBar1 = new Spry.Widget.MenuBar("MenuBar1", {imgDown:"SpryMenuBarDownHover.gif", imgRight:"SpryMenuBarRightHover.gif"});</script> -->
</head>
<div class="gridContainer clearfix">

    
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="vSearchSFDTitle.jsp" />--%>
                    <html:form action="/vSearchSFD">
                        <html:errors/>
                        
                        <p class='mainbodytexthead'>Submit Vector For Distribution</p>
                        
                        <p>Please search for existing vector submitted for distribution.</p>
                        <p><em>If you have any questions, please contact the <a href="mailto:Catherine_Cormier@hms.harvard.edu">scientific liaison</a></em>.</p>
                        <p>Before you start submitting the vector for distribution, please check whether the
                        vector has existed in our database. Search is NOT case sensitive. </p>
                        <table width="100%" border="0">
                            <tr valign="top">
                                <td width="15%"><strong>Vector Name</strong>:</td>
                                <td width="85%">
                                    <html:text property="VN"/>
                                    <html:submit value=" Find "/>
                                    <html:radio property="search" value="Y"/>Exact Match&nbsp;&nbsp;&nbsp;
                                    <html:radio property="search" value="N"/>Non-exact Match
                                </td>
                            </tr>
                        </table>
                    </html:form>
                    <p></p>
                    <html:form action="/continueVSFD">
                        <logic:notEmpty name="vSearchSFDForm" property="VN">
                            <logic:notPresent name="SFDR">
                                <center><font color="red"><b>No Vector Found. Please try again.</b></font></center>
                            </logic:notPresent>
                            <logic:present name="SFDR">
                                <logic:empty name="SFDR">
                                    <center><font color="red"><b>No Vector Found. Please try again.</b></font></center>
                                </logic:empty>
                                <logic:notEmpty name="SFDR">
                                    <p>We found the following vectors. Please click the vector name to review the
                                    detailed information to make sure it is the same vector.</p>
                                    <p></p>
                                    <table>
                                        <tr><td bgcolor="#FFFFFCC" width="20px"></td><td>&nbsp;&nbsp;Vector submission has not been finished. Please continue <a href="vSearch.jsp">vector submission</a> before continue.</td></tr>
                                        <tr><td bgcolor="#FFCCCC" width="20px"></td><td>&nbsp;&nbsp;This vector has been submitted for distribution as empty vector.</td></tr>
                                        <tr><td bgcolor="#CCFFCC" width="20px"></td><td>&nbsp;&nbsp;This vector can be submitted for distribution.</td></tr>
                                    </table>
                                    
                                    <p></p>
                                    <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                        <tr bgcolor="white">
                                            <td width="4%">&nbsp;</td>
                                            <td width="16%"><strong>Vector Name</strong></td>
                                            <td width="80%"><strong>Description</strong></td>
                                        </tr>
                                        <logic:iterate id="result" name="SFDR">
                                            <logic:equal name="result" property="status" value="PENDING">
                                                <tr bgcolor="#FFFFFCC">
                                                    <td>
                                                        &nbsp;
                                                    </td>
                                                    <td align="left">
                                                        <html:link href="GetVectorDetail.do" paramId="vectorid" paramName="result" paramProperty="vectorid" target="VectorDetail">
                                                            <bean:write name="result" property="name"/>&nbsp;
                                                        </html:link>
                                                    </td>
                                                    <td><bean:write name="result" property="description"/>&nbsp;</td>
                                                </tr>
                                            </logic:equal>
                                            <logic:notEqual name="result" property="status" value="PENDING">
                                                <logic:equal name="result" property="cloneid" value="-1">
                                                    <tr bgcolor="#CCFFCC">
                                                        <td>
                                                            <input type="radio" id="VID" name="VID" value="<bean:write name="result" property="vectorid"/>">&nbsp;
                                                        </td>
                                                        <td align="left">
                                                            <html:link href="GetVectorDetail.do" paramId="vectorid" paramName="result" paramProperty="vectorid" target="VectorDetail">
                                                                <bean:write name="result" property="name"/>&nbsp;
                                                            </html:link>
                                                        </td>
                                                        <td><bean:write name="result" property="description"/>&nbsp;</td>
                                                    </tr>
                                                </logic:equal>
                                                <logic:notEqual name="result" property="cloneid" value="-1">
                                                    <tr bgcolor="#FFCCCC">
                                                        <td>&nbsp;</td>
                                                        <td align="left">
                                                            <html:link href="GetVectorDetail.do" paramId="vectorid" paramName="result" paramProperty="vectorid" target="VectorDetail">
                                                                <bean:write name="result" property="name"/>&nbsp;
                                                            </html:link>
                                                        </td>
                                                        <td><bean:write name="result" property="description"/>&nbsp;</td>
                                                    </tr>
                                                </logic:notEqual>
                                            </logic:notEqual>
                                        </logic:iterate>
                                    </table>
                                    <p></p>
                                    <html:submit value="Continue to Vector Submission For Distribution" onclick="return checkForm2();"/>
                                </logic:notEmpty>
                            </logic:present>
                        </logic:notEmpty>
                    </html:form>
                </td>
            </tr>
        </table>
        
    <jsp:include page="footer.jsp" /></body>
</div>
</html>

