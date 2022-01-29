package pa1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;

import org.jsoup.Jsoup;

import api.Graph;
import api.TaggedVertex;
import api.Util;

/**
 * Implementation of an inverted index for a web graph.
 *
 * @author Zach DeMaris
 * @author James Taylor
 */
public class Index {

	//HashMap of linked lists, key = word, value = ArrayList of TaggedVertexs<url, word count>
	private HashMap<String, ArrayList<TaggedVertexWordCount>> map;
	
	//Helper HashMap that helps lookup index into the above map, key = (URL + word), value = index into the array lists in the above map
	private HashMap<String, Integer> helperMap;
	
	//List of tagged vertexes that is made up of <url, in degree>
	private List<TaggedVertex<String>> mURLS;

	/**
	 * Constructs an index from the given list of URLs. The tag value for each URL
	 * is the in-degree of the corresponding node in the graph to be indexed.
	 *
	 * @param urls information about graph to be indexed
	 */
	public Index(List<TaggedVertex<String>> urls) {
		this.mURLS = urls;
		map = new HashMap<>();
		helperMap = new HashMap<>();
	}

	/**
	 * Creates the index. n = the number of URLs in the graph. m = the number of
	 * words in a given page. This runs in O(nm).
	 *
	 * @throws IOException
	 */
	public void makeIndex(){

		for (TaggedVertex<String> tV : mURLS) {// For everyweb page
			if (Crawler.politnessCheck()) {
				Crawler.currentRequests = 0;
			}
			String pageBody = "";
			Crawler.currentRequests++;
			try {
				pageBody = Jsoup.connect(tV.getVertexData()).get().body().text();
			}catch(Exception e){
				System.out.println("Something strange happened, not adding words from " + tV.getVertexData() + "to index");
				continue;
			}
			if(pageBody == null) continue;
			System.out.println("pulling contents from: " + tV.getVertexData());
			Scanner sc = new Scanner(pageBody);// For every word on one webpage
			while (sc.hasNext()) {
				String word = sc.next();
				word = Util.stripPunctuation(word);
				if (Util.isStopWord(word) || word.equals("")) {
					continue;
				}

				String wordOnWebPage = word + tV.getVertexData();

				// Does this word exist in hash map?
				if (!map.containsKey(word)) {
					helperMap.put(wordOnWebPage, 0);
					map.put(word, new ArrayList<TaggedVertexWordCount>());
					map.get(word).add(new TaggedVertexWordCount(tV, 0));
				}

				// if the helperMap does not contain the word for this web page:
				// 1. Add an element to array list in map
				// 2. Add the word/webpage to helper map
				if (!helperMap.containsKey(wordOnWebPage)) {
					// If its not in the helper it can't be in map so add an element to arraylist in map
					map.get(word).add(new TaggedVertexWordCount(tV, 0));
					int size = map.get(word).size();
					helperMap.put(wordOnWebPage, size - 1);
				}

				int arrayIndex = helperMap.get(wordOnWebPage);
				map.get(word).get(arrayIndex).incrementWordCount();
			}
			sc.close();
		}
	}

	/**
	 * Searches the index for pages containing keyword w. Returns a list of urls
	 * ordered by ranking (largest to smallest). The tag value associated with each
	 * url is its ranking. The ranking for a given page is the number of occurrences
	 * of the keyword multiplied by the indegree of its url in the associated graph.
	 * No pages with rank zero are included.
	 *
	 * @param w keyword to search for
	 * @return ranked list of urls
	 */
	public List<TaggedVertex<String>> search(String w) {
		// Grab the list of URLS associated with a given word
		List<TaggedVertexWordCount> wordList = map.get(w);
		if(wordList==null){return null;}
		URLRanking[] rankingArray = new URLRanking[wordList.size()];

		// Go through all TaggedVertexWordCount for a list, add them to a list in a new coupling object
		for (int i = 0; i < wordList.size(); i++) { 		// O(V)
			TaggedVertexWordCount thisCount = wordList.get(i);
			int rank = thisCount.getWordCount() * thisCount.getTaggedVertex().getTagValue();
			TaggedVertex<String> url = new TaggedVertex<String>(thisCount.getTaggedVertex().getVertexData(), rank);
			rankingArray[i] = new URLRanking(url);
		}

		Arrays.sort(rankingArray); // Uses TimSort, worse case O(VlogV) - bounding time of this methods
		List<TaggedVertex<String>> toRet = new ArrayList<>();

		// Copy to correct output list in O(V)
		for (int j = 0; j < rankingArray.length; j++) {
			toRet.add(rankingArray[j].getUrl());
		}

		return toRet;
	}

