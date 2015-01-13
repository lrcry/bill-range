package cn.range.tools.db.utils;

import java.text.DateFormat;
import java.util.Date;

public class DateUtils {
	/**
	 * Judge whether the date string is in the format given<br/>
	 * 
	 * @param dateStr
	 *            date string
	 * @param format
	 *            date format
	 * @return is the string in the format
	 */
	public static boolean isDateStringInFormat(String dateStr, DateFormat format) {
		Date date = null;
		if (format == null) {
			System.out.println("Give a date format first!");
			return false;
		}

		try {
			date = format.parse(dateStr);
		} catch (Exception e) {
			System.out.println("Input date string is not in the format given.");
			return false;
		}

		if (date == null) {
			System.out.println("U have input an empty date.");
			return false;
		} else {
			return true;
		}
	}
}
