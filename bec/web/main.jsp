<%@page contentType="text/html"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<HTML>
    <HEAD><TITLE><bean:message key="bec.name"/></TITLE>
    </HEAD>
		<frameset cols="15%,*" border=0>
  				<frame src="MainMenu.jsp" name="menu" marginwidth=0 bgcolor="beige">
  				<frame src="overview.jsp" name="display">
		</frameset> 
</HTML>
