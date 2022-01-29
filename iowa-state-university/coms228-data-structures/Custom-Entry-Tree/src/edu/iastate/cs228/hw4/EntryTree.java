package edu.iastate.cs228.hw4;

import java.util.Arrays;

/**
 * @author James Taylor
 *
 *         An entry tree class Add Javadoc comments to each method
 */
public class EntryTree<K, V> {
	/**
	 * dummy root node made public for grading
	 */
	protected Node root;

	/**
	 * prefixlen is the largest index such that the keys in the subarray keyarr
	 * from index 0 to index prefixlen - 1 are, respectively, with the nodes on
	 * the path from the root to a node. prefixlen is computed by a private
	 * method shared by both search() and prefix() to avoid duplication of code.
	 */
	protected int prefixlen;

	protected class Node implements EntryNode<K, V> {
		protected Node child; // link to the first child node
		protected Node parent; // link to the parent node
		protected Node prev; // link to the previous sibling
		protected Node next; // link to the next sibling
		protected K key; // the key for this node
		protected V value; // the value at this node

		public Node(K aKey, V aValue) {
			key = aKey;
			value = aValue;
			child = null;
			parent = null;
			prev = null;
			next = null;
		}

		@Override
		public EntryNode<K, V> parent() {
			return parent;
		}

		@Override
		public EntryNode<K, V> child() {
			return child;
		}

		@Override
		public EntryNode<K, V> next() {
			return next;
		}

		@Override
		public EntryNode<K, V> prev() {
			return prev;
		}

		@Override
		public K key() {
			return key;
		}

		@Override
		public V value() {
			return value;
		}
	}

	/**
	 * An internal method that prints all the Data for a Node Used in creating
	 * and debugging. May be useful for future changes
	 * 
	 * @param x
	 *            The node the data will be printed for
	 * @param NodeName
	 *            The name that node is given (for clarity)
	 */
	@SuppressWarnings("unused")
	private void printData(Node x, String NodeName) {
		System.out.println("Data for " + NodeName + " :");
		System.out.println("-------------------------");
		System.out.println("Key:" + x.key);
		System.out.println("Value: " + x.value);
		System.out.println("Child: " + x.child);
		System.out.println("Parent: " + x.parent);
		System.out.println("Next: " + x.next);
		System.out.println("Prev: " + x.prev);
	}

	public EntryTree() {
		root = new Node(null, null);
	}

	/**
	 * Returns the value of the entry with a specified key sequence, or null if
	 * this tree contains no entry with the key sequence.
	 * 
	 * @param keyarr
	 *            The Keyarr we are searching for
	 * @return The value of the given keyarr (if it exists)
	 */
	public V search(K[] keyarr) {

		Node x = findNode(keyarr);

		if (x == null) {
			return null;
		}

		return x.value;
	}

	/**
	 * The method returns an array of type K[] with the longest prefix of the
	 * key sequence specified in keyarr such that the keys in the prefix label
	 * the nodes on the path from the root to a node. The length of the returned
	 * array is the length of the longest prefix.
	 * 
	 * @param keyarr
	 *            The keyarr we are looking for the prefix of in our existing
	 *            tree
	 * @return The Array of <K> that exists in our tree already.
	 */
	public K[] prefix(K[] keyarr) {

		findNode(keyarr);

		if (prefixlen == 0) {
			return null;
		}

		K[] toReturn = Arrays.copyOf(keyarr, prefixlen);
		return toReturn;
	}

	/**
	 * A method that will find any node of a given key array and will return
	 * that node. This is used in other methods to traverse through our tree.
	 * 
	 * @param keyarr
	 *            The key arr we are looking for
	 * @return The Node we were looking for (if it exists). Otherwise Null.
	 * 
	 * @throws NullPointerException
	 *             if any part of keyarr is null.
	 */
	private Node findNode(K[] keyarr) {
		if ((keyarr == null) || (keyarr.length == 0)) {
			return null;
		}

		for (int i = 0; i < keyarr.length; i++) {
			if (keyarr[i] == null) {
				throw new NullPointerException();
			}
		}

		Node cur = root;
		int index = 0;
		prefixlen = 0;

		while (cur.child != null) {// While we can move down.

			K lookingFor = keyarr[index];// what we are trying to find.
			cur = cur.child;// move down 1.
			cur = findSibling(cur, lookingFor);// look thru all the kids.

			if (cur == null) {// if it can't find it, then the whole method
								// should return null.
				return null;
			}

			index++;// find the next thing.

			if (index == keyarr.length) {// if we are the end.. break out. we
											// found it.
				prefixlen++;
				break;
			}

			prefixlen++;// for our prefix method.

		}
		if (index != keyarr.length) {
			return null;
		}
		return cur;

	}

