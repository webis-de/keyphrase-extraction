/*
 * @(#)RSPExtractorFO.java
 * 
 *  Description: 
 *  
 *  Created:     14.02.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universit�t Weimar
 *  Bauhausstra�e 11
 *  99423 Weimar � Germany
 *  
 */
package de.aitools.keywordextraction.extractor.rsp;

import de.aitools.keywordextraction.extractor.rsp.comparator.FOComparator;

/**
 * @author Karsten Klueger, BUW
 *
 */
public class RSPExtractorFO extends RSPExtractor {
	
	/**
	 * 
	 *
	 */
	public RSPExtractorFO() {
		super();
		this.comparator = new FOComparator();
	}
}