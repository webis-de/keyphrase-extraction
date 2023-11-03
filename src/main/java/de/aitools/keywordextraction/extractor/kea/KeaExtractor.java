/*
 * @(#)KeaExtractor.java
 * 
 * Description: This class is a wrapper class for the KEA keyword extractor. It 
 *              implents the KeywordExtractor interface to enable the construction
 *              of new instances of KEA using the KeywordExtractorFactory class.
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
package de.aitools.keywordextraction.extractor.kea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

import de.aitools.keywordextraction.extractor.KeywordExtractor;

/**
 * This class is a wrapper class for the KEA keyword extractor. It implents the
 * KeywordExtractor interface to enable the construction of new instances of KEA
 * using the KeywordExtractorFactory class.
 * 
 * @author Karsten Klueger, BUW
 * @version 1.0
 * 
 */
public abstract class KeaExtractor extends KeywordExtractor {
	
	private static final String TMPFILENAME = "tmp";
	
	private static final String MODELPATH = "/de/aitools/keywordextraction/extractor/kea/model/";
	
	protected String model;

	private String tmpDir;

	private KEAKeyphraseExtractor kea;

	/**
	 * 
	 *
	 */
	protected KeaExtractor() {
		super();
		this.kea = new KEAKeyphraseExtractor();
		this.kea.setEncoding("default");
		try {
			File tmpFile = File.createTempFile("kea", "txt");
			this.tmpDir = tmpFile.getAbsolutePath(); // FilenameUtils.getFullPath(tmpFile.getAbsolutePath());
			this.kea.setDirName(this.tmpDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	protected Vector<String> extract(String text, int numberOfKeywords) {
	
		// set language dependend model path
		String modelPath = System.getProperty("user.dir") + MODELPATH + this.language + "/";
		
		// set the model to use
		this.kea.setModelName(modelPath + this.model);
		
		this.kea.setNumPhrases(numberOfKeywords);
		// create tmpfile
		this.createTmpFile(TMPFILENAME, text);
		// extract keywords		
		Vector<String> keywords = this.extractKeywords();
		// delete tmpFiles
		this.deleteTmpFiles();
	
		return keywords;
	}

	/**
	 * Since Kea uses text files to extract keywords from, this method creates
	 * such a temp file. 
	 * 
	 * @param fileName
	 * @param textBuffer
	 */
	private void createTmpFile(String fileName, String text) {

		File tmpFile = new File(this.tmpDir + fileName + ".txt");		
		
		try {
			BufferedWriter textWriter = new BufferedWriter(
					new FileWriter(tmpFile));
			textWriter.write(text);
			textWriter.flush();
			textWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Delete temporary files.
	 *
	 */
	private void deleteTmpFiles() {
		File keyFile = new File(this.tmpDir + TMPFILENAME + ".key");		
		keyFile.delete();
		File tmpFile = new File(this.tmpDir + TMPFILENAME + ".txt");
		tmpFile.delete();
	}
	
	/**
	 * Starts the Kea extractor.
	 * 
	 * @return
	 */
	private Vector<String> extractKeywords() {
		Vector<String> keywords = new Vector<String>();		
		
		// extract keywords with kea
		try {
			kea.loadModel();
			kea.extractKeyphrases(kea.collectStems());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		// get result from file		
		try {
			String keyword = ""; 
			File resultFile = new File(this.tmpDir + "/" + TMPFILENAME + ".key");
			Reader in = new FileReader(resultFile);
			BufferedReader inReader = new BufferedReader(in);
			while((keyword = inReader.readLine()) != null) {
				keywords.add(keyword);
			}
			in.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		
		return keywords;
	}	
}