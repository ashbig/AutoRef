<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Genes associated with a particular disease</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>

<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Genes
      Associated with a Particular Gene</font></b></td>
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

<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%">
    <tr>
      <td width="100%"><html:errors/></td>
    </tr>
  </table>
  </center>
</div>

<html:form action="selectSpeciesForGeneGene.do">  
<div align="center">
  <center>
  <table border="0" cellpadding="0" cellspacing="0" width="80%" height="39">
    <tr>
      <td width="57%" align="left" valign="top" rowspan="2" height="39">
        <table border="1" cellpadding="0" cellspacing="0" width="100%" bordercolor="#99CCFF">
          <tr>
            <td width="100%" bgcolor="#99CCFF"><b><font color="#000099">&nbsp; <img border="0" src="jpg/prompt.gif" width="13" height="13">&nbsp;
              </font><font face="Arial" color="#003399">Please select the species where your gene come from.</font></b></td>
          </tr>
          <tr>
            <td width="100%" bgcolor="#F0F8FF"><b>
            <font color="#000099">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></b>
              <div align="left">
              <table>
                <tr>
                  <td vAlign="top"><font color="#003399"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    Species:</b></font></td>
                  <td>&nbsp;&nbsp;&nbsp; 
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
                </table>
              </div>
              <p>
              &nbsp;<br>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <html:submit property="submit" value="continue"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <img border="0" src="jpg/rodent4s.gif" width="47" height="64"><br>
              <b><font face="Arial" color="#003399">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <br>
              </font></b></p>
            </td>
          </tr>
        </table>
      </td>
      <td width="43%" valign="top" align="right" height="1">
        <div align="right">
        <table border="1" cellpadding="0" cellspacing="0" width="87%" bordercolor="#D1D1BA" height="245">
          <tr>
            <td width="100%" bgcolor="#D1D1BA" height="19">
              <p align="center"><b><font color="#666633">Note</font></b></td>
          </tr>
  </center>
          <tr>
            <td width="100%" valign="top" align="left" height="222">
              &nbsp;
              <div align="center">
                <center>
                <table border="0" cellpadding="0" cellspacing="0" width="89%">
                  <tr>
                    <td width="100%"><font color="#333300">&nbsp;&nbsp;&nbsp; The gene-gene associations were&nbsp;&nbsp; extracted from
              Medline only for human genes at MedGene. However, genes from other
              species listed here will be mapped to human genes by using
              HomoloGene at NCBI. Then their relationships to other human genes
              can be retrieved by using MedGene.</font></td>
                  </tr>
                </table>
                </center>
              </div>
              <p align="left">&nbsp;
  <center>
              </center>
              </td>
          </tr>
        </table>
        </div>
      </td>
    </tr>
    <tr>
      <td width="43%" valign="top" align="right" height="68">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</div>
</html:form>
<br>
<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
