/*
 * @(#)FOComparator.java
 * 
 * Description: The FOComparator is used to compare two RSnGrams. 
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
package de.aitools.keywordextraction.extractor.rsp.comparator;

import java.util.Comparator;

import de.aitools.keywordextraction.extractor.rsp.RSnGram;

/**
 * The FOComparator is used to compare two RSnGrams. 
 * 
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public class FOComparator implements Comparator {

	/**
	 * 
	 * @param rs1
	 * @param rs2
	 * @return
	 */
	public int compare(Object rs1, Object rs2) {
		double foRs1 = ((RSnGram) rs1).getFirstOccurrence();
		double foRs2 = ((RSnGram) rs2).getFirstOccurrence();

		if (foRs1 > foRs2) {
			return -1;
		} else if (foRs1 < foRs2) {
			return 1;
		} else {
			return 0;
		}
	}

}