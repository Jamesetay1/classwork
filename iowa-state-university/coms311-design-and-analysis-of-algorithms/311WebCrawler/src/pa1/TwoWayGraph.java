package pa1;

import api.Graph;
import api.TaggedVertex;

import java.util.*;

/**
 * Implementation of Graph Interface using a HashMap
 *
 * @author Zach DeMaris
 * @author James Taylor
 **/
public class TwoWayGraph<E> implements Graph {

    private E rootNode;

    //Using LinkedHashMaps for outward and inward Adjacency list due to efficency
    private LinkedHashMap<E, List<E>> outAdj;
    private LinkedHashMap<E, List<E>> inAdj;

    //Maps each vertex to its known index
    //Can use an index because we are using Linked hashmap
    private LinkedHashMap<E, Integer> indexMap;
    private int indexMapIndex = -1;

    //This arraylist keeps track of what vertex is at which index.
    //We can do this because we are using a linked hash map, where index won't change
    private ArrayList<E> indexList = new ArrayList<>();

    //Running count of each nodes incoming edge count, updated on edge add.
    private ArrayList<TaggedVertex<E>> incomingEdgeCount = new ArrayList<>();

    //Adjacency List for indicies
    private ArrayList<ArrayList<Integer>> outIndexAdj;
    private ArrayList<ArrayList<Integer>> inIndexAdj;

    //HashMap for keeping track of specific edges
    private LinkedHashMap<String, Integer> edgesHash;
    
    public TwoWayGraph() {
        this.outAdj = new LinkedHashMap<>();
        this.inAdj = new LinkedHashMap<>();
        this.indexMap = new LinkedHashMap<>();
        this.outIndexAdj = new ArrayList<>();
        this.inIndexAdj = new ArrayList<>();
        this.edgesHash = new LinkedHashMap<>();
    }

    // This function adds the edge
    // between source to destination
    public void addVertex(E vertex) {//O(1)
        if (outAdj.size()==0) rootNode = vertex;
        if (hasVertex(vertex)) return;
        outAdj.put(vertex, new LinkedList<>());
        inAdj.put(vertex, new LinkedList<>());
        indexMap.put(vertex, ++indexMapIndex);
        outIndexAdj.add(new ArrayList<>());
        inIndexAdj.add(new ArrayList<>());

        indexList.add(vertex);

        //Add to incomingEdgeCount with a size of 1
        int incomingSize  = vertex.equals(rootNode)? (inAdj.get(vertex).size()): inAdj.get(vertex).size();
        TaggedVertex<E> newVertex = new TaggedVertex<>(vertex, incomingSize);
        incomingEdgeCount.add(newVertex);
    }

    public void addEdge(E from, E to) {//O(1)
        //outAdj and inAdj must have the same
        //vertices, so we will only check one

        if (from.equals(to)) return; //no self edges!
        if (!outAdj.containsKey(from))
            addVertex(from);
        if (!outAdj.containsKey(to)) {
            addVertex(to);
        }
        if (hasEdge(from, to)){
            System.out.println("TRYING TO ADD DUPLICATE EDGE FROM: " + from + " TO " + to);
                    return; //O(V)
        }

        outAdj.get(from).add(to);
        inAdj.get(to).add(from);
        outIndexAdj.get(indexMap.get(from)).add(indexMap.get(to));
        inIndexAdj.get(indexMap.get(to)).add(indexMap.get(from));

        incomingEdgeCount.set(indexMap.get(to), new TaggedVertex<>(to, inAdj.get(to).size()));
        edgesHash.put(from+""+to, null);
    }

    // This function gives whether
    // a vertex is present or not.
    public boolean hasVertex(E s)//O(1)
    {
        if (outAdj.containsKey(s)) {
            return true;
        }
        return false;
    }

    // This function gives whether an edge is present or not.
    public boolean hasEdge(E s, E d)//O(1)
    {
        //System.out.println("edgesHash:" + edgesHash);
        if (edgesHash.containsKey(s+""+d)){
            System.out.println("TRYING TO ADD DUPLICATE EDGE FROM: " + s + " TO " + d);
            return true;
        }
        return false;
    }

    // Prints the adjacency list of each vertex.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (E v : outAdj.keySet()) {
            builder.append(v.toString() + ": ");
            for (E w : outAdj.get(v)) {
                builder.append(w.toString() + " ");
            }
            builder.append("\n");
        }

        return (builder.toString());
    }

    /**SPECIFIED IN INTERFACE**/
    /**
     * Returns an ArrayList of the actual objects constituting the vertices
     * of this graph.
     *
     * @return ArrayList of objects in the graph
     */
    public ArrayList<E> vertexData() {//O(1)
        return indexList;
    }

    /**
     * Returns an ArrayList that is identical to that returned by vertexData(), except
     * that each vertex is associated with its incoming edge count.
     *
     * @return ArrayList of objects in the graph, each associated with its incoming edge count
     */
    public ArrayList<TaggedVertex<E>> vertexDataWithIncomingCounts() {//O(1)
        //Give seed url its free incoming edge for being the seed and return
        int rootCount = incomingEdgeCount.get(0).getTagValue() + 1;
        E rootData = incomingEdgeCount.get(0).getVertexData();
        incomingEdgeCount.set(0, new TaggedVertex<>(rootData, rootCount));
        return incomingEdgeCount;

    }

    /**
     * Returns a list of outgoing edges, that is, a list of indices for neighbors
     * of the vertex with given index.
     * This method may throw ArrayIndexOutOfBoundsException if the index
     * is invalid.
     *
     * @param index index of the given vertex according to vertexData()
     * @return list of outgoing edges
     */
    public List<Integer> getNeighbors(int index) {//O(1)
        return outIndexAdj.get(index);
    }

    /**
     * Returns a list of incoming edges, that is, a list of indices for vertices
     * having the given vertex as a neighbor.
     * This method may throw ArrayIndexOutOfBoundsException if the index
     * is invalid.
     *
     * @param index index of the given vertex according to vertexData()
     * @return list of incoming edges
     */
    public List<Integer> getIncoming(int index) {//O(1)
        return inIndexAdj.get(index);
    }
}

