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
                    <td bgcolor="beige"><h:outputText styleClass="header" value="#{msgs.prehistogram}"/></td>
                </tr>
            </table>
            
            <t:saveState id="prehistogramBeanid" value="#{prehistogramBean}"/>
            
            <h:form id="prehistogramform">
                <h:panelGrid columns="2" columnClasses="top,top"> 
                    <h:panelGroup>
                        <h:outputLabel styleClass="prompt" value="#{msgs.enterSlideLabelRootPromt}:" for="barcodeid"/>
                        <h:inputText size="20" styleClass="text" id="barcodeid" value="#{prehistogramBean.barcode}" />
                    </h:panelGroup>
                    <h:commandButton value="#{msgs.searchButton}" action="#{prehistogramBean.findSlides}"/>
                </h:panelGrid> 
                
                <t:dataTable rendered="#{prehistogramBean.showSlides}" value="#{prehistogramBean.slides}" var="slide" frame="box">
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="Slide"/>
                        </f:facet>
                        <h:outputText styleClass="text" value="#{slide.barcode}"/>
                    </t:column>
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="Date Results Uploaded"/>
                        </f:facet>
                        <h:outputText styleClass="text" value="#{slide.process.when}"/>
                    </t:column>
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value="Researcher"/>
                        </f:facet>
                        <h:outputText styleClass="text" value="#{slide.process.who.name}"/>
                    </t:column>
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value=""/>
                        </f:facet>
                        <h:commandLink target="histogramview" styleClass="text" action="#{prehistogramBean.viewHistogram}">
                            <h:outputText styleClass="text" value="View Histogram"/>
                            <f:param name="slideid" value="#{slide.slideid}"/>
                            <f:param name="slidebarcode" value="#{slide.barcode}"/>
                            <f:param name="executionid" value="#{slide.process.executionid}"/>
                        </h:commandLink>
                    </t:column>
                    <t:column>
                        <f:facet name="header">
                            <h:outputText value=""/>
                        </f:facet>
                        <h:commandLink target="plotview" styleClass="text" action="#{prehistogramBean.viewPlot}">
                            <h:outputText styleClass="text" value="View Zone Variation"/>
                            <f:param name="slideid" value="#{slide.slideid}"/>
                            <f:param name="slidebarcode" value="#{slide.barcode}"/>
                            <f:param name="executionid" value="#{slide.process.executionid}"/>
                        </h:commandLink>
                    </t:column>
                </t:dataTable>
                <h:commandButton rendered="#{prehistogramBean.showSlides}" value="#{msgs.generateAllHistogram}" action="#{prehistogramBean.viewAllHistogram}"/> 
                <h:commandButton rendered="#{prehistogramBean.showSlides}" value="#{msgs.generateAllPlot}" action="#{prehistogramBean.viewAllPlot}"/> 
            </h:form> 
            <h:outputText styleClass="errors" value="#{prehistogramBean.message}"/>
            
        </f:view>
    </body>
</html>
