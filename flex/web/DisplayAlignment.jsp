<%@ page language="java" %>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head><title><bean:message key="flex.name"/> : View Alignment</title></head>
<body>
<h2><bean:message key="flex.name"/> : View Blast Alignment</h2>
<hr>
<html:errors/>
<p>
<pre>
<bean:write name="alignment"/>
</pre>

</body>
</html>
