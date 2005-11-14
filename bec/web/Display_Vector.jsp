<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<html>

<head>

<title>vectors</title>
<LINK REL=StyleSheet HREF="application_styles.css" TYPE="text/css" MEDIA=screen>
<link href="FlexStyle.css" rel="application_styles" type="text/css">
</head>

<body >


<jsp:include page="NavigatorBar_Administrator.jsp" />
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> Vectors Information </font>
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

<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<% BioVector vector = null;
    ArrayList vectors = (ArrayList) request.getAttribute("vectors");
String[] row_class = {"evenRowColoredFont","oddRowColoredFont"} ; int row_count = 0;
 for (int count1 = 0; count1 < vectors.size(); count1++)
{

    vector = (BioVector)vectors.get(count1);

%>
<tr><td colspan=2><HR></td></tr>
<tr class=<%= row_class[row_count++ % 2]%>>
	<td >Vector Name  </td>	<td  ><%= vector.getName() %></td></tr>
<!--<tr> 
	<td  bgColor="#e4e9f8" ><b>Vector Id:</b></td>
	 <td bgColor="#e4e9f8">= vector.getId() %></td>  
	  </tr>-->
<tr class=<%= row_class[row_count++ % 2]%>> 
	<td  >Vector Source:</td><td ><%= vector.getSource() %></td>  	  </tr>
<tr class=<%= row_class[row_count++ % 2]%>> 
	<td >Vector Type:</td>	 <td ><%= vector.getTypeAsString() %></td>  	  </tr>

<tr> 
<td colspan=2 ALIGN='CENTER'>
<p><b><P></P>Vector Features</b> </p> 
     
      <table width="85%" border="0" align="center">
		   <tr  class='headerRow'> 
			  <th >Name</th>
			  <th >Type </th>
			  <th >Description </th>
			</tr>
	  <% ArrayList features = (ArrayList) vector.getFeatures();

	     String row_color = "bgColor='#e4e9f8'" ;
		 BioVectorFeature vf = null;

		 for (int count = 0; count < features.size(); count++)
		{
			vf = (BioVectorFeature)features.get(count);
                        row_count = 0;
		%>
       
        <tr class=<%= row_class[row_count++ % 2]%>> 
          <td width="44%" ><%= vf.getName() %></td>
          <td width="16%"><div align="center"><%= vf.getTypeAsString() %></div></td>
          <td ><div align="center"> <%= vf.getDescription() %></div></td>
        </tr>
		<%}%>
		</table>
</td>
</tr>
<TR><TD>&nbsp;</TD></TR>
<%}%>
  </table><hr><P>
</body>

</html>


