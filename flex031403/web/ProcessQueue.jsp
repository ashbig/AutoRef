<%--
        $Id: ProcessQueue.jsp,v 1.2 2001-06-04 15:26:34 dongmei_zuo Exp $ 

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
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

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
        <logic:iterate id="curItem" name="<%=edu.harvard.med.hip.flex.Constants.APPROVED_SEQUENCE_LIST_KEY%>">
            <bean:message key="flex.name"/> id:
            <flex:linkFlexSequence sequenceName="curItem" seqProperty="item">
                <bean:write name="curItem" property="item.id"/>
            </flex:linkFlexSequence>
            <BR>
            Quality Assessment: <bean:write name="curItem" property="item.quality"/>
            <BR>
            <logic:iterate id="curUser" name="curItem" property="item.requestingUsers">
                Requestor: 
                <A HREF="mailto:<bean:write name="curUser" property="userEmail"/>">
                    <bean:write name="curUser" property="username"/>
                </A>
            </logic:iterate>
            <BR>
            GI:
            <A HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="curItem" property="item.gi"/>&dopt=GenBank"> 
                <bean:write name="curItem" property="item.gi"/>
            </A>
            <BR>
            Description: <bean:write name="curItem" property="item.description"/>
            <BR>
            <P>
        </logic:iterate>
	
	<%-- display all rejected sequences --%>
	<H4> Rejected Sequences</H4>
	<logic:iterate id="curItem" name="<%=edu.harvard.med.hip.flex.Constants.REJECTED_SEQUENCE_LIST_KEY%>">
            <bean:message key="flex.name"/> id:
            <flex:linkFlexSequence sequenceName="curItem" seqProperty="item">
                <bean:write name="curItem" property="item.id"/>
            </flex:linkFlexSequence>
            <BR>
            Quality Assessment: <bean:write name="curItem" property="item.quality"/>
            <BR>
            <logic:iterate id="curUser" name="curItem" property="item.requestingUsers">
                Requestor: 
                <A HREF="mailto:<bean:write name="curUser" property="userEmail"/>">
                    <bean:write name="curUser" property="username"/>
                </A>
            </logic:iterate>
            <BR>
            GI:
            <A HREF="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&list_uids=<bean:write name="curItem" property="item.gi"/>&dopt=GenBank"> 
                <bean:write name="curItem" property="item.gi"/>
            </A>
            <BR>
            Description: <bean:write name="curItem" property="item.description"/>
            <BR>
            <P>
        </logic:iterate>


</body>
</html>