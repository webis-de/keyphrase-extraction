/*
 * @(#)TFComparator.java
 * 
 * Description: The TFComparator is used to compare two Terms. 
 *  
 * Created:	    2006-01-31
 * Author:      Karsten Klueger
 * mailto:      karsten.klueger@medien.uni-weimar.de
 *
 * Copyright Bauhaus-Universität Weimar
 * Bauhausstraße 11
 * 99423 Weimar, Germany
 *  
 */
package de.aitools.keywordextraction.extractor.cooccurrence.comparator;

import java.util.Comparator;

import de.aitools.keywordextraction.extractor.cooccurrence.Term;

/**
 * The TFComparator is used to compare two Terms.
 * 
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public class TFComparator implements Comparator {

	/**
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public int compare(Object t1, Object t2) {
		double tf1 = ((Term) t1).getFrequency();
		double tf2 = ((Term) t2).getFrequency();

		if (tf1 > tf2) {
			return -1;
		} else if (tf1 < tf2) {
			return 1;
		} else {
			return 0;
		}
	}

}