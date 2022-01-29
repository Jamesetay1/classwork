package edu.iastate.cs228.hw1;

/**
 * @author
 * James Taylor || COM S 228, Iowa State University || 9/8/2017         
*/

public class DNASequence extends Sequence
{
	/**
	 * constructs a new DNASequence given dnletrr
	 * @param dnletrr - What we will make our DNASequence of.
	 * @throws IllegalArgumentException 
     * 			If a character of dnletrr is not a,A,c,C,g,G, or t,T
	 */
  public DNASequence(char[] dnletrr)
  {
    super(dnletrr);
  }

  /**
   * Checks to make sure that let is a,A,c,C,g,G or t,T
   * @param let - The letter we are checking to see if it is valid
   * @return If it is one of those characters or not (bool)
   * 
   */
  @Override
  public boolean isValidLetter(char let)
  {
	  if ((let == 'a')||(let == 'A')||
		  (let == 'c')||(let == 'C')||
		  (let == 'g')||(let == 'G')||
		  (let == 't')||(let == 'T')){return true;}
	  return false;
  }

  /**
   * reverses the sequence and then gets the complements, return the reverse complement of seqarr
   * @return the reverse complement of seqarr
   */
  public char[] getReverseCompSeq()
  {
	  int i = seqarr.length - 1, j = 0, k=0;
	  char[] rev = new char[seqarr.length];
	  while(i >= 0){
	       rev[j] = seqarr[i];
	       i--;
	       j++;
	  }
	  
	  char[] compRev = new char[rev.length];
	 
	  for (k=0; k<compRev.length; k++){
			if (Character.toUpperCase(rev[k])==('C')) compRev[k]='G';
			if (Character.toUpperCase(rev[k])==('G')) compRev[k]='C';
			if (Character.toUpperCase(rev[k])==('A')) compRev[k]='T';
			if (Character.toUpperCase(rev[k])==('T')) compRev[k]='A';
		};	
		return compRev;
  }
 /**
  * save the reverse complement in seqarr.
  */
  public void reverseComplement(){
	  char[] compRev = getReverseCompSeq();
	  seqarr = compRev;  
  }
}
