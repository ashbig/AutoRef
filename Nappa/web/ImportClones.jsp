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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.ImportClones}"/></td>
                </tr>
            </table>
            <t:saveState id="importClonesBeanID" value="#{ImportClonesBean}"/>
            
            <h:form id="importClonesForm" enctype="multipart/form-data">
                <h:panelGrid columns="2"> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.uploadfilePrompt}:" for="importclonefile"/>
                    <h:panelGroup>
                        <t:inputFileUpload id="importclonefile" value="#{ImportClonesBean.file}" required="true"/> 
                        <h:outputLink target="_blank" value="cloneinfo.txt"> 
                            <h:outputText value="[clone sample]"/>
                        </h:outputLink> 
                        <h:outputLink target="_blank" value="control.txt"> 
                            <h:outputText value="[control sample]"/>
                        </h:outputLink>
                        <h:message styleClass="errors" for="importclonefile" />
                    </h:panelGroup>
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectImportFileFormat}:" for="formatid"/>
                    <h:selectOneRadio styleClass="text" id="formatid" value="#{ImportClonesBean.format}">	
                        <f:selectItems value="#{ImportClonesBean.fileFormats}" />
                    </h:selectOneRadio> 
                    
                    <h:outputLabel styleClass="prompt" value="#{msgs.selectIDtype}:" for="idtype"/>
                    <h:selectOneMenu styleClass="text" id="idtype" value="#{ImportClonesBean.idtype}">	
                        <f:selectItems value="#{ImportClonesBean.ids}" />
                    </h:selectOneMenu> 
                </h:panelGrid>
                
                <h:commandButton value="#{msgs.importButton}" action="#{ImportClonesBean.runImport}"/>    
            </h:form> 
            
            <h:panelGroup rendered="#{ImportClonesBean.importstatus != 0}">   
                <h:outputText styleClass="errors" value="#{ImportClonesBean.importmessage}"/>
                
                <h:panelGroup rendered="#{ImportClonesBean.importstatus > 0}">   
            
                    <h:panelGrid  columns="2"> 
                        <h:outputText styleClass="prompt" value="Protocol:"/>
                        <h:outputText styleClass="text" value="#{ImportClonesBean.c.protocol.name}"/>
                        <h:outputText styleClass="prompt" value="Researcher:"/>
                        <h:outputText styleClass="text" value="#{ImportClonesBean.c.who.name}"/>
                        <h:outputText styleClass="prompt" value="Date:"/>
                        <h:outputText styleClass="text" value="#{ImportClonesBean.c.when}"/>
                    </h:panelGrid>
                    
                    <h:form id="viewFileForm">
                        <h:outputText styleClass="prompt" value="Input objects:"/>    
                        <h:panelGrid  columns="2"> 
                            <h:outputText styleClass="prompt" value="File Name:"/>
                            <h:commandLink target="fileview" value="#{ImportClonesBean.inputfile.filename}" styleClass="text" action="#{FileViewBean.viewFile}">
                                <f:param name="fileid" value="#{ImportClonesBean.inputfile.filereferenceid}" />
                            </h:commandLink>
                        </h:panelGrid>
                    </h:form>
                    
                    <h:form id="viewContainerForm">
                        <h:outputText styleClass="prompt" value="Output objects:"/>    
                        <t:dataTable headerClass="tableheader" rowClasses="tablerowodd, tableroweven" value="#{ImportClonesBean.outputcontainers}" var="c" frame="box">
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Container"/>
                                </f:facet>
                                <h:commandLink target="containerview" value="#{c.barcode}" action="#{ContainerViewBean.viewContainer}"> 
                                    <f:param name="containerid" value="#{c.containerid}" />
                                </h:commandLink>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Type"/>
                                </f:facet>
                                <h:outputText value="#{c.type}"/>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Format"/>
                                </f:facet>
                                <h:outputText value="#{c.format}"/>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Status"/>
                                </f:facet>
                                <h:outputText value="#{c.status}"/>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Location"/>
                                </f:facet>
                                <h:outputText value="#{c.location}"/>
                            </t:column>
                        </t:dataTable>
                    </h:form>
                    
                </h:panelGroup>
            </h:panelGroup>
        </f:view>
    </body>
</html>
