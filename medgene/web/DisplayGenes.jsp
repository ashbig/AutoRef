<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Disease Search</title>
    </head>
    <body>
    <% int i = 0; %>
    <center>
    <h1>Top 
    <logic:present name="number">
        <bean:write name="number"/>
    </logic:present>
        Genes Associated With <bean:write name="disease"/>
    </h1>
    <h1>
        By Statistical Method Of "<bean:write name="stat" property="type"/>"
    </h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>
   
    <br>
    <hr>
    <b>Note:</b> If you want to find whether your interested genes are on this page, 
    just click Edit on your browser's menu bar and use Find to search the current page. 
    <hr><br>

    <p>
    <TABLE width = "100%" align="center" border="1" cellpadding="2" cellspacing="0" >

    <COLGROUP>
        <COL width="3%">
        <COL width="12%">
        <COL width="5%">
        <COL width="10%">
        <COL width="29%">
        <COL width="26%">
        <COL width="5%">
        <COL width="5%">
        <COL width="5%">
    <THEAD>

    <TR bgcolor="#cccccc">
        <TH>NO.</TH>
        <TH><A HREF="KeySearchTerm.jsp" target="_blank">Key Search Term</A></TH>
        <TH><A HREF="SearchType.jsp" target="_blank">Search Type</A></TH>
        <TH><A HREF="GeneSymbol.jsp" target="_blank">Gene Symbol</A></TH>
        <TH><A HREF="AllSearchTerms.jsp" target="_blank">All Search Terms</A></TH>
        <TH><A HREF="GOAnnotation.jsp" target="_blank">GO Annotations</A></TH>
        <TH><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
        <TH><A HREF="NumberOfPapers.jsp" target="_blank">Papers</A></TH>
        <TH><A HREF="DefaultRefSeqID.jsp" target="_blank">Default RefSeq ID</A></TH>
    </TR>

    <logic:iterate id="association" name="associations"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>

            <logic:equal name="association" property="gene.type" value="GENE">
            <TD>
                <a href="DisplayLinks.do?hipGeneId=<bean:write name="association" property="gene.hipGeneId"/>" target="_blank"><bean:write name="association" property="gene.name"/></a>&nbsp                          
            </TD>
            </logic:equal>

            <logic:equal name="association" property="gene.type" value="FAMILY">
            <TD>
                <bean:write name="association" property="gene.name"/>&nbsp                          
            </TD>
            </logic:equal>


            <TD>
                <bean:write name="association" property="geneIndex.searchType"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="gene.symbol"/>&nbsp
            </TD>


            <logic:equal name="association" property="gene.type" value="GENE">
            <TD>
                <bean:write name="association" property="gene.nicknamesString"/>&nbsp
            </TD>
            </logic:equal>



            <logic:equal name="association" property="gene.type" value="FAMILY">
            <TD>
                <logic:iterate id="child" name="association" property="gene.nicknames"> 
                    <a href="DisplayLinks.do?geneSymbol=<bean:write name="child"/>" target="_blank">
                    <bean:write name="child"/></a>&nbsp
                </logic:iterate>
            </TD>
            </logic:equal>

            <TD>
                <bean:write name="association" property="gene.gosString"/>&nbsp
            </TD>
            <TD align="center">
                <bean:write name="association" property="stat.score"/>&nbsp
            </TD>
            
            <TD align="center">
                <logic:equal  name="association" property="gene.type" value="GENE">
                <a href="DisplayPaperLinks.do?disease_id=<bean:write name="association" property="disease.id"/>&gene_index=<bean:write name="association" property="geneIndex.index"/>
&disease_mesh_term=<bean:write name="disease"/>&gene_symbol=<bean:write name="association" property="gene.symbol"/>" target="_blank">
                </logic:equal>
                <logic:equal name="association" property="gene.type" value="FAMILY">
                <a href="DisplayPaperLinks.do?disease_id=<bean:write name="association" property="disease.id"/>&gene_index=<bean:write name="association" property="geneIndex.index"/>
&disease_mesh_term=<bean:write name="disease"/>&gene_symbol=<bean:write name="association" property="gene.name"/>" target="_blank">
                </logic:equal>
                <bean:write name="association" property="data.doublehit"/></a>&nbsp
            </TD>
            <TD><a href="http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=<bean:write name="association" property="gene.refSeq_NM2"/>" target="_blank">
                <font size=2> <bean:write name="association" property="gene.refSeq_NM"/> </font></a> &nbsp
            </TD>

        </tr>
    </logic:iterate> 
</TABLE>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>