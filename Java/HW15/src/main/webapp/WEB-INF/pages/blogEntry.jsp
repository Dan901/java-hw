<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<head>
<title>Blog</title>

<style type="text/css">
.error {
	font-family: fantasy;
	font-weight: bold;
	font-size: 0.9em;
	color: #FF0000;
}
</style>
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

	<h2>${entry.title}</h2>
	<textarea rows="20" cols="100" readonly>${entry.text}</textarea>
	<br>

	<p>
		Author: <i>${entry.creator.nick}</i><br>
		Created: <fmt:formatDate type="both" dateStyle="short" timeStyle="short"
			value="${entry.createdAt}" /> <br>
		Last modified: <fmt:formatDate type="both" dateStyle="short" timeStyle="short"
			value="${entry.lastModifiedAt}" /><br>
		<c:if test="${userIsAuthor}">
			Click here to <a href="edit?id=${entry.id}">edit</a>.
		</c:if>
	</p>
	<br>
	
	<h3>Comments:</h3>
	<ul>
		<c:forEach var="comment" items="${entry.comments}">
			<li>
				${comment.message} - 
				(<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${comment.postedOn}" />)
			</li>
			<br>
		</c:forEach>
	</ul>
	<br>
	<form action="${entry.id}" method="post">
		<fieldset>
			<legend>New comment</legend>
			
			Email:<br>
			<input type="email" name="email"
				value='<c:out value="${form.email}"/>' size="30" maxlength="100"
				placeholder="Email">
			<br>
			<c:if test="${form.hasError('email')}">
				<div class="error">
					<c:out value="${form.getError('email')}" />
				</div>
			</c:if>
			<br>
			
			Message:<br>
			<textarea name="message" rows="3" cols="60" maxlength="4096"></textarea>
			<br>
			<c:if test="${form.hasError('message')}">
				<div class="error">
					<c:out value="${form.getError('message')}" />
				</div>
			</c:if>
			<br>
			
			<input type="submit" name="action" value="Save">
			<input type="submit" name="action" value="Cancel">
			
		</fieldset>
	</form>
	

	<br>
	<br>
	<a href="/blog">Go to home page</a>
</body>
</html>