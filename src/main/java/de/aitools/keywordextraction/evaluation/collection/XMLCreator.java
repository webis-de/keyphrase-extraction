///*
// * @(#)XMLCreator.java
// * 
// *  Description: 
// *  
// *  Created:     2006-02-07
// *  Author:	     Karsten Klueger
// *  mailto:	     karsten.klueger@medien.uni-weimar.de
// *
// *  Copyright Bauhaus-Universität Weimar
// *  Bauhausstraße 11
// *  99423 Weimar, Germany
// *  
// */
//package de.aitools.keywordextraction.evaluation.collection;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.util.Scanner;
//
//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.XMLStreamWriter;
//
//import org.apache.commons.io.FilenameUtils;
//
//import de.aitools.keywordextraction.evaluation.Utils;
//
///**
// * 
// * @author Karsten Klueger, BUW
// * 
// */
//public class XMLCreator {
//
//	private static final String LOGFILENAME = "_message.txt";
//
//	private Scanner textScan;
//
//	private boolean success;
//	
//	private boolean warning;
//
//	private String fileName;
//
//	private String logPath;
//
//	/**
//	 * 
//	 * @param fileName
//	 * @param text
//	 * @return
//	 */
//	public boolean generate(File xmlFile, String text) {
//
//		this.fileName = xmlFile.getName();
//		this.logPath = FilenameUtils.getFullPath(xmlFile.getAbsolutePath());
//		this.success = true;
//		this.warning = false;
//
//		String keywordString = this.getKeywordSubstring(text);		
//		String keywords = this.prepareKeywords(keywordString);
//		String mainText = this.getText(text, keywordString);
//
//		if (this.success) {
//			XMLOutputFactory factory = XMLOutputFactory.newInstance();
//			XMLStreamWriter w;
//			try {
//				w = factory
//						.createXMLStreamWriter(new FileOutputStream(xmlFile));
//				w.writeStartDocument("iso-8859-1", "1.0");
//				w.writeStartElement("document");
//				w.writeStartElement("keywords");
//				w.writeCharacters(keywords.toCharArray(), 0, keywords.length());
//				w.writeEndElement();
//				w.writeStartElement("text");
//				w.writeCharacters(mainText.toCharArray(), 0, mainText.length());
//				w.writeEndElement();
//				w.writeEndElement();
//				w.writeEndDocument();
//				w.flush();
//				w.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//				System.err.println(e.getMessage());
//			} catch (XMLStreamException e) {
//				e.printStackTrace();
//				System.err.println(e.getMessage());
//			}
//		}
//
//		return this.success;
//	}
//
//	/**
//	 * 
//	 * @param text
//	 * @return
//	 */
//	private String getKeywordSubstring(String text) {
//		this.textScan = new Scanner(text);
//		boolean appendable = false;
//
//		StringBuffer textBuffer = new StringBuffer();
//		while (this.textScan.hasNextLine()) {
//			String line = textScan.nextLine();
//
//			if (this.isNewElementBeginning("keywords", line)) {
//				appendable = true;
//			} else if (this.isNewElementBeginning("key words", line)) {
//				appendable = true;
//			} else if (this.isNewElementBeginning("additional keywords", line)) {
//				appendable = true;
//			} else if (this.isNewElementBeginning("additional key words", line)) {
//				appendable = true;
//			} else if (this.isNewElementBeginning("introduction", line)) {
//				appendable = false;
//				break;
//			} else if (this.isNewElementBeginning("acknowledgments", line)) {
//				appendable = false;
//				break;
//			} else if (this.isNewElementBeginning("abstract", line)) {
//				appendable = false;
//			}
//
//			if (appendable) {
//				textBuffer.append(line + "\n");
//			}
//		}
//
//		return textBuffer.toString();
//	}
//
//	/**
//	 * 
//	 * @param text
//	 * @param keywords
//	 * @return
//	 */
//	private String getText(String text, String keywords) {
//		if(this.warning) {
//			return text;
//		}
//		StringBuffer textBuffer = new StringBuffer(text);
//		int start = textBuffer.indexOf(keywords,0);
//		int end = start + keywords.length();
//		textBuffer.delete(start, end);
//		return textBuffer.toString();
//	}
//
//	/**
//	 * 
//	 * @param keywords
//	 * @return
//	 */
//	private String prepareKeywords(String keywords) {
//		// prepare return string
//		if (keywords.length() > 0) {
//			keywords = keywords.replace("keywords", "");
//			keywords = keywords.replace("key words", "");
//			keywords = keywords.replace("additional keywords", "");
//			keywords = keywords.replace("additional key words", "");
//			keywords = keywords.replace(":", "");
//			keywords = keywords.replace("1", "");
//			keywords = keywords.replace(".", "");
//			keywords = keywords.replaceAll("\\p{Punct}^-", ",");
//
//			this.textScan = new Scanner(keywords);
//
//			// check number of words: if number of words > 20
//			// keywords may be wrong
//			int wordcount = 0;
//			while (textScan.hasNext()) {
//				textScan.next();
//				wordcount++;
//			}
//			if (wordcount > 20) {
//				Utils.appendLine(new File(this.logPath + LOGFILENAME),
//						this.fileName + ": please check keywords manually.");
//				this.warning = true;
//				// delete files 
//				this.success = false;
//			}
//		} else {
//			this.success = false;
//		}
//
//		return keywords.trim();
//	}
//
//	/**
//	 * 
//	 * @param element
//	 * @param line
//	 * @return true if current line is beginning of a new element
//	 */
//	private boolean isNewElementBeginning(String element, String line) {
//		// trim if possible
//		line = line.trim();
//	
//		// a beginnig of a new element mus contain the element string
//		if (line.contains(element)) {
//	
//			// the element is an extra line (may include numbering)
//			if (line.substring(line.length() - element.length(), line.length())
//					.equals(element)) {
//				return true;
//			}
//	
//			// if the element is part of a line then it must be in the fisrt
//			// part of this line (may include numbering)
//			int minlen = Math.min(element.length() + 5, line.length());
//			String subLine = line.substring(0, minlen);
//			if (subLine.contains(element)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//}