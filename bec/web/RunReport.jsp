<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<html>
<head>
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

<title>Run report</title>
</head>
<body >

<% String redirection = BecProperties.getInstance().getProperty("JSP_REDIRECTION");%>
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%">
 <i>If you are not sure about certain parameter please consult <a href="<%= redirection%>help/Help_ReportRunner.html"> help
</a>. </i> 
  </td>    </tr>
  </table>
  </center>
</div>

<form action="RunProcess.do" method='POST' onsubmit="return validate_run_report(this);"> 
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
        information: </strong></font></div></td>
    <td width="50%" bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Clone 
              raw data information:</strong></font></div></td>
  </tr>
  <tr> 
    <td >   <input type="checkbox" name="clone_id" value="1">    Clone ID</td>
    <td >   <input type="checkbox" name="dir_name" value="1"> Directory name</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="clone_final_status" value="1">      Clone final status</td>
          <td> <input type="checkbox" name="read_length" value="1">
            End reads length( high quality region)</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="plate_label" value="1">      Plate label</td>
    <td> <input type="checkbox" name="assembly_attempt_status" value="1"> Status of clone sequence assembly</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="sample_type" value="1">      Sample type</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="position" value="1">      Sample Well</td>
    <td>&nbsp;</td>
  </tr>
<tr> 
    <td> <input type="checkbox" name="score" value="1">      Clone score</td>
    <td>&nbsp;</td>
  </tr>
<tr> 
    <td> <input type="checkbox" name="rank" value="1">      Clone rank</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td bgColor="#e4e9f8"><div align="center"><font color="000080"><strong>Reference 
        sequence information:</strong></font></div></td>
    <td bgColor="#e4e9f8"><div align="center" ><font color="000080"><strong>Clone 
        assembly information:</strong></font></div></td>
  </tr>
  <tr> 
    <td>      <input type="checkbox" name="ref_sequence_id" value="1">      Sequence ID</td>
    <td>      <input type="checkbox" name="clone_seq_id" value="1">      Clone sequence ID</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="ref_cds_start" value="1">      CDS start</td>
    <td><input type="checkbox" name="clone_status" value="1"> Status of     clone sequence analysis</td>
  </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_cds_stop" value="1">      CDS stop</td>
    <td><input type="checkbox" name="clone_seq_cds_start" value="1">    Clone sequence CDS start</td>
  </tr>
  <tr> 
    <td><input type="checkbox" name="ref_cds_length" value="1">      CDS length</td>
    <td>  <input type="checkbox" name="clone_seq_cds_stop" value="1">  Clone sequence CDS stop</td>
  </tr>
  <tr> 
    <td > <input type="checkbox" name="ref_gc" value="1">     GC content</td>
      <td>  <input type="checkbox" name="clone_seq_text" value="1">   Clone sequence</td>
   </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_seq_text" value="1">      Sequence text</td>
    <td><input type="checkbox" name="clone_discr_high" value="1">   High quality  discrepancies (separated by type)</td>
 
  </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_cds" value="1">     CDS</td>
    <td> <input type="checkbox" name="clone_disc_low" value="1"> Low quality  discrepancies (separated by type)</td>
  
  </tr>
  <tr> 
    <td> <input type="checkbox" name="ref_gi" value="1">      GI number</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>  <input type="checkbox" name="ref_gene_symbol" value="1">      Gene symbol</td>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td><input type="checkbox" name="ref_species_id" value="1">
          Species specific ID</td><td>&nbsp;</td> </tr>
  <tr>
     <td><input type="checkbox" name="ref_ids" value="1">
          All available identifiers</td> <td>&nbsp;</td></tr>
  
</table>



</td></tr>





</table>
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    &nbsp; 
    <input type="reset" value="Reset" name="B2">
</div>
</form> 
</body>

</html>


