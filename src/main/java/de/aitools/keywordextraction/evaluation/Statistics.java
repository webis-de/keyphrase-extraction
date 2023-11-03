/*
 * @(#)Statistics.java
 * 
 *  Description: 
 *  
 *  Created:     15.02.2006
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
package de.aitools.keywordextraction.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Karsten Klueger, BUW
 * 
 */
public class Statistics {

	/**
	 * 
	 * @param xmlFiles
	 * @return
	 */
	public static int getMininumNumberOfKeywords(List<File> xmlFiles, List<String> tags) {

		List<String> authorKeywords = null;
		StringBuffer textBuffer = null;

		int minNumberOfKeywords = Integer.MAX_VALUE;
		int numberOfKeywords = 0;

		for (File xmlFile : xmlFiles) {
			authorKeywords = new ArrayList<String>();
			textBuffer = new StringBuffer();

			Utils.processFile(xmlFile, authorKeywords, textBuffer, tags);

			numberOfKeywords = getNumberOfStrings(authorKeywords);

			if (numberOfKeywords < minNumberOfKeywords) {
				minNumberOfKeywords = numberOfKeywords;
			}

		}
		return minNumberOfKeywords;
	}

	/**
	 * 
	 * @param xmlFiles
	 * @return
	 */
	public static int getMaxiumNumberOfKeywords(List<File> xmlFiles, List<String> tags) {

		List<String> authorKeywords = null;
		StringBuffer textBuffer = null;

		int maxNumberOfKeywords = 0;
		int numberOfKeywords = 0;

		for (File xmlFile : xmlFiles) {
			authorKeywords = new ArrayList<String>();
			textBuffer = new StringBuffer();

			Utils.processFile(xmlFile, authorKeywords, textBuffer, tags);

			numberOfKeywords = getNumberOfStrings(authorKeywords);

			if (numberOfKeywords > maxNumberOfKeywords) {
				maxNumberOfKeywords = numberOfKeywords;
			}
			if (numberOfKeywords > 32) {
				System.out
						.println(xmlFile.getName() + " - " + numberOfKeywords);

			}
		}
		return maxNumberOfKeywords;
	}

	/**
	 * 
	 * @param xmlFiles
	 * @return
	 */
	public static double getAverageNumberOfKeywords(List<File> xmlFiles, List<String> tags) {

		List<String> authorKeywords = null;
		StringBuffer textBuffer = null;

		int numberOfKeywords = 0;
		int numberOfFiles = 0;

		for (File xmlFile : xmlFiles) {
			numberOfFiles++;

			authorKeywords = new ArrayList<String>();
			textBuffer = new StringBuffer();

			Utils.processFile(xmlFile, authorKeywords, textBuffer, tags);

			numberOfKeywords += getNumberOfStrings(authorKeywords);
		}

		double avNumberOfKeywords = (double) numberOfKeywords
				/ (double) numberOfFiles;

		return Math.rint(avNumberOfKeywords * 10) / 10;
	}

	/**
	 * 
	 * @param xmlFiles
	 * @return
	 */
	public static double getKeywordsInTextRatio(List<File> xmlFiles, List<String> tags) {

		List<String> authorKeywords = null;
		StringBuffer textBuffer = null;
		
		int keywordCount = 0;
		int hitCount = 0;
		StringTokenizer termTokenizer;

		for (File xmlFile : xmlFiles) {
			authorKeywords = new ArrayList<String>();
			textBuffer = new StringBuffer();
			// get keywords
			Utils.processFile(xmlFile, authorKeywords, textBuffer, tags);
			// check if keywords are in the text
			String text = textBuffer.toString();
			for (String keyword : authorKeywords) {
				termTokenizer = new StringTokenizer(keyword, " -()/");
				while (termTokenizer.hasMoreTokens()) {
					if (text.contains(termTokenizer.nextToken())) {
						hitCount++;
					}
					keywordCount++;
				}
			}
		}

		return 100 * (double) hitCount / (double) keywordCount;
	}

