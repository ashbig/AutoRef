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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.addControls}"/></td>
                </tr>
            </table>
            
            <t:saveState id="containerid" value="#{addControlsBean.container}" />
            <t:saveState id="samplesid" value="#{addControlsBean.samples}" />
            
            <h:panelGrid  columns="2"> 
                <h:outputText styleClass="prompt" value="Label"/>
                <h:outputText styleClass="text" value="#{addControlsBean.container.barcode}"/>
                <h:outputText styleClass="prompt" value="Type"/>
                <h:outputText styleClass="text" value="#{addControlsBean.container.containertype.type}"/>
                <h:outputText styleClass="prompt" value="Format"/>
                <h:outputText styleClass="text" value="#{addControlsBean.container.format}"/>
                <h:outputText styleClass="prompt" value="Status"/>
                <h:outputText styleClass="text" value="#{addControlsBean.container.status}"/>
                <h:outputText styleClass="prompt" value="Location"/>
                <h:outputText styleClass="text" value="#{addControlsBean.container.location}"/>
            </h:panelGrid>
            
            <h:outputText styleClass="prompt" value="Samples:"/> 
            
            <h:form id="addControlsForm">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:panelGroup>
                        <t:dataTable id="table" value="#{addControlsBean.plateModel}" var="plate" frame="box">
                            <t:column id="rowlabel">
                                <h:outputText value="#{addControlsBean.rowLabel.cell.posx}"/>
                            </t:column>
                            <t:columns id="col" value="#{addControlsBean.headerModel}" var="colHeader">        
                                <f:facet name="header">
                                    <h:outputText value="#{colHeader}"/>
                                </f:facet>
                                <h:selectOneMenu rendered="#{addControlsBean.sample && addControlsBean.plateValue.cell.emptycell}" value="#{addControlsBean.plateValue.newReagent}" >
                                    <f:selectItems value="#{addControlsBean.controlreagents}" />
                                </h:selectOneMenu>  
                                <t:dataTable rendered="#{addControlsBean.sample}" value="#{addControlsBean.plateValue.reagents}" var="reagent">
                                    <t:column>
                                        <h:outputText value="#{reagent.name}"/>
                                    </t:column>     
                                </t:dataTable>
                            </t:columns>
                        </t:dataTable>
                    </h:panelGroup>
                </h:panelGrid>
                <h:commandButton value="#{msgs.saveButton}" action="#{addControlsBean.saveControls}"/>  
            </h:form>
        </f:view>
    </body>
</html>
