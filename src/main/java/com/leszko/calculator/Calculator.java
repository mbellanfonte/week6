package com.leszko.calculator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/** 
 * This is the Calculator public class to perform calculations!! 
 */
@Service
public class Calculator {
        final static int UMLNUMBER1 = 3;
	@Cacheable("sum")
	public int sum(int a, int b) {
		return a + b;
	}
}
