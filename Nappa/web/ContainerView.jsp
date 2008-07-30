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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.ViewContainer}"/></td>
                </tr>
            </table>
            
            <h:panelGrid  columns="2"> 
                <h:outputText styleClass="prompt" value="Label"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.containerheader.barcode}"/>
                <h:outputText styleClass="prompt" value="Type"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.containerheader.containertype.type}"/>
                <h:outputText styleClass="prompt" value="Format"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.containerheader.format}"/>
                <h:outputText styleClass="prompt" value="Status"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.containerheader.status}"/>
                <h:outputText styleClass="prompt" value="Location"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.containerheader.location}"/>
            </h:panelGrid>
            
            <h:outputText styleClass="prompt" value="Samples:"/> 
            <h:form id="viewSampleDetailForm1"> 
                <h:panelGrid  columns="3"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.changeCultureThreashold}:" for="culturecut"/>
                    <h:inputText id="culturecut" value="#{ContainerViewBean.culturecut}"/>  
                    <h:commandButton value="#{msgs.submitButton}" action="#{ContainerViewBean.changeCulturecut}"/> 
                </h:panelGrid>     
            </h:form>
            <h:form id="viewSampleDetailForm2"> 
                <h:panelGrid  columns="3"> 
                    <h:outputLabel styleClass="prompt" value="#{msgs.changeDnaThreashold}:" for="dnacut"/>
                    <h:inputText id="dnacut" value="#{ContainerViewBean.dnacut}"/>  
                    <h:commandButton value="#{msgs.submitButton}" action="#{ContainerViewBean.changeDnacut}"/> 
                </h:panelGrid>     
            </h:form>
            
            <h:panelGrid columns="2" columnClasses="top,top"> 
                <h:panelGroup>
                    <h:form id="viewSampleDetailForm">
                        <t:dataTable id="table" value="#{ContainerViewBean.plateModel}" var="plate" frame="box">
                            <t:column id="rowlabel">
                                <h:outputText value="#{ContainerViewBean.rowLabel.cell.posx}"/>
                            </t:column>
                            <t:columns id="col" value="#{ContainerViewBean.headerModel}" var="colHeader">        
                                <f:facet name="header">
                                    <h:outputText value="#{colHeader}"/>
                                </f:facet>
                                <t:dataTable value="#{ContainerViewBean.plateValue.reagents}" var="reagent">
                                    <t:column>
                                        <h:commandLink action="#{ContainerViewBean.showReagent}">
                                            <h:outputText value="#{reagent.name}"/>
                                            <f:param name="reagentid" value="#{reagent.reagentid}" />
                                        </h:commandLink>
                                    </t:column>
                                </t:dataTable>
                                <t:dataTable value="#{ContainerViewBean.plateValue.properties}" var="property">
                                    <t:column>
                                        <h:outputText rendered="#{property.isculture}" styleClass="#{ContainerViewBean.plateValue.cultureFail? 'culturefail' : 'culture'}" value="#{property.value}"/>
                                    </t:column>
                                    <t:column>
                                        <h:outputText rendered="#{property.isdna}" styleClass="#{ContainerViewBean.plateValue.dnaFail? 'dnafail' : 'dna'}" value="#{property.value}"/>
                                    </t:column>
                                </t:dataTable>
                            </t:columns>
                        </t:dataTable>
                    </h:form>
                </h:panelGroup>
                
                <h:panelGroup rendered="#{ContainerViewBean.displayDetail}">
                    <h:panelGrid columns="4">
                        <h:outputText styleClass="prompt" value="Name"/>
                        <h:outputText styleClass="text" value="#{ContainerViewBean.reagent.name}"/>
                        <h:outputText styleClass="prompt" value="Type"/>
                        <h:outputText styleClass="text" value="#{ContainerViewBean.reagent.type}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="Source Clone ID"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.srccloneid}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="Source"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.source}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="GenBank Accession"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.genbank}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="GI"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.gi}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="Gene ID"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.geneid}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="Gene Symbol"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.symbol}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="Growth Condition"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.growthname.name}"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="prompt" value="Vector Name"/>
                        <h:outputText rendered="#{ContainerViewBean.clone}" styleClass="text" value="#{ContainerViewBean.reagent.vectorname.name}"/>
                    </h:panelGrid>
                </h:panelGroup>
            </h:panelGrid>
            
        </f:view>
    </body>
</html>
