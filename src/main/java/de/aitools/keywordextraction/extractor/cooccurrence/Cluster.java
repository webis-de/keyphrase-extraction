/*
 * @(#)Cluster.java
 * 
 *  Description: 
 *  
 *  Created:     20.03.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 * Copyright Bauhaus-Universität Weimar
 * Bauhausstraße 11
 * 99423 Weimar, Germany
 *  
 */
/**
 * 
 */
package de.aitools.keywordextraction.extractor.cooccurrence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karsten
 * 
 */
public class Cluster {

	private List<Term> terms;

	private double expectedProbability;

	/**
	 * 
	 * @param terms
	 */
	public Cluster(List<Term> terms) {
		this.terms = terms;
		this.expectedProbability = 0.0;
	}

	/**
	 * 
	 * 
	 */
	public Cluster() {
		this.terms = new ArrayList<Term>();
		this.expectedProbability = 0.0;
	}

	/**
	 * @return Returns the terms.
	 */
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * @param terms
	 *            The terms to set.
	 */
	public void setTerms(List<Term> terms) {
		this.terms = terms;
		this.expectedProbability = 0.0;
	}

	/**
	 * 
	 * @param t
	 */
	public void addTerm(Term term) {
		this.terms.add(term);
	}

	/**
	 * @return Returns the expectedProbability.
	 */
	public double getExpectedProbability() {
		return expectedProbability;
	}

	/**
	 * @param expectedProbability The expectedProbability to set.
	 */
	public void setExpectedProbability(double expectedProbability) {
		this.expectedProbability = expectedProbability;
	}	

}
