<%@page contentType="text/html"%>
<html>
<head><title>relative risk of disease</title></head>
<body>
<center><h1>Relative Risk of Disease<BR></H1></center>
<center><img src="table.jpg"></center><br><br>
<center><b>The 2X2 contingency table</b><BR></center>
<center><b>a: papers only studying the gene</b><BR></center>
<center><b>d: papers only studying the disease</b><BR></center>
<center><b>b: papers studying both the gene and the disease</b><BR></center>
<center><b>c: papers studying neither the disease nor the gene</b><BR></center>
<center><b>n = a+b+c+d, total number of papers</b><BR></center><br>
<center><font size=5 face=Arial color=blue><b>relative risk of disease = (b/(b+d))/(a/(a+c))</b></font><BR></center>
<BR>
<center><b>For example, the distribution of relative risk of disease (log value) for all the breast cancer related genes</b></center>
<center><img src="relative_risk_disease.jpg"></center>
<jsp:include page="links.jsp" flush="true"/>
</body>
</html>

