package com.jwt;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ParameterizedTestEx {
	
	@ParameterizedTest
	@ValueSource(ints = {1,2,3,4,5})	
	void testSquare(int value) {
			Assertions.assertEquals(value*value, square(value));
	}
	private int square(int number) {
	    return number * number;
	}
	
	@ParameterizedTest
	@CsvSource({ "Jane, Doe, F, 1990-05-20", "John, Doe, M, 1985-03-15" })
	void testPerson(String firstName, String lastName, String gender, String birthdate) {
	  Assertions.assertNotNull(firstName);
	  Assertions.assertNotNull(lastName);
	  Assertions.assertTrue(birthdate.contains("-"));
	}

	@ParameterizedTest
	@MethodSource("stringProvider")
	void testWithMethodSource(String input) {
	    Assertions.assertNotNull(input);
	}

	static Stream<String> stringProvider() {
	    return Stream.of("apple", "banana", "cherry");
	}
	




}
