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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.enterResults}"/></td>
                </tr>
            </table>
            
            <t:saveState id="enterResultsControllerID" value="#{enterResultsBean.controller}" />

            <h:form id="enterResultsForm">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectResulttypePrompt}:" for="resulttype"/>
                    <h:selectOneMenu styleClass="text" id="resulttype" value="#{enterResultsBean.resulttype}">	
                        <f:selectItems value="#{enterResultsBean.resulttypes}" />
                    </h:selectOneMenu> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterContainerLabelsPrompt}:" for="containerLabels"/>
                    <h:panelGroup>
                        <h:panelGrid columns="2" columnClasses="top,top">
                            <h:inputTextarea cols="50" rows="10" styleClass="text" id="containerLabels" value="#{enterResultsBean.labelstring}" required="true"/>
                            <h:message styleClass="errors" for="containerLabels" />
                        </h:panelGrid>
                    </h:panelGroup>
                    
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{enterResultsBean.parsefiles}"/>    
            </h:form> 
            <h:outputText styleClass="errors" value="#{enterResultsBean.message}"/>
            
        </f:view>
    </body>
</html>
