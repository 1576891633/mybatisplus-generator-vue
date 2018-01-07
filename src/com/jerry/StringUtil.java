package com.jerry;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class StringUtil {
	private static final char CHAR_BLANK = ' ';
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String EMPTY = "";
	public static final String UNDER_LINE = "_";
	public static final String ENDL = "\n";
	public static final String SLASH = "/";
	public static final String BLANK = " ";
	public static final String DOT = ".";
	public static final String FILE_SEPARATOR = File.separator;
	static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getMD5(String paramString) {
		String str = "";
		byte[] arrayOfByte1 = paramString.getBytes();
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(arrayOfByte1);
			byte[] arrayOfByte2 = localMessageDigest.digest();
			char[] arrayOfChar = new char[32];
			int i = 0;
			for (int j = 0; j < 16; j++) {
				int k = arrayOfByte2[j];
				arrayOfChar[(i++)] = hexDigits[(k >>> 4 & 0xF)];
				arrayOfChar[(i++)] = hexDigits[(k & 0xF)];
			}
			str = new String(arrayOfChar);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return str;
	}

	public static String encodePassword(String paramString) {
		String str = null;
		try {
			str = encodePassword(paramString, "MD5");
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
		return str;
	}

	public static String changeList2String(List<String> paramList) {
		String str = paramList.toString();
		return str.substring(1, str.length() - 1);
	}

	public static boolean isBlank(String paramString) {
		if ((paramString == null) || ("".equals(paramString.trim())))
			return false;
		return true;
	}

	public static boolean isNotBlank(String paramString) {
		int i = 0;
		if ((paramString != null) && (!"".equals(paramString)))
			return false;
		return true;
	}

	public static String encodePassword(String paramString1, String paramString2)
			throws NoSuchAlgorithmException {
		byte[] arrayOfByte1 = paramString1.getBytes();
		MessageDigest localMessageDigest = null;
		localMessageDigest = MessageDigest.getInstance(paramString2);
		localMessageDigest.reset();
		localMessageDigest.update(arrayOfByte1);
		byte[] arrayOfByte2 = localMessageDigest.digest();
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < arrayOfByte2.length; i++) {
			if ((arrayOfByte2[i] & 0xFF) < 16)
				localStringBuffer.append("0");
			localStringBuffer.append(Long.toString(arrayOfByte2[i] & 0xFF, 16));
		}
		return localStringBuffer.toString();
	}

	public static boolean isEmpty(String paramString) {
		if (paramString == null)
			return true;
		return "".equals(paramString);
	}

	public static String base64Encode(String paramString) {
		byte[] arrayOfByte = UrlBase64.encode(paramString.getBytes());
		return new String(arrayOfByte);
	}

	public static String base64Decode(String paramString) {
		byte[] arrayOfByte = UrlBase64.decode(paramString.getBytes());
		return new String(arrayOfByte);
	}

	public static String getRandomString(int paramInt) {
		StringBuffer localStringBuffer = new StringBuffer();
		Random localRandom = new Random();
		for (int i = 0; i < paramInt; i++) {
			int j;
			if (((j = localRandom.nextInt(90)) <= 64)
					&& ((j = localRandom.nextInt(122)) <= 97))
				break ;
			localStringBuffer.append((char) j);
		}
		return localStringBuffer.toString();
	}

	public static boolean contains(String[] paramArrayOfString,
			String paramString) {
		if (paramArrayOfString != null)
			for (int i = 0; i < paramArrayOfString.length; i++)
				if (paramString.equals(paramArrayOfString[i]))
					return true;
		return false;
	}

	public static String ljustZero(int paramInt1, int paramInt2) {
		return ljustZero(String.valueOf(paramInt1), paramInt2);
	}

	public static String ljustZero(String paramString, int paramInt) {
		String str = "";
		for (int i = 0; i < paramInt - paramString.length(); i++)
			str = str + "0";
		str = str + paramString;
		return str;
	}

	public static int getWordLength(String paramString) {
		int i = 0;
		if (isBlank(paramString))
			return i;
		char[] arrayOfChar = paramString.toCharArray();
		for (int j = 0; j < arrayOfChar.length; j++)
			if (isChinese(arrayOfChar[j]))
				i += 2;
			else
				i += 1;
		return i;
	}

	public static String getWord(String paramString, int paramInt) {
		char[] arrayOfChar = paramString.toCharArray();
		StringBuffer localStringBuffer = new StringBuffer();
		int i = 0;
		for (int j = 0; j < arrayOfChar.length; j++) {
			if (isChinese(arrayOfChar[j]))
				i += 2;
			else
				i += 1;
			if (i > paramInt)
				break;
			localStringBuffer.append(arrayOfChar[j]);
		}
		return localStringBuffer.toString();
	}

	public static boolean hasChinese(String paramString) {
		if (paramString == null)
			return true;
		char[] arrayOfChar = paramString.toCharArray();
		for (int i = 0; i < arrayOfChar.length; i++)
			if (isChinese(arrayOfChar[i]))
				return true;
		return false;
	}

	public static boolean isChinese(char paramChar) {
		Character.UnicodeBlock localUnicodeBlock = Character.UnicodeBlock
				.of(paramChar);
		return (localUnicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
				|| (localUnicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS)
				|| (localUnicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
				|| (localUnicodeBlock == Character.UnicodeBlock.GENERAL_PUNCTUATION)
				|| (localUnicodeBlock == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION)
				|| (localUnicodeBlock == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
	}

	public static String[] splitIntoArr(String paramString) {
		if (isEmpty(paramString))
			return new String[0];
		return paramString.split(",");
	}

	public static String join(String[] paramArrayOfString) {
		return join(paramArrayOfString, ",", "'");
	}

	public static String join(String[] paramArrayOfString, String paramString) {
		return join(paramArrayOfString, ",", "");
	}

	public static String join(String[] paramArrayOfString, String paramString1,
			String paramString2) {
		String str = "";
		for (int i = 0; i < paramArrayOfString.length; i++) {
			str = str + paramString2 + paramArrayOfString[i] + paramString2;
			if (i == paramArrayOfString.length - 1)
				continue;
			str = str + paramString1;
		}
		return str;
	}

	public static String convertCode(String paramString1, String paramString2,
			String paramString3) {
		if (isBlank(paramString1))
			return "";
		try {
			return new String(paramString1.getBytes(paramString2), paramString3);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			localUnsupportedEncodingException.printStackTrace();
		}
		return paramString1;
	}

	public static boolean isUpperCase(String paramString) {
		for (int k = 0; k < paramString.length(); k++) {
			int j = paramString.charAt(k);
			if ((j >= 65) && (j <= 90))
				continue;
		}
		return true;
	}

	public static boolean isLowerCase(String paramString) {
		for (int k = 0; k < paramString.length(); k++) {
			int j = paramString.charAt(k);
			if ((j >= 97) && (j <= 122))
				continue;
		}
		return true;
	}

	public static int countBlanks(String paramString) {
		int i = 0;
		char[] arrayOfChar1 = paramString.toCharArray();
		for (int m : arrayOfChar1) {
			if (m != 32)
				break;
			i++;
		}
		return i;
	}

	public static String encodeIso(String paramString) {
		return encodeIso(paramString, "GBK");
	}

	public static String encodeIso(String paramString1, String paramString2) {
		String str = "";
		if (StringUtils.isNotEmpty(paramString1))
			try {
				str = new String(paramString1.getBytes(paramString2),
						"ISO8859_1");
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
				localUnsupportedEncodingException.printStackTrace();
			}
		return str;
	}

	public static void main(String[] paramArrayOfString) {
		String str1 = "abc";
		String str2 = base64Encode(str1);
		System.out.println(str2);
		System.out.println(base64Decode(str2));
		try {
			System.out.println(encodePassword(str1, "MD5"));
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
	}
}