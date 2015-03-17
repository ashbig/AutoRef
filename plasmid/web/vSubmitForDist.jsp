<%@ page language="java" %>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
    <head>
        <title>Submit Vector For Distribution</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script src="js/common.js"></script>
    </head>
    
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr>
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle">
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="vSubmitForDistTitle.jsp" />
                    <logic:present name="Clone">
                        <p></p>
                        <p>Clone vector <bean:write name="Clone" property="name"/> has been successfully submitted for distribution. <br>
                            The Clone ID is <bean:write name="Clone" property="cloneid"/>.<br>
                        Thank you.</p>
                    </logic:present>
                    <logic:notPresent name="Clone">
                        <html:form action="/vSubmitForDist" method="POST">
                            <html:errors/>
                            <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                            <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                            <html:hidden property="step" value="9"/>
                            <h2>Submit Vector For Distribution</h2>
                            <p><em>If you would like to distribute this vector through us, please fill out
                            the following page.</em></p>
                            <p>*Required field is in bold</p>
                            <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                <tr bgcolor="white">
                                    <td align="right" width="21%"><strong>Verified</strong>:<img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', vrftitle, gx(this), gy(this));"/>&nbsp;</td>
                                    <td width="79%">
                                        <html:radio property="verified" value="Y"/>Yes&nbsp;&nbsp; Verified method &nbsp;<html:text property="verifiedmethod" size="50"/><br>
                                        <html:radio property="verified" value="N"/>No
                                    </td>
                                </tr>
                                <tr bgcolor="white" valign="top">
                                    <td align="right"><strong>Source</strong>:<img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', srctitle, gx(this), gy(this));"/>&nbsp;</td>
                                    <td>
                                        <html:radio property="source" value="Non-PSI"/>Non-PSI:&nbsp;&nbsp;<html:text property="nonpsi"  size="50"/><br>
                                        <html:radio property="source" value="PSI"/>PSI:&nbsp;&nbsp;
                                        <logic:present name="PSIC">
                                            <html:select property="psi">
                                                <html:options name="PSIC"/>
                                            </html:select>
                                        </logic:present>
                                        <logic:notPresent name="PSIC">
                                            <html:text property="psi" size="50"/>
                                        </logic:notPresent>
                                        
                                    </td>
                                </tr>
                                <tr bgcolor="white" valign="top">
                                    <td align="right"><strong>Comments:</strong></td>
                                    <td>&nbsp;
                                        <html:textarea property="comments" cols="68" rows="5"/>
                                    </td>
                                </tr>
                            </table>
                            <table border="0" width="100%">
                                <tr height="24px"><td></td></tr>
                                
                                <tr><td>
                                        <strong>Growth Condition for vector alone: </strong>
                                </td></tr>
                                <tr height="8px"><td></td></tr>
                                <tr><td>
                                        <strong><font size="-1">If the growth condition is different from the growth condition of the
                                        vector with insert, please indicate below:</font></strong>
                                </td></tr>
                                <tr><td>
                                        <input type="checkbox" id="sameasvector" name="sameasvector" onclick="sameVGC();"/>Same as vector with insert
                                </td></tr>
                                <tr><td>
                                        <span id="vgca" name="vgca" style="display:block;">
                                            <table width="100%" border="0">
                                                <tr><td>
                                                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                                            <tr bgcolor="white">
                                                                <td width="20%" align="right">
                                                                    Growth Condition:&nbsp;
                                                                </td>
                                                                <td width="80%">&nbsp;
                                                                    <logic:present name="GC">
                                                                        <html:select property="growthcondition">
                                                                            <html:options name="GC"/>
                                                                        </html:select>
                                                                    </logic:present>
                                                                    <logic:notPresent name="GC">
                                                                        <html:text property="growthcondition" size="50"/>
                                                                    </logic:notPresent>
                                                                    <input type="button" value="Info" onclick="return showGC();"/>
                                                                    <html:submit value="Add New Growth Condition"/>
                                                                </td>
                                                            </tr>
                                                            <tr bgcolor="white">
                                                                <td align="right">Is Recommended?&nbsp;</td>
                                                                <td>&nbsp;
                                                                    <select id="isrecommended" name="isrecommended">
                                                                        <option value="Y">Yes</option>
                                                                        <option value="N">No</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                </td></tr>
                                                <tr bgcolor="white" height="2px"><td></td></tr>
                                                <tr bgcolor="white"><td><html:submit value="Add To List" onclick="return checkFormAdd();"/></td></tr>
                                                <logic:present name="VGCA">
                                                    <tr bgcolor="white" height="12px"><td></td></tr>
                                                    <tr bgcolor="white"><td>
                                                            <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                                                <tr bgcolor="white">
                                                                    <th width="3%">&nbsp;</th>
                                                                    <th width="45%"><strong>Growth Condition</strong></th>
                                                                    <th width="44%"><strong>Is Recommended</strong></th>
                                                                </tr>
                                                                
                                                                <logic:iterate id="vg" name="VGCA" indexId="GCID">
                                                                    <tr bgcolor="white">
                                                                        <td><input type="radio" name="GCID" id="GCID" value="<bean:write name="GCID"/>"/></td>
                                                                        <td><bean:write name="vg" property="growthname"/>&nbsp;</td>
                                                                        <td>
                                                                            <logic:equal name="vg" property="isrecommended" value="Y">
                                                                                Yes
                                                                            </logic:equal>
                                                                            <logic:equal name="vg" property="isrecommended" value="N">
                                                                                No
                                                                            </logic:equal>
                                                                            &nbsp;
                                                                        </td>
                                                                    </tr>
                                                                </logic:iterate>
                                                            </table>
                                                    </td></tr>
                                                    <tr bgcolor="white" height="2px"><td></td></tr>
                                                    <tr bgcolor="white"><td><html:submit value="Remove From List" onclick="return checkFormRemove();"/></td></tr>
                                                </logic:present>
                                            </table>
                                        </span>
                                </td></tr>
                                <tr bgcolor="white" height="24px"><td></td></tr>
                                <tr bgcolor="white"><td>
                                        <strong>Growth Condition for vector with insert:</strong>
                                </td></tr>
                                <tr bgcolor="white"><td>
                                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                            <tr bgcolor="white">
                                                <th width="80%"><strong>Growth Condition</strong></th>
                                                <th width="20%"><strong>Is Recommended</strong></th>
                                            </tr>
                                            <logic:present name="VGC">
                                                <logic:iterate id="vg" name="VGC">
                                                    <tr bgcolor="white">
                                                        <td><bean:write name="vg" property="growthname"/>&nbsp;</td>
                                                        <td>
                                                            <logic:equal name="vg" property="isrecommended" value="Y">
                                                                Yes
                                                            </logic:equal>
                                                            <logic:equal name="vg" property="isrecommended" value="N">
                                                                No
                                                            </logic:equal>
                                                            &nbsp;
                                                        </td>
                                                    </tr>
                                                </logic:iterate>
                                            </logic:present>
                                        </table>
                                </td></tr>
                                <tr bgcolor="white" height="24px"><td></td></tr>
                                <tr bgcolor="white"><td>
                                        <html:submit value="Submit" onclick="return checkForm();"/>
                                </td></tr>
                            </table>
                        </html:form>
                    </logic:notPresent>
                </td>
            </tr>
        </table>
        <logic:notPresent name="Clone">
            <div id="divTitle"
                 onmouseout="showTitle(this.id, null, 0, 0);"
                 style="visibility: hidden; position: absolute; float: left; left: 0; top: 0; z-index: 999; border: none 0px black; background-color: #FFFFCC; padding: 10px;">
            </div>
            <span id="verifiedtitle" style="display:none;">
                Indicate if the vector has been verified by sequencing or restriction digest. Indicate which method in the "Verification Method" box.
            </span>
            <span id="sourcetitle" style="display:none;">
                If you are submitting from a PSI site, please input your
                institution in this field. If you are non-PSI, please input the laboratory
                PI where this vector was produced.
            </span>
            <script>
                var vrftitle = document.getElementById("verifiedtitle").innerHTML;
                var srctitle = document.getElementById("sourcetitle").innerHTML;
                if (document.getElementById("sameasvector").checked)
                    document.getElementById("vgca").style.display = "block";

                function checkFormAdd() {
                    return true;
                }
                function checkFormRemove() {
                    v = document.getElementsByName("GCID");
                    for (i=0; i<v.length; i++) {
                        if (v[i].checked)
                            return true;
                    }
                    alert("Please select a growth condition before continue.");
                    return false;
                }
                function showGC() {
                    var ogc = document.forms["vSubmitForDistForm"].elements["growthcondition"];
                    if (typeof ogc != 'undefined') {
                        var gc = ogc.options[ogc.selectedIndex].value;
                        if (gc.length > 0) {
                            var u = "gcinfo.jsp?GC=" + gc;
                            window.open(u);
                        } else {
                            alert("Please select a growth condition first.");
                        }
                    }
                    return false;
                }
                function sameVGC() {
                    c = document.getElementById("sameasvector");
                    t = document.getElementById("vgca");

                    if (c.checked) {
                        t.style.display = "none";
                    } else {
                        t.style.display = "block";
                    }
                }
                function checkForm() {
                    v = document.getElementsByName("verified");
                    vm = document.getElementById("verifiedmethod").value;

                    if (!v[0].checked && !v[1].checked) {
                        alert("Please select verify result before continue.");
                        return false;
                    }
                    if ((v[0].checked) && ((vm == null) || (vm.length < 1))) {
                        alert("Please enter verified method before continue.");
                        return false;
                    }

                    s = document.getElementsByName("source");
                    np = document.getElementById("nonpsi").value;
                    p = document.getElementById("psi").value;
                    if (!s[0].checked && !s[1].checked) {
                        alert("Please select source before continue.");
                        return false;
                    }
                    if ((s[0].checked) && (np.length < 1)) {
                        alert("Please enter laboratory PI before continue.");
                        return false;
                    }
                    if ((s[1].checked) && (p.length < 1)) {
                        alert("Please enter institution before continue.");
                        return false;
                    }

                    c = document.getElementById("sameasvector");
                    if (!c.checked) {
                        g = document.getElementsByName("GCID");
                        if ((g == 'undefined') || (g.length < 1)) {
                            alert("Please add at least one growth condition for vector alone before continue.");
                            return false;
                        }
                    }
                    return true;
                }
            </script>
        </logic:notPresent>
        <jsp:include page="footer.jsp" />
    </body>
</html>

