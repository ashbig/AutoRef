<%@page contentType="text/html"%>
<html>
<head><title>product of incidence</title></head>
<body>
<center><h1>Product of Incidence<BR></H1></center>
<center><img src="circle.jpg"></center><br><br>
<center><font size=5 face=Arial color=blue><b>product of incidence = (Y/(X+Y))*(Y/(Y+Z))</b></font><BR></center>
<center><b>Y/(X+Y): incidence rate of this disease out of the papers studying this gene</b><BR></center>
<center><b>Y/(Y+Z): incidence rate of this gene out of the papers studying this disease</b><BR></center>
<br>
<center><b>For example, the distribution of product of incidence (log value) for all the breast cancer related genes</b></center>
<center><img src="product_incidence.jpg"></center>
<jsp:include page="links.jsp" flush="true"/> 
</body>
</html>
