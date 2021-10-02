package pa1;

import api.TaggedVertex;

/**
 * URL Ranking holds a tagged vertex (Url and rank)
 * the inDegree of such a vertex, and the running rank used while computing
 *
 * @author Zach DeMaris
 * @author James Taylor
 */
public class URLRanking implements Comparable<URLRanking> {

    TaggedVertex<String> url;
    private int inDegree;
    private int runningRank;

    public URLRanking(TaggedVertex<String> url) {
        this.url = url;
        this.inDegree = 0;
        this.runningRank = 0;
    }

    public TaggedVertex<String> getUrl() {
        return url;
    }

    public void setUrl(TaggedVertex<String> url) {
        this.url = url;
    }

    public int getInDegree() {
		return inDegree;
	}

	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}

	public int getRunningRank() {
		return runningRank;
	}

	public void setRunningRank(int runningRank) {
		this.runningRank = runningRank;
	}
	
	@Override
	public String toString()
	{
		return " runningRank: " + runningRank + " incomingEdges: " + inDegree + "\n";
	}

	@Override
    public int compareTo(URLRanking anotherURL) {
    	return anotherURL.getUrl().getTagValue() - this.getUrl().getTagValue();
    }

}
