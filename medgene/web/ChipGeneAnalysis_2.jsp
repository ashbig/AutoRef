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

    <table width="80%" align="center" border="0"><tr><td> 
    <html:errors/>

    <html:form action="chipGeneAnalysis_2.do" enctype="multipart/form-data">   


    <table cellpadding="2" cellspacing="2" border="0" width="80%" align="Center">
        <tr>
            <td valign="Top" bgcolor="#ccccff" width="80%">
                <font color="#3333ff"><b> Step 1: </b></font>
                 &nbsp; Select the disease name from the following corresponding MeSH term(s) for your high-throughput data.                
            </td>
        </tr>
        <tr><td></td></tr>
    </table>

    <table cellpadding="2" cellspacing="2" border="0" width="80%" align="Center">
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

        <tr>
            <td valign="Top" bgcolor="#ccccff" width="80%">
                <font color="#3333ff"><b> Step 2: </b></font>
                 &nbsp; Select your gene input type.
            </td>
        </tr>

        <tr>
           <td>
               <html:radio property="geneInputType" value="Gene Symbol" />Gene Symbol &nbsp;&nbsp;&nbsp;&nbsp;
               <html:radio property="geneInputType" value="Locus ID"/>Locus ID 
           </td>
        </tr>  
    
        <tr>
            <td valign="Top" bgcolor="#ccccff" width="80%">
                <font color="#3333ff"><b> Step 3: </b></font>
                 &nbsp; Input your gene list by one of the following two methods.
            </td>
        </tr>
     
        <tr>
            <td>
            You can paste your gene list here. &nbsp;&nbsp;  [<a href="ChipGeneInputSample.jsp">sample input</a>]<BR>
            We currently support the following delimiters: new line(\n), tab(\t).<BR><BR>
            <html:textarea property="chipGeneInput" rows="15" cols="50"/><BR>
            <br><b>OR</b><br><br>
            You can upload your gene list as .txt file. <BR>
            </td>
        </tr>
    </table>
   
    <table cellpadding="2" cellspacing="2" border="0" width="80%" align="Center">
        <tr>
            <td>Please select the sequence file:</td>
            <td><html:file property="chipGeneInputFile" /></td>
            <td>[<a href="ChipGeneInputSample.jsp">sample file</a>]</td>
        </tr>
    </table>

  
    <br>
    <table cellpadding="2" cellspacing="2" border="0" width="80%" align="Center">
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