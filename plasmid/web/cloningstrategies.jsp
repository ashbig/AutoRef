<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>Cloning Strategies</title>
<meta name='description' content='Educational material about various cloning strategies'>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link href="layout.css" rel="stylesheet" type="text/css" />
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
        <!--<script type="text/javascript" src="SpryMenuBar.js"></script>  code from dreamweaver doesn't work, submenu action re-done with css  
        <script type="text/javascript">
        var MenuBar1 = new Spry.Widget.MenuBar("MenuBar1", {imgDown:"SpryMenuBarDownHover.gif", imgRight:"SpryMenuBarRightHover.gif"});</script> -->
<style type="text/css">
<!--
.style1 {color: #000000}
.style2 {color: #cc0000}
.style3 {color: cc000}
.style4 {
	font-size: 18px;
	color: #CC0000;
}
.style5 {
	font-size: 14px
}
.style6 {color: 00000}
.style7 {font-size:12px; font-family: Arial, Helvetica, sans-serif;}
body {
	margin-left: 30px;
	margin-right: 30px;
}
-->
</style>
</head>
<div class="gridContainer clearfix">
<body>
<jsp:include page="signinMenuBar.jsp" />
<p align="left" class="mainbodytexthead">Information about Cloning Methods</p>

<pre>
<span class="mainbodytext">Restriction Digest Approaches:</span>
<span class="mainbodytext">1.&nbsp; <a href="#StandardRE">Standard RE Digests</a></span> 
<span class="mainbodytext">2.&nbsp; <a href="#CpoI">CpoI-based Directional Cloning</a></span>

<span align="left" class="mainbodytext">Recombinational Approaches:</span>
<span class="mainbodytext">1.&nbsp; <a href="#CreLox">Cre/Lox (P1 phage) approach (e.g. Clontech  Creator System)</a></span>
<span class="mainbodytext">2.&nbsp; <a href="#att">Recombinational Approaches, att lambda phage  approach (e.g.  Invitrogen  Gateway System)</a></span>

<span align="left" class="mainbodytext">Bacterial Mating Approaches:</span> 
<span class="mainbodytext"><a href="#bacerialmating">MAGIC approach (Elledge Lab, HMS)</a></span>
</pre>

<p>&nbsp;</p>
<div align="left">
  <pre align="left"><a name="StandardRE" class="mainbodytexthead">Standard RE Digests</a></pre>
</div>
<p class="mainbodytext"><u>Description</u><br>
  In standard restriction enzyme (RE) cloning, specific  enzymes are used to cleave DNA at specific DNA sequences, and fragments with  compatible ends (or blunt-ended fragments) are ligated to one another to form a  circular plasmid, which can be transformed and propagated in bacteria. The  method can be used to add, remove or otherwise manipulate DNA sequences.</p>
<p class="mainbodytext"><u>Learn More</u><br>
  To learn more about this method, please consult a molecular  biology textbook or protocol manual.</p>
<p class="mainbodytext"><u>Advantages</u><br>
  A significant advantage of RE cloning is that because it has  been in use for many years, reagents and protocols are readily available to  most researchers.</p>
<p class="mainbodytext"><u>In PlasmID </u><br>
  Virtually any plasmid can be manipulated using restriction  enzyme cloning methods. Particularly useful are those plasmids that contain a  multiple cloning site (MCS), also called a polylinker. </p>
<div align="left">
  <pre align="left" class="mainbodytexthead"><a name="CpoI"></a>CpoI-based Directional Cloning</pre>
</div>
<p class="mainbodytext"><u>Description</u><br>
  The CpoI enzyme (available from Fermentas and other vendors)  recognizes the sequence cggtccg or cggaccg. The enzyme cuts after the first cg,  leaving a three base-pair three-prime overhang. These properties make it  possible to use CpoI to clone specially designed PCR-amplified fragments  in-frame and directionally into a CpoI-digested vector. The CpoI approach can  be used to add inserts to a vector with aCpoI cloning site.</p>
<p class="mainbodytext"><u>Learn More</u><br>
  To learn more about this method, see papers that use the  method, such as Petrucco et al. (2002) &quot;A Nick-sensing 3' DNA Repair Enzyme from Arabidopsis&quot; (PMID: 11948185), Izumiya  et al. (2003) &quot;Cell cycle regulation ...&quot; (PMID: 12915577 ) or Izumiya et al. (2005)  &quot;Kaposi's sarcoma-associated ...&quot; (PMID: 16014952 ). In addition, it is helpful to draw  out the vector and PCR product ends to see how the method can work.</p>
<p class="mainbodytext"><u>Advantages</u><br>
  Significant advantages of the CpoI-based directional cloning  method over conventional restriction digest methods include (1) as the overhang  is three base-pairs, it's easy to keep things in-frame, and (2) a single enzyme  can be used in a directional approach (most directional approaches require that  one end is cut with one restriction enzyme and the other end is cut with a  different restriction enzyme).</p>
<p class="mainbodytext"><u>In PlasmID </u><br>
  Several plasmids shared with the repository by J. Kamil  (Laboratory of D. Coen, BCMP Department at Harvard   Medical School)  that are useful for adding N-terminal and C-terminal tags (e.g. 6xHis, T7  eptiope tags). Try &quot;advanced search&quot; with &quot;Kamil&quot; as author  or try &quot;search by vector&quot; and choose &quot;CpoI-based cloning&quot;  as the cloning method.</p>
<div align="left">
  <pre align="left" class="mainbodytexthead"><a name="CreLox"></a>Cre/Lox (P1 phage) approach (e.g. Clontech Creator System</pre>
</div>
<p class="mainbodytext"><u>Description</u><br>
  Clontech's Creator (TM) system and other LoxP  site-containing plasmids can be used to add, replace or otherwise manipulate  DNA using the Cre enzyme, which catalyzes site-specific recombination at LoxP  sites. Please note that not every vector containing LoxP is appropriate for  Cre/Lox-based cloning approaches. Sometimes the LoxP is present to make  possible some experimental approach that is unrelated to cloning (for example,  LoxP-containing constructs are sometime introduced into cells and then in vivo  expression of Cre is used to bring about changes).</p>
<p class="mainbodytext"><u>Learn More</u><br>
  To learn more about this method, please see the Clontech  website <a href="www.clonetech.com" target="_blank">www.clontech.com</a> and appropriate references, which include Hoess et al.  (1982) &quot;P1 site-specific recombination: nucleotide sequence of the recombining  sites&quot; (PubMed ID 6954485.).</p>
<p class="mainbodytext"><u>Advantages</u><br>
  A significant advantage of the system is that an insert can  be cloned into a single 'master' or 'entry' vector and readily sub-cloned into  many different expression vectors without the need to digest with restriction  enzymes, gel purify, ligate, etc.</p>
<p class="mainbodytext"><u>In PlasmID </u><br>
  The many sequence-verified open reading frame (ORF) or cDNA  clones in pDNR-Dual shared with the repository by the Harvard Institute of  Proteomics (HIP) at Harvard Medical   School. Some of these clones are  &quot;closed&quot; format such that the normal stop codon is present and others  are &quot;fusion&quot; format in which the stop codon is replaced by a leu  codon so that a C-terminal tag can be added.</p>
<div align="left">
  <pre align="left" class="mainbodytexthead"><a name="att"></a>att Lambda Phage Recombination approach (e.g. Invitrogen Gateway System)</pre>
</div>
<p class="mainbodytext"><u>Description</u><br>
  Invitrogen's Gateway (TM) system and other lambda phage  (att) recombination site-containing plasmids can be used to add, replace or  otherwise manipulate DNA using an enzyme that catalyzes site-specific  recombination at att-type sites.</p>
<p class="mainbodytext"><u>Learn More</u><br>
  To learn more about this method, please see the Invitrogen  website <a href="www.invitrogen.com" target="_blank">www.invitrogen.com</a> and appropriate references, which include Landy  (1989) &quot;Dynamic, structural and regulatory aspects of lambda site-specific  recombination&quot; (PubMed ID 2528323).</p>
<p class="mainbodytext"><u>Advantages</u><br>
  Similar to the Cre/Lox approach, a significant advantage of  the system is that an insert can be cloned into a single 'master' or 'entry'  vector and readily sub-cloned into many different expression vectors without  the need to digest with restriction enzymes, gel purify, ligate, etc.</p>
<p class="mainbodytext"><u>In PlasmID </u><br>
  Examples include the very many sequence-verified open reading  frame (ORF) or cDNA clones in pDONR201 or pDONR221 shared with the repository  by the Harvard Institute of Proteomics (HIP) at Harvard   Medical School.  Some of these clones are &quot;closed&quot; format such that the normal stop  codon is present and others are &quot;fusion&quot; format in which the stop  codon is replaced by a leu codon so that a C-terminal tag can be added.</p>
<div align="left">
  <pre class="mainbodytexthead" align="left"><a name="bacerialmating"></a>Bacterial Mating Approach (e.g. MAGIC, Elledge Lab, HMS)</pre>
</div>
<p class="mainbodytext"><u>Description</u><br>
&quot;Mating-Assisted Genetically Integrated Cloning&quot;  or MAGIC was developed by Li &amp; Elledge (HHMI/Harvard Medical School) and  relies on in vivo DNA cleavage and homologous recombination events that occur  during bacterial mating to combine specifically designed 'donor' and  'recipient' plasmids (e.g. insert-containing donors and specific recipient  expression vectors).</p>
<p class="mainbodytext"><u>Learn More</u><br>
  To learn more about this method, please see Li &amp; Elledge  (2005) &quot;MAGIC, an in vivo genetic method for the rapid construction of  recombinant DNA molecules&quot; (PubMed ID 15731760).</p>
<p class="mainbodytext"><u>Advantages</u><br>
  A significant advantage of the method is that the bacterial  do the work of combining inserts and vectors and thus, constructs can be  generated without the need for restriction enzyme digest, gel purification,  etc. or use of recombination-promoting enzymes.</p>
<p class="mainbodytext"><u>In PlasmID </u><br>
  Examples include the set of pPRIME-(marker)-recipient clones  for shRNA-based approaches that was shared with the repository by the Elledge  lab (HHMI/Harvard Medical School).</p>
<jsp:include page="footer.jsp" />
</body>
</div>
</html>
