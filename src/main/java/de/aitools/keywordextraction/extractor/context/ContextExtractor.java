package de.aitools.keywordextraction.extractor.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.yahoo.search.SearchException;

import de.aitools.clustering.Clustering;
import de.aitools.common.math.linearalgebra.NDimensionalPoint;
import de.aitools.common.math.linearalgebra.VectorRepresentation;
import de.aitools.keywordextraction.KeywordExtractorFactory;
import de.aitools.keywordextraction.extractor.KeywordExtractor;
import de.aitools.retrievalmodel.similarity.CosineDissimilarity;
import de.aitools.retrievalmodel.similarity.DistanceMeasure;

/**
 * @author smze
 * @author Karsten Klueger, BUW
 * 
 * 
 */
public class ContextExtractor extends KeywordExtractor {

	// the number of keywords to get from the bootstrap extractor
	private static final int NUMBER_BOOTSTRAP_KW = 30;

	// the number of bootstrap keywords to build the search query from
	private static final int NUMBER_QUERY_KW = 4;

	// the minimum number of clusters to build
	private static final int MIN_CLUSTERS = 5;

	// the maximum number of clusters to build
	private static final int MAX_CLUSTERS = 15;

	/**
	 * Constructor
	 * 
	 */
	public ContextExtractor() {
		super();
	}

	/**
	 * extracts the keywords
	 * 
	 * @param text
	 * @param numberOfKeywords
	 * @return
	 */
	public List<String> extract(String text, int numberOfKeywords) {

		// get bootstrap keywords
		KeywordExtractor extractor = KeywordExtractorFactory
				.getCoocurrenceExtractor();
		
		List<String> bootstrapKeywords = extractor.getSingleWords(text,
				NUMBER_BOOTSTRAP_KW);
		List<String> bootstrapKeyphrases = extractor.getPhrases(text,
				NUMBER_BOOTSTRAP_KW);
		
//		List<String> kw = new List<String>();
//		for(String k: bootstrapKeywords) {
//			int rank=WordClassDictionary.getWordClass(k);
//				if(rank > 3 && rank < 18 ) {
//					kw.add(k);
//			}
//		}
		
		
		
		List<String> kw = new ArrayList<String>();
		for(String k: bootstrapKeywords) {
			if(!bootstrapKeyphrases.contains(k)) {
				kw.add(k);
			}
		}
		
		return kw;
		
//		String keywordstring = "";
//		for (String keyword : bootstrapKeywords) {
//			keywordstring += keyword + " ";
//		}
//		System.out.println(keywordstring);
//		for (String k : bootstrapKeyphrases) {
//			System.out.println(k);
//		}
//
//		try {
//			// get snippets
//			List<String> textSnippets = this.getSnippets(bootstrapKeywords);
//			textSnippets.add(keywordstring);
//
//			// create document models
//			Documentgroup g = this.makeDocumentgroup(textSnippets);
//
//			// get List representation of input document
//			VectorRepresentation inputList = g.get(g.size() - 1)
//					.getCompressedWordList();
//
//			// wheight with idf for clustering
//			this.weightWithIdf(g);
//
//			int minClusters = Math.min(textSnippets.size(), MIN_CLUSTERS);
//			int maxclusters = Math.min(textSnippets.size(), MAX_CLUSTERS);
//
//			// cluster snippets
//			Clustering clustering = this.clusterSnippets(g, minClusters,
//					maxclusters);
//
//			// if clustering was not successfull, use bootstrap keyword
//			if (clustering == null) {
//				return bootstrapKeywords;
//			}
//
//			// get probabilities for each term of every cluster
//			List<Map<String, Double>> indexTermProbs = this
//					.getIndexTermProbs(clustering, textSnippets);
//
//			// get centroids for documents
//			NDimensionalPoint[] centroids = this.getCentroids(g, clustering);
//
//			// find closest centroid
//			int closestCentroidIndex = this.getClosestCentroidIndex(centroids,
//					inputList);
//
//			// **********************************************************************
//
//			// get terms
//			List<Term> closestCentroidTerms = this.getTerms(centroids,
//					closestCentroidIndex, indexTermProbs, bootstrapKeywords);
//
//			List<Term> keywordTerms = this.computeKeywords(
//					closestCentroidTerms, bootstrapKeyphrases);
//
//			// **********************************************************************
//
//			// reduce to number of desired keywords
//			keywordTerms.setSize(Math.min(numberOfKeywords,
//					keywordTerms.size()));
//
//			// output as a List of strings
//			List<String> keywords = new List<String>();
//			for (Term t : keywordTerms) {
//				keywords.add(t.toString());
//			}
//			return keywords;
//
//		} catch (SearchException e) {
//			System.out.println("can't search yahoo:" + e.getMessage());
//			return bootstrapKeywords;
//		} catch (IOException e) {
//			System.out.println("can't search yahoo:" + e.getMessage());
//			return bootstrapKeywords;
//		}
	}

