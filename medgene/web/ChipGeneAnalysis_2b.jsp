<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
    <head>
        <title>MedGene : Analyze a Gene List</title>
    </head>
    <body>

    <center>
    <h1>Analyze a Gene List</h1>
    </center>

    <table width="85%" align="center" border="0"><tr><td> 
    <html:errors/>

    <html:form action="chipGeneAnalysis_2b.do" enctype="multipart/form-data">   


    <table cellpadding="2" cellspacing="2" border="0" width="85%" align="Center">
        <tr>
            <td valign="Top" bgcolor="#ccccff" width="85%">
                <font color="#3333ff"><b> Step 1: </b></font>
                 &nbsp; Select the disease name from the following corresponding MeSH term(s) for your high-throughput data.                
            </td>
        </tr>
        <tr><td></td></tr>
    </table>

    <table cellpadding="2" cellspacing="2" border="0" width="85%" align="Center">
        <tr>
            <html:select property="diseaseTerm">
            <html:options
                collection="diseases"
                property="id"
                labelProperty="term"
            />
            </html:select>
        </tr>
        <tr><td></td></tr>
    </table>
    
    <table cellpadding="2" cellspacing="2" border="0" width="85%" align="Center">
        <tr>
            <td valign="Top" bgcolor="#ccccff" width="85%">
                <font color="#3333ff"><b> Step 2: </b></font>
                 &nbsp; Select the statistical method.
            </td>
        </tr>
        <tr><td></td></tr>
    </table>
    <table cellpadding="2" cellspacing="2" border="0" width="85%" align="Center">
        <tr><td>
            <html:select property="stat">
            <html:options
                collection = "stats"
                property = "id"
                labelProperty = "type" 
            />
            </html:select>
        </td></tr>
        <tr><td></td></tr>
     </table>
     <table cellpadding="2" cellspacing="2" border="0" width="85%" align="Center">
        <tr>
            <td valign="Top" bgcolor="#ccccff" width="100%">
                <font color="#3333ff"><b> Step 3: </b></font>
                 &nbsp; Select your gene input type.
            </td>
        </tr>

        <tr>
           <td>
               <html:radio property="geneInputType" value="LocusID"/>LocusID (<a href="locus_id_from_Access_Number.jsp" target="_blank">how </a>to get LocusID or official gene symbol)
               <br>
               <html:radio property="geneInputType" value="Unigene"/>Unigene
               <br>
               <html:radio property="geneInputType" value="Accession"/>Accession Number
           </td>
        </tr>  
    
        <tr>
            <td valign="Top" bgcolor="#ccccff" width="85%">
                <font color="#3333ff"><b> Step 4: </b></font>
                 &nbsp; Input your gene list by one of the following two methods. 
            </td>
        </tr>
     
        <tr>
            <td>
            <% int i = ((Integer)(session.getAttribute("user_type"))).intValue();
               if (i != 1) { %>
                The maxium input size is <b>2000</b>. If there are more than 2000 genes, only the first 2000 will be processed.<br>
            <% } %>
            You can paste your gene list here. &nbsp;&nbsp;  [<a href="ChipGeneInputSample_b.jsp" target="_blank">sample input</a>]<BR>
            We currently support the following delimiters: new line(\n), tab(\t).<BR><BR>
            <html:textarea property="chipGeneInput" rows="15" cols="50"/><BR>
            <br><b>OR</b><br><br>
            You can upload your gene list as .txt file. <BR>
            </td>
        </tr>
    </table>
   
    <table cellpadding="2" cellspacing="2" border="0" width="85%" align="Center">
        <tr>
            <td>Please select the sequence file:</td>
            <td><html:file property="chipGeneInputFile" /></td>
            <td>[<a href="ChipGeneInputSample_b.jsp" target="_blank">sample file</a>]</td>
        </tr>
    </table>

    <br>
    <table cellpadding="2" cellspacing="2" border="0" width="85%" align="Center">
        <tr>
            <td>  <font color="#ff0000"><b>Note:</b></font> The default statistical method to rank the gene list is "Product of Incidence".
                 The time cost to process your data depends on the size of your gene input and the disease you select. 
                 Sometimes, it may take 5-6 minutes to complete the query. 
            <br><br>
            <html:submit property="submit" value="Submit"/> &nbsp;&nbsp;&nbsp;&nbsp;
            <html:reset value="Reset"/>
            </td>
         </tr>
    </table>
    </html:form>

    <p>
    <jsp:include page="links.jsp" flush="true"/>
    </body>
</html>