<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Trigonometric values</title>
<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 5px;
}
</style>
</head>
<body style="background-color:${pickedBgCol};">
	<a href="/webapp2/index.jsp">Go back</a>
	<br>
	<br>
	<table>
		<thead>
			<tr>
				<th>x</th>
				<th>sin(x)</th>
				<th>cos(x)</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="e" items="${values}">
				<tr>
					<td>${e.key}</td>
					<td>${e.value.sinValue}</td>
					<td>${e.value.cosValue}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>