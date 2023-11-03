/*
 * @(#)Snippet.java
 * 
 *  Description: 
 *  
 *  Created:     16.05.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universität Weimar
 *  Bauhausstraße 11
 *  99423 Weimar - Germany
 *  
 */
package de.aitools.keywordextraction.extractor.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 
 * @author Karsten Klueger, BUW
 * 
 */
public class Snippet {

	private String text;

	private int clusterIndex;

	private int index;

	private List<Term> terms;

	private int wordCount;

	/**
	 * 
	 * @param text
	 */
	public Snippet(String text, int index, int clusterIndex) {
		this.text = text;
		this.index = index;
		this.wordCount = 0;
		this.clusterIndex = clusterIndex;		
		this.getTerms(text);
	}

	/**
	 * 
	 */
	public String toString() {
		return this.text;
	}

	/**
	 * @return Returns the clusterIndex.
	 */
	public int getClusterIndex() {
		return clusterIndex;
	}

	/**
	 * @param clusterIndex
	 *            The clusterIndex to set.
	 */
	public void setClusterIndex(int clusterIndex) {
		this.clusterIndex = clusterIndex;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	private void getTerms(String text) {
		this.terms = new ArrayList<Term>();
		
		Map<String, Double> hash = new HashMap<String, Double>();

		StringTokenizer tokenizer = new StringTokenizer(text,
				Delimiter.Delimiters);
		while (tokenizer.hasMoreTokens()) {
			this.wordCount++;
			String word = tokenizer.nextToken();
			double freq = 1.0;
			if (hash.containsKey(word)) {
				freq += hash.get(word);
			}
			hash.put(word, freq);
		}

		Iterator<String> wordIter = hash.keySet().iterator();
		while (wordIter.hasNext()) {
			String word = wordIter.next().toString();
			double freq = hash.get(word) / (double) this.wordCount;
			Term term = new Term(word, freq);
			this.terms.add(term);
		}
	}

	/**
	 * @return Returns the terms.
	 */
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}

}
