<%@ page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>


<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.mapping.*" %>
<%@ page import="edu.harvard.med.hip.bec.sampletracking.objects.*" %>
<%@ page import="edu.harvard.med.hip.bec.coreobjects.endreads.*" %>

<%-- The container that was searched --%>
<head>
<style type="text/css">
<!--
.red {
	background-color:#9999CC;
}
.green {
	background-color: #99FFFF;
}
.black {
	background-color: #999999;
}
.brown {
	background-color: #ffe4c4;
}
.yellow {
	background-color: #9999FF;
}
.orange {
	background-color: #99CCFF;
}
.control {
	background-color: #CCCCFF;
}
.empty {
	background-color: #CCCCCC;
}

.nosample {
	background-color: white;
}
.notanalized {
	background-color: white;
}
.nomatch {
	background-color: #ADFF2F;
}
-->
</style>
</head>
<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" /> 
	<p><P>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
    <tr>
        
    <td > <font color="#008000" size="5"><b> container Quality Report</font> 
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

Container container = (Container)request.getAttribute("container") ;
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
  <tr> 
    <td width="19%"><strong>Label:</strong></td>
    <td width="81%"> 
      <%= container.getLabel() %>
    </td>
  </tr>
  <tr> 
    <td><strong>Container Id:</strong></td>
    <td> 
      <%= container.getId() %>
    </td>
  </tr>
   <tr> 
    <td><strong>Container Type:</strong></td>
    <td> 
      <%= container.getType() %>
    </td>
  </tr>
  
  
  <tr> 
    <td><strong>Cloning Strategy</strong></td>
    <td> 
      <a href="<%= edu.harvard.med.hip.utility.ApplicationHostDeclaration.JSP_REDIRECTION %>Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %>&amp;ID=<%= container.getCloningStrategyId() %>">
	    <%= container.getCloningStrategyId() %></A>
    </td>
  </tr>
  
 
</table>
<P><P></P></P>
<%  
    String[] borders = {"border:solid 2px magen", 
	"border:solid 1px #060", 
	"border:dotted 2px magen",
	"border:dotted 1px #060",
	"border:solid 2px red",
	"border:dashed 2px blue",
	"border:dotted 1px green",
	"border:solid 2px black",
	"border:dashed 1px green",
	"border:solid 2px magen"};
%>
	

 <%
//build array of cell entries
        int rows = ((Integer)request.getAttribute("rows")).intValue();
        int cols = ((Integer)request.getAttribute("cols")).intValue();

        int forwardName = ((Integer)request.getAttribute("forwardName")).intValue();
	Sample sample = null;	int constructid = -1; int border_index = -1;
        String[][] cell_data = new String[rows][cols];
        int row = 0; int col = 0;
        String anchor = null;
     
         for (int count = 0; count < container.getSamples().size(); count ++)
        {	
                sample = (Sample)container.getSamples().get(count);

if ( forwardName == Constants.CONTAINER_RESULTS_VIEW)
{
                anchor = "<A HREF=\"\" onClick=\"window.open('/BEC/Seq_GetItem.do?forwardName="+ Constants.SAMPLE_ISOLATE_RANKER_REPORT  
		+ "&amp;ID="+ sample.getId()+"&amp;container_label="+container.getLabel()+"','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;\"><div align=center>"+ sample.getPosition()+"</div></a>";
}
if (forwardName == Constants.PROCESS_APROVE_ISOLATE_RANKER && sample.getIsolateTrackingEngine() != null)
{
   anchor = "<A HREF=\"\" onClick=\"window.open('/BEC/Seq_GetItem.do?forwardName="+ Constants.CONSTRUCT_DEFINITION_REPORT 
		+ "&amp;ID="+ sample.getIsolateTrackingEngine().getConstructId()+"','"+sample.getIsolateTrackingEngine().getConstructId()+"','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;\"><div align=center>"+ sample.getPosition()+"</div></a>";
}
                row = Algorithms.convertWellNumberIntoRowNumber(sample.getPosition())-1;
                 col =Algorithms.convertWellNumberIntoColNumber(sample.getPosition() ) -1;
                if (constructid != sample.getIsolateTrackingEngine().getConstructId())
                {
                        border_index++;
                        if ( border_index > borders.length -1) border_index = 0;
                }
                if ( sample.getType().equals("CONTROL_POSITIVE") ||  sample.getType().equals("CONTROL_NEGATIVE"))
		{
			cell_data[row][col] =" <td class='control'><div align=center>"+sample.getPosition()+"</div></td>";
		}
                else if ( sample.getType().equals("EMPTY") )
                {
                      cell_data[row][col] =" <td class='empty' style="+ borders[border_index]+" ><div align=center>"+sample.getPosition()+"</div></td>";
                }
                else if ( sample.getIsolateTrackingEngine().getStatus() == IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH ||
                            sample.getIsolateTrackingEngine().getStatus() == IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH)
                    {
                        cell_data[row][col] =" <td class='nomatch' style="+ borders[border_index]+" >"+ anchor + "</td>";
                    }
                else
                {
                        int rank = sample.getIsolateTrackingEngine().getRank();
                        switch ( rank )
                        {
                                case 1: 
                                {
                                        cell_data[row][col] =" <td class='green' style='" + borders[border_index] +"' >"+anchor+"</td>";
                                        break;
                                }
                                case 2:
                                {
                                        cell_data[row][col] =" <td class='orange' style='"+ borders[border_index] +"' >"+anchor+"</td>";
                                        break;
}
                                case 3:
                                {
                                    cell_data[row][col] =" <td class='yellow' style='"+ borders[border_index] +"' >"+anchor+"</td>";
                                    break;
                                }
                                case 4:
                                {
                                    cell_data[row][col] =" <td class='red' style='"+ borders[border_index] +"' >"+anchor+"</td>";
                                    break;
                                }
                                case -1://not analized
                                {
                                    cell_data[row][col] =" <td class='notanalized' style='"+ borders[border_index] +"' >"+anchor+"</td>";
                                    break;
                                }
                                case IsolateTrackingEngine.RANK_BLACK:
                                {
                                    if (sample.getIsolateTrackingEngine().getStatus() ==IsolateTrackingEngine.PROCESS_STATUS_ER_NO_LONG_READS || sample.getIsolateTrackingEngine().getStatus()==IsolateTrackingEngine.PROCESS_STATUS_ER_NO_READS)
                                    {
                                        cell_data[row][col] =" <td class='brown' style='"+ borders[border_index] +"' >"+anchor+"</td>";
                                   }
                                    else 
                                    cell_data[row][col] =" <td class='black' style='"+ borders[border_index] +"' >"+anchor+"</td>";
                                }   
                        }

                }
//System.out.println(cell_data[row][col]);
                constructid = sample.getIsolateTrackingEngine().getConstructId();
 }
	%>
	
