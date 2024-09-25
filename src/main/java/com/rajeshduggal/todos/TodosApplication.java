package com.rajeshduggal.todos;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodosApplication.class, args);
	}

}

record Todo(Integer id, String title) {
}

class Todos {
	private static final AtomicInteger id = new AtomicInteger(0);

	static Todo todo(String title) {
		return new Todo(id.incrementAndGet(), title);
	}
}
