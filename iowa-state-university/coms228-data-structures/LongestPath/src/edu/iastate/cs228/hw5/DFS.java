package edu.iastate.cs228.hw5;
/**
 * @author James Taylor
 */

import java.util.HashMap;
import java.util.Iterator;

public class DFS {
	

	/**
	 * A method that performs a Depth First Search of a graph.  It starts by coloring all
	 * vertex "green".  It then goes through them all and as long as the method visitDFS does 
	 * not return false on a green vertex, then it will return the topological order of the graph.
	 * 
	 * @throws IllegalArgumentException if aGraph is null.
	 * @param aGraph That the Depth First Search will be performed on.
	 * @return The Topological Order of the Graph.
	 */
	public static <V> LinkedStack<V> depthFirstSearch(DiGraph<V> aGraph) {
		// if aGraph is null throws IllegalArgumentException
		if (aGraph == null) {
			throw new IllegalArgumentException();
		}

		// creating color, pred, and empty linked stack.
		HashMap<V, String> color = new HashMap<V, String>();
		HashMap<V, V> pred = new HashMap<V, V>();
		LinkedStack<V> topoOrder = new LinkedStack<V>();

		// Each vertex is colored green in color map and null in pred map.
		for (V w : aGraph.vertices()) {
			color.put(w, "green");
			pred.put(w, null);
		}

		// as long as green vertex w left, method calls visit DFS.
		// if false, return null
		// if true, returns the stack in topo order.
		boolean visitDFSbool = false;
		for (V w : aGraph.vertices())
			if (color.get(w).equals("green")) {
				visitDFSbool = visitDFS(aGraph, w, color, pred, topoOrder);
				if (!visitDFSbool) {
					return null;	
				}
			}
		return topoOrder;	
	}

/**
 * This method helps to determine if part of a graph is cyclic.
 * It will look at connected vertex and mark them as reached, reached and processed, or invalid.
 * 
 * @param aGraph  The graph we are working with
 * @param s       The vertex we are working with
 * @param color  The given color
 * @param pred   The pred map given
 * @param topoOrder  The topological order of the vertexes (saved at the end of this method).
 * @return if we do not find a cycle (if we do not find a red edge).
 */

	protected static <V> boolean visitDFS(DiGraph<V> aGraph, V s,
			HashMap<V, String> color, HashMap<V, V> pred,
			LinkedStack<V> topoOrder) {

		color.put(s, "red"); // reached but not processed
		LinkedStack<V> nodestack = new LinkedStack<V>();
		LinkedStack<Iterator<Edge<V, Integer>>> edgestack = new LinkedStack<Iterator<Edge<V, Integer>>>();
		Iterator<Edge<V, Integer>> siter = aGraph.adjacentTo(s).iterator();
		
		nodestack.push(s);
		edgestack.push(siter);

		while (!nodestack.isEmpty()){
			V c = nodestack.peek();
			Iterator<Edge<V, Integer>> citer = edgestack.peek();

			if (citer.hasNext()) {
				V w = citer.next().getVertex();

				if (color.get(w).equals("green")) {
					color.put(w, "red"); // reached but not processed
					pred.put(w, c);
					Iterator<Edge<V, Integer>> witer = aGraph.adjacentTo(w).iterator();
					nodestack.push(w);
					edgestack.push(witer);
				}
				// Case if there is still a red at the end.
				else if (color.get(w).equals("red")) {
					return false;
				}
			} else {
				color.put(c, "black"); // processed
				topoOrder.push(c);
				nodestack.pop(); // vertex c is removed
				edgestack.pop(); // its edge iterator is removed
				
			}
		}
		return true; 
	}

}
