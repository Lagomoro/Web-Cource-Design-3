package pers.ZY2018003010153;

public class Product {
	
	private int id;
	private String username;
	private String name;
	private String image;
	private String content;
	private double price;
	
	public Product(int id, String username, String name, String image, String content, double price) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.image = image;
		this.content = content;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void toHtml(StringBuilder builder) {
		builder.append("<div><img src=\\\"" + this.image +  "\\\"/><div><label>" + this.name + 
				" &nbsp;&nbsp;单价：" + this.price + "元 &nbsp;&nbsp;&nbsp;&nbsp;" + 
				"<a href=\\\"javascript:void(0)\\\" onclick=\\\"deleteShop(" + this.getId() + ")\\\">下架</a></label>");
		builder.append("<label>" + this.content + "</label></div></div>");
	}
	
	public void toStorageHtml(StringBuilder builder) {
		builder.append("<div><img src=\\\"" + this.image +  "\\\"/><div><label>" + this.name + 
				" &nbsp;&nbsp;单价：" + this.price + "元 &nbsp;&nbsp;&nbsp;&nbsp;" + 
				"<a href=\\\"javascript:void(0)\\\" onclick=\\\"addToCart(" + this.getId() + ")\\\">加入购物车</a></label>");
		builder.append("<label>" + this.content + "</label></div></div>");
	}
	
	public void toCartHtml(StringBuilder builder, int amount) {
		builder.append("<div><img src=\\\"" + this.image +  "\\\"/><div><label>" + this.name + 
				" &nbsp;&nbsp;单价：" + this.price + "元  &nbsp;&nbsp;数量：" + 
				"<a href=\\\"javascript:void(0)\\\" onclick=\\\"changeCart(" + this.getId() + ", -1)\\\">&nbsp;-&nbsp;</a> " + amount + 
				" <a href=\\\"javascript:void(0)\\\" onclick=\\\"changeCart(" + this.getId() + ", 1)\\\">&nbsp;+&nbsp;</a>" + "</label>");
		builder.append("<label>" + this.content + "</label></div></div>");
	}

}
