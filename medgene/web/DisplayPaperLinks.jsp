<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title>Medline Paper Links</title></head>
<body>
<center>
<h1>Selected Paper Links</h1>
</center>
<p>
    &nbsp;&nbsp;<b>Disease Mesh Term:</b>&nbsp;&nbsp;<bean:write name="disease_name"/><br>
    &nbsp;&nbsp;<b>Gene Name:</b>&nbsp;&nbsp;<bean:write name="gene_symbol"/><br><br>
    &nbsp;&nbsp;The papers are listed in descending order of the PubMed ID.<br><br>
    <%  String url, s; %>
    <%  java.util.Vector records = (java.util.Vector)(request.getAttribute("medline_records"));
        if(records.size() == 0){ %>
            <b> &nbsp;&nbsp; No paper found. </b> <br>
     <% }
        else if(records.size() <= 200){
            url = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids=";
            s = "";
            for (int n=0; n < records.size(); n++){
                s = s + ((String)(records.elementAt(n))).toString() + ",";
            }
            url = url + s.substring(0, s.length()-1);
     %>
            <logic:redirect href="<%= url %>" />
    <%  }    
        else{
            int i = records.size()/200;
            int j = records.size()%200;
            for (int k=0; k < i; k++){ 
                s = "";
                url = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids=";
                for (int a=200*k+1; a <= 200*(k+1); a++){
                    s = s + ((String)(records.elementAt(a-1))).toString() + ",";
                }
                url = url + s.substring(0, s.length()-1);
                %>            
                <li> <a href="<%= url %>" target="_blank"> <%= 200*k+1 %> - <%= 200*(k+1) %> </a> </li>
         <% } 

            s = "";
            url = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids=";
            for (int a=200*i+1; a <= 200*i+j; a++){
                s = s + ((String)(records.elementAt(a-1))).toString() + ",";               
            }
            url = url + s.substring(0, s.length()-1);
         %>
            <li> <a href="<%= url %>" target="_blank"> <%= 200*i+1 %> - <%= 200*i+j %> </a> </li>
     <% } %>
     
<p>
<br>
<jsp:include page="links.jsp" flush="true"/>

</body>
</html>


