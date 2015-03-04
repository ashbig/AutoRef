<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %> 
 <link href="layout.css" rel="stylesheet" type="text/css" />
 <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
 <link href="boilerplate.css" rel="stylesheet" type="text/css" />
 <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
 <link rel="shortcut icon" href="dnacore.ico">
 <link rel="stylesheet" type="text/css" href="print.css" media="print">
<link rel="stylesheet" href="responsivemobilemenu.css" type="text/css"/>
<script type="text/javascript" src="http://code.jquery.com/jquery.min.js"></script>
<script type="text/javascript" src="responsivemobilemenu.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
<!-- start mobile menu, jsp version -->
<div id="rmm" class="rmm" data-menu-style = "graphite">
            <ul>
                    <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
                            <li><a href="Login.jsp">Sign In</a></li>
                            <li><a href="PrepareRegistration.do">Registration</a></li>
                            <li><a href="FindPassword.jsp">Find Password</a></li>
                    </logic:notPresent> 
                    <logic:present name="<%=Constants.USER_KEY%>" scope="session">
                        <li><a href="ViewCart.do">My Cart</a></li>
                        <li><a href="Account.jsp">My Account</a></li>
                        <li><a href="ViewOrderHistory.do" title="View Orders">My Orders</a></li>
                    </logic:present>
                        <li><a href="OrderOverview.jsp">Search</a></li>
                        <li><a href="#">More</a></li>
                        <%--<ul>
                            <li><a href="GetVectorsByType.do">Vectors</a></li>
                            <li><a href="GetCollectionList.do" title="View List of Collections">Collections</a></li>
                            <li><a href="Submission.jsp" title="Submit plasmids">Submission</a></li>
                            <li><a href="Pricing.jsp" title="pricing">Pricing</a></li>
                            <li><a href="cloningstrategies.jsp" title="Learn About Cloning Methods">Learn</a></li>
                            <li><a href="Contactus.jsp">Contact</a></li>
                            <li><a href="AboutUs.jsp">About Us</a></li>
                            <li><a target="_blank" href="FAQ.jsp">FAQ</a></li>                
                            <logic:present name="<%=Constants.USER_KEY%>" scope="session">
                            <li><a href="Logout.do">Sign Out</a></li>                        
                            </logic:present>                                                
                        </ul>--%>                            
                    <logic:present name="<%=Constants.USER_KEY%>" scope="session">
                        <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">      
                            <li><a class="MenuBarItemSubmenu" href="Admin.jsp" title="Admin">Admin</a></li>
                        </logic:equal>
                    </logic:present>

            </ul>
</div>
<!-- end mobile menu, jsp version--> 
<br>
<br>
<div id='hmslogo'><a href="http://hms.harvard.edu/" target="_blank"><img id="hmslogo" src="HMS_BiochemMolPhamH.jpg" alt="BCMP"></a>
<table id="cart" width="275px" height="30px" border="0" align="right">
            <tr> 
                <td width="105px" height="12px" border="0" align="left" class="countrytext" valign="middle">
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
                <td width="170px" height="12px" align="right" valign="bottom" class="countrytext"> 
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
            
</table></div>
<div id="header">          
    <br><hr></div>
<br>
<div class="logo">
<img height="55" src="dnacore.jpg"/>
<img height="55" src="PlasmID_logo.jpg"/>

<!-- Alerts to users about closings etc can be typed in this P class. Note: You must update both homeTemplate.xhtml and signinMenuBar.jsp to see this alert throughout the whole site.
    <p>
    <table width ="50%" align="center">
    <td>
    <p class="alertred" align="left">Harvard will be closed for Winter Recess starting Tuesday Dec 24 and will reopen on Monday Jan 5. We will continue to accept plasmid request during this break but they will be filled once we return. For time-sensitive requests you may want to consider a commercial provider.</p>
    </td>
    </table>
    </p>
End Alert Section-->
</div>
<h:body>
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
      
      <li><a class="MenuBarItemSubmenu" href="OrderOverview.jsp">Inserts</a>
        <ul>
          <li><a href="GeneSearch.xhtml" title= "Human and Mouse Gene Search">Human &amp; Mouse Genes</a></li>
          <li><a href="PrepareAdvancedSearch.do?psi=0" title= "Text Search">Text Search</a></li>
          <li><a href="GetDataForRefseqSearch.do" title= "All Organism Gene Search">Search by Gene</a></li>
          <!--<li><a href="GetVectorPropertyTypes.do" title= "Vector Search">Search by Vector</a></li>  confusing search tool, little success finding vector or plasmid of interest   -->
          <li><a href="SearchClone.jsp" title= "Clone ID">Search by Clone ID</a></li>
          <li><a href="PrepareBlast.do" title= "BLAST search">BLAST Search</a></li>
        </ul>
      </li>
      <li><a class="MenuBarItemSubmenu" href="GetVectorsByType.do">Vectors</a>
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
      <li><a href="GetCollectionList.do" title="View List of Collections">Collections</a></li>
      <li><a href="Submission.jsp" title="Submit plasmids">Submission</a></li>
      <li><a href="Pricing.jsp" title="pricing">Pricing</a></li>
      <li><a href="cloningstrategies.jsp" title="Learn About Cloning Methods">Learn</a></li>
      <li><a href="Contactus.jsp">Contact</a></li>
      <li><a href="AboutUs.jsp">About Us</a></li>
          <logic:present name="<%=Constants.USER_KEY%>" scope="session">
          <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">      
      
      <li><a class="MenuBarItemSubmenu" href="Admin.jsp" title="Admin">Admin</a>
        <ul>
                    <li><a href="PrepareSearchInvoice.do" title="Search Invoices">PlasmID Invoices</a></li>
                    <li><a href="SEQ_InvoiceHome.jsp">Sequencing Invoices</a></li>
                    <li><a href="vSearch.jsp" title="Vector Submission">Vector Submission</a></li>
                    <li><a href="vSearchSFD.jsp" title="Vector For Distribution">Vector For Distribution</a></li>
                    <li><a href="pReceiveSearch.jsp" title="Recieve Plasmids">Receive Plasmids</a></li>
                    <li><a href="AddInstitutionsInput.jsp" title="Add Institutions">Add Institutions</a></li>
                    <li><a href="AddEmtaMembersInput.jsp" title="Add Institution to Expedited MTA Members">Add EP-MTA Members</a></li>   

          </logic:equal>
          </logic:present>
          <logic:notPresent name="<%=Constants.USER_KEY%>" scope="session">
          </logic:notPresent> 
         </ul>
      </li>
      
    </ul>
  </div>