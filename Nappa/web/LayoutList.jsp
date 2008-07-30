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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.listLayout}"/></td>
                </tr>
            </table>
            
            <t:saveState id="layoutsID" value="#{layoutDesignBean.layouts}" />
            <h:form id="layoutListForm">
            <h:outputText styleClass="prompt" value="Click the layout name to view the detailed information"/>
                    <h:dataTable value='#{layoutDesignBean.layouts}' var='item' border="1" cellpadding="2" cellspacing="0">
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Name"/>
                            </f:facet>             
                            <h:commandLink styleClass="text" action="#{layoutDesignBean.queryLayout}">
                                <h:outputText styleClass="text" value="#{item.name}"/>
                                <f:param name="name" value="#{item.name}"/>
                            </h:commandLink> 
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Description"/>
                            </f:facet>
                            <h:outputText value="#{item.description}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Researcher"/>
                            </f:facet>
                            <h:outputText value="#{item.researcher}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Status"/>
                            </f:facet>
                            <h:outputText value="#{item.status}"/>
                        </h:column>
                    </h:dataTable>
            </h:form>
        </f:view>
    </body>
</html>