	/**
	 * A method that goes along the siblings to see if it can find what we are looking for.
	 * Helper method to FindNode
	 * 
	 * @param node
	 *            The node we are starting at.
	 * @param toFind
	 *            The node key we are trying to find
	 * 
	 * @Return Node if we find a match (otherwise null).
	 */
	private Node findSibling(Node node, K key) {

		while (node != null) {
			if (node.key == key) {
				return node;
			}

			node = node.next;
		}

		return null;
	}
	/**
	 * The method locates the node P corresponding to the longest prefix of the
	 * key sequence specified in keyarr such that the keys in the prefix label
	 * the nodes on the path from the root to the node. If the length of the
	 * prefix is equal to the length of keyarr, then the method places aValue at
	 * the node P and returns true. Otherwise, the method creates a new path of
	 * nodes (starting at a node S) labelled by the corresponding suffix for the
	 * prefix, connects the prefix path and suffix path together by making the
	 * node S a child of the node P, and returns true.
	 * 
	 * @param keyarr
	 *            The keyarr we are adding
	 * @param aValue
	 *            The value we are adding at aforementioned keyarray.
	 * @return true if an add was successful, otherwise false.
	 */
	public boolean add(K[] keyarr, V aValue) {

		 
		if (keyarr == null || keyarr.length == 0 || aValue == null) {
			return false;
		}

		K[] result = prefix(keyarr);
		int suffixEnd = keyarr.length;
		int suffixStart = prefixlen;
		Node cur;
		
		// REPLACE CASE. NO ADDITIONS.
		if (prefixlen == suffixEnd) {
			Node replace = findNode(keyarr);
			replace.value = aValue;
			return true;
		}
		
		// Sets the cur position based on prefixlen
		
		if (prefixlen == 0) {
			cur = root;
		} else {
			cur = findNode(result);
		}

		for (int i = suffixStart; i < suffixEnd; i++) {

			// Decides whether we are putting an entry or just a node along the
			// way.
			Node N;
			if (i == suffixEnd - 1) {
				N = new Node(keyarr[i], aValue);
			} else {
				N = new Node(keyarr[i], null);
			}

			// CASE IF WE HAVE NO CHILD.
			if (cur.child == null) { // if we have no child... simply add under
				cur.child = N;
				N.parent = cur;
			} else { // CASE IF WE HAVE A CHILD ALREADY. Must move to side.
				N.parent = cur;
				cur = cur.child;
				
				while (cur.next != null) {// move to the end of the siblings							
					cur = cur.next;
				}
				//when we get to the end set it there
				cur.next = N;
				N.prev = cur;
			}
			cur = N;
		}
		return true;
	}

	/**
	 * Removes the entry for a key sequence from this tree and returns its value
	 * if it is present. Otherwise, it makes no change to the tree and returns
	 * null.
	 * 
	 * @param keyarr
	 *            The keyarr of the entry we are removing
	 * @return The value we removed
	 */
	public V remove(K[] keyarr) {
	
	if (findNode(keyarr) == null) {
			return null;
		} else { // if the element exists... (We work to remove)

			// traverse to node, take out value.
			Node cur = findNode(keyarr);
			V value = cur.value;
			cur.value = null;

			if (cur.child == null) { // If there is nothing underneath
				
				while (cur.parent != null) {
					
					cur = cur.parent; // go up the tree

					if (cur.value == null) { // and if the value is null
						
						Node curKid = cur.child;

						if (cur.child.next == null) { // If cannot go right.
							// erase this node.
							cur.child = null;
							curKid.key = null;
							curKid.value = null;
							curKid.parent = null;

						} else {// Otherwise...
							// move to the next, and erase the one behind it.
							curKid = cur.child.next;
							curKid.parent = curKid.prev.parent;// set new parent
							
							Node lastKid = curKid.prev;//erase the last kid.
							
							lastKid.key = null;
							lastKid.value = null;
							lastKid.parent = null;
							lastKid.prev = null;

							return value;
						}

					} else {// if the value is not null, return since we can't remove a non null value.
						return value;
					}
				}
			}
			return value;
		}
	}

	/**
	 * The method prints the tree on the console in the output format shown in
	 * an example output file.
	 */
	public void showTree() {
		showTreeRec(root, 0);
	}

	/**
	 * This method is recursively called to print out the tree.
	 * 
	 * @param node
	 *            The node we are looking at.
	 * @param numSpaces
	 *            The number of spaces that must be printed on this line.
	 */
	private void showTreeRec(Node node, int numSpaces) {
		if ((node == root) && (node == null)) {// BASE CASE
			return;
		}

		printSpaces(numSpaces);//method to print spaces is modularized.

		System.out.println(node.key() + "->" + node.value());

		if (node.child != null)// UNDER CASE
			showTreeRec(node.child, numSpaces + 1);
		if (node.next != null)// OVER CASE
			showTreeRec(node.next, numSpaces);
	}

	/**
	 * A helper method that will print the correct number of spaces for the
	 * recursive method
	 * 
	 * @param num
	 *            Number of Spaces that will be printed (Here, a space is 2
	 *            spaces). It functions as an index max of sorts
	 */
	private void printSpaces(int num) {
		for (int i = 0; i < num; i++) {
			System.out.print("  ");
		}
	}

}
