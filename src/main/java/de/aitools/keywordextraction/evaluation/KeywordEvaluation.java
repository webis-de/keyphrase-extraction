package de.aitools.keywordextraction.evaluation;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import de.aitools.keywordextraction.KeywordExtractorFactory;
import de.aitools.keywordextraction.extractor.KeywordExtractor;

/**
 * 
 * @author Karsten
 * 
 */
public class KeywordEvaluation {

	// input parameters
	private static final String TASK = "-t";

	private static final String TASK_TEST = "test";

	private static final String TASK_CREATE_FEATUREFILES = "cf";

	private static final String TASK_CREATE_KEATRAININGFILES = "ck";

	private static final String TASK_GET_COLLECTION_STATISTICS = "cs";

	private static final String PATH = "-p";

	private static final String MAXNUMBEROFKEYWORDS = "-nmax";

	private static final String MINNUMBEROFKEYWORDS = "-nmin";

	private static final String STEP = "-step";

	private static final String USE_COOCCURRENCE_EXTRACTOR = "-cooc";

	private static final String USE_KEA_EXTRACTOR_CSTR = "-keacstr";

	private static final String USE_KEA_EXTRACTOR_LONG = "-keal";
	
	private static final String USE_KEA_EXTRACTOR_SHORT = "-keas";

	private static final String USE_RSPEXTRACTORTF = "-rsptf";

	private static final String USE_RSPEXTRACTORFO = "-rspfo";

	private static final String USE_BCEXTRACTORFO = "-bcfo";

	private static final String USE_BCEXTRACTORTF = "-bctf";

	private static final String USE_CONTEXTEXTRACTOR = "-context";

	private static final String CREATE_LONG_REPORT = "-longrep";
	
	private static final String TAGS = "-tags";

	//	
	private static final String RECALL = "recall";

	private static final String PRECISION = "precision";

	private static final String FMEASURE = "f-measure";

	private static final String AUTHOR_KEYWORDS = "Author assigned";

	private static final Locale DEFAULT_LANGUAGE = Locale.ENGLISH;

	private static final String LOGFILENAME = "_report";

	private static final String REPORT_SEPARATOR = ";";

	// 
	private File collectionDir;

	private int maxNumberOfKeywords;

	private int minNumberOfKeywords;

	private int step;

	private String task;

	private HashMap<String, KeywordExtractor> extractors;

	private HashMap<String, HashMap<String, Double>> avValues;

	private List<String> usedValues;

	private int numberOfTestFiles;

	private File logfile;

	private boolean longReport = false;
	
	private List<String> tags;

	/**
	 * 
	 * 
	 */
	public KeywordEvaluation() {
		this.extractors = new HashMap<String, KeywordExtractor>();
		this.usedValues = new ArrayList<String>();
		this.tags = new ArrayList<String>();

		this.numberOfTestFiles = 0;

		// default number of keywords
		this.maxNumberOfKeywords = 5;
		this.minNumberOfKeywords = 5;
		this.step = 1;
	}

	/**
	 * 
	 * @param extractor
	 */
	private void addExtractor(KeywordExtractor extractor) {
		this.extractors.put(extractor.getName(), extractor);
	}

	/**
	 * 
	 * 
	 */
	private void startTest() {

		this.createLogfile();
		this.writeReportHead();
		this.initAvValuesHash();

		// start test for each file of the test collection
		List<File> testFiles = Utils.getAllFiles(this.collectionDir, "xml");
		this.numberOfTestFiles = testFiles.size();
		int fileCount = 1;
		for (File file : testFiles) {
			System.out.println("processing file" + " " + fileCount + "/"
					+ this.numberOfTestFiles + ": " + file.getName());

			if (this.longReport) {
				// write filename into report file
				this.report(file.getName() + "\n\n");
			}

			// get keywords
			HashMap<String, List<String>> fileResult = this
					.extractKeywords(file);

			// write results into a report file
			this.getResults(fileResult);

			fileCount++;
		}

		// compute sumary write summary to file
		this.generateSummary();
	}

	/**
	 * 
	 * 
	 */
	private void createLogfile() {
		this.logfile = new File(this.collectionDir.getAbsolutePath() + "/"
				+ LOGFILENAME + "_" + this.minNumberOfKeywords + "-"
				+ this.maxNumberOfKeywords + ".csv");
	}

