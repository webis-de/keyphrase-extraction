/*
 * @(#)Extractor.java
 * 
 *  Description: 
 *  
 *  Created:     15.03.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universit�t Weimar
 *  Bauhausstra�e 11
 *  99423 Weimar � Germany
 *  
 */
/**
 * 
 */
package de.aitools.keywordextraction.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import de.aitools.stemming.SnowballStemmer;
import de.aitools.stemming.Stemmer;
import de.aitools.stopwords.StopWordList;

/**
 * @author Karsten Klueger, BUW
 * 
 */
public abstract class KeywordExtractor {

	protected static final Locale DEFAULT_LANGUAGE = Locale.ENGLISH;

	protected Locale language;

	/**
	 * Constructor
	 * 
	 */
	protected KeywordExtractor() {
		this.language = DEFAULT_LANGUAGE;
	}

	/**
	 * Use this method to get single keywords.
	 * 
	 * @param text
	 * @param numberOfKeywords
	 * @return
	 */
	public List<String> getSingleWords(String text, int numberOfKeywords) {
		// convert text to lower case
		text = text.toLowerCase();

		return this.getSingleWordsForOutput(this
				.extract(text, numberOfKeywords), numberOfKeywords);
	}

	/**
	 * Use this method to get phrases.
	 * 
	 * @param text
	 * @param numberOfKeywords
	 * @return
	 */
	public List<String> getPhrases(String text, int numberOfKeywords) {
		// convert text to lower case
		text = text.toLowerCase();
		return this.extract(text, numberOfKeywords);
	}

	/**
	 * This method must be implemented by the derived class!!!
	 * 
	 * @param text
	 * @param numberOfKeywords
	 * @return
	 */
	protected abstract List<String> extract(String text, int numberOfKeywords);

	/**
	 * Returns the name of the extractor.
	 * 
	 * @return
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * Set the language. Default language is English.
	 * 
	 * @param language
	 */
	public void setLanguage(Locale language) {
		this.language = language;
	}

	/**
	 * Create single words from input.
	 * 
	 * @param keywords
	 * @param numberOfKeywords
	 * @return
	 */
	private List<String> getSingleWordsForOutput(List<String> keywords,
			int numberOfKeywords) {
		List<String> singleWords = new ArrayList<String>();
		List<String> stemms = new ArrayList<String>();
		Stemmer stemmer = new SnowballStemmer(this.language);
		StringTokenizer st;

		for (String keyword : keywords) {
			st = new StringTokenizer(keyword, " ");
			while (st.hasMoreTokens()) {
				String w = st.nextToken();
				String stem = stemmer.stem(w);
				if (!stemms.contains(stem)
						&& !StopWordList.contains(w, this.language)) {
					stemms.add(stem);
					singleWords.add(w);
				}
			}
		}

		singleWords.subList(Math.min(numberOfKeywords, singleWords.size()), singleWords.size()).clear();

		return singleWords;
	}

}
