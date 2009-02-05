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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.uploadMicrovigene}"/></td>
                </tr>
            </table>
            <t:saveState id="uploadMicrovigeneBeanID" value="#{uploadMicrovigeneBean}"/>
            
            <h:outputText styleClass="errors" value="#{uploadMicrovigeneBean.message}"/>
            <h:form id="uploadMicrovigeneForm" enctype="multipart/form-data">
                <h:panelGrid columns="2"> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.enterContainerLabelPrompt}:" for="containerLabel"/>
                    <h:panelGroup>
                            <h:inputText styleClass="text" id="containerLabel" value="#{uploadMicrovigeneBean.label}" required="true"/>
                            <h:message styleClass="errors" for="containerLabel" />
                    </h:panelGroup>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.uploadMicrovigene}:" for="vigenefile"/>
                    <h:panelGroup>
                        <t:inputFileUpload id="vigenefile" value="#{uploadMicrovigeneBean.file}" required="true"/> 
                        <h:message styleClass="errors" for="vigenefile" />
                    </h:panelGroup>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.uploadImagefilePrompt}:" for="imagefile"/>
                    <h:panelGroup>
                        <t:inputFileUpload id="imagefile" value="#{uploadMicrovigeneBean.imagefile}" required="true"/> 
                        <h:message styleClass="errors" for="imagefile" />
                    </h:panelGroup>
                
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{uploadMicrovigeneBean.runUpload}"/>    
            </h:form> 
        </f:view>
    </body>
</html>
