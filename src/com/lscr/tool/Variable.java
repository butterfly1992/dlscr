package com.lscr.tool;

import java.text.SimpleDateFormat;

/**
 * 常量
 * @author Administrator
 *
 */
public class Variable {

	public static SimpleDateFormat formats = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
	public static String errorJson = "{\"flag\":0}";
	public static String correntJson = "{\"flag\":1}";
	public static String updateJson = "{\"flag\":2}";
	public static String testId = "test";
	//863166011518029--王翠，356512057050126--霍金龙，355056050219792--松姐，864133029488958--丁尚亮,358071043359917-程革
	public static String invalidImei="356512057050126,358071043359917";
	public static String validJson="{\"flag\":-1}";//没有机会时，返回标识
}
