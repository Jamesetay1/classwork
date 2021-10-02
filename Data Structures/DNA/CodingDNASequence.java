package edu.iastate.cs228.hw1;

/**
 * @author
 * James Taylor || COM S 228, Iowa State University || 9/8/2017         
*/

public class CodingDNASequence extends DNASequence
{
	/**
	 * Constructs a new CodingDNASequence based on cdnaarr
	 * @param cdnaarr - What we will make our CodingDNASequence based on
	 * @throws IllegalArgumentException 
     * 			If a character of cdnaarr is not a,A,c,C,g,G, or t,T
	 */
  public CodingDNASequence(char[] cdnaarr)
  {
     super(cdnaarr);
  }

  /**
   * checks the beginning of seqarr and makes sure it starts with ATG
   * @return false if it doesn't start with 'ATG'
   */
  public boolean checkStartCodon()
  {
    if (seqarr.length<3){return false;}
    if (seqarr[0] != 'A' && seqarr[0] != 'a') return false;
    if (seqarr[1] != 'T' && seqarr[1] != 't') return false;
    if (seqarr[2] != 'G' && seqarr[2] != 'g') return false;
    return true;
  }

  /**
	* The method translates the coding sequence in the character array seqarr into a protein
	* sequence by calling the private method getAminoAcid on every codon in the coding
	* sequence. The translation stops if the method getAminoAcid returns the character
	* ‘$’, which is not part of the protein sequence. Otherwise, the translation stops when
	* the end of the array seqarr is reached. The method returns the protein sequence
	* in a new character array, where the length of the protein sequence is equal to the
	* length of the array
   * @return - the translated protein sequence
   * @throws RuntimeException If there is no startCodon.
   */
  public char[] translate()
  {
    if (checkStartCodon() == false) throw new RuntimeException("No start codon"); 
    
    char[] translated = new char[seqarr.length/3];
    int count = 0, j=0;
    String codon = "";
    
    for (int i = 0; i<seqarr.length; i++){	
		codon += seqarr[i];
		count++;
		if (count == 3 || (seqarr.length%3 == 1 && count == 1 && seqarr.length-i<3) || (seqarr.length%3 == 2 && count ==2 && seqarr.length-i<3)){ //(if full or if 2 left or if 1 left)
			if (getAminoAcid(codon)=='$'){break;}
			translated[j] = getAminoAcid(codon);
			j++;
			count = 0;
			codon = "";
		}
		
		
	}
    
    //Trimming due and extra " " at end of everything
   String trimming = new String(translated);
   trimming = trimming.trim();
	  return trimming.toCharArray();
  }

  /**
   * If the coding is an amino acid, return that based on what amino acid.
   * Otherwise return $.
   * @param codon
   * @return
   */
  private char getAminoAcid(String codon)
  {
    if ( codon == null ) return '$';
    char aa = '$';
    switch ( codon.toUpperCase() )
    {
      case "AAA": aa = 'K'; break;
      case "AAC": aa = 'N'; break;
      case "AAG": aa = 'K'; break;
      case "AAT": aa = 'N'; break;

      case "ACA": aa = 'T'; break;
      case "ACC": aa = 'T'; break;
      case "ACG": aa = 'T'; break;
      case "ACT": aa = 'T'; break;

      case "AGA": aa = 'R'; break;
      case "AGC": aa = 'S'; break;
      case "AGG": aa = 'R'; break;
      case "AGT": aa = 'S'; break;

      case "ATA": aa = 'I'; break;
      case "ATC": aa = 'I'; break;
      case "ATG": aa = 'M'; break;
      case "ATT": aa = 'I'; break;

      case "CAA": aa = 'Q'; break;
      case "CAC": aa = 'H'; break;
      case "CAG": aa = 'Q'; break;
      case "CAT": aa = 'H'; break;

      case "CCA": aa = 'P'; break;
      case "CCC": aa = 'P'; break;
      case "CCG": aa = 'P'; break;
      case "CCT": aa = 'P'; break;

      case "CGA": aa = 'R'; break;
      case "CGC": aa = 'R'; break;
      case "CGG": aa = 'R'; break;
      case "CGT": aa = 'R'; break;

      case "CTA": aa = 'L'; break;
      case "CTC": aa = 'L'; break;
      case "CTG": aa = 'L'; break;
      case "CTT": aa = 'L'; break;

      case "GAA": aa = 'E'; break;
      case "GAC": aa = 'D'; break;
      case "GAG": aa = 'E'; break;
      case "GAT": aa = 'D'; break;

      case "GCA": aa = 'A'; break;
      case "GCC": aa = 'A'; break;
      case "GCG": aa = 'A'; break;
      case "GCT": aa = 'A'; break;

      case "GGA": aa = 'G'; break;
      case "GGC": aa = 'G'; break;
      case "GGG": aa = 'G'; break;
      case "GGT": aa = 'G'; break;

      case "GTA": aa = 'V'; break;
      case "GTC": aa = 'V'; break;
      case "GTG": aa = 'V'; break;
      case "GTT": aa = 'V'; break;

      case "TAA": aa = '$'; break;
      case "TAC": aa = 'Y'; break;
      case "TAG": aa = '$'; break;
      case "TAT": aa = 'Y'; break;

      case "TCA": aa = 'S'; break;
      case "TCC": aa = 'S'; break;
      case "TCG": aa = 'S'; break;
      case "TCT": aa = 'S'; break;

      case "TGA": aa = '$'; break;
      case "TGC": aa = 'C'; break;
      case "TGG": aa = 'W'; break;
      case "TGT": aa = 'C'; break;

      case "TTA": aa = 'L'; break;
      case "TTC": aa = 'F'; break;
      case "TTG": aa = 'L'; break;
      case "TTT": aa = 'F'; break;
      default:    aa = '$'; break;
    }
    return aa;
  }
}
