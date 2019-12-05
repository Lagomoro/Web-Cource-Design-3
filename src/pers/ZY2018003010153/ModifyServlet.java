package pers.ZY2018003010153;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ModifyServlet", urlPatterns = { "/modify.do" })
public class ModifyServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String username = null;
		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");
		String new_password = request.getParameter("new_password");
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for(int i = 0;i < cookies.length;i++){
				Cookie cookie = cookies[i];
				if(cookie.getName().equals("username")) {
					username = cookie.getValue();
				}
			}
		}
		
		getServletContext().log("修改信息请求：" + username + ", " + nickname + ", " + password + ", " + new_password);
		
		if(username == null) {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":5,\"err_msg\":\"请先登录\"}");
			getServletContext().log("修改信息请求：未登录");
			return;
		}
        
		MySQLModel.init();

		String err_msg = "";
		int err_code = 0;
		if(MySQLModel.haveUser(username)) {
			if (nickname != "") {
				if (MySQLModel.updateNickname(username, nickname) == 0) {
					err_code += 0;
					err_msg += "昵称修改成功 ";
					getServletContext().log("修改信息请求：昵称修改成功");
				} else {
					err_code += 1;
					err_msg += "昵称修改失败 ";
					getServletContext().log("修改信息请求：昵称修改失败");
				}
			}
			if (password != "" && new_password != "") {
				if (MySQLModel.resetPassword(username, password, new_password) == 0) {
					err_code += 0;
					err_msg += "密码修改成功";
					getServletContext().log("修改信息请求：密码修改成功");
				} else {
					err_code += 2;
					err_msg += "密码修改失败，旧密码错误";
					getServletContext().log("修改信息请求：密码修改失败");
				}
			}
		} else {
			err_code = 4;
			err_msg = "用户不存在";
			getServletContext().log("修改信息请求：用户不存在");
		}
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("{\"err_code\":" + err_code + ",\"err_msg\":\"" + err_msg + "\"}");
	}

}
