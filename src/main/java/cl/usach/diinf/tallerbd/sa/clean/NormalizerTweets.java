package cl.usach.diinf.tallerbd.sa.clean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Normaliza patrones que se pueden encontrar en los tweets como hashtags, usuarios, urls, etc.
 * @author rvasquez
 *
 */
public class NormalizerTweets {

	private static String PATTERN_HASHTAG = "(?:\\s|\\A)[##]+([A-Za-z0-9-_]+)";
	private static String PATTERN_USERS = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";

	public NormalizerTweets() {
		super();
	}
	/**
	 * Reemplaza los patrones que se encuentren en un texto.
	 * @param strpattern patron a reemplazar
	 * @param text entrada de texto
	 * @param replace texto por el que será reemplazado
	 * @return
	 */
	private String normalizePattern(String strpattern, String text, String replace){
		Pattern pattern = Pattern.compile(strpattern);
		Matcher matcher = pattern.matcher(text);
		String result = "";
		while (matcher.find()) {
			result = matcher.group();
	        result = result.replace(" ", "");
	        result = result.replace("@", "");
	        text = text.replace(result,replace);

		}
		return text;
	}
	/**
	 * Recibe un tweet y retorna una versión normalizada
	 * @param tweet
	 * @return
	 */
	public String normalizeTweet(String tweet){
		
		String normalizedTweet = normalizePattern(PATTERN_HASHTAG, tweet, "<HASHTAG>");
		normalizedTweet = normalizePattern(PATTERN_USERS, normalizedTweet, "<USER>");
		
		return normalizedTweet;
			
	}
}
