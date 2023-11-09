package com.team.tetris.login.register;


	import java.io.IOException;
	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;

	@WebServlet("/Register")
	public class Register extends HttpServlet {

	    private static final long serialVersionUID = 1L;

	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        request.setCharacterEncoding("UTF-8");

	        
	        String userid = request.getParameter("userid");
	        String userpassword = request.getParameter("userpassword");	      
	        String useremail = request.getParameter("useremail");

	        String jdbcUrl = "jdbc:oracle:thin:@192.168.0.126:1521:XE"; 
	        String jdbcUser = "tetris"; 
	        String jdbcPassword = "1234";

	        try {
	            // JDBC 드라이버 로드
	            Class.forName("oracle.jdbc.driver.OracleDriver");

	            // 데이터베이스 연결
	            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

	            // SQL 쿼리
	            String sql = "INSERT INTO USERINFO (USERID, USERPASSWORD, USEREMAIL) VALUES (?, ?, ?)";

	            // SQL 쿼리 실행을 위한 PreparedStatement 작성하였음
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, userid);
	            preparedStatement.setString(2, userpassword);
	            preparedStatement.setString(3, useremail);

	            // 쿼리 실행문을 만들어봄 
	            int rowsInserted = preparedStatement.executeUpdate();

	            // 연결과 자원 반환
	            preparedStatement.close();
	            connection.close();

	            if (rowsInserted > 0) {
	                // 회원가입 성공 시, 어떤 페이지로 리다이렉트할지 여기에서 설정
	            	System.out.println("회원가입완료");
	                response.sendRedirect("http://localhost:8090/loginpage.jsp"); // 성공 페이지 경로로 수정
	            } else {
	                // 회원가입 실패 시, 어떤 페이지로 리다이렉트할지 여기에서 설정
	            	System.out.println("이미 있는 아이디 입니다.다시 만들어주세요");
	                response.sendRedirect("http://localhost:8090/tetris/main/register.jsp"); // 실패 페이지 경로로 수정
	            }
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}



