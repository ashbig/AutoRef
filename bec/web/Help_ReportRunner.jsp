
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
        
    <td > <font color="#008000" size="5"><b> Help : Report Runner</font> 
      <hr>    <p>    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>      <td width="100%"><html:errors/></td>    </tr>
  </table>
  </center>
</div>



<table width="100%" border="1" cellpadding="2" cellspacing="2">
  <tr> 
    <td colspan="3" bgColor="#1145A6"> <div align="center"><font color="#FFFFFF"><strong>Tool 
        goal: </strong></font></div></td>
  </tr>
  <tr> 
    <td colspan="3" >&nbsp; </td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#1145A6"> <div align="center"><font color="#FFFFFF"><strong>Information 
        provided by report</strong></font></div></td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#e4e9f8" ><font color="000080"><strong>&nbsp;&nbsp;Infomation 
      type: General&nbsp;Clone Information:</strong></font></td>
  </tr>
  <tr> 
    <td width="23%" ><div align="center"><em><strong>Field Name</strong></em></div></td>
    <td width="43%" ><div align="center"><em><strong>Description </strong></em></div></td>
    <td width="34%" ><div align="center"><em><strong>Values</strong></em></div></td>
  </tr>
  <tr> 
    <td><strong> Clone Id</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Sample Id</strong></td>
    <td>&nbsp; </td>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td><strong>Plate Label</strong></td>
    <td>&nbsp; </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Sample Type</strong></td>
    <td>&nbsp; </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong> Sample Position</strong></td>
    <td>&nbsp; </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong> Clone Score</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Clone Rank</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp; </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td ></td>
    <td >&nbsp;</td>
    <td >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#e4e9f8"> <div align="left"><font color="000080"><strong>Infomation 
        type: Clone Trace Files Information</strong></font></div></td>
  </tr>
  <tr> 
    <td ><strong>Directory Name</strong></td>
    <td >&nbsp; </td>
    <td ></td>
  </tr>
  <tr> 
    <td><strong>End Reads Length(High Quality Region)</strong></td>
    <td>&nbsp; </td>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#e4e9f8"><font color="000080"><strong>Infomation 
      type: Reference Sequence Information</strong></font></td>
  </tr>
  <tr> 
    <td><strong>Sequence ID </strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>CDS Start</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td><strong>CDS Stop</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td><strong>CDS Length</strong></td>
    <td>&nbsp; </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>GC Content</strong></td>
    <td>&nbsp; </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Sequence Text </strong></td>
    <td>&nbsp; </td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong> CDS</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>GI Number</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Gene Symbol</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>PA Number (for Pseudomonas only)</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>SGA Number (for Yeast only)</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#e4e9f8"><font color="000080"><strong>Infomation 
      type: Clone Sequence Information</strong></font></td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Id</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Analysis Status</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Cds Start</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Cds Stop</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Discrepancies High Quality (separated by type)</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Discrepancies Low Quality (separated by type)</strong></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  
</table>



</td></tr>





</table>

</body>

</html>


