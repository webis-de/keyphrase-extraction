/*
 * @(#)HNFrequencyComparator.java
 * 
 * Description: HNFrequencyComparator 
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
package de.aitools.keywordextraction.extractor.bc.comparator;

import java.util.Comparator;

import de.aitools.keywordextraction.extractor.bc.HeadNoun;

/**
 * HNFrequencyComparator
 * 
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public class HNFrequencyComparator implements Comparator {

	/**
	 * 
	 * @param hn1
	 * @param hn2
	 * @return
	 */
	public int compare(Object hn1, Object hn2) {
		double freqHN1 = ((HeadNoun) hn1).getFrequency();
		double freqHN2 = ((HeadNoun) hn2).getFrequency();

		if (freqHN1 > freqHN2) {
			return -1;
		} else if (freqHN1 < freqHN2) {
			return 1;
		} else {
			return 0;
		}
	}

}