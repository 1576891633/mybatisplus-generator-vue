package com.jerry;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final int SECOND = 1000;
	public static final int MINUTE = 60000;
	public static final int HOUR = 3600000;
	public static final int DAY = 86400000;
	public static final int DAYOFMIDMONTH = 15;

	public static Date getNow() {
		Calendar localCalendar = Calendar.getInstance();
		return localCalendar.getTime();
	}

	public static Date getDate(int paramInt1, int paramInt2, int paramInt3) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.set(1, paramInt1);
		localCalendar.set(2, paramInt2 - 1);
		localCalendar.set(5, paramInt3);
		localCalendar.set(11, 0);
		localCalendar.set(12, 0);
		localCalendar.set(13, 0);
		localCalendar.set(14, 0);
		return localCalendar.getTime();
	}

	public static Date getTodayZeroClock() {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(new Date());
		localCalendar.set(11, 0);
		localCalendar.set(12, 0);
		localCalendar.set(13, 0);
		localCalendar.set(14, 0);
		return localCalendar.getTime();
	}

	public static boolean isBefore(Date paramDate1, Date paramDate2) {
		return paramDate1.getTime() - paramDate2.getTime() < 0L;
	}

	public static boolean isBeforeToday(Date paramDate) {
		Date localDate = getTodayZeroClock();
		return isBefore(paramDate, localDate);
	}

	public static boolean isBeforeMidMonth(Date paramDate) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(paramDate);
		int i = localCalendar.get(5);
		return i <= 15;
	}

	public static Date beforeMonths(Date paramDate, int paramInt) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(paramDate);
		localCalendar.set(2, localCalendar.get(2) - paramInt);
		return localCalendar.getTime();
	}

	public static Date beforeDays(Date paramDate, int paramInt) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(paramDate);
		localCalendar.set(2, localCalendar.get(5) - paramInt);
		return localCalendar.getTime();
	}

	public static Date beforeDays(int paramInt) {
		return beforeDays(getNow(), paramInt);
	}

	public static Date beforeHours(int paramInt) {
		return beforeHours(getNow(), paramInt);
	}

	public static Date beforeHours(Date paramDate, int paramInt) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(paramDate);
		localCalendar.set(10, localCalendar.get(10) - paramInt);
		return localCalendar.getTime();
	}

	public static Date beforeMinutes(Date paramDate, int paramInt) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(paramDate);
		localCalendar.set(12, localCalendar.get(12) - paramInt);
		return localCalendar.getTime();
	}

	public static Date afterMinutes(Date paramDate, int paramInt) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(paramDate);
		localCalendar.set(12, localCalendar.get(12) + paramInt);
		return localCalendar.getTime();
	}

	public static long countMilliSecondsBetween(Date paramDate1, Date paramDate2) {
		if ((paramDate1 == null) || (paramDate2 == null))
			throw new IllegalArgumentException("����d1��d2������null����!");
		return Math.abs(paramDate1.getTime() - paramDate2.getTime());
	}

	public static long countSecondsBetween(Date paramDate1, Date paramDate2) {
		return countMilliSecondsBetween(paramDate1, paramDate2) / 1000L;
	}

	public static long countMinutesBetween(Date paramDate1, Date paramDate2) {
		return countMilliSecondsBetween(paramDate1, paramDate2) / 60000L;
	}

	public static long countHoursBetween(Date paramDate1, Date paramDate2) {
		return countMilliSecondsBetween(paramDate1, paramDate2) / 3600000L;
	}

	public static long countDaysBetween(Date paramDate1, Date paramDate2) {
		return countMilliSecondsBetween(paramDate1, paramDate2) / 86400000L;
	}

	public static Date getThisYearBeginning() {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(new Date());
		localCalendar.set(2, 0);
		localCalendar.set(5, 0);
		localCalendar.set(11, 0);
		localCalendar.set(12, 0);
		localCalendar.set(13, 0);
		localCalendar.set(14, 0);
		return localCalendar.getTime();
	}

	public static boolean isBeforeThisYear(Date paramDate) {
		Date localDate = getThisYearBeginning();
		return isBefore(paramDate, localDate);
	}

	public static void main(String[] paramArrayOfString) {
		System.out.println(getThisYearBeginning());
	}
}