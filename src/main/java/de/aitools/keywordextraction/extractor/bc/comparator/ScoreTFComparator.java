/*
 * @(#)ScoreTFComparator.java
 * 
 * Description: ScoreTFComparator 
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

import de.aitools.keywordextraction.extractor.bc.HeadNounPhrase;

/**
 * ScoreTFComparator
 * 
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public class ScoreTFComparator implements Comparator {

	/**
	 * 
	 * @param hnp1
	 * @param hnp2
	 * @return
	 */
	public int compare(Object hnp1, Object hnp2) {
		double scoreHnp1 = ((HeadNounPhrase) hnp1).getScoreTF();
		double scoreHnp2 = ((HeadNounPhrase) hnp2).getScoreTF();

		if (scoreHnp1 > scoreHnp2) {
			return -1;
		} else if (scoreHnp1 < scoreHnp2) {
			return 1;
		} else {
			return 0;
		}
	}

}