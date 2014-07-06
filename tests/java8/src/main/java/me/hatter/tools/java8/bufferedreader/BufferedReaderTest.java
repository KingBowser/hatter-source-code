package me.hatter.tools.java8.bufferedreader;

import java.io.BufferedReader;

public class BufferedReaderTest {

	public static void main(String[] args) {
		new BufferedReader(null).lines().forEach((String line) -> {
			System.out.println(line);
		});
	}
}
