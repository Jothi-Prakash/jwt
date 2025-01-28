package com.jwt;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class MyDynamicTest {
    
	@TestFactory
    Stream<DynamicTest> dynamicTestsFromObjects() {
        return Stream.of("Test1", "Test2", "Test3")
                .map(testName -> DynamicTest.dynamicTest(testName, () -> {
                    System.out.println("Running " + testName);
                   
                }));
    }

	

}
