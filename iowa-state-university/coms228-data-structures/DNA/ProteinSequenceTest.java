package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author
 * James Taylor || COM S 228, Iowa State University || 9/8/2017         
*/
public class ProteinSequenceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Before Class");
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("After Class");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("\t\tBefore Test");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("\t\tAfter Test");
	}

	@Test
	public void testProteinSequence(){
		System.out.println("\t\t\t\tWE ARE TESTING");
		
		//-----------------------------------------------------------------------------------------------------
		
		
		String PSdemodna = new String("HiMate");
		ProteinSequence PSdemo = new ProteinSequence(PSdemodna.toCharArray());
		
		//First testing out Sequence methods on Protein to make sure called correctly.
		assertTrue(PSdemo.seqLength()==6);	
		char[] expected = {'H','i','M','a','t','e'};
	    assertArrayEquals(expected, PSdemo.getSeq());
	    assertTrue(PSdemo.toString().equals("HiMate"));
	    char[] notexpected = {'H','i','M','a','m','e'};
	    assertFalse(Arrays.equals(notexpected, PSdemo.getSeq()));   
	    
	  //-----------------------------------------------------------------------------------------------------
	    
	    //Then testing isValidLetter()
	    assertTrue(PSdemo.isValidLetter('v'));
	    assertTrue(PSdemo.isValidLetter('C'));
	    assertTrue(PSdemo.isValidLetter('G'));
	 
	    assertTrue(PSdemo.isValidLetter('a'));
	    assertFalse(PSdemo.isValidLetter('b'));
	    assertFalse(PSdemo.isValidLetter('B'));
	    assertFalse(PSdemo.isValidLetter('j'));
	    assertFalse(PSdemo.isValidLetter('J'));
	    assertFalse(PSdemo.isValidLetter('o'));
	    assertFalse(PSdemo.isValidLetter('O'));
	    assertFalse(PSdemo.isValidLetter('u'));
	    assertFalse(PSdemo.isValidLetter('U'));
	    assertFalse(PSdemo.isValidLetter('x'));
	    assertFalse(PSdemo.isValidLetter('X'));
	    assertFalse(PSdemo.isValidLetter('z'));
	    assertFalse(PSdemo.isValidLetter('Z'));
	    
	  //-----------------------------------------------------------------------------------------------------
	    
	    
	  //Exception Testing
	    try{
			String PSdemodna3 = new String("ATGBATTAC"); //Throws these exceptions because of the !
			ProteinSequence PSdemo3 = new ProteinSequence(PSdemodna3.toCharArray());
			}catch(IllegalArgumentException e){
				assertEquals(e.getClass(),IllegalArgumentException.class); 
				assertEquals(e.getMessage(), "Invalid sequence letter for class edu.iastate.cs228.hw1.ProteinSequence");
				assertNotEquals(e.getClass(),IllegalStateException.class); 
				assertNotEquals(e.getMessage(), "FancyMessage");
			}
	    //Exception Testing
	    try{
			String PSdemodna4 = new String("ATGATTAb"); //Throws these exceptions because of the !
			ProteinSequence PSdemo4 = new ProteinSequence(PSdemodna4.toCharArray());
			}catch(IllegalArgumentException e){
				assertEquals(e.getClass(),IllegalArgumentException.class); 
				assertEquals(e.getMessage(), "Invalid sequence letter for class edu.iastate.cs228.hw1.ProteinSequence");
				assertNotEquals(e.getClass(),IllegalStateException.class); 
				assertNotEquals(e.getMessage(), "FancyMessage");
			}
	}
}
