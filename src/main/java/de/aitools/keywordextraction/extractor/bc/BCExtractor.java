/*
 * @(#)BCExtractor.java
 * 
 *  Description: 
 *  
 *  Created:     03.03.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universit�t Weimar
 *  Bauhausstra�e 11
 *  99423 Weimar � Germany
 *  
 */
package de.aitools.keywordextraction.extractor.bc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import qtag.Tagger;
import de.aitools.keywordextraction.extractor.KeywordExtractor;
import de.aitools.keywordextraction.extractor.bc.comparator.HNFrequencyComparator;
import de.aitools.stemming.SnowballStemmer;

/**
 * @author Karsten Klueger, BUW
 * 
 */
public abstract class BCExtractor extends KeywordExtractor {

	private static final String WORD_DELIMITER = ".?!,;()<>_:+#�`\t\\}][{\"�$%&/)\n ";

	private static final String QTAG_RESOURCE_PATH = "/de/aitools/keywordextraction/extractor/bc/qtagres/";

	private static final String QTAG_RESOURCE_NAME = "qtag";

	private Tagger qtag;

	protected Comparator comparator;

	private int numberOfKeywords;

	/**
	 * 
	 */
	protected BCExtractor() {
		super();
		this.initTagger();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aitools.keywordextraction.extractor.KeywordExtractor#setLanguage(java.lang.String)
	 */
	public void setLanguage(Locale language) {
		this.language = language;
		this.initTagger();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aitools.keywordextraction.extractor.KeywordExtractor#extract(java.lang.StringBuffer,
	 *      int)
	 */
	protected List<String> extract(String text, int numberOfKeywords) {
		List<String> keywords = new ArrayList<String>();
		
		this.numberOfKeywords = numberOfKeywords;		
		
		// get each semantic unit
		List<String> semanticUnits = this.getSemanticUnits(text);

		// get noun phrases
		List<NounPhrase> nounPhrases = this.getNounPhrases(semanticUnits);
		
		// get most frequent head nouns
		List<HeadNoun> headNouns = this.getHeadNouns(nounPhrases);

		// get top scored head noun phrases
		List<HeadNounPhrase> headNounPhrases = this.getHeadNounPhrases(
				headNouns, nounPhrases);

		// remove wholly-included phrases
		keywords = this.getCandidates(headNounPhrases);

		// return only requestet number of keywords
		keywords.subList(Math.min(numberOfKeywords, keywords.size()),keywords.size()).clear();

		return keywords;
	}

	/**
	 * Initailaizes the qtag tagger
	 * 
	 */
	private void initTagger() {
		String qtagResource = QTAG_RESOURCE_PATH + this.language + "/"
				+ QTAG_RESOURCE_NAME;
		this.qtag = null;
		try {
			this.qtag = new Tagger(qtagResource);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	private List<String> getSemanticUnits(String text) {
		List<String> sentences = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(text, ".!?");
		while (st.hasMoreElements()) {
			sentences.add(st.nextToken().trim());
		}

		return sentences;
	}

	/**
	 * 
	 * @param textBuffer
	 * @return
	 */
	private List<List<String>> tag(String text) {

		// create input for pos tagger
		List<String> tokenList = new ArrayList<String>();
		StringTokenizer textST = new StringTokenizer(text, WORD_DELIMITER);
		while (textST.hasMoreTokens()) {
			tokenList.add(textST.nextToken());
		}

		// tag input
		String[] tags = this.qtag.tag(tokenList);

		// create output
		int i = 0;
		List<List<String>> taggedText = new ArrayList<List<String>>();
		List<String> taggedToken = null;
		for (String tag : tags) {
			taggedToken = new ArrayList<String>();
			taggedToken.add(tokenList.get(i));
			taggedToken.add(tag);
			taggedText.add(taggedToken);
			i++;
		}

		return taggedText;
	}

	/**
	 * 
	 * @param semanticUnits
	 * @return
	 */
	private List<NounPhrase> getNounPhrases(List<String> semanticUnits) {
		List<NounPhrase> nounPhrases = new ArrayList<NounPhrase>();

		// get all base noun phrases from every semantic unit
		for (String semanticUnit : semanticUnits) {
			// tag unit
			List<List<String>> taggedUnit = this.tag(semanticUnit);
			// get noun phrase for this unit
			List<NounPhrase> bnps = this.getNounPhrasesForUnit(taggedUnit);
			// add found noun phrases to a List
			nounPhrases.addAll(bnps);
		}

		// set first occurrence and frequency
		Map<String, NounPhrase> nounPhraseHash = new HashMap<String, NounPhrase>();
		int currentPos = 1;
		for (NounPhrase np : nounPhrases) {
			String phrase = np.getPhrase();
			if (nounPhraseHash.containsKey(phrase)) {
				np = nounPhraseHash.get(phrase);
				np.setFirstOccurrence(Math.min(np.getFirstOccurrence(),
						currentPos));
				np.setFrequency(np.getFrequency() + 1);
			} else {
				np.setFirstOccurrence(currentPos);
				np.setFrequency(1);
			}
			nounPhraseHash.put(phrase, np);
			currentPos++;
		}

		// create output
		nounPhrases = new ArrayList<NounPhrase>(nounPhraseHash.values());

		return nounPhrases;
	}

	/**
	 * JJ, JJR, JJS equals adjective NN, NNS eqals noun
	 * 
	 * @param taggedText
	 * @return
	 */

	private List<NounPhrase> getNounPhrasesForUnit(
			List<List<String>> taggedText) {
		List<NounPhrase> baseNounPhrases = new ArrayList<NounPhrase>();

		List<String> words = new ArrayList<String>();
		int lastNounPos = 0;
		for (List<String> token : taggedText) {
			int tokenLen = token.get(1).length();
			String tag = "";
			if (tokenLen > 1) {
				tag = token.get(1).substring(0, 1);
			}
			// if tag is noun or adjective, add word to tempPhrase
			if (tag.equalsIgnoreCase("N") || tag.equalsIgnoreCase("J")) {
				// get the word
				String word = token.get(0);
				// if the word consists of more than two letters
				if (word.length() > 2) {
					// add word to word List
					words.add(word);
					// if tag is noun, remember this position in the word List
					// as position of the last noun (this could be the head
					// noun)
					if (tag.equalsIgnoreCase("N")) {
						lastNounPos = words.size();
					}
				}
			} else {
				// if there are words in the word List
				if (words.size() > 0) {
					if (lastNounPos < words.size()) {
						// remove all words after the last noun
						words.subList(lastNounPos,words.size()).clear();
					}
					// if there are still words in the word List, we found a
					// noun phrase
					if (words.size() > 0) {
						baseNounPhrases.add(new NounPhrase(words));
					}
					// clean the word List to find next noun phrase
					words = new ArrayList<String>();
				}
			}
		}

		return baseNounPhrases;
	}

	/**
	 * 
	 * @param baseNounPhrases
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<HeadNoun> getHeadNouns(List<NounPhrase> baseNounPhrases) {
		List<HeadNoun> allHeadNouns = new ArrayList<HeadNoun>();
		SnowballStemmer stemmer = new SnowballStemmer(this.language);

		// get head nouns
		Map<String, HeadNoun> headNounsHash = new HashMap<String, HeadNoun>();
		HeadNoun hn = null;
		for (NounPhrase bnp : baseNounPhrases) {
			int frequency = bnp.getFrequency();
			int firstOccurence = bnp.getFirstOccurrence();
			String noun = bnp.getWords().get(bnp.getWords().size() - 1);
			String headNounStem = stemmer.stem(noun);

			if (headNounsHash.containsKey(headNounStem)) {
				hn = headNounsHash.get(headNounStem);
				hn.setFrequency(frequency + hn.getFrequency());
				hn.setFirstOccurrence(Math.min(firstOccurence, hn
						.getFirstOccurrence()));
				hn.addNoun(noun);
			} else {
				hn = new HeadNoun();
				hn.setNounStemm(headNounStem);
				hn.addNoun(noun);
				hn.setFirstOccurrence(firstOccurence);
				hn.setFrequency(frequency);
				headNounsHash.put(headNounStem, hn);
			}
		}

		// create output
		Iterator<String> hnIter = headNounsHash.keySet().iterator();
		while (hnIter.hasNext()) {
			hn = headNounsHash.get(hnIter.next());
			allHeadNouns.add(hn);
		}
		// sort list
		Collections.sort(allHeadNouns, new HNFrequencyComparator());

		List<HeadNoun> headNouns = new ArrayList<HeadNoun>();
		for (int i = 0; i < this.numberOfKeywords * 3 && i < allHeadNouns.size(); i++) {
			headNouns.add(allHeadNouns.get(i));
		}
		return headNouns;
	}

	/**
	 * 
	 * @param headNouns
	 * @param baseNounPhrases
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<HeadNounPhrase> getHeadNounPhrases(
			List<HeadNoun> headNouns, List<NounPhrase> nounPhrases) {
		List<HeadNounPhrase> headNounPhrases = new ArrayList<HeadNounPhrase>();

		// recover all unstemmed head nouns
		List<String> nouns = new ArrayList<String>();
		Map<String, HeadNoun> headNounsHash = new HashMap<String, HeadNoun>();
		for (HeadNoun hn : headNouns) {			
			for (String noun : hn.getNouns()) {
				headNounsHash.put(noun, hn);
				nouns.add(noun);
			}
		}

		// get head noun phrases
		for (NounPhrase nounPhrase : nounPhrases) {
			List<String> words = nounPhrase.getWords();
			String headNoun = words.get(words.size() - 1);
			if (nouns.contains(headNoun)) {
				headNounPhrases.add(new HeadNounPhrase(headNoun, nounPhrase
						.getFrequency(), headNounsHash.get(headNoun)
						.getFirstOccurrence(), words));
			}
		}

		// sort by score
		Collections.sort(headNounPhrases, this.comparator);

		return headNounPhrases;
	}

	/**
	 * 
	 * @param headNounPhrases
	 * @return
	 */
	private List<String> getCandidates(
			List<HeadNounPhrase> headNounPhrases) {
		List<String> candidates = new ArrayList<String>();

		StringBuffer allPhrasesBuff = new StringBuffer();
		for (HeadNounPhrase testhnp : headNounPhrases) {
			String phrase = testhnp.getPhrase();
			for (HeadNounPhrase hnp : headNounPhrases) {
				if (testhnp != hnp) {
					allPhrasesBuff.append(hnp.getPhrase() + ";");
				}
			}
			if (!allPhrasesBuff.toString().contains(phrase)) {
				candidates.add(phrase);
			}
			allPhrasesBuff = new StringBuffer();
		}

		return candidates;
	}

}
