package br.com.customredis.executable;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.customredis.model.ScoredValues;

public class TestSortedSet {
	
	public static void main(String[] args) {
		SortedSet<ScoredValues<String>> so = new TreeSet<>(Collections.reverseOrder());
		
		so.add(new ScoredValues<String>(1,"one"));
		so.add(new ScoredValues<String>(1,"uno"));
		so.add(new ScoredValues<String>(2,"two"));
		so.add(new ScoredValues<String>(3,"three"));
		
        System.out.println(so);

	}

}
