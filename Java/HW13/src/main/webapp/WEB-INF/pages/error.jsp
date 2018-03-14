<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Error</title>
</head>
<body style="background-color:${pickedBgCol};">
	<h2>An error occurred!</h2>
	<p>${message}</p>
	<br>
	<a href="/webapp2/index.jsp">Go back</a>
</body>
</html>