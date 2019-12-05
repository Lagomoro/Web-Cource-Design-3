package pers.ZY2018003010153;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RegisterServlet", urlPatterns = { "/register.do" })
public class RegisterServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("username");
		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");
		
		getServletContext().log("注册请求：" + username + ", " + nickname + ", " + password);
		
		MySQLModel.init();
		
		if(MySQLModel.haveUser(username)) {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":1,\"err_msg\":\"注册失败：用户已经存在\"}");
			getServletContext().log("注册请求：用户已经存在");
		} else {
			MySQLModel.insertUser(username, nickname, password);
			if (MySQLModel.checkLogin(username, password) == 0) {
				Cookie userCookie = new Cookie("username", username); 
				userCookie.setMaxAge(60*60*24);
				response.addCookie(userCookie);
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":0,\"err_msg\":null}");
				getServletContext().log("注册请求：注册成功");
			} else {
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":2,\"err_msg\":\"注册失败：服务器故障\"}");
				getServletContext().log("注册请求：服务器故障");
			}
		}
	}
	
}
