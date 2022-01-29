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
public class GenomicDNASequenceTest {

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
	public void testGenomicDNASequence() {
		System.out.println("\t\t\t\tWE ARE TESTING");
		//-----------------------------------------------------------------------------------------------------		
		//testing Markcoding with example from spec
		String Gdemodna2 = new String("AATGCCAGTCAGCATAGCGTAAATGCCAGTCAGGCGCTAATATCGTCAGCATAGCCGAAGG");
		GenomicDNASequence Gdemo2 = new GenomicDNASequence(Gdemodna2.toCharArray());
		assertEquals(Gdemo2.seqLength(), 61);
		Gdemo2.markCoding(55,10);
		boolean arr2[] = new boolean[] {false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false};
		assertArrayEquals(Gdemo2.iscoding, arr2);
		boolean arr2b[] = new boolean[] {false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false};
		assertFalse(Arrays.equals(Gdemo2.iscoding, arr2b));
			
//-----------------------------------------------------------------------------------------------------
		
		//Testing all with second example from Spec
		String Gdemodna = new String("AATGCCAGTCAGCATAGCGTA");
		GenomicDNASequence Gdemo = new GenomicDNASequence(Gdemodna.toCharArray());
		Gdemo.markCoding(1,16);
		boolean arr[] = new boolean[] {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false};
		assertArrayEquals(Gdemo.iscoding, arr);
		boolean arrb[] = new boolean[] {false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false};
		assertFalse(Arrays.equals(Gdemo.iscoding, arrb));
		
		//Now to extract Exons
		char concat[] = new char[] {'A', 'T', 'G', 'C', 'C', 'T', 'C', 'A', 'A', 'T', 'A', 'G'};
		int exonpairs[] = new int[] {1,5,8,10,13,16};
		assertArrayEquals(Gdemo.extractExons(exonpairs), concat);
		
//-----------------------------------------------------------------------------------------------------
		
		//Trying False Cases and different exonpairs
	
		char concat2[] = new char[] {'A', 'T', 'A', 'C', 'C', 'T', 'C', 'A', 'A', 'T', 'A', 'G'};
		assertFalse(Arrays.equals(Gdemo.extractExons(exonpairs), concat2));
		int exonpairs2[] = new int[] {1,3,8,10,13,16};
		assertFalse(Arrays.equals(Gdemo.extractExons(exonpairs2), concat));
		
		char concat3[] = new char[] {'A', 'T', 'G', 'T', 'C', 'A', 'A', 'T', 'A', 'G'};
		assertArrayEquals(Gdemo.extractExons(exonpairs2), concat3);
		
		int exonpairs4[] = new int[] {1,3,8,10};
		char concat4[] = new char[] {'A', 'T', 'G', 'T', 'C', 'A'};
		assertArrayEquals(Gdemo.extractExons(exonpairs4), concat4);
		
		int exonpairs5[] = new int[] {8,10};
		char concat5[] = new char[] { 'T', 'C', 'A'};
		assertArrayEquals(Gdemo.extractExons(exonpairs5), concat5);
				
		
//------------------------------------------------------------------------------		
		

		
		//TESTING EXCEPTIONS
		String Gdemodna3 = new String("AATGCCAGTCAGCATAGCGTA");
		GenomicDNASequence Gdemo3 = new GenomicDNASequence(Gdemodna3.toCharArray());
		Gdemo3.markCoding(1,16);
		
		try{//first < 0			
			Gdemo3.markCoding(-1, 6);
			}catch(IllegalArgumentException e){
				assertEquals(e.getClass(),IllegalArgumentException.class); 
				assertEquals(e.getMessage(), "Coding border is out of bounds");
				}
		try{//last <0
			Gdemo3.markCoding(7, -5);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Coding border is out of bounds");
			}
		try{//first >= slen (= in this case)
			Gdemo3.markCoding(2, 21);  //Will throw since Gdemo3 length is 20.
			assertEquals(Gdemodna3.length(),21);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Coding border is out of bounds");
			}
		try{//last >= slen (> in this case)
			Gdemo3.markCoding(14,40);  //Will throw since Gdemo3 length is 20.
			assertEquals(Gdemodna3.length(),21);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Coding border is out of bounds");
			}
		
		try{//Empty Exon pairs!
			int exonpairs3[] = new int[] {};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Empty array or odd number of array elements");
			}
		try{//Odd Exon pairs!
			int exonpairs3[] = new int[] {1};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Empty array or odd number of array elements");
			}
		try{//Odd Exon pairs!
			int exonpairs3[] = new int[] {1, 5 , 7};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Empty array or odd number of array elements");
			}
		try{//First is Out of Bounds
			int exonpairs3[] = new int[] {-1, 5};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Exon position is out of bound");
			}
		try{//Last is out of bounds
			int exonpairs3[] = new int[] {1, 22};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Exon position is out of bound");
			}
		try{//out of order
			int exonpairs3[] = new int[] {1, 5, 3, 6};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalArgumentException e){
			assertEquals(e.getClass(),IllegalArgumentException.class); 
			assertEquals(e.getMessage(), "Exon positions are out of order");
			}
		try{//first is non coding
			int exonpairs3[] = new int[] {0, 3, 14, 15};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalStateException e){
			assertEquals(e.getClass(),IllegalStateException.class); 
			assertEquals(e.getMessage(), "Noncoding position found");
			}
		try{//first is non coding
			int exonpairs3[] = new int[] {2, 3, 14, 19};
			Gdemo3.extractExons(exonpairs3);
			}catch(IllegalStateException e){
			assertEquals(e.getClass(),IllegalStateException.class); 
			assertEquals(e.getMessage(), "Noncoding position found");
			}
			
		}
	}


