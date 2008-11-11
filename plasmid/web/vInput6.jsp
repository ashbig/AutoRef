<%@ page language="java" %>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
    <head>
        <title>Submit vector information - Step 6</title>
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
                    <jsp:include page="vInput6Title.jsp" />
                    <html:form action="/vInput6" method="POST">
                        <html:errors/>
                        <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                        <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                        <logic:present name="Author"><html:hidden name="Author" property="authorid"/></logic:present>
                        <logic:notPresent name="Author"><html:hidden property="authorid" value="0"/></logic:notPresent>
                        <html:hidden property="step" value="6"/>
                        <h2>Submit Vector Information</h2>
                        <h4><em>Step 6: Authors</em></h4>
                        <p>List all the authors who were responsible for the creation or modification
                            of this vector. <em>Note: Please do not list researchers who subsequently used
                        this vector for cloning or additional experiments. </em></p>
                        <p>*Required field is in bold</p>
                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="red">
                            <tr bgcolor="white">
                                <td width="21%" align="right"><strong>Name</strong>: </td>
                                <td width="79%">
                                    <logic:present name="Author">
                                        <html:text name="Author" property="name"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:text property="name"/>&nbsp;
                                    </logic:notPresent>
                                    <html:submit value="Find" onclick="return checkForm1();"/>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td width="21%" align="right">Fist Name: </td>
                                <td width="79%">
                                    <logic:present name="Author">
                                        <html:text name="Author" property="firstname"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:text property="firstname"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td width="21%" align="right">Last Name: </td>
                                <td width="79%">
                                    <logic:present name="Author">
                                        <html:text name="Author" property="lastname"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:text property="lastname"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                
                                <td align="right">Email:</td>
                                <td>
                                    <logic:present name="Author">
                                        <html:text name="Author" property="email"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:text property="email"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td align="right">Phone:</td>
                                <td>
                                    <logic:present name="Author">
                                        <html:text name="Author" property="tel"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:text property="tel"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td align="right">Fax:</td>
                                <td>
                                    <logic:present name="Author">
                                        <html:text name="Author" property="fax"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:text property="fax"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td align="right">Website:</td>
                                <td>
                                    <logic:present name="Author">
                                        <html:text name="Author" property="www" maxlength="256" size="66"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:text property="www" maxlength="256" size="66"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right">Address:</td>
                                <td>
                                    <logic:present name="Author">
                                        <html:textarea name="Author" property="address" cols="50" rows="5"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:textarea property="address" cols="50" rows="5"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white" valign="top">
                                <td align="right">Description:</td>
                                <td>
                                    <logic:present name="Author">
                                        <html:textarea name="Author" property="description" cols="50" rows="5"/>&nbsp;
                                    </logic:present>
                                    <logic:notPresent name="Author">
                                        <html:textarea property="description" cols="50" rows="5"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td id="at" align="right">Author Type:<img src="img/info.jpg" border="0" onmouseover="javascript: showTitle('divTitle', attitle, gx(this), gy(this));"/>&nbsp;</td>
                                <td>
                                    <html:select property="authortype">
                                        <html:option value="Academic researcher, vector creator">Academic researcher, vector creator</html:option>
                                        <html:option value="Academic researcher, vector PI">Academic researcher, vector PI</html:option>
                                        <html:option value="Academic researcher, vector donor">Academic researcher, vector donor</html:option>
                                        <html:option value="Academic researcher, first author">Academic researcher, first author</html:option>
                                        <html:option value="Commercial Vector">Commercial Vector</html:option>
                                    </html:select>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td colSpan="2">
                                    <html:submit value="Add To List" onclick="return checkForm2();"/>
                                    <input type="button" value="Clear" onclick="return clearForm(false);"/>
                                </td>
                            </tr>
                        </table>
                        <p></p>
                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="green">
                            <tr bgcolor="white">
                                <th width="3%">&nbsp;</th>
                                <th width="27%"><strong>Author Name</strong></th>
                                <th width="48%"><strong>Author Type</strong></th>
                                <th width="25%">Creation Date</th>
                            </tr>
                            <logic:present name="VAs">
                                <logic:iterate id="VA" name="VAs" indexId="VAID">
                                    <tr bgcolor="white">
                                        <td><input type="radio" name="VAID" id="VAID" value="<bean:write name="VAID"/>"></td>
                                        <td>
                                            <bean:write name="VA" property="name"/>&nbsp;
                                        </td>
                                        <td>
                                            <bean:write name="VA" property="authortype"/>&nbsp;
                                        </td>
                                        <td>
                                            <bean:write name="VA" property="creationdate"/>&nbsp;
                                        </td>
                                    </tr>
                                </logic:iterate>
                                <tr bgcolor="white"><td colSpan="4"><html:submit value="Remove From List" onclick="return checkForm();"/></td></tr>
                            </logic:present>
                        </table>
                        <p></p>
                        <html:submit value="Continue" onclick="return checkNext();"/>
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
        <span id="attitle" style="display:none;">
            <p><strong><font size="-1">Author Type:</font></strong></p>
            <p><font size="-1">This refers to the role of each author in the creation
            of the vector. </font> </p>
            <table border="0" cellpadding="5" cellspacing="1" bgcolor="green">
                <tr bgcolor="#FFFFCC">
                    <th><font size="-1">Author type</font></th>
                    <th><font size="-1">Role</font></th>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Academic researcher, vector creator</font></td>
                    <td><font size="-1">Made or modified the vector</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Academic researcher, vector PI</font></td>
                    <td><font size="-1">Vector was made in this individual's laboratory</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Academic researcher, vector donor</font></td>
                    <td><font size="-1">Vector was made by another laboratory
                            but donated by this individual. Note: if this is the case, please indicate
                    who made the vector</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Academic researcher, first author</font></td>
                    <td><font size="-1">First author of any publications resulting
                            from the creation of this vector. Note: do not include the author of publications
                    that include the vector plus an insert</font></td>
                </tr>
                <tr bgcolor="#FFFFCC">
                    <td><font size="-1">Commercial Vector</font></td>
                    <td><font size="-1">Created by a commercial entity.<br>Note: if vector was
                            made by a company please check for any potential licensing issues with
                    your office of technology transfer</font></td>
                </tr>
            </table>
        </span>
        
        <jsp:include page="footer.jsp" />
        <script>
            var attitle = document.getElementById("attitle").innerHTML;

            if (parseInt(a) > 0) {
                showInput(true);
            } else {
                showInput(false);
            }

            function showInput(t) {
                if (t) {
                    document.getElementById("name").readOnly = true;
                    document.getElementById("firstname").readOnly = true;
                    document.getElementById("lastname").readOnly = true;
                    document.getElementById("email").readOnly = true;
                    document.getElementById("tel").readOnly = true;
                    document.getElementById("fax").readOnly = true;
                    document.getElementById("www").readOnly = true;
                    document.getElementById("address").readOnly = true;
                    document.getElementById("description").readOnly = true;
                } else {
                    document.getElementById("name").readOnly = false;
                    document.getElementById("firstname").readOnly = false;
                    document.getElementById("lastname").readOnly = false;
                    document.getElementById("email").readOnly = false;
                    document.getElementById("tel").readOnly = false;
                    document.getElementById("fax").readOnly = false;
                    document.getElementById("www").readOnly = false;
                    document.getElementById("address").readOnly = false;
                    document.getElementById("description").readOnly = false;
                }
            }

            function checkForm() {
                t = document.getElementsByName("VAID");
                for (i=0; i<t.length; i++) {
                    if (t[i].checked)
                        return true;
                }
                alert("Please select an author before continue.");
                return false;
            }

            function checkForm1() {
                an = document.getElementById("name").value;
                if ((an == null) || (an.length < 1)) {
                    alert("Please enter a name before continue.");
                    return false;
                }
                return true;
            }

            function checkForm2() {
                an = document.getElementById("name").value;
                if ((an == null) || (an.length < 1)) {
                    alert("Please enter a name before continue.");
                    return false;
                }
                at = document.getElementById("authortype").value;
                if ((an == null) || (an.length < 1)) {
                    alert("Please select author type before continue.");
                    return false;
                }
                return true;
            }

            function clearForm(t) {
                showInput(t);
                document.getElementById("authorid").value = "0";
                document.getElementById("name").value = "";
                document.getElementById("firstname").value = "";
                document.getElementById("lastname").value = "";
                document.getElementById("email").value = "";
                document.getElementById("tel").value = "";
                document.getElementById("fax").value = "";
                document.getElementById("www").value = "";
                document.getElementById("address").value = "";
                document.getElementById("description").value = "";
            }

            function checkNext() {
                t = document.getElementsByName("VAID");
                if (t.length < 1) {
                    alert("Please add at least one author before continue.")
                    return false;
                }
                return true;
            }



        </script>
        
    </body>
</html>