	/**
	 * Searches the index for pages containing both of the keywords w1 and w2.
	 * Returns a list of qualifying urls ordered by ranking (largest to smallest).
	 * The tag value associated with each url is its ranking. The ranking for a
	 * given page is the number of occurrences of w1 plus number of occurrences of
	 * w2, all multiplied by the indegree of its url in the associated graph. No
	 * pages with rank zero are included.
	 *
	 *
	 * @param w1 first keyword to search for
	 * @param w2 second keyword to search for
	 * @return ranked list of urls
	 */
    @SuppressWarnings("Duplicates")
	public List<TaggedVertex<String>> searchWithAnd(String w1, String w2) {

		HashMap<String, URLRanking> rankingsInitial = new HashMap<String, URLRanking>();
		HashMap<String, URLRanking> rankingsFinal = new HashMap<String, URLRanking>();

		// Grab the list of URLS associated with a given word
		List<TaggedVertexWordCount> w1WordList = map.get(w1);
		List<TaggedVertexWordCount> w2WordList = map.get(w2);

		// Look at all the URLs that contain w1, compute the running rank, Add all to initial map
		if (w1WordList != null) {
			for (int i = 0; i < w1WordList.size(); i++) {
				TaggedVertexWordCount thisCount = w1WordList.get(i);
				//Compute the running rank given the number of time w1 is found on this page
				int runningRank = thisCount.getWordCount();
				TaggedVertex<String> url = new TaggedVertex<String>(thisCount.getTaggedVertex().getVertexData(), 0);
				//Create a URL ranking to keep track of running rank
				URLRanking toAdd = new URLRanking(url);
				toAdd.setRunningRank(runningRank);
				toAdd.setInDegree(thisCount.getTaggedVertex().getTagValue());
				rankingsInitial.put(thisCount.getTaggedVertex().getVertexData(), toAdd);
			}
		}
		// Look at all the URLs that contain w2, compute the running rank, Add all to final map only if
		// this word is in the initial map, which means w1 & w2 are on the same page
		if (w2WordList != null) {
			for (int i = 0; i < w2WordList.size(); i++) {
				TaggedVertexWordCount thisCount = w2WordList.get(i);
				int runningRank = thisCount.getWordCount();
				String currentURL = thisCount.getTaggedVertex().getVertexData();
				// Was the page already found when searching for word 1? if so it will be in the final map.
				if (rankingsInitial.containsKey(currentURL)) {
					// Current rank from finding w1
					int currentRank = rankingsInitial.get(currentURL).getRunningRank();
					//Create a new element to add to final map, using second map will get rid of any extra urls from the first map,
					//that w2 isn't in
					TaggedVertex<String> url = new TaggedVertex<String>(thisCount.getTaggedVertex().getVertexData(), 0);
					URLRanking toAdd = new URLRanking(url);
					toAdd.setInDegree(thisCount.getTaggedVertex().getTagValue());
					toAdd.setRunningRank(currentRank + runningRank);
					rankingsFinal.put(currentURL, toAdd);
				}
			}
		}

		// List to be sorted
		List<URLRanking> rankedList = new ArrayList<>();

		// Compute the final ranking for all pages that contain both w1 & w2
		for (Map.Entry<String, URLRanking> entry : rankingsFinal.entrySet()) {

			int finalRank = entry.getValue().getRunningRank() * entry.getValue().getInDegree();

			TaggedVertex<String> urlRank = new TaggedVertex<String>(entry.getValue().url.getVertexData(), finalRank);

			entry.getValue().setUrl(urlRank);

			rankedList.add(entry.getValue());

		}

		// Sort in O(nlogn), dominating runtime
		Collections.sort(rankedList);

		List<TaggedVertex<String>> toRet = new ArrayList<>();

		// Copy to final return list in O(V)
		for (URLRanking urlRank : rankedList) {
			toRet.add(urlRank.getUrl());
		}

		return toRet;
	}

