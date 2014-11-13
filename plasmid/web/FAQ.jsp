<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="homeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>
        <td width="83%" align="center" valign="top">
            <br>
        <p class="mainbodytext"><font size= "4"><strong>Answers to Frequently Asked Questions &nbsp</strong></font><br>
        <p></p>
<html:errors/>
<table width="100%" border="3" cellpadding="10"> 
      <td class="mainbodytext" align="Justify"><p class="mainbodytext"><strong><u>Order Status</u></strong><br>
            <a href="#When">When will my order arrive?</a><br>
            <a href="#Troubleshooting">What does the Troubleshooting Status mean?</a><br>
            <a href="#Partiallyshipped">My order has been Partially Shipped, what happens next?</a><br>
        </p>
          <p class="mainbodytext"><strong><u>Registration</u></strong><br>
            <a href="#RegistrationError">The registration form gives me an error, what is wrong?</a><br>
            <a href="#RegisterNeeded">Do I need to register?</a><br>
            <a href="#PI">My PI is not listed, what should I do?</a><br>
            </p>
          <p class="mainbodytext"><strong><u>Financial</u></strong><br>
            <a href="#cost">How much do plasmids cost?</a><br>
            <a href="#quote">Can I get a price quote?</a><br>
            <a href="#thirtythreedigitcodes">The site asks for a 33-digit code, but I don&rsquo;t have one.  What do I do?</a><br>
            <a href="#remit">What is your remit to address?</a><br>
            <a href="#name">What is your legal name?</a><br>
            <a href="#vendorregistration">Who can complete these Vendor Registration forms?</a><br>
            </p>
          <p class="mainbodytext"><strong><u>Picking A  Construct</u></strong><br>
            <a href="#searches">There are multiple search options, what do they do?</a><br>
            <a href="#multiples">I see multiple plasmids for my gene, how do I pick the best  one?</a><br>
            <a href="#expression">How can I tell if a plasmid will express?</a><br>
            </p>
          <p class="mainbodytext"><strong><u>MTA&rsquo;s</u></strong><br>
            <a href="#MTAneeded">Do I need to complete an MTA for this order?</a><br>
            <a href="#download">Where do I find/ download a copy of the MTA for my order?</a><br>
            <a href="#joinEPMTA">My Institution is not in the Expedited MTA Network, can I  still place an order?</a><br>
            <a href="#Expedited">How can my institution join the Expedited MTA Network?</a></p>
          <p class="mainbodytext"><strong><u>Other Questions</u></strong><br>
            <a href="#deposit">I have a construct  that I would like to share. How do I deposit it?</a><br>
            <a href="#gateway">What is the  GATEWAY System?</a></p>
          <p class="mainbodytext"></p></td>
    </tr>
    <tr>
    <td><p class="mainbodytext"></p>

		<p class="mainbodytextlarge"><u><strong>Order Status</strong></u>
        
        </p><p class="mainbodytext"align="justify"><strong>
<a name="When"></a>Q. When will my order arrive? </strong><br>
A. Orders typically ship within 7-10 business days of your order. </p>
        
        <p class="mainbodytext"align="justify"><strong>      
<a name="Troubleshooting"></a>Q. What does the Troubleshooting status mean?</strong><br>
A. The troubleshooting status generally means that we were not able to verify one or more of the plasmids in your order using end read sequencing. The technician filling your order  may have already attempted to correct this problem by streaking the bacterial stock on agar, picking isolates, and sequencing again. That effort has also failed. At this point you are welcome to select any alternate construct from our library or ask taht we drop the charges for that construct. Please contact <a href="plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a> if you need help selecting an alternate. 
        
        <p class="mainbodytext"align="justify"><strong>
<a name="Partiallyshipped"></a>Q. My order has been Partially Shipped, what happens next?</strong><br>
A. Once your order Partially Ships, we will continue to work on any of remaining constructs. As we continue to work your order may move into our Troubleshooting status if we continue to have trouble verifying the construct. If we are able to verify the remaining construct(s) you will receive another shipment notification when the remaining constructs are shipped. 
        
        <p class="mainbodytextlarge"><strong><u>Registration</u></strong>        
                  
        <p class="mainbodytext"align="justify"><strong>
