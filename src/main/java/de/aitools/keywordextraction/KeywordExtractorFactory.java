/*
 * @(#)KeywordExtractorFactory.java
 * 
 *  Description: This factory Class enables applications to obtain
 *               a KeywordExtractor for extracting keywords from a 
 *               StringBuffer.
 *  
 *  Created:     2006-01-31
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universit�t Weimar
 *  Bauhausstra�e 11
 *  99423 Weimar � Germany
 *  
 */
package de.aitools.keywordextraction;

import de.aitools.keywordextraction.extractor.KeywordExtractor;
import de.aitools.keywordextraction.extractor.bc.BCExtractorFO;
import de.aitools.keywordextraction.extractor.bc.BCExtractorTF;
import de.aitools.keywordextraction.extractor.context.ContextExtractor;
import de.aitools.keywordextraction.extractor.cooccurrence.CoocurrenceExtractor;
import de.aitools.keywordextraction.extractor.kea.KeaExtractorCSTR;
import de.aitools.keywordextraction.extractor.kea.KeaExtractorLong;
import de.aitools.keywordextraction.extractor.kea.KeaExtractorShort;
import de.aitools.keywordextraction.extractor.rsp.RSPExtractorFO;
import de.aitools.keywordextraction.extractor.rsp.RSPExtractorTF;

/**
 * This factory Class enables applications to obtain a KeywordExtractor
 * for extracting keywords from a StringBuffer.
 * 
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public class KeywordExtractorFactory {
	
	/**
	 * Protected constructor to force use of 
	 * {@link #getKeaExtractorCSTR() getKeaExtractorCSTR()},
	 * {@link #getKeaExtractorLong() getKeaExtractorLong()},
	 * {@link #getKeaExtractorShort() getKeaExtractorShort()},
	 * {@link #getRSPExtractorTF() getRSPExtractorTF()},
	 * {@link #getRSPExtractorFO() getRSPExtractorFO()},
	 * {@link #getCoocurrenceExtractor() getCoocurrenceExtractor()},
	 * {@link #getBCExtractorTF() getBCExtractorTF()},
	 * {@link #getBCExtractorFO() getBCExtractorFO()}.
	 *
	 */
	protected KeywordExtractorFactory() {}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.kea.KeaExtractorCSTR#KeaExtractorCSTR() KeaExtractorCSTR}
	 * 
	 * @return KeaExtractorCSTR
	 */
	public static KeywordExtractor getKeaExtractorCSTR() {
		return new KeaExtractorCSTR();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.kea.KeaExtractorLong#KeaExtractorLong() KeaExtractorLong}
	 * 
	 * @return KeaExtractorLong
	 */
	public static KeywordExtractor getKeaExtractorLong() {
		return new KeaExtractorLong();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.kea.KeaExtractorShort#KeaExtractorShort() KeaExtractorShort}
	 * 
	 * @return KeaExtractorShort
	 */
	public static KeywordExtractor getKeaExtractorShort() {
		return new KeaExtractorShort();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.rsp.RSPExtractorTF#RSPExtractorTF() RSPExtractorTF}
	 * 
	 * @return RSPExtractorTF
	 */
	public static KeywordExtractor getRSPExtractorTF() {
		return new RSPExtractorTF();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.rsp.RSPExtractorTFxFO#RSPExtractorFO() RSPExtractorFO}
	 * 
	 * @return RSPExtractorFO
	 */
	public static KeywordExtractor getRSPExtractorFO() {
		return new RSPExtractorFO();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.cooccurrence.CoocurrenceExtractor#CoocurrenceExtractor() CoocurrenceExtractor}
	 * 
	 * @return CoocurrenceExtractor
	 */
	public static KeywordExtractor getCoocurrenceExtractor() {
		return new CoocurrenceExtractor();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.bc.BCExtractorTF#BCExtractorTF() BCExtractorTF}
	 * 
	 * @return BCExtractor
	 */
	public static KeywordExtractor getBCExtractorTF() {
		return new BCExtractorTF();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.bc.BCExtractorFO#BCExtractorFO() BCExtractorFO}
	 * 
	 * @return BCExtractor
	 */
	public static KeywordExtractor getBCExtractorFO() {
		return new BCExtractorFO();
	}
	
	/**
	 * Returns a new instance of a 
	 * {@link de.aitools.keywordextraction.extractor.context.ContextExtractor#ContextExctractor() ContextExtractor}
	 * 
	 * @return ContextExtractor
	 */
	public static KeywordExtractor getContextExtractor() {
		return new ContextExtractor();
	}

}