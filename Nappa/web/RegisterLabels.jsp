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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.RegisterLabels}"/></td>
                </tr>
            </table>
            
            <h:form id="registerLabelsForm">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.containertypePrompt}:" for="containertypeid"/>
                    <h:selectOneMenu styleClass="text" id="containertypeid" value="#{registerLabelsBean.containertype}">	
                        <f:selectItems value="#{registerLabelsBean.containertypes}" />
                    </h:selectOneMenu> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterContainerLabelsPrompt}:" for="containerlabels"/>
                    <h:panelGroup>
                        <h:panelGrid columns="2" columnClasses="top,top">
                            <h:inputTextarea cols="30" rows="15" styleClass="text" id="containerlabels" value="#{registerLabelsBean.labels}" required="true"/>
                            <h:message styleClass="errors" for="containerlabels" />
                        </h:panelGrid>
                    </h:panelGroup>
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{registerLabelsBean.registerLables}"/>    
            </h:form> 
            
            <h:panelGroup rendered="#{registerLabelsBean.status}">   
                <h:outputText styleClass="errors" value="#{registerLabelsBean.message}"/>
            </h:panelGroup>
            
        </f:view>
    </body>
</html>
