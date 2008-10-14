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
                        <p>A vector feature is any definable aspect of your vector. This includes the 
                            origin of replication, the promoter upstream of the potential insert, the primers 
                            used for sequencing, the multiple cloning site (MCS), the method of cloning 
                            that can be used to transfer inserts into this vector, the bacterial or mammalian 
                            selectable markers etc. Please include any and all vector features in as much 
                            detail as possible, because the more information you provide the more information 
                        we can give our users.</p>
                        <p>*Required field is in bold</p>
                        <table width="100%" border="0"><tr valign="top"> 
                                <td width="17%"><strong>Feature type</strong>: <label id="ftdesp">?</label></td>
                                <td width="83%"> 
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
                            <tr valign="top"> 
                                <td><strong>Feature name</strong>: <label id="fndesp">?</label> </td>
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
                            <tr valign="top"> 
                                <td>Description:</td>
                                <td> 
                                    <html:textarea property="description" cols="60" rows="6"/>
                                </td>
                            </tr>
                            <tr valign="top"> 
                                <td>Start position:</td>
                                <td> 
                                    <html:text property="start"/>
                                </td>
                            </tr>
                            <tr valign="top"> 
                                <td>End position:</td>
                                <td> 
                                    <html:text property="stop"/>
                                </td>
                            </tr>
                        </table>
                        <html:submit value="Add To List" onclick="return checkForm();"/>
                        
                                                
                        <h5><em>List of features:</em></h5>
                        <logic:present name="Features">
                            <table width="100%" border="0">
                                <tr>
                                    <td>
                                        <table width="100%" border="1">
                                            <tr align="center"> 
                                                <td width="3%">&nbsp;</td>
                                                <td width="20%">Name</td>
                                                <td width="40%">Description</td>
                                                <td width="23%">Type</td>
                                                <td width="7%">Start</td>
                                                <td width="7%">End</td>
                                            </tr>
                                            <logic:iterate id="feature" name="Features">
                                                <tr> 
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
                        <html:submit value="Continue" onclick="checkFeatures();"/>
                        <html:submit value="Back"/>
                        <html:submit value="Save..."/>
                    </html:form>
                </td>
            </tr>
        </table>
        <span id="fttitle" style="display:none;">
        Feature Type refers to the general category that a vector 
        feature belongs to. For example: 
      Feature Type Feature Name Description
        Selectable marker AmpR Ampicillin Resistance
        Tag His C-terminal His tag
        Protease cleavage site TEV TEV protease cleavage site
        Recombination site attL1 Gateway attL1 recombination site
      If you don't think your feature type is listed, 
        add a new feature type to the list.
        </span>
        <span id="fntitle" style="display:none;">
        Feature Name refers to the specific feature of 
        the plasmid that you are describing. For example:
      Feature Type Feature Name Description
        Selectable marker AmpR Ampicillin Resistance
        Tag His C-terminal His tag
        Protease cleavage site TEV TEV protease cleavage site
        Recombination site attL1 Gateway attL1 recombination site
      Please include all the features that you know about, 
        and add new features if your particular feature name isn't listed. 
        </span>
        <script>
            document.getElementById("ftdesp").title = document.getElementById("fttitle").innerHTML;
            document.getElementById("fndesp").title = document.getElementById("fntitle").innerHTML;
        </script>                            
        <jsp:include page="footer.jsp" />
    </body>
</html>

