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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.performExperiment}"/></td>
                </tr>
            </table>
            <t:saveState id="experimentBeanID" value="#{experimentBean}"/>
            
            <h:outputText styleClass="errors" value="#{experimentBean.message}" />
            <h:form id="experimentForm" enctype="multipart/form-data">
                <h:panelGrid columns="2" columnClasses="top, top"> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.experimentDatePrompt}:" for="dateid"/>
                    <h:panelGroup>
                        <h:inputText size="50" styleClass="text" id="dateid" value="#{experimentBean.date}">
                        <f:convertDateTime pattern="MM/dd/yyyy"/>
                        </h:inputText>
                            <h:message styleClass="errors" for="dateid" />
                    </h:panelGroup>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterContainerLabelsPrompt}:" for="containerLabelsid"/>
                    <h:panelGroup>
                        <h:inputTextarea cols="50" rows="10" styleClass="text" id="containerLabelsid" value="#{experimentBean.labels}"/>
                            <h:message styleClass="errors" for="containerLabelsid" />
                    </h:panelGroup>
                                        
                    <h:outputLabel styleClass="prompt" value="#{msgs.addVariables}:" />
                    <h:panelGroup>
                        <h:panelGrid columns="2"> 
                            <h:outputLabel styleClass="prompt" value="#{msgs.enterTypePrompt}:" for="typeid" />
                            <h:selectOneMenu styleClass="text" id="typeid" value="#{experimentBean.type}">
                                <f:selectItems value="#{experimentBean.variableTypes}"/>
                            </h:selectOneMenu>

                            <h:outputLabel styleClass="prompt" value="#{msgs.enterValuePrompt}:" for="valueid" />
                            <h:panelGroup>
                                <h:inputText styleClass="text" id="valueid" value="#{experimentBean.value}" required="false"/>
                                <h:message styleClass="errors" for="valueid" />
                            </h:panelGroup>

                            <h:outputLabel styleClass="prompt" value="#{msgs.enterExtrainfoPrompt}:" for="extraid" />
                            <h:panelGroup>
                                <h:inputText styleClass="text" id="extraid" value="#{experimentBean.extra}" required="false"/>
                                <h:message styleClass="errors" for="extraid" />
                            </h:panelGroup>
                            
                            <h:commandButton value="#{msgs.addVariableButton}" action="#{experimentBean.addVariable}"/> 
                        </h:panelGrid> 
                    </h:panelGroup>
                    
                    <t:dataTable value="#{experimentBean.variables}" var="var" rendered="#{experimentBean.showVariables}" border="1">    
                        <t:column>
                        <f:facet name="header">
                            <h:outputText value="Type"/>
                        </f:facet>
                        <h:outputText value="#{var.type}"/>
                        </t:column>                        
                        <t:column>
                        <f:facet name="header">
                            <h:outputText value="Value"/>
                        </f:facet>
                        <h:outputText value="#{var.value}"/>
                        </t:column>
                        <t:column>
                        <f:facet name="header">
                            <h:outputText value="Extra Information"/>
                        </f:facet>
                        <h:outputText value="#{var.extra}"/>
                        </t:column>
                </t:dataTable>
                
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{experimentBean.performExperiment}"/>    
            </h:form> 
        </f:view>
    </body>
</html>
