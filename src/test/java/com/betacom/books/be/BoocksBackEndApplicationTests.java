package com.betacom.books.be;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.OrderItem.OrderItemControllerTest;
import com.betacom.books.be.OrderItem.OrderItemServiceTest;
import com.betacom.books.be.Publisher.PublisherControllerTest;
import com.betacom.books.be.Publisher.PublisherServiceTest;
import com.betacom.books.be.bookTest.BookControllerTest;
import com.betacom.books.be.bookTest.BookServiceTest;
import com.betacom.books.be.categoryTest.CategoryControllerTest;
import com.betacom.books.be.categoryTest.CategoryServiceTest;
import com.betacom.books.be.controller.PublisherController;


@Suite
@SelectClasses({
	BookControllerTest.class,
	BookServiceTest.class,
	CategoryControllerTest.class,
	CategoryServiceTest.class,
	OrderItemControllerTest.class,
	PublisherControllerTest.class,
	PublisherServiceTest.class
	
	})

@SpringBootTest
class BoocksBackEndApplicationTests {

	@Test
	void contextLoads() {
	}

}
