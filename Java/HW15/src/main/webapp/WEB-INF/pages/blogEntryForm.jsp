<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Blog entry</title>

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
	
	<h4>Welcome ${user_fn} ${user_ln}</h4>
	<form action="/blog/servlets/logout" method="post">
		<input type="submit" value="Logout">
	</form>
	<br>

	<form action="save?id=${form.id}" method="post">
		<fieldset>
			<legend>New blog entry</legend>
			
			Title:<br>
			<input type="text" name="title"
				value='<c:out value="${form.title}"/>' size="30" maxlength="60"
				placeholder="Title" autofocus>
			<br>
			<c:if test="${form.hasError('title')}">
				<div class="error">
					<c:out value="${form.getError('title')}" />
				</div>
			</c:if>
			<br>
			
			Text:<br>
			<textarea name="text" rows="20" cols="100" maxlength="4096">${form.text}</textarea>
			<br>
			<c:if test="${form.hasError('text')}">
				<div class="error">
					<c:out value="${form.getError('text')}" />
				</div>
			</c:if>
			
			<input type="submit" name="action" value="Save">
			<input type="submit" name="action" value="Cancel">
			
		</fieldset>
	</form>
	
</body>
</html>