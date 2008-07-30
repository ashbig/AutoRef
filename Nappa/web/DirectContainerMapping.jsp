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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.DirectContainerMapping}"/></td>
                </tr>
            </table>
            
            <t:saveState id="directContainerMapBeanid" value="#{directContainerMapBean}" />

            <h:form id="directContainerMappingForm" enctype="multipart/form-data">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectProcessPrompt}:" for="protocolid"/>
                    <h:selectOneMenu styleClass="text" id="protocolid" value="#{directContainerMapBean.protocol}" onchange="submit();" valueChangeListener="#{directContainerMapBean.changeProtocol}">	
                        <f:selectItems value="#{directContainerMapBean.protocols}" />
                    </h:selectOneMenu> 
                </h:panelGrid>
                
                <h:outputText styleClass="prompt" value="#{msgs.enterContainerLabelsPrompt}" /> 
                <h:panelGrid columns="6" columnClasses="top,top,top,top,top,top"> 
                    <h:outputLabel rendered="#{directContainerMapBean.showsrc}" styleClass="prompt" value="#{msgs.enterSrcLabelsPrompt}:" for="srcLabelsid"/>
                    <h:inputTextarea rendered="#{directContainerMapBean.showsrc}" cols="30" rows="20" styleClass="text" id="srcLabelsid" value="#{directContainerMapBean.srcLabels}" required="true"/>
                    
                    <h:outputLabel rendered="#{directContainerMapBean.showmmix}" styleClass="prompt" value="#{msgs.enterMasterMixPrompt}:" for="mmixid"/>
                    <h:inputTextarea rendered="#{directContainerMapBean.showmmix}" cols="30" rows="20" styleClass="text" id="mmixid" value="#{directContainerMapBean.mmix}" required="true"/>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterDestLabelsPrompt}:" for="destLabelsid" />
                    <h:inputTextarea cols="30" rows="20" styleClass="text" id="destLabelsid" value="#{directContainerMapBean.destLabels}" required="true"/>
                </h:panelGrid>
                
                <h:panelGrid columns="2" columnClasses="top"> 
                    <h:outputLabel rendered="#{directContainerMapBean.showfile}" styleClass="prompt" value="#{msgs.uploadMappingFilePrompt}:" for="fileid"/>
                    <t:inputFileUpload rendered="#{directContainerMapBean.showfile}" id="fileid" value="#{directContainerMapBean.file}" required="true"/>
                    
                    <h:outputLabel rendered="#{directContainerMapBean.showfile}" styleClass="prompt" value="#{msgs.selectMapFileNumFormat}:" for="formatid"/>
                    <h:selectOneRadio rendered="#{directContainerMapBean.showfile}" styleClass="text" id="formatid" value="#{directContainerMapBean.format}">	
                        <f:selectItems value="#{directContainerMapBean.fileFormats}" />
                    </h:selectOneRadio> 
                    
                    <h:outputLabel rendered="#{directContainerMapBean.showlogfile}" styleClass="prompt" value="#{msgs.uploadLogFilePrompt}:" for="logfileid"/>
                    <t:inputFileUpload rendered="#{directContainerMapBean.showlogfile}" id="logfileid" value="#{directContainerMapBean.logfile}" required="true"/>
                    
                    <h:outputLabel rendered="#{directContainerMapBean.shownumofslide}" styleClass="prompt" value="#{msgs.numofslidePrompt}:" for="numofslideid"/>
                    <h:inputText rendered="#{directContainerMapBean.shownumofslide}" id="numofslideid" value="#{directContainerMapBean.numofslide}" required="true"/>
                    
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{directContainerMapBean.doMapping}"/>    
            </h:form> 
            
            <h:panelGroup rendered="#{directContainerMapBean.status}">   
                <h:outputText styleClass="errors" value="#{directContainerMapBean.message}"/>
            </h:panelGroup>
            
            <h:panelGroup rendered="#{directContainerMapBean.showcontainer}">   
                <h:form id="viewProcessForm"> 
                    <h:commandLink target="_blank" action="#{processViewBean.viewProcess}">
                        <h:outputText styleClass="text" value="View Detail"/>
                        <f:param name="executionid" value="#{directContainerMapBean.executionid}"/>
                    </h:commandLink>
                </h:form>
            </h:panelGroup>
            
        </f:view>
    </body>
</html>
