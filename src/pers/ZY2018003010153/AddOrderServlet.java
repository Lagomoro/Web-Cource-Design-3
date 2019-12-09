package pers.ZY2018003010153;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddOrderServlet", urlPatterns = { "/addOrder.do" })
public class AddOrderServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String username = null;
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for(int i = 0;i < cookies.length;i++){
				Cookie cookie = cookies[i];
				if(cookie.getName().equals("username")) {
					username = cookie.getValue();
				}
			}
		}
		
		getServletContext().log("添加订单请求：" + username);
		
		if(username == null) {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":3,\"err_msg\":\"请先登录\"}");
			getServletContext().log("添加订单请求：未登录");
			return;
		}
        
		MySQLModel.init();

		if(MySQLModel.haveUser(username)) {
			if (MySQLModel.updateOrder(username, MySQLModel.getUserOrder(username).add(MySQLModel.getUserCart(username).toString())) == 0) {
				MySQLModel.updateCart(username, new Cart(""));
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":0,\"err_msg\":null}");
				getServletContext().log("添加订单请求：添加成功");
			}else {
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":1,\"err_msg\":\"服务器故障\"}");
				getServletContext().log("添加订单请求：服务器故障");
			}
		} else {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":2,\"err_msg\":\"用户不存在\"}");
			getServletContext().log("添加订单请求：用户不存在");
		}
		
	}

}