	/**
	 * 
	 * @param keywordList
	 * @return
	 * @throws IOException
	 * @throws SearchException
	 */
	private List<String> getSnippets(List<String> keywordList){
//			throws IOException, SearchException {
//		//YahooApi ya = new YahooApi();
//
//		String keywords = new String();
//		for (int i = 0; i < NUMBER_QUERY_KW; i++) {
//			keywords += keywordList.get(i).toString() + " ";
//		}
//		System.out.println("querying Yahoo with: " + keywords);
//	//	WebSearchResults wsr = ya.search(keywords, 100, "en");
//	//	WebSearchResult[] result = wsr.listResults();
//		List<String> snippets = new List<String>();
////		for (int i = 0; i < result.length; i++) {
//			String curSnippet = new String();
////			curSnippet += result[i].getTitle() + " ";
////			curSnippet += result[i].getSummary();
//			snippets.add(curSnippet.toLowerCase());
//		}
//
//		return snippets;
	return null;
	}

//	/**
//	 * 
//	 * @param snippets
//	 * @return
//	 */
//	private Documentgroup makeDocumentgroup(List<String> snippets) {
//		// make document models
//		Documentgroup g = new Documentgroup();
//		for (String curSnippet : snippets) {
//			Document d = new Document();
//			PlainIndexer plainIndexer = new PlainIndexer();
//			//plainIndexer.setStemmer(null, this.language); // null==no stemming
//			d.setIndexer(plainIndexer);
//			d.index(curSnippet);
//			d.deleteText();
//			g.add(d);
//		}
//		return g;
//	}

//	/**
//	 * 
//	 * @param g
//	 */
//	private void weightWithIdf(Documentgroup g) {
//		// weight with Idf
//		IntVector df = g.computeDF();
//		TfIdf tfidf = new TfIdf();
//		for (int i = 0; i < g.size(); i++) {
//			Document d = g.get(i);
//			tfidf.weight(d, g.size(), df);
//		}
//	}

//	/**
//	 * 
//	 * @param snippets
//	 * @param kMin
//	 * @param kMax
//	 * @return
//	 */
//	private Clustering clusterSnippets(Documentgroup g, int kMin, int kMax) {
//		if (kMin > kMax)
//			throw new RuntimeException("bogus parameters!");
//		VectorRepresentation[] vr = g.getWordVectors();
//		KMeans kMeans = new KMeans();
//		DistanceMeasure dm = new CosineDissimilarity();
//		Clustering[] clusterings = new Clustering[kMax - kMin + 1];
//		for (int k = kMin; k <= kMax; k++) {
//			Clustering c = kMeans.cluster(vr, dm, k, 10);
//			clusterings[k - kMin] = c;
//		}
//		GeneralizedDunnIndex gdi = new GeneralizedDunnIndex(vr, dm);
//		double[] scores = gdi.measure(clusterings);
//		int bestIndex = -1;
//		double bestScore = 0;
//		for (int i = 0; i < scores.length; i++) {
//			if (scores[i] > bestScore) {
//				bestScore = scores[i];
//				bestIndex = i;
//			}
//		}
//		// System.out.println("best k:" + bestIndex);
//		if (bestIndex > -1) {
//			return clusterings[bestIndex];
//		} else {
//			return null;
//		}
//	}

