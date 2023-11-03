/**
 * This is a short example showing how to use the keyword extraction.
 * 
 */
package de.aitools.keywordextraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import de.aitools.keywordextraction.extractor.KeywordExtractor;

/**
 * @author Karsten Klueger, BUW
 * 
 * 
 */
public class Example {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		int numberOfKeywordsToExtract = 5;
		int fileNumber = 9; // the i-th document from the collection dir

		String filename = "/home/smartie/study/picapica/keywordextraction/gpl-2.0.txt";
//		String fileExtension = "txt";

		// parts of the input file to extract keywords from
//		Vector<String> tags = new Vector<String>();
//		tags.add("abstract");
//		tags.add("text");
//		for(int i = 0; i < 14; i++) {
		    // get the input file
//			File testFile = Utils.getFile(new File(collectionDir), fileExtension, i);
	
			File testFile = new File(filename);
		
			StringBuffer text = new StringBuffer();
	//		Vector<String> actualKeywords = new Vector<String>();
	
			// parse the input file and return the text and the actual keywords
	//		Utils.processFile(testFile, actualKeywords, text, tags);
			BufferedReader br = new BufferedReader(new FileReader(testFile));
			String line = null;
			while((line = br.readLine()) != null) {
				text.append(line);
				text.append("\n");
			}
	
			// get an instance of a KeywordExtractor
			KeywordExtractor extractor = KeywordExtractorFactory.getKeaExtractorCSTR();
			// extract new keywords
			List<String> extractedKeywords = extractor.getPhrases(text.toString(), numberOfKeywordsToExtract);
	
			// output
	//		System.out.println("extracted keywords from document:/n" + " "
	//				+ testFile.getName());
	//				+ "/n/n actual keywords:/n ----------------");
	//		for (String actualKeyword : actualKeywords) {
	//			System.out.println(" " + actualKeyword);
	//		}
	//		System.out.println("/n/n extracted keywords:/n -------------------");
			for (String extractedKeyword : extractedKeywords) {
				System.out.println(" " + extractedKeyword);
			}
//		}
	}

}
