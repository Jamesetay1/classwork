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
public class CodingDNASequenceTest {

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
	public void testCodingDNASequence() {
		System.out.println("\t\t\t\tWE ARE TESTING");
		
		//-----------------------------------------------------------------------------------------------------
		
		
		//Total checkStartCodon() Test
		String Cdemodna7 = new String("ATG"); 
		CodingDNASequence Cdemo7 = new CodingDNASequence(Cdemodna7.toCharArray());
		assertEquals(Cdemo7.checkStartCodon(), true);
		
		String Cdemodna8 = new String("ATA"); 
		CodingDNASequence Cdemo8 = new CodingDNASequence(Cdemodna8.toCharArray());
		assertEquals(Cdemo8.checkStartCodon(), false);
		
		String Cdemodna9 = new String("AGT"); 
		CodingDNASequence Cdemo9 = new CodingDNASequence(Cdemodna9.toCharArray());
		assertEquals(Cdemo9.checkStartCodon(), false);
		
		String Cdemodna10 = new String("CTG"); 
		CodingDNASequence Cdemo10 = new CodingDNASequence(Cdemodna10.toCharArray());
		assertEquals(Cdemo10.checkStartCodon(), false);

///--------------------------------------------------------------------------------------------		
		
		
		
		//Checking what is translated, and where it stops
		String Cdemodna = new String("ATGGATTAC"); //SHOULD WORK
		CodingDNASequence Cdemo = new CodingDNASequence(Cdemodna.toCharArray());
		String testing = new String("MDY");
		assertArrayEquals(Cdemo.translate(), testing.toCharArray());

		String Cdemodna3 = new String("ATGTAATAC"); //SHOULD STOP AFTER FIRST. TAA CODON is $
		CodingDNASequence Cdemo3 = new CodingDNASequence(Cdemodna3.toCharArray());
		String testing3 = new String("M");
		assertArrayEquals(Cdemo3.translate(), testing3.toCharArray());
		
		String Cdemodna4 = new String("ATGTACTAA"); //SHOULD STOP AFTER SECOND. TAA CODON is $
		CodingDNASequence Cdemo4 = new CodingDNASequence(Cdemodna4.toCharArray());
		String testing4 = new String("MY");
		assertArrayEquals(Cdemo4.translate(), testing4.toCharArray());
		
		String Cdemodna5 = new String("ATGTACTAG"); 
		CodingDNASequence Cdemo5 = new CodingDNASequence(Cdemodna5.toCharArray());
		String testing5 = new String("MY");
		assertArrayEquals(Cdemo5.translate(), testing5.toCharArray());
		
		//-----------------------------------------------------------------------------------------------------		
		
		
		//Complete Test
		String Cdemodna6 = new String("ATGCCTCAATAG"); 
		CodingDNASequence Cdemo6 = new CodingDNASequence(Cdemodna6.toCharArray());
		assertEquals(Cdemo6.checkStartCodon(), true);
		String testing6 = new String("MPQ");
		assertArrayEquals(Cdemo6.translate(), testing6.toCharArray());
		assertFalse(Arrays.equals(Cdemo6.translate(), "MPQ ".toCharArray()));
	
		//----------------------------------------------------------------------------------------
		
		
		//Exception Testing
		try{
			String Cdemodna12 = new String("ATATACTAG"); //Throws these exceptions because of the !
			CodingDNASequence Cdemo12 = new CodingDNASequence(Cdemodna12.toCharArray());
			Cdemo12.translate();
			}catch(RuntimeException e){
				assertEquals(e.getClass(),RuntimeException.class); 
				assertEquals(e.getMessage(), "No start codon");
				assertNotEquals(e.getClass(),IllegalStateException.class); 
				assertNotEquals(e.getMessage(), "FancyMessage");
			}
	}
	

}
