<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Gene Search</title>
    </head>
    <body>
    <center>
    <h1><br>Genes Associated with a Particular Gene</h1>
    </center>

    <table width="90%" align="center" border="0"><tr><td>
    <html:errors/>

    <html:form action="selectSpeciesForGeneGene.do">   
    <table width="90%" align="center">
        <tr><td bgcolor="#ccccff">
        Please select the species where your gene come from. <br>
        </td></tr>
    </table>
    <table width="90%" align="center">        
        <tr>            
            <td valign="top"><br>Species:</td>
            <td><br>
                <html:select property="species" size="7" >
                <html:option value="Arabidopsis thaliana">Arabidopsis thaliana</html:option>
                <html:option value="Bos taurus">Bos taurus</html:option>
                <html:option value="Caenorhabditis briggsae">Caenorhabditis briggsae</html:option>
                <html:option value="Caenorhabditis elegans">Caenorhabditis elegans</html:option>
                <html:option value="Danio rerio">Danio rerio</html:option>
                <html:option value="Drosophila melanogaster">Drosophila melanogaster</html:option>
                <html:option value="Homo sapiens">Homo sapiens</html:option>
                <html:option value="Hordeum vulgare">Hordeum vulgare</html:option>
                <html:option value="Mus musculus">Mus musculus</html:option>
                <html:option value="Oryza sativa">Oryza sativa</html:option>
                <html:option value="Rattus norvegicus">Rattus norvegicus</html:option>
                <html:option value="Triticum aestivum">Triticum aestivum</html:option>
                <html:option value="Xenopus laevis">Xenopus laevis</html:option>
                <html:option value="Zea mays">Zea mays</html:option>
                </html:select>
            </td>
        <tr>
        <tr>
            <td><br></td>
            <td><br><html:submit property="submit" value="continue"/></td>
        </tr>
    </table>
    </html:form>
    <table width="90%" align="center">  
    <tr><td>
    <p> <font color="red">Note:</font> The gene-gene associations were extracted from Medline only for human genes at MedGene.
    However, genes from other species listed here will be mapped to human genes by using HomoloGene at NCBI.  
    Then their relationships to other human genes can be retrieved by using MedGene. 
    </td></tr></table>
    <br><br><br>
    <jsp:include page="links.jsp" flush="true"/>

    </body>
</html>
