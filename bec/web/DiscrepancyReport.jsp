<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.sequence.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.feature.*" %>
<%-- The container that was searched --%>

<html>

<body>

	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> discrepancy Report  </font>
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
<p></p>
<% AnalyzedScoredSequence sequence = (AnalyzedScoredSequence)request.getAttribute("sequence") ;%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="19%"><strong>Sequence Id:</strong>
   
     <A HREF="" onClick="window.open('/BEC/Seq_GetItem.do?forwardName=<%=Constants.SCOREDSEQUENCE_DEFINITION_INT%>&amp;ID=<%= sequence.getId()%>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;">
    	<%= sequence.getId()%>
	</a>
	</td>
  </tr>
  <tr> 
    <td> </P> 
      <table border="1" cellpadding="0" cellspacing="0" width="100%" align=center>
   		 <tr >
       
    		<th width="13%" bgcolor="#1145A6"><strong><font color="#FFFFFF">Number</font></strong></th>
            <th width="31%" bgcolor="#1145A6"><strong><font color="#FFFFFF">Description</font></strong></th>
          <th width="25%" bgcolor="#1145A6"><strong><font color="#FFFFFF">Proteine Description</font></strong></th>
        
    <th width="17%" bgcolor="#1145A6"><strong><font color="#FFFFFF">Polymorphism</font></strong></th>
        
    <th width="14%" bgcolor="#1145A6"><strong><font color="#FFFFFF">Confidence</font></strong></th>
		 
    </tr>
	
	
<%  
    String row_color = " bgColor='#e4e9f8'";
	Mutation mut = null;
	LinkerMutation linker = null;
   RNAMutation rm = null;
   AAMutation am = null; int cur_number =1;
System.out.println("size "+sequence.getDiscrepancies().size());
    for (int count = 0; count < sequence.getDiscrepancies().size(); count ++)
	{
	
		if (count % 2 == 0)
		{
		  row_color = " bgColor='#e4e9f8'";
		}
		else
		{
			row_color =" bgColor='#b8c6ed'";
		}
	  mut = (Mutation)sequence.getDiscrepancies().get(count);
                if (mut.getType() == Mutation.RNA)
		{

			rm = (RNAMutation)sequence.getDiscrepancies().get(count);

    	%>
			<tr>
                        <td  <%= row_color %>><%= rm.getNumber() %></td>
                        <td  <%= row_color %>><%= rm.toHTMLString()%></td>
  			<%
			if ( sequence.getDiscrepancies().size() >= count )
	   		{
                                mut= (Mutation) sequence.getDiscrepancies().get(count+1);
                              
                                if (mut.getType() == Mutation.AA)
                                {

                                    am = (AAMutation)sequence.getDiscrepancies().get(count+1);
                                    count++;


	   		%>
			 <td  <%= row_color %>><%= am.toHTMLString()%></td>
 			<%}else{%> <td  <%= row_color %>>&nbsp;</td> <%}}%>
			<td  <%= row_color %>><%= rm.getPolymorphismFlagAsString()%></td>  
			<td  <%= row_color %>><%= rm.getQualityAsString()%></td>   
		    </tr>                
		<%}
	   else if (mut.getType() == Mutation.LINKER_3P || mut.getType() == Mutation.LINKER_5P)
	   {
	   	linker = (LinkerMutation) sequence.getDiscrepancies().get(count);
		%><tr>
			 <td  <%= row_color %>><%= linker.getNumber() %></td> 
              <td  <%= row_color %>><%= linker.toHTMLString()%></td>   
			    <td  <%= row_color %>>&nbsp;</td> 
			 <td  <%= row_color %>>&nbsp;</td>        
			 <td  <%= row_color %>><%= linker.getQualityAsString()%></td>   
		    </tr>                
	<%}}%>
	
    </table>
	
	</td></tr></table>
</body>
</html>