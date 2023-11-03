/*
 * @(#)RSnGram.java
 * 
 * Description: RSnGram
 * 
 *  
 * Created:	    2006-01-31
 * Author:      Karsten Klueger
 * mailto:      karsten.klueger@medien.uni-weimar.de
 *
 * Copyright Bauhaus-Universit�t Weimar
 * Bauhausstra�e 11
 * 99423 Weimar � Germany
 *  
 */
package de.aitools.keywordextraction.extractor.rsp;

import java.util.ArrayList;

/**
 * @author Karsten
 *
 */
public class RSnGram {
	
	private int absoluteFrequency;
	
	private double frequency;

	private double firstOccurrence;
	
	private ArrayList<String> words;
	
	private boolean mergedWithElementBefore;
	
	/**
	 * create a new empty RSnGram
	 *
	 */
	public RSnGram() {
		this.words = new ArrayList<String>();
		this.absoluteFrequency = 0;
		this.frequency = 0.0;
		this.mergedWithElementBefore = false;
	}
	
	/**
	 * 
	 *
	 */
	public void setMergedWithElementBefore() {
		this.mergedWithElementBefore = true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMergedWithElementBefore() {
		return this.mergedWithElementBefore;
	}
	
	/**
	 * add a new word
	 * 
	 * @param word
	 */
	public void addWord(String word) {
		this.words.add(word);
	}
	
	/**
	 * get all words as one single string (separated by whitespace)
	 * 
	 * @return
	 */
	public String toString() {
		String allWords = "";
		for (String word : this.words)  {
			allWords += " " + word;
		}
		return allWords.trim();
	}
	
	/**
	 * get a copy of all words as ArrayList
	 * @return
	 */
	public ArrayList<String> getWordList() {
		return this.words;
	}
	
	/**
	 * get the current number of stored words
	 * 
	 * @return
	 */
	public int getNumberOfWords() {
		return this.words.size();
	}
	
	/**
	 * retruns the occuring absoluteFrequency of this nGram
	 * 
	 * @return
	 */
	public int getAbsoluteFrequency() {
		return this.absoluteFrequency;
	}
	
	/**
	 * set the occurring absoluteFrequency
	 * 
	 * @param absoluteFrequency
	 */
	public void setAbsoluteFrequency(int frequency) {
		this.absoluteFrequency = frequency;
	}
	
	/**
	 * returns the absolute position of the first occurrence of this RSnGram in
	 * the text
	 * 
	 * @return
	 */
	public double getFirstOccurrence() {
		return this.firstOccurrence;
	}

	/**
	 * set the absolute position of the first occurrence of this RSnGram in the
	 * text
	 * 
	 * @param firstOccurrence
	 */
	public void setFirstOccurrence(double firstOccurrence) {
		this.firstOccurrence = firstOccurrence;
	}

	/**
	 * get the relative frequency
	 * 
	 * @return
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * set relative frequency
	 * 
	 * @param frequency
	 */
	public void setFrequency(double relFrequency) {
		this.frequency = relFrequency;
	}
	
	
	
}