
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
    <td colspan="2" bgColor="#1145A6"> <div align="center"><font color="#FFFFFF"><strong>Information 
        provided by report</strong></font></div></td>
  </tr>
  <tr> 
    <td colspan="2" bgColor="#e4e9f8" ><font color="000080"><strong>&nbsp;&nbsp;Infomation 
      type: General&nbsp;Clone Information:</strong></font></td>
  </tr>
  <tr> 
    <td width="23%" ><div align="center"><em><strong>Field Name</strong></em></div></td>
    <td width="77%" > <div align="center"><em><strong>Description </strong></em></div>
      <div align="center"><em></em></div></td>
  </tr>
  <tr> 
    <td><strong> Clone Id</strong></td>
    <td ><strong>Clone ID is a unique clone identiver shared by BEC and FLEX</strong></td>
  </tr>
  <tr> 
    <td><strong>Sample Id</strong></td>
    <td ><strong>BEC sample Id</strong></td>
  </tr>
  <tr> 
    <td><strong>Plate Label</strong></td>
    <td><strong>Plate label</strong></td>
  </tr>
  <tr> 
    <td><strong>Sample Type</strong></td>
    <td><strong>Sample Type (Isolate, Empty, Control)</strong></td>
  </tr>
  <tr> 
    <td><strong> Sample Position</strong></td>
    <td><strong>Position on the plate ( 1 - 96 )</strong></td>
  </tr>
  <tr> 
    <td><strong> Clone Score</strong></td>
    <td><strong>Clone penalty score, calculated</strong></td>
  </tr>
  <tr> 
    <td><strong>Clone Rank</strong></td>
    <td><strong>Clone Rank, defines whether clone (a) match exspected ORF; (b) 
      acceptable based on user criteria; (c) quality compare to the other clones 
      originated from the same isolate.</strong></td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td ></td>
    <td >&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#e4e9f8"> <div align="left"><font color="000080"><strong>Infomation 
        type: Clone Trace Files Information</strong></font></div></td>
  </tr>
  <tr> 
    <td ><strong>Directory Name</strong></td>
    <td ><strong>Name of trace files directory for the clone</strong></td>
  </tr>
  <tr> 
    <td><strong>End Reads Length(High Quality Region)</strong></td>
    <td><strong>Specifies wether end reads for the clone are available and length 
      of high quality stretch (Trimming performed by Phred )</strong></td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#e4e9f8"><font color="000080"><strong>Infomation 
      type: Reference Sequence Information</strong></font></td>
  </tr>
  <tr> 
    <td><strong>Sequence ID </strong></td>
    <td><strong>BEC/Flex sequence Id, both values specified</strong></td>
  </tr>
  <tr> 
    <td><strong>CDS Start</strong></td>
    <td><strong>Cds start</strong></td>
  </tr>
  <tr> 
    <td><strong>CDS Stop</strong></td>
    <td><strong>Cds stop</strong></td>
  </tr>
  <tr> 
    <td><strong>CDS Length</strong></td>
    <td><strong>Cds length</strong></td>
  </tr>
  <tr> 
    <td><strong>GC Content</strong></td>
    <td><strong>GC content of gene sequence</strong></td>
  </tr>
  <tr> 
    <td><strong>Sequence Text </strong></td>
    <td><strong>Gene sequence (text) as was defined in FLEX on reference sequence 
      upload </strong></td>
  </tr>
  <tr> 
    <td><strong> CDS</strong></td>
    <td><strong>Gene CDS (text) as was defined in FLEX on reference sequence upload</strong></td>
  </tr>
  <tr> 
    <td><strong>GI Number</strong></td>
    <td><strong>GI as was stored in FLEX on reference sequence upload</strong></td>
  </tr>
  <tr> 
    <td><strong>Gene Symbol</strong></td>
    <td><strong>Gene symbol, as was stored in FLEX on reference sequence upload</strong></td>
  </tr>
  <tr> 
    <td><strong>PA Number (for Pseudomonas only)</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>SGA Number (for Yeast only)</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3" bgColor="#e4e9f8"><font color="000080"><strong>Infomation 
      type: Clone Sequence Information</strong></font></td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Id</strong></td>
    <td><strong>Clone sequence Id, if it is equal to 0 clone does not have assembled 
      sequence</strong></td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Analysis Status</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Cds Start</strong></td>
    <td><strong>Clone sequence cds start</strong></td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence Cds Stop</strong></td>
    <td><strong>Clone sequence cds stop</strong></td>
  </tr>
  <tr> 
    <td><strong>Clone Sequence</strong></td>
    <td><strong>Clone sequence (text)</strong></td>
  </tr>
  <tr> 
    <td><strong>Discrepancies High Quality (separated by type)</strong></td>
    <td><strong>Summary for all discrepancies identified for assembled sequence 
      (quality high, as defined based on user criteria. <em>NOTE: </em>discrepancy 
      quality is defined based on user criteria that was used for the first run 
      of Discrepancy Finder for this sequence). If sequence was submitted without 
      quality scores ALL discrepancies get quality property assigned as 'NOT KNOW' 
      they are trated as high quality discrepancies.</strong></td>
  </tr>
  <tr> 
    <td><strong>Discrepancies Low Quality (separated by type)</strong></td>
    <td><strong>Summary for all discrepancies identified for assembled sequence 
      (quality low)</strong></td>
  </tr>
</table>



</td></tr>





</table>

</body>

</html>


