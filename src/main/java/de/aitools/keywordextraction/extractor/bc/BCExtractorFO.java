/*
 * @(#)BCExtractorFO.java
 * 
 *  Description: 
 *  
 *  Created:     09.03.2006
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
package de.aitools.keywordextraction.extractor.bc;

import de.aitools.keywordextraction.extractor.bc.comparator.ScoreFOComparator;

/**
 * @author Karsten
 *
 */
public class BCExtractorFO extends BCExtractor {

	/**
	 * 
	 */
	public BCExtractorFO() {
		super();
		this.comparator = new ScoreFOComparator();
	}

}
