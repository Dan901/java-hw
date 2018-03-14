<%@page import="java.util.concurrent.TimeUnit"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	long millis = System.currentTimeMillis() - (Long) getServletContext().getAttribute("startTime");
	String time = String.format("%01d days %02d hours %02d minutes %02d seconds and %03d milliseconds",
			TimeUnit.MILLISECONDS.toDays(millis),
			TimeUnit.MILLISECONDS.toHours(millis) % TimeUnit.DAYS.toHours(1),
			TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
			TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
			TimeUnit.MILLISECONDS.toMillis(millis) % TimeUnit.SECONDS.toMillis(1));
%>

<html>
<head>
<title>Running time</title>
</head>
<body style="background-color:${pickedBgCol};">
	<p>
		Application has been running for:
		<%=time%>.
	</p>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Go back</a>
</body>
</html>