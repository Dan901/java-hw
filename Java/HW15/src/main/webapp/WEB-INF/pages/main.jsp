<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>DPS Blog</title>

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
			<form action="logout" method="post">
				<input type="submit" value="Logout">
			</form>
		</c:when>
		
		<c:otherwise>
			<h4>You are currently not logged in.</h4>
			<form action="login" method="post">
				<fieldset>
					<legend>Log in</legend>
					
					Nick:<br>
					<input type="text" name="nick"
						value='<c:out value="${form.nick}"/>' size="20">
					<br>
					<c:if test="${form.hasError('nick')}">
						<div class="error">
							<c:out value="${form.getError('nick')}" />
						</div>
					</c:if>
					<br>
					
					Password:<br>
					<input type="password" name="password"
						value='<c:out value="${form.password}"/>' size="20">
					<br>
					<c:if test="${form.hasError('password')}">
						<div class="error">
							<c:out value="${form.getError('password')}" />
						</div>
					</c:if>
					<br>
					<input type="submit" name="action" value="Login">
				</fieldset>
			</form>
			<br>
			<br>
			For new user registration click: <a href="register">Registaration</a>
		</c:otherwise>
	</c:choose>
	<br>
	<br>
	
	<p>List of registered authors. Click on one to see their blog entries.</p>
	<ul>
		<c:forEach var="user" items="${users}">
			<li><a href="author/${user.nick}">${user.nick}</a></li>
		</c:forEach>
	</ul>
	
</body>
</html>