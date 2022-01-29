package edu.iastate.cs228.hw3;

public class Main {

	public static void main(String[] args) {
		AdaptiveList<String> seq = new AdaptiveList<String>(); // code
		seq.add("B");
		seq.add("A");
		seq.add("C");
		System.out.println("After the three seq.add() operations:");
		System.out.println("linkedUTD: " + seq.getlinkedUTD());
		System.out.println("arrayUTD: " + seq.getarrayUTD());
		System.out.println(seq.toString()); // show both linked list and array
		
		System.out.println( seq.get(1) );
		System.out.println("After the seq.get(1) operation:");
		System.out.println("linkedUTD: " + seq.getlinkedUTD());
		System.out.println("arrayUTD: " + seq.getarrayUTD());
		System.out.println(seq.toString());
		System.out.println( seq.set(1, "D") );
		System.out.println("After the seq.set(1, ’D’) operation:");
		System.out.println("linkedUTD: " + seq.getlinkedUTD());
		System.out.println("arrayUTD: " + seq.getarrayUTD());
		System.out.println(seq.toString());
		seq.add("E");
		System.out.println("After the seq.add(’E’) operation:");
		System.out.println("linkedUTD: " + seq.getlinkedUTD());
		System.out.println("arrayUTD: " + seq.getarrayUTD());
		System.out.println(seq.toString());
		System.out.println("Any change in array: " + seq.reverse());
		System.out.println("linkedUTD: " + seq.getlinkedUTD());
		System.out.println("arrayUTD: " + seq.getarrayUTD());
		System.out.println(seq.toString());


	}

}
