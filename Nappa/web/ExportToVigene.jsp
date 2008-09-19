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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.generateVigeneFile}"/></td>
                </tr>
            </table>
            
            <h:form id="searchContainerForm">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterSlideLabelRootPromt}:" for="containerLabel"/>
                    <h:panelGroup>
                    <h:inputText styleClass="text" id="containerLabel" value="#{exportBean.label}" required="true"/>
                    <h:message styleClass="errors" for="containerLabel" />
                    </h:panelGroup>
                                        
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectTemplatePrompt}:" for="templateid"/>
                    <h:selectOneMenu styleClass="text" id="templateid" value="#{exportBean.template}">	
                        <f:selectItems value="#{exportBean.templateList}" />
                    </h:selectOneMenu> 
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{exportBean.exportFile}"/>    
            </h:form> 
            <h:outputText styleClass="errors" value="#{exportBean.message}"/>
            
        </f:view>
    </body>
</html>
