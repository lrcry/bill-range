package cn.range.tools.db.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.range.tools.db.common.Msg;
import cn.range.tools.db.common.Option;
import cn.range.tools.db.jobs.BillRecorder;
import cn.range.tools.db.model.Bill;

public class BillMgmtCmd {
	private static BillRecorder recorder = new BillRecorder();

	private static final String JDBC_NAME = "com.mysql.jdbc.Driver";
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/financial_db";
	private static final String JDBC_USER = "root";
	private static final String JDBC_PASSWD = "admin";

	public static void main(String[] args) {
		System.out.print(Msg.LOADING_DRIVER);
		try {
			Class.forName(JDBC_NAME);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			return;
		}

		System.out.println(Msg.OK_MSG);

		System.out.print(Msg.CONN_TO_DB);
		Connection conn = null;
		try {
			conn = DriverManager
					.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWD);
			System.out.println(Msg.OK_MSG);
			loadWelcomeMessages();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));

			// while (true)
			List<Bill> bills = new ArrayList<Bill>();
			// List<Bill> sharings = new ArrayList<Bill>();

			String option = reader.readLine();
			if (option.equals(Option.ADD_BILL)
					|| option.equals(Option.ADD_SHARING)) {
				double expense = 0;
				String details = new String("");
				// String date = new String("");

				System.out.print(Msg.INPUT_EXPENSE);
				String expenseString = reader.readLine();
				try {
					expense = Double.parseDouble(expenseString);
				} catch (Exception e) {
					System.out.println(Msg.EXPENSE_EMPTY_INVALID);
					return;
				}

				System.out.print(Msg.INPUT_DETAILS);
				details = reader.readLine();
				if (details == null || details.equals("")) {
					System.out.println(Msg.DETAILS_EMPTY);
					return;
				}

				System.out.print(Msg.INPUT_DATE);
				String dateString = reader.readLine();

				Bill bill = new Bill();
				bill.setExpense(expense);
				bill.setDetails(details);
				bill.setDate(dateString);
				bills.add(bill);

				recorder.insertBill(bills, conn);
				System.out.println(Msg.ADD_BILL_SUCCESSFUL);
				if (option.equals(Option.ADD_SHARING)) {
					recorder.insertSharing(bills, conn);
					System.out.println(Msg.ADD_SHARING_SUCCESSFUL);
				}
			} else if (option.equals(Option.EXIT)) {
				// System.out.println(Msg.EXIT);
				// return; // break if while loop outside
			} else { // invalid input
				System.out.println(Msg.INVALID_OPTION + " " + option);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				System.out.print(Msg.DISCONNECT_FROM_DB);
				if (conn == null || conn.isClosed()) {
					return;
				} else {

					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("... fuck u programmer");
			}
			System.out.println(Msg.OK_MSG);
		}

	}

	public static void loadWelcomeMessages() {
		System.out.println("Welcome to Bill Management ver 0.1.");
		System.out.println("Press 1 to add expense records;");
		System.out.println("press 2 to add sharing records;");
		System.out.println("press 3 to exit.");
		System.out.println("Your option: ");
	}
}