	/**
	 * 
	 * 
	 */
	private void writeReportHead() {
		this
				.report("\n\n*********************************************************\n"
						+ "*\n"
						+ "*   keyword extraction test\n"
						+ "*\n"
						+ "*   start at : " + new Date() + "\n" + "*\n\n");

		this.report("no. of keywords to extract:\n");
		this.report(" max: " + this.maxNumberOfKeywords + "\n");
		this.report(" min: " + this.minNumberOfKeywords + "\n\n");
	}

	/**
	 * 
	 * @param text
	 */
	private void report(String text) {
		Utils.appendText(this.logfile, text);
	}

	/**
	 * initialize the HashMap avValues
	 * 
	 * HashMap layout: {('extractorName', HashMap{('precision', double
	 * value), ('recall', double value), ('f-measure, double value) }, ... }
	 * 
	 */
	private void initAvValuesHash() {

		this.avValues = new HashMap<String, HashMap<String, Double>>();
		Iterator<String> extractorsIter = this.extractors.keySet().iterator();
		while (extractorsIter.hasNext()) {
			HashMap<String, Double> tempHash = new HashMap<String, Double>();

			//
			for (int i = this.maxNumberOfKeywords; i >= this.minNumberOfKeywords; i = i
					- this.step) {
				String suffix;
				if (i < 10) {
					suffix = "0" + i;
				} else {
					suffix = Integer.toString(i);
				}
				tempHash.put(PRECISION + suffix, 0.0);
				tempHash.put(RECALL + suffix, 0.0);
				tempHash.put(FMEASURE + suffix, 0.0);
				if (!this.usedValues.contains(PRECISION + suffix)) {
					this.usedValues.add(PRECISION + suffix);
				}
				if (!this.usedValues.contains(RECALL + suffix)) {
					this.usedValues.add(RECALL + suffix);
				}
				if (!this.usedValues.contains(FMEASURE + suffix)) {
					this.usedValues.add(FMEASURE + suffix);
				}
			}
			this.avValues
					.put(extractorsIter.next(), tempHash);
		}

		Collections.sort(this.usedValues);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	private HashMap<String, List<String>> extractKeywords(File file) {
		HashMap<String, List<String>> keywordHash = new HashMap<String, List<String>>();

		List<String> authorKeywords = new ArrayList<String>();

		// clear textBuffer before used in handler
		StringBuffer textBuffer = new StringBuffer();

		// load and parse the test xml file and fill the textBuffer and the
		// keywords assigned by the author
		Utils.processFile(file, authorKeywords, textBuffer, tags);

		// /////////////////////////////////////////////////
//		String report = "";
//		for (String s : authorKeywords)
//			report += s + " ";
//		File f;
//		int i = 1;
//		while ((f = new File("Z:/transfer/" + i + "_authKw" + ".csv")).exists())
//			i++;
//		Utils.appendLine(f, report);
		// /////////////////////////////////////////////////

		// get the keywords from the author
		keywordHash.put(AUTHOR_KEYWORDS, authorKeywords);

		// get the keywords from each extractor
		Iterator<String> extractorsIter = this.extractors.keySet().iterator();
		while (extractorsIter.hasNext()) {
			String method = extractorsIter.next();
			keywordHash.put(method, this.extractors.get(method).getSingleWords(
					textBuffer.toString(), this.maxNumberOfKeywords));
		}

		return keywordHash;
	}

	/**
	 * 
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	private void getResults(HashMap<String, List<String>> result) {
		
		List<String> extractionTypes = new ArrayList<String>();

		// sort extractors in alphabetical order
		Iterator<String> keys = result.keySet().iterator();
		while (keys.hasNext()) {
			extractionTypes.add(keys.next());
		}
		Collections.sort(extractionTypes);

		//
		for (String extractionType : extractionTypes) {

			if (this.longReport) {
				// write the name of the algorithm into report file
				if (extractionType.equalsIgnoreCase(AUTHOR_KEYWORDS)) {
					this.report(" " + extractionType + "\n ");
				} else {
					this.report(" "
							+ FilenameUtils.getExtension(this.extractors.get(
									extractionType).getName()) + "\n ");
				}
			}

			// for each number of keywords from MAXNUMBEROFKEYWORDS to
			// MINNUMBEROFKEYWORDS
			for (int i = this.maxNumberOfKeywords; i >= this.minNumberOfKeywords; i = i
					- this.step) {
				String suffix;
				if (i < 10) {
					suffix = "0" + i;
				} else {
					suffix = Integer.toString(i);
				}

				// get the ectracted keywords
				List<String> keywords = new ArrayList<String>(result.get(extractionType));
				if (keywords.size() > i) {
					keywords.subList(i, keywords.size()).clear();
				}

				if (this.longReport) {
					// write keywords for algorithm into report file
					for (String keyword : keywords) {
						this.report(keyword + ", ");
					}
					this.report("\n\n");
				}

				List<String> authorKeywords = result.get(AUTHOR_KEYWORDS);

				// convert keywords to compare (stemming and filtering)
				authorKeywords = Utils.convertToSingleStems(authorKeywords,
						DEFAULT_LANGUAGE);
				keywords = Utils.convertToSingleStems(keywords,
						DEFAULT_LANGUAGE);

				// uncomment if number of keywords should be eqal to author
				// keywords				
//				keywords.setSize(Math.min(authorKeywords.size(), keywords
//						.size()));
//				while(keywords.size() < authorKeywords.size()) {
//					keywords.add("xxxxxx");
//				}				
				
//				System.out.println("number of author keywords = "
//						+ authorKeywords.size());
//				System.out.println("number of extracted keywords = "
//						+ keywords.size());

				// compute relation parameters to compare algorithms
				if (!extractionType.equalsIgnoreCase(AUTHOR_KEYWORDS)) {

					if (this.longReport) {
						this.report("  a:" + authorKeywords.toString() + "\n");
						this.report("  e:" + keywords.toString() + "\n");
					}

					// compute precision, recall and f-messure
					double precision = Statistics.computePrecision(
							authorKeywords, keywords);

					double recall = Statistics.computeRecall(authorKeywords,
							keywords);

					double fmeasure = Statistics.computeFMeasure(precision,
							recall);

					// store to compute average value
					this.addToAverageValues(extractionType, PRECISION + suffix,
							precision);
					this.addToAverageValues(extractionType, RECALL + suffix,
							recall);
					this.addToAverageValues(extractionType, FMEASURE + suffix,
							fmeasure);

					if (this.longReport) {
						// write to file
						this.report("\n  " + PRECISION + " = " + precision);
						this.report(", " + RECALL + " = " + recall);
						this
								.report(", " + FMEASURE + " = " + fmeasure
										+ "\n\n");
					}

				}
			}
		}

		if (this.longReport) {
			this
					.report("--------------------------------------------------------------------------------\n\n");
		}
	}

	/**
	 * 
	 * @param extractionType
	 * @param valueType
	 * @param value
	 */
	private void addToAverageValues(String extractionType, String valueType,
			double value) {
		double tempValue = this.avValues.get(extractionType).get(valueType)
				.floatValue();
		this.avValues.get(extractionType).put(valueType, tempValue + value);
	}

	/**
	 * generate average values of precision, recall and f-measure and write to a
	 * report file
	 * 
	 */
	private void generateSummary() {
		List<String> extractionTypes = new ArrayList<String>();

		this.report("\n\nSUMMARY:\n");

		// get used values
		this.report("Extractor");
		for (String value : this.usedValues) {
			this.report(REPORT_SEPARATOR + value);
		}
		this.report("\n");

		// sort results in alphabetical order
		Iterator<String> extractionTypeIter = this.avValues.keySet().iterator();
		while (extractionTypeIter.hasNext()) {
			extractionTypes.add(extractionTypeIter.next().toString());
		}
		Collections.sort(extractionTypes);

		// write results for every extractor to file
		for (String extractionType : extractionTypes) {
			this.report(FilenameUtils.getExtension(this.extractors.get(
					extractionType).getName())
					+ REPORT_SEPARATOR);

			HashMap<String, Double> extractionTypeHash = this.avValues
					.get(extractionType);

			Iterator<String> valueTypeIter = extractionTypeHash.keySet().iterator();
			List<String> keys = new ArrayList<String>();
			while (valueTypeIter.hasNext()) {
				keys.add(valueTypeIter.next().toString());
			}

			Collections.sort(keys);

			DecimalFormat df = new DecimalFormat("#0.000");

			for (String valueType : keys) {
				double value = extractionTypeHash.get(valueType)
						/ this.numberOfTestFiles;
				this.report(df.format(Math.rint(value * 1000) / 1000)
						+ REPORT_SEPARATOR);
			}
			this.report("\n");
		}
	}

	/**
	 * 
	 * @param args
	 */
	private void setOptions(String[] args) {

		for (int i = 0; i < args.length; i++) {

			// path to collection
			if (args[i].equals(PATH)) {
				i++;
				if (i >= args.length) {
					usage();
				}
				this.collectionDir = new File(args[i]);
			}

			// use cooccurrence extractor
			else if (args[i].equals(USE_COOCCURRENCE_EXTRACTOR)) {
				this.addExtractor(KeywordExtractorFactory
						.getCoocurrenceExtractor());
			}

			// use kea extractor cstr
			else if (args[i].equals(USE_KEA_EXTRACTOR_CSTR)) {
				this.addExtractor(KeywordExtractorFactory
								.getKeaExtractorCSTR());
			}

			// use kea extractor long
			else if (args[i].equals(USE_KEA_EXTRACTOR_LONG)) {
				this.addExtractor(KeywordExtractorFactory
						.getKeaExtractorLong());
			}			

			// use kea extractor short
			else if (args[i].equals(USE_KEA_EXTRACTOR_SHORT)) {
				this.addExtractor(KeywordExtractorFactory
								.getKeaExtractorShort());
			}

			// use rsp extractor tf
			else if (args[i].equals(USE_RSPEXTRACTORTF)) {
				this.addExtractor(KeywordExtractorFactory.getRSPExtractorTF());
			}

			// use rsp extractor fo
			else if (args[i].equals(USE_RSPEXTRACTORFO)) {
				this.addExtractor(KeywordExtractorFactory.getRSPExtractorFO());
			}

			// use bc extractor tf
			else if (args[i].equals(USE_BCEXTRACTORTF)) {
				this.addExtractor(KeywordExtractorFactory.getBCExtractorTF());
			}

			// use bc extractor fo
			else if (args[i].equals(USE_BCEXTRACTORFO)) {
				this.addExtractor(KeywordExtractorFactory.getBCExtractorFO());
			}

			// use context extractor
			else if (args[i].equals(USE_CONTEXTEXTRACTOR)) {
				this
						.addExtractor(KeywordExtractorFactory
								.getContextExtractor());
			}

			// max. number of keywords
			else if (args[i].equals(MAXNUMBEROFKEYWORDS)) {
				i++;
				this.maxNumberOfKeywords = Integer.parseInt(args[i]);
			}

			// min. number of keywords
			else if (args[i].equals(MINNUMBEROFKEYWORDS)) {
				i++;
				this.minNumberOfKeywords = Integer.parseInt(args[i]);
			}

			// step from max to min keywords
			else if (args[i].equals(STEP)) {
				i++;
				this.step = Integer.parseInt(args[i]);
			}

			// task
			else if (args[i].equals(TASK)) {
				i++;
				this.task = args[i];
			}

			// report
			else if (args[i].equals(CREATE_LONG_REPORT)) {
				this.longReport = true;
			}

			// tags
			else if (args[i].equals(TAGS)) {
				i++;
				String tags = args[i];
				for(int j = 0; j < tags.length(); j++) {
					String t = String.valueOf(tags.charAt(j));
					if(t.equalsIgnoreCase("t")) {
						this.tags.add("text");
					}
					else if (t.equalsIgnoreCase("a")) {
						this.tags.add("abstract");
					}
				}
			}

			else {
				usage();
			}
		}

	}

	/**
	 * This will print the usage requirements and exit.
	 */
	private static void usage() {
		System.err
				.println("Usage: java aitools.keywordextraction.evaluation.KeywordEvaluation");
		System.exit(1);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		KeywordEvaluation kwe;

		kwe = new KeywordEvaluation();
		kwe.setOptions(args);
		if (kwe.task.equalsIgnoreCase(TASK_TEST)) {
			kwe.startTest();
		} else if (kwe.task.equalsIgnoreCase(TASK_CREATE_FEATUREFILES)) {
			Utils.createFeatureFile(kwe.collectionDir, DEFAULT_LANGUAGE);
		} else if (kwe.task.equalsIgnoreCase(TASK_CREATE_KEATRAININGFILES)) {
			Utils.createKeaTrainingFiles(kwe.collectionDir, kwe.tags);
		} else if (kwe.task.equalsIgnoreCase(TASK_GET_COLLECTION_STATISTICS)) {
			Statistics.getCollectionStatistics(kwe.collectionDir, kwe.tags);
		}

		System.out.println("done!");
	}

}