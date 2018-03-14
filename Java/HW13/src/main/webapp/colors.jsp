<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Background color</title>
</head>
<body style="background-color:${pickedBgCol};">
	<h2>Pick a background color:</h2>
	<a href="setcolor?color=white">WHITE</a>
	<br>
	<a href="setcolor?color=red">RED</a>
	<br>
	<a href="setcolor?color=green">GREEN</a>
	<br>
	<a href="setcolor?color=cyan">CYAN</a>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Go back</a>
</body>
</html>