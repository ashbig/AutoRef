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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.viewLayout}"/></td>
                </tr>
            </table>
            
            <t:saveState id="layoutID" value="#{layoutDesignBean.layout}" />
            <t:saveState id="isdesignID" value="#{layoutDesignBean.design}" />
            <h:outputText styleClass="errors" value="#{layoutDesignBean.message}"/>
            <h:panelGrid  columns="2"> 
                <h:outputText styleClass="prompt" value="Name"/>
                <h:outputText styleClass="text" value="#{layoutDesignBean.layout.name}"/>
                <h:outputText styleClass="prompt" value="Description"/>
                <h:outputText styleClass="text" value="#{layoutDesignBean.layout.description}"/>
                <h:outputText styleClass="prompt" value="Researcher:"/>
                <h:outputText styleClass="text" value="#{layoutDesignBean.layout.researcher}"/>
                <h:outputText styleClass="prompt" value="Date:"/>
                <h:outputText styleClass="text" value="#{layoutDesignBean.layout.date}"/>
                <h:outputText styleClass="prompt" value="Status"/>
                <h:outputText styleClass="text" value="#{layoutDesignBean.layout.status}"/>
                <h:outputText styleClass="prompt" value="#{msgs.program1Promp}"/>
                <h:outputText styleClass="text" value="#{layoutDesignBean.layout.program1}"/>
                <h:outputText styleClass="prompt" value="#{msgs.program2Promp}"/>
                <h:outputText styleClass="text" value="#{layoutDesignBean.layout.program2}"/>
            </h:panelGrid>
            
            <h:form id="layoutViewForm">
                <h:commandLink target="layoutdetailview" styleClass="text" action="#{layoutViewBean.viewSlideDetail}">
                    <h:outputText styleClass="text" value="#{layoutDesignBean.layout.slide.name}"/>
                    <f:param name="name" value="#{layoutDesignBean.layout.slide.name}"/>
                </h:commandLink>
                
                <t:dataTable value="#{layoutDesignBean.layout.lineageinfo}" var="lineage" frame="box">
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="384 Well Plate"/>
                        </f:facet>
                        <t:dataTable value="#{lineage.to}" binding="#{layoutDesignBean.toDataTable}" var="l" frame="box">
                            <t:column rendered="#{layoutDesignBean.design}">
                                <h:selectBooleanCheckbox id="selected" value="#{l.iscontrol}" onclick="submit();" valueChangeListener="#{layoutDesignBean.setControls}"/>
                            </t:column>
                            <t:column styleClass="#{l.iscontrol? 'controlcell' : 'regularcell'}">
                                <h:commandLink target="layoutdetailview" action="#{layoutViewBean.viewContainerDetail}">
                                    <h:outputText value="#{l.name}"/>
                                    <f:param name="name" value="#{l.name}"/>
                                    <f:param name="level" value="#{l.level}"/>
                                    <f:param name="io" value="#{l.ioflag}"/>
                                </h:commandLink>
                            </t:column>
                        </t:dataTable>
                    </t:column>
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="96 Well Plate"/>
                        </f:facet>
                        <t:dataTable value="#{lineage.from}" binding="#{layoutDesignBean.fromDataTable}" var="from" frame="box">
                            <t:column rendered="#{layoutDesignBean.design}">
                                <h:selectBooleanCheckbox id="selected" value="#{from.iscontrol}" onclick="submit();"  immediate="true" valueChangeListener="#{layoutDesignBean.setControls}"/>
                            </t:column>
                            <t:column styleClass="#{from.iscontrol? 'controlcell' : 'regularcell'}">
                                <h:commandLink target="layoutdetailview" action="#{layoutViewBean.viewContainerDetail}">
                                    <h:outputText value="#{from.name}"/>
                                    <f:param name="name" value="#{from.name}"/>
                                    <f:param name="level" value="#{from.level}"/>
                                    <f:param name="io" value="#{from.ioflag}"/>
                                </h:commandLink>
                            </t:column>
                        </t:dataTable>
                    </t:column>
                </t:dataTable>
                <h:commandButton value="#{msgs.downloadButton}" action="#{layoutViewBean.downloadMap}"/>                  
                <h:commandButton rendered="#{layoutDesignBean.design}" value="#{msgs.saveButton}" action="#{layoutDesignBean.saveLayout}"/>  
            </h:form>
        </f:view>
    </body>
</html>
