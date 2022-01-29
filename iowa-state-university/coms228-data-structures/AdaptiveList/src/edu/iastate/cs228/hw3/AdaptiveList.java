package edu.iastate.cs228.hw3;
/**
 *  @author James Taylor
 *
 *  An implementation of List<E> based on a doubly-linked list with an array for indexed reads/writes
 *
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class AdaptiveList<E> implements List<E> {
	/**
	 * A node of our list that contains some item (data).
	 * Also has a connecting link and prev so it knows what is in front
	 * of, and behind it.
	 */
	public class ListNode // private member of outer class
	{
		public E data; // public members:
		public ListNode link; // used outside the inner class
		public ListNode prev; // used outside the inner class

		public ListNode(E item) {
			data = item;
			link = prev = null;
		}
	}

	public ListNode head; // dummy node made public for testing.
	public ListNode tail; // dummy node made public for testing.
	private int numItems; // number of data items
	private boolean linkedUTD; // true if the linked list is up-to-date.

	public E[] theArray; // the array for storing elements
	private boolean arrayUTD; // true if the array is up-to-date.

	public AdaptiveList() {
		clear();
	}

	@Override
	public void clear() {
		head = new ListNode(null);
		tail = new ListNode(null);
		head.link = tail;
		tail.prev = head;
		numItems = 0;
		linkedUTD = true;
		arrayUTD = false;
		theArray = null;
	}

	public boolean getlinkedUTD() {
		return linkedUTD;
	}

	public boolean getarrayUTD() {
		return arrayUTD;
	}

	/**
	 * Constructor when given a collection of things. Puts them into the linked
	 * list
	 * 
	 * @param c
	 *            - What will be put into our List.
	 */
	public AdaptiveList(Collection<? extends E> c) {
		this();
		for (E e : c) {
			add(e);
		}
		linkedUTD = true;
		arrayUTD = false;
		theArray = null;
	}

	// Removes the node from the linked list.
	// This method should be used to remove a node from the linked list.
	private void unlink(ListNode toRemove) {
		if (toRemove == head || toRemove == tail) {
			throw new RuntimeException("An attempt to remove head or tail");
		}
		toRemove.prev.link = toRemove.link;
		toRemove.link.prev = toRemove.prev;
	}

	// Inserts new node toAdd right after old node current.
	// This method should be used to add a node to the linked list.
	private void link(ListNode current, ListNode toAdd) {
		if (current == tail) {
			throw new RuntimeException("An attempt to link after tail");
		}
		if (toAdd == head || toAdd == tail) {
			throw new RuntimeException(
					"An attempt to add head/tail as a new node");
		}
		toAdd.link = current.link;
		toAdd.link.prev = toAdd;
		toAdd.prev = current;
		current.link = toAdd;
	}
	/**
	 * After checking to make sure there is numItems and that linked is Up to
	 * date, copies all of linked list into the Array.
	 * 
	 * @throws RunetimeException
	 *             if numItems <0
	 * @throws RuntimeException
	 *             if linkedUTD is false (linked list is not up to date)
	 */
	@SuppressWarnings("unchecked")
	private void updateArray() // makes theArray up-to-date.
	{
		if (numItems < 0) {
			throw new RuntimeException("numItems is negative: " + numItems);
		}
		if (!linkedUTD) {
			throw new RuntimeException("linkedUTD is false");
		}

		theArray = (E[]) new Object[numItems];

		for (int i = 0; i < theArray.length; i++) {
			theArray[i] = findNode(i + 1).data;
		}
		arrayUTD = true;
	}

	/**
	 * After checking to make sure there is numItem, array is Up to date, and
	 * the Array is not null or shorter than numItems, copies all of Array into
	 * the Linked List.
	 * 
	 * @throws RunetimeException
	 *             if numItems <0
	 * @throws RuntimeException
	 *             if arrayUTD is false (array is not up to date)
	 * @throws RuntimeException
	 *             if theArray is null or shoter than numItems
	 */
	private void updateLinked() // makes the linked list up-to-date.
	{
		linkedUTD = true;
		if (numItems < 0) {
			throw new RuntimeException("numItems is negative: " + numItems);
		}
		if (!arrayUTD) {
			throw new RuntimeException("arrayUTD is false");
		}

		if (theArray == null || theArray.length < numItems) {
			throw new RuntimeException("theArray is null or shorter");
		}

		head = new ListNode(null);
		tail = new ListNode(null);
		head.link = tail;
		tail.prev = head;
		numItems = 0;
		
		for (int i = 0; i < theArray.length; i++) {
			add(theArray[i]);
		}
	}

	@Override
	public int size() {
		if (!linkedUTD) {
			updateLinked();
		}

		arrayUTD = false;
		return numItems;

	}

	@Override
	public boolean isEmpty() {
		if (!linkedUTD) {
			updateLinked();
		}

		if (numItems == 0) {
			arrayUTD = false;
			return true;
		}
		arrayUTD = false;
		return false;
	}

	@Override
	public boolean add(E obj) {
		if (!linkedUTD) {
			updateLinked();
		}

		ListNode addafter = findNode(numItems);// finds last data node
		ListNode adding = new ListNode(obj);// makes new node based on obj

		link(addafter, adding);
		numItems++;
		
		arrayUTD = false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (!linkedUTD) {
			updateLinked();
		}
		if (c.isEmpty()) {
			return false;
		}
		if (this.equals(c)){
			Collection< ? extends E > temp = c;
			temp = new AdaptiveList<>(c);
			for(E e : temp){
				add(e);
			}
			arrayUTD = false;
			return true;
		}
		
		for (E e : c) {
			add(e);
		}
		arrayUTD = false;
		return true;
	} // addAll 1

	@Override
	public boolean remove(Object obj) {
		if (!linkedUTD)
			updateLinked();

		if (!contains(obj))
			return false;

		int pos = indexOf(obj);
		if (pos == 0)
			pos++;// possibly could remove, check indexOf for more.
		unlink(findNode(pos));
		numItems--;

		arrayUTD = false;

		return true;
	}

	private void checkIndex(int pos) // a helper method
	{
		if (pos >= numItems || pos < 0)
			throw new IndexOutOfBoundsException(
					"Index: " + pos + ", Size: " + numItems);
	}

	private void checkIndex2(int pos) // a helper method
	{
		if (pos > numItems || pos < 0)
			throw new IndexOutOfBoundsException(
					"Index: " + pos + ", Size: " + numItems);
	}

	private void checkNode(ListNode cur) // a helper method
	{
		if (cur == null || cur == tail)
			throw new RuntimeException(
					"numItems: " + numItems + " is too large");
	}

	private ListNode findNode(int pos) // a helper method
	{
		if (!linkedUTD)
			updateLinked();

		ListNode cur = head;
		for (int i = 0; i < pos; i++) {
			checkNode(cur);
			cur = cur.link;
		}
		checkNode(cur);

		arrayUTD = false;
		return cur;
	}

	@Override
	public void add(int pos, E obj) {
		if (!linkedUTD)
			updateLinked();

		checkIndex2(pos);
		ListNode addingAt = findNode(pos);
		ListNode adding = new ListNode(obj);
		link(addingAt, adding);
		arrayUTD = false;
		numItems++;
	}

	@Override
	public boolean addAll(int pos, Collection<? extends E> c) {
		if (!linkedUTD)
			updateLinked();
		checkIndex2(pos); // making sure we are trying to start outside of tail.
		if (c.isEmpty()) {
			return false;
		}
		
		if (this.equals(c)){
			Collection< ? extends E > temp = c;
			temp = new AdaptiveList<>(c);
			int i = pos;
			for(E e : temp){
				add(i++, e);
			}
			arrayUTD = false;
			return true;
		}
		
		int i = pos;
		for (E e : c) {
			add(i++, e);
		}
		arrayUTD = false;
		return true;
	} // addAll 2

	@Override
	public E remove(int pos) {
		if (!linkedUTD)
			updateLinked();
		checkIndex(pos);
		ListNode toRemove = findNode(pos + 1);
		unlink(toRemove);
		numItems--;
		arrayUTD = false;
		return toRemove.data;
	}

	@Override
	public E get(int pos) {
		if (!arrayUTD)
			updateArray();

		return theArray[pos];
	}

	@Override
	public E set(int pos, E obj) {
		checkIndex(pos);

		if (!arrayUTD)
			updateArray();

		E c = theArray[pos];
		theArray[pos] = obj;

		linkedUTD = false;

		return c;

	}

	/**
	 * if num items is = or less than 1, it cannot be reversed.
	 * updates array if not done, and then flips the indexes to 
	 * reverse the array with a for loop
	 * @return true - if array was changed.
	 */
	public boolean reverse() {
		if (numItems <= 1)
			return false;

		if (!arrayUTD)
			updateArray();

		int j = theArray.length - 1;
		for (int i = 0; i < theArray.length / 2; i++, j--) {
			E temp = theArray[i];
			theArray[i] = theArray[j];
			theArray[j] = temp;
		}

		linkedUTD = false;
		return true;
	}

	@Override
	public boolean contains(Object obj) {

		ListNode cur;
		for (cur = head; cur != null; cur = cur.link) {
			if (cur != head && cur != tail) {
				if (obj == cur.data || obj != null && obj.equals(cur.data)
						&& cur.data != null)
					return true;
			}
		}

		return false;

	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (!contains(o) && o != null) {
				return false;
			}

		return true;

	} // containsAll

	@Override
	public int indexOf(Object obj) {
		ListNode cur;
		int pos = 0;
		for (cur = head; cur != tail; cur = cur.link, pos++) {
			if (obj == cur.data || obj != null && obj.equals(cur.data)) {
				// Some adjustments to account for +1/-1 (or lack thereof) in
				// other places
				if (pos == 1)
					return 0;// if pos is right after the head, its technically
								// 0.
				if (pos > 0 && pos != numItems)
					pos--;// if pos is greater than 0 and not the max, index max
				return pos;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object obj) {
		ListIterator<E> iter = listIterator(numItems);
		while (iter.hasPrevious()) {
			E data = iter.previous();
			if (obj == data || obj != null && obj.equals(data))
				return iter.nextIndex();
		}
		return -1;
	}
	// REQUIRES ITER
	@Override
	public boolean removeAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException();

		boolean changed = false;
		ListIterator<E> iter = listIterator(0);

		while (iter.hasNext()) {
			E data = iter.next();
			if (c.contains(data)) {
				iter.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException();

		boolean changed = false;
		ListIterator<E> iter = listIterator(0);

		while (iter.hasNext()) {
			E data = iter.next();
			if (!c.contains(data)) {
				iter.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Object[numItems];
		ListIterator<E> iter = listIterator(0);

		for (int i = 0; i < numItems; i++)
			arr[i] = iter.next();

		return arr;

	}

	@Override
	public <T> T[] toArray(T[] arr) {
		
		if (arr.length < numItems)
			arr = Arrays.copyOf(arr, numItems);

		System.arraycopy(toArray(), 0, arr, 0, numItems);

		if (arr.length > numItems)
			arr[numItems] = null;

		return arr;
	}

	@Override
	public List<E> subList(int fromPos, int toPos) {
		throw new UnsupportedOperationException();
	}

	private class AdaptiveListIterator implements ListIterator<E> {
		private int index; // index of next node;
		private ListNode cur; // node at index - 1
		private ListNode last; // node last visited by next() or previous()
		private int lastCallNext = 0;

		/**
		 * Constructor for the Iterator if not given a position. Essentially,
		 * the position of index is set to 0.
		 */
		public AdaptiveListIterator() {
			if (!linkedUTD)
				updateLinked();

			last = null;
			index = 0;
			cur = findNode(index - 1);
		}
		/**
		 * Constructor for the Iterator if a pos is given. Starts at position
		 * given.
		 * 
		 * @param pos
		 *            - Where the iterator will begin.
		 */
		public AdaptiveListIterator(int pos) {
			if (!linkedUTD)
				updateLinked();

			checkIndex2(pos);
			index = pos;
			last = null;

			cur = findNode(index);

		}

		@Override
		public boolean hasNext() {
			return index < numItems;
		}

		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();

			if (index >= numItems)
				throw new RuntimeException("index 1 error, your index is: "
						+ index + " but num items is:  " + numItems);
			index++;
			last = cur == null ? head : cur.link;

			if (cur == null) {
				last = head;
			} else {
				last = cur.link;
			}

			cur = findNode(index);
			lastCallNext = 1;
			return cur.data; // cur != null

		}

		@Override
		public boolean hasPrevious() {
			return index > 0;

		}

		@Override
		public E previous() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			if (index <= 0) {
				throw new RuntimeException(
						"index 2 error. Your index is:" + index);
			}
			index--;

			last = cur;
			cur = findNode(index);
			lastCallNext = 0;
			return last.data;

		}

		@Override
		public int nextIndex() {
			return index;

		}

		@Override
		public int previousIndex() {
			return index - 1;

		}

		public void remove() {
			{
				if (last == null)
					throw new IllegalStateException();

				if (cur == last) {
					if (index <= 0)
						throw new RuntimeException("index 3");
					index--;
				}

				numItems--;

				if (last == head) {
					unlink(head.link);
				} else {
					unlink(last);
				}

				last = null;
			}
		}

		public void add(E obj) {
			cur = (findNode(index));
			ListNode toAdd = new ListNode(obj);

			numItems++;
			ListNode addAt = cur;
			link(addAt, toAdd);

			index++;
			cur = (findNode(index));

			last = null;
		} // add

		@Override
		public void set(E obj) {
			if (last == null)
				throw new IllegalStateException();

			if (lastCallNext == 1 && last == head) {
				last.link.data = obj;
			} else {
				last.data = obj;
			}
		}
	} // AdaptiveListIterator

	@Override
	public boolean equals(Object obj) {
		if (!linkedUTD)
			updateLinked();
		if ((obj == null) || !(obj instanceof List<?>))
			return false;
		List<?> list = (List<?>) obj;
		if (list.size() != numItems)
			return false;
		Iterator<?> iter = list.iterator();
		for (ListNode tmp = head.link; tmp != tail; tmp = tmp.link) {
			if (!iter.hasNext())
				return false;
			Object t = iter.next();
			if (!(t == tmp.data || t != null && t.equals(tmp.data)))
				return false;
		}
		if (iter.hasNext())
			return false;
		return true;
	} // equals

	@Override
	public Iterator<E> iterator() {
		return new AdaptiveListIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return new AdaptiveListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int pos) {
		checkIndex2(pos);
		return new AdaptiveListIterator(pos);
	}

	// Adopted from the List<E> interface.
	@Override
	public int hashCode() {
		if (!linkedUTD)
			updateLinked();
		int hashCode = 1;
		for (E e : this)
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
		return hashCode;
	}

	// You should use the toString*() methods to see if your code works as
	// expected.
	@Override
	public String toString() {
		String eol = System.getProperty("line.separator");
		return toStringArray() + eol + toStringLinked();
	}

	public String toStringArray() {
		String eol = System.getProperty("line.separator");
		StringBuilder strb = new StringBuilder();
		strb.append("A sequence of items from the most recent array:" + eol);
		strb.append('[');
		if (theArray != null)
			for (int j = 0; j < theArray.length;) {
				if (theArray[j] != null)
					strb.append(theArray[j].toString());
				else
					strb.append("-");
				j++;
				if (j < theArray.length)
					strb.append(", ");
			}
		strb.append(']');
		return strb.toString();
	}

	public String toStringLinked() {
		return toStringLinked(null);
	}

	// iter can be null.
	public String toStringLinked(ListIterator<E> iter) {
		int cnt = 0;
		int loc = iter == null ? -1 : iter.nextIndex();

		String eol = System.getProperty("line.separator");
		StringBuilder strb = new StringBuilder();
		strb.append(
				"A sequence of items from the most recent linked list:" + eol);
		strb.append('(');
		for (ListNode cur = head.link; cur != tail;) {
			if (cur.data != null) {
				if (loc == cnt) {
					strb.append("| ");
					loc = -1;
				}
				strb.append(cur.data.toString());
				cnt++;

				if (loc == numItems && cnt == numItems) {
					strb.append(" |");
					loc = -1;
				}
			} else
				strb.append("-");

			cur = cur.link;
			if (cur != tail)
				strb.append(", ");
		}
		strb.append(')');
		return strb.toString();
	}
}
