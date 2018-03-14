<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Results</title>
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
<body>
	<h1>Voting results</h1>
	<p>This are the results of voting.</p>
	<table>
		<thead>
			<tr>
				<th>Option</th>
				<th>Votes</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="option" items="${options}">
				<tr>
					<td>${option.title}</td>
					<td>${option.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<br>
	<h2>Graphic display of results</h2>
	<img alt="Pie-chart" src="voting-graphic?pollID=${poll.id}" />
	<br>
	<br>
	<h2>Results in XLS format</h2>
	<p>
		Results in XLS format are available <a
			href="voting-xls?pollID=${poll.id}">here</a>.
	</p>
	<br>
	<h2>Miscellaneous</h2>
	<p>Links for winning options:</p>
	<ul>
		<c:forEach var="option" items="${winners}">
			<li><a href="${option.link}">${option.title}</a></li>
		</c:forEach>
	</ul>
	<br>
	<br>
	<a href="/webapp-baza/index.html">Go back</a>
</body>