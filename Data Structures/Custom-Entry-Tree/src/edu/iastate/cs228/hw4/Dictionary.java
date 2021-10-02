package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author James Taylor
 * 
 *         An application class
 */
public class Dictionary {
	public static void main(String[] args) throws FileNotFoundException {
		
		EntryTree<Character, String> aTree = new EntryTree<>();
		
		String key; 
		Character[] keyarr = null;
		String aValue;
		File file = new File(args[0]);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()){
			String command = scan.next();		
			if(command.equals("add")){
				key = scan.next();
                keyarr = key.chars().mapToObj(c -> (char)c).toArray(Character[]::new);  
				aValue = scan.next();
				
				System.out.println("Command: " + command +" "+ key + " "+aValue);
				System.out.println("Result from an add: " + aTree.add(keyarr, aValue) + "\n");	
				
			}
			if(command.equals("prefix")){
				key = scan.next();
                keyarr = key.chars().mapToObj(c -> (char)c).toArray(Character[]::new);  
                
				System.out.println("Command: "+ command + " " + key);
				Character[] keyarray = aTree.prefix(keyarr);
				
				String keyarrS = "";
				for (int i = 0; i<keyarray.length; i++){
					keyarrS += Character.toString(keyarr[i]);
				}
				System.out.println("Result from a prefix: " + keyarrS + "\n");
			}
			if(command.equals("search")){
				key = scan.next();
				keyarr = key.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
				
				System.out.println("Command: " + command + " " + key);
				System.out.println("Result from a search: " + aTree.search(keyarr)+ "\n");
			}
			if(command.equals("remove")){
				key = scan.next();
				keyarr = key.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
				
				System.out.println("Command: " + command + " " + key);
				System.out.println("Result from a remove: " + aTree.remove(keyarr)+ "\n");
			}
			if(command.equals("showTree")){	
				System.out.println("Command: " + command + "\n");
				System.out.println("Result from a showTree:");
				 aTree.showTree();
				 System.out.println();
				 
			}
		}
		
scan.close();
	}
}
