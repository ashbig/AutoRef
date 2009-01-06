<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %>
<%@ page import="plasmid.coreobject.VectorProperty" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>Submit vector information - Step 3</title>
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
                    <jsp:include page="vInput3Title.jsp" />
                    <html:form action="/vInput3" method="POST">
                        <html:errors/>
                        <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                        <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                        <html:hidden property="step" value="3"/>
                        <h2>Submit Vector Information</h2>
                        <h4><em>Step 3: Properties</em></h4>
                        <p>Properties are a set of features that define your vector. This subset of features
                            will be used for specific searches. Please check the box of all of the features
                        that apply to your vector (you can check multiple boxes). </p>
                        <p>*Required field is in bold</p>
                        <table width="100%" border="0">
                            <tr><td height="12px"></td></tr>
                            <tr bgcolor="#6666EE"><td>Assay&nbsp;</td></tr>
                            <logic:present name="VPA">
                                <logic:iterate id="VP" name="VPA" indexId="VPID">
                                    <tr>
                                        <td>
                                            <logic:equal name="VP" property="vectorid" value="0">
                                                <input type="checkbox" name="VPA" id="VPA" value="<bean:write name="VPID"/>"/>
                                            </logic:equal>
                                            <logic:greaterThan name="VP" property="vectorid" value="0">
                                                <input type="checkbox" name="VPA" id="VPA" value="<bean:write name="VPID"/>" checked/>
                                            </logic:greaterThan>
                                            <bean:write name="VP" property="displayValue"/>
                                        </td>
                                    </tr>
                                </logic:iterate>
                            </logic:present>
                            <tr><td height="12px"></td></tr>
                            <tr bgcolor="#6666EE"><td>Cloning System&nbsp;</td></tr>
                            <logic:present name="VPC">
                                <logic:iterate id="VP" name="VPC" indexId="VPID">
                                    <tr>
                                        <td>
                                            <logic:equal name="VP" property="vectorid" value="0">
                                                <input type="checkbox" name="VPC" id="VPC" value="<bean:write name="VPID"/>"/>
                                            </logic:equal>
                                            <logic:greaterThan name="VP" property="vectorid" value="0">
                                                <input type="checkbox" name="VPC" id="VPC" value="<bean:write name="VPID"/>" checked/>
                                            </logic:greaterThan>
                                            <% out.println(((VectorProperty) VP).getDisplayValue()); %>
                                        </td>
                                    </tr>
                                </logic:iterate>
                            </logic:present>
                            <tr><td height="12px"></td></tr>
                            <tr bgcolor="#6666EE"><td>Expression&nbsp;</td></tr>
                            <logic:present name="VPE">
                                <logic:iterate id="VP" name="VPE" indexId="VPID">
                                    <tr>
                                        <td>
                                            <logic:equal name="VP" property="vectorid" value="0">
                                                <input type="checkbox" name="VPE" id="VPE" value="<bean:write name="VPID"/>"/>
                                            </logic:equal>
                                            <logic:greaterThan name="VP" property="vectorid" value="0">
                                                <input type="checkbox" name="VPE" id="VPE" value="<bean:write name="VPID"/>" checked/>
                                            </logic:greaterThan>
                                            <bean:write name="VP" property="displayValue"/>
                                        </td>
                                    </tr>
                                </logic:iterate>
                            </logic:present>
                            <tr height="18px" colspan="3"><td>&nbsp;</td></tr>
                        </table>
                        <div id="divTitle"
                             onmouseout="showTitle(this.id, null, 0, 0);"
                             style="visibility: hidden; position: absolute; float: left; left: 0; top: 0; z-index: 999; border: none 0px black; background-color: #FFFFCC; padding: 10px;">
                        </div>
                        <html:submit value="Continue"/>
                        <html:submit value="Back"/>
                        <html:submit value="Save..."/>
                    </html:form>
                </td>
            </tr>
        </table>

    <jsp:include page="footer.jsp" /></body>
</html>
