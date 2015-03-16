<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
    <head>
        <title>Submit vector information - Step 1</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script src="js/common.js"></script>
        <script>
            function checkForm() {
                var n = document.getElementById("Name").value;
                if (n.length < 1) {
                    alert("Please enter the name before continue.");
                    return false;
                }
                var f = document.getElementById("form").value;
                if (f.length < 1) {
                    alert("Please select the form before continue.");
                    return false;
                }
                var t = document.getElementById("type").value;
                if (t.length < 1) {
                    alert("Please select the type before continue.");
                    return false;
                }
                var s = document.getElementById("size").value;
                if (s.length > 0) {
                    if (isNaN(parseInt(s)) || (s.indexOf(".") > 0) || (s.indexOf("-") > 0) || (s.indexOf("+") > 0) || (s.indexOf("E") > 0)) {
                        alert("Please size in integer before continue.");
                        return false;
                    }
                }
                return true;
            }
        </script>
    </head>
    
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="vInputTitle.jsp" />
                    <html:form action="/vInput" method="POST" enctype="multipart/form-data">
                        <html:errors/>
                        <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                        <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                        <html:hidden property="step" value="1"/>
                        <h2>Submit Vector Information</h2>
                        <h4><em>Step 1: Basic information</em></h4>
                        <p>*Required field is in bold</p>
                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                            <tr bgcolor="white" valign="top">
                                <td width="15%" align="right"><strong>Name</strong>:</td>
                                <td width="85%">
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:present name="Vector">
                                            <html:text name="Vector" property="name" maxlength="50" size="50"/>
                                        </logic:present>
                                        <logic:notPresent name="Vector">
                                            <html:text property="name" maxlength="50" size="50"/>
                                        </logic:notPresent>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <html:text property="name" maxlength="50" size="50"/>
                                    </logic:notEmpty>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td width="15%" align="right">Synonyms:</td>
                                <td width="85%">
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:present name="Vector">
                                            <html:text name="Vector" property="syns" maxlength="200" size="50"/>(separate synonyms by comma (,))
                                        </logic:present>
                                        <logic:notPresent name="Vector">
                                            <html:text property="syns" maxlength="200" size="50"/>(separate synonyms by comma (,))
                                        </logic:notPresent>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <html:text property="syns" maxlength="200" size="50"/>(separate synonyms by comma (,))
                                    </logic:notEmpty>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right"><strong>Form</strong>:</td>
                                <td>
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:notPresent name="VForms">
                                            <logic:present name="Vector">
                                                <html:text name="Vector" property="form"/>
                                            </logic:present>
                                            <logic:notPresent name="Vector">
                                                <html:text property="form"/>
                                            </logic:notPresent>
                                        </logic:notPresent>
                                        <logic:present name="VForms">
                                            <logic:present name="Vector">
                                                <html:select name="Vector" property="form">
                                                    <html:options name="VForms"/>
                                                </html:select>
                                            </logic:present>
                                            <logic:notPresent name="Vector">
                                                <html:select property="form">
                                                    <html:options name="VForms"/>
                                                </html:select>
                                            </logic:notPresent>
                                        </logic:present>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <logic:notPresent name="VForms">
                                            <html:text property="form"/>
                                        </logic:notPresent>
                                        <logic:present name="VForms">
                                            <html:select property="form">
                                                <html:options name="VForms"/>
                                            </html:select>
                                        </logic:present>
                                    </logic:notEmpty>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right"><strong>Type</strong>:</td>
                                <td>
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:notPresent name="VTypes">
                                            <logic:present name="Vector">
                                                <html:text name="Vector" property="type"/>
                                            </logic:present>
                                            <logic:notPresent name="Vector">
                                                <html:text property="type"/>
                                            </logic:notPresent>
                                        </logic:notPresent>
                                        <logic:present name="VTypes">
                                            <logic:present name="Vector">
                                                <html:select name="Vector" property="type">
                                                    <html:options name="VTypes"/>
                                                </html:select>
                                            </logic:present>
                                            <logic:notPresent name="Vector">
                                                <html:select property="type">
                                                    <html:options name="VTypes"/>
                                                </html:select>
                                            </logic:notPresent>
                                        </logic:present>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <logic:notPresent name="VTypes">
                                            <html:text property="type"/>
                                        </logic:notPresent>
                                        <logic:present name="VTypes">
                                            <html:select property="type">
                                                <html:options name="VTypes"/>
                                            </html:select>
                                        </logic:present>
                                    </logic:notEmpty>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right">Size in bp:</td>
                                <td>
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:present name="Vector">
                                            <html:text name="Vector" property="size" maxlength="50" size="50"/>
                                        </logic:present>
                                        <logic:notPresent name="Vector">
                                            <html:text property="size" maxlength="50" size="50"/>
                                        </logic:notPresent>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <html:text property="size" maxlength="50" size="50"/>
                                    </logic:notEmpty>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right">Map:</td>
                                <td>
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:present name="Vector">
                                            <logic:notEmpty name="Vector" property="mapfilename">
                                                <font size="-1">Map file <bean:write name="Vector" property="mapfilename"/> has been uploaded.</font><br>
                                            </logic:notEmpty>
                                        </logic:present>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <logic:notEmpty name="vInputForm" property="mapfilename">
                                            <font size="-1">Map file <bean:write name="vInputForm" property="mapfilename"/> has been uploaded.</font><br>
                                        </logic:notEmpty>
                                    </logic:notEmpty>
                                    <html:file property="mapfile" maxlength="128" size="50"/>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right">Sequence:</td>
                                <td>
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:present name="Vector">
                                            <logic:notEmpty name="Vector" property="seqfilename">
                                                <font size="-1">Squence file <bean:write name="Vector" property="seqfilename"/> has been uploaded.</font><br>
                                            </logic:notEmpty>
                                        </logic:present>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <logic:notEmpty name="vInputForm" property="seqfilename">
                                            <font size="-1">Squence file <bean:write name="vInputForm" property="seqfilename"/> has been uploaded.</font><br>
                                        </logic:notEmpty>
                                    </logic:notEmpty>
                                    <html:file property="seqfile" maxlength="128" size="50"/>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right">Comments:</td>
                                <td>
                                    <logic:empty name="vInputForm" property="step">
                                        <logic:present name="Vector">
                                            <html:textarea name="Vector" property="comments" cols="38" rows="5"/>
                                        </logic:present>
                                        <logic:notPresent name="Vector">
                                            <html:textarea property="comments" cols="38" rows="5"/>
                                        </logic:notPresent>
                                    </logic:empty>
                                    <logic:notEmpty name="vInputForm" property="step">
                                        <html:textarea property="comments" cols="38" rows="5"/>
                                    </logic:notEmpty>
                                </td>
                            </tr>
                        </table>
                        <div id="divTitle"
                             onmouseout="showTitle(this.id, null, 0, 0);"
                             style="visibility: hidden; position: absolute; float: left; left: 0; top: 0; z-index: 999; border: none 0px black; background-color: #FFFFCC; padding: 10px;">
                        </div>
                        <p></p>
                        <html:submit value="Continue" onclick="return checkForm();"/>
                        <html:submit value="Save..."/>
                        <html:submit value="Cancel"/>
                    </html:form>
                </td>
            </tr>
        </table>
        
    <jsp:include page="footer.jsp" /></body>
</html>

