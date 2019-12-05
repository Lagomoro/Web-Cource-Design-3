package pers.ZY2018003010153;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginServlet", urlPatterns = { "/login.do" })
public class LoginServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		getServletContext().log("登录请求：" + username + ", " + password);
		
		MySQLModel.init();
		
		if(MySQLModel.haveUser(username)) {
			if (MySQLModel.checkLogin(username, password) == 0) {
				Cookie userCookie = new Cookie("username", username); 
				userCookie.setMaxAge(60*60*24);
				response.addCookie(userCookie);
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":0,\"err_msg\":null}");
				getServletContext().log("登录请求：登录成功");
			} else {
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":2,\"err_msg\":\"用户名和密码不匹配\"}");
				getServletContext().log("登录请求：用户名和密码不匹配");
			}
		} else {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":1,\"err_msg\":\"用户不存在\"}");
			getServletContext().log("登录请求：用户不存在");
		}
	}

}
