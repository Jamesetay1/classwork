package pa1;

import api.TaggedVertex;

/**
 * Coupling object that holds a tagged vertex and a corresponding word count
 */
public class TaggedVertexWordCount {

    private TaggedVertex<String> taggedVertex;
    private int wordCount;

    public TaggedVertexWordCount(TaggedVertex<String> tV, int wC) {

        this.taggedVertex = tV;
        this.wordCount = wC;

    }

    public TaggedVertex<String> getTaggedVertex() {
        return taggedVertex;
    }

    public void setTaggedVertex(TaggedVertex<String> taggedVertex) {
        this.taggedVertex = taggedVertex;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void incrementWordCount() {
        this.wordCount++;
    }

    public String toString() {
        return "Tagged Vertex: " + taggedVertex.getVertexData() + " : " + taggedVertex.getTagValue() + ", wordCount: " + wordCount + "\n";
    }

}
