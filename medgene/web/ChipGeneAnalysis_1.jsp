<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
    <head>
        <title>MedGene : Analyze a Gene List</title>
    </head>
    <body>
    <center>
    <h1>Analyze a Gene List</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>

    <p><br>Please type in the disease name and select the species for your high-throughput experiment data. <BR>
       You may type in any name you like.  The corresponding MeSH term(s) will be displayed.</p>

    <html:form action="chipGeneAnalysis_1.do">   
    <table width="80%" align="left">
    <table width="80%" align="center">
        <tr>
            <td><br>Disease term:</td>
            <td><br><html:text property="searchTerm" size="50" /></td>
        </tr>
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
            <td><br></td>
            <td><br><html:submit property="submit" value="continue"/></td>
        </tr>
    </html:form>
    <p>
    </table>
<BR><BR><A HREF="http://www.nlm.nih.gov/mesh/" target="_blank"><b>Link to MeSH home</b></A>
<BR><BR>
<A HREF="DiseaseList.jsp" target="_blank"><b>Show all disease MeSH terms</b></A>
<br>
<p> <font color="red">Note:</font> The disease-gene associations were extracted from Medline only for human genes and diseases at MedGene.
    However, genes from other species listed here will be mapped to human genes by using HomoloGene at NCBI.  
    Then their relationship to human diseases can be retrieved and the corresponding micro-array data can be analyzed by using MedGene. 
<br><br><br>
<jsp:include page="links.jsp" flush="true"/>

</td></tr></table></body>
</html>