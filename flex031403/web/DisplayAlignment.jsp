<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head><title><bean:message key="flex.name"/> : View Alignment</title></head>
<body>
<h2><bean:message key="flex.name"/>: View Blast Alignment</h2>
<hr>

<pre>
<bean:write name="alignment"/>
</pre>

</body>
</html>
