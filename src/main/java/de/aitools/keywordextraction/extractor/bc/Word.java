/*
 * @(#)Word.java
 * 
 *  Description: 
 *  
 *  Created:     10.03.2006
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

/**
 * @author Karsten
 *
 */
public class Word {

	private String word;

	private int frequency;

	private int fisrtOccurrence;

	/**
	 * 
	 */
	public Word(String noun) {
		this.word = noun;
	}

	/**
	 * 
	 */
	public Word(String noun, int frequency, int firstOccurrence) {
		this.word = noun;
		this.frequency = frequency;
		this.fisrtOccurrence = firstOccurrence;
	}

	/**
	 * @return Returns the fisrtOccurrence.
	 */
	public int getFisrtOccurrence() {
		return fisrtOccurrence;
	}

	/**
	 * @param fisrtOccurrence The fisrtOccurrence to set.
	 */
	public void setFisrtOccurrence(int fisrtOccurrence) {
		this.fisrtOccurrence = fisrtOccurrence;
	}

	/**
	 * @return Returns the frequency.
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency The frequency to set.
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return Returns the word.
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word The word to set.
	 */
	public void setWord(String word) {
		this.word = word;
	}

}
