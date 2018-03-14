<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Glasanje</title>
</head>
<body style="background-color:${pickedBgCol};">
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na
		link kako biste glasali!</p>
	<ol>
		<c:forEach var="e" items="${bands}">
			<li><a href="glasanje-glasaj?id=${e.key}">${e.value.name}</a></li>
		</c:forEach>
	</ol>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Go back</a>
</body>
</html>
