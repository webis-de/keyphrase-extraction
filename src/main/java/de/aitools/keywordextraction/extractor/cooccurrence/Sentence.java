/*
 * @(#)Sentence.java
 * 
 *  Description: 
 *  
 *  Created:     18.03.2006
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
package de.aitools.keywordextraction.extractor.cooccurrence;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import de.aitools.lexer.Delimiter;
import de.aitools.stemming.SnowballStemmer;
import de.aitools.stemming.Stemmer;
import de.aitools.stopwords.StopWordList;

/**
 * @author Karsten
 * 
 */
public class Sentence {

	// TODO: Use another API-Collection!
	private List<String> elements;

	private List<String> stemmedElements;

	private List<String> nonStopwordElements;

	private Stemmer stemmer;

	private Locale language;

	public Sentence(String sentence, Locale language) {
		this.language = language;
		this.stemmer = new SnowballStemmer(language);
		this.elements = this.convertToElementVector(sentence);
		this.stemmedElements = this.computeStemmedElements(this.elements);
		this.nonStopwordElements = this
				.computeNonStopwordElements(this.elements);
	}

	/**
	 * @return Returns the elements.
	 */
	public List<String> getElements() {
		return elements;
	}

	/**
	 * @return Returns the stemmedElements.
	 */
	public List<String> getStemmedElements() {
		return stemmedElements;
	}

	/**
	 * @return Returns the nonStopwordElements.
	 */
	public List<String> getNonStopwordElements() {
		return nonStopwordElements;
	}

	/**
	 * 
	 */
	public String toString() {
		StringBuffer sBuff = new StringBuffer();
		for (String element : this.elements) {
			sBuff.append(element + " ");
		}
		return sBuff.toString().trim();
	}

	/**
	 * 
	 * @param sentence
	 * @return
	 */
	private List<String> convertToElementVector(String sentence) {
		List<String> elements = new ArrayList<String>();
		StringTokenizer wordTokenizer = new StringTokenizer(sentence,
				Delimiter.ALL);
		while (wordTokenizer.hasMoreTokens()) {
			String element = wordTokenizer.nextToken();
			if(element.length() > 1) {
				elements.add(element);
			}
		}	
		return elements;
	}

	/**
	 * 
	 * @param elements
	 * @return
	 */
	private List<String> computeStemmedElements(List<String> elements) {
		List<String> processedElements = new ArrayList<String>();
		for (String element : elements) {
			processedElements.add(stemmer.stem(element));
		}
		return processedElements;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private List<String> computeNonStopwordElements(List<String> elements) {
		List<String> processedElements = new ArrayList<String>();
		for (String element : elements) {
			if (!StopWordList.contains(element, this.language)) {
				processedElements.add(element);
			} else {
				processedElements.add("");
			}
		}
		return processedElements;
	}

}
