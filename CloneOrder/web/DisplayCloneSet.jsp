<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Display Clone Set</title></head>
<body>
<h2 align="center"><b><font color="#003366">Kinase Clone Set</font></b></h2>
<br><br>
<div align="center">
  <center>
  <table width="85%">
    <tr><td>
        Click the FLEX ID hyperlink to get the full-length CDS sequence for the clone.
    </td></tr>
    <tr><td>
        Click <a href="downloadPlate.do">here</a> to download the plate and sequence information in Microsoft Excel format.
    </td></tr>
  <table>
  <br><br>
  <table border="1" cellpadding="0" cellspacing="0" width="85%">
    <tr>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">Plate
        ID</font></td>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">Well
        ID</font></td>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">FLEX
        ID</font></td>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">Gene
        Symbol</font></td>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">Genbank
        Accession Number</font></td>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">GI</font></td>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">Sugen
        ID</font></td>
      <td bgcolor="#006699" align="center"><font color="#FFFFFF">Clone
        Type</font></td>
    </tr>
    <logic:iterate id = "clone" name = "clones">
    <tr>

      <td width="15%" align="center"><bean:write name = "clone" property = "plateId"/>&nbsp;</td>
      <td width="7%" align="center"><bean:write name = "clone" property = "wellIdPosition"/>&nbsp;</td>

      <td width="16%" align="center">
        <a href="getGeneSequence.do?flexid=<bean:write name = "clone" property = "flexId"/>" target="_blank">
        <bean:write name = "clone" property = "flexId"/></a>&nbsp;
      </td>

      <td width="13%" align="center"><bean:write name = "clone" property = "geneSymbol"/>&nbsp;</td>
      <td width="14%" align="center"><bean:write name = "clone" property = "genbankAcc"/>&nbsp;</td>
      <td width="13%" align="center"><bean:write name = "clone" property = "gi"/>&nbsp;</td>
      <td width="11%" align="center"><bean:write name = "clone" property = "sugenId"/>&nbsp;</td>
      <td width="11%" align="center"><bean:write name = "clone" property = "cloneType"/>&nbsp;</td>
    </tr>
    </logic:iterate>
  </table>
  </center>
</div>


</body>
</html>
