package com.betacom.books.be;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.bookTest.BookControllerTest;
import com.betacom.books.be.bookTest.BookServiceTest;
import com.betacom.books.be.categoryTest.CategoryControllerTest;
import com.betacom.books.be.categoryTest.CategoryServiceTest;
import com.betacom.books.be.reviewTest.ReviewControllerTest;
import com.betacom.books.be.reviewTest.ReviewImplTest;
import com.betacom.books.be.userTest.UserControllerTest;
import com.betacom.books.be.userTest.UserImplTest;


@Suite
@SelectClasses({
	BookControllerTest.class,
	BookServiceTest.class,
	CategoryControllerTest.class,
	CategoryServiceTest.class,
	UserImplTest.class,
	UserControllerTest.class,
	ReviewImplTest.class,
	ReviewControllerTest.class
	})

@SpringBootTest
class BoocksBackEndApplicationTests {

	@Test
	void contextLoads() {
	}

}
