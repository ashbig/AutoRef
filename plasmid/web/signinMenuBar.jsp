<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %> 
 <link href="layout.css" rel="stylesheet" type="text/css" />
 <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
 <link href="boilerplate.css" rel="stylesheet" type="text/css" />
 <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
 <link rel="shortcut icon" href="dnacore.ico">
 <script src="respond.min.js"></script>

<br>
<br>
<a href="http://hms.harvard.edu/" target="_blank"><img height="55" src="HMS_BiochemMolPhamH.jpg" alt="BCMP"></a>
<div id="header">          
    <hr>
<br>
<div class="logo">
<img height="55" src="dnacore.jpg"/>

<img height="55" src="PlasmID_logo.jpg"/>
</div>
<br>


<h:body>
<table width="100%" height="30px" border="0" align="center">
            <tr> 
                <td width="50%" height="12px" border="0" align="left" class="countrytext">
                    <%--<a href="http://dnaseq.med.harvard.edu" target="_blank">DNA Resource Core</a><br><br>--%>
                    
            <logic:present name="<%=Constants.USER_KEY%>" scope="session"> 
            <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                    <a href="SampleTrackingHome.jsp">Sample Tracking</a>&nbsp;    
                    <a href="SEQ_InvoiceHome.jsp">Sequencing</a>
            </logic:equal>
                    
            <logic:notEqual name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">&nbsp;      
            </logic:notEqual>
            </logic:present>
            <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session"> &nbsp;        
            </logic:notPresent>
                    
              </td>
                <td width="50%" height="12px" align="right" valign="bottom" class="countrytext"> 
                    <a href="ViewCart.do" border="0"><img src="shoppingcart2.gif" width="105" height="18"/></a>
                    <br>
                   <br>
                <logic:present name="<%=Constants.USER_KEY%>" scope="session">    
                    <a href="Logout.do" class="countrytext">Sign Out |</a> 
                    <a href="Account.jsp" class="countrytext"> My Account |</a>
                </logic:present>
                
                <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
                    <a href="Login.jsp" class="countrytext" >Sign In |</a> 
                    <a href="PrepareRegistration.do" class="countrytext"> Registration |</a> 
                </logic:notPresent> 
                    <a href="FAQ.jsp" class="countrytext"> FAQ</a>
                    
                    
                </td>
            </tr>
            
