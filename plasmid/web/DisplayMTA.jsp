<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.MTA" %> 
<%@ page import="java.util.List" %> 

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    
<SCRIPT LANGUAGE="JavaScript">

function Disab(num) {
    frm=document.checkoutForm;
    var found_it
    var b = 1
    var n = 0;
    
    while(n<num) {
        var tagname = "isagree["+n+"]"
        for (var i=0; i<document.getElementsByName(tagname).length; i++)  { 
            if (document.getElementsByName(tagname)[i].checked)  {
                found_it = document.getElementsByName(tagname)[i].value
                if (found_it == "Do Not Agree") {
                    b = 0;
                } 
            } 
        }
        n++
    }
     
    if(b == 1) {
        frm.submit.disabled=false;
    } else {
        frm.submit.disabled=true;
    }
}

</SCRIPT>

</head>

<body>
<jsp:include page="orderTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="checkoutTitle.jsp" />

<p class="mainbodytexthead">
   Your institution has pre-approved the transfer of plasmid clones in our collection that are covered by our standard Plasmid Transfer Agreement (MTA).  The plasmid(s) you requested fall into the standard MTA category.  Thus, you do not need an institutional signature on this document.  
</p>
<p class="mainbodytexthead">
Please read the MTA to be sure you understand the obligations herein, which may include restrictions on commercial use and/or patentability of the material. To indicate your agreement to these terms, please click ?Agree? below.  
</p>
<p class="mainbodytexthead">
    If you have any questions, contact us at <a href="mailto:plasmidMTA@hms.harvard.edu">plasmidMTA@hms.harvard.edu</a>.
</p>

<html:form action="DisplayMTA.do">
    <input type="hidden" name="isBatch" value="<bean:write name="isbatch"/>">
    <% int num=((List)request.getAttribute("mtas")).size(); %>
    <logic:iterate name="mtas" id="mta" indexId="n">
        <html:textarea rows="10" cols="80" name="mta" property="textfile" readonly="true"/>
        <p><b>You acknowledge that you have read and understood the conditions outlined in this Agreement You understand that your Institution has agreed to be bound by the conditions set forth in this Agreement and you also agree to abide by them in the receipt and use of the Plasmids.  
        </b></p>
        <p>
            <html:radio styleClass="text" property='<%="isagree["+n+"]"%>' value="<%=MTA.ISAGREE_Y%>" onclick='<%="Disab("+num+")"%>'>Agree</html:radio>
            <html:radio styleClass="text" property='<%="isagree["+n+"]"%>' value="<%=MTA.ISAGREE_N%>" onclick='<%="Disab("+num+")"%>'>Do Not Agree</html:radio>
        </P>
    </logic:iterate>
    <html:submit value="Continue" disabled="true"/>
</html:form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>
