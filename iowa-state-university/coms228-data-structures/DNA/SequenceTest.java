package edu.iastate.cs228.hw1;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * @author
 * James Taylor || COM S 228, Iowa State University || 9/8/2017         
*/
public class SequenceTest {

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
		public void testSequence() {
			System.out.println("\t\t\t\tWE ARE TESTING");
			
			//-----------------------------------------------------------------------------------------------------
			
			
			//Testing .seqLength()		
			String Sdemodna = new String("AATG"); 
		    Sequence Sdemo = new Sequence(Sdemodna.toCharArray());
		    assertTrue(Sdemo.seqLength()==4);
		   
		    String Sdemodna3 = new String(""); 
		    Sequence Sdemo3 = new Sequence(Sdemodna3.toCharArray());
		    assertTrue(Sdemo3.seqLength()==0);
		    
		  //-----------------------------------------------------------------------------------------------------
		    
		    //Testing .getSeq() and .toString()
		    char[] expected = {'A','A','T','G'};
		    assertArrayEquals(expected, Sdemo.getSeq());
		    assertTrue(Sdemo.toString().equals("AATG"));
		    
		  //-----------------------------------------------------------------------------------------------------
		    
		    //Testing .equals()
		    Sequence Sdemo1 = new Sequence(Sdemodna.toCharArray());
		    assertTrue(Sdemo.equals(Sdemo1));
		    String Sdemodna1 = new String("AGTG");
		    Sequence Sdemo2 = new Sequence(Sdemodna1.toCharArray());
		    assertFalse(Sdemo.equals(Sdemo2));
		    String Sdemodna4 = new String("AATGAAGT");
		    Sequence Sdemo4 = new Sequence(Sdemodna4.toCharArray());
		    assertFalse(Sdemo.equals(Sdemo4));
		    
		  //-----------------------------------------------------------------------------------------------------
		    
		    //Exception Testing
		    try{
				String Sdemodna5 = new String("AT!GATTAC"); //Throws these exceptions because of the !
				Sequence Sdemo5 = new Sequence(Sdemodna5.toCharArray());
				}catch(IllegalArgumentException e){
					assertEquals(e.getClass(),IllegalArgumentException.class); 
					assertEquals(e.getMessage(), "Invalid sequence letter for class edu.iastate.cs228.hw1.Sequence");
					assertNotEquals(e.getClass(),IllegalStateException.class); 
					assertNotEquals(e.getMessage(), "FancyMessage");
				}
		}
		
		
	}


