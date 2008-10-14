<%@ page language="java" %>
<%@ page import="plasmid.Constants" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
    <head>
        <title>Submit vector information - Step 5</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    </head>
    
    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="vInput5Title.jsp" />
                    <html:form action="/vInput5" method="POST">
                        <html:errors/>
                        <logic:present name="Vector"><html:hidden name="Vector" property="vectorid"/></logic:present>
                        <logic:notPresent name="Vector"><html:hidden property="vectorid" value="0"/></logic:notPresent>
                        <html:hidden property="step" value="5"/>
                        <h2>Submit Vector Information</h2>
                        <h4><em>Step 5: Description</em></h4>
                        <p>*Required field is in bold</p>
                        <table width="100%" border="0">
                            <tr valign="top"> 
                                <td width="15%"><strong>Description</strong>: <font color="#0000FF"><label id="infodesp">?</label></font> 
                                </td>
                                <td width="85%"> 
                                    <logic:present name="Vector">
                                        <html:textarea name="Vector" property="description" cols="80" rows="10"><bean:write name="Vector" property="description"/></html:textarea>
                                    </logic:present>
                                    <logic:notPresent name="Vector">
                                        <html:textarea property="description" cols="80" rows="10"/>
                                    </logic:notPresent>
                                </td>
                            </tr>
                            <tr height="18px"><td colspan="2">&nbsp;</td></tr>
                        </table>
                        <html:submit value="Continue"/>
                        <html:submit value="Back"/>
                        <html:submit value="Save..."/>
                    </html:form>
                </td>
            </tr>
        </table>
        <span id="desptitle" style="display:none;">
            <p><font size="-1"><strong>Description:</strong></font></p>
            <font size="-1">Based on information you provide in these forms, we will 
                create a description to be displayed for each of your vectors. This description 
                will include the type of vector, the promoter, tags (if applicable), the 
            selection markers and the method of cloning. </font><font size="-1" face="Courier New, Courier, mono">
                <p>Example: Mammalian expression vector with CMV promoter, T3, SP6; ampicillin 
                resistance; restriction enzyme cloning.</p>
            </font> <p><font size="-1">If there is other critical information about 
                    your vector that you want to be included in this description, please input 
            it here.</font></p>
        </span>
        <script>
            document.getElementById("infodesp").title = document.getElementById("desptitle").innerHTML;
        </script>        
        <jsp:include page="footer.jsp" />
    </body>
</html>

