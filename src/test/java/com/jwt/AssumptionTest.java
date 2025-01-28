package com.jwt;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class AssumptionTest {
	
	// assumeTrue Method
	@Test
	void testJavaVersion() {
		Assumptions.assumeTrue(System.getProperty("java.version").startsWith("17"));
		System.out.println("Test is running on java");
	}
	
	// assumeFalse Method
	@Test
	void testNotInCI() {
		Assumptions.assumeFalse("false".equals(System.getenv("CI")));
		System.out.println("Test is not running in CI");
	}
	
	//assumeThat method
	@Test
	void testWithFeatureFlag() {
        String environment = System.getProperty("env", "dev");

        Assumptions.assumingThat("test".equals(environment), () -> {
            System.out.println("Feature is enabled in the test environment");
        });

        System.out.println("This runs in all environments");
    }

}
