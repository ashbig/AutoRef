<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>

<title>End Reads Parameters</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/> : Set End Reads Parameters</h2>
<hr>
<html:errors/> 
<html:form action="/logon.do" > 
<h3 >Set parameters for Sequence Trimming and Analysis of End Reads</h3>
<i>If you are not sure about certain parameter settings, leave them unchanged 
</i> <a href="helpSequenceTrimmingParam.htm">[parameter help file]</a>.</b> 
<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <input type="text" name="set_name" size="53" value="default">
<P><font color="#2693A6" size="4"><b> 
  <input type="radio" name="radiobutton" value="Y">
  Ends Trimming Parameters</b></font> 
<table border="0" CELLSPACING="4" width="95%">
  <tr> 
    <td background="barbkgde.gif"><strong><i><font size="+1">5 prime end</font></i></strong></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b> 
      <input type="checkbox" name="checkbox" value="checkbox">
      Trim no more than 25%, trim until the first 
      <input type="text" name="primer_min" size="5" value="25">
      bases contain less than 
      <input name="textfield" type="text" value="3" size="5">
      ambiquities </b></td>
  </tr>
  <tr> 
    <td background="barbkgde.gif" valign="top"><b> 
      <input type="checkbox" name="checkbox2" value="checkbox">
      Trim no more than 25%, trim until the first 
      <input type="text" name="primer_min2" size="5" value="25">
      bases contain less than 
      <input name="textfield2" type="text" value="3" size="5">
      bases with confidence below 
      <input name="textfield3" type="text" value="7" size="5">
      </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b> 
      <input type="checkbox" name="checkbox222" value="checkbox">
      Trim ABI primer blobs, where 
      <input type="text" name="primer_gc_min2" size="5" value="3">
      bases from 5prime end </b></td>
  </tr>
  
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b> 
      <p> 
      <p><i><font size="+1">3 prime end</font></i></b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b> 
      <input type="checkbox" name="checkbox" value="checkbox">
      Starting 
      <input type="text" name="primer_min" size="5" value="25">
      bases after 5prime trim, trim first 
      <input name="textfield" type="text" value="3" size="5">
      bases containing more than 
      <input name="textfield4" type="text" value="3" size="5">
      ambiquities </b> </td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" valign="top"><b> 
      <input type="checkbox" name="checkbox2" value="checkbox">
      Trim from 3prim end, until last 
      <input type="text" name="primer_min2" size="5" value="25">
      bases contain less than 
      <input name="textfield2" type="text" value="3" size="5">
      ambiquities</b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif"><b> 
      <input type="checkbox" name="checkbox23" value="checkbox">
      Trim from 3prim end, until last 
      <input type="text" name="primer_min22" size="5" value="25">
      bases contain less than 
      <input name="textfield22" type="text" value="3" size="5">
      bases with confidence below 
      <input name="textfield32" type="text" value="7" size="5">
      </b></td>
  </tr>
</table>
<p>&nbsp; 
<p><font size="4" color="#2693A6"><b>
  <input type="radio" name="radiobutton" value="N">
  Sequence Trimming Parameters</b></font> 
<table border="0" cellspacing="4" width="95%">
  <tr> 
    <td width="758" background="barbkgde.gif" ><b> 
      <input type="checkbox" name="checkbox24" value="checkbox">
      Standard Modd algorithm (-trim option for Phred)</b></td>
  </tr>
  <tr> 
    <td background="barbkgde.gif" ><b> 
      <input type="checkbox" name="checkbox25" value="checkbox">
      Modified Modd algorithm  (-trim_alt option for Phred)  
      <input name="textfield5" type="text" value="0.05" size="10">
      </b></td>
  </tr>
<tr> 
    <td background="barbkgde.gif" ><b> 
      
      Vectror sequence (optional)
      <input name="textfield5" type="text" value="" size="50">
      </b></td>
  </tr>
