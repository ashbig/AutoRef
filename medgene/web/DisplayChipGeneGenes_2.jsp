<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Analysis Result of Gene List</title>
    </head>
    <body>
    
    <center>
    <h1>Analysis Result of Gene List</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>
    
    <br>
    <TABLE width = "80%" align="center" border="0" cellpadding="2" cellspacing="0" > 
    <TR>
    <TD>
        Your input genes are grouped into <a href="microArray_category.jsp" target="_black">4 categories</a> according to their relationship with 
        the gene of <b><bean:write name="gene_index"/></b> as cited in literature. 
        The statistical method you selected is <b><bean:write name="stat"/></b>. <br><br>
        <a href="#direct"> First degree associations </a> <br>
        <a href="#indirect"> Second degree associations </a> <br>
        <a href="#new"> Genes new to this gene </a> <br>
        <a href="#no_homolog"> Genes having no human homolog </a> <br>
    </TD>
    </TR>
    </TABLE>    

    <br>

    <a name="direct"> </a><br><br><% int i = 0; %>
       
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >   
    <TR> <b>First degree associations</b> <br><br> </TR>
    <TR bgcolor="#cccccc">
        <TH width="5%">Rank</TH>

        <logic:equal name="input_type" value="1">
        <TH width="20%">LocusID</TH>
        </logic:equal>
        <logic:equal name="input_type" value="2">
        <TH width="20%">Unigene</TH>
        </logic:equal>       
        <logic:equal name="input_type" value="3">
        <TH width="20%">Accession</TH>
        </logic:equal>

        <TH width="20%">Homo sapiens Locus ID</TH>
        <TH width="25%"><A HREF="GeneSymbol.jsp" target="_blank">Homo sapiens Gene Symbol</A></TH>
        <TH width="20%"><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
        <TH width="10%">Selected Papers</TH>
    </TR>

    <logic:iterate id="directChipGene" name="direct_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD align="center">
                <bean:write name="directChipGene" property="homolog_id"/>
            </TD>
            <TD align="center">
                <a href="http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=<bean:write name="directChipGene" property="locus_id"/>" target="_blank">
                <bean:write name="directChipGene" property="locus_id"/></a>
            </TD>
            <TD align="center">
                <a href="DisplayLinks.do?geneSymbol=<bean:write name="directChipGene" property="gene_symbol"/>" target="_blank"> 
                <bean:write name="directChipGene" property="gene_symbol"/></a>               
            </TD>
            <TD align="center">
                <bean:write name="directChipGene" property="score"/>
            </TD>
            <TD align="center">
                <A HREF="DisplayPaperLinks_GeneGene.do?source_gene_index=<bean:write name="gene_index"/>&target_gene_locusid=<bean:write name="directChipGene" property="locus_id"/>" target="_blank" >
                Link</A>
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>

    <br>

    <a name="indirect"> </a><br><br><% i = 0; %>
    
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >  
    <TR> <p> <b>Second degree associations </b> <br><br>  </TR> 
    <TR bgcolor="#cccccc">
        <TH width="10%">Rank</TH>

        <logic:equal name="input_type" value="1">
        <TH width="20%">LocusID</TH>
        </logic:equal>
        <logic:equal name="input_type" value="2">
        <TH width="20%">Unigene</TH>
        </logic:equal>       
        <logic:equal name="input_type" value="3">
        <TH width="20%">Accession</TH>
        </logic:equal>

        <TH width="25%">Homo sapiens Locus ID</TH>
        <TH width="25%"><A HREF="GeneSymbol.jsp" target="_blank">Homo sapiens Gene Symbol</A></TH>
        <TH width="20%"><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
    </TR>

    <logic:iterate id="indirectChipGene" name="indirect_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD align="center">
                <bean:write name="indirectChipGene" property="homolog_id"/>
            </TD>
            <TD align="center">
                <a href="http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=<bean:write name="indirectChipGene" property="locus_id"/>" target="_blank">
                <bean:write name="indirectChipGene" property="locus_id"/></a>
            </TD>
            <TD align="center">
                <a href="DisplayLinks.do?geneSymbol=<bean:write name="indirectChipGene" property="gene_symbol"/>" target="_blank">
                <bean:write name="indirectChipGene" property="gene_symbol"/></a>
            </TD>
            <TD align="center">
                <bean:write name="indirectChipGene" property="score"/>
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>

    <br>

    <a name="new"> </a><br><br><% i = 0; %>
   
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >   
    <TR> <p> <b>Genes new to this Gene</b> <br><br>   </TR> 
    <TR bgcolor="#cccccc">
        <TH width="10%">Rank</TH>

        <logic:equal name="input_type" value="1">
        <TH width="20%">LocusID</TH>
        </logic:equal>
        <logic:equal name="input_type" value="2">
        <TH width="20%">Unigene</TH>
        </logic:equal>       
        <logic:equal name="input_type" value="3">
        <TH width="20%">Accession</TH>
        </logic:equal>

        <TH width="25%">Homo sapiens Locus ID</TH>
        <TH width="25%"><A HREF="GeneSymbol.jsp" target="_blank">Homo sapiens Gene Symbol</A></TH>
        <TH width="20%"><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
    </TR>

    <logic:iterate id="newChipGene" name="new_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD align="center">
                <bean:write name="newChipGene" property="homolog_id"/>
            </TD>

            <logic:equal name="newChipGene" property="locus_id" value="0">
            <TD align="center"> - </TD>            
            </logic:equal>
            <logic:notEqual name="newChipGene" property="locus_id" value="0">
            <TD align="center"> <a href="http://www.ncbi.nlm.nih.gov/LocusLink/LocRpt.cgi?l=<bean:write name="newChipGene" property="locus_id"/>" target="_blank">
                <bean:write name="newChipGene" property="locus_id"/></a> </TD>            
            </logic:notEqual>

            <logic:equal name="newChipGene" property="gene_symbol" value=" ">
            <TD align="center"> - </TD>            
            </logic:equal>
            <logic:notEqual name="newChipGene" property="gene_symbol" value=" ">
            <TD align="center"> <a href="DisplayLinks.do?geneSymbol=<bean:write name="newChipGene" property="gene_symbol"/>" target="_blank">
                <bean:write name="newChipGene" property="gene_symbol"/></a> </TD>
            </logic:notEqual>

            <TD align="center"> - <TD>
        </tr>
    </logic:iterate> 
    </TABLE>

    <a name="no_homolog"> </a><br><br><% i = 0; %>
   
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >   
    <TR> <p> <b>Genes having no human homolog</b> <br><br>   </TR> 
    <TR bgcolor="#cccccc">
        <TH width="5%">Rank</TH>

        <logic:equal name="input_type" value="1">
        <TH width="95%">LocusID</TH>
        </logic:equal>
        <logic:equal name="input_type" value="2">
        <TH width="95%">Unigene</TH>
        </logic:equal>       
        <logic:equal name="input_type" value="3">
        <TH width="95%">Accession</TH>
        </logic:equal>
    </TR>

    <logic:iterate id="gene" name="no_homolog_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD align="center">
                <bean:write name="gene"/>
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>   

<br><br> 

<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>