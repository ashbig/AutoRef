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
<%@ page import="edu.harvard.med.hip.bec.ui_objects.*" %>
<%-- The container that was searched --%>
<head>
<link href="application_styles.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>scripts.js"></script>

<%

String[] border_line_style ={"solid","dotted","dashed"}; 
String[] border_line_color={"#FF00FF", "#00FF00", "#FF0000","#00FFFF","#6B8E23","#DAA520","black","#D2691E","#9400D3","#708090"};

int border_line_style_counter = 0;
int border_line_color_counter = 0;
%>

</head>
<html>

<body>
<jsp:include page="NavigatorBar_Administrator.jsp" /> 
<p><P><br>
<table border="0" cellpadding="0" cellspacing="0" width="74%" align=center>
<tr><td > <font color="#008000" size="5"><b> Container Quality Report</font><hr>
<p></td> </tr></table>

<div align="center">
<center><table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr> <td width="100%"><html:errors/></td></tr></table></center></div>
<p></p>
<% 

Container container = (Container)request.getAttribute("container") ;
%>
<table border="0" cellpadding="0" cellspacing="0" width="84%" align=center>
<tr>     <td width="19%"><strong>Label: </strong></td>    <td width="81%">       <%= container.getLabel() %>    </td>  </tr>
<tr> <td><strong>Container Id: </strong></td><td>       <%= container.getId() %>    </td>  </tr>
<tr>     <td><strong>Container Type: </strong></td>    <td>       <%= container.getType() %>    </td>  </tr>
 <tr><td><strong>Cloning Strategy: </strong></td><td> 
    <% if ( container.getCloningStrategyId() != BecIDGenerator.BEC_OBJECT_ID_NOTSET){%> 
      <a href="<%= edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") %>Seq_GetItem.do?forwardName=<%= Constants.CLONING_STRATEGY_DEFINITION_INT %>&amp;ID=<%= container.getCloningStrategyId() %>">
	    <%= container.getCloningStrategyId() %></A>
	    <%} else {%> &nbsp; <%}%>    </td></tr>
</table><P><P></P></P>

	

 <%
//build array of cell entries
        int rows = ((Integer)request.getAttribute("rows")).intValue();
        int cols = ((Integer)request.getAttribute("cols")).intValue();

        int forwardName = ((Integer)request.getAttribute("forwardName")).intValue();
	UICloneSample sample = null;	int constructid = -1; int border_index = -1;
        String[][] cell_data = new String[rows][cols];
        int row = 0; int col = 0;
        String anchor = null;
        Hashtable construct_border_style = new Hashtable();
        String border_style = null;
        for (int count = 0; count < container.getSamples().size(); count ++)
        {   
                sample = (UICloneSample)container.getSamples().get(count);
                if ( border_line_color_counter > border_line_color.length -1) 
                        border_line_color_counter = 0;
                if ( border_line_style_counter >   border_line_style.length - 1)
                    border_line_style_counter = 0;
                border_style = "border:"+border_line_style[border_line_style_counter++]+" 2px "+border_line_color[border_line_color_counter++];
                construct_border_style.put( new Integer( sample.getConstructId()), border_style);
        }
         for (int count = 0; count < container.getSamples().size(); count ++)
        {	
                sample = (UICloneSample)container.getSamples().get(count);
if ( forwardName == Constants.CONTAINER_RESULTS_VIEW)
{
                anchor = "<A HREF=\"\" onClick=\"window.open('"+
edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") +"Seq_GetItem.do?forwardName="+ Constants.SAMPLE_ISOLATE_RANKER_REPORT + "&amp;ID="+ sample.getIsolateTrackingId()+"&amp;container_label="+container.getLabel()+"','newWndNt','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;\"><div align=center>"+ sample.getPosition()+"</div></a>";
}
/*if (forwardName == Constants.PROCESS_APROVE_ISOLATE_RANKER && sample.getIsolateTrackingEngine() != null)
{
   anchor = "<A HREF=\"\" onClick=\"window.open('"
    + edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("JSP_REDIRECTION") 
    + "Seq_GetItem.do?forwardName="+ Constants.CONSTRUCT_DEFINITION_REPORT 
		+ "&amp;ID="+ sample.getIsolateTrackingEngine().getConstructId()+"','"+sample.getIsolateTrackingEngine().getConstructId()+"','width=500,height=400,menubar=no,location=no,scrollbars=yes,resizable=yes');return false;\"><div align=center>"+ sample.getPosition()+"</div></a>";
}*/
                row = Algorithms.convertWellNumberIntoRowNumber(sample.getPosition())-1;
                col =Algorithms.convertWellNumberIntoColNumber(sample.getPosition() ) -1;
                constructid = sample.getConstructId();
                border_style = (String)construct_border_style.get( new Integer(constructid));
                if ( sample.getSampleType().equals("CONTROL_POSITIVE") ||  sample.getSampleType().equals("CONTROL_NEGATIVE"))
		{
			cell_data[row][col] =" <td class='control'><div align=center>"+sample.getPosition()+"</div></td>";
		}
                else if ( sample.getSampleType().equals("EMPTY") )
                {

                      cell_data[row][col] =" <td class='empty' style = '"+ border_style + "' ><div align=center>"+sample.getPosition()+"</div></td>";
          
}
                else if ( sample.getCloneStatus()  == IsolateTrackingEngine.PROCESS_STATUS_ER_ANALYZED_NO_MATCH ||
                            sample.getCloneStatus() == IsolateTrackingEngine.PROCESS_STATUS_CLONE_SEQUENCE_ANALYZED_NO_MATCH)
                    {
                        cell_data[row][col] =" <td class='nomatch' style='"+ border_style+"' >"+ anchor + "</td>";
                  
}
                else
                {
                        int rank = sample.getRank();
                        switch ( rank )
                        {
                                case 1: 
                                {
                                        cell_data[row][col] =" <td class='green' style='" + border_style +"' >"+anchor+"</td>";
                                        break;
                                }
                                case 2:
                                {
                                        cell_data[row][col] =" <td class='orange' style='"+ border_style +"' >"+anchor+"</td>";
                                        break;
}
                                case 3:
                                {
                                    cell_data[row][col] =" <td class='yellow' style='"+border_style +"' >"+anchor+"</td>";
                                    break;
                                }
                                case 4:
                                {
                                    cell_data[row][col] =" <td class='red' style='"+ border_style +"' >"+anchor+"</td>";
                                    break;
                                }
                                case -1://not analized
                                {
                                    cell_data[row][col] =" <td class='notanalized' style='"+ border_style +"' >"+anchor+"</td>";
                                    break;
                                }
                                case IsolateTrackingEngine.RANK_BLACK:
                                {
                                    if (sample.getCloneStatus()  ==IsolateTrackingEngine.PROCESS_STATUS_ER_NO_LONG_READS || sample.getCloneStatus()==IsolateTrackingEngine.PROCESS_STATUS_ER_NO_READS)
                                    {
                                        cell_data[row][col] =" <td class='brown' style='"+ border_style +"' >"+anchor+"</td>";
                                   }
                                    else 
                                    cell_data[row][col] =" <td class='black' style='"+ border_style +"' >"+anchor+"</td>";
                                }   
                        }
               }
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
    {
        if (  cell_data[count][count_col] == null )
        {
            cell_data[count][count_col] = " <td class='nosample' style='border:solid 2px black' ><div align=center>" + ((count+1)+(count_col)* 8) +"</div></td>";
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