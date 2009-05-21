<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@page import="util.Constants" %> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <f:view>
        <t:saveState id="prehistogramBeanid" value="#{prehistogramBean}"/>
        <h:outputLink value="#{prehistogramBean.plotFileURL}">
            <h:outputText value="View Zone Variation"/>
        </h:outputLink>
        </f:view>
    </body>
</html>
