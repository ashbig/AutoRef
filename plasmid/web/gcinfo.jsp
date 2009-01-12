<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="gcm" class="plasmid.database.DatabaseManager.GrowthConditionManager" />
<jsp:useBean id="gc" class="plasmid.coreobject.GrowthCondition" />
<%
	String sgc = request.getParameter("GC");
        if ((sgc == null) || (sgc.length() < 1)) {
            gc = null;
        } else {
            gc = gcm.getGrowthCondition(sgc);
        }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
        <title>Growth Condition Detail Information - <%=sgc %></title>
</head>
<body>
    <%
        if (gc == null) {
    %>
<div align="center" style="background-color: #EEFFEE;"><font size=+1>Can not find detail information regarding Growth Condition <%=sgc %></font></div>
    <%
        } else {
    %>
<div align="center" style="background-color: #EEFFEE;"><font size=+1>Detail Information of Growth Condition <%=sgc %></font></div>
<p>
    <table width="500px" width="1px">
        <tr><td align="right">Growth Condition&nbsp;</td><td align="left"><%=gc.getName()%></td></tr>
        <tr><td align="right">Host Type&nbsp;</td><td align="left"><%=gc.getHosttype()%></td></tr>
        <tr><td align="right">Antibiotic Selection&nbsp;</td><td align="left"><%=gc.getSelection()%></td></tr>
        <tr><td align="right">Description&nbsp;</td><td align="left"><%=gc.getCondition()%></td></tr>
        <tr><td align="right">Comment&nbsp;</td><td align="left"><%=gc.getComments()%></td></tr>
    </table>
</p>
    <%
        }
    %>
</body>
</html>
