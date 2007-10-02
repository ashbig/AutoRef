<%@ page contentType="text/html"%>
<%@ page language="java" %>

<%@ page import="java.util.*" %>
<%@ page import="edu.harvard.med.hip.flex.*" %>
<%@ page import="edu.harvard.med.hip.flex.core.*" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>




<html>
<head>
    <title><bean:message key="flex.name"/> :<%= request.getAttribute(Constants.UI_PAGE_TITLE) %></title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
<h2><bean:message key="flex.name"/> : <%= request.getAttribute(Constants.UI_PAGE_TITLE) %></h2>
<hr>
<html:errors/>
<p>


 
   <br>

<%--Loop through all the items  --%>
<logic:present name="<%= Constants.UI_TABLE_NO_DATA %>">
    <%= request.getAttribute(Constants.UI_TABLE_NO_DATA) %>
   
</logic:present>

<logic:present name="linkers">
       
         
         	
        
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow">                       <td>Linker ID</td><td>Linker Name</td><td>Linker Sequence</td>    </tr>
         <logic:iterate id="linker" name="linkers"  >
        
             <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
                
                 <logic:empty name="linker" property="id">  <td>&nbsp;</td> </logic:empty>
                 <logic:notEmpty name="linker" property="id"> <td> <flex:write name="linker" property="id"/>   </td>    </logic:notEmpty>       
                
                    <logic:empty name="linker" property="name">  <td>&nbsp;</td> </logic:empty>
                 <logic:notEmpty name="linker" property="name"> <td> <flex:write name="linker" property="name"/>   </td>    </logic:notEmpty>       
             
                             <logic:empty name="linker" property="sequence">  <td>&nbsp;</td> </logic:empty>
		                    <logic:notEmpty name="linker" property="sequence"> <td> <flex:write name="linker" property="sequence"/>   </td>    </logic:notEmpty>   
		                     
          
           
            </flex:row>
             
        </logic:iterate>
    </table>
    
</logic:present>

<logic:present name="vectors">
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow"> <td>Vector ID</td><td>Vector Name</td><td>Vector Source</td><td>Vector Type</td> 
   <td>Description</td> <td>HIP Name</td>  </tr>
       
        <logic:iterate id="vector" name="vectors"  >
          <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
   
              <logic:empty name="vector" property="vectorid" >           <td>&nbsp;</td>  </logic:empty>
      <logic:notEmpty name="vector" property="vectorid"  ><td> <bean:write name="vector" property="vectorid"/>  </td> </logic:notEmpty>
 
          <logic:empty name="vector" property="name" >           <td>&nbsp;</td>  </logic:empty>
      <logic:notEmpty name="vector" property="name"  ><td> <bean:write name="vector" property="name"/>  </td> </logic:notEmpty>
 
         <logic:empty name="vector" property="source" >           <td>&nbsp;</td>  </logic:empty>
      <logic:notEmpty name="vector" property="source"  ><td> <bean:write name="vector" property="source"/>  </td> </logic:notEmpty>
 
      <logic:empty name="vector" property="type" ><td>&nbsp;</td></logic:empty>
     <logic:notEmpty name="vector" property="type"  > <td> <bean:write name="vector" property="type"/>  </td>                       </logic:notEmpty>
        
        <logic:empty name="vector" property="description" ><td>&nbsp;</td></logic:empty>
	 <logic:notEmpty name="vector" property="description"  > <td><bean:write name="vector" property="description"/>  </td>         </logic:notEmpty>
       
         <logic:empty name="vector" property="hipname" ><td>&nbsp;</td></logic:empty>
	 <logic:notEmpty name="vector" property="hipname"  > <td><bean:write name="vector" property="hipname"/>  </td>         </logic:notEmpty>
       
          
          </flex:row>
        </logic:iterate>
    </table>
</logic:present>


<logic:present name="clstrategies">
    <TABLE border="1" cellpadding="2" cellspacing="0">
        <tr class="headerRow"> <td>Cloning Strategy Name</td><td>Vector Name</td><td>5p Linker Name</td>    <td>3p Linker Name</td> <td>Type</td>  </tr>
 <% ArrayList cl = (ArrayList) request.getAttribute("clstrategies");
        %>
        
        <logic:iterate id="str" name="clstrategies"  >
          <flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
          <logic:empty name="str" property="name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="name"  > <td><bean:write name="str" property="name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="clonevector.name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="clonevector.name"  > <td><bean:write name="str" property="clonevector.name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="linker5p.name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="linker5p.name"  > <td><bean:write name="str" property="linker5p.name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="linker3p.name" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="linker3p.name"  > <td><bean:write name="str" property="linker3p.name"/></td> </logic:notEmpty>
         
          <logic:empty name="str" property="type" > <td>&nbsp;</td></logic:empty>
          <logic:notEmpty name="str" property="type"  > <td><bean:write name="str" property="type"/></td> </logic:notEmpty>
            
      
      
            </flex:row>
        </logic:iterate>
    </table>
</logic:present>


<br>


    
</body>
</html>