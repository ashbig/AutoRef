<%@page contentType="text/html"%>
<html>
<head><title>product of frequency</title></head>
<body>
<center><h1>Product of frequency<BR></H1></center>
<center><img src="circle.jpg"></center><br><br>
<center><font size=5 face=Arial color=blue><b>product of frequency = (Y/(X+Y))*(Y/(Y+Z))</b></font><BR></center>
<center><b>Y/(X+Y): frequency rate of this disease out of the papers studying this gene</b><BR></center>
<center><b>Y/(Y+Z): frequency rate of this gene out of the papers studying this disease</b><BR></center>
<center><b>The less negative the log value, the greater the correlation.</b><BR></center><br>
<br>
<center><b>For example, the distribution of product of frequency (log value) for all the breast cancer related genes</b></center>
<center><img src="product_incidence.jpg"></center>
<jsp:include page="links.jsp" flush="true"/> 
</body>
</html>
