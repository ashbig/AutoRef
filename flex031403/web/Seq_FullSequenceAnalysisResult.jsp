<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.sequence.*" %>
<html>
<script>


</script>
<head><title>JSP Page</title></head>
<body>

<h1>Sequence Analysis</h1>
<html:errors/> 
<!-- ref sequence details -->
<h2>Reference Sequence Id: 

<a href="/FLEX/ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<%= request.getAttribute("refseqid") %>">
<%= request.getAttribute("refseqid") %>
</a>
<hr>
<p>

<% ArrayList full_sequences = (ArrayList) request.getAttribute("full_sequences");
if (full_sequences.size() == 0) 
{%>
    <h3.rejected>No Experimental sequence was submitted for this reference sequence</h3.rejected>
<%}
else
{
    
 %>
 <table  cellpadding=0 cellspacing=2 border=1>
     
     <tr class="headerRow">
        
        <TH>&nbsp;&nbsp;Full Sequence Id&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Discreptancy Summary&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Status&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Suggested Quality&nbsp&nbsp</TH>
        <TH>&nbsp;&nbsp;Quality&nbsp&nbsp</TH>
     </TR>

<%
        
    for (int count = 0 ; count <   full_sequences.size(); count++)
    {
        FullSequence full_seq = (FullSequence) full_sequencesget(count);%>
<flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
<tr>
<td>
<a href="/FLEX/Seq_ViewFullSequence.do?<%= Constants.FULL_SEQUENCE_ID_KEY %>=
<%= full_seq.getId() %>">
<%= full_seq.getId() %>
</a>
</td>
<td>
<% if ( full_seq.getMutationSummary().size() == 0)
{%>
    <b><i>No Discreptancy</i></b>
<%}
else
{%>
    <table  cellpadding=0 cellspacing=2 border=0>
     <tr class="headerRow">
        <TH>&nbsp;&nbsp;Type&nbsp&nbsp</TH>
        <TH>&nbsp;&nbsp;Number&nbsp&nbsp</TH>
     </TR>
     <%
        Enumeration keys = full_seq.getMutationSummary().keys();
        while ( keys.hasMoreElements() )
        {
          String key = keys.nextElement());
          String val = (String)full_seq.getMutationSummary().get(key);
          %>
          <tr><td>key</td><td>val</td></tr>
         <%}

     %>
     </table>
<%}%>


</td>
<td><%= full_seq.getStatusName() %>"></td>
<td><%= full_seq.getQualityName() %>"></td>
<td>
</td>
</tr>

</flex:row>
<%}%>
</table>
<%
}%>


</body>
</html>
