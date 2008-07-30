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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.viewProcess}"/></td>
                </tr>
            </table>
            
            <t:saveState id="processViewBeanID" value="#{processViewBean}" />
            
            <h:panelGrid  columns="2"> 
                <h:outputText styleClass="prompt" value="Protocol:"/>
                <h:outputText styleClass="text" value="#{processViewBean.p.protocol.name}"/>
                <h:outputText styleClass="prompt" value="Researcher:"/>
                <h:outputText styleClass="text" value="#{processViewBean.p.who.name}"/>
                <h:outputText styleClass="prompt" value="Date:"/>
                <h:outputText styleClass="text" value="#{processViewBean.p.when}"/>
            </h:panelGrid>
            
            <h:form id="processViewForm">
                <h:outputText styleClass="prompt" value="Objects:"/>    
                <t:dataTable value="#{processViewBean.p.objectlineages}" var="lineage" frame="box">
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="Input"/>
                        </f:facet>
                        <t:dataTable value="#{lineage.from}" var="from" frame="box">
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Object"/>
                                </f:facet>
                                <h:commandLink target="objectview" styleClass="text" action="#{processViewBean.viewObject}">
                                    <h:outputText styleClass="text" value="#{from.objectname}"/>
                                    <f:param name="objectid" value="#{from.objectid}"/>
                                    <f:param name="objecttype" value="#{from.objecttype}"/>
                                </h:commandLink>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Type"/>
                                </f:facet>
                                <h:outputText styleClass="text" value="#{from.objecttype}"/>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Order"/>
                                </f:facet>
                                <h:outputText styleClass="text" value="#{from.order}"/>
                            </t:column>
                        </t:dataTable>
                    </t:column>
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="Output"/>
                        </f:facet>
                        <t:dataTable value="#{lineage.to}" var="l" frame="box">
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Object"/>
                                </f:facet>
                                <h:commandLink target="objectview" styleClass="text" action="#{processViewBean.viewObject}">
                                    <h:outputText styleClass="text" value="#{l.objectname}"/>
                                    <f:param name="objectid" value="#{l.objectid}"/>
                                    <f:param name="objecttype" value="#{l.objecttype}"/>
                                </h:commandLink>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Type"/>
                                </f:facet>
                                <h:outputText styleClass="text" value="#{l.objecttype}"/>
                            </t:column>
                            <t:column>
                                <f:facet name="header">
                                    <h:outputText value="Order"/>
                                </f:facet>
                                <h:outputText styleClass="text" value="#{l.order}"/>
                            </t:column>
                        </t:dataTable>
                    </t:column>
                </t:dataTable>
            </h:form>
        </f:view>
    </body>
</html>
