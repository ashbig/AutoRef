<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="nappa.css" rel="stylesheet" type="text/css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <f:view>
            <f:loadBundle basename="bean.messages" var="msgs"/>
            
            <table width="100%">
                <tr>
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.ViewReagent}"/></td>
                </tr>
            </table>
            
            <h:panelGrid  columns="2"> 
                <h:outputText styleClass="prompt" value="Label"/>
                <h:outputText styleClass="text" value="#{ReagentViewBean.reagent.name}"/>
                <h:outputText styleClass="prompt" value="Type"/>
                <h:outputText styleClass="text" value="#{ReagentViewBean.reagent.type}"/>
                <h:outputText styleClass="prompt" value="Format"/>
                <h:outputText styleClass="text" value="#{ReagentViewBean.reagent.description}"/>
            </h:panelGrid>
        </f:view>
    </body>
</html>
