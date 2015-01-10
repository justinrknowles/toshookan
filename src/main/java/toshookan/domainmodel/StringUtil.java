package toshookan.domainmodel;

/**
 * Utility class which contains static methods that should be part of the String
 * class but are not due to the version of Java, etc...
 * <p>
 * The development community is encouraged to contribute functionality to this
 * utility. The functionality must not be available in the existing Java classes
 * or must be a significant extension or simplification of a feature available
 * in the Java classes.
 * <p>
 * Note that the replace, match, and split methods are supported by the String
 * object since 1.4 and format since 1.5 and are therefore similar functionality
 * has not been included in this class.
 * 
 * @version $Revision: 1.2 $
 * @author $Author: jknowles $
 */
public class StringUtil {

	/**
	 * A String containing typical "special" charaters.
	 */
	public static final String SPECIAL_CHARACTERS = 
		",&!#$%*()+:;<=>?[]`{|}^_\\\'";

	/**
	 * Returns the index within this string of the first occurrence of any of
	 * the characters specified in string any. If none of the characters occur
	 * in this string, then -1 is returned.
	 * 
	 * @param string
	 *            the string to search
	 * @param any
	 *            the characters to search for
	 * @return the first index of any of the characters in "any" in "string", or
	 *         -1 if none are found.
	 */
	public static int indexOfAny(String string, String any) {
		return indexOfAny(string, any, 0);
	}

	/**
	 * Returns the index within string of the first occurrence of any of the
	 * characters specified in any at or after the character at index start. If
	 * none of the characters occur in the substring, then -1 is returned.
	 * 
	 * @param string
	 *            the string to search
	 * @param any
	 *            the characters to search for
	 * @param start
	 *            the string index to start the search at
	 * @return the first index of any of the characters in "any" in a substring
	 *         of "string" from index start, or -1 if none are found.
	 */
	public static int indexOfAny(String string, String any, int start) {
		
		if((string == null) || (any == null)) {
			return -1;
		}
		try {
			int len = string.length();
			for (int i = start; i < len; i++) {
				if (any.indexOf(string.charAt(i)) >= 0) {
					return i;
				}
			}
			return -1;
		}
		catch (StringIndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * Returns an empty String if string is null. Otherwise string is returned.
	 * 
	 * @param string
	 *            the string to scrub
	 * @return string or empty string if s is null
	 */
	public static String scrubNull(String string) {
		return (string == null) ? "" : string;
	}

	/**
	 * Returns the aDefault value if string is null. Otherwise string is
	 * returned.
	 * 
	 * @param string
	 *            the string to scrub
	 * @param aDefault
	 *            the string to return if s is null
	 * @return s or aDefault if s is null
	 */
	public static String scrubNull(String string, String aDefault) {
		return (string == null) ? aDefault : string;
	}

	/**
	 * Determines is the passed string is not null and not empty and contains
	 * characters other than all whitespace.
	 * 
	 * @param string
	 *            The string to check.
	 * @return true is the string is set otherwise false.
	 */
	public static boolean isSet(String string) {
		return ((string != null) && (string.trim().length() > 0));
	}

	/**
	 * Splits a String into multiple sub-strings of equal length.
	 * 
	 * @param string
	 *            the string to split
	 * @param length
	 *            the length of the sub-strings
	 * @return an array of substrings.
	 */
	public static String[] split(String string, int length) {

		if ((string == null) || (length < 1)) {
			return new String[0];
		}
		int l = (int) Math.ceil((double) string.length() / (double) length);
		String[] result = new String[l];

		for (int i = 0; i < l; ++i) {
			try {
				result[i] = string.substring((i * length),
						((i * length) + length));
			}
			catch (StringIndexOutOfBoundsException e) {
				result[i] = string.substring(i * length);
			}
		}

		return result;
	}

	/**
	 * Joins the strings contained in strings using the delimiter delim. There
	 * is no leading or trailing delimiter. If strings is null or empty an empty
	 * string is returned.
	 * 
	 * @param strings
	 *            the strings to join together
	 * @param delim
	 *            the string to use as a separator between joined strings.
	 * @return the joined string
	 */
	public static String join(String[] strings, String delim) {

		StringBuffer sb = new StringBuffer();

		if ((strings == null) || (strings.length == 0)) {
			return "";
		}

		String d = (delim == null) ? "" : delim;

		sb.append(strings[0]);
		for (int i = 1; i < strings.length; ++i) {
			sb.append(d);
			sb.append(strings[i]);
		}

		return sb.toString();
	}

	/**
	 * Pads a string "s" with characters "c" so that it is "length" characters
	 * long. If length is positive the front of the string is padded. If length
	 * is negative the end of the string is padded. If the string is already the
	 * specified length the string is returned.
	 * <p>
	 * String.format() can be used when padding strings with spaces ( ) or
	 * numbers with zeros (0).
	 * 
	 * @param string
	 *            the string to pad.
	 * @param aChar
	 *            the character to use as padding.
	 * @param length
	 *            The final length of the new string after padding.
	 * @return the padded string.
	 */
	public static String pad(String string, char aChar, int length) {
		StringBuffer sb = new StringBuffer();

		if (string.length() == length) {
			return string;
		}

		for (int i = 0; (i < (Math.abs(length) - string.length())); ++i) {
			sb.append(aChar);
		}

		if (length >= 0) {
			sb.append(string);
		}
		else {
			sb.insert(0, string);
		}

		return sb.toString();
	}

	/**
	 * Truncates the String by removing characters from the right side of the
	 * String so it is length characters long. If the String is already length
	 * characters long or less then the string is returned. If a negative length
	 * is specified the characters are removed from the left side of the string.
	 * 
	 * @param string
	 *            the string to truncate
	 * @param length
	 *            The final length of the new string after trucating
	 * @return the truncated string
	 */
	public static String trunc(String string, int length) {

		int absLength = Math.abs(length);

		if ((string == null) || (string.length() <= absLength)) {
			return string;
		}

		if (length < 0) {
			int sLength = string.length();
			return string.substring(sLength - absLength, sLength);
		}

		return string.substring(0, length);
	}

	/**
	 * Removes all extra whitespace characters from the passed String. All
	 * occurrences of consecutive white space characters as defined by
	 * Character#isWhitespace(char) are reduced down to one whitespace character
	 * (the first character in the sequence). A new modified String is returned.
	 * The string is not trimed.
	 * 
	 * @param string
	 *            the String to modify
	 * @return a new String without repeated whitespace characters.
	 */
	public static String squeeze(String string) {

		if(string == null) {
			return null;
		}
		
		char[] oldString = string.toCharArray();
		int oldLength = oldString.length;
		char[] newString = new char[oldLength];
		int newLength = 0;
		char currentChar;
		boolean consecutive = false;

		for (int i = 0; i < oldLength; ++i) {
			currentChar = oldString[i];
			if (Character.isWhitespace(currentChar)) {
				if (!consecutive) {
					consecutive = true;					
					newString[newLength] = currentChar;
					newLength++;
				}
			}
			else {
				consecutive = false;				
				newString[newLength] = currentChar;
				newLength++;
			}
		}

		return new String(newString, 0, newLength);
	}
}