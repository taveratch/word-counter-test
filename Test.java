package test;

import static org.junit.Assert.*;
import main.OOSyllableCounter;

public class Test {
	@org.junit.Test
	public void test() {
		String word = "beauty";
		OOSyllableCounter counter = new OOSyllableCounter();
		assertEquals(2, counter.countSyllables(word));
	}
}
