<%@page contentType="text/html"%>
<html>
<head><title><font size=11 face=Arial color=black><b>probability</b></font></title></head>
<body>
<center><h1>Probability<BR></H1></center>
<center><img src="circle.jpg"></center><br><br>
<center><font size=5 face=Arial color=blue><b>Probability = Y/(X+Y+Z)</b></font><BR></center>
<center><b>Y: papers studying both the gene and the disease</b><BR></center>
<center><b>X+Y+Z: papers studying either the gene or the disease</b><BR></center>
<center><b>The less negative the log value, the greater the correlation.</b><BR></center><br>
<BR>
<center><b>For example, the distribution of probability (log value) for all the breast cancer related genes</b></center>
<center><img src="probability.jpg"></center>
<jsp:include page="links.jsp" flush="true"/> 
</body>
</html>
