<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Blog</title>
</head>

<body>
	<c:choose>
		<c:when test="${loggedIn}">
			<h4>Welcome ${user_fn} ${user_ln}</h4>
			<form action="/blog/servlets/logout" method="post">
				<input type="submit" value="Logout">
			</form>
		</c:when>
		
		<c:otherwise>
			<h4>You are currently not logged in.</h4>
		</c:otherwise>
	</c:choose>
	<br>

	<p>
		Blog entries posted by ${author.firstName} ${author.lastName} (<b>${author.nick}</b>):
	</p>
	<ul>
		<c:forEach var="entry" items="${author.entries}">
			<li>
				<a href="${author.nick}/${entry.id}">${entry.title}</a>
			</li>
			<br>
		</c:forEach>
	</ul>
	
	<c:if test="${userIsAuthor}">
		<p>Add new blog entry <a href="${author.nick}/new">here</a>.</p>
	</c:if>
	
	<br>
	<br>
	<a href="/blog">Go to home page</a>
</body>
</html>