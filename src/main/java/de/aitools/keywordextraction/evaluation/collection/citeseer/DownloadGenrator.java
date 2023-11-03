///*
// * @(#)Download.java
// * 
// *  Description: 
// *  
// *  Created:     02.02.2006
// *  Author:	     Karsten Klueger
// *  mailto:	     karsten.klueger@medien.uni-weimar.de
// *
// *  Copyright Bauhaus-Universität Weimar
// *  Bauhausstraße 11
// *  99423 Weimar, Germany
// *  
// */
///**
// * 
// */
//package de.aitools.keywordextraction.evaluation.collection.citeseer;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Vector;
//
//import de.aitools.download.AsynchronousDownloadManager;
//import de.aitools.download.DownloadJob;
//
///**
// * @author Karsten
// *
// */
//public class DownloadGenrator extends Thread {
//
//	private static final String ENCODINGSCHEME = "ISO8859_1"; 
//
//	private String targetPath;
//	private String fileType;
//	private URL sourceUrl;
//	private AsynchronousDownloadManager adm;
//	private boolean finished;
//	
//	private Vector<String> excludedFileNames; 
//
//	/**
//	 * 
//	 * @param targetPath
//	 */
//	public DownloadGenrator(String targetPath) {
//		this.targetPath = targetPath;
//		this.finished = false;
//		this.excludedFileNames = new Vector<String>();
//	}
//
//	/**
//	 * @param excludedFileNames The excludedFileNames to set.
//	 */
//	public void setExcludedFileNames(Vector<String> excludedFileNames) {
//		this.excludedFileNames = excludedFileNames;
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public boolean isFinished() {
//		return this.finished;
//	}
//
//	/**
//	 * 
//	 * @param sourceUrl
//	 * @param adm
//	 */
//	public synchronized void download(URL sourceUrl, AsynchronousDownloadManager adm, String fileType) {
//		this.sourceUrl = sourceUrl;
//		this.adm = adm;
//		this.fileType = fileType;
//		this.start();
//	}
//	
//	/**
//	 * 
//	 */
//	public void run() {
//		String urlContent = this.getUrlContent(this.sourceUrl);
//		Vector<URL> resultPageUrls = Engine.getInstance().getResultPageUrls(
//				urlContent);
//		for (URL resultPageUrl : resultPageUrls) {
//			urlContent = this.getUrlContent(resultPageUrl);
//			Vector<URL> fileUrls = Engine.getInstance().getFileUrls(urlContent,
//					this.fileType);
//			for (URL fileUrl : fileUrls) {
//				File file = this.getTargetFile(fileUrl);
//				
//				// if file is not in excluded files add file to downlaod manager
//				if(!this.excludedFileNames.contains(file.getName())) {
//					this.adm.addDownloadJob(new DownloadJob(fileUrl, file));					
//				}
//			}
//		}
//		this.finished = true;
//	}
//
//	/**
//	 * Returns the file for an URL
//	 * 
//	 * @param targetUrl
//	 * @return
//	 */
//	private File getTargetFile(URL targetUrl) {
//		String urlFileString = targetUrl.getFile();
//		File f = new File(this.targetPath
//				+ Engine.getInstance().getFileName(urlFileString));
//		return f;
//	}
//
//	/**
//	 * Reads the content of an URL
//	 * 
//	 * @param url
//	 * @return 
//	 */
//	private String getUrlContent(URL url) {
//		StringBuffer content = new StringBuffer();
//		BufferedReader br;
//		HttpURLConnection httpConnection = null;
//
//		try {
//			httpConnection = (HttpURLConnection) url.openConnection();
//			httpConnection.setRequestMethod("GET");
//			httpConnection.connect();
//
//		} catch (IOException ioe) {
//			System.err.println(ioe.getMessage());
//		}
//
//		try {
//			br = new BufferedReader(new InputStreamReader(httpConnection
//					.getInputStream(), ENCODINGSCHEME));
//			String curLine;
//
//			while ((curLine = br.readLine()) != null) {
//				content.append(curLine);
//			}
//
//		} catch (UnsupportedEncodingException e) {
//			System.err.println(e.getMessage());
//		} catch (IOException e) {
//			System.err.println(e.getMessage());
//		}
//		return content.toString();
//	}
//
//}
