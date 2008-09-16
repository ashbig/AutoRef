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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.viewTemplate}"/></td>
                </tr>
            </table>
            
            <t:saveState id="templateID" value="#{templateDesignBean.template}" />
            <t:saveState id="isdesignID" value="#{templateDesignBean.design}" />
            <h:outputText styleClass="errors" value="#{templateDesignBean.message}"/>
            <h:panelGrid  columns="2"> 
                <h:outputText styleClass="prompt" value="Layout Name"/>
                <h:outputText styleClass="text" value="#{templateDesignBean.template.layout.name}"/>
                <h:outputText styleClass="prompt" value="Template Name"/>
                <h:outputText styleClass="text" value="#{templateDesignBean.template.name}"/>
                <h:outputText styleClass="prompt" value="Description"/>
                <h:outputText styleClass="text" value="#{templateDesignBean.template.description}"/>
                <h:outputText styleClass="prompt" value="Researcher:"/>
                <h:outputText styleClass="text" value="#{templateDesignBean.template.researcher}"/>
                <h:outputText styleClass="prompt" value="Date:"/>
                <h:outputText styleClass="text" value="#{templateDesignBean.template.date}"/>
                <h:outputText styleClass="prompt" value="Status"/>
                <h:outputText styleClass="text" value="#{templateDesignBean.template.status}"/>
            </h:panelGrid>
            
            <h:outputText rendered="#{templateDesignBean.design}" styleClass="warning" value="Please click the link below to edit the controls for each container, and then click Save button to save the template."/>
            
            <h:form id="templateViewForm">
                <h:commandLink target="templatedetailview" styleClass="text" action="#{layoutViewBean.viewSlideDetail}">
                    <h:outputText styleClass="text" value="#{templateDesignBean.template.layout.slide.name}"/>
                    <f:param name="name" value="#{templateDesignBean.template.layout.slide.name}"/>
                </h:commandLink>
                
                <t:dataTable value="#{templateDesignBean.template.layout.lineageinfo}" var="lineage" frame="box">
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="384 Well Plate"/>
                        </f:facet>
                        <t:dataTable value="#{lineage.to}" binding="#{templateDesignBean.toDataTable}" var="l" frame="box">
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
                        <t:dataTable value="#{lineage.from}" binding="#{templateDesignBean.fromDataTable}" var="from" frame="box">
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
                <h:commandButton rendered="#{templateDesignBean.design}" value="#{msgs.saveButton}" action="#{templateDesignBean.saveTemplate}"/>  
            </h:form>
        </f:view>
    </body>
</html>
