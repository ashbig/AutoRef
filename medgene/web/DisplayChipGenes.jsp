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
        Your input genes are grouped into 3 categories according to their relationship with 
        the disease of <b><bean:write name="disease_mesh_term"/></b> as cited in literature. <br><br>
        <a href="#direct"> First degree associations </a> <br>
        <a href="#direct_family"> First degree associations by family term </a> <br>
        <a href="#indirect"> Second degree associations </a> <br>
        <a href="#new"> Genes new to this disease </a> <br>
    </TD>
    </TR>
    </TABLE>    

    <br>

    <a name="direct"> </a><br><br><% int i = 0; %>
       
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >   
    <TR> <b>First degree associations</b> <br><br> </TR>
    <TR bgcolor="#cccccc">
        <TH width="10%">Rank</TH>
        <TH width="30%">Locus ID</TH>
        <TH width="30%"><A HREF="GeneSymbol.jsp" target="_blank">Gene Symbol</A></TH>
        <TH width="30%"><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
    </TR>

    <logic:iterate id="directChipGene" name="direct_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD align="center">
                <bean:write name="directChipGene" property="locus_id"/>
            </TD>
            <TD align="center">
                <bean:write name="directChipGene" property="gene_symbol"/>
            </TD>
            <TD align="center">
                <bean:write name="directChipGene" property="score"/>
            </TD>
        </tr>
    </logic:iterate> 
    </TABLE>

    <br>

    <a name="direct_family"> </a><br><br><% i = 0; %>
    <TABLE width = "80%" align="center" border="1" cellpadding="2" cellspacing="0" >   
    <TR> <b>First degree associations by family term</b> <br><br> </TR>
    <TR bgcolor="#cccccc">
        <TH width="10%">Rank</TH>
        <TH width="30%">Locus ID</TH>
        <TH width="30%"><A HREF="GeneSymbol.jsp" target="_blank">Gene Symbol</A></TH>
        <TH width="30%"><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
    </TR>

    <logic:iterate id="directChipGeneByFamily" name="direct_children_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD align="center">
                <bean:write name="directChipGeneByFamily" property="locus_id"/>
            </TD>
            <TD align="center">
                <bean:write name="directChipGeneByFamily" property="gene_symbol"/>
            </TD>
            <TD align="center">
                <bean:write name="directChipGeneByFamily" property="score"/>
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
        <TH width="30%">Locus ID</TH>
        <TH width="30%"><A HREF="GeneSymbol.jsp" target="_blank">Gene Symbol</A></TH>
        <TH width="30%"><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
    </TR>

    <logic:iterate id="indirectChipGene" name="indirect_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>
            <TD align="center">
                <bean:write name="indirectChipGene" property="locus_id"/>
            </TD>
            <TD align="center">
                <bean:write name="indirectChipGene" property="gene_symbol"/>
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
    <TR> <p> <b>Genes new to this disease</b> <br><br>   </TR> 
    <TR bgcolor="#cccccc">
        <TH width="10%">Rank</TH>
        <TH width="30%">Locus ID</TH>
        <TH width="30%"><A HREF="GeneSymbol.jsp" target="_blank">Gene Symbol</A></TH>
        <TH width="30%"><A HREF="statistic_menu.jsp" target="_blank">Statistical Score</A></TH>
    </TR>

    <logic:iterate id="newChipGene" name="new_genes"> 
        <tr>
            <TD align="center"><% out.println(++i); %></TD>

            <logic:equal name="newChipGene" property="locus_id" value="0">
            <TD align="center"> - </TD>            
            </logic:equal>
            <logic:notEqual name="newChipGene" property="locus_id" value="0">
            <TD align="center"> <bean:write name="newChipGene" property="locus_id"/> </TD>
            </logic:notEqual>

            <logic:equal name="newChipGene" property="gene_symbol" value="null">
            <TD align="center"> - </TD>            
            </logic:equal>
            <logic:notEqual name="newChipGene" property="gene_symbol" value="null">
            <TD align="center"> <bean:write name="newChipGene" property="gene_symbol"/> </TD>
            </logic:notEqual>

            <TD align="center"> - <TD>
        </tr>
    </logic:iterate> 
    </TABLE>
<br><br> 

<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>