package pers.ZY2018003010153;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteOrderServlet", urlPatterns = { "/deleteOrder.do" })
public class DeleteOrderServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String username = null;
		int id = Integer.parseInt(request.getParameter("id"));
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for(int i = 0;i < cookies.length;i++){
				Cookie cookie = cookies[i];
				if(cookie.getName().equals("username")) {
					username = cookie.getValue();
				}
			}
		}
		
		getServletContext().log("删除订单请求：" + username + ", " + id);
		
		if(username == null) {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":3,\"err_msg\":\"请先登录\"}");
			getServletContext().log("删除订单请求：未登录");
			return;
		}
        
		MySQLModel.init();

		if(MySQLModel.haveUser(username)) {
			if (MySQLModel.updateOrder(username, MySQLModel.getUserOrder(username).delete(id)) == 0) {
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":0,\"err_msg\":null}");
				getServletContext().log("删除订单请求：删除成功");
			}else {
				response.setContentType("text/plain;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{\"err_code\":1,\"err_msg\":\"服务器故障\"}");
				getServletContext().log("删除订单请求：服务器故障");
			}
		} else {
			response.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("{\"err_code\":2,\"err_msg\":\"用户不存在\"}");
			getServletContext().log("删除订单请求：用户不存在");
		}
		
	}

}
