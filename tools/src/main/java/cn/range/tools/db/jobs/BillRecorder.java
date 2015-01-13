package cn.range.tools.db.jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.range.tools.db.common.Msg;
import cn.range.tools.db.model.Bill;

/**
 * Bill Recorder<br/>
 * 
 * @author range
 *
 */
public class BillRecorder {
	private static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	// these SQL statements can be set in a .properties file
	private static final String INSERT_BILL = "insert into expenserecords (expense, details, recdate) values (?,?,?)";
	private static final String INSERT_SHARING = "insert into foodgrocerysharing (expense, details, recdate) values (?,?,?)";

	/**
	 * Insert a new bill to expense table<br/>
	 * 
	 * @param bills
	 *            bill list
	 * @param conn
	 *            connection
	 * @throws SQLException
	 */
	public void insertBill(List<Bill> bills, Connection conn)
			throws SQLException {
		insert(bills, conn, INSERT_BILL);
	}

	/**
	 * Insert a new sharing to sharing table<br/>
	 * 
	 * @param sharings
	 *            sharing list
	 * @param conn
	 *            connection
	 * @throws SQLException
	 */
	public void insertSharing(List<Bill> sharings, Connection conn)
			throws SQLException {
		insert(sharings, conn, INSERT_SHARING);
	}

	/**
	 * Insert a record into db by SQL<br/>
	 * 
	 * @param billList
	 *            record list
	 * @param conn
	 *            connection to db
	 * @param sql
	 *            sql statement
	 * @throws SQLException
	 */
	private void insert(List<Bill> billList, Connection conn, String sql)
			throws SQLException {
		PreparedStatement pStmt = conn.prepareStatement(sql);
		if (billList.size() == 0) {
			System.out.println(Msg.NO_BILLS_ADDED);
			return;
		}

		int countInsert = 0;

		for (Bill bill : billList) {
			if (!isBillNull(bill)) {
				boolean isPsSet = setPreparedStatememt(pStmt, bill);
				if (isPsSet) {
					pStmt.addBatch(); // add to the pStmt
					++countInsert;
				}
			}

			if (countInsert >= 500) { // 500 to batch
				pStmt.executeBatch();
			}
		}

		pStmt.executeBatch();
	}

	/**
	 * Set prepared statement<br/>
	 * 
	 * @param pStmt
	 *            preparedstatement object
	 * @param bill
	 *            bill object
	 * @return if the pStmt set successfully
	 * @throws SQLException
	 */
	private boolean setPreparedStatememt(PreparedStatement pStmt, Bill bill)
			throws SQLException {
		pStmt.setDouble(1, bill.getExpense());
		pStmt.setString(2, bill.getDetails());
		String date = getDateFromString(bill.getDate(), dateFormat);
		if (date == null) {
			return false;
		}
		System.out.println("Now date is " + date);
		pStmt.setString(3, date);
		return true;
	}

	/**
	 * Judge whether a bill is empty<br/>
	 * 
	 * @param bill
	 *            bill object
	 * @return is bill empty?
	 */
	private boolean isBillNull(Bill bill) {
		if (bill == null)
			return true;

		return (bill.getExpense() == 0) || (bill.getDetails() == null);
	}

	/**
	 * Get the date from input string<br/>
	 * 
	 * @param dateString
	 *            string of a date
	 * @return date in the string
	 */
	private String getDateFromString(String dateString, DateFormat format) {
//		System.out.println("dateString=[" + dateString + "] and it is empty? " + (dateString.equals("")));
		if (dateString == null || dateString.equals("")
				|| dateString.equals("''")) {
//			System.out.println("If it is empty, it should be a new Date today.");
			return format.format(new Date());
		}

		Date date = null;

		try {
			date = dateFormat.parse(dateString);
		} catch (Exception e) {
			System.out.println("Date problem");
		}

		return dateString;
	}
}
