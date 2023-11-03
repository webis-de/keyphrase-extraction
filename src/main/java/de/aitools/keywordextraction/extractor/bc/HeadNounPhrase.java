/*
 * @(#)HeadNounPhrase.java
 * 
 *  Description: 
 *  
 *  Created:     07.03.2006
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
package de.aitools.keywordextraction.extractor.bc;

import java.util.List;

/**
 * @author Karsten Klueger, BUW
 *
 */
public class HeadNounPhrase {

	private int frequency;

	private int firstOccurrence;

	private double scoreTF;

	private double scoreFO;

	private List<String> words;

	private String headnoun;

	/**
	 * 
	 * @param frequency
	 * @param words
	 */
	public HeadNounPhrase(String headnoun, int frequency, int firstOccurrence,
			List<String> words) {
		this.headnoun = headnoun;
		this.frequency = frequency;
		this.firstOccurrence = firstOccurrence;
		this.words = words;
		this.calculateScore();
	}

	/**
	 * @return Returns the scoreFO.
	 */
	public double getScoreFO() {
		return scoreFO;
	}

	/**
	 * @return Returns the scoreTF.
	 */
	public double getScoreTF() {
		return scoreTF;
	}

	/**
	 * @return Returns the headnoun.
	 */
	public String getHeadnoun() {
		return headnoun;
	}

	/**
	 * 
	 *
	 */
	private void calculateScore() {
		this.scoreFO = this.firstOccurrence / this.words.size();
		this.scoreTF = this.frequency * this.words.size();
	}

	/**
	 * 
	 * @return
	 */
	public String getPhrase() {
		StringBuffer phrase = new StringBuffer();
		for (String w : this.words) {
			phrase.append(w + " ");
		}
		return phrase.toString().trim();
	}

}
