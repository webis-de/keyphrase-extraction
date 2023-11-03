package de.aitools.keywordextraction.extractor.yahoo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.yahoo.search.ContentAnalysisRequest;
import com.yahoo.search.ContentAnalysisResults;
import com.yahoo.search.SearchClient;
import com.yahoo.search.SearchException;

public class TermExtraction {
	private String url;
	public TermExtraction() {
		url = "http://search.yahooapis.com/ContentAnalysisService/V1/termExtraction";
	}

	public List<String> extract(String string) throws IOException, SearchException {
		SearchClient client = new SearchClient("buwplagiarism");
		ContentAnalysisRequest car = new ContentAnalysisRequest(string);

		List<String> results = new ArrayList<String>();

//		try {
			ContentAnalysisResults ca_result = client.termExtractionSearch(car);

			String[] tmp = ca_result.getExtractedTerms();

			for(int i=0; i<tmp.length; ++i) {
				results.add(tmp[i]);
			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SearchException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return results;
	}


	public List<String> extractWebService(String string, String query) {
		List<String> terms = new ArrayList<String>();
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(this.url);

		postMethod.addRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

		NameValuePair[] data = {
				new NameValuePair("appid","buwplagiarism"),
				new NameValuePair("context",string)
		};

		postMethod.addParameters(data);

		try {
			int responseCode = httpClient.executeMethod(postMethod);

			if(responseCode != 200)
				return null;

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setIgnoringComments(true);

			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				builder.setErrorHandler(new ErrorHandler() {
					public void warning(SAXParseException e) throws SAXException {
						throw e;
					}
					public void error(SAXParseException e) throws SAXException {
						throw e;
					}
					public void fatalError(SAXParseException e) throws SAXException {
						throw e;
					}
				});

				InputStream in = postMethod.getResponseBodyAsStream();
				try {
					Document doc = builder.parse(in);
					in.close();

					NodeList results = doc.getElementsByTagName("Result");
					if(results != null) {
						for (int i=0; i<results.getLength(); ++i) {
							Node node = results.item(i);
							terms.add(getTextValue(node));
						}
					}
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			System.out.println(responseCode);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return terms;
	}

	private String getTextValue(Node node) {
		if(node.hasChildNodes()) {
			return node.getFirstChild().getNodeValue();
		} else
			return "";
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TermExtraction te = new TermExtraction();

		List<String> result = null;
		try {
			result = te.extract("lItalian sculptors and painters of the renaissance favored the Virgin Mary for inspiration.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i=0; i<result.size(); ++i) {
			System.out.println(result.get(i));
		}
	}

}
