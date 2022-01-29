package edu.iastate.cs228.hw4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
/**
 * 
 * @author James Taylor
 * 	
 * 	        JUnit Tests for EntryTree
 *
 */
public class EntryTreeTest {

     //Test for all parts of search BUT exception
	@Test
	public void testSearch() {
		EntryTree<Character, String>  SearchTree =  new EntryTree<>(); 

		//General Test
		Character[] keyEDIT = {'E', 'D', 'I', 'T'};
		Character[] keyEDITED = {'E', 'D', 'I', 'T', 'E', 'D'};
		Character[] keyEDICT = {'E', 'D', 'I', 'C', 'T'};
		Character[] keyEDITOR = {'E', 'D', 'I', 'T', 'O', 'R'};
		Character[] keyNULL = null;
		Character[] keyEMPTY = {};
		
		SearchTree.add(keyEDIT, "Change");
		assertEquals("Change", SearchTree.search(keyEDIT));
		assertNotEquals("change", SearchTree.search(keyEDIT));
		
		assertEquals(null, SearchTree.search(keyEDITED));
		SearchTree.add(keyEDITED, "Changed");
		assertEquals("Changed", SearchTree.search(keyEDITED));
		
		assertEquals(null, SearchTree.search(keyEDICT));
		SearchTree.add(keyEDITOR, "One who changes");
		assertEquals("One who changes", SearchTree.search(keyEDITOR));
		
		assertEquals(null, SearchTree.search(keyNULL));
		assertNotEquals("Change", SearchTree.search(keyNULL));
		assertEquals(null, SearchTree.search(keyEMPTY));
	}
	
	//Testing exception throwing for search
	@Test(expected = NullPointerException.class)
	public void testSearchException(){
		EntryTree<Character, String>  SearchTree =  new EntryTree<>(); 

		Character[] keyEDITNull = {'E', 'D', null, 'T'};
		SearchTree.search(keyEDITNull);
	}
	
	//Testing The Prefix Method
	@Test
	public void testPrefix(){
		EntryTree<Character, String>  PrefixTree =  new EntryTree<>(); 

		//General Test
		Character[] keyEDIT = {'E', 'D', 'I', 'T'};
		Character[] keyEDITED = {'E', 'D', 'I', 'T', 'E', 'D'};
		Character[] keyEDICT = {'E', 'D', 'I', 'C', 'T'};
		Character[] keyEDITOR = {'E', 'D', 'I', 'T', 'O', 'R'};
		Character[] keyEDITORIAL = {'E', 'D', 'I', 'T', 'O', 'R', 'I', 'A', 'L'};
		Character[] keyNULL = null;
		Character[] keyEMPTY = {};
		
		//Testing with EDI, EDIT, and EDICT
		PrefixTree.add(keyEDIT, "To Change");
		Character[] keyEDI = {'E', 'D', 'I'};
		assertArrayEquals(keyEDI, PrefixTree.prefix(keyEDI));
		assertArrayEquals(keyEDI, PrefixTree.prefix(keyEDICT));
		assertFalse(Arrays.equals(keyEDIT, PrefixTree.prefix(keyEDI)));
		assertFalse(Arrays.equals(keyEDIT, PrefixTree.prefix(keyEDICT)));
	
		//Some Siblings/Children
		PrefixTree.add(keyEDITED, "Changed");
		PrefixTree.add(keyEDICT, "An Order");
		PrefixTree.add(keyEDITOR, "A Changer");
		
		assertArrayEquals(keyEDITOR, PrefixTree.prefix(keyEDITORIAL));
		assertFalse(Arrays.equals(keyEDITORIAL, PrefixTree.prefix(keyEDITORIAL)));
		
		//But if we add editorial...
		PrefixTree.add(keyEDITORIAL, "Opinion");
		
		assertArrayEquals(keyEDITORIAL, PrefixTree.prefix(keyEDITORIAL));
		assertFalse(Arrays.equals(keyEDITOR, PrefixTree.prefix(keyEDITORIAL)));
		
		//Null Cases:
		Character[] keyMOOSE = {'M', 'O', 'O', 'S', 'E'};
		assertArrayEquals(null, PrefixTree.prefix(keyMOOSE));
		Character[] keyDIT = {'D', 'I', 'T'};
		assertArrayEquals(null, PrefixTree.prefix(keyDIT));
		assertFalse(Arrays.equals(keyDIT, PrefixTree.prefix(keyDIT)));
		
		
		assertArrayEquals(null, PrefixTree.prefix(keyNULL));
		assertArrayEquals(null, PrefixTree.prefix(keyEMPTY));
		 
	}
	//Testing exception throwing for search
	@Test(expected = NullPointerException.class)
	public void testPrefixException(){
		EntryTree<Character, String>  PrefixTree =  new EntryTree<>(); 
		
		Character[] keyEDITNull = {'E', 'D', null, 'T'};
		PrefixTree.prefix(keyEDITNull);
	}
	
