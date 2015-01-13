package cn.range.tools.db.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import cn.range.tools.db.common.Msg;
import cn.range.tools.db.jobs.BillRetriever;
import cn.range.tools.db.model.Bill;

public class TestBillRetriever {
	private static Connection conn = null;
	private BillRetriever retriever = new BillRetriever();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/financial_db", "root", "admin");
		assert (conn == null);
	}

	// @Test
	public void testGetLastSharingSummary() throws SQLException {
		if (conn != null) {
			double lastSharingSum = retriever.getLastSharingSummary(conn);
			System.out.println(lastSharingSum);
		} else {
			System.out.println(Msg.CONNECTION_NULL);
		}
	}

	// @Test
	public void testGetAllBills() throws SQLException {
		List<Bill> bills = retriever.getAllBills(conn);
		for (Bill bill : bills) {
			bill.printBill();
		}
	}

	@Test
	public void testGetBillsFromStartToEnd() throws SQLException {
		List<Bill> bills = retriever.getBillsFromStartToEnd(conn, "2015-1-11",
				"2015-1-13");
		for (Bill bill : bills) {
			bill.printBill();
		}
	}

}
