<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Vote</title>
</head>
<body>
	<h1>${poll.title}</h1>
	<p>${poll.message}</p>
	<ol>
		<c:forEach var="option" items="${options}">
			<li><a href="voting-vote?id=${option.id}">${option.title}</a></li>
		</c:forEach>
	</ol>
	<br>
	<br>
	<a href="/webapp-baza/index.html">Go back</a>
</body>
</html>