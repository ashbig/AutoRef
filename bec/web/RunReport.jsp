<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<html>
<head>
<title>create report</title>
</head>
<body >
<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td > <font color="#008000" size="5"><b> create Report </font>    <hr>    <p>    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>
  </center>
</div>

<html:form action="/RunProcess.do" > 
<input name="forwardName" type="hidden" value="<%=Constants.PROCESS_CREATE_REPORT%>" >

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr><td>
<jsp:include page="enter_items.jsp" /></td></tr>
<tr><td>
<table width="100%" border="0" cellpadding="2" cellspacing="2">
  <tr> 
    <td colspan="2" bgColor="#1145A6"> <font color="#FFFFFF"><strong>Select fields 
      to be reported</strong></font></td>
  </tr>
  <tr> 
    <td width="50%" bgColor="#e4e9f8" ><div align="center"><font color="000080"><strong>&nbsp;&nbsp;&nbsp;Clone 
        Information: </strong></font></div></td>
    <td width="50%" bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Clone 
              Raw Data Information</strong></font></div></td>
  </tr>
  <tr> 
    <td >   <input type="checkbox" name="clone_id" value="1">    Clone Id</td>
    <td >   <input type="checkbox" name="dir_name" value="1"> Directory Name</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="sample_id" value="1">      Sample Id</td>
          <td> <input type="checkbox" name="read_length" value="1">
            End Reads Length(High Quality Region)</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="plate_label" value="1">      Plate Label</td>
          <td>&nbsp; </td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="sample_type" value="1">      Sample Type</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="position" value="1">      Sample Position</td>
    <td>&nbsp;</td>
  </tr>
<tr> 
    <td> <input type="checkbox" name="rank" value="1">      CLone Rank</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Reference 
        Sequence Information</strong></font></div></td>
    <td bgColor="#e4e9f8"><div align="center" ><font color="000080"><strong>Clone 
        Sequence Information</strong></font></div></td>
  </tr>
  <tr> 
    <td>      <input type="checkbox" name="ref_sequence_id" value="1">      Sequence ID</td>
    <td>      <input type="checkbox" name="clone_seq_id" value="1">      Clone Sequence Id</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="ref_cds_start" value="1">      CDS Start</td>
    <td><input type="checkbox" name="clone_status" value="1">      Clone Sequence Analysis Status</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_cds_stop" value="1">      CDS Stop</td>
    <td><input type="checkbox" name="clone_discr_high" value="1">    Discrepancies High Quality (separated by type)</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="ref_cds_length" value="1">      CDS Length</td>
    <td>  <input type="checkbox" name="clone_disc_low" value="1">   Discrepancies Low Quality (separated by type)</td>
  </tr>
  <tr> 
    <td > <input type="checkbox" name="ref_gc" value="1">     GC Content</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_seq_text" value="1">      Sequence Text</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_cds" value="1">     CDS</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_gi" value="1">      GI Number</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>  <input type="checkbox" name="ref_gene_symbol" value="1">      Gene Symbol</td>
    <td>&nbsp;</font></td>
  </tr>
  <tr> 
    <td>
      <input type="checkbox" name="ref_panum" value="1">      PA Number (for Pseudomonas project only)</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="ref_sga" value="1">      SGA Number (for Yeast project only)</td>
    <td>&nbsp;</td>
  </tr>
  
</table>



</td></tr>





</table>
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>

</html>


