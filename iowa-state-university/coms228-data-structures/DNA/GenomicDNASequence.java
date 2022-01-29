package edu.iastate.cs228.hw1;

/**
 * @author
 * James Taylor || COM S 228, Iowa State University || 9/8/2017         
*/

public class GenomicDNASequence extends DNASequence
{
  public boolean[] iscoding; // made public instead of private for grading.

  /**
   * constructs a new GenomicDNASequence with gdnaar.
   *  it creates a boolean array of the same length as gdnaarr,
   *  saves its reference in the field iscoding, and sets the boolean array to false at each
   *  index.
   * @param gdnaarr The character array we will be making our new Genomic DNA Sequence out of.
   * @throws IllegalArgumentException 
   * 			If a character of gdnaar is not a,A,c,C,g,G, or t,T
   */
  public GenomicDNASequence(char[] gdnaarr)
  {
    super(gdnaarr);
    boolean[] isCodingTemp = new boolean[gdnaarr.length]; //Java automatically initalizes to false.
    iscoding = isCodingTemp;
    }
  
/**
 * if first > last, call reverse complement and transforms first and last. 
 * Regardless it sets all indexes between first and last of iscoding to true.
 * @param first Where the coding should start being marked
 * @param last Where the coding should stop being marked.
 * @throws IllegalArgumentException If first < 0 or last is <0
 * @throws IllegalArgumentException If first or last are > length of the sequence.
 */
  public void markCoding(int first, int last)
  {
    if (first <0 || last <0) throw new IllegalArgumentException("Coding border is out of bounds");
   int slen = seqLength();
    if (first >= slen || last >= slen)throw new IllegalArgumentException("Coding border is out of bounds");
    
    if (first > last){ 
    	reverseComplement();
    	first = (slen - 1 - first);
    	last = (slen - 1 - last);
    	
    }
    for (int i = first; i<=last; i++){//CHECK: Is this first through last INCLUSIVE?
		iscoding[i]=true;
	}
    }
  
/**
 *  Takes all the coding exons specified by the array exonpos, concatenates them in order, and returns
 * the resulting sequence in a new character array
 * @param exonpos where the coding exons start and end.
 * @return the concatenated list of all coding exons.
 * @throws IllegalArgumentException If exonpos length is 0 or odd
 *  @throws IllegalArgumentException If exonpos is outside of the array
 *   @throws IllegalArgumentException If exonpos is not in order form least to most
 *   @throws IllegalStateException If isCoding is false at an exonpos position
 */
  public char[] extractExons(int[] exonpos)
  {
	  if (exonpos.length == 0 || exonpos.length % 2 == 1) throw new IllegalArgumentException("Empty array or odd number of array elements");
	  
	  for (int i = 0; i<exonpos.length; i++){
	   if (exonpos[i]<0 || exonpos[i] > seqLength()) throw new IllegalArgumentException("Exon position is out of bound"); 
	  }
	  
	  for (int j = 0; j<exonpos.length-1; j++){
		  if (exonpos[j]>=exonpos[j+1]) throw new IllegalArgumentException("Exon positions are out of order");
	  }
	 
	  for (int k = 0; k <exonpos.length; k++){ 
		  if (iscoding[exonpos[k]]==false) throw new IllegalStateException("Noncoding position found");
	  }
	
	  int n = 0;
	  String filling = "";
	  String toConvert = "";
	  for (int l = 0; l<exonpos.length-1; l++){
		  char[] empty = new char[exonpos[l+1]-exonpos[l]+1];
		  for (int m = exonpos[l]; m<exonpos[l+1]+1; m++){		  
			  empty[n] = seqarr[m];
			  n++;
			  filling = new String(empty); //Takes the working array and puts it into filling
		  }
		 n=0;
		 l++;
		 toConvert += filling;
		 		 
	  }
	  return toConvert.toCharArray();
	  
  }

}
