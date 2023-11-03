/*
 * @(#)Term.java
 * 
 *  Description: 
 *  
 *  Created:     16.03.2006
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.aitools.keywordextraction.extractor.cooccurrence.comparator.TFComparator;
import de.aitools.stopwords.StopWordList;

/**
 * @author Karsten
 * 
 */
public class Term {

	private Locale language;
	
	private int frequency;
	
	private List<String> elements;

	private List<Term> rootTerms;

	private List<Sentence> rootSentences;	

	private Map<Term, Integer> cooccurrences;	
	
	private double chiSquareValue;

	/**
	 * 
	 * @param stemmedPhrase
	 */
	public Term(Locale language) {
		this.language = language;
		this.frequency = 1;
		this.elements = new ArrayList<String>();
		this.rootTerms = new ArrayList<Term>();		
		this.rootSentences = new ArrayList<Sentence>();
		this.cooccurrences = new HashMap<Term, Integer>();
		this.chiSquareValue = 0.0;
	}

	/**
	 * 
	 * @param term
	 */
	@SuppressWarnings("unchecked")
	public void addRootTerm(Term term) {
		List<String> elements = new ArrayList<String>();
		for (String element : term.getElements()) {
			if (!StopWordList.contains(element, this.language)) {
				elements.add(element);
			}			
		}
		term.setElements(elements);
		String addedTerm = term.toString();
		boolean isIncluded = false;
		for (Term rootTerm : this.rootTerms) {
			if (rootTerm.toString().equalsIgnoreCase(addedTerm)) {
				isIncluded = true;
				rootTerm.setFrequency(rootTerm.getFrequency()
						+ term.getFrequency());
			}
		}
		if (!isIncluded) {
			this.rootTerms.add(term);
			Collections.sort(this.rootTerms, new TFComparator());
		}
	}

	/**
	 * 
	 * @param sentence
	 */
	public void addRootSentence(Sentence sentence) {
		if(!this.rootSentences.contains(sentence)) {
			this.rootSentences.add(sentence);
		}
	}

	/**
	 * 
	 * @param element
	 */
	public void addElement(String element) {
		if (element != null) {
			this.elements.add(element);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Term getMostFrequentRootTerm() {
		return this.rootTerms.get(0);
	}
	
	/**
	 * 
	 * @return
	 */
	public Term getKeywordTerm() {
		Term longestTerm = this.rootTerms.get(0);		
		for(Term t : this.rootTerms) {
			if(t.getElements().size() > longestTerm.getElements().size()) {
				longestTerm = t;
			}
		}		
		return longestTerm;
	}

	/**
	 * @return Returns the frequency.
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency
	 *            The frequency to set.
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return Returns the rootTerms.
	 */
	public List<Term> getRootTerms() {
		return rootTerms;
	}

	/**
	 * @param rootTerms
	 *            The rootTerms to set.
	 */
	@SuppressWarnings("unchecked")
	public void setRootTerms(List<Term> rootTerms) {
		this.rootTerms = rootTerms;
		Collections.sort(this.rootTerms, new TFComparator());
	}

	/**
	 * @return Returns the elements.
	 */
	public List<String> getElements() {
		return elements;
	}

	/**
	 * @param elements
	 *            The elements to set.
	 */
	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	/**
	 * @return Returns the rootSentences.
	 */
	public List<Sentence> getRootSentences() {
		return rootSentences;
	}

	/**
	 * @param rootSentences
	 *            The rootSentences to set.
	 */
	public void setRootSentences(List<Sentence> rootSentences) {
		this.rootSentences = rootSentences;
	}	

	/**
	 * @return Returns the cooccurrences.
	 */
	public Map<Term, Integer> getCooccurrences() {
		return cooccurrences;
	}

	/**
	 * @param cooccurrences The cooccurrences to set.
	 */
	public void setCooccurrences(Map<Term, Integer> cooccurrences) {
		this.cooccurrences = cooccurrences;
	}

	/**
	 * 
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();
		;
		for (String word : elements) {
			str.append(word + " ");
		}
		return str.toString().trim();
	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public int compareTo(Object o) {
		if (this.frequency > ((Term) o).frequency) {
			return -1;
		} else if (this.frequency < ((Term) o).frequency) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @return nw Returns the total number of terms in the sentences inluding this term.
	 */
	public int getTotalNumberOfTerms() {
		int nw = 0;
		for(Sentence sentence : this.rootSentences) {
			nw += sentence.getElements().size();
		}
		return nw;
	}

	/**
	 * @return Returns the chiSquareValue.
	 */
	public double getChiSquareValue() {
		return chiSquareValue;
	}

	/**
	 * @param chiSquareValue The chiSquareValue to set.
	 */
	public void setChiSquareValue(double chiSquareValue) {
		this.chiSquareValue = chiSquareValue;
	}

}
