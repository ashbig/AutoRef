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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.lowNorm}"/></td>
                </tr>
            </table>
             
            <h:form id="lowNormFormid">
            <t:saveState id="lowNormController" value="#{lowNormBean.controller}"/>
            <t:saveState id="slides1" value="#{lowNormBean.slides1}"/>
            <t:saveState id="slides2" value="#{lowNormBean.slides2}"/>
            <t:saveState id="foundSlides" value="#{lowNormBean.foundSlides}"/>
            <t:saveState id="selectedSlides" value="#{lowNormBean.selectedSlides}"/>
            <t:saveState id="selectedControls" value="#{lowNormBean.selectedControls}"/>
            <t:saveState id="controls" value="#{lowNormBean.controls}"/>
            <t:saveState id="background" value="#{lowNormBean.background}"/>
            <t:saveState id="variation" value="#{lowNormBean.variation}"/>
            <t:saveState id="histogram" value="#{lowNormBean.histogram}"/>
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:panelGroup>
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterSlideLabelRootPromt}:" for="barcodeid"/>
                    <h:inputText size="20" styleClass="text" id="barcodeid" value="#{lowNormBean.barcode}" required="true"/>
                    </h:panelGroup>
                    
                    <h:panelGroup>
                    <h:outputLabel styleClass="prompt" value="number from " for="fromid"/>
                    <h:inputText size="10" styleClass="text" id="fromid" value="#{lowNormBean.from}" required="false"/>
                    <h:outputLabel styleClass="prompt" value="to " for="toid"/>
                    <h:inputText size="10" styleClass="text" id="toid" value="#{lowNormBean.to}" required="false"/>
                    <h:commandButton value="#{msgs.searchButton}" action="#{lowNormBean.findSlides}"/>
                    </h:panelGroup>
               </h:panelGrid> 
               
               <h:panelGrid columns="2" columnClasses="top,top"> 
                   <h:outputText styleClass="prompt" value="Found slides"/>
                   <h:outputText styleClass="prompt" value="Selected slides"/>
                    
                    <h:selectManyMenu style="width: 150px; height: 150px;" value="#{lowNormBean.slides1}">
                        <f:selectItems value="#{lowNormBean.foundSlides}"/>
                    </h:selectManyMenu>
                    <h:selectManyMenu style="width: 150px; height: 150px;" value="#{lowNormBean.slides2}">
                        <f:selectItems value="#{lowNormBean.selectedSlides}"/>
                    </h:selectManyMenu>
                    
                    <h:commandButton value="#{msgs.addButton}" action="#{lowNormBean.addSlides}"/>  
                    <h:commandButton value="#{msgs.removeButton}" action="#{lowNormBean.removeSlides}"/>  
                </h:panelGrid>
                <p></p>
                <h:outputText styleClass="subtitle" value="Low level normalization"/>
                <h:panelGrid columns="2" columnClasses="top,top">
                    <h:selectBooleanCheckbox styleClass="text" value="#{lowNormBean.background}"/>
                    <h:outputText styleClass="text" value="Subtract background"/>
                    <h:outputText value=" "/>
                    <h:selectManyCheckbox styleClass="text" value="#{lowNormBean.selectedControls}">
                            <f:selectItems value="#{lowNormBean.controls}"/>
                    </h:selectManyCheckbox>
                    <h:outputText value=" "/>
                    <h:panelGroup>
                            <h:outputText styleClass="text" value="Calculate background using "/>
                            <h:inputText styleClass="text" value="#{lowNormBean.percentile}"/>
                            <h:outputText styleClass="text" value=" percentile"/>
                   </h:panelGroup>
                   <h:selectBooleanCheckbox styleClass="text" value="#{lowNormBean.variation}"/>
                   <h:outputText styleClass="text" value="Normalize slide variation"/>
                   
                   <h:selectBooleanCheckbox styleClass="text" value="#{lowNormBean.histogram}"/>
                    <h:outputText styleClass="text" value="Generate histogram"/>
                </h:panelGrid>
                
               <h:outputLabel styleClass="prompt" value="Please name this process:" for="nameid"/>
               <h:inputText size="30" styleClass="text" id="nameid" value="#{lowNormBean.name}" required="true"/>
               
               <h:commandButton value="#{msgs.submitButton}" action="#{lowNormBean.parsefiles}"/>    
            </h:form> 
            <h:outputText styleClass="errors" value="#{lowNormBean.message}"/>
            
        </f:view>
    </body>
</html>
