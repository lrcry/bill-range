package cn.range.tools.db.model;

public class Bill {
	private double expense;
	
	private String details;
	
	private String date;

	public double getExpense() {
		return expense;
	}

	public void setExpense(double expense) {
		this.expense = expense;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public void printBill() {
		if (this.details == null) 
			this.details = "";
		if (this.date == null)
			this.date = "";
		
		System.out.println("========== Bill starts ==========");
		System.out.println("Expense: " + this.expense);
		System.out.println("Details: " + this.details);
		System.out.println("RecDate: " + this.date);
		System.out.println("========== Bill ends ==========");
		
	}
}