</table>
</div>
     
  <div id="nav" align="center">
    <ul id="MenuBar1" class="MenuBarHorizontal">
      <li><a class="MenuBarItemSubmenu" href="Home.xhtml" title="Home">Home</a>
        <ul>
           <logic:present name="<%=Constants.USER_KEY%>" scope="session">
          <li><a href="Logout.do" title="Log Out">Sign Out</a></li>
          <li><a href="Account.jsp" title="My Account">My Account</a></li>
          <li><a href="PrepareRegistration.do?update=true&first=true" title="Update Account">Update Account</a></li>
          <li><a href="ViewOrderHistory.do" title="View Orders">View Orders</a></li> 
            <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
          <!--<li><a href="ViewContainers.jsp" title="View Containers">View Containers</a></li>
          <li><a href="vSearch.jsp" title="Vector Submission">Vector Submission</a></li>
          <li><a href="vSearchSFD.jsp" title="Vector For Distribution">Vector For Distribution</a></li>-->
          <li><a href="SearchOrderInput.jsp" title="Search Orders">Search Orders</a></li>
          <!--<li><a href="PrepareSearchInvoice.do" title="Search Invoices">Search Invoices</a></li>
          <li><a href="pReceiveSearch.jsp" title="Recieve Plasmids">Receive Plasmids</a></li>-->
          <li><a href="CloneValidationInput.jsp" title="Clone Validation">Clone Validation</a></li>
          <!--<li><a href="AddInstitutionsInput.jsp" title="Add Institutions">Add Institutions</a></li>
          <li><a href="AddEmtaMembersInput.jsp" title="Add Institution to Expedited MTA Members">Add EP-MTA Members</a></li>-->
          <li><a href="SampleTrackingHome.jsp">Sample Tracking</a></li>            
          <!--<li><a href="SEQ_InvoiceHome.jsp">Sequencing Invoices</a></li>-->
          </logic:equal>
          </logic:present>
          <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
          <li><a href="Login.jsp" title="Sign In">Sign In</a></li>
          <li><a href="FindPassword.jsp" title="Find lost password">Find Password</a></li>
          <li><a href="PrepareRegistration.do" title="Registration">Registration</a></li>
          </logic:notPresent> 
         </ul>
      </li>
      
      <li><a class="MenuBarItemSubmenu" href="OrderOverview.jsp">cDNA/ORF</a>
        <ul>
          <li><a href="GeneSearch.xhtml" title= "Human and Mouse Gene Search">Human &amp; Mouse Genes</a></li>
          <li><a href="PrepareAdvancedSearch.do?psi=0" title= "Text Search">Text Search</a></li>
          <li><a href="GetDataForRefseqSearch.do" title= "All Organism Gene Search">Search by Gene</a></li>
          <!--<li><a href="GetVectorPropertyTypes.do" title= "Vector Search">Search by Vector</a></li>  confusing search tool, little success finding vector or plasmid of interest   -->
          <li><a href="SearchClone.jsp" title= "Clone ID">Search by Clone ID</a></li>
          <li><a href="PrepareBlast.do" title= "BLAST search">BLAST Search</a></li>
        </ul>
      </li>
      <li><a class="MenuBarItemSubmenu"href="GetVectorsByType.do">Vectors</a>
        <ul>
          <li><a href="GetVectorsByType.do?type=viral production" title= "Viral Production">Viral Production</a></li>
          <li><a href="GetVectorsByType.do?type=RNA interference" title= "RNA Interference">RNA Interference</a></li>
          <li><a href="GetVectorsByType.do?type=mammalian expression" title= "Mammalian Expression">Mammalian Expression</a></li>
          <li><a href="GetVectorsByType.do?type=drosophila in vitro and in vivo expression" title= "Drosophila Expression">Drosophila Expression</a></li>
          <li><a href="GetVectorsByType.do?type=bacterial expression" title= "Bacterial Expression">Bacterial Expression</a></li>
          <li><a href="GetVectorsByType.do?type=baculovirus/insect cell expression" title= "Baculovirus/ Insect Cell Expression">Insect Cell Expression</a></li>
          <li><a href="GetVectorsByType.do?type=cloning vector" title= "Cloning Vectors">Cloning Vectors</a></li>
          <li><a href="GetVectorsByType.do?type=mutagenesis vector" title= "Mutagenesis Vectors">Mutagenesis</a></li>
          <li><a href="GetVectorsByType.do?type=Yeast Expression" title= "Yeast Expression">Yeast Expression</a></li>
          <li><a href="GetVectorsByType.do?type=fluorescent marker" title= "Fluorescence Marker">Fluorescence Marker</a></li>
        </ul>
      </li>
      <li><a href="Submission.jsp" title="Submit plasmids">Submission</a></li>
      <li><a href="GetCollectionList.do" title="View List of Collections">Collections</a></li>
      <li><a href="Pricing.jsp" title="pricing">Pricing</a></li>
      <li><a href="cloningstrategies.jsp" title="Learn About Cloning Methods">Learn</a></li>
      <li><a href="Contactus.jsp">Contact</a></li>
      <li><a href="AboutUs.jsp">About Us</a></li>
          <logic:present name="<%=Constants.USER_KEY%>" scope="session">
          <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">      
      
      <li><a class="MenuBarItemSubmenu" href="#" title="Admin">Admin</a>
        <ul>
                    <li><a href="ViewContainers.jsp" title="View Containers">View Containers</a></li>
                    <li><a href="vSearch.jsp" title="Vector Submission">Vector Submission</a></li>
                    <li><a href="vSearchSFD.jsp" title="Vector For Distribution">Vector For Distribution</a></li>
                    <li><a href="PrepareSearchInvoice.do" title="Search Invoices">Search Invoices</a></li>
                    <li><a href="pReceiveSearch.jsp" title="Recieve Plasmids">Receive Plasmids</a></li>
                    <li><a href="AddInstitutionsInput.jsp" title="Add Institutions">Add Institutions</a></li>
                    <li><a href="AddEmtaMembersInput.jsp" title="Add Institution to Expedited MTA Members">Add EP-MTA Members</a></li>   
                    <li><a href="SEQ_InvoiceHome.jsp">Sequencing Invoices</a></li>
          </logic:equal>
          </logic:present>
          <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
          </logic:notPresent> 
         </ul>
      </li>
      
    </ul>
  </div>
 