<%@ page contentType="text/html"%>
<%@ page language="java" %>


<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>
<%@ page import="edu.harvard.med.hip.flex.process.*" %>
<%@ page import="java.util.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%-- The container that was searched --%>
<logic:present name="<%=Constants.CONTAINER_KEY%>"> 
    <bean:define name="<%=Constants.CONTAINER_KEY%>" id="searchContainer"/>
</logic:present>
<html>
<head>
    <title><bean:message key="flex.name"/> : Container Process History</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
    <h2><bean:message key="flex.name"/> : Container Process History</h2>
    <hr>
    <html:errors/>
    <p>
    <table border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>Protocol</th>
        <th>Execution Date</th>
        <th>Subprotocol</th>
        <th>Researcher</th>
        <th>Container</th>
        <th>Container Type</th>
    </tr>
<% ContainerThread elements  = (ContainerThread) request.getAttribute(Constants.THREAD_KEY);
Container target = (Container) request.getAttribute(Constants.CONTAINER_KEY);
int count = 1;
   Iterator iter = elements.getElements().iterator();
   while(iter.hasNext()) 
   {
         ThreadElement tr = (ThreadElement)iter.next();

        edu.harvard.med.hip.flex.process.Process p = tr.getProcess();
        Protocol pr = null;
        if (p != null)     {        pr = p.getProtocol();     }
        Container c = (Container)tr.getObject();

        if (c.getId() == target.getId()){%><tr class='highlightRow'><%}
        else if (count % 2 == 0)  {%><tr class ='evenRow'><%}
        else if (count % 2 == 1){%><tr class="oddRow"><%}%>
        <% if (p != null)
        {
            String link = "/FLEX/ViewContainerDetails.do?CONTAINER_ID=" + c.getId()  +"&PROCESS_ID="+p.getExecutionid();
      
            %>
                <td> <%= pr.getProcessname() %></td>
                <td> <%= p.getDate() %></td>
                <% if (p.getSubprotocol()  != null){ %>
                    <td> <%= p.getSubprotocol() %></td>
                <%}else {%>
                     <td> &nbsp;</td>
                <%}%>
                <td> <%= p.getResearcher().getName() %></td>
                <td> <a href= <%= link %>  >
                        <%= c.getLabel() %>
                      </a></td>
                <td> <%= c.getType() %></td>
        <%}
        else
        {
             String link_mgc = "/FLEX/MgcViewContainerDetails.do?CONTAINER_ID=" +  c.getId();
            %>
            <td> &nbsp;</td>
            <td> &nbsp;</td>
            <td> &nbsp;</td>
            <td> &nbsp;</td>
            <td> <a href= "<%= link_mgc %>"    >
                <%= c.getLabel() %>
                 </a>
            </td>
             <td> <%= c.getType() %></td>
        <%}%>
        </tr>    
    <%
    count++;
    }
%>

    
    </table>
</body>
</html>