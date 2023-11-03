/*
 * @(#)HeadNoun.java
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
package de.aitools.keywordextraction.extractor.bc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karsten Klueger, BUW
 *
 */
public class HeadNoun {

	private String nounStemm;

	private int frequency;
	
	private int firstOccurrence;
	
	private List<String> nouns;

	/**
	 * 
	 */
	public HeadNoun() {
		this.frequency = 0;
		this.nounStemm = "";
		this.nouns = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param nounStemm
	 * @param frequency
	 * @param nouns
	 */
	public HeadNoun(String nounStemm, int frequency, int firstOccurrence, List<String> nouns) {
		this.frequency = frequency;
		this.firstOccurrence = firstOccurrence;
		this.nounStemm = nounStemm;
		this.nouns = nouns;
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
	 * @return Returns the nounStemm.
	 */
	public String getNounStemm() {
		return nounStemm;
	}

	/**
	 * @param nounStemm The nounStemm to set.
	 */
	public void setNounStemm(String noun) {
		this.nounStemm = noun;
	}

	/**
	 * @return Returns the nouns.
	 */
	public List<String> getNouns() {
		return nouns;
	}

	/**
	 * @param nouns The nouns to set.
	 */
	public void setNouns(List<String> nouns) {
		this.nouns = nouns;
	}
	
	/**
	 * 
	 * @param noun
	 */
	public void addNoun(String noun) {
		if(!this.nouns.contains(noun)) {
			this.nouns.add(noun);
		}
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

}
