<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>New user</title>

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

	<form action="new" method="post">
		<fieldset>
			<legend>New user registration</legend>
			
			First name:<br>
			<input type="text" name="firstName"
				value='<c:out value="${form.firstName}"/>' size="30" maxlength="30"
				placeholder="First name" autofocus>
			<br>
			<c:if test="${form.hasError('firstName')}">
				<div class="error">
					<c:out value="${form.getError('firstName')}" />
				</div>
			</c:if>
			<br>
			
			Last name:<br>
			<input type="text" name="lastName"
				value='<c:out value="${form.lastName}"/>' size="30" maxlength="50"
				placeholder="Last name">
			<br>
			<c:if test="${form.hasError('lastName')}">
				<div class="error">
					<c:out value="${form.getError('lastName')}" />
				</div>
			</c:if>
			<br>
			
			Email:<br>
			<input type="email" name="email"
				value='<c:out value="${form.email}"/>' size="30" maxlength="100"
				placeholder="email" autocomplete="off">
			<br>
			<c:if test="${form.hasError('email')}">
				<div class="error">
					<c:out value="${form.getError('email')}" />
				</div>
			</c:if>
			<br>
			
			Nick:<br>
			<input type="text" name="nick"
				value='<c:out value="${form.nick}"/>' size="30" maxlength="30"
				placeholder="Nick">
			<br>
			<c:if test="${form.hasError('nick')}">
				<div class="error">
					<c:out value="${form.getError('nick')}" />
				</div>
			</c:if>
			<br>
			
			Password:<br>
			<input type="password" name="password"
				value='<c:out value="${form.password}"/>' size="30"
				placeholder="Password">
			<br>
			<c:if test="${form.hasError('password')}">
				<div class="error">
					<c:out value="${form.getError('password')}" />
				</div>
			</c:if>
			<br>
			
			<input type="submit" name="action" value="Save">
			<input type="submit" name="action" value="Cancel">
			
		</fieldset>
	</form>

</body>
</html>