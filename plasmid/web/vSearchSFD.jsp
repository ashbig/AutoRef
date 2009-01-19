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
    </head>
    
    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="vSearchSFDTitle.jsp" />
                    <html:form action="/vSearchSFD">
                        <html:errors/>
                        
                        <h2>Submit Vector For Distribution</h2>
                        
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
                        <logic:notPresent name="vSearchSFDForm" property="results">
                        <center><font color="red"><b>No Vector Found. Please try again.</b></font></center>
                        </logic:notPresent>
                        <logic:present name="vSearchSFDForm" property="results">
                            <logic:empty name="vSearchSFDForm" property="results">
                                <center><font color="red"><b>No Vector Found. Please try again.</b></font></center>
                            </logic:empty>
                            <logic:notEmpty name="vSearchSFDForm" property="results">
                                <p>We found the following vectors. Please click the vector name to review the
                                    detailed information to make sure it is the same vector.</p>
                                <p></p>
                                <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                    <tr bgcolor="white">
                                        <td width="4%">&nbsp;</td>
                                        <td width="16%"><strong>Vector Name</strong></td>
                                        <td width="80%"><strong>Description</strong></td>
                                    </tr>
                                    <logic:iterate id="result" name="vSearchSFDForm" property="results">
                                        <tr bgcolor="white">
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
                                    </logic:iterate>
                                </table>
                                <p></p>
                                <html:submit value="Continue to Vector Submission For Distribution"/>
                            </logic:notEmpty>
                        </logic:present>
                    </html:form>
                </td>
            </tr>
        </table>
        
    <jsp:include page="footer.jsp" /></body>
</html>

