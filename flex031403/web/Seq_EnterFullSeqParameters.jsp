<%@page contentType="text/html"%>
<html>
<head>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<p><b><font size="4" color="#2693A6">Full Sequence Analysis</font></b> </p>

<font color="#2693A6" size="4"> <b>Set Name</b></font> 
  <input type="text" name="set_name" size="53" value="default">
<P> <strong>Acceptable sequence criteria. </strong>
<table border="0" cellspacing="4" width="60%">

		  <tr> 
          <td width="56%"  background="barbkgde.gif" ><b>Silent mutation</b></td>
		 <td width="44%"  background="barbkgde.gif" ><b>
				<input name="textfield8" type="text" value="2" size="5"></b></td>
			
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="3" size="5">
            </b></td>

		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Non conservative mutation</b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="1" size="5">
            </b></td>
		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Frameshift</b></td>
			
          <td  background="barbkgde.gif" ><b>
            <input name="textfield82" type="text" value="0" size="5">        </b></td>

		 </tr>
		  <tr> 
			<td  background="barbkgde.gif" ><b>Stop codon</b></td>
			<td  background="barbkgde.gif" ><b>
            <input name="textfield8" type="text" value="0" size="5">            </b></td>
		
		 </tr>
	
</table>
<p>&nbsp;</p>
<table width="60%" border="0" cellspacing="2" cellpadding="0">
  <tr>
    <td width="56%" background=barbkgde.gif><strong>Number of '<i>N</i>' for 100 bases </strong></td>
    <td width="44%" background=barbkgde.gif><input type="text" name="textfield" value ="5" size="5"></td>
  </tr>
  <tr>
    <td background="barbkgde.gif"><strong>Number of 'N' in a row</strong></td>
    <td background="barbkgde.gif"><input name="textfield2" type="text" value="2" size="5"></td>
  </tr>
</table>
<p> 
<p> 
<div align="center"> 
  <p> 
    <input type="submit" value="Submit" name="B1">
    <input type="reset" value="Reset" name="B2">
</div>
</html:form> 
</body>
</html>
