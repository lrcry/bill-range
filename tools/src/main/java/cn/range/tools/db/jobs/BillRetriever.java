package cn.range.tools.db.jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.range.tools.db.common.Msg;
import cn.range.tools.db.model.Bill;
import cn.range.tools.db.utils.DateUtils;

public class BillRetriever {
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static final String GET_LAST_SHARING_ID = "select max(id) from foodgrocerysharing where details='sharing clear'";
	private static final String GET_ALL_SHARING_SUM = "select sum(expense) from foodgrocerysharing";
	private static final String GET_ALL_SHARING_SUM_FROM_LAST = "select sum(expense) from foodgrocerysharing where id>?";

	private static final String GET_ALL_BILLS = "select expense, details, recdate from expenserecords order by recdate desc";
	private static final String GET_BILLS_FROM_START_TO_END = "select expense, details, recdate from expenserecords where recdate between ? and ? order by recdate desc";

	/**
	 * Get the last sharing sums<br/>
	 * 
	 * @param conn
	 *            connection to db
	 * @return last sharing sums
	 * @throws SQLException
	 */
	public Double getLastSharingSummary(Connection conn) throws SQLException {
		double lastSharingSum = 0;
		if (conn == null) {
			System.out.println(Msg.CONNECTION_NULL);
			return null;
		}

		PreparedStatement pStmt = conn.prepareStatement(GET_LAST_SHARING_ID);
		ResultSet rs = pStmt.executeQuery();//
		if (rs == null) {
			System.out.println("rs is nothing");
			return null;
		}

		if (rs.next()) { // max id of sharing clear
			int lastSharingClearId = rs.getInt(1);
			if (lastSharingClearId == 0) { // no sum
				PreparedStatement pStmtAllSharingSum = conn
						.prepareStatement(GET_ALL_SHARING_SUM);
				ResultSet rsAllSharingSum = pStmtAllSharingSum.executeQuery();
				if (rsAllSharingSum.next()) {
					lastSharingSum = rsAllSharingSum.getDouble(1);
				} else {
					System.out.println("rsAllSharingSum is nothing");
					return null;
				}
			} else { // use the last sharing clear id
				PreparedStatement pStmtSumFromLast = conn
						.prepareStatement(GET_ALL_SHARING_SUM_FROM_LAST);
				pStmtSumFromLast.setInt(1, lastSharingClearId);
				ResultSet rsSumFromLast = pStmtSumFromLast.executeQuery();
				if (rsSumFromLast.next()) {
					lastSharingSum = rsSumFromLast.getDouble(1);
				} else {
					System.out.println("rsSumFromLast is nothing");
					return null;
				}
			}
		}

		return lastSharingSum;
	}

	/**
	 * Get bills from a start date to an end date<br/>
	 * 
	 * @param conn
	 *            db connection
	 * @param start
	 *            start date
	 * @param end
	 *            end date
	 * @return list of bills
	 * @throws SQLException
	 */
	public List<Bill> getBillsFromStartToEnd(Connection conn, String start,
			String end) throws SQLException {
		if (conn == null || conn.isClosed()) {
			System.out.println(Msg.LOSE_CONN);
			return null;
		}

		if (!DateUtils.isDateStringInFormat(start, format)
				|| !DateUtils.isDateStringInFormat(end, format)) {
			System.out.println(Msg.INVALID_DATE);
			return null;
		}

		List<Bill> bills = new ArrayList<Bill>();
		PreparedStatement pStmt = conn
				.prepareStatement(GET_BILLS_FROM_START_TO_END);
		pStmt.setString(1, start);
		pStmt.setString(2, end);
		ResultSet rs = pStmt.executeQuery();
		bills = convertResultSetToBillList(rs);

		return bills;
	}

	public void clearSharing(double sharing) {
		/*
		 * 1. input sharing 2. Bill it with the sharing, 'sharing clear' and the
		 * date of today 3. insert
		 */

	}

	/**
	 * Get all the bills from db<br/>
	 * 
	 * @param conn
	 *            connection
	 * @return bill list
	 * @throws SQLException
	 */
	public List<Bill> getAllBills(Connection conn) throws SQLException {
		if (conn == null) {
			System.out.println("conn is null when getting all the bills");
			return null;
		}

		List<Bill> bills = new ArrayList<Bill>();
		PreparedStatement pStmt = conn.prepareStatement(GET_ALL_BILLS);
		ResultSet rs = pStmt.executeQuery();
		bills = convertResultSetToBillList(rs);
		return bills;
	}

	/**
	 * Convert the result set to a list of bills<br/>
	 * 
	 * @param rs
	 *            result set
	 * @return bill list
	 * @throws SQLException
	 */
	private List<Bill> convertResultSetToBillList(ResultSet rs)
			throws SQLException {
		List<Bill> bills = new ArrayList<Bill>();
		if (rs == null) { // rs is nothing
			System.out.println("rs is nothing");
			return null;
		}

		while (rs.next()) {
			double expense = rs.getDouble(1);
			String details = rs.getString(2);
			String dateStr = rs.getString(3);
			Bill bill = new Bill();
			bill.setExpense(expense);
			bill.setDetails(details);
			bill.setDate(dateStr);
			bills.add(bill);
		}

		return bills;
	}
}
