<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>OS usage</title>
</head>
<body style="background-color:${pickedBgCol};">
	<h1>OS usage</h1>
	<p>
		Here are the results of OS usage in survey that we completed.<br>
		<br> <img alt="Pie-chart" src="/webapp2/reportImage">
	</p>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Go back</a>
</body>
</html>