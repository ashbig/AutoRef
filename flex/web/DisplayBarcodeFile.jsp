<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title><bean:message key="flex.name"/> : Barcode File</title></head>
<body>
<hr>
<html:errors/>
<p>
<pre>
<bean:write name="barcodeFile"/>
</pre>

</body>
</html>

