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
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%-- The container that was searched --%>

<html>

<body>

	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        <td >
    <font color="#008000" size="5"><b> Discrepancy Report  </font>
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
<% 

//AnalyzedScoredSequence sequence = (AnalyzedScoredSequence)request.getAttribute("sequence") ;
 //ArrayList discrepancies = null;

//if ( sequence != null) discrepancies = sequence.getDiscrepancies() ;
//else discrepancies = (ArrayList)request.getAttribute("discrepancies") ;
 
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<!-- if ( sequence != null)
  {%>
<tr> 
    <td width="19%"><strong>Sequence Id:</strong>
   
     <A HREF="" onClick="window.open('=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")%>Seq_GetItem.do?forwardName= =Constants.SCOREDSEQUENCE_DEFINITION_INT%>&amp;ID==  sequence.getId()%>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;">
    	= sequence.getId()%>
	</a>
	</td>
  </tr>
}%> -->
<% if ( request.getAttribute("sequence_id") != null)
  {%>
<tr> 
    <td width="19%"><strong>Sequence Id:</strong>
   
     <A HREF="" onClick="window.open('<%=edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION")%>Seq_GetItem.do?forwardName=<%=Constants.SCOREDSEQUENCE_DEFINITION_INT%>&amp;ID=<%= request.getAttribute("sequence_id") %>','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes');return false;">
    	<%= request.getAttribute("sequence_id")%>
	</a>
	</td>
  </tr> <%}%>
  <tr> 
    <td> </P> 
     <!--   = Mutation.toHTMLString(discrepancies ) %> -->
      
       <table border=1 cellpadding=0 cellspacing=0 width='90%' align=center><tr >
      <th width='5%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Number</font></strong></th>
      <th  bgcolor='#1145A6'><strong><font color='#FFFFFF'>Description</font></strong></th>
      <th width='25%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Protein Description</font></strong></th>
      <th width='17%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Polymorphism</font></strong></th>
       <th width='14%' bgcolor='#1145A6'><strong><font color='#FFFFFF'>Confidence</font></strong></th></tr>

<%  String row_color = " bgColor='#e4e9f8'"; 
    ArrayList rna_discrepancies = null;
    int printed_discr_number = 1;
    DiscrepancyDescription discr_definition = null;RNAMutation rm = null;Mutation mut =null;
    ArrayList discrepancy_definition = (ArrayList)request.getAttribute("discrepancies");
     if ( discrepancy_definition != null )
         {
    for (int count = 0; count < discrepancy_definition.size(); count ++)
    {
            if (count % 2 == 0){  row_color = " bgColor='#e4e9f8'";	}
            else	{	row_color =" bgColor='#b8c6ed'";	}
            discr_definition = (DiscrepancyDescription)discrepancy_definition.get(count);
            if ( discr_definition.getDiscrepancyDefintionType() == DiscrepancyDescription.TYPE_AA)
            {
                rna_discrepancies = discr_definition.getRNACollection();
                for (int rna_count = 0; rna_count < rna_discrepancies.size(); rna_count++)
                {
                     rm = (RNAMutation)rna_discrepancies.get(rna_count);
                     %>  <tr><td  <%= row_color %> > <%= printed_discr_number++ %></td>
                         <td <%= row_color%> > <%= rm.toHTMLString()%> </td>
                        <%    if (rna_count== 0) {%>
                            <td  <%= row_color %> rowspan='<%= rna_discrepancies.size() %>' >  <%= ((AAMutation)discr_definition.getAADefinition()).toHTMLString() %></td><%}%>
                            <td  align='center' <%= row_color %> ><b>  <%= rm.getPolymorphismFlagAsString() %>
                            <% if ( rm.getPolymorphismFlag() == Mutation.FLAG_POLYM_YES )
                            {%>
 <P>   <input type=BUTTON value="Details"  onClick="window.open('<%=edu.harvard.med.hip .bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Display_PolymorphismHits.jsp?DISCR_ID=<%= rm.getId() %>&amp;SEQUENCE_ID=<%= request.getAttribute("sequence_id")%>&amp;POLYM_IDS=<%= Algorithms.replaceString(rm.getPolymorphismId()," ", "|") %>',<%= rm.getId() %>,'width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;"> 

                      
                            <%}%>
                            </b></td>  
                             <td  align='center'  <%=row_color %> >  <%= rm.getQualityAsString()%> </td> </tr>
                       <% 
             
		}
                }
	   else 
           {
               mut = discr_definition.getRNADefinition();
               %><tr><td  <%= row_color %> > <%= printed_discr_number++ %> </td> 
	       <td  <%= row_color%> > <%= ((Mutation)discr_definition.getRNADefinition()).toHTMLString() %></td>  
               <td <%= row_color %> >&nbsp;</td><td  <%=  row_color %> > &nbsp;</td>        
	       <td  align='center' <%= row_color%>> <%= mut.getQualityAsString() %></td>    </tr>     
           <%	}}} %>
       </table> 
     </td></tr></table>
</body>
</html>