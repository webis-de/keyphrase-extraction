/*
 * @(#)JensenShannonDistance.java
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

/**
 * @author Karsten
 * 
 */
public class JensenShannonDistance {

	/**
	 * 
	 * @param t
	 * @param cluster
	 * @return
	 */
	public static double computeDistance(Term t1, Cluster cluster) {

		double dist = 0.0;
		for (Term t2 : cluster.getTerms()) {
			for (Term t : cluster.getTerms()) {
				double pw1 = computePww1(t, t1);
				double pw2 = computePww1(t, t2);
				double pw1w2 = pw1 + pw2;
				dist += computeHx(pw1w2) - computeHx(pw1) - computeHx(pw2);
			}
		}

		dist = Math.log(2.0) + 0.5 * dist;

		return dist;
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	private static double computeHx(double x) {
		if (x == 0.0) {
			return x;
		}
		return -x * Math.log(x);
	}

	/**
	 * 
	 * @param w
	 * @param w1
	 * @return
	 */
	private static double computePww1(Term w, Term w1) {
		double freqWW1 = 0.0;
		if (w.getCooccurrences().containsKey(w1)) {
			freqWW1 = (double) w.getCooccurrences().get(w1);
		}
		double freqW1 = (double) w1.getFrequency();
		return freqWW1 / freqW1;
	}

}
