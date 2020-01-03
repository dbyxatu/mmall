package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by geely
 */
public class DateTimeUtil {

	// joda-time

	// str->Date
	// Date->str
	public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 按照自定义格式转换
	 * 
	 * @param dateTimeStr
	 * @param formatStr
	 * @return
	 */
	public static Date strToDate(String dateTimeStr, String formatStr) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}

	/**
	 * 按照自定义格式转换
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String dateToStr(Date date, String formatStr) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(formatStr);
	}

	/**
	 * 按照标准格式进行转换
	 * 
	 * @param dateTimeStr
	 * @return
	 */
	public static Date strToDate(String dateTimeStr) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}

	/**
	 * 按照标准格式进行转换
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(STANDARD_FORMAT);
	}

	/**
	 * 测试主方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(DateTimeUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateTimeUtil.strToDate("2010-01-01 11:11:11", "yyyy-MM-dd HH:mm:ss"));

	}

}