	@Test
	public void testAdd(){
		EntryTree<Character, String>  AddTree =  new EntryTree<>(); 
		
		
		Character[] keyE = {'E'};
		Character[] keyEDU = {'E', 'D', 'U'};
		Character[] keyEDIT = {'E', 'D', 'I', 'T'};
		Character[] keyNULL = null;
		Character[] keyEMPTY = {};
		
		assertEquals(null, AddTree.root.child);
		
		AddTree.add(keyE, "The Letter E");
		assertNotEquals(null, AddTree.root.child);
		assertEquals((Character) 'E', AddTree.root.child.key);
		assertEquals((String) "The Letter E", AddTree.root.child.value);
		assertEquals(AddTree.root, AddTree.root.child.parent);
		assertEquals(null, AddTree.root.child.child);
		assertEquals(null, AddTree.root.child.next);
		assertEquals(null, AddTree.root.child.prev);
		
		
		
		AddTree.add(keyEDU, "Education");
		assertNotEquals(null, AddTree.root.child.child);
		assertEquals((Character) 'U', AddTree.root.child.child.child.key);
		assertEquals((String) "Education", AddTree.root.child.child.child.value);
		assertNotEquals((String) "The Letter E", AddTree.root.child.child.child.value);
		
		assertEquals(null, AddTree.root.child.child.child.next);
		AddTree.add(keyEDIT, "To Change");
		assertNotEquals(null, AddTree.root.child.child.child.next);
		assertEquals((Character) 'I', AddTree.root.child.child.child.next.key);
		assertEquals((String) null, AddTree.root.child.child.child.next.value);
		
		assertEquals((Character) 'T', AddTree.root.child.child.child.next.child.key);
		assertEquals((String) "To Change", AddTree.root.child.child.child.next.child.value);
		
		//Replacing a Value
		AddTree.add(keyE, "Re Do");
		assertEquals((String) "Re Do", AddTree.root.child.value);
		assertNotEquals((String) "The Letter E", AddTree.root.child.value);
		
		//Boolean Result Testing
		Character[] keyEDI = {'E', 'D', 'I'};
		assertTrue(AddTree.add(keyEDI, "Something"));
		assertTrue(AddTree.add(keyEDIT, "Change it up"));
		
	
		assertFalse(AddTree.add(keyEMPTY, "Its Empty"));
		assertFalse(AddTree.add(keyEMPTY, null));
		assertFalse(AddTree.add(keyNULL, "Its Null"));
		
	}
	
	//Testing exception throwing for search
		@Test(expected = NullPointerException.class)
		public void testAddException(){
			EntryTree<Character, String>  AddTree =  new EntryTree<>(); 
		
			Character[] keyEDITNull = {'E', 'D', null, 'T'};
			AddTree.add(keyEDITNull, "broke");
		}
		
		//Testing The Remove Method
		@Test
		public void testRemove(){
			EntryTree<Character, String>  RemoveTree =  new EntryTree<>(); 

			//General Test
			Character[] keyEDIT = {'E', 'D', 'I', 'T'};
			Character[] keyEDITED = {'E', 'D', 'I', 'T', 'E', 'D'};
			Character[] keyEDICT = {'E', 'D', 'I', 'C', 'T'};
			Character[] keyNULL = null;
			Character[] keyEMPTY = {};
			
			RemoveTree.add(keyEDIT, "To Change");
			RemoveTree.remove(keyEDIT);
			assertEquals(null, RemoveTree.root.child);
			
			RemoveTree.add(keyEDIT, "To Change");
			RemoveTree.add(keyEDITED, "Changed");
			assertEquals((String) "To Change", RemoveTree.root.child.child.child.child.value);
			RemoveTree.remove(keyEDIT);//Makes value at T null.
			assertEquals((Character) 'T', RemoveTree.root.child.child.child.child.key);
			assertEquals(null, RemoveTree.root.child.child.child.child.value);
			assertNotEquals(null, RemoveTree.root.child);
			RemoveTree.remove(keyEDITED);//Clears whole Tree
			assertEquals(null, RemoveTree.root.child);
			
			//Null Cases
			RemoveTree.add(keyEDIT, "To Change");
			assertEquals(null, RemoveTree.remove(keyNULL));
			assertEquals(null, RemoveTree.remove(keyEMPTY));
			assertEquals(null, RemoveTree.remove(keyEDICT));
			
			//Return Values
			assertEquals((String) "To Change", RemoveTree.remove(keyEDIT));	
			
		}
		
		//Testing exception throwing for search
		@Test(expected = NullPointerException.class)
		public void testRemoveException(){
			EntryTree<Character, String>  RemoveTree =  new EntryTree<>(); 
		
			Character[] keyEDIT = {'E', 'D', 'I', 'T'};
			Character[] keyEDITNull = {'E', 'D', null, 'T'};
			RemoveTree.add(keyEDIT, "To Change");
			RemoveTree.remove(keyEDITNull);
		}
		
		
}

