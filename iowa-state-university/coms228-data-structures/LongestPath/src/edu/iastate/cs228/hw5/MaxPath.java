package edu.iastate.cs228.hw5;
/**
 * @author James Taylor
 */

import java.util.HashMap;
import java.util.HashSet;

public class MaxPath {


/**
 * A method to find the maximum path in a directed, acyclic graph.
 * Uses a modified form of Djikstras algorithm, involving a dist and pred map.
 * 
 * @param G The graph we are finding the max path on.
 * @param maxPath The maximum path of vertexes.
 * @return The maximum path.
 */
public static <V> Integer findMaxPath(DiGraph<V> G, LinkedStack<V> maxPath) {
		
	if (G==null || maxPath==null){
			throw new IllegalArgumentException("null arguments");
	}
		
	if (!maxPath.isEmpty()){
			throw new IllegalArgumentException("maxPath is not empty");
	}
		
	LinkedStack<V> topoOrder = DFS.depthFirstSearch(G);
		
	if (topoOrder==null){
			throw new IllegalArgumentException("The graph has a cycle");
	}
		
	if (topoOrder.isEmpty()){
			throw new IllegalStateException("topoOrder is empty");
	}
		
		HashMap<V, Integer> dist = new HashMap<V, Integer>();
		HashMap<V, V> pred = new HashMap<V, V>();
		HashSet<V> vset = new HashSet<V>();
		
		
		for (V w : G.vertices()) {
			dist.put(w, 0);
			pred.put(w, null);
		}
		
		int score = 0;
		V endingVertex = null;
		
		while (!topoOrder.isEmpty()) {
			
			V u = topoOrder.pop();
			
			if (!vset.contains(u)) {
				
				vset.add(u);
				
				for (Edge<V, Integer> tup: G.adjacentTo(u)) {
					V v = tup.getVertex();
					Integer altdist = dist.get(u) + tup.getCost();
					Integer vdist = dist.get(v);
					
					if (vdist == null || vdist < altdist) {
						dist.put(v, altdist);
						pred.put(v, u);
						score = altdist;
						endingVertex = v;
					}
				}
			}
		}
		// as long as the ending vertex is not null then push ending vertex onto maxPath
		//and get the next endingVertex from predMap.
		while (endingVertex != null) {
			maxPath.push(endingVertex);
			endingVertex = pred.get(endingVertex);
		}
		return score;
	}
}