	/**
	 * 
	 * @param clustering
	 * @param snippets
	 * @return
	 */
	private List<Map<String, Double>> getIndexTermProbs(
			Clustering clustering, List<String> snippets) {
		List<Map<String, Double>> indexTermProbs = new ArrayList<Map<String, Double>>();

		List<Map<String, Integer>> indexTermFrequencies = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> overAllFrequenciesHash = new HashMap<String, Integer>();
		Map<String, Integer> clusterFrequenciesHash = null;
		Map<String, Double> clusterProbsHash = null;
		int numberOfClusters = clustering.getClusterCount();

		// count frequencies of each word
		StringTokenizer tokenizer = null;
		for (int i = 0; i < numberOfClusters; i++) {
			clusterFrequenciesHash = new HashMap<String, Integer>();
			int[] items = clustering.getItemsOfCluster(i);
			for (int snippetIndex : items) {
				String snippet = snippets.get(snippetIndex);
				tokenizer = new StringTokenizer(snippet, Delimiter.Delimiters);
				while (tokenizer.hasMoreTokens()) {
					String word = tokenizer.nextToken();
					// put to clusterhash
					if (clusterFrequenciesHash.containsKey(word)) {
						int freq = clusterFrequenciesHash.get(word);
						clusterFrequenciesHash.put(word, freq + 1);
					} else {
						clusterFrequenciesHash.put(word, 1);
					}
					// put to overAllHash
					if (overAllFrequenciesHash.containsKey(word)) {
						int freq = overAllFrequenciesHash.get(word);
						overAllFrequenciesHash.put(word, freq + 1);
					} else {
						overAllFrequenciesHash.put(word, 1);
					}
				}
			}
			indexTermFrequencies.add(clusterFrequenciesHash);
		}

		// compute probabilities for each word in every cluster
		for (int i = 0; i < numberOfClusters; i++) {
			clusterProbsHash = new HashMap<String, Double>();
			clusterFrequenciesHash = indexTermFrequencies.get(i);
			Iterator<String> wordIter = clusterFrequenciesHash.keySet().iterator();
			while (wordIter.hasNext()) {
				String word = wordIter.next();
				int freq = clusterFrequenciesHash.get(word);
				double prob = 0.0;
				// if (freq > 1) {
				prob = (double) freq
						/ (double) overAllFrequenciesHash.get(word);
				// }
				clusterProbsHash.put(word, prob);
			}
			indexTermProbs.add(clusterProbsHash);
		}

		return indexTermProbs;
	}

	/**
	 * 
	 * @param clustering
	 * @param snippets
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Map<String, Double>> getIndexTermFreq(
			Clustering clustering, List<String> snippets) {

		List<Map<String, Double>> indexTermFrequencies = new ArrayList<Map<String, Double>>();
		Map<String, Double> clusterFrequenciesHash = null;
		int numberOfClusters = clustering.getClusterCount();

		// count frequencies of each word
		StringTokenizer tokenizer = null;
		for (int i = 0; i < numberOfClusters; i++) {
			clusterFrequenciesHash = new HashMap<String, Double>();
			int[] items = clustering.getItemsOfCluster(i);
			for (int snippetIndex : items) {
				String snippet = snippets.get(snippetIndex);
				tokenizer = new StringTokenizer(snippet, Delimiter.Delimiters);
				while (tokenizer.hasMoreTokens()) {
					String word = tokenizer.nextToken();
					// put to clusterhash
					if (clusterFrequenciesHash.containsKey(word)) {
						double freq = clusterFrequenciesHash.get(word);
						clusterFrequenciesHash.put(word, freq + 1.0);
					} else {
						clusterFrequenciesHash.put(word, 1.0);
					}
				}
			}
			indexTermFrequencies.add(clusterFrequenciesHash);
		}

		return indexTermFrequencies;
	}

//	/**
//	 * 
//	 * @param g
//	 * @param c
//	 * @return
//	 */
//	private NDimensionalPoint[] getCentroids(Documentgroup g, Clustering c) {
//		NDimensionalPoint[] centroids = new NDimensionalPoint[c
//				.getClusterCount()];
//		VectorRepresentation[] docLists = g.getWordVectors();
//		int dimension = getMaxDimension(docLists);
//		for (int i = 0; i < c.getClusterCount(); i++) {
//			NDimensionalPoint centroid = new NDimensionalPoint(dimension);
//			int[] items = c.getItemsOfCluster(i);
//			for (int j = 0; j < items.length; j++) {
//				centroid.add(docLists[items[j]]);
//			}
//			centroid.scale((double) 1 / (double) items.length);
//			centroids[i] = centroid;
//		}
//		return centroids;
//	}

	/**
	 * 
	 * @param centroids
	 * @param documentList
	 * @return
	 */
	private int getClosestCentroidIndex(VectorRepresentation[] centroids,
			VectorRepresentation documentList) {
		DistanceMeasure dm = new CosineDissimilarity();
		double minDistance = Double.MAX_VALUE;
		int minIndex = -1;
		for (int i = 0; i < centroids.length; i++) {
			double curDistance = dm.f(centroids[i], documentList);
			if (curDistance < minDistance) {
				minDistance = curDistance;
				minIndex = i;
			}
		}
		return minIndex;
	}

