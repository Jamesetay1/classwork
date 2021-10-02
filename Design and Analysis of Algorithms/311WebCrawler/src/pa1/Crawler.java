package pa1;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import api.*;

/**
 * Implementation of a basic web crawler that creates a graph of some portion of
 * the world wide web.
 *
 * @author Zach DeMaris
 * @author James Taylor
 */
public class Crawler {
	private String seedUrl;
	private int maxDepth;
	private int maxPages;
	protected static int currentRequests = 0;

	/**
	 * Constructs a Crawler that will start with the given seed url, including only
	 * up to maxPages pages at distance up to maxDepth from the seed url.
	 *
	 * @param seedUrl
	 * @param maxDepth
	 * @param maxPages
	 */
	public Crawler(String seedUrl, int maxPages, int maxDepth) {
		this.seedUrl = seedUrl;
		this.maxDepth = maxDepth;
		this.maxPages = maxPages;
	}

	/**
	 * Creates a web graph for the portion of the web obtained by a BFS of the web
	 * starting with the seed url for this object, subject to the restrictions
	 * implied by maxDepth and maxPages.
	 *
	 * @return an instance of Graph representing this portion of the web
	 */
	public Graph<String> crawl() {
		TwoWayGraph<String> webGraph = new TwoWayGraph<>();
		int currentDepth = 0; // How many layers are we away from the root
		int currentPages = 1; // Total number of vertexes in graph
		Document doc = null;

		try {
			Crawler.currentRequests++;
			doc = Jsoup.connect(seedUrl).get();
		} catch (UnsupportedMimeTypeException e) {
			System.out.println("--Seed is unsupported document type, exiting...");
			return null;
		} catch (HttpStatusException e) {
			System.out.println("--Seed is an invalid link, exiting...");
			return null;
		} catch (MalformedURLException e) {
			System.out.println("--Seed is a malformed URL, exiting...");
			return null;
		} catch (IOException e) {
			System.out.println("--Seed threw IO Exception, exiting...");
			return null;
		} catch (Exception e) {
			System.out.println("something wacky happaned while connecting to seed URL, do nothing");
			return null;
		}

		LinkedList<StringDocument> queue = new LinkedList<StringDocument>();
		queue.add(new StringDocument(seedUrl, doc));
		queue.add(null);
		while (queue.size() != 0 && currentDepth <= maxDepth && currentPages <= maxPages) {
			// Grab the current url
			StringDocument stringDoc = queue.removeFirst();
			if (stringDoc == null) {
				currentDepth++;
				queue.add(null);
				continue;
			}
			
			// Grab the page and links
			Elements links = stringDoc.getDoc().select("a[href]");
			for (Element link : links) {
				String url = link.attr("abs:href");
				// Check for visited here?
				if (!Util.ignoreLink(stringDoc.getUrl(), url)) {
					
					//If this edge already exists move on
					if (webGraph.hasEdge(stringDoc.getUrl(), url))
						continue;
					
					//If the graph already has this url just go ahead and add the edge
					if(webGraph.hasVertex(url))
					{
						webGraph.addEdge(stringDoc.getUrl(), url);
					}
					else if(currentPages < maxPages && currentDepth < maxDepth) {

						if (Crawler.politnessCheck()) {
							Crawler.currentRequests = 0;
						}
						try {
							Crawler.currentRequests++;
							long start_time = System.currentTimeMillis();
							doc = Jsoup.connect(url).get();
							System.out.println(System.currentTimeMillis() - start_time + "ms to connect to " + url);
							queue.add(new StringDocument(url, doc));
							webGraph.addEdge(stringDoc.getUrl(), url);
							
							currentPages++;

						} catch (UnsupportedMimeTypeException e) {

							System.out.println(url + " is an unsupported document type, do nothing");
							continue;
						} catch (HttpStatusException e) {
							System.out.println("Catch: " + url);
							System.out.println(url + " is an invalid link, do nothing");
							continue;
						} catch (MalformedURLException e) {
							System.out.println(url + " is a Malformed URL, do nothing");
							continue;
						} catch (IOException e) {
							System.out.println(url + "IO Exception, do nothing");
							continue;
						} catch (Exception e) {
							System.out.println("At" + url + " something wacky happened while connecting, do nothing");
						}
					}
		
				}
			}
		}
		
		return webGraph;
	}

	protected static boolean politnessCheck() {
		if (Crawler.currentRequests > 50) {
			try {
				System.out.println("Sleep");
				Thread.sleep(3000);
			} catch (InterruptedException ignore) {

			}
			return true;
		}

		return false;
	}
}
