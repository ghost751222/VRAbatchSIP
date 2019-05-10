package com.macaron.vra.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

	private static final DateTimeFormatter FULL_AD_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
	private static final DateTimeFormatter AD_YMD_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public static String getNowFullAd(){
		return LocalDateTime.now().format(FULL_AD_FMT);
	}
	public static String getNowAdYMD(){
		return LocalDateTime.now().format(AD_YMD_FMT);
	}
	
	public static LocalDateTime toLdt(Date date){
		return  LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	public static Date toDate(LocalDateTime ldt){
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
}
