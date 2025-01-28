package com.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssertionsTest {
	
	@Test
	void exampleTest() {
		
		String str = "Hello world!";
		
		Assertions.assertEquals("Hello world!", str,"String should be equal");
		Assertions.assertTrue(str.startsWith("hello"), "String should be start with 'hello'");
		Assertions.assertFalse(str.startsWith("hello"));
	}

}
