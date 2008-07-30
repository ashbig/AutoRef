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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.ListContainer}"/></td>
                </tr>
            </table>
            
            <h:form id="listContainerForm">
                <h:dataTable value='#{ContainerViewBean.containers}' var='item' border="1" cellpadding="2" cellspacing="0">
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Label"/>
                        </f:facet>
                        <h:commandLink target="containerview" action="#{ContainerViewBean.viewContainerOrSlide}">
                            <h:outputText value="#{item.barcode}"/>
                            <f:param name="containerid" value="#{item.containerid}"/>
                            <f:param name="containertype" value="#{item.type}"/>
                        </h:commandLink>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Type"/>
                        </f:facet>
                        <h:outputText value="#{item.type}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Format"/>
                        </f:facet>
                        <h:outputText value="#{item.format}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Status"/>
                        </f:facet>
                        <h:outputText value="#{item.status}"/>
                    </h:column>
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="Location"/>
                        </f:facet>
                        <h:outputText value="#{item.location}"/>
                    </h:column>
                </h:dataTable>
            </h:form>
            
        </f:view>
    </body>
</html>
