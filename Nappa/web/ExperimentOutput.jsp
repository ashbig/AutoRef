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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.performExperiment}"/></td>
                </tr>
            </table>
            
            <p><h:outputText styleClass="errors" value="#{experimentBean.message}" /></p>
 
            <h:panelGrid columns="2" columnClasses="top, top"> 
                
                <h:outputLabel styleClass="prompt" value="#{msgs.experimentDatePrompt}:" for="dateid"/>
                <h:outputText styleClass="text" id="dateid" value="#{experimentBean.date}"/>
                
                <h:outputLabel styleClass="prompt" value="#{msgs.ListContainer}:" for="containerLabelsid"/>
                <t:dataTable value="#{experimentBean.barcodes}" var="barcode">    
                    <t:column>
                        <h:outputText value="#{barcode}"/>
                    </t:column>   
                </t:dataTable>
                
                <h:outputLabel styleClass="prompt" value="#{msgs.addVariables}:" />
                <h:panelGroup>
                    <t:dataTable value="#{experimentBean.variables}" var="var" rendered="#{experimentBean.showVariables}" border="1">    
                        <t:column>
                            <f:facet name="header">
                                <h:outputText value="Type"/>
                            </f:facet>
                            <h:outputText value="#{var.type}"/>
                        </t:column>                        
                        <t:column>
                            <f:facet name="header">
                                <h:outputText value="Value"/>
                            </f:facet>
                            <h:outputText value="#{var.value}"/>
                        </t:column>
                        <t:column>
                            <f:facet name="header">
                                <h:outputText value="Extra Information"/>
                            </f:facet>
                            <h:outputText value="#{var.extra}"/>
                        </t:column>
                    </t:dataTable>
                </h:panelGroup>
                
            </h:panelGrid>
                    
        </f:view>
    </body>
</html>
