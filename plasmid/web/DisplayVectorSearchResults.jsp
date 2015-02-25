<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.RefseqNameType" %> 
<%@ page import="plasmid.form.RefseqSearchForm" %> 
<%@ page import="plasmid.coreobject.Clone" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">

<script language="JavaScript">
function readCookie(name){
return(document.cookie.match('(^|; )'+name+'=([^;]*)')||0)[2]
}
</script>

 <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">


    <body onScroll="document.cookie='ypos=' + window.pageYOffset" onLoad="window.scrollTo(0,readCookie('ypos'))">
        <jsp:include page="orderTitle.jsp" />
        <table id='content' width="auto" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="136" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="menu.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="searchResultTitle.jsp" />--%>

                    <bean:define id="size" name="refseqSearchForm" property="pagesize"/>
                    <bean:define id="p" name="refseqSearchForm" property="page"/>
                    <bean:define id="total" name="numOfFound"/>

                    <html:form action="SetDisplay.do">

                        <p class="mainbodytexthead">List of search terms found</p>
                        <table width="100%" border="0">
                            <tr class="mainbodytexthead">
                                <td align="left" class="mainbodytexthead">Page: 
                                    <html:select property="page">
                                        <%  int i = 0;
                                            while (i < Integer.parseInt(total.toString()) / Integer.parseInt(size.toString())) {
                                        %>
                                        <html:option value="<%=(new Integer(i + 1)).toString()%>"/>
                                        <%      i++;
                                            }
                                            if ((Integer.parseInt(total.toString()) % Integer.parseInt(size.toString())) > 0)
                                        %>
                                        <html:option value="<%=(new Integer(i+1)).toString()%>"/>
                                    </html:select>
                                    <html:submit property="button" value="Display"/>
                                </td>
                                <td class="mainbodytexthead"><html:submit property="button" value="<%=Constants.DOWNLOAD%>"/></td>
                                <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
                            </tr>
                        </table>

                        <p>
                        <table width="100%" border="0">
                            <tr style="background:#999999; text-align: center; vertical-align: top;">
                                <th class="tableheader">&nbsp;</th>
                                <th class="tableheader" id="extrainfo">Search Term</th>
                                <th class="tableheader"><a href="SetDisplay.do?page=1&sortby=cloneid">Clone ID</a></th>
                                <th class="tableheader" id="extrainfo"><a href="SetDisplay.do?page=1&sortby=originalcloneid">Original Clone ID</a></th>
                                <th class="tableheader"><a href="SetDisplay.do?page=1&sortby=clonetype">Clone Type</a></th>
                                <th class="tableheader" id="extrainfo" style="min-width:80px"><a href="SetDisplay.do?page=1&sortby=geneid">Species Specific ID</a></th>
                                <th class="tableheader"><a href="SetDisplay.do?page=1&sortby=genesymbol">Gene Symbol</a></th>
                                <!--<td class="tableheader">Keywords</td>
                                <td class="tableheader">Gene Name</td>
                                <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=targetseq">Reference Sequence</a></td>
                                <td class="tableheader"><a href="SetDisplay.do?page=1&sortby=mutdis">Mutation/ Discrepancy</a></td> -->
                                <th class="tableheader"><a href="SetDisplay.do?page=1&sortby=insertformat">Insert Format</a></th>
                                <th class="tableheader"><a href="SetDisplay.do?page=1&sortby=vectorname">Vector</a></th>
                                <th class="tableheader" id="extrainfo2"><a href="SetDisplay.do?page=1&sortby=selection">Selection Markers</a></th>
                                <th class="tableheader">&nbsp;</th>
                            </tr>

                            <% int i = Integer.parseInt(size.toString()) * (Integer.parseInt(p.toString()) - 1);
                            %>
                            <logic:iterate name="found" id="clone" length="size" offset="<%=(new Integer(i)).toString()%>">

                                <tr class="tableinfo"> 
                                    <td><%=++i%></td>
                                    <td id="extrainfo"><bean:write name="clone" property="term"/></td>
                                    <td><a target="_blank" href="GetCloneDetail.do?cloneid=<bean:write name="clone" property="cloneid"/>&species=<bean:write name="refseqSearchForm" property="species"/>"><bean:write name="clone" property="name"/></a></td>
                                    <td id="extrainfo"><bean:write name="clone" property="originalCloneid"/></td>
                                    <td><bean:write name="clone" property="type"/></td>
                                    <logic:equal name="clone" property="type" value="<%=Clone.NOINSERT%>">
                                        <!--<td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>-->
                                        <td id="extrainfo">&nbsp;</td>
                                        <td id="extrainfo">&nbsp;</td>
                                        <td id="extrainfo">&nbsp;</td>
                                    </logic:equal>

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
                                            <%--<td><bean:write name="insert" property="annotation"/></td>
                                            <td><bean:write name="insert" property="description"/></td>
                                            <td><a target="_blank" href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=<bean:write name="insert" property="targetseqidForNCBI"/>"><bean:write name="insert" property="targetgenbank"/></a></td>
                                            <td><bean:write name="insert" property="hasmutdis"/></td>--%>
                                            <td><bean:write name="insert" property="format"/></td>
                                        </logic:iterate>
                                    </logic:notEqual>
                                    <td><a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="clone" property="vectorid"/>"><bean:write name="clone" property="vectorname"/></a></td>
                                    <td id="extrainfo2">
                                        <logic:iterate name="clone" property="selections" id="selection">
                                            <bean:write name="selection" property="hosttype"/>: <bean:write name="selection" property="marker"/><br>
                                        </logic:iterate>
                                    </td>
                                    <html:form action="SetDisplay.do">
                                    <input type="hidden" name="displayPage" value="indirect"/>
                                    <input type="hidden" name="forward" value="vectorSearchResult"/>
                                    <input type="hidden" name="cloneid" value="<bean:write name="clone" property="cloneid"/>"/>
                                    <logic:equal name="clone" property="isAddedToCart" value="true">
                                        <TD valign="center">
                                            <input name="button" type="submit" class="itemtext" value="In Cart" disabled="true"/>
                                        </td>
                                    </logic:equal>
                                    <logic:notEqual name="clone" property="isAddedToCart" value="true">
                                        <TD valign="center">
                                            <input name="button" type="submit" class="itemtext" value="Add To Cart"/>
                                        </td>
                                    </logic:notEqual>
                                </html:form>
                    </tr>
                </logic:iterate> 
            </table>
        </html:form>

        <html:form action="SetDisplay.do">
            <table width="100%" border="0">
                <tr class="mainbodytexthead">
                    <td align="left" class="mainbodytexthead">Page: 
                        <html:select property="page">
                            <%  int k = 0;
                                while (k < Integer.parseInt(total.toString()) / Integer.parseInt(size.toString())) {
                            %>
                            <html:option value="<%=(new Integer(k + 1)).toString()%>"/>
                            <%      k++;
                                }
                                if ((Integer.parseInt(total.toString()) % Integer.parseInt(size.toString())) > 0)
                            %>
                            <html:option value="<%=(new Integer(k+1)).toString()%>"/>
                        </html:select>
                        <html:submit property="button" value="Display"/>
                    </td>
                    <td class="mainbodytexthead"><html:submit property="button" value="<%=Constants.DOWNLOAD%>"/></td>
                    <td align="right" class="mainbodytexthead"><a target="_blank" href="TermDefinition.jsp">Explanation of Terms</a></td>
                </tr>
            </table>
        </html:form>

    </td>
</tr>
</table>
<jsp:include page="footer.jsp" /></body>
     </div>
</html>

