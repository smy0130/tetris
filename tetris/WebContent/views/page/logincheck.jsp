<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login check</title>
<style>
	@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap');
	
	* {
		font-family: 'Noto Sans KR', sans-serif;
		border: none;
	}
	
	body {
		padding: 30px;
		background-color: #1BBC9B;
	}
	
	#pageMove {
		background-color: #45e2e2;
		padding: 10px;
	}
</style>
</head>
<body>
	<h3>사용자님 환영합니다!</h3>
	<br />
	User ID : ${param.userid}
	<br />
	User PW : ${param.userpassword}
	<br /> <br />
	<form action="main.jsp">
		<input type="submit" value="게임화면 바로가기" id="pageMove">
	</form>
</body>
</html>
