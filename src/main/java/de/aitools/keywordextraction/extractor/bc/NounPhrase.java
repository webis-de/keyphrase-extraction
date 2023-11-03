/*
 * @(#)NounPhrase.java
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
public class NounPhrase {

	private int frequency;

	private int firstOccurrence;

	private List<String> words;

	/**
	 * 
	 * @param words
	 * @param frequency
	 */
	public NounPhrase(List<String> words, int frequency,
			int firstOccurrence) {
		this.words = words;
		this.frequency = frequency;
		this.firstOccurrence = firstOccurrence;
	}

	/**
	 * 
	 * @param words
	 */
	public NounPhrase(List<String> words) {
		this.words = words;
		this.frequency = 1;
	}

	/**
	 * @return Returns the frequency.
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * @return Returns the words.
	 */
	public List<String> getWords() {
		return words;
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

	/**
	 * @return Returns the firstOccurence.
	 */
	public int getFirstOccurrence() {
		return firstOccurrence;
	}

	/**
	 * @param firstOccurrence The firstOccurrence to set.
	 */
	public void setFirstOccurrence(int firstOccurrence) {
		this.firstOccurrence = firstOccurrence;
	}

	/**
	 * @param frequency The frequency to set.
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
