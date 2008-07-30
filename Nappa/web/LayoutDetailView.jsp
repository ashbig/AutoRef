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
            
            <h:form id="viewSampleDetailForm">
                <h:panelGrid  columns="2" columnClasses="top,top"> 
                    <h:outputText styleClass="nav" value="Blocks:"/>   
                    <h:outputText styleClass="nav" value="Block #{layoutViewBean.blocknum}" />
                    
                    <h:panelGroup>
                        <t:dataTable id="table" value="#{layoutViewBean.plateModel}" var="plate" frame="box">
                            <t:column id="rowlabel">
                                <h:outputText value="#{layoutViewBean.slideRowLabel}"/>
                            </t:column>
                            <t:columns id="col" value="#{layoutViewBean.headerModel}" var="colHeader">        
                                <f:facet name="header">
                                    <h:outputText value="#{colHeader}"/>
                                </f:facet>
                                <h:commandLink  action="#{layoutViewBean.viewBlock}">
                                    <h:outputText value="#{layoutViewBean.slideValue.num}"/>
                                    <f:param name="blocknum" value="#{layoutViewBean.slideValue.num}"/>
                                </h:commandLink>
                            </t:columns>
                        </t:dataTable>
                    </h:panelGroup>
                    
                    <h:panelGroup>
                        <t:dataTable id="block" value="#{layoutViewBean.blockModel}" var="block" frame="box">
                            <t:column id="rowlabelblock">
                                <h:outputText value="#{layoutViewBean.blockRowLabel}"/>
                            </t:column>
                            <t:columns id="colblock" value="#{layoutViewBean.blockHeaderModel}" var="colHeaderblock" styleClass="#{layoutViewBean.blockValue.cell.control? 'controlcell' : 'regularcell'}">        
                                <f:facet name="header">
                                    <h:outputText value="#{colHeaderblock}"/>
                                </f:facet>
                                <h:outputText value="#{layoutViewBean.blockValue.cell.pos},#{layoutViewBean.blockValue.cell.type}"/>
                                <t:dataTable value="#{layoutViewBean.blockValue.pre}" var="precell">
                                    <t:column>
                                        <h:outputText value="#{precell.containerlabel},#{precell.posx},#{precell.posy}"/>
                                    </t:column>
                                </t:dataTable>
                                <h:outputText rendered="#{!layoutViewBean.editcontrol}" id="reagentid1" value="#{layoutViewBean.blockValue.cell.controlreagent}"/>
                                <h:selectOneMenu rendered="#{layoutViewBean.editcontrol && layoutViewBean.blockValue.cell.control}" styleClass="text" id="reagentid" value="#{layoutViewBean.blockValue.cell.controlreagent}">	
                                    <f:selectItems value="#{layoutViewBean.controlreagents}" />
                                </h:selectOneMenu> 
                            </t:columns>
                        </t:dataTable>
                        <h:commandButton rendered="#{layoutViewBean.editcontrol}" value="#{msgs.saveButton}" action="#{layoutViewBean.saveSlideControls}"/>  
                    </h:panelGroup>
                </h:panelGrid>
            </h:form>
        </f:view>
    </body>
</html>
