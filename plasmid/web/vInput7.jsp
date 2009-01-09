<%@ page language="java" %>
<%@ page import="plasmid.Constants" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
    <head>
        <title>Submit vector information - Step 7</title>
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
                    <jsp:include page="vInput7Title.jsp" />
                    <html:form action="/vInput7" method="POST">
                        <html:errors/>
                        <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                        <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                        <logic:present name="PM"><html:hidden name="PM" property="publicationid"/></logic:present>
                        <logic:notPresent name="PM"><html:hidden property="publicationid" value="0"/></logic:notPresent>
                        <html:hidden property="step" value="7"/>
                        <h2>Submit Vector Information</h2>
                        <h4><em>Step 7: Publications and other information</em></h4>
                        <p>List any publications that describe the vector itself. You may also include
                            one or more additional publications that are representative of how this vector
                        has been used for experimental purposes.</p>
                        <p>*Required field is in bold</p>
                        <table width="100%" border="0">
                            <tr>
                                <td>
                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                            <tr bgcolor="white">
                                <td width="21%" align="right"><strong>PMID</strong>: </td>
                                <td width="79%">
                                    <logic:present name="PM">
                                        <html:text name="PM" property="pmid" readonly="true"/>
                                    </logic:present>
                                    <logic:notPresent name="PM">
                                        <html:text property="pmid"/>
                                    </logic:notPresent>
                                    &nbsp;<html:submit value="Find"/>
                                </td>
                            </tr>
                            <tr bgcolor="white">
                                <td width="21%" align="right"><strong>Title</strong>: </td>
                                <td width="79%">
                                    <logic:present name="PM">
                                        <html:text name="PM" property="title" readonly="true" size="80"/>
                                    </logic:present>
                                    <logic:notPresent name="PM">
                                        <html:text property="title" size="70" size="80"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                            <tr>
                                <td>
                                    <html:submit value="Add To List" onclick="return checkFormAdd();"/>
                                    <input type="button" value="Clear" onclick="return clearForm(false);"/>
                                </td>
                            </tr>
                        </table>
                        <p></p>
                        <table width="100%" border="0">
                            <tr>
                                <td>
                        <table width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="black">
                            <tr bgcolor="white">
                                <td width="3%">&nbsp;</td>
                                <td width="7%"><strong>PMID</strong></td>
                                <td width="90%"><strong>Title</strong></td>
                                
                            </tr>
                            <logic:present name="VPM">
                                <logic:iterate id="PM" name="VPM" indexId="PMNUM">
                                    <tr bgcolor="white">
                                        <td><input type="radio" id="PMNUM" name="PMNUM" value="<bean:write name="PMNUM"/>"></td>
                                        <td>
                                            <a href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=pubmed&cmd=search&term=<bean:write name="PM" property="pmid"/>" target="PMIDInfo"><bean:write name="PM" property="pmid"/></a>
                                        </td>
                                        <td><bean:write name="PM" property="title"/></td>
                                    </tr>
                                </logic:iterate>
                            </table>
                        </td>
                    </tr>
                                <tr><td><html:submit value="Remove From List" onclick="return checkForm();"/>&nbsp;</td></tr>
                            </logic:present>
                        </table>
                        <p></p>
                        <p><strong>Intellectual Property Disclosure</strong>: Please indicate below whether
                            distribution of this vector or any part of this vector would violate intellectual
                            property laws. If you are unsure, contact the technology transfer or legal counsel
                        office at your institution. </p>
                        <logic:empty name="vInput7Form" property="step">
                            <logic:present name="Vector">
                                <html:textarea name="Vector" property="IPD" rows="8" cols="80"/>
                            </logic:present>
                            <logic:notPresent name="Vector">
                                <html:textarea property="IPD" rows="8" cols="80"/>
                            </logic:notPresent>
                        </logic:empty>
                        <logic:notEmpty name="vInput7Form" property="step">
                            <html:textarea property="IPD" rows="8" cols="80"/>
                        </logic:notEmpty>
                        <p>
                            <html:submit value="Finish"/>
                            <html:submit value="Back"/>
                            <html:submit value="Save..."/>
                        </p>
                    </html:form>
                </td>
            </tr>
        </table>
        <div id="divTitle"
             onmouseout="showTitle(this.id, null, 0, 0);"
             style="visibility: hidden; position: absolute; float: left; left: 0; top: 0; z-index: 999; border: none 0px black; background-color: #FFFFCC; padding: 10px;">
        </div>
        
    <jsp:include page="footer.jsp" /></body>
    <script>
        a = document.getElementById("publicationid").value;
        if (parseInt(a) > 0) {
            showInput(true);
        } else {
            showInput(false);
        }

        function showInput(t) {
            if (t) {
                document.getElementById("pmid").readOnly = true;
                document.getElementById("title").readOnly = true;
            } else {
                document.getElementById("pmid").readOnly = false;
                document.getElementById("title").readOnly = false;
            }
        }

        function checkForm() {
            t = document.getElementById("PMNUM").value;
            if (t.length < 1) {
                alert("Please select publication before continue.");
                return false;
            } else
                return true;
        }

        function checkFormAdd() {
            t = document.getElementById("pmid").value;
            if ((t == null) || (t.length < 1)) {
                alert("Please enter PMID before continue.");
                return false;
            }
            t = document.getElementById("title").value;
            if ((t == null) || (t.length < 1)) {
                alert("Please enter title before continue.");
                return false;
            }
            return true;
        }

        function clearForm(t) {
            showInput(t);
            document.getElementById("pmid").value = "";
            document.getElementById("title").value = "";
            document.getElementById("publicationid") = "0";
        }
        
        function checkNext() {
            t = document.getElementsByName("PMNUM");
            if (t.length < 1) {
                alert("Please enter at least one publication before continue.");
                return false;
            }
            return true;
        }
    </script>
</html>

