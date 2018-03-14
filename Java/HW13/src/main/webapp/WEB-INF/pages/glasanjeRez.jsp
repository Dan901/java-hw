<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Rezultati</title>
<style>
table, th, td {
	text-align: center;
	border: 1px solid black;
	border-collapse: collapse;
}

th, td {
	padding: 5px;
}
</style>
</head>
<body style="background-color:${pickedBgCol};">
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table>
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="entry" items="${results}">
				<tr>
					<td>${entry.value.name}</td>
					<td>${entry.value.votes}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<br>
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="/webapp2/glasanje-grafika" />
	<br>
	<br>
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="/webapp2/glasanje-xls">ovdje</a>.
	</p>
	<br>
	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<c:forEach var="entry" items="${winners}">
			<li><a href="${entry.value.songURL}">${entry.value.name}</a></li>
		</c:forEach>
	</ul>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Go back</a>
</body>
</html>