	/**
	 * Searches the index for pages containing at least one of the keywords w1 and
	 * w2. Returns a list of qualifying urls ordered by ranking (largest to
	 * smallest). The tag value associated with each url is its ranking. The ranking
	 * for a given page is the number of occurrences of w1 plus number of
	 * occurrences of w2, all multiplied by the indegree of its url in the
	 * associated graph. No pages with rank zero are included.
	 *
	 * @param w1 first keyword to search for
	 * @param w2 second keyword to search for
	 * @return ranked list of urls
	 */
    @SuppressWarnings("Duplicates")
	public List<TaggedVertex<String>> searchWithOr(String w1, String w2) {

		HashMap<String, URLRanking> rankings = new HashMap<String, URLRanking>();

		// Grab the list of URLS associated with a given word
		List<TaggedVertexWordCount> w1WordList = map.get(w1);
		List<TaggedVertexWordCount> w2WordList = map.get(w2);

		// Look at all the URLs that contain w1, compute the running rank
		if (w1WordList != null) {
			for (int i = 0; i < w1WordList.size(); i++) {
				TaggedVertexWordCount thisCount = w1WordList.get(i);
				//Compute the running rank
				int runningRank = thisCount.getWordCount();
				TaggedVertex<String> url = new TaggedVertex<String>(thisCount.getTaggedVertex().getVertexData(), 0);
				URLRanking toAdd = new URLRanking(url);
				toAdd.setRunningRank(runningRank);
				toAdd.setInDegree(thisCount.getTaggedVertex().getTagValue());
				rankings.put(thisCount.getTaggedVertex().getVertexData(), toAdd);
			}
		}
		if (w2WordList != null) {
			// Look at all the URLs that contain w2 compute the running rank
			for (int i = 0; i < w2WordList.size(); i++) {
				TaggedVertexWordCount thisCount = w2WordList.get(i);
				int runningRank = thisCount.getWordCount();
				String currentURL = thisCount.getTaggedVertex().getVertexData();
				// Was the page already found when searching for word 1?
				if (rankings.containsKey(currentURL)) {
					// Current rank from finding w1
					int currentRank = rankings.get(currentURL).getRunningRank();
					// Update the ranking with the word count of w2
					rankings.get(currentURL).setRunningRank(currentRank + runningRank);
				} else // else page was not found when searching for word 1
				{
					TaggedVertex<String> url = new TaggedVertex<String>(thisCount.getTaggedVertex().getVertexData(), 0);
					URLRanking toAdd = new URLRanking(url);
					toAdd.setRunningRank(runningRank);
					toAdd.setInDegree(thisCount.getTaggedVertex().getTagValue());
					rankings.put(thisCount.getTaggedVertex().getVertexData(), toAdd);
				}
			}
		}
		
		// List to be sorted
		List<URLRanking> rankedList = new ArrayList<>();

		// Compute the final ranking for all pages
		for (Map.Entry<String, URLRanking> entry : rankings.entrySet()) {

			int finalRank = entry.getValue().getRunningRank() * entry.getValue().getInDegree();

			TaggedVertex<String> urlRank = new TaggedVertex<String>(entry.getValue().url.getVertexData(), finalRank);

			entry.getValue().setUrl(urlRank);

			rankedList.add(entry.getValue());

		}

		// Sort in O(nlogn)
		Collections.sort(rankedList);

		List<TaggedVertex<String>> toRet = new ArrayList<>();

		// Copy to final return list
		for (URLRanking urlRank : rankedList) {
			toRet.add(urlRank.getUrl());
		}

		return toRet;
	}

	/**
	 * Searches the index for pages containing keyword w1 but NOT w2. Returns a list
	 * of qualifying urls ordered by ranking (largest to smallest). The tag value
	 * associated with each url is its ranking. The ranking for a given page is the
	 * number of occurrences of w1, multiplied by the indegree of its url in the
	 * associated graph. No pages with rank zero are included.
	 *
	 * @param w1 first keyword to search for
	 * @param w2 second keyword to search for
	 * @return ranked list of urls
	 */
	@SuppressWarnings("Duplicates")
	public List<TaggedVertex<String>> searchAndNot(String w1, String w2) {
        List<TaggedVertexWordCount> wordList = map.get(w1);
        if(wordList==null){return null;} //word 1 not found at all

        List<URLRanking> rankingList = new ArrayList<>();

        // Go through all TaggedVertexWordCount for a list, add them to a list in a new coupling object
        for (int i = 0; i < wordList.size(); i++) { //O(V)
            TaggedVertexWordCount thisCount = wordList.get(i);
            String NotString = w2+""+thisCount.getTaggedVertex().getVertexData();
            //If word2+url is not in helper map then skip this one
            if (helperMap.containsKey(NotString)) continue;
            int rank = thisCount.getWordCount() * thisCount.getTaggedVertex().getTagValue();
            TaggedVertex<String> url = new TaggedVertex<String>(thisCount.getTaggedVertex().getVertexData(), rank);
            rankingList.add(new URLRanking(url));
        }

        Collections.sort(rankingList); // Uses TimSort, worse case O(VlogV) - bounding time of this methods
        List<TaggedVertex<String>> toRet = new ArrayList<>();

        // Copy to correct output list in O(V)
        for (int j = 0; j < rankingList.size(); j++) {
            toRet.add(rankingList.get(j).getUrl());
        }

        return toRet;
	}
}
