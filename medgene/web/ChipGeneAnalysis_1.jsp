<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>sort a gene list</title>
</head>

<body>
<jsp:include page="NavigatorBar.jsp" flush="true"/>
<div align="center">
<p align="left"><br>
</p>
  <center>
<table border="0" cellpadding="0" cellspacing="0" width="90%">
  <tr>
    <td width="93%"><b><font color="#000099" face="Verdana" size="5">Sort a Disease-Related Gene List</font></b></td>
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

<html:form action="chipGeneAnalysis_1.do">   
  <div align="center">
    <center>
    <table height="50" cellSpacing="0" cellPadding="0" width="77%" border="0">
      <tbody>
        <tr>
          <td vAlign="top" align="left" width="75%" height="50" rowSpan="2">
            <table borderColor="#99ccff" cellSpacing="0" cellPadding="0" width="100%" border="1">
              <tbody>
                <tr>
                  <td width="100%" bgColor="#99ccff"><b><font color="#000099">&nbsp;
                    <img src="jpg/red_tri.gif" >&nbsp;
                    </font><font face="Arial" color="#003399">Please type in a
                    disease name and select the species.</font></b></td>
                </tr>
                <tr>
                  <td width="100%" bgColor="#f0f8ff"><b><font color="#000099"><br>
                    <br>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Disease term:&nbsp; </font></b>
                    <html:text property="searchTerm" size="50" /><br>
                    <br>
                    <table width="330">
                      <tbody>
                        <tr>
                          <td vAlign="top" width="120"><font color="#003399"><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            Species: </b></font></td>
                          <td width="196">
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
                        </tr>
                      </tbody>
                    </table>
                    <p><br>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b><font color="#003399">The
                    corresponding MeSH term(s) will be displayed in the next
                    step.</font></b></p>
                    <p><br>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" value="continue" name="submit"><br>
                    &nbsp;</td>
                </tr>
              </tbody>
            </table>
          </td>
          <td vAlign="top" align="right" width="25%" height="50">
            <table borderColor="#d1d1ba" cellSpacing="0" cellPadding="0" width="70%" border="1">
              <tbody>
                <tr>
                  <td width="100%" bgColor="#d1d1ba">
                    <p align="center"><b><font color="#666633">Hint</font></b></p>
                  </td>
                </tr>
                <tr>
                  <td vAlign="top" align="right" width="100%">
                    <p align="center"><br>
                    <br>
                    <a href="http://www.nlm.nih.gov/mesh/" target="_blank"><font color="#666633"><b>MeSH
                    home</b></font></a><br>
                    <p align="center"><b><a href="DiseaseList.jsp" target="_blank"><font color="#666633">Disease<br>
                    MeSH terms</font></a></b>
                    <p>&nbsp;</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </td>
        </tr>
        <tr>
          <td vAlign="middle" align="right" width="25%" height="30">&nbsp; <img border="0" src="jpg/microarray.gif" width="80" height="79">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        </tr>
      </tbody>
    </table>
    </center>
  </div>
</form>
<br>
<div align="center">
  <center>
  <table cellPadding="2" width="72%" border="0" bordercolor="#336699">
    <tr>
      <td width="11%" valign="top" align="left"><font color="#003366"><b>NOTE:<br>
        &nbsp;&nbsp;&nbsp;<br>
        &nbsp; <img border="0" src="jpg/blueball.gif" width="18" height="16">&nbsp;&nbsp;</b></font><br>
        &nbsp;
      </td>
      <td width="89%"><font color="#003366"> The disease-gene associations
        were extracted from Medline only for human genes and diseases at MedGene. However, genes from other species listed here will be mapped to
        human genes by using HomoloGene at NCBI. Then their relationship to human diseases can be retrieved and the
        corresponding micro-array data can be analyzed by using MedGene.</font>
      </td>
    </tr>
  </table>
  </center>
</div>
</html:form>
<br>
<table width='80%' align="center">
  <jsp:include page="links.jsp" flush="true"/>
</table>
</body>
