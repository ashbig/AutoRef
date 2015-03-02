<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.Clone" %> 
<%@ page import="plasmid.coreobject.User" %>
<%@ page import="plasmid.coreobject.RefseqNameType" %> 

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">
    
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table id='content' width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menuHome.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="orderHistoryTitle.jsp" />--%>
                    
                    <p class="text">Order ID: <bean:write name="orderid"/>
                    <p>
                    <table width="100%" border="0">
                        <tr>
                            <td class="tableheader">&nbsp;</td>
                            <td class="tableheader">Clone ID</td>
                            <td class="tableheader" id="extrainfo">Species Specific ID</td>
                            <td class="tableheader">Gene Symbol</td>
                            <td class="tableheader" id="extrainfo">Insert Format</td>
                            <td class="tableheader">Vector</td>
                            <td class="tableheader" id="extrainfo">Selection Markers</td>
                            <td class="tableheader">Special MTA</td>
                            <logic:present name="isBatch">
                                <logic:equal name="isBatch" value="Y">
                                    <td class="tableheader">Target Plate</td>
                                    <td class="tableheader">Target Well</td>
                                </logic:equal>
                            </logic:present>
                        </tr>
                        
                        <% int i = 0;%>
                        <logic:iterate name="orderClones" id="clone">
                            <tr class="tableinfo"> 
                                <td><%=++i%></td>
                                <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>"><bean:write name="clone" property="name"/></a></td>
                                <logic:notEqual name="clone" property="type" value="<%=Clone.NOINSERT%>">
                                    <logic:iterate name="clone" property="inserts" id="insert">
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENEID%>">
                                            <td id="extrainfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&cmd=Retrieve&dopt=Graphics&list_uids=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PA%>">
                                            <td id="extrainfo"><a target="_blank" href="http://www.pseudomonas.com/AnnotationByPAU.asp?PA=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.SGD%>">
                                            <td id="extrainfo"><a target="_blank" href="http://db.yeastgenome.org/cgi-bin/locus.pl?locus=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.GENBANK%>">
                                            <td id="extrainfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/nuccore/<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.PRO_GI%>">
                                            <td id="extrainfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/protein/<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.FBID%>">
                                            <td id="extrainfo"><a target="_blank" href="http://www.flybase.org/.bin/fbidq.html?<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.WBGENEID%>">
                                            <td id="extrainfo"><a target="_blank" href="http://www.wormbase.org/db/gene/gene?name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.TAIR%>">
                                            <td id="extrainfo"><a target="_blank" href="http://arabidopsis.org/servlets/TairObject?type=locus&name=<bean:write name="insert" property="geneid"/>"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <logic:equal name="insert" property="speciesSpecificid" value="<%=RefseqNameType.LOCUS_TAG%>">
                                            <td id="extrainfo"><a target="_blank" href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene&cmd=&term=<bean:write name="insert" property="geneid"/>&go=Go"><bean:write name="insert" property="geneid"/></a></td>
                                        </logic:equal>
                                        <td><bean:write name="insert" property="name"/></td>
                                      
                                        <td id="extrainfo"><bean:write name="insert" property="format"/></td>
                                    </logic:iterate>
                                </logic:notEqual>
                                <logic:equal name="clone" property="type" value="<%=Clone.NOINSERT%>">
                                    <td id="extrainfo">&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td id="extrainfo">&nbsp;</td>
                                </logic:equal>
                                <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
                                <td  id="extrainfo">
                                    <logic:iterate name="clone" property="selections" id="selection">
                                        <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/>;
                                    </logic:iterate>
                                </td>
                                <td><bean:write name="clone" property="specialtreatment"/></td>
                                <logic:present name="isBatch">
                                    <logic:equal name="isBatch" value="Y">
                                        <td><bean:write name="clone" property="targetPlate"/></td>
                                        <td><bean:write name="clone" property="targetWell"/></td>
                                    </logic:equal>
                                </logic:present>
                            </tr>
                        </logic:iterate>
                    </table>
                    
                    <html:form action="DownloadClones.do">
                        <input type="hidden" name="type" value="<bean:write name="type"/>">
                        <input type="hidden" name="orderid" value="<bean:write name="orderid"/>">
                        <input type="hidden" name="collectionName" value="<bean:write name="collectionName"/>">
                        <input type="hidden" name="isBatch" value="<bean:write name="isBatch"/>">
                        <table width="100%" border="0">
                            <tr>
                                <td width="50%">&nbsp;</td>
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <td>
                                        <html:submit styleClass="text" property="button" value="<%=Constants.BUTTON_CREATE_BIOBANK_WORKLIST%>"/>
                                    </td>
                                </logic:equal>
                                <td>
                                    <html:submit styleClass="text" property="button" value="Download Clone List"/>
                                </td>
                            </tr>
                        </table>
                    </html:form>
                    
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
</div>
</html>

