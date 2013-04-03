<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    </head>

    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="cloneValidationTitle.jsp" />

                    <html:form action="CloneValidation.do" method="post">
                        <html:errors/>
                        <table width="100%" border="0">
                            <tr class="formlabel"> 
                                <td width="30%" align="left" valign="baseline">Researcher:</td>
                                <td width="70%" align="left" valign="baseline" class="text"> 
                                    <bean:write name="cloneValidationForm" property="researcher" />
                                </td>
                            </tr>
                        </table>

                        <p class="formlabel">Sequence analysis criteria:</p> 
                        <table width="100%" border="0">
                            <tr> 
                                <td width="30%" class="text">Minimum percent (%) identity:</td>
                                <td class="itemtext">
                                    <html:text property="pid" styleClass="itemtext"/>%
                                </td>
                            </tr>
                            <tr> 
                                <td width="30%" class="text">Minimum alignment length:</td>
                                <td class="itemtext">
                                    <html:text property="alength" styleClass="itemtext"/>nt
                                </td>
                            </tr> 
                            <tr> 
                                <td colspan="2">
                                    <html:submit styleClass="text" value="<%=Constants.LABEL_SEQ_ANALYSIS%>" property="submit" />
                                </td>
                                <td>&nbsp;</td>
                            </tr> 
                        </table>

                        <p>    
                            <% int n = 0;%>
                            <% int i = 0;%>
                            <logic:iterate name="<%=Constants.CLONEORDER%>" id="cloneorder">
                            <table>
                                <tr class="formlabel">
                                    <td width=50%"">Order ID: <bean:write name="cloneorder" property="orderid"/></td>
                                    <td>Validation Status:
                                        <html:select name="cloneValidationForm" property='<%="status[" + n + "]"%>' styleClass="text">
                                            <html:option value="">-- Please Select --</html:option>
                                            <html:options name="validationStatus"/>
                                        </html:select>   
                                    </td>
                                </tr>
                            </table>

                            <table width="100%" border="0">
                                <logic:iterate name="cloneorder" property="clones" id="clone">
                                    <tr>
                                        <td width="20">&nbsp;</td>
                                        <td width="30%" class="tableheader">PlasmID Clone ID:</td>
                                        <td class="tableheader"><bean:write name="clone" property="clone.name"/></td>
                                    </tr>
                                    <tr>
                                        <td width="20">&nbsp;</td>
                                        <td class="formlabel">Validation Workflow:</td>
                                        <td class="text"><html:select name="cloneValidationForm" property='<%="workflow[" + i + "]"%>' styleClass="text">
                                                <html:option value="">-- Please Select --</html:option>
                                                <html:options name="workflows"/>
                                            </html:select>   
                                        </td>
                                    </tr>
                                    <tr class="formlabel"> 
                                        <td width="20">&nbsp;</td>
                                        <td align="left" valign="baseline">Validation Method:</td>
                                        <td class="text" align="left" valign="baseline">
                                            <html:select name="cloneValidationForm" property='<%="method[" + i + "]"%>' styleClass="text">
                                                <html:option value="">-- Please Select --</html:option>
                                                <html:options name="validationMethods"/>
                                            </html:select>   
                                        </td>
                                    </tr>
                                    <tr align="top,top">
                                        <td width="20">&nbsp;</td>
                                        <td class="formlabel">Enter sequence:</td>
                                        <td class="text"><html:textarea rows="5" cols="50" name="cloneValidationForm" property='<%="sequence[" + i + "]"%>' styleClass="text"/></td>
                                    </tr>
                                    <tr>
                                        <td width="20">&nbsp;</td>
                                        <td class="formlabel">Select validation Result:</td>
                                        <td class="text"><html:select name="cloneValidationForm" property='<%="result[" + i + "]"%>' styleClass="text">
                                                <html:option value="">-- Please Select --</html:option>
                                                <html:options name="validationResults"/>
                                            </html:select>   
                                        </td>
                                    </tr>
                                    <tr align="top,top">
                                        <td width="20">&nbsp;</td>
                                        <td class="formlabel">Enter isolate name:</td>
                                        <td class="text"><html:text size="50" name="cloneValidationForm" property='<%="readname[" + i + "]"%>' styleClass="text"/></td>
                                    </tr>
                                    <tr align="top,top">
                                        <td width="20">&nbsp;</td>
                                        <td class="formlabel">Enter phred score:</td>
                                        <td class="text"><html:text size="50" name="cloneValidationForm" property='<%="phred[" + i + "]"%>' styleClass="text"/></td>
                                    </tr>
                                    <tr align="top,top">
                                        <td width="20">&nbsp;</td>
                                        <td class="formlabel">Enter read:</td>
                                        <td class="text"><html:textarea rows="5" cols="50" name="cloneValidationForm" property='<%="read[" + i + "]"%>' styleClass="text"/></td>
                                    </tr>
                                    <tr>
                                        <td width="20">&nbsp;</td>
                                        <td colspan="2" class="formlabel">Validation history:</td>
                                    </tr>
                                    <tr>
                                        <td width="20">&nbsp;</td>
                                        <td colspan="2">
                                            <logic:equal name="clone" property="hasHistory" value="1">
                                                <table width="800" border="1">
                                                    <tr>
                                                        <td class="tableheader">Read</td>
                                                        <td class="tableheader">Validation Result</td>
                                                        <td class="tableheader">Validation Method</td>
                                                        <td class="tableheader">Researcher</td>
                                                        <td class="tableheader">Date</td>
                                                    </tr>

                                                    <logic:iterate name="clone" property="history" id="v">
                                                        <tr class="tableinfo"> 
                                                            <td width="20"><bean:write name="v" property="read"/></td>
                                                            <td><bean:write name="v" property="result"/></td>
                                                            <td><bean:write name="v" property="method"/></td>
                                                            <td><bean:write name="v" property="who"/></td>
                                                            <td><bean:write name="v" property="when"/></td>
                                                        </logic:iterate>
                                                </table>
                                            </logic:equal>
                                            <logic:equal name="clone" property="hasHistory" value="0">
                                                <p class="text">No validation history.</p>
                                            </logic:equal>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="3">&nbsp;</td>
                                    </tr>
                                    <% i++;%>
                                </logic:iterate>
                            </table>
                            <% n++;%>
                        </logic:iterate>

                        <table>
                            <tr class="formlabel"> 
                                <td width="30%" align="right" valign="baseline">
                                    <html:submit styleClass="text" value="Submit"/>
                                </td>
                                <td width="70%" align="left" valign="baseline">
                                    <html:reset styleClass="text" value="Clear"/>
                                </td>
                            </tr>
                        </table>

                    </html:form></td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" /></body>
</html>

