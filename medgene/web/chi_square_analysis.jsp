<%@page contentType="text/html"%>
<html>
<head><title>chi square analysis</title></head>
<body>
<center><h1>Chi Square Analysis<BR></H1></center>
<center><img src="table.jpg"></center><br><br>
<center><font size=3 face=Arial color=black><b>The 2X2 contingency table</b></font><BR></center>
<center><font size=3 face=Arial color=black><b>a: papers only studying the gene</b></font><BR></center>
<center><font size=3 face=Arial color=black><b>d: papers only studying the disease</b></font><BR></center>
<center><font size=3 face=Arial color=black><b>b: papers studying both the gene and the disease</b></font><BR></center>
<center><font size=3 face=Arial color=black><b>c: papers studying neither the disease nor the gene</b></font><BR></center>
<center><font size=3 face=Arial color=black><b>n = a+b+c+d, total number of papers</b></font><BR></center><br>
<center><font size=5 face=Arial color=black><b> chi square = (n*(|a*d-b*c|-n/2)*(|a*d-b*c|-n/2))/((a+b)(c+d)(a+c)(b+d))</b></font><BR></center>

<jsp:include page="links.jsp" flush="true"/>
</body>
</html>