<a name="RegistrationError"></a>Q. The registration form gives me an error, what is wrong?</strong><br>
A. The registration form contains both drop down menu's and open text fields. Whenever possible please select values from a drop down menu and LEAVE THE TEXT FIELDS BLANK. The text fields should only be used if you are not able to find your insititution or PI. The form automatically adds the new value to the drop down list for next time. If the PI or institution is already in the drop down you will get an error.
        
        <p class="mainbodytext"align="justify"><strong>
<a name="RegisterNeeded"></a>Q. Do I need to register?</strong><br>
A. You do not have to register to search the database, download vector and clone maps, or view other information. However, you do have to register and sign in before placing an order. Registration is live so you will be able to sign in as soon as you have completed on-line registration.  We recommend signing in before you start searching for plasmids because not all plasmids are visible before registration and it saves a few steps later if you plan on ordering plasmids.</p>

		<p class="mainbodytext"align="justify"><strong>
<a name="PI"></a>Q. My PI or Institution is not listed on the registration form, what should I do?</strong><br>
A. Our registration form is designed to accept new PI and Institution names. Simply leave the drop down menu's for the appropriate field(s) on 'Please Select' then type the new value in the open text field below the drop down menu. If you have any trouble please email <a href="plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a>.</p>

		<p class="mainbodytextlarge"><strong><u>Financial</u></strong>  
           
        <p class="mainbodytext"align="justify"><strong>
<a name="cost"></a>Q. How much do plasmids cost?</strong><br>
A. Plasmids from our facility are free, but we do charge a retrieval fee and shipping to cover our operating costs. Please see our current fee structures  <a href="http://plasmid.med.harvard.edu/PLASMID/OrderOverview.jsp"> here</a>.</p>

		<p class="mainbodytext"align="justify"><strong>
<a name="quote"></a>Q. Can I get a price quote?</strong><br>
A. The shopping cart on our website can provide a full summary of all costs before you enter a PO or process a credit card. Most users print from this page, save their shopping cart, and then return to the saved cart when they are ready to place their order. If your institution requires more than this please write to plasmidhelp@hms.harvard.edu. Responses from this email may take up to 24 hours on weekdays, and no responses are given on the weekend.</p>

		<p class="mainbodytext"align="justify"><strong>
<a name="thirtythreedigitcodes"></a>Q. The sites asks for a 33-digit code, but I don't have one. What do I do?</strong><br>
A. Harvard uses 33-digit codes for all internal purchases. If you are at Harvard University you should be able to obtain this account number from the person who places orders in your lab, or from your grant manager. If you are at a Harvard affiliated hospital you are considered external and need to pay by a PO or credit card. You are being asked for a 33-digit code because you improperly registered your account as 'Harvard University' when it should be 'Harvard affiliate.' Please log into your account, select 'My Account' and then 'Update Account' to change this value. Once you have modified your account the website will no longer ask for a 33-digit code.</p>
           
           <p class="mainbodytext"><strong>
<a name="remit"></a>Q. What is your remit to address?</strong><br>
A. We ask that you write PO's and remit payment to: <br><br>
<em>Harvard Medical School<br> Dept BCMP, C1-214<br> 240 Longwood Ave. <br> 
Boston, MA 02115</em></p>

<p class="mainbodytext"align="justify"><strong>
<a name="name"></a>Q. What is your legal name?</strong><br>
A. Our legal name is 'President and Fellows of Harvard College' and we are doing business as 'Harvard Medical School'</p>

<p class="mainbodytext"><strong>
<a name="vendorregistration"></a>Q. Who can complete these vendor registration forms?</strong><br>
A. Vendor registration forms should be sent to <a href="plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a>.</p>

		<p class="mainbodytextlarge"><strong><u>Picking A Construct</u></strong>
        
