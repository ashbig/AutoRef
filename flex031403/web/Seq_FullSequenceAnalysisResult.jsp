<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="edu.harvard.med.hip.flex.Constants"%>
<%@ page import="edu.harvard.med.hip.flex.seqprocess.core.sequence.*" %>
<%@ page import="java.util.*" %>
<html>
<script>


</script>
<head><title>JSP Page</title></head>
<body>

<h1>Sequence Analysis</h1>
<html:errors/> 
<hr>
<!-- ref sequence details -->
<h2>Reference Sequence Id: 

<a href="/FLEX/ViewSequence.do?<%= Constants.FLEX_SEQUENCE_ID_KEY %>=<%= request.getAttribute("refseqid") %>">
<%= request.getAttribute("refseqid") %>
</a>

<p>

<% ArrayList full_sequences = (ArrayList) request.getAttribute("fullsequences");
if (full_sequences.size() == 0) 
{%>
    <h3.rejected>No Experimental sequence was submitted for this reference sequence</h3.rejected>
<%}
else
{
    
 %>
 <table  cellpadding=0 cellspacing=2 border=1 align='center' width='90%' >
     
     <tr class="headerRow">
        
        <TH>&nbsp;&nbsp;Full Sequence Id&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Discreptancy Summary&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Status&nbsp;&nbsp;</TH>
        <TH>&nbsp;&nbsp;Set Quality&nbsp&nbsp</TH>
        <TH>&nbsp;&nbsp;Approved By&nbsp&nbsp</TH>
        <TH>&nbsp;&nbsp;Quality&nbsp&nbsp</TH>
     </TR>

<%
        
    for (int count = 0 ; count <   full_sequences.size(); count++)
    {
        FullSequence full_seq = (FullSequence) full_sequences.get(count);%>
<flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
<tr>
<td align="CENTER">
<INPUT type="hidden" name="fullsequenceid" value="<%= full_seq.getId() %>" >
<a href="/FLEX/Seq_ViewFullSequence.do?<%= Constants.FULL_SEQUENCE_ID_KEY %>=
<%= full_seq.getId() %>" >
<%= full_seq.getId() %>
</a>
</td>
<td align="CENTER">
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
          String key = (String)keys.nextElement();
          String val = (String)full_seq.getMutationSummary().get(key);
          %>
          <tr><td><%= key %></td><td><%=val %></td></tr>
         <%}

     %>
     </table>
<%}%>


</td>
<td align="CENTER"><%= full_seq.getStatusName() %></td>
<td align="CENTER"><%= full_seq.getQualityName() %></td>

<td align="CENTER">&nbsp;<%= full_seq.getApprovedName() %></td>

<td align="CENTER">
 
    <SELECT NAME="status">
        <OPTION VALUE="<%= FullSequence.QUALITY_NOT_DEFINED %>" >Not Defined
        <OPTION VALUE="<%= FullSequence.QUALITY_STORAGE %>">Storage
        <OPTION VALUE="<%= FullSequence.QUALITY_BAD %>">Bad
        
    </SELECT>

</td>
</tr>

</flex:row>
<%}%>
</table>
<%
}%>

<p>
<P>
<div align="CENTER">
    <input type="button" value="Submit" name="submit" alt="Set sequence quality to user selection.">
    <input type="button" value="Resolve Polymorphism" name="polymorphism" alt="Start resolving polymorphism job.">
</div>

</body>
</html>
