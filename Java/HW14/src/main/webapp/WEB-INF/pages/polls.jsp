<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
	<title>DPS Polls</title>
</head>
<body>
	<h1>Choose a poll:</h1>
	<ol>
		<c:forEach var="poll" items="${polls}">
			<li><a href="voting?pollID=${poll.id}">${poll.title}</a></li>
		</c:forEach>
	</ol>
</body>
</html>