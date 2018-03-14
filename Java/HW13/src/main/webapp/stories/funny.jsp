<%@page import="java.util.Random"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%
	Random r = new Random();
	String color = "rgb(" + r.nextInt(255) + ", " + r.nextInt(255) + ", " + r.nextInt(255) + ")";
%>

<html>
<head>
<title>Funny story</title>
</head>
<body style="background-color:${pickedBgCol};">
	<h3 style="color: <%=color%> ;">
		A man flying in a hot air balloon suddenly realizes he’s lost. He
		reduces height and spots a man down below. He lowers the balloon
		further and shouts to get directions, "Excuse me, can you tell me
		where I am?"<br> The man below says: "Yes. You're in a hot air
		balloon, hovering 30 feet above this field."<br> "You must work
		in Information Technology." says the balloonist.<br> "I do"
		replies the man. "How did you know?"<br> "Well," says the
		balloonist, "everything you have told me is technically correct, but
		it's of no use to anyone."<br> The man below replies, "You must
		work in management."<br> "I do." replies the balloonist, "But
		how'd you know?"<br> "Well", says the man, "you don’t know where
		you are or where you’re going, but you expect me to be able to help.
		You’re in the same position you were before we met, but now it’s my
		fault."
	</h3>
	<br>
	<a href="/webapp2/index.jsp">Go back</a>
</body>
</html>