<p class="mainbodytext"align="justify"><strong>
<a name="multiples"></a>Q. I see multiple plasmids for my gene, how do I pick the best one?</strong><br>
A. In general most researchers want the plasmid that covers the largest part of the protein coding region (CDS) for their gene of interest. Most researchers will also want construct that do not have mutations compared to an NCBI reference, but some single point mutations are silent and reflect the true variety found in natural samples. If you are still having trouble choosing we are always happy to discuss your specific needs and help you to find the plasmid that is right for your research. Please email <a href="plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a> with your questions and we will be happy to help.</p> 

<p class="mainbodytext"align="justify"><strong>
<a name="expression"></a>Q. How can I tell if a plasmid will express?</strong><br>
A. Most of the constructs in our library are in a cloning vector and WILL NOT express. We do have a small collection of constructs in an expression vector. To determine if the construct will express you need to examine the annotation of the backbone vector. When you look at the annotation you should look for a promoter that your model organism or expression system uses. For example, in the case of mammalian expression a CMV promoter is often, but not always present. If you cannot determine whether a specific construct will express in your system please email plasmidhelp@hms.harvard.edu with the clone ID and the expression system or organism where you would like to express.</p>       

<p class="mainbodytext"align="justify"><strong>
<a name="searches"></a>Q. There are multiple search options, what do they do?</strong><br>
A. Our newest <a href="http://plasmid.med.harvard.edu/PLASMID/faces/GeneSearch.xhtml">Human and Mouse Gene</a> search tool allows you to simply enter a gene symbol for human or mouse and then see all plasmids that we have for that gene. Our tool automatically searches through all known aliases/ synonyms and will then show you how our plasmids compare to current NCBI reference sequences. In the search results you can view a bp or aa alignment of our plasmids and can also see what percentage of the protein coding region is covered by our plasmids. 

<br><br> Our <a href="http://plasmid.med.harvard.edu/PLASMID/PrepareBlast.do">BLAST</a> search tool allows you to enter nucleotide sequences in fasta format and then see which constructs match to the sequence that you provided. 

<br><br>Our <a href="http://plasmid.med.harvard.edu/PLASMID/PrepareAdvancedSearch.do?psi=0">Advanced Text Search</a> is the most flexible search tool at PlasmID. There, you can use gene names and synonyms, author names, the part or full name of a vector, species name, PDB ID, TargetDB/PepcDB ID or combinations to search plasmids. It is also helpful to use official gene names and identifiers as they appear in databases such as NCBI Entrez Gene or organism-specific databases like SGD or FlyBase. 

<br><br>If you arrived at our site with a known clone ID from our facility or from one of our depositors you can enter those ID's in our <a href="http://plasmid.med.harvard.edu/PLASMID/SearchClone.jsp">Clone ID</a> search tool. By default the tool is set to accept PlasmID clone ID's like HsCD#######. You can change the drop down menu to 'Other Clone ID' to search for all aliases or clone ID's from the depositor. 

<br><br>To search for plasmids that code for a specific gene we recommend that you start with our <a href="http://plasmid.med.harvard.edu/PLASMID/faces/GeneSearch.xhtml">Human and Mouse Gene</a> search tool. However, if you are looking for genes from other species you can use the <a href="http://plasmid.med.harvard.edu/PLASMID/GetDataForRefseqSearch.do">Gene</a> search tool to locate a list of plasmids where that gene symbol was mentioned. Since these symbols are not dynamically updated you may also need to search for older aliases to find the constructs that you need. 

<br><br>If you are searching for plasmids that contain a protein coding gene, and want to restrict the search to characteristics of a vector you can use the  <a href="http://plasmid.med.harvard.edu/PLASMID/GetVectorPropertyTypes.do"> Vector</a> search tool. Please note that most users place too many restrictions and see no results. We recommend the use of this tool only when the previous searches yield too many results.

<br><br>Finally, if you are looking for 'Empty' vectors that do not contain a protein coding gene of interest you can view a full list <a href="http://plasmid.med.harvard.edu/PLASMID/GetVectorsByType.do">here</a>, or select a specific vector collection from the left side navigation panel. </p>

		<p class="mainbodytextlarge"><strong><u>MTA's</u></strong>
        
