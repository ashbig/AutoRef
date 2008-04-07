<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>
<%@ page import="edu.harvard.med.hip.flex.user.*"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
  

    <% User user = (User)session.getAttribute(Constants.USER_KEY);
int user_level = 0;
int CUSTOMER = 0;
int COLLABORATOR = 1;
int RESEARCHER = 2;
int WADMIN = 3; 
int SADMIN = 4;
if (user.getUserGroup().equals("Customer")) user_level = CUSTOMER;
else if (user.getUserGroup().equals("Collaborator")) user_level = COLLABORATOR;
else if (user.getUserGroup().equals("Researcher")) user_level =RESEARCHER;
else if (user.getUserGroup().equals("Workflow Admin")) user_level = WADMIN;
else if (user.getUserGroup().equals("System Admin")) user_level = SADMIN;


%>
     </head>
    <body> 
    </body>
</html>