	/**
	 * 
	 * @param vr
	 * @return
	 */
	private int getMaxDimension(VectorRepresentation[] vr) {
		int maxDimension = 0;
		for (int i = 0; i < vr.length; i++) {
			if (vr[i].getDimension() > maxDimension)
				maxDimension = vr[i].getDimension();
		}
		return maxDimension;
	}

	/**
	 * 
	 * @param position
	 * @return
	 */
	private String getIndexTerm(int position) {
		throw new RuntimeException("this method needs a review.");
//		Symbol s = SymbolWrapper.getWordListAt(position);
//		return Symbol.symbolName(s);
	}

	/**
	 * 
	 * @param List
	 * @param indexTermValues
	 * @return
	 */
	private List<Term> getIndexTerms(VectorRepresentation List,
			Map<String, Double> indexTermValues) {
		List<Term> terms = new ArrayList<Term>();
		int dim = List.getDimension();
		for (int i = 0; i < dim; i++) {
			String word = this.getIndexTerm(i);
			double value = 0.0;
			if (indexTermValues != null) {
				if (indexTermValues.containsKey(word)) {
					value = indexTermValues.get(word);
				}
			} else {
				value = List.getValue(i);
			}
			Term t = new Term(word, value);
			terms.add(t);
		}
		return terms;
	}

	/**
	 * 
	 * @param centroids
	 * @param closestCentroidIndex
	 * @param indexTermProbs
	 * @param bootstrapKeywords
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Term> getTerms(NDimensionalPoint[] centroids,
			int closestCentroidIndex,
			List<Map<String, Double>> indexTermProbs,
			List<String> bootstrapKeywords) {
		int dimension = centroids[0].getDimension();
		Map<Integer, List<Term>> centriodsHash = new HashMap<Integer, List<Term>>();

		// create a List of terms for each centroid and store it in a hash
		for (int i = 0; i < centroids.length; i++) {
			VectorRepresentation centroid = centroids[i];
			List<Term> indexTerms = this.getIndexTerms(centroid,
					indexTermProbs.get(i));
			centriodsHash.put(i, indexTerms);
		}

		// compute entropy for each index term
		for (int i = 0; i < dimension; i++) {
			double entropy = 0.0;

			// add p * log(p) for each centroid
			for (int j = 0; j < centriodsHash.size(); j++) {
				Term t = centriodsHash.get(j).get(i);

				double prob = t.getNormalizedFrequency();
				if (prob > 0) {
					entropy += prob * this.ld(prob);
				}
			}
			// 
			entropy = 1 + ((1.0 / this.ld((double) centriodsHash.size())) * entropy);
			// set entropy
			for (int j = 0; j < centriodsHash.size(); j++) {
				Term t = centriodsHash.get(j).get(i);
				t.setEntropy(entropy);
			}
		}

		// get all terms equal to bootstrap keywords
		List<Term> terms = centriodsHash.get(closestCentroidIndex);
		List<Term> keywordTerms = new ArrayList<Term>();
		List<Term> initQueryTerms = new ArrayList<Term>();

		for (Term t : terms) {
			if (bootstrapKeywords.contains(t.toString())) {
				// keep terms from init query
				if (bootstrapKeywords.indexOf(t.toString()) < NUMBER_QUERY_KW) {
					initQueryTerms.add(t);
				} else if (t.getNormalizedFrequency() < 1.0) {
					keywordTerms.add(t);
				}
			}
		}

		Collections.sort(keywordTerms);

		initQueryTerms.addAll(keywordTerms);
		return initQueryTerms;
	}

	/**
	 * 
	 * @param x
	 * @return
	 */
	private double ld(double x) {
		return Math.log(x) / Math.log(2);
	}

	private List<Term> computeKeywords(List<Term> closestCentroidTerms,
			List<String> bootstrapKeyphrases) {
		List<Term> keywordTerms = new ArrayList<Term>();
		
		for(Term term: closestCentroidTerms) {
			String word = term.toString();
			if(!bootstrapKeyphrases.contains(word)) {
				keywordTerms.add(term);
			}
		}		
		return keywordTerms;
	}

}
