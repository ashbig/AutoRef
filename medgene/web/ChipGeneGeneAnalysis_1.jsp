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
    <h1><br>Analyze a Gene List</h1>
    </center>

    <table width="80%" align="center" border="0"><tr><td>
    <html:errors/>
    <p><br>Please type in the gene name for your high-throughput experiment data. 
           The corresponding official gene symbol(s) will be displayed.<br></p>

    <html:form action="chipGeneGeneAnalysis_1.do">   
    <table width="80%" align="center">
        <tr>
            <td><html:select property="term">
                <html:option value="Gene Name" />
                <html:option value="Gene Symbol" />
                <html:option value="Locus ID" />
                </html:select>
            </td>
            <td><html:text property="searchTerm" size="50"/></td>
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
        <tr>
            <td><br></td>
            <td><br><html:submit property="submit" value="continue"/></td>
        </tr>
    </table>
    </html:form>
<p>
<jsp:include page="links.jsp" flush="true"/>
</td></tr></table></body>
</html>