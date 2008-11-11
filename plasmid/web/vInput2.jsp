<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
    <head>
        <title>Submit vector information - Step 2</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script src="js/common.js"></script>
        <script>
            function checkFeatureID() {
                var f = document.getElementsByName("featureid");
                for (var i=0; i<f.length; i++) {
                    if (f[i].checked)
                        return true;
                }
                alert("Please select a feature first.");
                return false;
            }

            function checkForm() {
                var n = document.getElementById("Name").value;
                if (n.length < 1) {
                    alert("Please enter the name before add to list.");
                    return false;
                }
                if (false) {
                    var sb = parseInt(document.getElementById("start").value);
                    if (isNaN(sb) || (sb < 1)) {
                        alert("Please enter an positive nteger as start.");
                        return fasle;
                    }
                    var se = parseInt(document.getElementById("stop").value);
                    if (isNaN(se) || (se < 1)) {
                        alert("Please enter an positive nteger as stop.");
                        return fasle;
                    }
                    if (se < sb) {
                        alert("Start position can not be larger than stop position.");
                        return false;
                    }
                }
            }

            function checkFeatures() {
                var f = document.getElementsByName("featureid");
                if (f.length < 1) {
                    alert("Please add at least one feature before continue.");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    
    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="vInput2Title.jsp" />
                    <html:form action="/vInput2" method="POST">
                        <html:errors/>
                        <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                        <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                        <html:hidden property="fid"/>
                        <html:hidden property="step" value="2"/>
                        <h2>Submit Vector Information</h2>
                        <h4><em>Step 2: Features</em></h4>
                        <table width="100%" border="0">
                            <tr><td><font size="-1">
                        A vector feature is any definable aspect of your vector. This includes the
                            origin of replication, the promoter upstream of the potential insert, the primers
                            used for sequencing, the multiple cloning site (MCS), the method of cloning
                            that can be used to transfer inserts into this vector, the bacterial or mammalian
                            selectable markers etc. Please include any and all vector features in as much
                            detail as possible, because the more information you provide the more information
                        we can give our users.</font>
                        </td></tr>
                        <tr><td>
                                <font size="-1">*Required field is in bold</font>
                        </td></tr>
                        <tr><td>
                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="red">
                            <tr bgcolor="white" valign="top">
                                <td width="20%"><strong>Feature type</strong>: <img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', fttitle, gx(this), gy(this));"/></td>
                                <td width="80%">
                                    <logic:notPresent name="FT">
                                        <html:text property="maptype"/>
                                    </logic:notPresent>
                                    <logic:present name="FT">
                                        <html:select property="maptype">
                                            <html:options name="FT"/>
                                        </html:select>
                                    </logic:present>
                                    <html:submit value="Add New Feature Type"/>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td><strong>Feature name</strong>: <img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', fntitle, gx(this), gy(this));"/> </td>
                                <td width="67%">
                                    <logic:notPresent name="FN">
                                        <html:text property="name"/>
                                    </logic:notPresent>
                                    <logic:present name="FN">
                                        <html:select property="name">
                                            <html:options name="FN"/>
                                        </html:select>
                                    </logic:present>
                                    <html:submit value="Add New Feature Name"/>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td>Description:</td>
                                <td>
                                    <html:textarea property="description" cols="60" rows="6"/>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td>Start position:</td>
                                <td>
                                    <html:text property="start"/>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td>End position:</td>
                                <td>
                                    <html:text property="stop"/>
                                </td>
                            </tr>
                        </table>
                        </td></tr>
                        <tr><td>
                        <html:submit value="Add To List" onclick="return checkForm();"/>
                        </td></tr>
                        <tr><td>
                        
                        <h5><em>List of features:</em></h5>
                        </td></tr>
                        <tr><td>
                        <logic:present name="Features">
                            <table width="100%" border="0">
                                <tr bgcolor="white">
                                    <td>
                                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="green">
                                            <tr bgcolor="white" align="center">
                                                <td width="3%">&nbsp;</td>
                                                <td width="20%">Name</td>
                                                <td width="40%">Description</td>
                                                <td width="23%">Type</td>
                                                <td width="7%">Start</td>
                                                <td width="7%">End</td>
                                            </tr>
                                            <logic:iterate id="feature" name="Features">
                                                <tr bgcolor="white">
                                                    <td><input type="radio" name="featureid" id="featureid" value="<bean:write name="feature" property="featureid"/>"/></td>
                                                    <td><bean:write name="feature" property="name"/></td>
                                                    <td><bean:write name="feature" property="description"/></td>
                                                    <td><bean:write name="feature" property="maptype"/></td>
                                                    <td><bean:write name="feature" property="start"/></td>
                                                    <td><bean:write name="feature" property="stop"/></td>
                                                </tr>
                                            </logic:iterate>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <html:submit value="Edit" onclick="return checkFeatureID();"/>
                                        <html:submit value="Remove" onclick="return checkFeatureID();"/>
                                    </td>
                                </tr>
                                <tr height="18px"><td>&nbsp;</td></tr>
                            </table>
                        </logic:present>
                        </td></tr>
                        <tr><td>
                        <html:submit value="Continue" onclick="return checkFeatures();"/>
                        <html:submit value="Back"/>
                        <html:submit value="Save..."/>
                        </td></tr>
                    </table>
                    </html:form>
                </td>
            </tr>
        </table>
        <div id="divTitle"
             onmouseout="showTitle(this.id, null, 0, 0);"
             style="visibility: hidden; position: absolute; float: left; left: 0; top: 0; z-index: 999; border: none 0px black; background-color: #FFFFCC; padding: 10px;">
        </div>
        <span id="fttitle" style="display:none;">
            <p><strong>Feature type:</strong></p>
            <p><font size="-1">Feature Type refers to the general category that a vector
            feature belongs to. For example: </font></p>
            <table border="0" cellpadding="5" cellspacing="1" bgcolor="red" width="100%">
                <tr bgcolor="#FFFFCC">
                    <th><font size="-1">Feature Type</font></th>
                    <th><font size="-1">Feature Name</font></th>
                    <th><font size="-1">Description</font></th>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Selectable marker</font></td>
                    <td><font size="-1">AmpR</font></td>
                    <td><font size="-1">Ampicillin Resistance</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Tag His</font></td>
                    <td><font size="-1">C-terminal</font></td>
                    <td><font size="-1">His tag</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Protease cleavage site</font></td>
                    <td><font size="-1">TEV</font></td>
                    <td><font size="-1">TEV protease cleavage site</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Recombination site</font></td>
                    <td><font size="-1">attL1</font></td>
                    <td><font size="-1">Gateway attL1 recombination site</font></td>
                </tr>
            </table>
            <p><font size="-1">If you don't think your feature type is listed,
            add a new feature type to the list.</font></p>
        </span>
        <span id="fntitle" style="display:none;">
            <p><strong>Feature name:</strong></p>
            <p><font size="-1">Feature Name refers to the specific feature of the
            plasmid that you are describing. For example:</font></p>
            <table border="0" cellpadding="5" cellspacing="1" bgcolor="red" width="100%">
                <tr bgcolor="#FFFFCC">
                    <th><font size="-1">Feature Type</font></th>
                    <th><font size="-1">Feature Name</font></th>
                    <th><font size="-1">Description</font></th>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Selectable marker</font></td>
                    <td><font size="-1">AmpR</font></td>
                    <td><font size="-1">Ampicillin Resistance</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Tag His</font></td>
                    <td><font size="-1">C-terminal</font></td>
                    <td><font size="-1">His tag</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Protease cleavage site</font></td>
                    <td><font size="-1">TEV</font></td>
                    <td><font size="-1">TEV protease cleavage site</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Recombination site</font></td>
                    <td><font size="-1">attL1</font></td>
                    <td><font size="-1">Gateway attL1 recombination site</font></td>
                </tr>
            </table>
            <p><font size="-1">Please include all the features that you know about,
                and add new features if your particular feature name isn't listed. </font>
            </p>
        </span>
        <script>
            var fttitle = document.getElementById("fttitle").innerHTML;
            var fntitle = document.getElementById("fntitle").innerHTML;
        </script>
        <jsp:include page="footer.jsp" />
    </body>
</html>

