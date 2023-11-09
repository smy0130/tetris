<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register Page</title>
<style>
    @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300;400;500;700;900&display=swap');
    
    * {
        font-family: 'Noto Sans KR', sans-serif;
    }
    
    body {
        background-color: #1BBC9B;
    }
    
    form {
        padding: 30px;
    }
    
    input {
        width: 100%;
        padding: 10px;
        box-sizing: border-box;
        border-radius: 5px;
        border: none;
    }
    
    #registerBtn {
        margin-top: 10px;
        background-color: #8ce245;
    }
    
    #pageMove {
        background-color: #45e2e2;
    }
</style>
</head>
<body>
    <form action="/tetris/Register" method="POST">
        User ID : <input type="text" name="userid" placeholder="ID를 입력해주세요."><br>
        User PW : <input type="password" name="userpassword" placeholder="PW를 입력해주세요."><br>
        User E-mail : <input type="text" name="useremail" placeholder="E-mail를 입력해주세요."><br>
        <input type="submit" value="회원가입" id="registerBtn">
    </form>
    <form action="loginpage.jsp">
        <input type="submit" value="로그인 바로가기" id="pageMove">
    </form>
</body>

</html>
