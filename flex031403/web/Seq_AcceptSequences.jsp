<%@page contentType="text/html"%>
<html>
<head><title>Approve sequences</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
<link href="../developed/flex/web/FlexStyle.css" rel="stylesheet" type="text/css">
</head>

<body background='background.gif'>
<h2><bean:message key="flex.name"/> Approve sequences</h2>
<hr>
<html:errors/> 
<html:form action="/logon.do" >
<table width="80%" border="2" cellspacing="2" cellpadding="2">
  <tr> 
    <td><div align="center"><strong>Sequence Id</strong></div></td>
    <td><div align="center"><strong>Accept</strong></div></td>
    <td><div align="center"><strong>Reject</strong></div></td>
  </tr>
  <tr> 
    <td>1234</td>
    <td><div align="center"> 
        <input type="checkbox" name="checkbox2" checked value="checkbox">
      </div></td>
    <td><div align="center"> 
        <input type="checkbox" name="checkbox3" value="checkbox">
      </div></td>
  </tr>
  <tr> 
    <td>1233</td>
    <td><div align="center"> 
        <input name="checkbox4" type="checkbox" value="checkbox" checked>
      </div></td>
    <td><div align="center"> 
        <input type="checkbox" name="checkbox5" value="checkbox">
      </div></td>
  </tr>
  <tr> 
    <td>3456</td>
    <td><div align="center"> 
        <input type="checkbox" name="checkbox6" value="checkbox">
      </div></td>
    <td><div align="center"> 
        <input name="checkbox7" type="checkbox" value="checkbox" checked>
      </div></td>
  </tr>
</table>
<p> </html:form></p>

</body>
</html>
