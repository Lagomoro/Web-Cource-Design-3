package pers.ZY2018003010153;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class MySQLModel {
	
	public final static String SQL_USER     = "webteach";
	public final static String SQL_PASSWARD = "webteach";
	public final static String SQL_URL      = "jdbc:mysql://202.194.14.120:3306/webteach?useSSL=true&serverTimezone=GMT%2B8&characterEncoding=utf8";
	
	public final static String TABLE_NAME    = "web201800301015_zy3_";
	public final static String TABLE_USER    = TABLE_NAME + "userinfo";
	public final static String TABLE_PRODUCT = TABLE_NAME + "product";
	
	public static Connection connection;
	
	public static PreparedStatement statementUserTable;
	public static PreparedStatement statementProductTable;
	
	public static PreparedStatement statementUserGet;
	public static PreparedStatement statementUserInsert;
	public static PreparedStatement statementUserNickname;
	public static PreparedStatement statementUserPassword;
	public static PreparedStatement statementUserCart;
	public static PreparedStatement statementUserOrder;
	
	public static PreparedStatement statementProductGet;
	public static PreparedStatement statementProductInsert;
	public static PreparedStatement statementProductGetAll;
	public static PreparedStatement statementProductGetUser;
	public static PreparedStatement statementProductDelete;
	
	public static boolean inited = false;

	// ================================================================================
	// * Initialize
	// ================================================================================
	
	public static void init() {
		if(inited)return;
		try {   
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASSWARD);
			
			statementUserTable = connection.prepareStatement("CREATE TABLE " + TABLE_USER + " ("
					+ "id INT NOT NULL AUTO_INCREMENT, "
					+ "username VARCHAR(16) NOT NULL, "
					+ "nickname VARCHAR(16) NOT NULL, "
					+ "password VARCHAR(32) NOT NULL, "
					+ "shopping_cart VARCHAR(256), "
					+ "order_list VARCHAR(256), PRIMARY KEY (id)) ENGINE = InnoDB;");
			statementProductTable = connection.prepareStatement("CREATE TABLE " + TABLE_PRODUCT + " ("
					+ "id INT NOT NULL AUTO_INCREMENT, "
					+ "username VARCHAR(16) NOT NULL, "
					+ "name VARCHAR(64) NOT NULL, "
					+ "image MEDIUMTEXT NOT NULL, "
					+ "content VARCHAR(256) NOT NULL, "
					+ "price DOUBLE NOT NULL, PRIMARY KEY (id)) ENGINE = InnoDB;");
			
			statementUserGet = connection.prepareStatement("SELECT * FROM " + TABLE_USER
					+ " WHERE username = ?");
			statementUserInsert = connection.prepareStatement("INSERT INTO " + TABLE_USER
					+ " (username, nickname, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			statementUserNickname = connection.prepareStatement("UPDATE " + TABLE_USER
					+ " SET nickname = ? WHERE username = ?");
			statementUserPassword = connection.prepareStatement("UPDATE " + TABLE_USER
					+ " SET password = REPLACE (password, ?, ?) WHERE username = ?");
			statementUserCart = connection.prepareStatement("UPDATE " + TABLE_USER
					+ " SET shopping_cart = ? WHERE username = ?");
			statementUserOrder = connection.prepareStatement("UPDATE " + TABLE_USER
					+ " SET order_list = ? WHERE username = ?");
			
			statementProductGet = connection.prepareStatement("SELECT * FROM " + TABLE_PRODUCT
					+ " WHERE id = ?");
			statementProductInsert = connection.prepareStatement("INSERT INTO " + TABLE_PRODUCT
					+ " (username, name, image, content, price) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			statementProductGetAll = connection.prepareStatement("SELECT * FROM " + TABLE_PRODUCT);
			statementProductGetUser = connection.prepareStatement("SELECT * FROM " + TABLE_PRODUCT
					+ " WHERE username = ?");
			statementProductDelete = connection.prepareStatement("DELETE FROM " + TABLE_PRODUCT
					+ " WHERE id = ? AND username = ?");
			
			createUserTable();
			createProductTable();
			
			inited = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// ================================================================================
	// * Base Functions
	// ================================================================================
	
	public static Connection getConnection() {
		return connection;
	}
	
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	public static boolean haveTable(String table_name) {
		try {
			ResultSet resultSet = connection.getMetaData().getTables(null, null, table_name, null);
			boolean have = resultSet.next();
			resultSet.close();
			return have;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// ================================================================================
	// * Table Check
	// ================================================================================
	
	public static void createUserTable() {
		try {
			if(!haveTable(TABLE_USER)){
				statementUserTable.executeLargeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createProductTable() {
		try {
			if(!haveTable(TABLE_PRODUCT)){
				statementProductTable.executeLargeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ================================================================================
	// * Table User
	// ================================================================================
	
	public static boolean haveUser(String username) {
		try {
			statementUserGet.setString(1, username);
			ResultSet resultSet = statementUserGet.executeQuery();
			boolean have = resultSet.next();
			resultSet.close();
			return have;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static ResultSet getUser(String username) {
		try {
			statementUserGet.setString(1, username);
			return statementUserGet.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Current user already exist.
	 */
	public static int insertUser(String username, String password) {
		return insertUser(username, username, password);
	}
	public static int insertUser(String username, String nickname, String password) {
		try {
			statementUserInsert.setString(1, username);
			statementUserInsert.setString(2, nickname);
			statementUserInsert.setString(3, password);
			statementUserInsert.executeUpdate();
			ResultSet resultSet = statementUserInsert.getGeneratedKeys(); 
			if(resultSet.next()) {
				resultSet.close();
				return 0;
			}else {
				resultSet.close();
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 * 2: Invalid old password.
	 */
	public static int resetPassword(String username, String oldPassword, String newPassword) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				if(!resultSet.getString("password").equals(oldPassword)) {
					resultSet.close();
					return 2;
				}else {
					resultSet.close();
					statementUserPassword.setString(1, oldPassword);
					statementUserPassword.setString(2, newPassword);
					statementUserPassword.setString(3, username);
					return 1 - statementUserPassword.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updatePassword(String username, String password) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				int callback = resetPassword(username, resultSet.getString("password"), password);
				resultSet.close();
				return callback;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updateNickname(String username, String nickname) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUserNickname.setString(1, nickname);
				statementUserNickname.setString(2, username);
				return 1 - statementUserNickname.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updateCart(String username, Cart shopping_cart) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUserCart.setString(1, shopping_cart.toString());
				statementUserCart.setString(2, username);
				return 1 - statementUserCart.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 */
	public static int updateOrder(String username, Order order) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				statementUserOrder.setString(1, order.toString());
				statementUserOrder.setString(2, username);
				return 1 - statementUserOrder.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Unknown user.
	 * 2: Invalid password.
	 */
	public static int checkLogin(String username, String password) {
		try {
			if(!haveUser(username)) {
				return 1;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				if(resultSet.getString("password").equals(password)) {
					resultSet.close();
					return 0;
				}else {
					resultSet.close();
					return 2;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public static String getUserNickname(String username) {
		try {
			if(!haveUser(username)) {
				return null;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				String nickname = resultSet.getString("nickname");
				resultSet.close();
				return nickname;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Cart getUserCart(String username) {
		try {
			if(!haveUser(username)) {
				return null;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				String shopping_cart = resultSet.getString("shopping_cart");
				resultSet.close();
				return new Cart(shopping_cart == null ? "" : shopping_cart);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Order getUserOrder(String username) {
		try {
			if(!haveUser(username)) {
				return null;
			} else {
				ResultSet resultSet = getUser(username);
				resultSet.next();
				String order = resultSet.getString("order_list");
				resultSet.close();
				return new Order(order == null ? "" : order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// ================================================================================
	// * Table Product
	// ================================================================================
	
	public static boolean haveProduct(int product_id) {
		try {
			statementProductGet.setInt(1, product_id);
			ResultSet resultSet = statementProductGet.executeQuery();
			boolean have = resultSet.next();
			resultSet.close();
			return have;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Product getProduct(int product_id) {
		try {
			statementProductGet.setInt(1, product_id);
			ResultSet resultSet = statementProductGet.executeQuery();
			if(resultSet.next()) {
				Product product = new Product(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("image"), resultSet.getString("content"), resultSet.getDouble("price"));
				resultSet.close();
				return product;
			}
			resultSet.close();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: Current Product already exist.
	 */
	public static int insertProduct(String username, String name, String image, String content, double price) {
		try {
			statementProductInsert.setString(1, username);
			statementProductInsert.setString(2, name);
			statementProductInsert.setString(3, image);
			statementProductInsert.setString(4, content);
			statementProductInsert.setDouble(5, price);
			statementProductInsert.executeUpdate();
			ResultSet resultSet = statementProductInsert.getGeneratedKeys(); 
			if(resultSet.next()) {
				resultSet.close();
				return 0;
			}else {
				resultSet.close();
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public static ArrayList<Product> getAllProduct() {
		try {
			ResultSet resultSet = statementProductGetAll.executeQuery();
			ArrayList<Product> data = new ArrayList<Product>();
			while (resultSet.next()) {
				data.add(new Product(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("image"), resultSet.getString("content"), resultSet.getDouble("price")));
			}
			resultSet.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Product> getUserProduct(String username) {
		try {
			statementProductGetUser.setString(1, username);
			ResultSet resultSet = statementProductGetUser.executeQuery();
			ArrayList<Product> data = new ArrayList<Product>();
			while (resultSet.next()) {
				data.add(new Product(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("name"), resultSet.getString("image"), resultSet.getString("content"), resultSet.getDouble("price")));
			}
			resultSet.close();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return
	 * 0: Success 
	 * 1: User invalid.
	 * 2: Current Product already exist.
	 */
	public static int deleteProduct(int id, String username) {
		try {
			statementProductDelete.setInt(1, id);
			statementProductDelete.setString(2, username);
			int status = statementProductDelete.executeUpdate();
			return status > 0 ? 0 : 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return 2;
		}
	}
	
}
