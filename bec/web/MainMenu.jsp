
<%@ page language="java" %>
<%@ page import="java.util.*"%>
<%@ page import="edu.harvard.med.hip.bec.user.*"%>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>
<head>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body bgcolor="#9bbad6">
<center>
<div>
<table>
<tr>
    <TD>
        <center><h3><bean:message key="bec.name"/></h3></center>
    </td>
</tr>
<tr>
    <td><hr></td>
</tr>


<% 
    ArrayList menu_list = (ArrayList)session.getAttribute("menulist");
     String group_name = null;
    String prev_menu_group = "first_group";
    boolean isInGroup = false;
    boolean isStartGroup = false;
    boolean isEndGroup = false;
    MenuItem menu_item = null;
    for (int menu_count = 0; menu_count < menu_list.size(); menu_count++)
    {
         menu_item = (MenuItem) menu_list.get(menu_count);
         group_name = menu_item.getMenuGroup();
       
         isStartGroup = ( !group_name.equals(prev_menu_group) && !group_name.equals("None") ) ?  true : false;
         
         isEndGroup = ( !group_name.equals(prev_menu_group) && isInGroup) ?  true : false;
         if ( group_name.equals("None") && isInGroup) isEndGroup =  true ;
         isInGroup = group_name.equalsIgnoreCase("None")  ? false : true;
         prev_menu_group = group_name;
       

    // display home menu
          if (isEndGroup)
            {%>

                </table>
                </td></tr>
                <tr><td>&nbsp;</tr></td>
              
            <%}
            if ( isStartGroup )//print group name
            {%>
                <TR><TD><B><%=group_name %></b>
                <table  border="0">
            <%

            }
        if ( !isInGroup )
        {
        %>
            <tr>
                <td class="label">
                    <small>
                    <html:link forward='<%=menu_item.getMenuItem()%>' target="display"> 
                        <%=menu_item.getDescription()%>
                    </html:link>
                    </small>
                </td>
            </tr>

        <%
            if (menu_count == 0) %><tr>    <td>&nbsp</td>  </tr>  <%       
        } 
        else
        {
           %>
           
            <tr>
                <td width="10%"> &nbsp;</td>
                 <td class="label">
                    <small>
                    <html:link forward='<%=menu_item.getMenuItem()%>' target="display"> 
                        <%=menu_item.getDescription()%>
                    </html:link>
                    </small>
                </td>
           </tr>
           <%

        }

    
    }


%>


<tr>
    <td class="label">
    <small>
    <html:link forward='logout' target="_top"> 
       Logout
    </html:link>
    </small>
    </td>
</tr> 
<tr>
    <td><hr></td>
</tr>
<tr>
    <td>&nbsp</td>
</tr> 
<tr>
    <td>
        <small>
        <address><a href="mailto:flexgene_support@hms.harvard.edu">FLEXGene Support</a></address>
        </small>
     </td>
</tr>
<tr>
    <td>&nbsp</td>
</tr>

</table>


</div>
</center>
</body>
</html>