	/**
	 * 
	 * @param collectionDir
	 */
	public static void getCollectionStatistics(File collectionDir, List<String> tags) {
		List<File> xmlFiles = Utils.getAllFiles(collectionDir, "xml");

		double keywordRatio = getKeywordsInTextRatio(xmlFiles, tags);
		double avNumberOfKeywords = getAverageNumberOfKeywords(xmlFiles, tags);
		int minNoKeywords = getMininumNumberOfKeywords(xmlFiles, tags);
		int maxNoKeywords = getMaxiumNumberOfKeywords(xmlFiles, tags);
		Map<Integer, Integer> keywordDistribution = getKeywordDistribution(xmlFiles, tags);

		File reportFile = new File(collectionDir.getAbsolutePath()
				+ "/_statistics.txt");

		String reportString = "*************************************\n" + "*\n"
				+ "* statistics\n\n\n" + "Number of Files: " + xmlFiles.size()
				+ "\n\n" + " av. no. of keywords per file: "
				+ avNumberOfKeywords + "\n" + "min. no. of keywords per file: "
				+ minNoKeywords + "\n" + "man. no. of keywords per file: "
				+ maxNoKeywords + "\n" + "author assigned keywords in text: "
				+ Math.round(keywordRatio) + "%\n\n";

		Iterator<Integer> distriIter = keywordDistribution.keySet().iterator();
		while (distriIter.hasNext()) {
			int numberOfKeywords = (Integer) distriIter.next();
			reportString += numberOfKeywords + ";"
					+ keywordDistribution.get(numberOfKeywords) + "\n";
		}

		Utils.appendLine(reportFile, reportString);
	}

	/**
	 * computes the precision
	 * 
	 * @param authorKeywords
	 * @param extractedKeywords
	 * @return
	 */
	public static double computePrecision(List<String> authorKeywords,
			List<String> extractedKeywords) {
		double precision = 0.0;
		int a = 0;
		int b = 0;

		for (String keyword : extractedKeywords) {
			if (authorKeywords.contains(keyword)) {
				a++;
			}
		}

		b = extractedKeywords.size() - a;

		if ((a + b) > 0) {
			precision = (double) a / ((double) a + (double) b);
		}
		return precision;
	}

	/**
	 * computes the recall
	 * 
	 * @param authorKeywords
	 * @param extractedKeywords
	 * @return
	 */
	public static double computeRecall(List<String> authorKeywords,
			List<String> extractedKeywords) {
		double recall = 0.0;
		int a = 0;
		int c = 0;

		for (String keyword : extractedKeywords) {
			if (authorKeywords.contains(keyword)) {
				a++;
			}
		}

		c = authorKeywords.size() - a;

		if ((a + c) > 0) {
			recall = (double) a / ((double) a + (double) c);
		}
		return recall;
	}

	/**
	 * computes the f-measure
	 * 
	 * @param precision
	 * @param recall
	 * @return
	 */
	public static double computeFMeasure(double precision, double recall) {
		double fmeasure = 0.0;
		if (precision > 0 && recall > 0) {
			fmeasure = (2 * precision * recall) / (precision + recall);
		}
		return fmeasure;
	}

	/**
	 * 
	 * @param strings
	 * @return
	 */
	private static int getNumberOfStrings(List<String> strings) {
		int numberOfStrings = 0;

		String[] sub = null;
		for (String s : strings) {
			sub = s.split("\\W");
			numberOfStrings += sub.length;
		}
		return numberOfStrings;
	}

	/**
	 * computes the distribution of keywords
	 * 
	 * @param xmlFiles
	 * @return
	 */
	private static HashMap<Integer, Integer> getKeywordDistribution(
			List<File> xmlFiles, List<String> tags) {
		HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();

		List<String> authorKeywords = null;
		StringBuffer textBuffer = null;

		for (File xmlFile : xmlFiles) {
			authorKeywords = new ArrayList<String>();
			textBuffer = new StringBuffer();
			Utils.processFile(xmlFile, authorKeywords, textBuffer, tags);
			int numberOfKeywords = getNumberOfStrings(authorKeywords);
			int numberOfDocuments = 1;

			if (hash.containsKey(numberOfKeywords)) {
				numberOfDocuments += hash.get(numberOfKeywords);
			}

			hash.put(numberOfKeywords, numberOfDocuments);
		}

		return hash;
	}

}
