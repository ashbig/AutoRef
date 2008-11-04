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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.searchContainers}"/></td>
                </tr>
            </table>
            
            <h:form id="searchContainersForm">
                <h:panelGrid columns="1"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterContainerLabelsPrompt}:" for="containerLabels"/>
                    <h:inputTextarea cols="50" rows="10" styleClass="text" id="containerLabels" value="#{ContainerViewBean.labels}" required="false"/>
                    <h:message styleClass="errors" for="containerLabels" />
                </h:panelGrid>

                <h:panelGrid columns="2"> 
                    <h:selectBooleanCheckbox styleClass="text" id="islikequeryid" value="#{ContainerViewBean.islikequery}" required="false"/>
                    <h:outputLabel styleClass="prompt" value="Check here to find more containers with similar labels (please enter one label only)"/>
                </h:panelGrid>
                
                <h:panelGrid columns="1"> 
                <h:outputText styleClass="text" value="To find all the containers, don't enter any label, and check the above checkbox"/>
                </h:panelGrid>       
                
                <h:commandButton value="#{msgs.submitButton}" action="#{ContainerViewBean.findContainers}"/>    
            </h:form> 
            
        </f:view>
    </body>
</html>
