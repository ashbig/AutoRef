<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Analyze the gene list</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>

<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="80%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Sort a Disease-Related Gene List (Non Human)</font></b></td>
    <td width="22%" align="right"><b><font color="#003366" size="6"><img border="0" src="jpg/medgene02.gif" width="85" height="35"></font></b></td>
  </tr>
  <tr>
    <td width="126%"></td>
    <td width="9%"></td>
  </tr>
  <tr>
    <td width="135%" colspan="2"><br>
      <img border="0" src="jpg/menubar_upper.gif" width="900" height="10"></td>
  </tr>
</table>
  </center>
</div>

<br><br><br><br>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
 <td width="5%"></td>
 <td width="95%">

  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
  </table>


<html:form action="chipGeneAnalysis_2b.do" enctype="multipart/form-data">   
  
  <table border="0" cellpadding="0" cellspacing="0" width="80%" height="166">
    <tr>
      <td width="80%" height="166">
      <div align="left">
        <table border="1" cellpadding="0" cellspacing="0" width="99%" height="335" bordercolor="#FFFFFF">
            <tr>
              <td width="100%" valign="middle" align="left" height="9">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" height="32">
                  <tr>
                    <td width="4%" bgcolor="#D2E9FF" height="32"><img border="0" src="jpg/red_tri.gif" width="20" height="10"></td>
                    <td width="96%" bgcolor="#D2E9FF" height="32"><b><font face="Verdana" color="#000088">Select
                        the disease MeSH term for your high-throughput data.</font></b></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td width="200%" height="44" bgcolor="#F2F9FF">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                <html:select property="diseaseTerm">
                <html:options
                  collection="diseases"
                  property="id"
                  labelProperty="term"
                />
                </html:select>
              </td>
            </tr>
            <tr>
              <td width="200%" height="32" bgcolor="#FFFFFF">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" height="28">
                  <tr>
                    <td width="4%" bgcolor="#D2E9FF" height="28">
                      <img border="0" src="jpg/red_tri.gif" width="20" height="10">
                    </td>
                    <td width="96%" bgcolor="#D2E9FF" height="28">
                      <b><font color="#000088" face="Tahoma">Please choose a
                      <a href="statistic_menu.jsp" target="_blank">statistical method</a> to rank the gene list:</font> </b>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td width="200%" height="44" bgcolor="#F2F9FF">
                &nbsp; &nbsp;&nbsp;&nbsp; &nbsp;
                <html:select property="stat">
                <html:options
                  collection = "stats"
                  property = "id"
                  labelProperty = "type" 
                />
                </html:select>
              </td>
            </tr>
            <tr>
              <td width="200%" height="28">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" height="29">
                  <tr>
                    <td width="4%" height="29" bgcolor="#D2E9FF"><img border="0" src="jpg/red_tri.gif" width="20" height="10"></td>
                    <td width="96%" height="29" bgcolor="#D2E9FF">
                      <b><font face="Verdana" color="#000088">Please select your gene input type:</font> </b>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td width="200%" bgColor="#f2f9ff" height="56">
                <font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </font>
                <font color="#000080"><font face="Verdana"></font>
                <html:radio property="geneInputType" value="LocusID" /> 
                <font face="Verdana">LocusID (<a href="http://hipseq.med.harvard.edu/MEDGENE/locus_id_from_Access_Number.jsp" target="_blank">how
                </a>to get LocusID or official gene symbol)<br>&nbsp;&nbsp;&nbsp; 
                <html:radio property="geneInputType" value="Unigene"/>
                Unigene<br>&nbsp;&nbsp;&nbsp; 
                <html:radio property="geneInputType" value="Accession"/>
                Accession Number
                </font>
                </font>
             </td>
            </tr>
            <tr>
              <td width="200%" height="32" bgcolor="#FFFFFF">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" height="28">
                  <tr>
                    <td width="4%" height="28" bgcolor="#D2E9FF"><img border="0" src="jpg/red_tri.gif" width="20" height="10"></td>
                    <td width="96%" height="28" bgcolor="#D2E9FF"><b><font face="Verdana" color="#000088">Input
                      your gene list by one of the following two methods.</font></b></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td width="200%" bgColor="#f2f9ff" height="17"><font color="#0066cc"><br></font>
                <% int i = ((Integer)(session.getAttribute("user_type"))).intValue(); 
                    if (i != 1) { %>
                        <font face="Verdana" color="#000088">
                        &nbsp;&nbsp;&nbsp;&nbsp; <font size="2">The maxium input size is <b>2000</b>. If there are more than 2000 genes, <br></font>
                        &nbsp;&nbsp;&nbsp;&nbsp; <font size="2">only the first 2000 will be processed.<br></font>
                        &nbsp;
                        </font><br>
                <% } %>
                <font color="#0066cc">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </font><font face="Verdana" color="#000088">You can paste your gene list here. 
                &nbsp;&nbsp; [<a href="ChipGeneInputSample_b.jsp" target="_blank">sample input</a>]<br>
                &nbsp;&nbsp;&nbsp;&nbsp; We currently support the following delimiters: new line(\n), tab(\t).<br>
                <br>
                </font>
                <font color="#0066CC"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                  <html:textarea property="chipGeneInput" rows="15" cols="50"/><br><br>
                </font> 
                <font face="Verdana"><font color="#0066CC">&nbsp;&nbsp;&nbsp;&nbsp; </font><b><font color="#000088">OR</font></b><br></font><br>
                <font face="Verdana" color="#000099">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can upload your gene list as .txt file.<br>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Please select the sequence file:
                  <html:file property="chipGeneInputFile" />&nbsp;&nbsp;&nbsp;
                  [<a href="ChipGeneInputSample_b.jsp" target="_blank">sample file</a>]</font><p>&nbsp;
              </td>
            </tr>
            <tr>
              <td width="200%" height="16" bgcolor="#F2F9FF">
                <font color="#ff0000"><b>&nbsp; <br>
                  &nbsp;&nbsp;&nbsp;&nbsp; Note:</b></font>&nbsp; The time cost to process your
                  data depends on the size of your gene input and the disease you select.&nbsp; <br>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  Sometimes, it may take 4-5 minutes to process your request.<br>&nbsp;
              </td>
            </tr>
            <tr>
              <td width="200%" height="57" bgcolor="#F2F9FF">
                <p><br>&nbsp;&nbsp;&nbsp;&nbsp; <html:submit property="submit" value="Submit"/>&nbsp;&nbsp;&nbsp;&nbsp;
                <html:reset value="Reset"/>
                </p>
              </td>
            </tr>
          </table>
        </div>      
      </td>
    </tr>
  </table>
</html:form>
  </td>
 </tr>
</table>

<br>
<table width='90%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
