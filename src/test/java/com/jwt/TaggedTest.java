package com.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TaggedTest {
	
	@Test
	@Tag("fast")
	void fastTest() {
		Assertions.assertTrue(true,"This is a fast test");
	}
	
	@Test
	@Tag("slow")
	void slowTest() {
		Assertions.assertTrue(true, "this is a slow test");
	}

}