<table width="100%" border="0" align="center">
  <th align="center">Quality Report</th>
  <tr> 
    <td><table width="85%"  align="center">
        <tr> 
          <td width="7%"  >&nbsp;</td>
          <% 

		  
		  for (int count = 1 ; count <= cols; count++)
		  {
		  %>
          <td width="7%" align="center"><strong><%= count %></strong></td>
          <%}%>
        </tr>
        <tr> 
          <td align="center"><strong>A</strong></td>
          <td colspan="<%= cols %>" rowspan="<%= rows %>" >
		  <table border="1" cellpadding="2" cellspacing="2" width="100%">
		 
<% for (int count = 0; count <  rows; count++)
{
    %> <TR> <%
    for (int count_col = 0; count_col < cols; count_col++)
    {//System.out.println(cell_data[count][count_col]);
        if (  cell_data[count][count_col] == null )
        {
            cell_data[count][count_col] = " <td class='nosample' style='border:solid 2px black' ><div align=center>" + (count+1)*(count_col+1) +"</div></td>";
                                    
        }%>
       <%= cell_data[count][count_col] %>
    <%}
 %> </TR> <%
}%>
		
	
		  </table>
        </tr>
		<%  for (int count = 66 ; count <= rows -2 + 66; count++)
		  {
		  %>
          <tr>      <td align="center"><strong><%= (char) count %></strong></td>        </tr>
          <%}%>
             </table></td>
  </tr>
</table>


<P><P></P></P>	
<table width="52%" border="0" align="center" CELLSPACING="4">
  <th colspan="2">Color Schema </th>
  <tr> 
    <td width="80%" ><strong>Best Isolate in the group:</strong></td>
    <td width="20%" class="green">&nbsp;</td>
  </tr>
  <tr> 
    <td  ><strong> Second best isolate in the group:</strong></td>
    <td  class="orange">&nbsp; </td>
  </tr>
  <tr> 
    <td ><strong>Third best isolate in the group:</strong></td>
    <td class="yellow" >&nbsp;</td>
  </tr>
  <tr> 
    <td  > <strong>Worst isolate in the group:</strong></td>
    <td class="red"></td>
  </tr>
  <tr> 
    <td ><b> Isolate that can not be used :</b></td>
    <td  class="black"  ></td>
  </tr>
  <tr> 
    <td ><b> Isolate with no data :</b></td>
    <td  class="brown"  ></td>
  </tr>
  <tr> 
    <td ><b> Wrong ORF:</b></td>
    <td  class="nomatch"  ></td>
  </tr>

  <tr> 
    <td >&nbsp;</td>
    <td    ></td>
  </tr>
  <tr> 
    <td ><b> Control:</b></td>
    <td  class="control"  ></td>
  </tr>
  <tr> 
    <td ><b> Empty sample:</b></td>
    <td  class="empty"  ></td>
  </tr>
 <tr> 
    <td ><b> Empty well:</b></td>
    <td  class="nosample" align=center  >white</td>
  </tr>
</table>

</body>
</html>