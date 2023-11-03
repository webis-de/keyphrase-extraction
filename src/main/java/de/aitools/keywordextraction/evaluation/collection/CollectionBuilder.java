///*
// * @(#)CollectionBuilder.java
// * 
// *  Description: This class creates a collection of xml-document for evaluation
// *               of various keyword extraction algorithms.
// *               
// *               Therefore a number of pdf documents is downloades from 
// *               CiteSeer and converted into a xml file.
// *  
// *  Created:     2006-02-01
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
//import java.net.URL;
//import java.util.Date;
//import java.util.Vector;
//
//import org.apache.commons.io.FilenameUtils;
//import org.apache.commons.io.filefilter.SuffixFileFilter;
//
//import de.aitools.keywordextraction.evaluation.collection.citeseer.DownloadGenrator;
//import de.aitools.keywordextraction.evaluation.collection.citeseer.Engine;
//import de.aitools.keywordextraction.evaluation.collection.citeseer.PDFConverter;
//
//import de.aitools.download.AsynchronousDownloadManager;
//import de.aitools.keywordextraction.evaluation.Utils;
//
///**
// * This class creates a collection of xml-documents for evaluation of various
// * keyword extraction algorithms.<br>
// * Therefore a number of documents is downloaded from CiteSeer and converted
// * into a xml file.
// * 
// * @author Karsten Klueger
// * @version 1.0
// */
//public class CollectionBuilder {
//
//	// params
//	private static final String DOWNLOADPATH = "-d";
//
//	private static final String XMLPATH = "-x";
//
//	private static final String SEARCHTERM = "-s";
//
//	private static final String NUMBEROFFILES = "-n";
//
//	private static final String DOWNLOADONLY = "-do";
//
//	private static final String XMLONLY = "-xo";
//
//	//
//	private static final String PDFFILETYPE = ".pdf";
//
//	private static final String XMLFILETYPE = ".xml";
//
//	private static final String DOWNLOADHISTORYFILENAME = "/history.txt";
//
//	private static final long MAXFILELENGTH = 5000000;
//
//	private static int TIMEOUT_MIN = 6000;
//
//	private boolean createXml = true;
//
//	private boolean download = true;
//
//	//
//	private String downloadPath = new String();
//
//	private String xmlPath = new String();
//
//	private String searchTerm = new String();
//
//	private int numberOfFiles = 0;
//
//	private Vector<DownloadGenrator> activeDownloadGenerators;
//
//	private Vector<String> downloadHistory;
//
//	private AsynchronousDownloadManager asychrDownloadManager;
//
//	private File downloadHistoryFile;
//
//	/**
//	 * Construcor
//	 * 
//	 */
//	public CollectionBuilder() {
//		this.asychrDownloadManager = new AsynchronousDownloadManager();
//		this.downloadHistory = new Vector<String>();
//		this.activeDownloadGenerators = new Vector<DownloadGenrator>();
//	}
//
//	/**
//	 * 
//	 * @param searchTerm
//	 * @param fileType
//	 * @param numberOfFiles
//	 */
//	public void buildCollection(String searchTerm, int numberOfFiles) {
//
//		int numberOfPdfFilesBeforeDonwload = 0;
//		int numberOfPdfFilesAfterDonwload = 0;
//
//		// start download if download flag is set
//		if (this.download) {
//
//			// get number of files in download directory before download starts
//			numberOfPdfFilesBeforeDonwload = this.getFiles(this.downloadPath,
//					PDFFILETYPE).size();
//
//			// download documents from CiteSeer
//			this.downloadFiles(searchTerm, numberOfFiles);
//
//			// get number of files in download directory after download has
//			// finished
//			numberOfPdfFilesAfterDonwload = this.getFiles(this.downloadPath,
//					PDFFILETYPE).size();
//		}
//
//		// start xml file creation if flag is set
//		if (this.createXml) {
//
//			// if there are new downloaded files or downloadflag is unset
//			if (numberOfPdfFilesBeforeDonwload < numberOfPdfFilesAfterDonwload
//					|| !this.download) {
//
//				// convert to xml
//				this.createXmlFiles();
//			} else {
//				System.out.println("couldn't find any new files");
//			}
//		}
//	}
//
//	/**
//	 * 
//	 * @param searchTerm
//	 * @param fileType
//	 * @param numberOfFiles
//	 */
//	public void downloadFiles(String searchTerm, int numberOfFiles) {
//
//		System.out.println();
//		System.out.println("download manager started");
//		System.out
//				.println("please wait while trying to download new documents");
//
//		// get download history
//		this.downloadHistoryFile = new File(this.downloadPath
//				+ DOWNLOADHISTORYFILENAME);
//		this.downloadHistory = Utils.getLines(this.downloadHistoryFile);
//
//		// get files from initial result page
//		URL initURL = Engine.getInstance().getInitURL(searchTerm);
//		DownloadGenrator initDl = new DownloadGenrator(this.downloadPath);
//		initDl.setExcludedFileNames(this.downloadHistory);
//		initDl.download(initURL, this.asychrDownloadManager, PDFFILETYPE);
//		this.activeDownloadGenerators.add(initDl);
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// get files from next result pages
//		int numberOfResultPages = (int) (Math.ceil((double) numberOfFiles
//				/ (double) Engine.RESPERPAGE));
//		for (int i = 1; i < numberOfResultPages; i++) {
//			URL nextPageUrl = Engine.getInstance().getNextPageUrl(
//					i * Engine.RESPERPAGE, searchTerm);
//			DownloadGenrator nextPageDl = new DownloadGenrator(
//					this.downloadPath);
//			nextPageDl.setExcludedFileNames(this.downloadHistory);
//			nextPageDl.download(nextPageUrl, this.asychrDownloadManager,
//					PDFFILETYPE);
//			this.activeDownloadGenerators.add(nextPageDl);
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		// check if all downloads are finished for every 5 sec.
//		int downloadsInProgress;
//		while (this.isGeneratingDownloads()
//				|| !this.asychrDownloadManager.isFinished()) {
//			try {
//				Thread.sleep(5000);
//				downloadsInProgress = this.asychrDownloadManager
//						.getDownloadsInProgress();
//				if (downloadsInProgress > 0) {
//					System.out.println(downloadsInProgress
//							+ " downloads in progress");
//				} else {
//					System.out.print(".");
//				}
//			} catch (InterruptedException ie) {
//			}
//		}
//
//		// add downloaded files to download history
//		for (File file : this.getFiles(this.downloadPath, PDFFILETYPE)) {
//			String fileName = file.getName();
//			if (!downloadHistory.contains(fileName)) {
//				Utils.appendLine(this.downloadHistoryFile, fileName);
//			}
//		}
//
//		System.out.println("\nall downloads finished");
//		System.out.println(this.asychrDownloadManager.getAssignedJobsCount()
//				+ " downloads assigned");
//		System.out.println(this.asychrDownloadManager.getFinishedJobsCount()
//				+ " downloads finished");
//	}
//
//	/**
//	 * 
//	 * 
//	 */
//	private boolean createXmlFiles() {
//
//		System.out.println();
//		System.out.println("xml converter started");
//		System.out.println("please wait while converting new documents");
//
//		XMLCreator xmlCreator = new XMLCreator();
//
//		// get downloaded pdf files from download path
//		Vector<File> pdfFiles = this.getFiles(this.downloadPath, PDFFILETYPE);
//
//		// stop if there are no new files
//		if (pdfFiles.size() < 1) {
//			return false;
//		}
//
//		for (File pdfFile : pdfFiles) {
//
//			// try to create new xml file if current pdf file is allready not
//			// converted to xml
//			String baseName = FilenameUtils.getBaseName(pdfFile.getName());
//			File xmlFile = new File(this.xmlPath + "/" + baseName + XMLFILETYPE);
//			if (!xmlFile.exists()) {
//
//				// delete pdf-file if it is to large
//				if (pdfFile.length() > MAXFILELENGTH) {
//					pdfFile.delete();
//					System.out.println(xmlFile.getName() + ": too big");
//				} else {
//
//					// get text from pdf file
//					String text = PDFConverter.getText(pdfFile);
//
//					// if text has been successfully extracted
//					if (text.length() > 0) {
//
//						// create the new xml file
//						boolean success = xmlCreator.generate(xmlFile, text);
//
//						// if xml creation wasn't successful
//						if (!success) {
//							// delete pdf file
//							pdfFile.delete();
//							System.out.println(xmlFile.getName()
//									+ ": xml creation failed");
//						}
//					}
//
//					// file is useless
//					else {
//						// delete pdf file from download dir
//						pdfFile.delete();
//						System.out.println(xmlFile.getName() + ": pdf to text conversion failed");
//					}
//				}
//			}
//		}
//		return true;
//	}
//
//	/**
//	 * 
//	 * @param path
//	 * @param fileType
//	 * @return
//	 */
//	private Vector<File> getFiles(String path, String fileType) {
//		Vector<File> files = new Vector<File>();
//		// get files
//		for (String fileName : new File(path).list(new SuffixFileFilter(
//				fileType))) {
//			files.add(new File(this.downloadPath + "/" + fileName));
//		}
//		return files;
//	}
//
//	/**
//	 * 
//	 * @param args
//	 */
//	private void setOptions(String[] args) {
//
//		for (int i = 0; i < args.length; i++) {
//
//			// path to store downloaded files
//			if (args[i].equals(DOWNLOADPATH)) {
//				i++;
//				if (i >= args.length) {
//					usage();
//				}
//				this.setDownloadPath(args[i]);
//			}
//
//			// path to store xml files
//			if (args[i].equals(XMLPATH)) {
//				i++;
//				if (i >= args.length) {
//					usage();
//				}
//				this.setXmlPath(args[i]);
//			}
//
//			// number of desired target files
//			else if (args[i].equals(NUMBEROFFILES)) {
//				i++;
//				if (i >= args.length) {
//					usage();
//				}
//				this.setNumberOfFiles(Integer.parseInt(args[i]));
//			}
//
//			// search term
//			else if (args[i].equals(SEARCHTERM)) {
//				i++;
//				if (i >= args.length) {
//					usage();
//				}
//				this.setSearchTerm(args[i]);
//			}
//
//			// download only
//			else if (args[i].equals(DOWNLOADONLY)) {
//				this.setCreateXml(false);
//			}
//
//			// xml creation only
//			else if (args[i].equals(XMLONLY)) {
//				this.setDownload(false);
//			}
//		}
//
//		// check if all required args were set
//		if (this.numberOfFiles == 0 || this.searchTerm.length() == 0
//				|| this.downloadPath.length() == 0
//				|| this.xmlPath.length() == 0) {
//			usage();
//		}
//	}
//
//	/**
//	 * print the usage requirements and exit
//	 * 
//	 */
//	private static void usage() {
//		System.err
//				.println("Usage:\n java aitools.keywordextraction.evaluation.CollectionBuilder "
//						+ "-d <downloadPath> -x <xmlPath> -n <docnumber> -s <searchTerm>\n"
//						+ "  -p <sourcePath>    path to store downloaded files\n"
//						+ "  -x <xmlPath>       path to store xml files\n"
//						+ "  -n <docNumber>     number of desired documents\n"
//						+ "  -s <searchTerm>	the term to search for\n");
//		System.exit(1);
//	}
//
//	/**
//	 * 
//	 * @return Returns true if there
//	 */
//	public boolean isGeneratingDownloads() {
//		for (DownloadGenrator dlGen : this.activeDownloadGenerators) {
//			if (!dlGen.isFinished()) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * @param downloadPath
//	 *            The downloadPath to set.
//	 */
//	public void setDownloadPath(String downloadPath) {
//		this.downloadPath = downloadPath;
//	}
//
//	/**
//	 * @param numberOfFiles
//	 *            The numberOfFiles to set.
//	 */
//	public void setNumberOfFiles(int numberOfFiles) {
//		this.numberOfFiles = numberOfFiles;
//	}
//
//	/**
//	 * @param searchTerm
//	 *            The searchTerm to set.
//	 */
//	public void setSearchTerm(String searchTerm) {
//		this.searchTerm = searchTerm;
//	}
//
//	/**
//	 * @param xmlPath
//	 *            The xmlPath to set.
//	 */
//	public void setXmlPath(String xmlPath) {
//		this.xmlPath = xmlPath;
//	}
//
//	/**
//	 * @param createXml
//	 *            The createXml to set.
//	 */
//	public void setCreateXml(boolean createXml) {
//		this.createXml = createXml;
//	}
//
//	/**
//	 * @param download
//	 *            The download to set.
//	 */
//	public void setDownload(boolean download) {
//		this.download = download;
//	}
//
//	/**
//	 * 
//	 * Start
//	 * 
//	 * @param args
//	 *            <br>
//	 *            <b>-p</b> path to store downloaded files<br>
//	 *            <b>-x</b> path to store xml files<br>
//	 *            <b>-n</b> number of desired documents<br>
//	 *            <b>-s</b> the term to search for
//	 */
//	public static void main(String[] args) {
//
//		long startTime = System.currentTimeMillis();
//
//		System.out.println("CollectionBuilder started at " + new Date());
//
//		boolean finished = false;
//		CollectionBuilder cb = new CollectionBuilder();
//		try {
//			cb.setOptions(args);
//		} catch (Exception e1) {
//			System.out.println(e1.toString());
//		}
//		while (!finished) {
//			cb.buildCollection(cb.searchTerm, cb.numberOfFiles * 5);
//
//			if (cb.getFiles(cb.xmlPath, XMLFILETYPE).size() >= cb.numberOfFiles) {
//				finished = true;
//			} else if (!cb.download) {
//				finished = true;
//			}
//			// break after 120 min
//			if (System.currentTimeMillis() - startTime > 36000 * TIMEOUT_MIN) {
//				break;
//			}
//		}
//
//		// close download manager
//		cb.asychrDownloadManager.interrupt();
//
//		// end
//		if (!finished) {
//			System.err.println("\n\n break because of timeout !");
//		} else {
//			System.out.println("\n\n finished !");
//		}
//	}
//
//}