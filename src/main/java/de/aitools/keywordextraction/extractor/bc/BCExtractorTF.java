/*
 * @(#)BCExtractorTF.java
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

import de.aitools.keywordextraction.extractor.bc.comparator.ScoreTFComparator;

/**
 * @author Karsten Klueger, BUW
 *
 */
public class BCExtractorTF extends BCExtractor {

	/**
	 * 
	 */
	public BCExtractorTF() {
		super();
		this.comparator = new ScoreTFComparator();
	}

}
