<%--
        $Id: ProcessQueue.jsp,v 1.1 2001-06-01 18:10:50 dongmei_zuo Exp $ 

        File    : ProcessQueue.jsp
        Date    : 05102001
        Author  : Juan Munoz

        Processes workflow managers requests from PendingRequests UI and 
	displays a summary.
--%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
<head><title>Request Processed Summary</title></head>
<body>
	<H1> Request Summary </H1>
	<%-- display summary of requests processed --%>
<TABLE>
<TR><TD>Requests Processed: <bean:write name="<%=edu.harvard.med.hip.flex.Constants.PROCESSED_SEQ_NUM_KEY%>" /></TD>
<TD>Pending Requests: <bean:write name="<%=edu.harvard.med.hip.flex.Constants.PENDING_SEQ_NUM_KEY%>" /></TD></TR>
</TABLE>
	<%--display all accepted sequences--%>
	<H4>Accepted Sequences</H4>
	<%
	Iterator acceptedSeqIter = acceptedList.iterator();
	while(acceptedSeqIter.hasNext()) {
		QueueItem curItem = (QueueItem)acceptedSeqIter.next();
		FlexSequence curSeq = (FlexSequence)curItem.getItem();
		curItem.setProtocol(designProtocol);
		

		// update the status to be INPROCESS
		curSeq.updateStatus("INPROCESS");

		// hit db and get current values for the sequence
		curSeq.restore(curSeq.getId(), DatabaseTransaction.getInstance());
		Iterator userIter= curSeq.getRequestingUsers().iterator();
	%>
	<A HREF="SequenceUI.jsp?flexID=<%=curSeq.getId()%>"><%=curSeq.getId()%></A><BR>
	Quality Assessment: <%=curSeq.getQuality()%><BR>
	<%
	while(userIter.hasNext()) {
		User curUser =  (User)userIter.next();
	%>
	Requestor: <A HREF="mailto:<%=curUser.getUserEmail()%>"> <%=curUser.getUsername()%> </A> <BR>
	<%	} //end of user while
	%>
	<A HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<%=curSeq.getGi()%>&dopt=GenBank"> <%=curSeq.getAccession()%></A><BR>
	Description: <%=curSeq.getDescription()%> <BR>
	<P>
	
	<%
	} //end of accepted seq iter.
	%>




	<%-- display all rejected sequences --%>
	<H4> Rejected Sequences</H4>
	<%	
	Iterator rejectedSeqIter = rejectedList.iterator();
	while(rejectedSeqIter.hasNext()) {
		FlexSequence curSeq = (FlexSequence)((QueueItem)rejectedSeqIter.next()).getItem();
		// update the status to REJECTED
		curSeq.updateStatus("REJECTED");
		curSeq.restore(curSeq.getId(), DatabaseTransaction.getInstance());
		Iterator userIter= curSeq.getRequestingUsers().iterator();
	%>
	<A HREF="SequenceUI.jsp?flexID=<%=curSeq.getId()%>"><%=curSeq.getId()%></A><BR>
	Quality Assessment: <%=curSeq.getQuality()%><BR>
	<%
	while(userIter.hasNext()) {
		User curUser =  (User)userIter.next();
	%>
	Requestor: <A HREF="mailto:<%=curUser.getUserEmail()%>"> <%=curUser.getUsername()%> </A> <BR>
	<%	} //end of user while
	%>
	<A HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<%=curSeq.getGi()%>&dopt=GenBank"> <%=curSeq.getAccession()%></A><BR>
	Description: <%=curSeq.getDescription()%> <BR>
	<P>
	
	<%
	} //end of rejected seq iter.
	%>


</body>
</html>

<%
//add all the accepted items to the queue as 'design constructs' protocol
sequenceQueue.addQueueItems(acceptedList, DatabaseTransaction.getInstance());
DatabaseTransaction.getInstance().abort();
%>




