package com.betacom.books.be;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.bookTest.BookControllerTest;


@Suite
@SelectClasses({
	BookControllerTest.class
	})

@SpringBootTest
class BoocksBackEndApplicationTests {

	@Test
	void contextLoads() {
	}

}
