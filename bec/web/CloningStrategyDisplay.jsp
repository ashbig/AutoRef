<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>

<head>

<title>End Reads Parameters</title>
<LINK REL=StyleSheet HREF="file:///C|/Program%20Files/Apache%20Tomcat%204.0/webapps/BEC/FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="file:///C|/Program%20Files/Apache%20Tomcat%204.0/webapps/BEC/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body >


<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> cloning Startegy </font>
    <hr>
    
    <p>
    </td>
    </tr>
</table>

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
	
  </table>
  </center>
</div>

<% BioLinker linker3 = (BioLinker) request.getAttribute("linker3");
	BioLinker linker5 = (BioLinker) request.getAttribute("linker5");
	BioVector vector = (BioVector) request.getAttribute("vector");
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr>
	<td colspan=2 bgcolor="#1145A6" height="26"><strong><font color="#FFFFFF"> 5' Linker </font> </td>
	
</tr>

<tr>
	<td width="25%" bgColor="#e4e9f8">  <b>Linker Name</b>  </td>
	<td width="75%" bgColor="#e4e9f8"><%= linker5.getName() %></td>
</tr>
<tr> 
	<td  bgColor="#b8c6ed" ><strong>Sequence: </strong></td>
	<td bgColor="#b8c6ed"> <%= linker5.getSequence() %></td>
</tr>
<tr> 
	<td  bgColor="#e4e9f8" ><b>Linker Id:</b></td>
	 <td bgColor="#e4e9f8" height="29"><%= linker5.getId() %></td>   </tr>
 <tr>
	<td colspan=2 bgcolor="#1145A6" height="26"><strong><font color="#FFFFFF"> 3' Linker </font> </td>
	
</tr>

<tr>
	<td width="25%" bgColor="#e4e9f8">  <b>Linker Name</b>  </td>
	<td width="75%" bgColor="#e4e9f8"><%= linker3.getName() %></td>
</tr>
<tr> 
	<td  bgColor="#b8c6ed" ><strong>Sequence: </strong></td>
	<td bgColor="#b8c6ed"> <%= linker3.getSequence() %></td>
</tr>
<tr > 
	<td  bgColor="#e4e9f8" ><b>Linker Id:</b></td>
	 <td bgColor="#e4e9f8" height="29"><%= linker3.getId() %></td>   </tr>

 
  <TR><TD colspan="2">
Vector Information <P></P>
 <table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr>
	<td width="25%" bgColor="#b8c6ed">  <b>Vector Name</b>  </td>
	<td width="75%" bgColor="#b8c6ed"><%= vector.getName() %></td>
</tr>
<tr> 
	<td  bgColor="#e4e9f8" ><b>Vector Id:</b></td>
	 <td bgColor="#e4e9f8"><%= vector.getId() %></td>  
	  </tr>
 
<tr> 

<tr> 
	<td  bgColor="#b8c6ed" ><b>Vector Source:</b></td>
	 <td bgColor="#b8c6ed"><%= vector.getSource() %></td>  
	  </tr>
 

<tr> 
	<td  bgColor="#e4e9f8" ><b>Vector Type:</b></td>
	 <td bgColor="#e4e9f8"><%= vector.getTypeAsString() %></td>  
	  </tr>
 
<tr> 
<td colspan=2>
<p><b><P></P>Vector Features</b> </p> 
     
      <table width="85%" border="0" align="center">
		   <tr> 
			  <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Name</font></strong></th>
			  <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Type </font></strong></th>
			  <th bgcolor="#1145A6"><strong><font color="#FFFFFF">Description </font></strong></th>
			</tr>
	  <% ArrayList features = (ArrayList) vector.getFeatures();
	     String row_color = "bgColor='#e4e9f8'" ;
		 BioVectorFeature vf = null;
		 for (int count = 0; count < features.size(); count++)
		{
			vf = (BioVectorFeature)features.get(count);
			if (count % 2 == 0)
			{
			  row_color = "bgColor='#e4e9f8'";
			}
			else
			{
				row_color = " bgColor='#b8c6ed'";
			}
		%>
       
        <tr> 
          <td width="44%" <%= row_color %> ><strong><font color="#000080"><%= vf.getName() %></font></strong></td>
      
          <td width="16%" <%= row_color %>><div align="center"><%= vf.getTypeAsString() %></div></td>
          <td width="14%" <%= row_color %>><div align="center"> <%= vf.getDescription() %></div></td>
        </tr>
		<%}%>
		</table>
</td>
</tr>
  </table>
  </TD></TR>

  </table>

</body>

</html>


