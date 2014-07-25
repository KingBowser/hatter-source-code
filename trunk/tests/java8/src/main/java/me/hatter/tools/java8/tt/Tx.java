package me.hatter.tools.java8.tt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sun.awt.RepaintArea;
import me.hatter.tools.commons.collection.IteratorTool;
import me.hatter.tools.commons.iterator.IntegerRangeIterator;
import me.hatter.tools.commons.iterator.LongRangeIterator;
import me.hatter.tools.commons.iterator.RepeatIterator;

public class Tx {
	public static void main(String[] args) {
		IteratorTool.from(Arrays.asList("1", "2")).each((String s) -> {
			System.out.println(s);
		});
		IteratorTool.from(Arrays.asList("1", "2")).each((String s, int i) -> {
			System.out.println(i + ":::" + s);
		});

		System.out.println(IteratorTool.from(Arrays.asList("aa", "bb", "ccc"))
				.map(s -> s.length()).reduce((a, b) -> a + b));

		List<Integer> l = new ArrayList<Integer>();
		for (int i = 1; i <= 100; i++) {
			l.add(i);
		}
		Integer sum = IteratorTool.from(l).reduce((a, b) -> a + b);
		System.out.println(sum);

		Integer sum2 = IteratorTool.from(IntegerRangeIterator.from(1, 100))
				.reduce((a, b) -> a + b);
		System.out.println(sum2);

		Integer sum3 = IteratorTool.from(IntegerRangeIterator.from(100, 1))
				.reduce((a, b) -> a + b);
		System.out.println(sum3);

		System.out.println(LongRangeIterator.from(1, 10000).asIteratorTool()
				.reduce((a, b) -> a + b));

		System.out.println(RepeatIterator.from("A",3).asIteratorTool()
				.reduce((a, b) -> a + b));
	}
}
