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
            
            <h:panelGrid  columns="2" >
                <h:outputText styleClass="prompt" value="Label"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.barcode}"/>
                <h:outputText styleClass="prompt" value="Type"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.container.containertype.type}"/>
                <h:outputText styleClass="prompt" value="Format"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.container.format}"/>
                <h:outputText styleClass="prompt" value="Status"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.container.status}"/>
                <h:outputText styleClass="prompt" value="Location"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.container.location}"/>
                <h:outputText styleClass="prompt" value="Print Order"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.printorder}"/>
                <h:outputText styleClass="prompt" value="Surface Chemistry"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.surfacechem}"/>
                <h:outputText styleClass="prompt" value="Program"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.program}"/>
                <h:outputText styleClass="prompt" value="Print Date"/>
                <h:outputText styleClass="text" value="#{ContainerViewBean.slide.startdate}"/>
            </h:panelGrid>
            
            <h:form id="viewSampleDetailForm">
                <h:panelGrid  columns="3" columnClasses="top,top,top"> 
                    <h:panelGroup>
                        <h:outputText styleClass="nav" value="Blocks:"/>   
                        <t:dataTable id="table" value="#{ContainerViewBean.plateModel}" var="plate" frame="box">
                            <t:column id="rowlabel">
                                <h:outputText value="#{ContainerViewBean.slideRowLabel}"/>
                            </t:column>
                            <t:columns id="col" value="#{ContainerViewBean.headerModel}" var="colHeader">        
                                <f:facet name="header">
                                    <h:outputText value="#{colHeader}"/>
                                </f:facet>
                                <h:commandLink  action="#{ContainerViewBean.viewBlock}">
                                    <h:outputText value="#{ContainerViewBean.slideValue.num}"/>
                                    <f:param name="blocknum" value="#{ContainerViewBean.slideValue.num}"/>
                                </h:commandLink>
                            </t:columns>
                        </t:dataTable>
                    </h:panelGroup>
                    
                    <h:panelGroup rendered="#{ContainerViewBean.displayBlock}">
                        <h:panelGrid  columns="2"> 
                            <h:outputText value=""/>
                            <h:panelGroup>
                                <h:panelGrid  columns="3"> 
                                    <h:outputLabel styleClass="prompt" value="#{msgs.changeCultureThreashold}:" for="culturecut"/>
                                    <h:inputText id="culturecut" value="#{ContainerViewBean.culturecut}"/>  
                                    <h:commandButton value="#{msgs.submitButton}" action="#{ContainerViewBean.changeCulturecutForSamples}"/> 
                                </h:panelGrid>     
                            </h:panelGroup>
                            <h:outputText styleClass="nav" value="Block #{ContainerViewBean.blocknum}" /> 
                            <h:panelGroup>
                                <h:panelGrid  columns="3"> 
                                    <h:outputLabel styleClass="prompt" value="#{msgs.changeDnaThreashold}:" for="dnacut"/>
                                    <h:inputText id="dnacut" value="#{ContainerViewBean.dnacut}"/>  
                                    <h:commandButton value="#{msgs.submitButton}" action="#{ContainerViewBean.changeDnacutForSamples}"/> 
                                </h:panelGrid>      
                            </h:panelGroup>
                        </h:panelGrid> 
                        
                        <t:dataTable id="block" value="#{ContainerViewBean.blockModel}" var="block" frame="box">
                            <t:column id="rowlabelblock">
                                <h:outputText value="#{ContainerViewBean.blockRowLabel}"/>
                            </t:column>
                            <t:columns id="colblock" value="#{ContainerViewBean.blockHeaderModel}" var="colHeaderblock" styleClass="#{ContainerViewBean.blockValue.cell.control? 'controlcell' : 'regularcell'}">        
                                <f:facet name="header">
                                    <h:outputText value="#{colHeaderblock}"/>
                                </f:facet>
                                <t:dataTable value="#{ContainerViewBean.blockValue.reagents}" var="reagent1">
                                    <t:column>
                                        <h:commandLink action="#{ContainerViewBean.showReagent}">
                                            <h:outputText value="#{reagent1.name}"/>
                                            <f:param name="reagentid" value="#{reagent1.reagentid}"/>
                                        </h:commandLink>
                                    </t:column>
                                </t:dataTable>
                                <t:dataTable value="#{ContainerViewBean.blockValue.properties}" var="property">
                                    <t:column>
                                        <h:outputText rendered="#{property.isculture}" styleClass="#{ContainerViewBean.blockValue.cultureFail? 'culturefail' : 'culture'}" value="#{property.value}"/>
                                    </t:column>
                                    <t:column>
                                        <h:outputText rendered="#{property.isdna}" styleClass="#{ContainerViewBean.blockValue.dnaFail? 'dnafail' : 'dna'}" value="#{property.value}"/>
                                    </t:column>
                                </t:dataTable>
                            </t:columns>
                        </t:dataTable>
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
            </h:form>
        </f:view>
    </body>
</html>
