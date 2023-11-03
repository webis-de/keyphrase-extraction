/*
 * @(#)Term.java
 * 
 *  Description: 
 *  
 *  Created:     22.03.2006
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
package de.aitools.keywordextraction.extractor.context;

/**
 * @author Karsten
 * 
 */
public class Term implements Comparable, Cloneable {

	private String term;

	private double normalizedFrequency;
	
	private double entropy;
	
	private int absoluteFrequency;

	public Term(String term, double normalizedFrequency) {
		this.term = term;
		this.normalizedFrequency = normalizedFrequency;
		this.entropy = 0.0;
		this.absoluteFrequency = 0;
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	public int compareTo(Object o) {
		Term t = (Term) o;
//		double scoreSelf = this.entropy * this.getValue();
//		double scoreComp = t.getEntropy() * t.getValue();
		
		double scoreSelf = this.entropy;
		double scoreComp = t.getEntropy();
		
		if (scoreSelf > scoreComp) {
			return -1;
		} else if (scoreSelf < scoreComp) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * @return Returns the term.
	 */
	public String toString() {
		return term;
	}
	
	/**
	 * @return Returns the entropy.
	 */
	public double getEntropy() {
		return entropy;
	}

	/**
	 * @param entropy The entropy to set.
	 */
	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}
	
	/**
	 * 
	 */
	public final Object clone() throws CloneNotSupportedException {
		Term t = (Term) super.clone();
		t.term = this.term;
		t.entropy = this.entropy;
		t.normalizedFrequency = this.normalizedFrequency;
		t.absoluteFrequency = this.absoluteFrequency;
		return t;
	}

	/**
	 * @return Returns the absoluteFrequency.
	 */
	public int getAbsoluteFrequency() {
		return absoluteFrequency;
	}

	/**
	 * @param absoluteFrequency The absoluteFrequency to set.
	 */
	public void setAbsoluteFrequency(int absoluteFrequency) {
		this.absoluteFrequency = absoluteFrequency;
	}

	/**
	 * @return Returns the normalizedFrequency.
	 */
	public double getNormalizedFrequency() {
		return normalizedFrequency;
	}

	/**
	 * @param normalizedFrequency The normalizedFrequency to set.
	 */
	public void setNormalizedFrequency(double normalizedFrequency) {
		this.normalizedFrequency = normalizedFrequency;
	}
}