<p class="mainbodytext" align="justify"><strong>
<a name="MTAneeded"></a>Q. Do I need to complete an MTA for this order?</strong><br>
A. An MTA governs the transfer of material in all orders. Many institutions have signed a generalized MTA agreement with us and are therefore in our Expedited MTA Network. When you place an order that ships to one of these institutions you will be presented with our Standard Plasmid Transfer Agreement and you will be able to approve it electronically without the need for a signature. A paper copy of this same MTA will also ship with your order for your records. If your institution is not in our Expedited MTA Network we ask that you execute a separate Standard Plasmid Transfer Agreement for each request. MTA's can be downloaded from our <a href="http://plasmid.med.harvard.edu/PLASMID/TermAndCondition.jsp">Terms and Conditions</a> page. Completed MTA's should be emailed to <a href="plasmidMTA@hms.harvard.edu">plasmidMTA@hms.harvard.edu</a>. Researchers or Institutions who fail to execute this MTA implicitly agree to the terms of the Standard Plasmid Transfer Agreement through the use of this website.</p>   

<p class="mainbodytext" align="justify"><strong>
<a name="download"></a>Q. Where do I find/ download a copy of the MTA for my order?</strong><br>
A. MTA's can be downloaded from our <a href="http://plasmid.med.harvard.edu/PLASMID/TermAndCondition.jsp">Terms and Conditions</a> page.</p>   

<p class="mainbodytext" align="justify"><strong>
<a name="joinEPMTA"></a>Q. My Institution is not in the Expedited MTA Network, can I still place an order?</strong><br>
A. You are welcome to place an order even if your institution is not in our Expedited MTA Network. We ask taht you execute a separate Standard Plasmid Transfer Agreement for each request. Alternately you can have your institution join our Expedited MTA Network.</p>  


<p class="mainbodytext" align="justify"><strong>
<a name="Expedited"></a>Q. How can my institution join the Expedited MTA Network?</strong><br>
A. Institutions can join our Expedited MTA Network by executing the Standard Plasmid Transfer Agreement and Expedited Process Agreement at the same time. Both of these documents can be downloaded from our <a href="http://plasmid.med.harvard.edu/PLASMID/TermAndCondition.jsp">Terms and Conditions</a> page. Once these documents are completed, the original signed documents must be mailed to The DNA Resource Core at Harvard Medical School at the <a href="http://plasmid.med.harvard.edu/PLASMID/Contactus.jsp">address listed on our website</a>. </p>

	<p class="mainbodytextlarge"><strong><u>Other Questions</u></strong>

<p class="mainbodytext" align="justify"><strong>
<a name="deposit"></a>Q. I have a construct that I would like to share. How do I deposit it?</strong><br>
A. First of all, thank you for sharing your construct with the broader research community. We look forward to helping you make this resource available. Additional details about depositing plasmids can be found on our <a href="http://plasmid.med.harvard.edu/PLASMID/Submission.jsp">Plasmid Submission</a> page. This page is geared to deposits of libraries of plasmids. If you have fewer samples to deposit this page might not make sense. Please just drop us a line at <a href="plasmidhelp@hms.harvard.edu">plasmidhelp@hms.harvard.edu</a> and we will get the process started for you. </p>

<p class="mainbodytext" align="justify" ><strong>
<a name="gateway"></a>Q. What is the GATEWAY system?</strong><br>
A. GATEWAY is a enzymatic cloning system that eliminates the need for RE digests and ligations. This all in one system allows you to quickly shuttle an entry construct into a compatible destination vector in one enzymatic recombination step. More details and resources can be found on the <a href="http://www.lifetechnologies.com/us/en/home/life-science/cloning/gateway-cloning.html">Life Technologies website</a>.</p>
        
        </td>
    </tr>
  </table>
  <p class="homepageText2">&nbsp;</p>
</div>
<p align="center" class="mainbodytext">Still have questions? Please contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.</p></td>
  </tr>
</table>
</td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

