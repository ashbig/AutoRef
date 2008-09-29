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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.addReagent}"/></td>
                </tr>
            </table>
            
            <h:form id="addReagentForm">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectReagentTypePrompt}:" for="type"/>
                    <h:selectOneMenu styleClass="text" id="type" value="#{addReagentBean.type}">	
                        <f:selectItems value="#{addReagentBean.reagenttypes}" />
                    </h:selectOneMenu> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.namePrompt}:" for="name"/>
                    <h:panelGroup>
                        <h:inputText maxlength="50" id="name" value="#{addReagentBean.name}" required="true"/>
                        <h:message styleClass="errors" for="name" />
                    </h:panelGroup>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.descPrompt}:" for="desc"/>
                    <h:inputTextarea id="desc" value="#{addReagentBean.description}" required="false"/>
                </h:panelGrid>

                <h:commandButton value="#{msgs.submitButton}" action="#{addReagentBean.addReagent}"/>    
            </h:form> 
            <h:outputText styleClass="errors" value="#{addReagentBean.message}"/>
        </f:view>
    </body>
</html>
