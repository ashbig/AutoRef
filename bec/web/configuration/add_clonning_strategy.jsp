<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.spec.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.oligo.*" %>
<%@ page import="edu.harvard.med.hip.bec.action_runners.*" %>

<html>
<head>
<title>add cloning strategy</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body>

 
<% ArrayList linkers = (ArrayList ) request.getAttribute(Constants.LINKER_COL_KEY);
ArrayList vectors = (ArrayList ) request.getAttribute(Constants.VECTOR_COL_KEY);
%>
<table width="90%" border="0" align="center">
 <tr><td  bgColor='#b8c6ed'>Cloning strategy name</td> <TD bgColor='#b8c6ed' >   <input type='text' NAME='csname'></td> </tr> 
 <tr><td  bgColor='#e4e9f8'>First codon</td> <TD bgColor='#e4e9f8' > 
 <SELECT NAME='start_codon'>
    <%  String value = null;String name = null;
for (Enumeration e = BecProperties.getInstance().getStartCodons().keys() ; e.hasMoreElements() ;)
{
	name = (String) e.nextElement();
	value = (String)BecProperties.getInstance().getStartCodons().get(name);%>
	 <OPTION VALUE='<%= value %>'> <%= name%>
<%}%></SELECT> 
 </td> </tr> 
 <tr>   <td  bgColor='#b8c6ed'>Fusion last codon</td>    <TD bgColor='#b8c6ed' >  
 <SELECT NAME='fusion_stop_codon'> 
    <% 
for (Enumeration e = BecProperties.getInstance().getStopFusionCodons().keys() ; e.hasMoreElements() ;)
{
	name = (String) e.nextElement();
	value = (String)BecProperties.getInstance().getStopFusionCodons().get(name);%>
	 <OPTION VALUE='<%= value %>'> <%= name%>
<%}%></SELECT> 
</td>  </tr> 
 <tr>     <td  bgColor='#e4e9f8'>Closed last codon</td>    <TD bgColor='#e4e9f8' >  
 <SELECT NAME='closed_stop_codon'>
<%  
for (Enumeration e = BecProperties.getInstance().getStopClosedCodons().keys() ; e.hasMoreElements() ;)
{
	name = (String) e.nextElement();
	value = (String)BecProperties.getInstance().getStopClosedCodons().get(name);%>
	 <OPTION VALUE='<%= value %>'> <%= name%>
<%}%></SELECT>
</td>  </tr>
<tr><TD colspan='2'>&nbsp;</TD></TR>  <tr>  
 <td  bgcolor='#e4e9f8' width='50%'>Vector  </td>    <TD bgcolor='#e4e9f8' >  
 <SELECT NAME='<%=Constants.VECTOR_ID_KEY%>' ID='<%=Constants.VECTOR_ID_KEY%>'> 
<% for (int count = 0; count < vectors.size(); count++)
{BioVector vector = (BioVector)vectors.get(count);
 %><OPTION VALUE=<%= vector.getId()%>><%=vector.getName()%>
<%}%>
 </SELECT></td></tr> 
 <tr> <td  bgColor='#b8c6ed'>5' clone linker name</td><TD bgColor='#b8c6ed'>  
 <SELECT NAME='5LINKERID'> 
<%for (int count = 0; count < linkers.size(); count++)
{BioLinker linker = (BioLinker)linkers.get(count); %>
<OPTION VALUE=<%= linker.getId()%>><%=linker.getName()%>
<%}%>
 </SELECT></td></tr><tr> <td  bgcolor='#e4e9f8'> 3' clone linker name</td> 
 <TD bgcolor='#e4e9f8' ><SELECT NAME='3LINKERID'> 
<%for (int count = 0; count < linkers.size(); count++)
{BioLinker linker = (BioLinker)linkers.get(count); %>
<OPTION VALUE=<%= linker.getId()%>><%=linker.getName() %> <%}%>
 </SELECT></td></tr> 

</body>
</html>
