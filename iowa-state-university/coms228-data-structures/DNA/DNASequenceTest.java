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
public class DNASequenceTest {

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
	public void testDNASequence(){
		System.out.println("\t\t\t\tWE ARE TESTING");
		
		//-----------------------------------------------------------------------------------------------------
		
	
		//Testing reverseComplement() and getReverseCompSequence()
		String DNAdemodna = new String("TCAGAT");
		DNASequence DNAdemo = new DNASequence(DNAdemodna.toCharArray());
		char[] reverseArray = DNAdemo.getReverseCompSeq();
		char[] reverseArrayProbably = {'A','T','C','T','G','A'};
		assertArrayEquals(reverseArray,reverseArrayProbably);
		assertFalse(Arrays.equals(reverseArray, DNAdemo.seqarr));
		assertFalse(Arrays.equals(reverseArrayProbably, DNAdemo.seqarr));
		DNAdemo.reverseComplement();
		assertArrayEquals(reverseArray, DNAdemo.seqarr);
		assertArrayEquals(reverseArrayProbably, DNAdemo.seqarr);
		
		//-----------------------------------------------------------------------------------------------------
		
		//Testing isValid
		assertTrue(DNAdemo.isValidLetter('a'));
		assertTrue(DNAdemo.isValidLetter('A'));
		assertTrue(DNAdemo.isValidLetter('c'));
		assertTrue(DNAdemo.isValidLetter('C'));
		assertTrue(DNAdemo.isValidLetter('g'));
		assertTrue(DNAdemo.isValidLetter('G'));
		assertTrue(DNAdemo.isValidLetter('t'));
		assertTrue(DNAdemo.isValidLetter('T'));
		assertFalse(DNAdemo.isValidLetter('b'));
		assertFalse(DNAdemo.isValidLetter('q'));
		assertFalse(DNAdemo.isValidLetter('!'));
		assertFalse(DNAdemo.isValidLetter('F'));
		
		//-----------------------------------------------------------------------------------------------------
		
		//Exception Testing
		 try{
			String DNAdemodna2 = new String("ATAbTATAC"); //Throws these exceptions because of the !
			DNASequence DNAdemo2 = new DNASequence(DNAdemodna2.toCharArray());
			}catch(IllegalArgumentException e){
				assertEquals(e.getClass(),IllegalArgumentException.class); 
				assertEquals(e.getMessage(), "Invalid sequence letter for class edu.iastate.cs228.hw1.DNASequence");
				assertNotEquals(e.getClass(),IllegalStateException.class); 
				assertNotEquals(e.getMessage(), "FancyMessage");
				}
		}
}
		
		  

