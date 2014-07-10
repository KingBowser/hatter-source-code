package me.hatter.tools.java8.tt;

import java.util.Arrays;

import me.hatter.tools.commons.collection.IteratorTool;

public class Tx {
	public static void main(String[] args) {
		new IteratorTool<String>(Arrays.asList("1", "2")).each((String s) -> {
			System.out.println(s);
		});
		new IteratorTool<String>(Arrays.asList("1", "2"))
				.each((String s, int i) -> {
					System.out.println(i + ":::" + s);
				});
	}
}
