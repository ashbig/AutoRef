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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.addProgram}"/></td>
                </tr>
            </table>
            
            <h:form id="addProgramForm" enctype="multipart/form-data">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectProgramPrompt}:" for="type"/>
                    <h:selectOneMenu styleClass="text" id="type" value="#{addProgramBean.type}">	
                        <f:selectItems value="#{addProgramBean.allPrograms}" />
                    </h:selectOneMenu> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.programname}:" for="name"/>
                    <h:panelGroup>
                        <h:inputText id="name" value="#{addProgramBean.name}" required="true"/>
                        <h:message styleClass="errors" for="name" />
                    </h:panelGroup>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.programdescription}:" for="desc"/>
                    <h:inputTextarea id="desc" value="#{addProgramBean.description}" required="false"/>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.uploadMappingFilePrompt}:" for="fileid"/>
                    <h:panelGroup>
                        <t:inputFileUpload id="fileid" value="#{addProgramBean.file}" required="true"/>    
                        <h:message styleClass="errors" for="fileid" />       
                    </h:panelGroup>        
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectMapFileNumFormat}:" for="formatid"/>
                    <h:selectOneRadio styleClass="text" id="formatid" value="#{addProgramBean.format}">	
                        <f:selectItems value="#{addProgramBean.fileFormats}" />
                    </h:selectOneRadio> 
                    
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.submitButton}" action="#{addProgramBean.addProgram}"/>    
            </h:form> 
            <h:outputText styleClass="errors" value="#{addProgramBean.message}"/>
        </f:view>
    </body>
</html>
