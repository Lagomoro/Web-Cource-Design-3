package pers.ZY2018003010153;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GetOrderServlet", urlPatterns = { "/getOrder.do" })
public class GetOrderServlet extends HttpServlet{
	
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
		
		getServletContext().log("获取订单请求：" + username);
        
		if(username == null) {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":1,\"err_msg\":\"请先登录\",\"price\":0}");
			getServletContext().log("获取订单请求：未登录");
			return;
		}
		
		MySQLModel.init();
		
		Order order = MySQLModel.getUserOrder(username);
		
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("{\"err_code\":0,\"err_msg\":\"" + order.toHtml() + "\"}");
		getServletContext().log("获取订单请求：获取成功");
	}

}
