<%@ page language="java" %>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
    <head>
        <title>Submit vector information - Step 4</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
        <script src="js/common.js"></script>
        <script>
            function showgc() {
                var ogc = document.getElementById("growthcondition");
                if (typeof ogc != 'undefined') {
                    var gc = ogc.options[ogc.selectedIndex].value;
                    if (gc.length > 0) {
                        var u = "gcinfo.jsp?GC=" + encodeURIComponent(gc);
                        window.open(u);
                    } else {
                        alert("Please select a growth condition first.");
                    }
                }
                return false;
            }
            
            function checkHSL() {
                t = document.getElementsByName("HSID");
                for (i=0; i<t.length; i++) {
                    if (t[i].checked) {
                        return true;
                    }
                }
                alert("Please select a host strain before continue.");
                return false;
            }
            
            function checkGCL() {
                t = document.getElementsByName("GCID");
                for (i=0; i<t.length; i++) {
                    if (t[i].checked) {
                        return true;
                    }
                }
                alert("Please select a host growth condition continue.");
                return false;
            }
            
            function checkSML() {
                t = document.getElementsByName("SMID");
                for (i=0; i<t.length; i++) {
                    if (t[i].checked) {
                        return true;
                    }
                }
                alert("Please select a selectable marker before continue.");
                return false;
            }
            
            function checkForm() {
                hs = document.getElementsByName("HSID");
                gc = document.getElementsByName("GCID");
                sm = document.getElementsByName("SMID");
                if ((typeof(hs) == 'undefined') || (hs.length < 1)) {
                    alert("Please enter at least one host strain before continue.")
                    return false;
                }                
                if ((typeof(gc) == 'undefined') || (gc.length < 1)) {
                    alert("Please enter at least one growth condition before continue.")
                    return false;
                }
                if ((typeof(sm) == 'undefined') || (sm.length < 1)) {
                    alert("Please enter at least one selectable marker before continue.")
                    return false;
                }
                
                if (document.getElementById("gciry").value == 'N') {
                    alert("Please enter at least one recommended growth condition before continue.");
                    return false;
                }
                    
                return true;
            }
            
            function chkgciry() {
                var gciry = document.getElementById("gciry");
                if ((gciry.value != 'Y') && (document.forms["vInput4Form"].elements["isrecommended"].value == 'Y'))
                        gciry.value = 'Y';
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
                    <jsp:include page="vInput4Title.jsp" />
                    <html:form action="/vInput4" method="POST">
                        <html:errors/>
                        <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                        <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                        <html:hidden property="step" value="4"/>
                        <h2>Submit Vector Information</h2>
                        <h4><em>Step 4: Host, Growth and Selection</em></h4>
                        <table width="100%" border="0">
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr bgcolor="white" valign="middle">
                                            <td>* At least one host strain, growth condition and selectable marker are required.</td>
                                        </tr>
                                        <tr bgcolor="#9999CC" valign="middle">
                                            <td style="padding: 5px">
                                                <strong>Host Strain: </strong><img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', hstitle, gx(this), gy(this));"/>&nbsp;<html:submit value="Add New Host Strain"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                                    <tr bgcolor="white">
                                                        <td width="13%" align="right">Host Strain:</td>
                                                        <td width="87%" align="left">
                                                            <logic:notPresent name="HS">
                                                                <html:text property="hoststrain"/>
                                                            </logic:notPresent>
                                                            <logic:present name="HS">
                                                                <logic:empty name="HS">
                                                                    <html:text property="hoststrain"/>
                                                                </logic:empty>
                                                                <logic:notEmpty name="HS">
                                                                <select id="hoststrain" name="hoststrain">
                                                                    <logic:iterate id="hs" name="HS">
                                                                        <option value="<bean:write name="hs" />"><bean:write name="hs" /></option>
                                                                    </logic:iterate>
                                                                </select>                                                                    
                                                                </logic:notEmpty>
                                                            </logic:present>
                                                        </td>
                                                    </tr>
                                                    <tr bgcolor="white">
                                                        <td>Description</td>
                                                        <td width="60%">
                                                            <html:text property="description"/>
                                                        </td>
                                                    </tr>
                                                    <tr bgcolor="white"><td colSpan="2"><html:submit value="Add To Host Strain List"/></td></tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                            </td></tr>
                            <logic:present name="VHS">
                            <tr height="18px"><td>&nbsp;</td></tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                        <tr bgcolor="white">
                                            <td width="3%">&nbsp;</td>
                                            <td width="30%"><strong>Host Strain</strong></td>
                                            <td width="67%">Description</td>
                                        </tr>
                                        
                                            <logic:iterate id="vhs" name="VHS" indexId="HSID">
                                                <tr bgcolor="white">
                                                    <td><input type="radio" name="HSID" id="HSID" value="<bean:write name="HSID"/>"/></td>
                                                    <td><bean:write name="vhs" property="hoststrain"/>&nbsp;</td>
                                                    <td><bean:write name="vhs" property="description"/>&nbsp;</td>
                                                </tr>
                                            </logic:iterate>
                                            <tr bgcolor="white"><td colSpan="3"><html:submit value="Remove From Host Strain List" onclick="return checkHSL();"/></td></tr>
                                        
                                    </table>
                            </td></tr>
                            </logic:present>
                            <tr height="18px"><td>&nbsp;</td></tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr bgcolor="#9999CC" valign="middle">
                                            <td style="padding: 5px">
                                                <strong>Growth Condition: </strong><img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', gctitle, gx(this), gy(this));"/>&nbsp;<html:submit value="Add New Growth Condition"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                                    <tr bgcolor="white">                                    
                                                        <td  width="20%" align="right">Growth Condition:</td>
                                                        <td width="80%" align="left">
                                                            <logic:notPresent name="GC">
                                                                <html:text property="growthcondition"/>
                                                            </logic:notPresent>
                                                            <logic:present name="GC">
                                                                <select id="growthcondition" name="growthcondition">
                                                                    <logic:iterate id="gc" name="GC">
                                                                        <option value="<bean:write name="gc" />"><bean:write name="gc" /></option>
                                                                    </logic:iterate>
                                                                </select>
                                                            </logic:present>
                                                            <input type="button" name="gcinfo" id="gcinfo" value="Info" onclick="return showgc();"/>
                                                        </td>
                                                    </tr>
                                                    <tr bgcolor="white">
                                                        <td widht="13%" align="right">Is Recommended?</td>
                                                        <td>
                                                            <html:select property="isrecommended">
                                                                <html:option value="Y">Yes</html:option>
                                                                <html:option value="N">No</html:option>
                                                            </html:select>
                                                        </td>
                                                    </tr>
                                                    <tr bgcolor="white">
                                                        <td colspan="2">
                                                            <html:submit value="Add To Growth Condition List" onclick="return chkgciry();"/>
                                                            <input type="hidden" id="gciry" name="gciry" value="<bean:write name="vInput4Form" property="gciry"/>"/>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                            </td></tr>
                            <logic:present name="VGC">
                            <tr height="18px"><td>&nbsp;</td></tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                        <tr bgcolor="white">
                                            <td width="3%">&nbsp;</td>
                                            <td width="30%"><strong>Growth Condition</strong></td>
                                            <td width="67%"><strong>Is Recommended?</strong></td>
                                        </tr>
                                        
                                            <logic:iterate id="vg" name="VGC" indexId="GCID">
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
                                            <tr bgcolor="white"><td colSpan="3"><html:submit value="Remove From Growth Condition List" onclick="return checkGCL();"/></td></tr>
                                        
                                    </table>
                            </td></tr>
                            </logic:present>
                            <tr height="18px"><td>&nbsp;</td></tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tr bgcolor="#9999CC" valign="middle">
                                            <td style="padding: 5px">
                                                <strong>Selectable Marker: </strong><img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', smtitle, gx(this), gy(this));"/>&nbsp;
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                                    <tr bgcolor="white">
                                                        <td width="11%" align="right">Host Type:</td>
                                                        <td width="89%">
                                                            <logic:notPresent name="HT">
                                                                <html:text property="hosttype"/>
                                                            </logic:notPresent>
                                                            <logic:present name="HT">
                                                                <select id="hosttype" name="hosttype">
                                                                    <logic:iterate id="ht" name="HT">
                                                                        <option value="<bean:write name="ht" />"><bean:write name="ht" /></option>
                                                                    </logic:iterate>
                                                                </select>
                                                            </logic:present>
                                                            <html:submit value="Add New Host Type"/>
                                                        </td>
                                                    </tr>
                                                    <tr bgcolor="white">
                                                        <td width="11%" align="right">Marker:</td>
                                                        <td width="89%">
                                                            <logic:notPresent name="SM">
                                                                <html:text property="marker"/>
                                                            </logic:notPresent>
                                                            <logic:present name="SM">
                                                                <select id="marker" name="marker">
                                                                    <logic:iterate id="sm" name="SM">
                                                                        <option value="<bean:write name="sm" />"><bean:write name="sm" /></option>
                                                                    </logic:iterate>
                                                                </select>
                                                            </logic:present>
                                                            <html:submit value="Add New Marker"/>
                                                        </td>
                                                    </tr>
                                                    <tr bgcolor="white">
                                                        <td colspan="2"><html:submit value="Add To Selectable Marker List"/></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                            </td></tr>
                            <logic:present name="VSM">
                            <tr height="18px"><td>&nbsp;</td></tr>
                            <tr>
                                <td>
                                    <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                                        <tr bgcolor="white">
                                            <td width="3%">&nbsp;</td>
                                            <td width="30%"><strong>Host Type</strong></td>
                                            <td width="67%"><strong>Marker</strong></td>
                                        </tr>
                                        
                                            <logic:iterate id="vsm" name="VSM" indexId="SMID">
                                                <tr bgcolor="white">
                                                    <td><input type="radio" name="SMID" id="SMID" value="<bean:write name="SMID"/>"/></td>
                                                    <td><bean:write name="vsm" property="hosttype"/>&nbsp;</td>
                                                    <td><bean:write name="vsm" property="marker"/>&nbsp;</td>
                                                </tr>
                                            </logic:iterate>
                                            <tr bgcolor="white"><td colSpan="3"><html:submit value="Remove From Select Marker List" onclick="return checkSML();"/></td></tr>
                                        
                                    </table>
                            </td></tr>
                            </logic:present>
                            <tr height="18px"><td>&nbsp;</td></tr>
                        </table>
                        <html:submit value="Continue" onclick="return checkForm();"/>
                        <html:submit value="Back"/>
                        <html:submit value="Save..."/>
                    </html:form>
                </td>
            </tr>
        </table>
        <div id="divTitle"
             onmouseout="showTitle(this.id, null, 0, 0);"
             style="visibility: hidden; position: absolute; float: left; left: 0; top: 0; z-index: 999; border: none 0px black; background-color: #FFFFCC; padding: 10px;">
        </div>
        <span id="hstitle" style="display:none;">
            <p><strong><font size="-1">Host Strain:</font></strong></p>
            <p><font color="#000000" size="-1">We require that these vectors can grow 
                    in T1 phage resistant bacteria. DH5alpha T1 phage-resistant bacteria is 
                    our standard bacterial strain. If your vector CANNOT be grown in this 
            strain please indicate this here and describe the strain in detail below.</font></p>
        </span>
        <span id="gctitle" style="display:none;">
            <p><font color="#000000" size="-1"><strong>Growth Condition:</strong></font></p>
            <p><font size="-1">Growth condition refers to the conditions for growing 
                    this vector in bacteria. If the growth conditions of the vector alone 
                    differ from that of the vector plus the insert, you will have the opportunity 
            to input this information on a later page.</font></p><br>
            <p><font color="#000000" size="-1"><strong>Is Recommended:</strong></font></p>
            <p><font color="#000000" size="-1">Certain vectors can be grown in several 
                    different growth conditions. Please indicate which of these growth conditions 
            you recommend for most experiments.</font></p>            
        </span>
        <span id="smtitle" style="display:none;">
            <p><font color="#000000" size="-1"><strong>Selectable Marker:</strong></font></p>
            <p><font size="-1">Please list all selectable markers for this vector and 
            define in what organism this type of selection would be used. For example:</font></p>
            <table border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                <tr bgcolor="#FFFFCC">
                    <th><font size="-1">Host Type</font></th><th><font size="-1">Marker</font></th>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Mammalian</font></td><td><font size="-1">puromycin</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Bacterial</font></td><td><font size="-1">ampicillin</font></td>
                </tr>
            </table>
        </span>
        <script>
            var hstitle = document.getElementById("hstitle").innerHTML;
            var gctitle = document.getElementById("gctitle").innerHTML;
            var smtitle = document.getElementById("smtitle").innerHTML;
            
            function shdesp(d) {
                document.getElementById(d).style.display="block";
            }
            
            function hddesp(d) {
                document.getElementById(d).style.display="none";
            }
        </script>
        <jsp:include page="footer.jsp" />
    </body>
</html>

