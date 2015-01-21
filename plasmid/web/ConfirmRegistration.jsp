<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
 <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">


<body>
<jsp:include page="signinMenuBar.jsp" />
<table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
   <%-- <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>--%>
    <td width="100%%" align="left" valign="top">
    <%--<logic:equal name="registrationForm" property="update" value="true"> 
	<%--<jsp:include page="updateAccountTitle.jsp" />--%>
    <%--</logic:equal>
    <logic:notEqual name="registrationForm" property="update" value="true"> 
	<jsp:include page="registrationTitle.jsp" />
    </logic:notEqual>--%>
      <html:form action="Registration.do" enctype="multipart/form-data">
<input type="hidden" name="forward" value="submit"/>
<p class="mainbodytext">Please verify the following information. (* indicates required 
  field) </p>
<table width="1000px" border="0">
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*First Name:</td>
    <td width="78%" align="left" valign="baseline"> 
        <bean:write name="registrationForm" property="firstname"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*Last Name:</td>
    <td width="78%" align="left" valign="baseline"> 
        <bean:write name="registrationForm" property="lastname"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*Email:</td>
    <td width="78%" align="left" valign="baseline"> 
        <bean:write name="registrationForm" property="email"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*Phone:</td>
    <td width="78%" align="left" valign="baseline"> 
        <bean:write name="registrationForm" property="phone"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*Institution/<br>Company Name:</td>
    <td width="78%" align="left" valign="baseline"> 
        <bean:write name="registrationForm" property="institution"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*Category:</td>
    <td width="78%" align="left" valign="baseline"> 
        <bean:write name="registrationForm" property="category"/>
    </td>
  </tr>
  <logic:notEqual name="registrationForm"  property="piname" value="">
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">PI:</td>
    <td width="78%" align="left" valign="baseline"> 
        <bean:write name="registrationForm" property="piname"/>
    </td>
  </tr>
  </logic:notEqual>
  <logic:equal name="registrationForm" property="piname" value="">
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="top">PI information:</td>
    <td width="78%" align="left" valign="baseline"> <table width="100%" border="0" bordercolor="#000000">
        <tr class="formlabel"> 
          <td width="25%"> *Fist Name:</td>
          <td width="75%">
             <bean:write name="registrationForm" property="pifirstname"/>
          </td>
        </tr>
        <tr class="formlabel"> 
          <td class="formlabel"> *Last Name:</td>
          <td>
             <bean:write name="registrationForm" property="pilastname"/>
          </td>
        </tr>
        <tr class="formlabel"> 
          <td class="formlabel"> *Email:</td>
          <td>
             <bean:write name="registrationForm" property="piemail"/>
          </td>
        </tr>
      </table></td>
  </tr>
  </logic:equal>
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">Group:</td>
    <td width="78%" align="left" valign="baseline">
        <bean:write name="registrationForm" property="group"/>
    </td>
  </tr>

  <tr class="formlabel"> 
    <td width="22%" align="right">&nbsp;</td>
    <td width="78%">&nbsp;</td>
  </tr>
<!--
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*Password:</td>
    <td width="78%" align="left" valign="baseline"> 
        <html:password property="password" styleClass="text"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="22%" align="left" valign="baseline">*Re-enter the password 
      </td>
    <td width="78%" align="left" valign="baseline"> 
        <html:password property="password2" styleClass="text"/>
    </td>
  </tr>
-->
<html:hidden property="password"/>
  <tr class="formlabel"> 
    <td width="22%" align="right" valign="baseline">
        <html:submit styleClass="text" value="Confirm"/>
    </td>
  </tr>
</table>
      </html:form></td>
  </tr>
  <tr><td class='mainbodytext'><font color='red'>If this information is not correct please user your browser's back button to go back and make changes.</td></tr>
</table>
<jsp:include page="footer.jsp" /></body>
     </div>
</html>

