/*
 * @(#)Engine.java
 * 
 *  Description: A singleton class to extract urls out of a string.
 *  
 *  Created:     02.02.2006
 *  Author:	     Karsten Klueger
 *  mailto:	     karsten.klueger@medien.uni-weimar.de
 *
 *  Copyright Bauhaus-Universität Weimar
 *  Bauhausstraße 11
 *  99423 Weimar, Germany
 *  
 */
package de.aitools.keywordextraction.evaluation.collection.citeseer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;

/**
 * A singleton class to extract urls out of a string.
 * 
 * @author Karsten Klueger, BUW
 *
 */
public class Engine {
	
	private static Engine instance = null;
	
	private static final String RESULTPAGEURL_STARTPATTERN = "<!--RIS--><a href=\"";
	private static final String RESULTPAGEURL_ENDPATTERN = "\"";
	
	private static final String FILEURL_STARTPATTERN = "<a href=\"";
	private static final String FILEURL_ENDPATTERN = "\" onmouseover";
	
	private static final String FILENAME_DELIMITER = "qSq|/";
	
	public static final int RESPERPAGE = 20;
	
	/**
	 * Protected constructor to force use of 
	 * {@link #getExtractor(String type) getExtractor(String type)},
	 *
	 */
	protected Engine() {	
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static Engine getInstance() {
		if (instance == null) {
			instance = new Engine();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param content
	 * @return
	 */
	public Vector<URL> getResultPageUrls(String content) {
		Vector<URL> resultPageUrls = new Vector<URL>();
		Vector<String> urlStrings = this.getUrls(content, RESULTPAGEURL_STARTPATTERN, RESULTPAGEURL_ENDPATTERN);
		for(String urlString : urlStrings) {
			resultPageUrls.add(this.getUrl(urlString));
		}
		return resultPageUrls;
	}
	
	/**
	 * 
	 * @param content
	 * @param fileType
	 * @return
	 */
	public Vector<URL> getFileUrls(String content, String fileType) {
		Vector<URL> fileUrls = new Vector<URL>(); 
		String endPattern = fileType + FILEURL_ENDPATTERN;
		Vector<String> urlStrings = this.getUrls(content, FILEURL_STARTPATTERN , endPattern);
	
		if (urlStrings.size() > 0) {
			String urlString = urlStrings.get(0) + fileType;
			fileUrls.add(this.getUrl(urlString));
		}
		return fileUrls;
	}

	/**
	 * 
	 * @param searchString
	 * @return
	 */
	public URL getInitURL(String searchString) {
		String urlString = "http://citeseer.ist.psu.edu/cis?q=" + searchString
				+ "&submit=Search+Documents&cs=1";
		URL url;
		try {
			url = new URL(urlString);
			return url;
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param start
	 * @param searchString
	 * @return
	 */
	public URL getNextPageUrl(int start, String searchString) {

		String urlString = "http://citeseer.ist.psu.edu/cs?qb=dbnum%3D1"
				+ "%2Cstart%3D" + start
				+ "%2Cao%3DCitations%2Cam%3D20%2Caf%3DAny%2C"
				+ "qtype%3Ddocument:&q=" + searchString;

		URL url;
		try {
			url = new URL(urlString);
			return url;
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @param urlString
	 * @return
	 */
	public String getFileName(String urlString) {
		// get the last 5 chars from the url (could be e.g.  '.html' or '.pdf') 
		String fileType = urlString.substring(urlString.length() - 5);
		
		// get the extension
		fileType = fileType.substring(fileType.indexOf("."));
		
		String fileName = "";
		
		Scanner scanner = new Scanner(urlString).useDelimiter(FILENAME_DELIMITER);
		while (scanner.hasNext()) {
			String s = scanner.next();
			
			if(s.endsWith(fileType)) {
				fileName = "/" + s;
			}
		}
		return fileName;
	}

	/**
	 * 
	 * @param content
	 * @param startPattern
	 * @param endPattern
	 * @return
	 */
	private Vector<String> getUrls(String content, String startPattern,
			String endPattern) {
		Vector<String> urlStrings = new Vector<String>();
		Scanner scanner = new Scanner(content).useDelimiter(startPattern);
		while (scanner.hasNext()) {
			String s = scanner.next();
			if ((s.substring(0, 4).equalsIgnoreCase("http"))
					&& (s.contains(endPattern))) {
				urlStrings.add(s.substring(0, s.indexOf(endPattern)));
			}
		}
		return urlStrings;
	}

	/**
	 * 
	 * @param urlString
	 * @return
	 */
	private URL getUrl(String urlString) {		
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
	
}