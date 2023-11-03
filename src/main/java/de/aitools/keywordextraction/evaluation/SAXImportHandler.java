package de.aitools.keywordextraction.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Karsten Klueger, BUW
 * 
 */
public class SAXImportHandler extends DefaultHandler {

	private static final String KEYWORDS = "keywords";

	// buffer for keyword string
	private StringBuffer keywordBuffer;

	// keywords assigned by the author
	private List<String> authorKeywords;

	// text to extract keywords from
	private StringBuffer strBuff;

	// current parsed element
	private List<String> currentElement;
	
	private List<String> tags;

	/**
	 * 
	 * @param elements
	 * @param authorKeywords
	 * @param strBuffer
	 * @param tags 
	 */
	public SAXImportHandler(List<String> authorKeywords,
			StringBuffer strBuffer, List<String> tags) {
		this.authorKeywords = authorKeywords;
		this.strBuff = strBuffer;
		this.keywordBuffer = new StringBuffer();
		this.currentElement = new ArrayList<String>();
		this.tags = tags;
	}

	/**
	 * 
	 */
	public void startElement(String uri, String name, String qName,
			Attributes atts) {
		// set current element
		this.currentElement.add(0, qName);
	}

	/**
	 * 
	 */
	public void characters(char ch[], int start, int length) {
		if (this.tags.contains(this.currentElement.get(0)))
			// the content is put into the text buffer
			for (int i = start; i < start + length; i++) {
				this.strBuff.append(ch[i]);
			}
	
		// if current element is 'keywords'
		else if (this.currentElement.get(0).equalsIgnoreCase(KEYWORDS)) {
			// the content is put into the keyword buffer
			for (int i = start; i < start + length; i++) {
				this.keywordBuffer.append(ch[i]);
			}
		}
	}

	/**
	 * 
	 */
	public void endElement(String uri, String name, String qName) {
		this.currentElement.remove(0);
	}
	
	/**
	 * 
	 */
	public void endDocument() {
		String kw = this.keywordBuffer.toString();			
		kw = kw.replaceAll("\n", " ");
		kw = kw.replaceAll("  ", " ");
		kw = kw.replaceAll(" , ", ",");
		kw = kw.replaceAll(", ", ",");
		kw = kw.replaceAll("; ", ",");

		StringTokenizer kwST = new StringTokenizer(kw, ",");
		while (kwST.hasMoreTokens()) {
			String word = kwST.nextToken();
			this.authorKeywords.add(word.trim());
		}
	}

}