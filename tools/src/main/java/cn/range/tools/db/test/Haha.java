package cn.range.tools.db.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class Haha {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/financial_db", "root", "admin");
		conn.close();
		System.out.println(conn.isClosed()); // true after conn.close() if no
												// exception
		System.out.println(conn == null); // not null after conn.close()

		Connection emptyConn = null;
		emptyConn.isClosed(); // NullPointerException
	}

}
