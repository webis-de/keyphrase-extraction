/*
 * @(#)RSPExtractor.java
 * 
 * Description: The RSPExtractor (RSPattern = repeated string pattern)
 *              is the implentation of the algorithm described in the paper:
 *              Yuen-Hsien Tseng: "Multilingual Keyword Extraction for Term 
 *              Suggestion" in Proceedings of the 21st annual international
 *              ACM SIGIR conference on Research and Development in Information
 *              Retrieval, Melbourne, Australia, S. 377-378, 1998
 *              It exctracts a number of keywords from a StringBuffer by 
 *              searching frequent repeated string patterns(n-grams).
 * 
 *              Currently it can extract keywords only from english text.
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
package de.aitools.keywordextraction.extractor.rsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import de.aitools.lexer.Delimiter;
import de.aitools.keywordextraction.extractor.KeywordExtractor;
import de.aitools.keywordextraction.extractor.rsp.comparator.FOComparator;
import de.aitools.stopwords.StopWordList;

/**
 * The RSPExtractor (RSPattern = repeated string pattern) is the
 * implentation of the algorithm described in the paper:<br>
 * <br>
 * 
 * Yuen-Hsien Tseng: "Multilingual Keyword Extraction for Term Suggestion" in
 * Proceedings of the 21st annual international ACM SIGIR conference on Research
 * and Development in Information Retrieval, Melbourne, Australia, S. 377-378,
 * 1998 <br>
 * <br>
 * It exctracts a number of keywords from a StringBuffer by searching frequent
 * repeated string patterns. (n-grams)<br>
 * Currently it can extract keywords only from english text.
 * 
 * 
 * Created on 20.01.2006
 * 
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public abstract class RSPExtractor extends KeywordExtractor {

	// maximum length of a keyword
	private static final int MAXNUMWORDS = 7;

	// minimum length of a keyword
	private static final int MINNUMWORDS = 1;

	// minimum length of a word
	private static final int MINNUMCHARS = 5;

	private List<RSnGram> list;

	private List<RSnGram> finalList;

	private int threshold;

	protected Comparator comparator;

	/**
	 * Constructor
	 */
	protected RSPExtractor() {
		super();
		this.list = new ArrayList<RSnGram>();
		this.finalList = new ArrayList<RSnGram>();
		this.comparator = new FOComparator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aitools.keywordextraction.extractor.KeywordExtractor#extract(java.lang.StringBuffer,
	 *      int)
	 */
	protected List<String> extract(String input, int numberOfKeywords) {

		this.threshold = 0;

		List<String> keywords = new ArrayList<String>();			

		// repeat until requested number of keywords is reached
		while (keywords.size() < numberOfKeywords) {

			// create list of 2-grams from input StringBuffer
			this.list = this.createList(input);

			// compute threshold
			this.threshold = this.computeThreshold();

			// compute the final list of keywords
			this.finalList = this.computeFinalList(this.list);

			// clean the final list
			this.finalList = this.cleanList(this.finalList);

			// sort the final list by a ranking
			this.sortList(this.finalList);

			// return the top numberOfKeywords
			keywords.clear();			
			for (int i = 0; i < numberOfKeywords * 2; i++) {
				if (finalList.size() > i) {
					keywords.add(finalList.get(i).toString());
				}
			}
			
			// break if threshold is 1 to avoid a deadlock
			if(this.threshold == 1) {
				break;
			}
		}

		return keywords;
	}

	/**
	 * Compute the threshold for merging RSnGrams.
	 * 
	 * If the threshold is computed the fist time, it is currently set to
	 * log(number of words).
	 * 
	 * If the threshold has allready been computed, it is decremented by 1 until
	 * 1 is reached.
	 * 
	 * @return The threshold.
	 */
	private int computeThreshold() {
		int threshold = this.threshold;

		if (threshold == 0) {
			threshold = (int) Math.log(this.list.size());
		} else if (threshold > 1) {
			threshold = (int) Math.round((double)threshold / (double)1.5);
		}

		return threshold;
	}

	/**
	 * Creates a list of overlapping 2-grams (RSnGrams) including the occuring
	 * frequencies from a StingBuffer.
	 * 
	 * @param text
	 * @return
	 */
	private List<RSnGram> createList(String text) {
		List<RSnGram> list = new ArrayList<RSnGram>();
		String w1, w2;

		StringTokenizer textST = new StringTokenizer(text, Delimiter.ALL);
		w1 = textST.nextToken();

		// create list
		while (textST.hasMoreTokens()) {
			w2 = textST.nextToken();
			RSnGram rsng = new RSnGram();
			rsng.addWord(w1);
			rsng.addWord(w2);
			list.add(rsng);			
			w1 = w2;
		}

		// count frequencies
		this.countFrequencies(list);

		// compute the relative position of first occurrence
		this.computeRelativeFirstOccurrence(list);

		return list;
	}

	/**
	 * Counts the occurring frequency for each RSnGram in the given list
	 * 
	 * @param list
	 */
	private void countFrequencies(List<RSnGram> list) {

		String words;
		HashMap<String, Integer> counterMap = new HashMap<String, Integer>();

		// count elements and save frequency in a hashmap
		for (RSnGram rs : list) {
			words = rs.toString();
			if (counterMap.containsKey(words)) {
				counterMap.put(words, counterMap.get(words) + 1);
			} else {
				counterMap.put(words, 1);
			}
		}

		// add frequency from hashmap to list
		for (RSnGram rs : list) {
			int freq = counterMap.get(rs.toString());
			// absolute frequency for comparing agains threshold
			rs.setAbsoluteFrequency(freq);
			// relative frequency for use in ftfoComparator
			rs.setFrequency((double) freq / list.size());
		}
	}

	/**
	 * Computes the first occurrence value of each RSnGram in the list as a
	 * relative value.
	 * 
	 * @param list
	 */
	private void computeRelativeFirstOccurrence(List<RSnGram> list) {
		ListIterator<RSnGram> it = list.listIterator();
		int size = list.size();
		int i = 0;
		RSnGram r;
		while(it.hasNext()){
			r = it.next();
			r.setFirstOccurrence(1 - (i / (float)size));
			i++;
		}
	}

	/**
	 * Real implementation of the algorithm
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<RSnGram> computeFinalList(List<RSnGram> list) {
		list = new ArrayList(list);
		List<RSnGram> finalList = new ArrayList<RSnGram>();
		List<RSnGram> mergeList = new ArrayList<RSnGram>();

		RSnGram rs1, rs2, mergedRSn;

		// while LIST is not empty
		while (list.size() > 0) {

			// set MergeList to empty
			mergeList.clear();

			// for each pair of adjacent elements rs1 and rs2 in LIST
			for (int i = 0; i < list.size(); i++) {

				// get first element (rs1)
				rs1 = list.get(i);

				// if there is a next element
				if (i < list.size() - 1) {
					// get next element
					rs2 = list.get(i + 1);
				} else {
					// create an empty element to prevent null pointer exception
					rs2 = new RSnGram();
				}

				// if rs1 and rs2 are mergeable and both of their occurring
				// frequencies are greater than a threshold
				if ((rs1.getAbsoluteFrequency() > this.threshold)
						&& (rs2.getAbsoluteFrequency() > this.threshold)
						&& (mergedRSn = this.merge(rs1, rs2)) != null
						&& mergedRSn.getNumberOfWords() < MAXNUMWORDS) {

					// merge K1 and K2 into K and push K into MergeList
					mergeList.add(mergedRSn);

				}

				// if occuring frequency of K1 is greater then a threshold
				// and
				// K1 did not merge with element before
				else if ((rs1.getAbsoluteFrequency() > this.threshold)
						&& (!rs1.isMergedWithElementBefore() 
						   || rs1.getNumberOfWords() == MAXNUMWORDS-1)) {

					// push K1 into FinalList
					if (!this.listContains(finalList, rs1))
						finalList.add(rs1);
				}
			}

			// count frequencies
			this.countFrequencies(mergeList);

			// set LIST to MergeList
			list.clear();
			list.addAll(mergeList);
		}

		return finalList;
	}

	/**
	 * Tests if a given RSnGram is part of a list.
	 * 
	 * @param list
	 * @param rs
	 * @return
	 */
	private boolean listContains(List<RSnGram> list, RSnGram rs) {
		for (RSnGram tmprs : list) {
			if (tmprs.toString().equalsIgnoreCase(rs.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Merges the RSnGrams rs1 and rs2 and retruns a new RSnGram mergedRSnGram
	 * if rs1 and rs2 are mergeable. If rs1 and rs2 are not mergeable NULL is
	 * returned. Rs1 and rs2 are mergeable if ...
	 * 
	 * @TODO beschreibung der Vermischung
	 * 
	 * @param rs1
	 * @param rs2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private RSnGram merge(RSnGram rs1, RSnGram rs2) {

		// get words from both RSnGrams
		ArrayList<String> wordsRS1 = (ArrayList<String>) rs1.getWordList()
				.clone();
		ArrayList<String> wordsRS2 = (ArrayList<String>) rs2.getWordList()
				.clone();

		// store last word from rs2 for merging with rs1
		String lastWordRS2 = wordsRS2.get(wordsRS2.size() - 1);

		// delete first word from rs1 and last word from rs2
		wordsRS1.remove(0);
		wordsRS2.remove(wordsRS2.size() - 1);

		// get remaining words from rs1
		StringBuffer w1Buffer = new StringBuffer();
		for (String words : wordsRS1) {
			w1Buffer.append(words);
		}

		// get remaining words from rs2
		StringBuffer w2Buffer = new StringBuffer();
		for (String words : wordsRS2) {
			w2Buffer.append(words);
		}

		// if remaining words from rs1 and rs2 are equal then rs1 and rs2 are :
		//
		// mergeable
		if (w1Buffer.toString().equalsIgnoreCase(w2Buffer.toString())) {
			// set rs2 as merged with element before
			rs2.setMergedWithElementBefore();

			// craete a new RSnGram containing the merged ArrayList of words
			RSnGram mergedRSnGram = new RSnGram();

			// add words from rs1
			for (String word : rs1.getWordList()) {
				mergedRSnGram.addWord(word);
			}

			// add last word from rs2
			mergedRSnGram.addWord(lastWordRS2);

			// set first occurrence which is the first occurrence of rs1
			mergedRSnGram.setFirstOccurrence(rs1.getFirstOccurrence());

			return mergedRSnGram;
		}
		// not mergeable
		else {
			return null;
		}
	}

	/**
	 * Removes stopwords.
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<RSnGram> cleanList(List<RSnGram> list) {

		list = new ArrayList<RSnGram>(list);
		List<RSnGram> outList = new ArrayList<RSnGram>();

		// remove stopwords
		for (RSnGram rs : list) {
			this.removeStopwords(rs);
			// if the entire string contains more than 1 word after stopword
			// removal and is longer than minNumberOfChars characters it is
			// added to the output
			// list
			if ((rs.getNumberOfWords() <= MAXNUMWORDS)
					&& (rs.getNumberOfWords() >= MINNUMWORDS)
					&& (rs.toString().length() >= MINNUMCHARS)) {
				outList.add(rs);
			}
		}

		// remove included subterms
		list.clear();
		list.addAll(outList);
		outList.clear();
		for (RSnGram rs : list) {
			if (!this.isSubString(rs, list)) {
				outList.add(rs);
			}
		}

		return outList;
	}

	/**
	 * Removes recursively stopwords from the beginnig and end of a ArrayList.
	 * 
	 * @param wordList
	 */
	private void removeStopwords(RSnGram rs) {
		ArrayList<String> wordList = rs.getWordList();

		// remove stopwords from the beginning of wordlist
		if (StopWordList.contains(wordList.get(0), this.language)) {
			wordList.remove(0);
			if (wordList.size() > 0) {
				this.removeStopwords(rs);
			}
		}
		// remove stopwords from the end of wordlist
		else if (StopWordList.contains(wordList.get(wordList.size() - 1),
				this.language)) {
			wordList.remove(wordList.size() - 1);
			if (wordList.size() > 0) {
				this.removeStopwords(rs);
			}
		}
	}

	/**
	 * Returns true if a word is a subString in the given List.
	 * 
	 * @param words
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isSubString(RSnGram rs, List<RSnGram> list) {
		// copy the given list for comparison
		List<RSnGram> compareList = new ArrayList<RSnGram>(list);
		// remove given RSnGram from the list to compare with
		compareList.remove(rs);

		// check if RSnGram is substring of any RSnGram in the compareList
		for (RSnGram compareRs : compareList) {
			if (compareRs.toString().contains(rs.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sort a list of RSnGrams using the FTFOComparator
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	private void sortList(List<RSnGram> list) {
		Collections.sort(list, this.comparator);
	}

}