/*
 * @(#)KeaExtractorCSTR.java
 * 
 *  Description: 
 *  
 *  Created:     16.02.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universität Weimar
 *  Bauhausstraße 11
 *  99423 Weimar, Germany
 *  
 */
/**
 * 
 */
package de.aitools.keywordextraction.extractor.kea;

/**
 * @author Karsten Klueger, BUW
 *
 */
public class KeaExtractorCSTR extends KeaExtractor {
	
	public static final String MODEL = "cstr_model";		
	
	/**
	 * 
	 *
	 */
	public KeaExtractorCSTR() {
		super();				
		this.model = MODEL;
	}

}