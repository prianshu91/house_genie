package com.promelle.utils;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * This class is intended for providing application wide common functions
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public abstract class TextUtils {

	/**
	 * This function is responsible for performing like search.
	 * 
	 * @param text
	 * @param expr
	 * @return boolean
	 */
	public static boolean like(String text, String expr) {
		return text.toLowerCase().matches(
				expr.replace(".", "\\.").replace("_", ".").replace("%", ".*")
						.toLowerCase());
	}

	/**
	 * This function is responsible for adjusting the prefix & suffix spacing of
	 * string.
	 * 
	 * @param str
	 * @param len
	 * @return adjusted string
	 */
	public static String adjustSpacing(String str, int len) {
		return getSpaceString((len - str.length()) / 2)
				+ str
				+ getSpaceString(len
						- (((len - str.length()) / 2) + str.length()));
	}

	/**
	 * This function provides a string of chars of given length.
	 * 
	 * @param len
	 * @return string of chars
	 */
	public static String getRepeatingCharString(char ch, int len) {
		String str = "";
		for (int i = 0; i < len; i++) {
			str = str + ch;
		}
		return str;
	}

	/**
	 * Helper method to detect presence of given word in given string.
	 * 
	 * @param sentence
	 *            string to be tested for the presence of the word
	 * @param word
	 *            word to be searched
	 * @param punctuationChars
	 *            set of characters those should be used to identify boundaries
	 *            of words
	 * @return true if word is present, false otherwise
	 */
	public static boolean containsWord(String sentence, String word,
			Set<Character> punctuationChars) {
		if (StringUtils.isEmpty(word)) {
			return false;
		}
		String finalSentence = sentence.toLowerCase();
		String finalWord = word.toLowerCase();
		int idx = finalSentence.indexOf(word);
		while (idx != -1) {
			char prefixChar = idx == 0 ? ' ' : finalSentence.charAt(idx - 1);
			char suffixChar = idx == (finalSentence.length() - finalWord
					.length()) ? ' ' : finalSentence.charAt(idx
					+ finalWord.length());
			if (punctuationChars.contains(prefixChar)
					&& punctuationChars.contains(suffixChar)) {
				return true;
			}
			idx = finalSentence.indexOf(finalWord, idx + 1);
		}
		return false;
	}

	/**
	 * Helper method to detect presence of given word in given string.
	 * 
	 * @param sentence
	 *            string to be tested for the presence of the word
	 * @param word
	 *            word to be searched
	 * @param punctuationChars
	 *            list of characters those should be used to identify boundaries
	 *            of words
	 * @return true if word is present, false otherwise
	 */
	public static boolean containsWord(String sentence, String word,
			List<Character> punctuationChars) {
		return TextUtils.containsWord(sentence, word, new HashSet<Character>(
				punctuationChars));
	}

	public static String unAccent(String str) {
		return Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
				.matcher(Normalizer.normalize(str, Normalizer.Form.NFD))
				.replaceAll("");
	}

	/**
	 * This function provides a string of spaces of given length.
	 * 
	 * @param len
	 * @return string of spaces
	 */
	public static String getSpaceString(int len) {
		return getRepeatingCharString(' ', len);
	}

	/**
	 * This function is responsible for adjusting suffix spacing.
	 * 
	 * @param message
	 * @param key
	 * @return adjusted string
	 */
	public static String adjustSuffixSpacing(String message, String key) {
		int index = message.indexOf(key);
		String finalMsg = message;
		while (index != -1) {
			int pos = index + key.length();
			if (pos < finalMsg.length() && finalMsg.charAt(pos) != ' ') {
				finalMsg = finalMsg.substring(0, pos) + " "
						+ finalMsg.substring(pos);
			}
			index = finalMsg.indexOf(key, pos);
		}
		return finalMsg;
	}

}