</table>
<P></P>
<b><font size="4" color="#2693A6">End Reads Analysis</font></b> 
<table border="0" cellspacing="4" width="95%">
  <tr> 
    <td  background="barbkgde.gif"><b>Phred base score (high quality cut-off)
      <input name="textfield6" type="text" value="20" size="5">
      </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" >
  <input type="radio" name="radiobutton" value="N">
  <b>Phred base score (low quality cut-off)
      <input name="textfield8" type="text" value="10" size="5"> </b></td>
  </tr>
  <tr> 
    <td  background="barbkgde.gif" >
  <input type="radio" name="radiobutton" value="N">
<b>Discard up to 
      <input name="textfield7" type="text" value="2" size="5">
      high quality bases surronded by no less than 
      <input name="textfield8" type="text" value="3" size="5">
      low quality bases</b></td>
  </tr>
   <tr> 
    <td  background="barbkgde.gif" ><b>Mutation penalty for sequence scoring</b> 
       <P></P>
      <table border=0 width="90%" align="center">
		 <tr > 
			<td width="30%"  background="barbkgde.gif" ><b>&nbsp;</b></td>
			<td width="30%" background="barbkgde.gif" ><b>High quality base</b></td>
			<td width="30%" background="barbkgde.gif" ><b>Low quality base</b></td>
		 </tr>
		  <tr> 
			
          <td  background="barbkgde.gif" ><b>Silent mutation</b></td>
			<td  background="barbkgde.gif" ><b>
<input name="textfield8" type="text" value="2" size="5"></b></td>
			<td  background="barbkgde.gif" ><b>
<input name="textfield8" type="text" value="1" size="5">
            </b></td>
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="3" size="5">
            </b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="1" size="5">
            </b></td>
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Non conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="18" size="5">
            </b></td>
			<td  background="barbkgde.gif" ><b><input name="textfield8" type="text" value="3" size="5"></b></td>
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Frameshift</b></td>
			
          <td  background="barbkgde.gif" ><b>
            <input name="textfield82" type="text" value="100" size="5">
            </b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="50" size="5">
            </b></td>
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Stop codon</b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="100" size="5">
            </b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="50" size="5">
            </b></td>
		 </tr>
		</table>
		</td>
  </tr>
</table>
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
<HR>
<HR>
<h3>Available Sets </h3>
<P> <font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <input type="text" name="set_name" size="53" value="">
  
<P><font color="#2693A6" size="4"><b>Ends Trimming Parameters</b></font> 
<table border="0" CELLSPACING="4" width="95%">

  <tr> 
    <td background="barbkgde.gif"><strong><i><font size="+1">5 
      prime end</font></i></strong></td>
  
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b> 
      abc </b></td>
  </tr>

 
   <tr><td>&nbsp;</td></tr>
  <tr> 
    <td  background="barbkgde.gif"><b><p>
      <p><i><font size="+1">3 prime end</font></i></b></td>
  
  </tr>
  <tr> 
    <td  background="barbkgde.gif"> <b> 
       abc </b>
      </td>
  </tr>
 
  </table>
<p>&nbsp; 
<p><font size="4" color="#2693A6"><b>Sequence Trimming Parameters</b></font> 
<table border="0" cellspacing="4" width="95%">
  <tr> 
    <td background="barbkgde.gif" ><b> 
     abc</b>
  </td>
  </tr>
 
</table>
<P></P>
<b><font size="4" color="#2693A6">End Reads   Analysis</font></b> 

<table border="0" cellspacing="4" width="95%">
  <tr> 
    <td  background="barbkgde.gif"><b>Phred    base score 
      20
      </b></td>
 
  </tr>
  <tr> 
    <td  background="barbkgde.gif" ><b>Discard 
      up to 
      2
      high quality bases surronded by no less than 
      2
      low quality bases</b></td>
 
  </tr>
  </table>

</body>

</html>


