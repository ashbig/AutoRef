<%--
        $Id: PendingRequests.jsp,v 1.1 2001-05-31 15:07:33 dongmei_zuo Exp $ 

        File    : PendingRequests.jsp
        Date    : 05042001
        Author  : Juan Munoz

        Display the list of pending sequence requests and allow the 
	Workflow manager to Accept, Reject or leave the sequence
	as pending
--%>

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts.tld" prefix="struts" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head><title>Pending Requests</title></head>
<body>
    <H1><CENTER>Pending Requests</CENTER></H1> 
    <BR>
    <FORM method="POST" action="ProcessQueue.jsp">

    <TABLE BORDER>
    <TR>
        <TH>FLEX id</TH>
        <TH>Description</TH>
        <TH>Quality</TH>
        <TH>GenBank</TH>
        <TH>Requesting Users</TH>
        <TH>Status</TH>
    </TR>
<%
    /*
     * for each sequence in the queue, get all info
     * from db and display it
     */
    Iterator seqIter = approveSeqList.iterator();
    int listIndex = 0;
    while(seqIter.hasNext()) {
        FlexSequence curSeq = (FlexSequence)((QueueItem)seqIter.next()).getItem();
	curSeq.restore(curSeq.getId(), DatabaseTransaction.getInstance());
	Iterator userIter= curSeq.getRequestingUsers().iterator();
%>
    <TR>
        <TD>
            <A HREF="SequenceUI.jsp?flexID=<%=curSeq.getId()%>"><%=curSeq.getId()%></A><BR>
        </TD>
        <TD>
            <%=curSeq.getDescription()%>
        </TD>
        <TD>
            <%=curSeq.getQuality()%>
        </TD>
        <TD>
            <A HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<%=curSeq.getGi()%>&dopt=GenBank"> <%=curSeq.getAccession()%></A><BR>
        </TD>
        <TD>

<%
    while(userIter.hasNext()) {
        User curUser =  (User)userIter.next();
%>
        <A HREF="mailto:<%=curUser.getUserEmail()%>"> <%=curUser.getUsername()%> </A> <BR>
<%	
    }
%>
        </TD>


        <TD>
            <SELECT name="<%=listIndex%>">
                <Option > Pending
                <Option > Rejected
                <Option> Accepted
            </SELECT>
        </TD>
    <TR>
<%
    listIndex++;
} // end while
%>
</TABLE>
<INPUT title="Process Requests" name="Process Requests" type="submit">
<INPUT type="reset">
</FORM>
</body>


</html>






