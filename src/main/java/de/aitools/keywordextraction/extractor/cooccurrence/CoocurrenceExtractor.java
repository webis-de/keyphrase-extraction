/*
 * @(#)CoocurrenceExtractor.java
 * 
 * Description: This class is a wrapper class for the coocurrence KeywordExtractor. It 
 *              implents the KeywordExtractor interface to enable the construction
 *              of new instances of coocurrence KeywordExtractor using the 
 *              KeywordExtractorFactory class.
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
package de.aitools.keywordextraction.extractor.cooccurrence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import de.aitools.keywordextraction.extractor.KeywordExtractor;
import de.aitools.keywordextraction.extractor.cooccurrence.comparator.ChiComparator;
import de.aitools.keywordextraction.extractor.cooccurrence.comparator.TFComparator;
import de.aitools.stopwords.StopWordList;

/**
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public class CoocurrenceExtractor extends KeywordExtractor {

	private static final int MIN_PHRASE_FREQUENCY = 4;

	private static final int MIN_PHRASE_LENGTH = 2;

	private static final double FREQUENT_TERM_RATION = 0.3;

	/**
	 * 
	 * 
	 */
	public CoocurrenceExtractor() {
		super();
	}

	/**
	 * 
	 */
	protected List<String> extract(String text, int numberOfKeywords) {
		List<String> keywords = new ArrayList<String>();

		// step 1: text preprocessing
		// get sentences
		List<Sentence> sentences = this.getSentences(text);

		// get words
		List<Term> words = this.getWords(sentences);

		// get frequent phrases
		List<Term> frequentPhrases = this.getFrequentPhrases(sentences);

		// get all running terms
		List<Term> runningTerms = this
				.getRunningTerms(frequentPhrases, words);

		// get frequent terms
		List<Term> frequentTerms = this.getFrequentTerms(runningTerms);

		// compute cooccurrence between frequent term
		this.computeCooccurrences(frequentTerms, frequentTerms);

		// compute cooccurrences between words and frequent terms
		this.computeCooccurrences(words, frequentTerms);

		// cluster terms
		List<Cluster> clustering = this.clusterTerms(frequentTerms);

		// compute expected probability
		this.computeExpectedProbability(frequentTerms, clustering);

		// compute the chi square value for every word
		this.computeChiSquareValue(clustering, runningTerms);

		// get keywords
		keywords = this.getKeywords(runningTerms, numberOfKeywords);

		// output */
		return keywords;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	private List<Sentence> getSentences(String text) {
		List<Sentence> sentences = new ArrayList<Sentence>();
		StringTokenizer sentenceTokenizer = new StringTokenizer(text, ".!?:;,");
		while (sentenceTokenizer.hasMoreTokens()) {
			String s = sentenceTokenizer.nextToken();
			Sentence sentence = new Sentence(s, this.language);
			if (sentence.getElements().size() < 100) {
				sentences.add(sentence);
			}
		}
		return sentences;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Term> getWords(List<Sentence> sentences) {
		Map<String, Term> termHash = new HashMap<String, Term>();
		for (Sentence sentence : sentences) {
			List<String> elements = sentence.getElements();
			for (int i = 0; i < elements.size(); i++) {
				String element = elements.get(i);
				if (sentence.getNonStopwordElements().contains(element)) {
					Term rootTerm = new Term(this.language);
					rootTerm.addElement(element);
					String stem = sentence.getStemmedElements().get(i);
					if (termHash.containsKey(stem)) {
						Term t = termHash.get(stem);
						t.setFrequency(t.getFrequency() + 1);
						t.addRootTerm(rootTerm);
						t.addRootSentence(sentence);
					} else {
						Term t = new Term(this.language);
						t.addElement(stem);
						t.addRootTerm(rootTerm);
						t.addRootSentence(sentence);
						termHash.put(stem, t);
					}
				}
			}
		}

		List<Term> words = new ArrayList<Term>(termHash.values());
		Collections.sort(words, new TFComparator());
		return words;
	}

	/**
	 * Warning! Heavy stuff...
	 * 
	 * @param sentences
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Term> getFrequentPhrases(List<Sentence> sentences) {
		Map<String, Term> phrases = new HashMap<String, Term>();
		StringBuffer sbStemmed;

		// create all possible phrases
		for (Sentence sentence : sentences) {
			List<String> elements = sentence.getElements();
			List<String> processedElements = sentence.getStemmedElements();
			for (int i = 0; i < elements.size(); i++) {
				sbStemmed = new StringBuffer();
				List<String> words = new ArrayList<String>();
				List<String> wordstems = new ArrayList<String>();
				for (int j = i; j < elements.size(); j++) {
					words.add(elements.get(j));
					wordstems.add(processedElements.get(j));
					sbStemmed.append(processedElements.get(j) + " ");
					String stemmedPhrase = sbStemmed.toString().trim();
					Term rootPhrase = new Term(this.language);
					rootPhrase.setElements(new ArrayList<String>(words));
					if (phrases.containsKey(stemmedPhrase)) { // stemmed
						// phrase exists
						Term sp = phrases.get(stemmedPhrase);
						sp.setFrequency(sp.getFrequency() + 1);
						sp.addRootTerm(rootPhrase);
						sp.addRootSentence(sentence);
					} else { // new phrase
						Term sp = new Term(this.language);
						sp.setElements(new ArrayList<String>(wordstems));
						sp.addRootTerm(rootPhrase);
						sp.addRootSentence(sentence);
						phrases.put(stemmedPhrase, sp);
					}
				}
			}
		}

		// extract all frequent phrases
		Map<String, Term> frequentPhrasesHash = new HashMap<String, Term>();
		Iterator<String> keys = phrases.keySet().iterator();
		while (keys.hasNext()) {
			Term p = phrases.get(keys.next());
			if (p.getFrequency() >= MIN_PHRASE_FREQUENCY) {
				List<String> newWordStems = new ArrayList<String>();
				List<String> wordstems = p.getElements();
				for (String wordstem : wordstems)
					if (!StopWordList.contains(wordstem, this.language))
						newWordStems.add(wordstem);
				if (newWordStems.size() >= MIN_PHRASE_LENGTH) {
					p.setElements(newWordStems);
					if (!frequentPhrasesHash.containsKey(p.toString()))
						frequentPhrasesHash.put(p.toString(), p);
				}
			}
		}

		List<Term> frequentPhrases = new ArrayList<Term>(frequentPhrasesHash.values());

		frequentPhrases = this.removeFullyIncludedTerms(frequentPhrases);

		Collections.sort(frequentPhrases, new TFComparator());
		return frequentPhrases;
	}

	/**
	 * 
	 * @param terms
	 * @return
	 */
	private List<Term> removeFullyIncludedTerms(List<Term> terms) {

		List<Term> disjointTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.size(); i++) {
			for (int j = 0; j < terms.size(); j++) {
				if (i != j) {
					Term t1 = terms.get(i);
					Term t2 = terms.get(j);
					if (t1 != null && t2 != null) {
						List<String> elements1 = t1.getElements();
						List<String> elements2 = t2.getElements();
						boolean fullyIncluded = true;
						for (String element : elements2) {
							if (!elements1.contains(element)) {
								fullyIncluded = false;
							}
						}
						if (fullyIncluded) {
							for (Sentence s : t2.getRootSentences()) {
								t1.addRootSentence(s);
							}
							for (Term t : t2.getRootTerms()) {
								t1.addRootTerm(t);
							}
							t1.setFrequency(t1.getFrequency()
									+ t2.getFrequency());
							terms.set(j, null);
						}
					}
				}
			}
		}

		for (Term t : terms) {
			if (t != null) {
				disjointTerms.add(t);
			}
		}

		return disjointTerms;
	}

	/**
	 * 
	 * @param frequentPhrases
	 * @param words
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Term> getRunningTerms(List<Term> frequentPhrases,
			List<Term> words) {
		List<Term> runningTerms = new ArrayList<Term>(words);
		runningTerms.addAll(frequentPhrases);
		runningTerms = this.removeFullyIncludedTerms(runningTerms);
		Collections.sort(runningTerms, new TFComparator());
		return runningTerms;
	}

	/**
	 * 
	 * @param frequentPhrases
	 * @param words
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Term> getFrequentTerms(List<Term> runningTerms) {
		List<Term> frequentTerms = new ArrayList<Term>(runningTerms);
		int size = (int) ((double) frequentTerms.size() * FREQUENT_TERM_RATION);
		frequentTerms.subList(size,frequentTerms.size()).clear();
		return frequentTerms;
	}

	/**
	 * 
	 * @param terms
	 */
	private void computeCooccurrences(List<Term> terms, List<Term> coocTerms) {
		Map<Term, Integer> cooccurrences;
		Term coocTerm;
		for (Term term : terms) {
			cooccurrences = new HashMap<Term, Integer>();
			for (int i = 0; i < coocTerms.size(); i++) {
				coocTerm = coocTerms.get(i);
				if (coocTerm != term) {
					for (Sentence sentence : term.getRootSentences()) {
						if (coocTerm.getRootSentences().contains(sentence)) {
							if (cooccurrences.containsKey(coocTerm)) {
								cooccurrences.put(coocTerm, cooccurrences
										.get(coocTerm) + 1);
							} else {
								cooccurrences.put(coocTerm, 1);
							}
						}
					}
				}
			}
			term.setCooccurrences(cooccurrences);
		}
	}

	/**
	 * 
	 * @param frequentTerms
	 */
	@SuppressWarnings("unchecked")
	private List<Cluster> clusterTerms(List<Term> frequentTerms) {

		List<Cluster> clustering = new ArrayList<Cluster>();

		for (Term t : frequentTerms) {
			Cluster c = new Cluster();
			c.addTerm(t);
			clustering.add(c);
		}

		// TODO cluster bauen
		// double jsThreshold = 0.95 * Math.log(2.0);
		// double miThreshold = Math.log(2.0);
		//		
		// List<Term> terms = (List<Term>) frequentTerms.clone();
		// while(terms.size() > 0) {
		// Cluster c = new Cluster();
		// c.addTerm(terms.remove(0));
		// if(terms.size() > 0) {
		// int pos = 0;
		// boolean end = false;
		// do {
		// Term t = terms.get(pos);
		// double score = JensenShannonDistance.computeDistance(t, c);
		// if(score > jsThreshold) {
		// c.addTerm(terms.remove(pos));
		// }
		// else {
		// pos++;
		// }
		// if(pos >= terms.size()) {
		// end = true;
		// }
		// } while(!end);
		// }
		// clustering.add(c);
		// }

		return clustering;
	}

	/**
	 * 
	 * @param frequentTerms
	 */
	private void computeExpectedProbability(List<Term> frequentTerms,
			List<Cluster> clustering) {
		int nTotal = frequentTerms.size();
		for (Cluster cluster : clustering) {
			int nc = cluster.getTerms().size();
			double expectedProbability = (double) nc / (double) nTotal;
			cluster.setExpectedProbability(expectedProbability);
		}
	}

	/**
	 * 
	 * @param frequentTerms
	 */
	@SuppressWarnings("unchecked")
	private void computeChiSquareValue(List<Cluster> clustering,
			List<Term> runningTerms) {

		for (Term term : runningTerms) {
			double chiSquareValue = 0.0;
			double max = 0.0;
			for (Cluster cluster : clustering) {
				max = Math.max(max, this.computeSummand(cluster, term));
			}
			for (Cluster cluster : clustering) {
				chiSquareValue += this.computeSummand(cluster, term);
			}
			chiSquareValue = chiSquareValue - max;
			term.setChiSquareValue(chiSquareValue);
			Collections.sort(runningTerms, new ChiComparator());
		}
	}

	/**
	 * 
	 * @param cluster
	 * @param term
	 * @return
	 */
	private double computeSummand(Cluster cluster, Term term) {
		double nw = (double) term.getTotalNumberOfTerms();
		double pc = cluster.getExpectedProbability();
		double nwpc = nw * pc;
		double freqWC = 0;
		for (Term clusterTerm : cluster.getTerms()) {
			Map<Term, Integer> tmphash = clusterTerm.getCooccurrences();
			if (tmphash.containsKey(term)) {
				freqWC += (double) clusterTerm.getCooccurrences().get(term);
			}
		}
		return Math.pow((freqWC - nwpc), 2) / nwpc;
	}

	/**
	 * 
	 * @param runningTerms
	 * @param numberOfKeywords
	 * @return
	 */
	private List<String> getKeywords(List<Term> runningTerms,
			int numberOfKeywords) {
		List<String> keywords = new ArrayList<String>();
		runningTerms.subList(Math.min(numberOfKeywords, runningTerms.size()),runningTerms.size()).clear();
		for (Term t : runningTerms) {
			keywords.add(t.getKeywordTerm().toString());
		}
		return keywords;
	}

}