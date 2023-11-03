/*
 * @(#)Utils.java
 * 
 *  Description: These methods are used in the aitools.keywordextrtaction framework.
 *  
 *  Created:     08.02.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universit�t Weimar
 *  Bauhausstra�e 11
 *  99423 Weimar � Germany
 *  
 */
package de.aitools.keywordextraction.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.aitools.stemming.SnowballStemmer;

/**
 * These methods are used in the aitools.keywordextrtaction framework.
 * 
 * @author Karsten Klueger, BUW
 * 
 */
public class Utils {

	// delimiter
	private static final String DELIMITER = " .?!,;()<>-_:+#�`\t\\}][{\"�$%&/)\n";

	/**
	 * Protected constructor to prevent an instantiation.
	 * 
	 */
	protected Utils() {

	}

	/**
	 * Appends text in a new text line at the end of a file. If the file doesn't
	 * exist it will be created.
	 * 
	 * @param file
	 *            The target file.
	 * @param text
	 *            The text to append.
	 */
	public static synchronized void appendLine(File file, String text) {
		appendText(file, text + "\n");
	}

	/**
	 * Appends the text at the end of a file. If the file doesn't exist it will
	 * be created.
	 * 
	 * @param file
	 *            The target file.
	 * @param text
	 *            The text to append.
	 */
	public static synchronized void appendText(File file, String text) {
		RandomAccessFile rafile = null;
		try {
			rafile = new RandomAccessFile(file.getAbsolutePath(), "rw");
			rafile.seek(rafile.length());
			rafile.writeBytes(text);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (rafile != null) {
				try {
					rafile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns a Vector containing a string for every line in a file.
	 * 
	 * @param file
	 *            The file to get the text from.
	 * @return A set of all line strings.
	 */
	public static List<String> getLines(File file) {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			for (String line; (line = reader.readLine()) != null;) {
				lines.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return lines;
	}

	/**
	 * Parses a XML file considering the following tags:
	 * - "abstract" to get only the abstract
	 * - "text" to get only the text (doesn't include the abtract)
	 *  
	 * The delivered Vector "authorKeywords" and the "StringBuffer" 
	 * textBuffer are used to return the result of the parsing process.
	 * 
	 * @param file
	 * @param authorKeywords
	 * @param textBuffer
	 * @param tags
	 */
	public static void processFile(File file, List<String> authorKeywords,
			StringBuffer textBuffer, List<String> tags) {

		// SAX-Handler for document import that fills the textBuffer and the
		DefaultHandler handler = new SAXImportHandler(authorKeywords,
				textBuffer, tags);

		// load and parse the test xml file and fill the textBuffer and the
		// keywords assigned by the author
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			sp.parse(file, handler);
		} catch (ParserConfigurationException e) {
			System.out.println("on " + file.getName());
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("on " + file.getName());
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println("on " + file.getName());
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Returns all files from a directory in regard to the given extension.
	 * 
	 * @param directory
	 * @param extension
	 * @return Returns a Vector of files
	 */
	public static List<File> getAllFiles(File directory, String extension) {
		List<File> files = new ArrayList<File>();

		String path = directory.getAbsolutePath() + "/";

		String[] fileNames = directory.list(FileFilterUtils
				.suffixFileFilter("." + extension));

		for (String fileName : fileNames) {
			files.add(new File(path + fileName));
		}

		return files;
	}

	/**
	 * Returns a file from a directory in regard to the given extension and file number.
	 * 
	 * @param directory
	 * @param extension
	 * @param fileNumber
	 * @return
	 */
	public static File getFile(File directory, String extension, int fileNumber) {

		String path = directory.getAbsolutePath() + "/";

		String[] fileNames = directory.list(FileFilterUtils
				.suffixFileFilter("." + extension));

		return new File(path + fileNames[fileNumber]);
	}

	/**
	 * Normalize a vector of doubles
	 * 
	 * @param vector
	 */
	public static List<Double> getNormalizeVector(List<Double> vector) {
		List<Double> normalizedVector = new ArrayList<Double>();

		double squareSum = 0;
		for (int i = 0; i < vector.size(); i++) {
			squareSum += vector.get(i) * vector.get(i);
		}

		if (squareSum != 0) {
			double factor = (double) 1 / (double) Math.sqrt(squareSum);
			for (int i = 0; i < vector.size(); i++) {
				normalizedVector.add(factor * vector.get(i));
			}
			return normalizedVector;
		}

		return vector;
	}

	/**
	 * 
	 * 
	 */
	public static void createKeaTrainingFiles(File dir, List<String> tags) {
		List<String> keywords;

		StringBuffer text;
		File keywordFile;
		File textFile;
		for (File file : getAllFiles(dir, "xml")) {

			keywords = new ArrayList<String>();
			keywordFile = new File(dir.getAbsolutePath() + "/"
					+ FilenameUtils.getBaseName(file.getName()) + ".key");

			textFile = new File(dir.getAbsolutePath() + "/"
					+ FilenameUtils.getBaseName(file.getName()) + ".txt");
			text = new StringBuffer();

			processFile(file, keywords, text, tags);

			for (String keyword : keywords) {
				appendLine(keywordFile, keyword);
			}

			appendText(textFile, text.toString());
		}
	}

	/**
	 * Converts a vector of strings containing multiple words into a vector of
	 * single word stemms sorted in alphabetical order.
	 * 
	 * @param keywords
	 * @param numberOfKeywordStemms
	 * @param language
	 * @return
	 */
	public static List<String> convertToSingleStems(List<String> keywords,
			Locale language) {
		List<String> out = new ArrayList<String>();

		SnowballStemmer stemmer = new SnowballStemmer(language);
		for (String keyword : keywords) {
			StringTokenizer keywordST = new StringTokenizer(keyword, DELIMITER);
			while (keywordST.hasMoreTokens()) {
				String singleWord = keywordST.nextToken().toLowerCase();
				String stemm = stemmer.stem(singleWord);
				if (!out.contains(stemm)) {
					out.add(stemm);
				}
			}
		}

		// sort in alphabethical order
		// Collections.sort(out);

		return out;
	}

	/**
	 * 
	 * @param dir
	 * @param language
	 */
	public static void createFeatureFile(File dir, Locale language) {
		List<List<Double>> overAllFeatures = new ArrayList<List<Double>>();

		List<String> tags = new ArrayList<String>();
		tags.add("text");
		tags.add("abstract");

		// output
		StringBuffer doc;

		String separator = "\t";

		DecimalFormat df = new DecimalFormat("0.000000");
		df.setMaximumFractionDigits(10);

		for (File file : getAllFiles(dir, "xml")) {
			// input
			doc = new StringBuffer();
			List<String> authorKeywords = new ArrayList<String>();
			processFile(file, authorKeywords, doc, tags);
			Map<String, List<Double>> matrix = getDocumentFeatureVector(
					doc.toString(), authorKeywords, language);

			// add to over all vector
			Iterator<String> keys = matrix.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				List<Double> featureVector = matrix.get(key);
				overAllFeatures.add(featureVector);
			}
		}

		// create output
		doc = new StringBuffer();
		doc.append("Klasse" + separator + "Frequenz" + separator + "Distance"
				+ "\n");
		for (List<Double> featureVector : overAllFeatures) {
			doc.append(featureVector.get(0).intValue() + separator
					+ df.format(featureVector.get(1)) + separator
					+ df.format(featureVector.get(2)) + "\n");
		}

		// create report file
		File reportFile = new File(FilenameUtils.getFullPath(dir
				.getAbsolutePath())
				+ FilenameUtils.getBaseName(dir.getName()) + ".spss");
		appendText(reportFile, doc.toString());

	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	private static Map<String, List<Double>> getDocumentFeatureVector(
			String document, List<String> keywords, Locale language) {

		Map<String, List<Double>> hash = new HashMap<String, List<Double>>();
		SnowballStemmer stemmer = new SnowballStemmer(language);
		Scanner scanner = null;
		List<Double> featureVector = null;
		List<String> singleKeywords = new ArrayList<String>();

		// create single keywords
		for (String authorKeyword : keywords) {
			scanner = new Scanner(authorKeyword).useDelimiter(" ");
			while (scanner.hasNext()) {
				String word = stemmer.stem(scanner.next());
				singleKeywords.add(word);
			}
		}

		// get features for every single word from document
		scanner = new Scanner(document).useDelimiter(" |\\p{Punct}|\\n|\\d");
		int pos = 0;

		while (scanner.hasNext()) {
			String word = stemmer.stem(scanner.next());

			if (word.length() > 0) {
				pos++;
				featureVector = new ArrayList<Double>();

				if (hash.containsKey(word)) {
					double tf = hash.get(word).get(0) + (double) 1;
					double fo = hash.get(word).get(1);
					featureVector.add(tf);
					featureVector.add(fo);
				} else {
					featureVector.add((double) 1);
					featureVector.add((double) pos);
				}
				hash.put(word, featureVector);
			}
		}

		// compute relative values
		Iterator<String> hashIter = hash.keySet().iterator();
		while (hashIter.hasNext()) {
			String word = hashIter.next();
			featureVector = hash.get(word);
			double tf = featureVector.get(0) / (double) pos;
			double fo = featureVector.get(1) / (double) pos;
			featureVector.set(0, tf);
			featureVector.set(1, fo);
		}

		// normalize vector and add keyword feature
		Map<String, List<Double>> normalizedHash = new HashMap<String, List<Double>>();

		Iterator<String> keysIter = hash.keySet().iterator();
		while (keysIter.hasNext()) {
			String key = keysIter.next();
			// featureVector = getNormalizeVector(hash.get(key)); //
			// normalization
			featureVector = hash.get(key);
			if (singleKeywords.contains(key)) {
				featureVector.add(0, (double) 1); // is keyword
			} else {
				featureVector.add(0, (double) 0); // is no keyword
			}
			normalizedHash.put(key, featureVector);
		}

		return normalizedHash;
	}

}
