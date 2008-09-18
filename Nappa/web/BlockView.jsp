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
            
            <h:outputText styleClass="prompt" value="Block #{ContainerViewBean.blocknum}" />    
            <h:form id="viewSampleDetailForm">
                <t:dataTable id="table" value="#{ContainerViewBean.blockModel}" var="plate" frame="box">
                    <t:column id="rowlabel">
                        <h:outputText styleClass="prompt" value="#{ContainerViewBean.blockRowLabel}"/>
                    </t:column>
                    <t:columns id="col" value="#{ContainerViewBean.blockHeaderModel}" var="colHeader" styleClass="#{ContainerViewBean.blockValue.cell.control? 'controlcell' : 'regularcell'}">        
                        <f:facet name="header">
                            <h:outputText value="#{colHeader}"/>
                        </f:facet>
                        <h:outputText rendered="#{ContainerViewBean.blockValue.hasPrecell}" styleClass="text" value="#{ContainerViewBean.blockValue.precell.containerlabel}:#{ContainerViewBean.blockValue.precell.posx}#{ContainerViewBean.blockValue.precell.posy}"/>
                        <t:dataTable value="#{ContainerViewBean.blockValue.reagents}" var="reagent">
                            <t:column>
                                <h:commandLink action="#{ContainerViewBean.showReagent}">
                                    <h:outputText styleClass="text" value="#{reagent.name}"/>
                                    <f:param name="reagentid" value="#{reagent.reagentid}"/>
                                </h:commandLink>
                            </t:column>
                        </t:dataTable>
                        <t:dataTable value="#{ContainerViewBean.blockValue.properties}" var="property">
                            <t:column>
                                <h:outputText rendered="#{property.isculture}" styleClass="#{ContainerViewBean.blockValue.cultureFail? 'culturefail' : 'culture'}" value="#{property.value}"/>
                                <h:outputText rendered="#{property.isdna}" styleClass="#{ContainerViewBean.blockValue.dNAFail? 'dnafail' : 'dna'}" value="#{property.value}"/>
                            </t:column>
                        </t:dataTable>
                    </t:columns>
                </t:dataTable>
            </h:form>
            
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
            
        </f:view>
    </body>
</html>
