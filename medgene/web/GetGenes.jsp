<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
  <title>Genes associated with a particular disease</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>
<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Genes
      Associated with a Particular Disease</font></b></td>
    <td width="22%" align="right"><b><font color="#003366" size="6"><img border="0" src="jpg/medgene02.gif" width="85" height="35"></font></b></td>
  </tr>
  <tr>
    <td width="126%"></td>
    <td width="9%"></td>
  </tr>
  <tr>
    <td width="135%" colspan="2"><br>
      <img border="0" src="jpg/menubar_upper.gif" width="900" height="10"></td>
  </tr>
</table>
  </center>
</div>

<br><br><br>
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
  </table>
  </center>
</div>

<html:form action="GetGenes.do">
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%" height="166">
    <tr>
      <td width="80%" rowspan="2" height="166">
      <div align="left">
  <table border="1" cellpadding="0" cellspacing="0" width="96%" height="341" bordercolor="#FFFFFF">
    <tr>
      <td width="100%" valign="middle" align="left" height="29">
        <table border="0" cellpadding="0" cellspacing="0" width="82%">
          <tr>
            <td width="4%"><img border="0" src="jpg/prompt.gif" width="13" height="13"></td>
            <td width="96%"><b><font color="#000080" face="Tahoma">Following are
              the correspondng MeSH term(s). </font></b></td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td width="200%" height="44" bgcolor="#D2E9FF">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
    <html:select property="diseaseTerm">
        <html:options
        collection="diseases"
        property="id"
        labelProperty="term"
        />
    </html:select>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
      </tr>
    <tr>
      <td width="200%" height="29" bgcolor="#FFFFFF">
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="4%">
<img border="0" src="jpg/prompt.gif" width="13" height="13">
    </td>
    <td width="96%">
<b><font color="#000099" face="Tahoma">Please choose a
 <a href="statistic_menu.jsp" target="_blank">statistical
method</a> to rank the gene list:</font> </b>
    </td>
  </tr>
</table>
        </td>
      </tr>
    <tr>
      <td width="200%" height="44" bgcolor="#D2E9FF">
&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
    <html:select property="stat">
        <html:options
        collection = "stats"
        property = "id"
        labelProperty = "type" 
        />
    </html:select>
        </td>
      </tr>
    <tr>
      <td width="200%" height="28">
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="4%"><img border="0" src="jpg/prompt.gif" width="13" height="13"></td>
    <td width="96%">
<b>
 <font face="Verdana" color="#000080">Please choose the number of genes for your
list:</font> </b>
    </td>
  </tr>
</table>
        </td>
      </tr>
    <tr>
      <td width="200%" height="43" bgcolor="#D2E9FF">
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
    <html:select property="number">
        <% int i = ((Integer)(session.getAttribute("user_type"))).intValue();
           if (i != 1) { %>
            <html:option key="top 10" value="10"/>
            <html:option key="top 25" value="25"/>
            <html:option key="top 100" value="100"/>
        <% } 
           else { %>
            <html:option key="top 25" value="25"/>            
            <html:option key="top 100" value="100"/>
            <html:option key="top 500" value="500"/>  
            <html:option key="top 1500" value="1500"/>
            <html:option key="top 3000" value="3000"/>
        <% } %>
    </html:select>
        </td>
      </tr>
    <tr>
      <td width="200%" height="67" bgcolor="#D2E9FF">
<font color="#0066CC">&nbsp;&nbsp; &nbsp;&nbsp; </font> <font color="#0033CC" face="Times New Roman">For performance issue, we only display limited genes here.&nbsp;<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; If you
want longer list, please <a href="mailto:yanhui_hu@hms.harvard.edu">email
us</a>.</font>
        </td>
      </tr>
    <tr>
      <td width="200%" height="57" bgcolor="#D2E9FF">
        <p><br>&nbsp;&nbsp;&nbsp; &nbsp; 
        <html:submit property="submit" value="Get Genes"/>&nbsp;&nbsp;&nbsp;
        <html:submit property="submit" value="New Search"/> <br>&nbsp;
        </p>        
       </td>
      </tr>
    </table>
  </div>
</html:form>
      
      </td>
      <td width="20%" valign="top" align="right" height="140">
        <br>
        <table border="1" cellpadding="0" cellspacing="0" width="80%" bordercolor="#D1D1BA">
          <tr>
            <td width="100%" bgcolor="#D1D1BA">
              <p align="center"><b><font color="#666633">Hint</font></b></td>
          </tr>
          <tr>
            <td width="100%">
              <p align="center"><br>
              <br>
              <a href="http://www.nlm.nih.gov/mesh/" target="_blank"><font color="#666633"><b>MeSH
              home</b></font></a><br>
              <p align="center">
      <b> <a href="DiseaseList.jsp" target="_blank"><font color="#666633">Disease
      <br>
      MeSH terms</font></a>
      </b>
              <p>&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td width="20%" valign="top" align="right" height="26">
        <p align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img border="0" src="jpg/human_disease.jpg" width="45" height="45">
      </td>
    </tr>
  </table>
  </center>
</div>
<br>

<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>