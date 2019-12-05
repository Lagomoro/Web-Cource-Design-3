package pers.ZY2018003010153;

import java.util.HashMap;

public class Cart {
	
	private HashMap<Integer, Integer> cartMap = new HashMap<Integer, Integer>();
	
	public Cart(String cartData) {
		String[] data = cartData.split(";");
		String[] temp;
		for (String product : data) {
			if(product.length() >= 3) {
				temp = product.split(",");
				this.cartMap.put(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int product_id : this.cartMap.keySet()) {
			Product product = MySQLModel.getProduct(product_id);
			if(product != null) {
				builder.append(product_id).append(",").append(this.cartMap.get(product_id)).append(";");
			}
		}
		return builder.toString();
	}
	
	public Cart change(int id, int amount) {
		return amount > 0 ? add(id, amount) : minus(id, -amount);
	}
	
	public Cart add(int id, int amount) {
		boolean puted = false;
		for (int product_id : this.cartMap.keySet()) {
			if(id == product_id) {
				this.cartMap.replace(product_id, this.cartMap.get(product_id) + amount);
				puted = true;
			}
		}
		if(!puted) {
			this.cartMap.put(id, amount);
		}
		return this;
	}
	
	public Cart minus(int id, int amount) {
		for (int product_id : this.cartMap.keySet()) {
			if(id == product_id) {
				int number = this.cartMap.get(product_id);
				if(number > amount) {
					this.cartMap.replace(product_id, number - amount);
				}else {
					this.cartMap.remove(product_id);
				}
			}
		}
		return this;
	}
	
	public String toHtml() {
		StringBuilder builder = new StringBuilder();
		for (int product_id : this.cartMap.keySet()) {
			Product product = MySQLModel.getProduct(product_id);
			if(product != null) {
				product.toCartHtml(builder, this.cartMap.get(product_id));
			}
		}
		return builder.toString();
	}
	
	public double getPrice() {
		double price = 0;
		for (int product_id : this.cartMap.keySet()) {
			Product product = MySQLModel.getProduct(product_id);
			if(product != null) {
				int amount = this.cartMap.get(product_id);
				price += product.getPrice() * amount;
			}
		}
		return price;
	}

}
