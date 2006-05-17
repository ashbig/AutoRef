<!--Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

<%@ page import="edu.harvard.med.hip.bec.util.*" %>
<%@ page import="edu.harvard.med.hip.bec.*" %>
<div align='center'>
<hr>
&nbsp;&nbsp;&nbsp;<a href="page_main.jsp?<%=Constants.JSP_TITLE%>=ACE Overview&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Overview&amp;forwardName=<%=Constants.UI_ABOUT_PAGE%>" >About</a>
&nbsp;&nbsp;&nbsp;<a href="page_main.jsp?<%=Constants.JSP_TITLE%>=ACE Help&amp;<%=Constants.JSP_CURRENT_LOCATION%>=Home > Help&amp;forwardName=<%=Constants.UI_HELP_PAGE%>" >Help</a>
&nbsp;&nbsp;&nbsp;<a href="mailto:<%=  BecProperties.getInstance().getACEEmailAddress() %>">Contact Us </a>
&nbsp;&nbsp;&nbsp;<a href="Logout.do" >Log Out</a>
</div>

