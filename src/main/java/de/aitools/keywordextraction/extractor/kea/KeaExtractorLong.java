/*
 * @(#)KeaExtractorLong.java
 * 
 *  Description: 
 *  
 *  Created:     16.02.2006
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
package de.aitools.keywordextraction.extractor.kea;

/**
 * @author Karsten
 *
 */
public class KeaExtractorLong extends KeaExtractor {
	
	public static final String MODEL = "longtext";

	/**
	 * 
	 *
	 */
	public KeaExtractorLong() {
		super();
		this.model = MODEL;
	}

}