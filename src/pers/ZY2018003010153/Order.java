package pers.ZY2018003010153;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
	
	private ArrayList<HashMap<Integer, Integer>> orderMap = new ArrayList<HashMap<Integer, Integer>>();
	
	public Order(String orderData) {
		String[] lists = orderData.split("#");
		for (String listData : lists) {
			if(listData.length() >= 3) {
				this.add(listData);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (HashMap<Integer, Integer> orderList : this.orderMap) {
			for (int product_id : orderList.keySet()) {
				builder.append(product_id).append(",").append(orderList.get(product_id)).append(";");
			}
			builder.append("#");
		}
		return builder.toString();
	}
	
	public Order add(String listData) {
		HashMap<Integer, Integer> orderList = new HashMap<Integer, Integer>();
		this.orderMap.add(orderList);
		String[] data = listData.split(";");
		String[] temp;
		for (String product : data) {
			if(product.length() >= 3) {
				temp = product.split(",");
				orderList.put(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			}
		}
		return this;
	}
	
	public Order delete(int id) {
		this.orderMap.remove(id);
		return this;
	}
	
	public String toHtml() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (HashMap<Integer, Integer> orderList : this.orderMap) {
			builder.append("<div>");
			builder.append("<label style=\\\"margin-bottom: 10px;\\\">总价：" + String.format("%.2f", this.getPrice(orderList)) + "元 &nbsp;&nbsp;<a href=\\\"javascript:void(0)\\\" onclick=\\\"deleteOrder(" + (index++) + ", -1)\\\">删除订单</a></label>");
			for (int product_id : orderList.keySet()) {
				Product product = MySQLModel.getProduct(product_id);
				if(product != null) {
					product.toOrderHtml(builder, orderList.get(product_id));
				} else {
					builder.append("<div>物品不存在</div>");
				}
			}
			builder.append("</div>");
		}
		return builder.toString();
	}
	
	public double getPrice(HashMap<Integer, Integer> orderList) {
		double price = 0;
		for (int product_id : orderList.keySet()) {
			Product product = MySQLModel.getProduct(product_id);
			if(product != null) {
				int amount = orderList.get(product_id);
				price += product.getPrice() * amount;
			}
		}
		return price;
	}

}
