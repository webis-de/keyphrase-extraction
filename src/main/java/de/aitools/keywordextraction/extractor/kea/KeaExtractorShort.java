/*
 * @(#)KeaExtractorhort.java
 * 
 *  Description: 
 *  
 *  Created:     16.02.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universitaet Weimar
 *  Bauhausstrasse 11
 *  99423 Weimar - Germany
 *  
 */
package de.aitools.keywordextraction.extractor.kea;

/**
 * @author Karsten
 *
 */
public class KeaExtractorShort extends KeaExtractor {
	
	public static final String MODEL = "shorttext";

	/**
	 * 
	 *
	 */
	public KeaExtractorShort() {
		super();
		this.model = MODEL;
	}

}