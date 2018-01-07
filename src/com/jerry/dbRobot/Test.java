
package com.jerry.dbRobot;

/**
 *
 *2016年7月26日下午2:39:31
 *
 * @author liuqs
 * 
 */
public class Test {
	public static void main(String[] args) {
		String a="t_user_name_from";
		String[] s = a.split("_");
		StringBuffer fs=new StringBuffer();
		for(int i=0;i<s.length;i++){
			fs.append(captureName(s[i]));
		}
		System.out.println(fs.toString());
	}
	 public static String captureName(String name) {
		        char[] cs=name.toCharArray();
		        cs[0]-=32;
		        return String.valueOf(cs);
		    }
}
